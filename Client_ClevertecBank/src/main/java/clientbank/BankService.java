package clientbank;

import model.*;
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

public class BankService {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8761;
    private Network network;
    private Socket socket;

    public BankService() {
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            network = new Network(
                    new ObjectInputStream(socket.getInputStream()),
                    new ObjectOutputStream(socket.getOutputStream()));
        } catch (Exception e) {
            System.err.println("Failed to initialize network connection.");
            e.printStackTrace();
        }
    }

    public Message authenticate(String login, String password) throws IOException, ClassNotFoundException {
        network.sendMessage(new AuthRequestMessage(login, password));
        return network.receiveMessage();
    }

    public Message sendBalanceRequest() throws IOException, ClassNotFoundException {
        network.sendMessage(new BalanceRequestMessage());
        return network.receiveMessage();
    }

    public Message deposit(BigDecimal depositAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new DepositRequestMessage(depositAmount));
        return network.receiveMessage();
    }

    public Message withdrawal(BigDecimal withdrawAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new WithdrawalRequestMessage(withdrawAmount));
        return network.receiveMessage();
    }

    public Message transfer(String accountNumber, BigDecimal transferAmount) throws IOException, ClassNotFoundException {
        network.sendMessage(new TransferRequestMessage(accountNumber, transferAmount));
        return network.receiveMessage();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing resources.");
            e.printStackTrace();
        }
    }

    public void exit() throws IOException {
        network.sendMessage(new ExitMessage());
    }
}
