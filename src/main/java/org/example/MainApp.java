package org.example;

import java.io.*;
import java.sql.*;

//Класс в ДЗ не используется, создан исключительно для отладки.

public class MainApp {

    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stmt = connection.createStatement();
    }

    public static void main(String[] args) {

//        try {
//            InputStreamReader in = new InputStreamReader(new FileInputStream(""), StandardCharsets.UTF_8);
//            int x;
//            while ((x= in.read()) != -1) {
//                System.out.print((char) x);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            connect();
//            //clearTable();
//            //exRollback();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            disconnect();
//        }
    }

    public static void exRollback() throws SQLException {
        stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Bob1', 65);");
        Savepoint sp1 = connection.setSavepoint();

        stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Bob2', 65);");
        connection.rollback(sp1);
        stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Bob3', 65);");
        connection.commit();
    }

    public static void fillTableBatch() throws SQLException {
        long begin = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 1; i <= 1000; i++) {
            psInsert.setString(1, "Bob" + i);
            psInsert.setInt(2, i * 15 % 100);
            psInsert.addBatch();
        }
        psInsert.executeBatch();
        connection.commit();
        long end = System.currentTimeMillis();
        System.out.printf("Time: %d ms", end - begin);
    }

    public static void fillTable() throws SQLException {
        long begin = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 1; i <= 10; i++) {
            psInsert.setString(1, "Bob" + i);
            psInsert.setInt(2, i * 15 % 100);
            psInsert.executeUpdate();
        }
        connection.commit();
        long end = System.currentTimeMillis();
        System.out.printf("Time: %d ms", end - begin);
    }

    public static void prepareStatements() throws SQLException {
        psInsert = connection.prepareStatement("INSERT INTO users (username, nickname) VALUES(?, ?);");
    }

    // CRUD create read update delete
    public void exSelect() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT username, nickname FROM users WHERE id>1;");

        while (rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getInt("score"));
        }

        rs.close();
    }

    public static void clearTable() throws SQLException {
        stmt.executeUpdate("DELETE FROM users;");
    }

    public void exDelete() throws SQLException {
        stmt.executeUpdate("DELETE FROM users WHERE id=4;");
    }

    public void exUpdate() throws SQLException {
        stmt.executeUpdate("UPDATE users SET nickname='test' WHERE id=2;");
    }

    public void exInsert1() throws SQLException {
        stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Bob4', 15), ('Bob5', 95),('Bob6', 75) ;");
    }


    public static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


}