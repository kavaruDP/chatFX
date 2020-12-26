package server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;
    private List<String> itemsDB;

    public SimpleAuthService() {
        users = new ArrayList<>();
        itemsDB = new ArrayList<>();
        if (SQLHandler.getUsersFromDB() != null) {
            itemsDB = SQLHandler.getUsersFromDB();
            for (int i = 0; i < itemsDB.size(); i++) {
                String[] token = itemsDB.get(i).split(" ", 3);
                users.add(new UserData(token[0], token[1], token[2]));
            }

        } else {
            users.add(new UserData("qwe", "qwe", "qwe"));
            users.add(new UserData("asd", "asd", "asd"));
            users.add(new UserData("zxc", "zxc", "zxc"));
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if(user.login.equals(login) && user.password.equals(password)){
                return user.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        for (UserData user : users) {
            if(user.login.equals(login) || user.nickname.equals(nickname)){
                return false;
            }
        }

        users.add(new UserData(login, password, nickname));
        return true;
    }

    @Override
    public boolean changeNick(String oldNickname, String newNickname) {

        if (SQLHandler.changeNick(oldNickname, newNickname)) {
            for (UserData user : users) {
                if(user.nickname.equals(oldNickname)){
                    user.nickname = newNickname;
                }
            }
            return true;
        }
        else {
            return false;
        }



    }
}
