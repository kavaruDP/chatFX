package server;

//import sql.MainSQL;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        executorService = Executors.newCachedThreadPool();

//        Handler fileHandler = null;
//        try {
//            fileHandler = new FileHandler("logs/server.log",true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        logger.addHandler(fileHandler);
//        fileHandler.setFormatter(new SimpleFormatter());
//        logger.setUseParentHandlers(false);

        if (!SQLHandler.connect()) {
            logger.severe("Не удалось подключиться к БД");
            throw new RuntimeException("Не удалось подключиться к БД");
        }
        //Видоизмененный метод с возможностью начитывать данные из БД и обновлять в ней ник.
        authService = new SimpleAuthService();
//        authService = new DBAuthService();
        try {
            server = new ServerSocket(PORT);
            //System.out.println("server started!");
            logger.info("server started!");
            while (true) {
                socket = server.accept();
                //System.out.println("client connected " + socket.getRemoteSocketAddress());
                logger.info("client connected " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SQLHandler.disconnect();
            //System.out.println("server closed");
            logger.info("server closed");
            executorService.shutdown();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        String message = String.format("%s : %s", sender.getNickname(), msg);

        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[ %s ] private [ %s ] : %s", sender.getNickname(), receiver, msg);

        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                c.sendMsg(message);
                if (!c.equals(sender)) {
                    sender.sendMsg(message);
                }
                return;
            }
        }
        sender.sendMsg("Not found user: " + receiver);
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isloginAuthenticated(String login){
        for (ClientHandler c : clients) {
            if(c.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList(){
        StringBuilder sb = new StringBuilder("/clientlist ");
        for (ClientHandler c : clients) {
            sb.append(c.getNickname()).append(" ");
        }

        String msg = sb.toString();
        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }
}
