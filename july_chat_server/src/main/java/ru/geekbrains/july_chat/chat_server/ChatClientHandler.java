package ru.geekbrains.july_chat.chat_server;

import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;
import ru.geekbrains.july_chat.chat_server.error.WrongCredentialsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

public class ChatClientHandler {
    public static final String REGEX = "%&%";
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
//    private Thread handlerThread;
    private JulyChatServer server;
    private String currentUser;
    private ExecutorService executorService;

    public ChatClientHandler(Socket socket, JulyChatServer server) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Handler created");
            this.server = server;
            this.executorService = server.getExecutorService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
//        handlerThread = new Thread(() -> {
        executorService.execute(() -> {
            long a = System.currentTimeMillis();
            authorize();
            System.currentTimeMillis();
            long b = System.currentTimeMillis()-a;
            if (b > 120000){
                try {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                        String message = in.readUTF();
                        handleMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.removeAuthorizedClientFromList(this);
                }
            }
        });
//        handlerThread.start();
    }

    //auth: lllll ppppp
    private void authorize() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("/auth") || message.startsWith("/register")) {
                    if (handleMessage(message)) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean handleMessage(String message) {
        try {
            String[] parsed = message.split(REGEX);
            switch (parsed[0]) {
                case "/w":
                    server.sendPrivateMessage(this.currentUser, parsed[1], parsed[2], this);
                    break;
                case "/ALL":
                    server.broadcastMessage(this.currentUser, parsed[1]);
                    break;
                case "/change_nick":
                    String nick = server.getAuthService().changeNickname(this.currentUser, parsed[1]);
                    server.removeAuthorizedClientFromList(this);
                    this.currentUser = nick;
                    server.addAuthorizedClientToList(this);
                    sendMessage("/change_nick_ok");
                    break;
                case "/change_pass":
                    server.getAuthService().changePassword(this.currentUser, parsed[1], parsed[2]);
                    sendMessage("/change_pass_ok");
                    break;
                case "/remove":
                    server.getAuthService().deleteUser(this.currentUser);
                    this.socket.close();
                    break;
                case "/register":
                    server.getAuthService().createNewUser(parsed[1], parsed[2], parsed[3]);
                    sendMessage("register_ok:");
                    break;
                case "/auth":
                    this.currentUser = server.getAuthService().getNicknameByLoginAndPassword(parsed[1], parsed[2]);
                    if (server.isNicknameBusy(currentUser)) {
                        sendMessage("ERROR:" + REGEX + "U're clone!");
                    } else {
                        this.server.addAuthorizedClientToList(this);
                        sendMessage("authok:" + REGEX + this.currentUser);
                        return true;
                    }
                    break;
                default:
                    sendMessage("ERROR:" + REGEX + "command not found!");
            }
        } catch (Exception e) {
            sendMessage("ERROR:" + REGEX + e.getMessage());
        }
        return false;
    }

//    public Thread getHandlerThread() {
//        return handlerThread;
//    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void sendMessage(String message) {
        try {
            this.out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
