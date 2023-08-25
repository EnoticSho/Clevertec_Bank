package clientbank;

import model.*;
import model.auth.AuthRequestMessage;
import model.auth.AuthResponse;
import model.auth.ServerAuthMessage;
import model.operations.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BankClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8761;
    private Network network;
    private Socket socket;
    private final Scanner scanner;

    public BankClient() {
        scanner = new Scanner(System.in);
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            network = new Network(
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()));
            authenticate();
            readMessages();
        } catch (Exception e) {
            System.err.println("Failed to initialize network connection.");
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void authenticate() {
        try {
            while (true) {
                Message message = network.receiveMessage();
                if (message instanceof ServerAuthMessage sam) {
                    handleServerAuthMessage(sam);
                } else if (message instanceof AuthResponse ao) {
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
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        sendMessage(new AuthRequestMessage(login, password));
    }

    private void readMessages() {
        try {
            while (true) {
                Message message = network.receiveMessage();
                if (message instanceof ServerOperationMessage) {
                    handleServerOperationMessage((ServerOperationMessage) message);
                } else if (message instanceof BalanceResponseMessage) {
                    System.out.println("Your balance: " + ((BalanceResponseMessage) message).getBalance());
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
        switch (num) {
            case 1 -> sendMessage(new BalanceRequestMessage());
            case 2 -> sendMessage(new WithdrawalRequestMessage());
            case 3 -> sendMessage(new DepositRequestMessage());
            case 4 -> sendMessage(new TransferRequestMessage());
            default -> {
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            network.sendMessage(message);
        } catch (IOException e) {
            System.err.println("Failed to send message.");
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (network != null) {
                network.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing resources.");
            e.printStackTrace();
        }
    }
}
