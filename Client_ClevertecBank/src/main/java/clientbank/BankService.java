package clientbank;

import lombok.extern.slf4j.Slf4j;
import model.ExitMessage;
import model.Message;
import model.auth.AuthRequestMessage;
import model.operations.BalanceRequestMessage;
import model.operations.DepositRequestMessage;
import model.operations.TransferRequestMessage;
import model.operations.WithdrawalRequestMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

/**
 * Represents a client-side service for interacting with the bank server.
 * Provides methods for authentication, balance inquiry, deposit, withdrawal, and transfer operations.
 */
@Slf4j
public class BankService {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8761;
    private Network network;
    private Socket socket;

    /**
     * Constructs a new BankService instance and initializes the network connection.
     */
    public BankService() {
        initNetwork();
    }

    /**
     * Initializes the network connection to the bank server.
     */
    private void initNetwork() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            network = new Network(
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()));
        } catch (Exception e) {
            log.error("Failed to initialize network connection.", e);
        }
    }

    /**
     * Authenticates a user against the bank server.
     *
     * @param login    The login/username.
     * @param password The password.
     * @return The response message from the server.
     * @throws IOException, ClassNotFoundException
     */
    public Message authenticate(String login, String password) throws IOException, ClassNotFoundException {
        network.sendMessage(new AuthRequestMessage(login, password));
        return network.receiveMessage();
    }

    /**
     * Sends a request to the bank server to retrieve the balance.
     *
     * @return The response message from the server.
     * @throws IOException, ClassNotFoundException
     */
    public Message sendBalanceRequest() throws IOException, ClassNotFoundException {
        network.sendMessage(new BalanceRequestMessage());
        return network.receiveMessage();
    }

    /**
     * Sends a deposit request to the bank server.
     *
     * @param depositAmount The amount to be deposited.
     * @return The response message from the server.
     * @throws IOException, ClassNotFoundException
     */
    public Message deposit(BigDecimal depositAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new DepositRequestMessage(depositAmount));
        return network.receiveMessage();
    }

    /**
     * Sends a withdrawal request to the bank server.
     *
     * @param withdrawAmount The amount to be withdrawn.
     * @return The response message from the server.
     * @throws IOException, ClassNotFoundException
     */
    public Message withdrawal(BigDecimal withdrawAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new WithdrawalRequestMessage(withdrawAmount));
        return network.receiveMessage();
    }

    /**
     * Sends a transfer request to the bank server.
     *
     * @param accountNumber  The recipient's account number.
     * @param transferAmount The amount to be transferred.
     * @return The response message from the server.
     * @throws IOException, ClassNotFoundException
     */
    public Message transfer(String accountNumber, BigDecimal transferAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new TransferRequestMessage(accountNumber, transferAmount));
        return network.receiveMessage();
    }

    /**
     * Closes the network connection and resources.
     */
    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            log.error("Error while closing resources.", e);
        }
    }

    /**
     * Sends an exit message to the bank server, signaling the end of the session.
     *
     * @throws IOException
     */
    public void exit() throws IOException {
        network.sendMessage(new ExitMessage());
    }
}
