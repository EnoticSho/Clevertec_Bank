package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer {
    private static final int PORT = 8761;
    private final Map<String, ClientHandler> clients;
    private volatile boolean running = true;

    public BankServer() {
        clients = new HashMap<>();
    }

    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is running...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                executorService.execute(new ClientHandler(clientSocket, this));
            }
        } catch (BindException be) {
            System.err.println("The port " + PORT + " is already in use. Please choose another port or close the application using it.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isUserIn(String username) {
        return clients.containsKey(username);
    }

    public synchronized void subscribe(ClientHandler client) {
        clients.put(client.getUsername(), client);
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler.getUsername());
    }
}
