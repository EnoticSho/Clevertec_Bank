package server;

import model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                authenticate();
//                readMessages();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void authenticate() {
        sendMessage(new ServerAuthMessage());
        while (true) {
//            try {
//                final Message message = (Message) in.readObject();
//                if (message instanceof AuthMessage am) {
//                    final AuthMessage authMessage = (AuthMessage) message;
//                    final String login = authMessage.getLogin();
//                    final String password = authMessage.getPassword();
//                    final String nick = authService.getNickByLoginAndPassword(login, password);
//                    if (nick != null) {
//                        if (server.isNickBusy(nick)) {
//                            sendMessage(ErrorMessage.of("Пользователь уже авторизован"));
//                            continue;
//                        }
//                        authTimeThread.interrupt();
//                        sendMessage(AuthOkMessage.of(nick));
//                        this.nick = nick;
//                        server.broadcast(SimpleMessage.of("Пользователь " + nick + " зашел в чат"));
//                        server.subscribe(this);
//                        break;
//                    } else {
//                        sendMessage(ErrorMessage.of("Неверные логин и пароль"));
//                    }
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
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
}
