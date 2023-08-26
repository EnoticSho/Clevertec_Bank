package server;

import lombok.Getter;
import model.ErrorMessage;
import model.Message;
import model.auth.AuthRequestMessage;
import model.auth.AuthResponse;
import model.operations.*;
import server.dbConnection.DatabaseConnectionManager;
import server.service.AccountService;
import server.service.TransactionService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final BankServer bankServer;
    @Getter
    private String username;
    @Getter
    private Integer accountId;

    public ClientHandler(Socket clientSocket, BankServer bankServer, DatabaseConnectionManager connectionManager) {
        this.clientSocket = clientSocket;
        this.bankServer = bankServer;
        connectionManager = new DatabaseConnectionManager();
        transactionService = new TransactionService(connectionManager);
        accountService = new AccountService(connectionManager);
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
            bankServer.removeClient(this);
            closeResources();
        }
    }

    private void authenticate() {
        while (true) {
            try {
                final Message message = (Message) in.readObject();
                if (message instanceof AuthRequestMessage am) {
                    String login = am.getLogin();
                    String password = am.getPassword();
                    System.out.println(password);
                    accountId = accountService.getAccountIdByClientLogin(login, password);
                    if (accountId != null) {
                        if (bankServer.isUserIn(login)) {
                            sendMessage(new ErrorMessage("Пользователь уже авторизован"));
                            continue;
                        }
                        username = login;
                        bankServer.subscribe(this);
                        sendMessage(new AuthResponse(true));
                        break;
                    } else {
                        sendMessage(new ErrorMessage("Неверный логин или пароль"));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readMessages() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                if (message instanceof BalanceRequestMessage) {
                    BigDecimal currentBalance = accountService.getCurrentBalance(username);
                    sendMessage(new BalanceResponseMessage(currentBalance));
                }else if (message instanceof DepositRequestMessage drm) {
                    if (transactionService.deposit(accountId, drm.getAmount())) {
                        sendMessage(new DepositResponseMessage(drm.getAmount()));
                    }
                }else if (message instanceof WithdrawalRequestMessage wrm) {
                    if (transactionService.withdraw(accountId, wrm.getAmount())) {
                        sendMessage(new WithdrawalResponseMessage(wrm.getAmount()));
                    }
                }else if (message instanceof TransferRequestMessage trm) {
                    if (transactionService.transfer(accountId, trm.getAccount(), trm.getBigDecimal())) {
                        sendMessage(new TransferResponseMessage(trm.getAccount(), trm.getBigDecimal()));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
