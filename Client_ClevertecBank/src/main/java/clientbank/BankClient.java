package clientbank;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Represents the bank client application for end-users.
 * Provides functionalities like authentication, checking balance, deposit, withdrawal, and transfers.
 */
@Slf4j
public class BankClient {
    private final Scanner scanner;
    private final BankService bankService;
    private boolean isAuthenticated = false;
    private boolean isRunning = true;

    /**
     * Constructs a new BankClient instance.
     *
     * @param bankService The service to interact with the bank server.
     */
    public BankClient(BankService bankService) {
        this.bankService = bankService;
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the bank client application. Authenticates the user and displays the main menu.
     */
    public void start() {
        System.out.println("Welcome to the CleverBank.");
        authenticate();
        if (isAuthenticated) {
            while (isRunning) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                handleUserChoice(choice);
            }
        }
        scanner.close();
    }

    /**
     * Displays the main menu for the client application.
     */
    private void displayMenu() {
        System.out.println("""
                1. checkBalance
                2. deposit
                3. withdrawal
                4. transfer
                0. Exit
                """);
    }

    /**
     * Handles the user's choice from the main menu.
     *
     * @param choice The menu option chosen by the user.
     */
    private void handleUserChoice(int choice) {
        switch (choice) {
            case 1 -> checkBalance();
            case 2 -> deposit();
            case 3 -> withdrawal();
            case 4 -> transfer();
            case 0 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Authenticates the user by prompting for login and password.
     */
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
                        log.warn("Authentication failed. Please try again.");
                    }
                } else if (response instanceof ErrorMessage) {
                    System.out.println(((ErrorMessage) response).getErrorMessage());
                } else {
                    handleUnexpectedResponse(response, "auth");
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Error during authentication.", e);
            }
        }
    }

    /**
     * Sends a balance check request and displays the result to the user.
     */
    private void checkBalance() {
        try {
            Message response = bankService.sendBalanceRequest();
            if (response instanceof BalanceResponseMessage brm) {
                System.out.println(brm.getMessage());
            } else if (response instanceof ErrorMessage em) {
                System.out.println(em.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error during deposit.", e);
        }
    }

    /**
     * Prompts the user for an amount and sends a deposit request.
     */
    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();
        System.out.println(bigDecimal);

        try {
            Message response = bankService.deposit(bigDecimal);
            if (response instanceof DepositResponseMessage) {
                System.out.println(((DepositResponseMessage) response).getMessage());
            } else if (response instanceof ErrorMessage em) {
                System.out.println(em.getErrorMessage());
            } else {
                handleUnexpectedResponse(response, "deposit");
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error during deposit.", e);
        }
    }

    /**
     * Prompts the user for an amount and sends a withdrawal request.
     */
    private void withdrawal() {
        System.out.print("Enter amount to withdraw: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();

        try {
            Message response = bankService.withdrawal(bigDecimal);
            if (response instanceof WithdrawalResponseMessage) {
                System.out.println(((WithdrawalResponseMessage) response).getMessage());
            } else if (response instanceof ErrorMessage em) {
                System.out.println(em.getErrorMessage());
            } else {
                handleUnexpectedResponse(response, "withdraw");
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error during withdrawal.", e);
        }
    }

    /**
     * Prompts the user for an amount and an account number to transfer to, then sends a transfer request.
     */
    private void transfer() {
        System.out.print("Enter amount to transfer: ");
        BigDecimal bigDecimal = scanner.nextBigDecimal();

        System.out.print("Enter account number to transfer to: ");
        String accountNumber = scanner.next();

        try {
            Message response = bankService.transfer(accountNumber, bigDecimal);
            if (response instanceof TransferResponseMessage) {
                System.out.println(((TransferResponseMessage) response).getMessage());
            } else if (response instanceof ErrorMessage em) {
                System.out.println(em.getErrorMessage());
            } else {
                handleUnexpectedResponse(response, "transfer");
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error during transfer.", e);
        }
    }

    /**
     * Handles unexpected server responses and logs the event.
     *
     * @param response The received server response.
     * @param action   The action during which the response was received.
     */
    private void handleUnexpectedResponse(Message response, String action) {
        System.out.println("Unexpected server response during " + action + ". Please try again.");
        log.error("Received unexpected response type during " + action + ": " + response.getClass().getName());
    }


    /**
     * Exits the client application, closing all resources.
     */
    private void exit() {
        System.out.println("Exiting...");
        try {
            bankService.exit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bankService.close();
        isRunning = false;
    }
}
