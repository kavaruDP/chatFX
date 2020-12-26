package server;

import java.io.IOException;
import java.util.logging.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class StartServer {
    private static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) throws IOException {
        Handler fileHandler = new FileHandler("logs/server.log",true);
        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new SimpleFormatter());
        new Server();
        // Для тестов
//        Handler consoleHandler = new ConsoleHandler();
//        consoleHandler.setLevel(Level.ALL);
//        consoleHandler.setFormatter(new SimpleFormatter());
//        logger.setLevel(Level.ALL);
//        logger.addHandler(consoleHandler);
//        logger.setUseParentHandlers(false);
//        LogManager manager = LogManager.getLogManager();
//        manager.readConfiguration(new FileInputStream("logging.properties"));
    }
}
