package server;

import model.*;
import server.service.AccountService;
import server.service.AuthService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final AuthService authService;
    private final AccountService accountService;
    private String username;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        authService = new AuthService();
        accountService = new AccountService();
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            authenticate();
            readMessages();
        } finally {
            closeResources();
        }
    }

    private void authenticate() {
        sendMessage(new ServerAuthMessage());
        while (true) {
            try {
                final Message message = (Message) in.readObject();
                if (message instanceof AuthMessage am) {
                    String login = am.getLogin();
                    String password = am.getPassword();
                    if (authService.isClientExists(login, password)) {
//                        if (server.isNickBusy(nick)) {
//                            sendMessage(ErrorMessage.of("Пользователь уже авторизован"));
//                            continue;
//                        }
                        username = login;
                        sendMessage(new AuthOk());
                        break;
                    } else {
                        sendMessage(new ErrorMessage("Неверные логин и пароль"));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readMessages() {
        sendMessage(new ServerOperationMessage());
        try {
            while (true) {
                Message message = (Message) in.readObject();
                if (message instanceof BalanceMessage bm) {
                    BigDecimal currentBalance = accountService.getCurrentBalance(username);
                    sendMessage(new BalanceMessage(currentBalance));
                }
//                if (message.getCommand() == Command.MESSAGE) {
//                    final SimpleMessage simpleMessage = (SimpleMessage) message;
//                    server.broadcast(simpleMessage);
//                }
//                if (message.getCommand() == Command.PRIVATE_MESSAGE) {
//                    final PrivateMessage privateMessage = (PrivateMessage) message;
//                    server.sendMessageToClient(this, privateMessage.getNickTo(), privateMessage.getMessage());
//                }
//                if (message.getCommand() == Command.CHANGENICK) {
//                    try (ChangeNickService changeNickService = new ChangeNickService()) {
//                        ChangeNickMessage changeNickMessage = (ChangeNickMessage) message;
//                        changeNickService.changeNick(changeNickMessage.getNewNick(), changeNickMessage.getOldNick());
//                        this.nick = changeNickMessage.getNewNick();
//                        server.update(changeNickMessage.getOldNick(), this);
//                        server.broadcast(changeNickMessage);
//                    }
//                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            System.out.println("SERVER: " + message.getType());
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeResources() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
