package server;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLHandler {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psChangeNick;
    public static boolean isAuthDBmethodOK = true;
    private static List<String> users;

    public static boolean connect() {           //throws SQLException, ClassNotFoundException
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            psGetNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
            psRegistration = connection.prepareStatement("INSERT INTO users(login, password, nickname) VALUES (? ,? ,? );");
            psChangeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
            stmt = connection.createStatement();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            isAuthDBmethodOK = false;
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            isAuthDBmethodOK = false;
            e.printStackTrace();
            return false;
        }
    }

    public static String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psGetNickname.setString(1, login);
            psGetNickname.setString(2, password);
            ResultSet rs = psGetNickname.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }

    public static boolean registration(String login, String password, String nickname) {
        try {
            psRegistration.setString(1, login);
            psRegistration.setString(2, password);
            psRegistration.setString(3, nickname);
            psRegistration.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registration2(String login, String password, String nickname) {
        try {
            String sql = "INSERT INTO users(login, password, nickname) VALUES ('" + login + "','" + password + "','" + nickname + "');" ;
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changeNick(String oldNickname, String newNickname) {
        try {
            psChangeNick.setString(1, newNickname);
            psChangeNick.setString(2, oldNickname);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List getUsersFromDB() {
        users = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT login, password, nickname FROM users;");
            while (rs.next()) {
                users.add(rs.getString("login") + " " + rs.getString("password")+ " " + rs.getString("nickname"));
            }
            rs.close();
            return users;
        } catch (SQLException e) {
            return null;
        }
    }

    public static void disconnect() {
        try {
            psRegistration.close();
            psGetNickname.close();
            psChangeNick.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        connect();
        //System.out.println(getUsersFromDB().get(0));
//        try {
//            stmt.executeUpdate("INSERT INTO users (nickname, login, password) VALUES('Джон','jon','jon');");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        //System.out.println(registration2("jon6","jon","Джон6"));
        //System.out.println(getNicknameByLoginAndPassword("bob","bob"));
        //System.out.println(changeNick("Джон2","Джон3"));

    }

}
