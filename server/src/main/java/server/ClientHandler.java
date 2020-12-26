package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {

//        Handler fileHandler = null;
//        try {
//            fileHandler = new FileHandler("logs/server.log",true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        logger.addHandler(fileHandler);
//        fileHandler.setFormatter(new SimpleFormatter());
//        logger.setUseParentHandlers(false);

        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            //new Thread(() -> {
            server.getExecutorService().execute(() -> {
                try {
                    socket.setSoTimeout(120000);
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.startsWith("/reg ")) {
                                String[] token = str.split("\\s", 4);
                                boolean b = server.getAuthService()
                                        .registration(token[1], token[2], token[3]);
                                if (b) {
                                    sendMsg("/regok");
                                } else {
                                    sendMsg("/regno");
                                }
                            }

                            if (str.startsWith("/auth ")) {
                                String[] token = str.split("\\s", 3);
                                String newNick = server.getAuthService()
                                        .getNicknameByLoginAndPassword(token[1], token[2]);
                                if (newNick != null) {
                                    login = token[1];
                                    if (!server.isloginAuthenticated(login)) {
                                        nickname = newNick;
                                        out.writeUTF("/authok " + nickname);
                                        server.subscribe(this);
                                        socket.setSoTimeout(0);
                                        break;
                                    } else {
                                        out.writeUTF("Учетная запись уже используется");
                                    }
                                } else {
                                    out.writeUTF("Неверный логин / пароль");
                                }
                            }
                        }
                    }

                    //Цикл работы
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.startsWith("/w")) {
                                String[] token = str.split("\\s+", 3);
                                if (token.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, token[1], token[2]);
                            }
                            if (str.startsWith("/chnick ")) {

                                String[] token = str.split(" ", 2);
                                if (token.length < 2) {
                                    continue;
                                }
                                if (token[1].contains(" ")) {
                                    sendMsg("Ник не может содержать пробелов");
                                    continue;
                                }
                                if (server.getAuthService().changeNick(this.nickname, token[1])) {
                                    sendMsg("/yournickis " + token[1]);
                                    sendMsg("Ваш ник изменен на " + token[1]);
                                    //logger.info("Клиент сменил ник на " + token[1]);
                                    this.nickname = token[1];
                                    server.broadcastClientList();
                                } else {
                                    sendMsg("Не удалось изменить ник. Ник " + token[1] + " уже существует");
                                }
                            }
                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                        } else {
                            server.broadcastMsg(this, str);
                            logger.info(nickname + " отправил сообщение" );
                        }
                    }
                }catch (SocketTimeoutException e){
                    sendMsg("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Client disconnected!");
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            //}).start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
