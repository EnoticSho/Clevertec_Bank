package clientbank;

import model.ErrorMessage;
import model.Message;
import model.auth.AuthResponse;
import model.operations.BalanceResponseMessage;
import model.operations.DepositResponseMessage;
import model.operations.TransferResponseMessage;
import model.operations.WithdrawalResponseMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class BankClient {
    private final Scanner scanner;
    private final BankService bankService;
    private boolean isAuthenticated = false;

    public BankClient(BankService bankService) {
        this.bankService = bankService;
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the CleverBank.");

        authenticate();

        if (isAuthenticated) {
            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                handleUserChoice(choice);
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("""
            1. checkBalance
            2. deposit
            3. withdrawal
            4. transfer
            0. Exit
            """);
    }

    private void handleUserChoice(int choice) {
        switch (choice) {
            case 1 -> checkBalance();
            case 2 -> deposit();
            case 3 -> withdrawal();
            case 4 -> transfer();
            case 0 -> {
                System.out.println("Exiting...");
                bankService.close();
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void authenticate() {
        boolean authenticationSuccess = false;

        while (!authenticationSuccess) {
            System.out.print("Enter login: ");
            String login = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            try {
                Message response = bankService.authenticate(login, password);

                if (response instanceof AuthResponse ar) {
                    isAuthenticated = ar.getAuthOk();
                    if (isAuthenticated) {
                        authenticationSuccess = true;
                    } else {
                        System.out.println("Authentication failed. Please try again.");
                    }
                } else if (response instanceof ErrorMessage) {
                    System.out.println(((ErrorMessage) response).getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error during authentication.");
                e.printStackTrace();
            }
        }
    }

    private void checkBalance() {
        try {
            Message response = bankService.sendBalanceRequest();
            if (response instanceof BalanceResponseMessage brm) {
                System.out.println(brm.getMessage());
            } else if(response instanceof ErrorMessage em){
                System.out.println(em.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during deposit.");
            e.printStackTrace();
        }
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();
        System.out.println(bigDecimal);

        try {
            Message response = bankService.deposit(bigDecimal);
            if (response instanceof DepositResponseMessage) {
                System.out.println(((DepositResponseMessage) response).getMessage());
            } else if(response instanceof ErrorMessage em){
                System.out.println(em.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during deposit.");
            e.printStackTrace();
        }
    }

    private void withdrawal() {
        System.out.print("Enter amount to withdraw: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();

        try {
            Message response = bankService.withdrawal(bigDecimal);
            if (response instanceof WithdrawalResponseMessage) {
                System.out.println(((WithdrawalResponseMessage) response).getMessage());
            } else if(response instanceof ErrorMessage em){
                System.out.println(em.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during withdrawal.");
            e.printStackTrace();
        }
    }

    private void transfer() {
        System.out.print("Enter amount to transfer: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();

        System.out.print("Enter account number to transfer to: ");
        String accountNumber = scanner.next();

        try {
            Message response = bankService.transfer(accountNumber, bigDecimal);
            if (response instanceof TransferResponseMessage) {
                System.out.println(((TransferResponseMessage) response).getMessage());
            } else if(response instanceof ErrorMessage em){
                System.out.println(em.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during transfer.");
            e.printStackTrace();
        }
    }
}
