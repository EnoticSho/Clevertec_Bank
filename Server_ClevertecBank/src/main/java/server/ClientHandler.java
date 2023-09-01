package server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.ErrorMessage;
import model.ExitMessage;
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


/**
 * Handles individual client connections, facilitating authentication,
 * reading messages, and performing transactions on behalf of the client.
 */
@Slf4j
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private static final int MAX_ATTEMPTS = 5;
    private final TransactionService transactionService;
    private final AccountService accountService;

    private final BankServer bankServer;
    @Getter
    private String username;
    @Getter
    private Integer accountId;

    /**
     * Creates a new client handler for a given socket connection and bank server instance.
     *
     * @param clientSocket      The socket representing the client connection.
     * @param bankServer        The main server instance.
     * @param connectionManager The database connection manager.
     * @throws IOException If there's an error setting up the input/output streams.
     */
    public ClientHandler(Socket clientSocket, BankServer bankServer, DatabaseConnectionManager connectionManager) throws IOException {
        this.clientSocket = clientSocket;
        this.bankServer = bankServer;

        this.transactionService = new TransactionService(connectionManager);
        this.accountService = new AccountService(connectionManager);

        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Authenticates the client, reads their messages, and handles their requests.
     */
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

    /**
     * Authenticates the user by checking their credentials against the database.
     */
    private void authenticate() {
        int attempts = 0;

        while (true) {
            if (attempts >= MAX_ATTEMPTS) {
                sendMessage(new ErrorMessage("Maximum authentication attempts reached. Closing connection."));
                closeResources();
                return;
            }

            try {
                final Message message = (Message) in.readObject();
                if (message instanceof AuthRequestMessage am) {
                    String login = am.getLogin();
                    String password = am.getPassword();
                    accountId = accountService.getAccountIdByClientLogin(login, password);
                    if (accountId != null) {
                        if (bankServer.isUserIn(login)) {
                            sendMessage(new ErrorMessage("User is already authenticated."));
                            attempts++;
                            continue;
                        }
                        username = login;
                        bankServer.subscribe(this);
                        sendMessage(new AuthResponse(true));
                        break;
                    } else {
                        sendMessage(new ErrorMessage("Incorrect username or password."));
                        attempts++;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Error occurred during authentication for user {}", username, e);
            }
        }
    }

    /**
     * Continuously reads messages from the client and dispatches them for processing.
     */
    private void readMessages() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                handleReceivedMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error reading messages", e);
        } catch (SQLException e) {
            log.error("Database error", e);
        }
    }

    /**
     * Handles received messages and takes appropriate actions based on the message type.
     *
     * @param message The received message from the client.
     * @throws IOException  If there's an IO error.
     * @throws SQLException If there's a database error.
     */
    private void handleReceivedMessage(Message message) throws IOException, SQLException {
        if (message instanceof BalanceRequestMessage) {
            handleBalanceRequest();
        } else if (message instanceof DepositRequestMessage) {
            handleDepositRequest((DepositRequestMessage) message);
        } else if (message instanceof WithdrawalRequestMessage) {
            handleWithdrawalRequest((WithdrawalRequestMessage) message);
        } else if (message instanceof TransferRequestMessage) {
            handleTransferRequest((TransferRequestMessage) message);
        } else if (message instanceof StatementRequestMessage) {
            handleStatementRequest((StatementRequestMessage) message);
        } else if (message instanceof ExitMessage) {
            handleExitRequest();
        } else {
            log.warn("Received unrecognized message type: {}", message.getType());
        }
    }

    private void handleBalanceRequest() {
        BigDecimal currentBalance = accountService.getCurrentBalance(accountId);
        sendMessage(new BalanceResponseMessage(currentBalance));
    }

    private void handleDepositRequest(DepositRequestMessage drm) throws IOException, SQLException {
        transactionService.deposit(accountId, drm.getAmount());
        sendMessage(new DepositResponseMessage(drm.getAmount()));
    }

    private void handleWithdrawalRequest(WithdrawalRequestMessage wrm) throws IOException, SQLException {
        transactionService.withdraw(accountId, wrm.getAmount());
        sendMessage(new WithdrawalResponseMessage(wrm.getAmount()));
    }

    private void handleTransferRequest(TransferRequestMessage trm) throws IOException, SQLException {
        transactionService.transfer(accountId, trm.getAccount(), trm.getBigDecimal());
        sendMessage(new TransferResponseMessage(trm.getAccount(), trm.getBigDecimal()));
    }


    private void handleStatementRequest(StatementRequestMessage message) throws SQLException {
        String accountStatement = transactionService.getAccountStatement(accountId, message.getStart(), message.getEnd());
        sendMessage(new StatementResponseMessage(accountStatement));
    }

    private void handleExitRequest() {
        bankServer.removeClient(this);
        closeResources();
    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(Message message) {
        try {
            log.info("SERVER: {}", message.getType());
            out.writeObject(message);
        } catch (IOException e) {
            log.error("Error sending message for user {}", username, e);
        }
    }

    /**
     * Closes all the resources associated with this client, including streams and the socket connection.
     */
    private void closeResources() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            log.error("Error closing resources for user {}", username, e);
        }
    }
}
