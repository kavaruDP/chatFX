package server;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);
    boolean changeNick(String oldNickname, String newNickname);
    boolean registration(String login, String password, String nickname);
}
