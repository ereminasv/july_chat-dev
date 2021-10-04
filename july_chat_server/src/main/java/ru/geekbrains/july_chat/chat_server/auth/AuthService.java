package ru.geekbrains.july_chat.chat_server.auth;

import java.sql.SQLException;

public interface AuthService  {
    void start() throws SQLException, ClassNotFoundException;
    void stop();
    String getNicknameByLoginAndPassword(String login, String password) throws SQLException;
    String changeNickname(String oldNick, String newNick) throws SQLException;
    void changePassword(String nickname, String oldPassword, String newPassword) throws SQLException;
    void createNewUser(String login, String password, String nickname) throws SQLException;
    void deleteUser(String nickname) throws SQLException;
}
