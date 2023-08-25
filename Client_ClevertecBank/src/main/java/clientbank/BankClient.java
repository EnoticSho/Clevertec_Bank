package clientbank;

import model.*;
import model.auth.AuthRequestMessage;
import model.auth.AuthResponse;
import model.auth.ServerAuthMessage;
import model.operations.*;

import java.io.*;
import java.math.BigDecimal;
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
                handleServerAuthMessage();
                Message message = network.receiveMessage();
                if (message instanceof AuthResponse ao) {
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

    private void handleServerAuthMessage() throws IOException {
        System.out.println(new ServerAuthMessage().getString());
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        sendMessage(new AuthRequestMessage(login, password));
    }

    private void readMessages() {
        try {
            while (true) {
                handleServerOperationMessage();
                Message message = network.receiveMessage();
                if (message instanceof BalanceResponseMessage brm) {
                    printText(brm.getMessage(), scanner);
                } else if (message instanceof DepositResponseMessage drm) {
                    printText(drm.getMessage(), scanner);
                } else if (message instanceof WithdrawalResponseMessage wrm) {
                    printText(wrm.getMessage(), scanner);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while reading messages. Server might be offline.");
            e.printStackTrace();
        }
    }

    private void handleServerOperationMessage() throws IOException {
        System.out.println(new ServerOperationMessage().getOperations());
        int num = Integer.parseInt(scanner.nextLine());
        switch (num) {
            case 1 -> {
                sendMessage(new BalanceRequestMessage());
            }
            case 2 -> {
                System.out.print("Enter amount of withdrawal: ");
                BigDecimal bg = BigDecimal.valueOf(Long.parseLong(scanner.nextLine()));
                sendMessage(new WithdrawalRequestMessage(bg));
            }
            case 3 -> {
                System.out.print("Enter amount of deposit: ");
                BigDecimal bg = BigDecimal.valueOf(Long.parseLong(scanner.nextLine()));
                sendMessage(new DepositRequestMessage(bg));
            }
            case 4 -> {
                System.out.print("Enter amount of deposit: ");
                BigDecimal bg = BigDecimal.valueOf(Long.parseLong(scanner.nextLine()));
                System.out.print("Enter number of account: ");
                String accountNumber = scanner.nextLine();
                sendMessage(new TransferRequestMessage(accountNumber, bg));
            }
            case 5 -> {
                close();
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

    private void printText(String s, Scanner scanner) {
        System.out.println(s);
        System.out.println("""
                            1. Продолжить
                            2. Выйти
                            """);
        int a = Integer.parseInt(scanner.nextLine());
        if (a == 2) close();
    }

    private void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (network != null) {
                network.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing resources.");
            e.printStackTrace();
        }
    }
}
