package ru.geekbrains.july_chat.chat_server.auth;

import ru.geekbrains.july_chat.chat_server.error.BadRequestException;
import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;
import ru.geekbrains.july_chat.chat_server.error.WrongCredentialsException;

import java.sql.*;

public class InMemoryAuthService implements AuthService  {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static Connection connection;
    private static Statement statement;

    public InMemoryAuthService() {
        try {
            connect();
            dropTableEx();
            createTableEx();
            createNewUser111("log1","pass","nick1");
            createNewUser111("log2","pass","nick2");
            createNewUser111("log3","pass","nick3");
            createNewUser111("log4","pass","nick4");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection("jdbc:sqlite:authList.db");
        statement = connection.createStatement();
    }

    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTableEx() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS authList (\n" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " login TEXT  NOT NULL UNIQUE,\n" +
                " password TEXT  NOT NULL,\n" +
                " nickname TEXT  NOT NULL UNIQUE\n" +
                ");");
    }

    public static void readEx() throws SQLException {
        try (
            ResultSet resultSet = statement.executeQuery("SELECT * FROM authList;")) {
    while (resultSet.next()) {
        System.out.println(resultSet.getInt(1) +  " " + resultSet.getString(2) + " " +
                resultSet.getString(3) + " " + resultSet.getString(4));
            }
        }
    }

    public void createNewUser(String login, String password, String nickname) throws SQLException {
       statement.executeUpdate("INSERT INTO authList (login, password, nickname) VALUES (\'"+login+"\', \'"+password+"\', \'"+nickname+"\');");
    }

    public void deleteUser(String nickname) throws SQLException {
        statement.executeUpdate("DELETE FROM authList WHERE  nickname = \'"+nickname+"\';");
    }

    private static void clearTableEx() throws SQLException {
        statement.executeUpdate("DELETE FROM authList;");
    }

    private static void dropTableEx() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS authList;");
    }

    public  void start() {
        System.out.println("Auth service started!");
    }

    public void stop() {
        disconnect();
        System.out.println("Auth service stopped");
    }

    public String getNicknameByLoginAndPassword (String login, String password) throws SQLException {
        try(
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authList WHERE login = \'"+login+"\' AND password = \'"+password+"\';")) {
            while (resultSet.next()) {
                if (login.equals(resultSet.getString(2))) {
                    if (password.equals(resultSet.getString(3)))
                        return resultSet.getString(4);
                    else throw new WrongCredentialsException("111");
                }
            }
        }
        throw new UserNotFoundException("User not found");
    }

    public String changeNickname (String oldNick, String newNick) throws SQLException {
        try(
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authList WHERE nickname = \'"+newNick+"\';")) {
            while (resultSet.next()) {throw new BadRequestException("This nick busy");
            }
        }
        try(
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authList WHERE nickname = \'"+oldNick+"\';")) {
            while (resultSet.next()) {
                if (oldNick.equals(resultSet.getString(4))) {
                    statement.executeUpdate("UPDATE authList SET nickname = \'"+newNick+"\' WHERE  nickname = \'"+oldNick+"\';");
                    return newNick;
                }
            }
        }
        throw new UserNotFoundException("User not found");
    }

    public void changePassword (String nickname, String oldPassword, String newPassword) throws SQLException {
        try (
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authList WHERE nickname = \'" + nickname + "\';")) {
            while (resultSet.next()) {
                if (nickname.equals(resultSet.getString(4))) {
                    if (oldPassword.equals(resultSet.getString(3))) {
                        statement.executeUpdate("UPDATE authList SET password = \'" + newPassword + "\' WHERE  nickname = \'" + nickname + "\';");
                        return;
                    }
                }
            }
        }
            throw new UserNotFoundException("User not found");
    }

    public void createNewUser111 (String login, String password, String nickname) throws SQLException {
        statement.executeUpdate("INSERT INTO authList (login, password, nickname) VALUES (\'"+login+"\', \'"+password+"\', \'"+nickname+"\');");
    }
}






