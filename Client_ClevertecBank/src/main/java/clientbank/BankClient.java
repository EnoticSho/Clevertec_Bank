package clientbank;

import model.*;

import java.io.*;
import java.net.*;

public class BankClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8761;
    private Network<ObjectInputStream, ObjectOutputStream> network;
    private Socket socket;
    private boolean needReadMessages = true;
    private final DaemonThreadFactory factory;

    public BankClient() {
        factory = new DaemonThreadFactory();
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            network = new Network<>(
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()));
            factory.newThread(() -> {
                        BankClient.this.auth();
                        BankClient.this.readMessages();
                    })
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void auth() {
        System.out.println("auth client");
        try {
            while (true) {
                Message message = (Message) network.getInputStream().readObject();
                if (message instanceof ServerAuthMessage sam) {
                    System.out.println(sam.getString());
                } else if (message instanceof ErrorMessage em) {
                } else if (message instanceof RegistrationSuccessMessage rs) {
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void readMessages() {
        System.out.println("dadsfadsfaaaaaaa");
        try {
            while (needReadMessages) {
                Message cloudMessage = (Message) network.getInputStream().readObject();
            }
        } catch (Exception e) {
            System.err.println("Server off");
            e.printStackTrace();
        }
    }



    public void sendMessage(Message message) {
        try {
            System.out.println("Send message: " + message);
            network.getOutputStream().writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
