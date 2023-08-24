package clientbank;

import model.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BankClient {
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 8761;
    private Network<ObjectInputStream, ObjectOutputStream> network;
    private final Scanner scanner;

    public BankClient() {
        scanner = new Scanner(System.in);
        initNetwork();
    }

    private void initNetwork() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            network = new Network<>(
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()));
            authenticate();
            readMessages();
        } catch (Exception e) {
            System.err.println("Failed to initialize network connection.");
            e.printStackTrace();
        }
    }

    private void authenticate() {
        try {
            while (true) {
                Message message = (Message) network.getInputStream().readObject();
                if (message instanceof ServerAuthMessage sam) {
                    handleServerAuthMessage(sam);
                } else if (message instanceof AuthOk ao) {
                    System.out.println(ao.getAuthOk());
                    break;
                } else if (message instanceof ErrorMessage em) {
                    System.out.println(em.getErrorMessage());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Authentication failed.");
            e.printStackTrace();
        }
    }

    private void handleServerAuthMessage(ServerAuthMessage sam) throws IOException {
        System.out.println(sam.getString());
        String login = scanner.nextLine();
        String password = scanner.nextLine();
        sendMessage(new AuthMessage(login, password));
    }

    private void readMessages() {
        try {
            while (true) {
                Message message = (Message) network.getInputStream().readObject();
                if (message instanceof ServerOperationMessage) {
                    handleServerOperationMessage((ServerOperationMessage) message);
                } else if (message instanceof BalanceMessage) {
                    System.out.println("Your balance: " + ((BalanceMessage) message).getBalance());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while reading messages. Server might be offline.");
            e.printStackTrace();
        }
    }

    private void handleServerOperationMessage(ServerOperationMessage som) throws IOException {
        System.out.println(som.getOperations());
        int num = Integer.parseInt(scanner.nextLine());
        if (num == 1) {
            sendMessage(new BalanceMessage());
        } else if (num == 4) {

            sendMessage(new TransferMessage());
        }
        // Handle other operation options here
    }

    public void sendMessage(Message message) {
        try {
            network.getOutputStream().writeObject(message);
        } catch (IOException e) {
            System.err.println("Failed to send message.");
            e.printStackTrace();
        }
    }
}
