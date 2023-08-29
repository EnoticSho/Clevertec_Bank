package server;

import server.config.ConfigurationLoader;
import server.dbConnection.DatabaseConnectionManager;
import server.service.AccountService;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class BankServer {
    private static final int PORT = 8761;
    private final Map<String, ClientHandler> clients;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
    private volatile boolean running = true;
    private final AccountService accountService;
    private final DatabaseConnectionManager connectionManager;

    public BankServer() {
        clients = new HashMap<>();
        connectionManager = new DatabaseConnectionManager();
        accountService = new AccountService(connectionManager);
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        startInterestTask();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is running...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                executorService.execute(new ClientHandler(clientSocket, this, connectionManager));
            }
        } catch (BindException be) {
            System.err.println("The port " + PORT + " is already in use. Please choose another port or close the application using it.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startInterestTask() {
        Map<String, Object> config;
        try {
            config = new ConfigurationLoader().loadConfig();
            Double interestRate = (Double) config.get("interestRate");
            scheduler.scheduleWithFixedDelay(() -> {
                LocalDate now = LocalDate.now();
                if (now.equals(now.with(TemporalAdjusters.lastDayOfMonth()))) {
                    applyInterest(interestRate);
                }
            }, 0, 30, TimeUnit.SECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyInterest(double interestRate) {
        accountService.addPercentByAccount(interestRate);
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

    public void shutdown() {
        executorService.shutdown();

        scheduler.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();

                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService did not terminate.");
                }
            }

            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Scheduler did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            scheduler.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }
}
