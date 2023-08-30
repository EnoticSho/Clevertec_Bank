package server;

import lombok.extern.slf4j.Slf4j;
import server.config.ConfigurationLoader;
import server.dbConnection.DatabaseConnectionManager;
import server.service.AccountService;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This class represents the main server functionality for the bank application.
 * It handles client connections, interest rate scheduling, and server shutdown tasks.
 */
@Slf4j
public class BankServer {
    private static final int PORT = 8761;
    private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
    private volatile boolean running = true;
    private final AccountService accountService;
    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructs a new BankServer instance.
     * Initializes the connection manager and account service.
     */
    public BankServer() {
        connectionManager = new DatabaseConnectionManager();
        accountService = new AccountService(connectionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * Starts the bank server. It listens to client connections and handles
     * incoming requests using client handlers.
     */
    public void run() {
        startInterestTask();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server is running...");
            while (running) {
                Socket clientSocket = serverSocket.accept();
                log.info("New client connected: {}", clientSocket.getInetAddress().getHostAddress());
                executorService.execute(new ClientHandler(clientSocket, this, connectionManager));
            }
        } catch (BindException be) {
            log.error("The port {} is already in use. Please choose another port or close the application using it.", PORT);
        } catch (IOException e) {
            log.error("IO Exception encountered", e);
        }
    }

    /**
     * Starts the scheduled task to apply interest to accounts at the end of each month.
     */
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

    /**
     * Applies the given interest rate to all accounts.
     *
     * @param interestRate The interest rate to be applied to accounts.
     */
    private void applyInterest(double interestRate) {
        accountService.addPercentByAccount(interestRate);
    }

    /**
     * Checks if a user with the given username is currently connected to the server.
     *
     * @param username The username to check.
     * @return true if the user is connected, false otherwise.
     */
    public boolean isUserIn(String username) {
        return clients.containsKey(username);
    }

    /**
     * Adds a client to the active clients list.
     *
     * @param client The client handler to be added.
     */
    public void subscribe(ClientHandler client) {
        clients.put(client.getUsername(), client);
    }

    /**
     * Removes a client from the active clients list.
     *
     * @param clientHandler The client handler to be removed.
     */
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler.getUsername());
    }

    /**
     * Shuts down the server gracefully, terminating all executor services.
     */
    public void shutdown() {
        shutdownExecutor(executorService);
        shutdownExecutor(scheduler);
    }

    /**
     * Gracefully shuts down an executor service. If tasks do not finish termination
     * in 60 seconds, they are forcibly stopped.
     *
     * @param executor The executor service to be shut down.
     */
    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
