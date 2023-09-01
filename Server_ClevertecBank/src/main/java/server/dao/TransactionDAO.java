package server.dao;

import lombok.extern.slf4j.Slf4j;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;
import server.entity.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing transaction-related data in the database.
 * Provides methods to create, retrieve, and manage transaction records.
 * Utilizes the {@link DatabaseConnectionManager} to manage and perform database operations.
 */
@Slf4j
public class TransactionDAO {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructs a new TransactionDAO instance with the specified database connection manager.
     *
     * @param connectionManager The manager handling database connections.
     */
    public TransactionDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Inserts a new transaction record into the database.
     *
     * @param transaction The {@link Transaction} entity to be inserted.
     * @throws IllegalArgumentException if the transaction object is null.
     */
    public void createTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction object is null");
        }

        String sql = "INSERT INTO transaction (amount, transaction_date, sender_account_id, receiver_account_id, transaction_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBigDecimal(1, transaction.getAmount());
            stmt.setTimestamp(2, transaction.getTransactionDate());

            if (transaction.getSenderAccountId() != null) {
                stmt.setInt(3, transaction.getSenderAccountId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (transaction.getReceiverAccountId() != null) {
                stmt.setInt(4, transaction.getReceiverAccountId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setObject(5, transaction.getTransactionType().name(), Types.OTHER);
            stmt.executeUpdate();
            log.info("Transaction created successfully for sender account ID: {}", transaction.getSenderAccountId());
        } catch (SQLException e) {
            log.error("Error creating transaction for sender account ID: {}", transaction.getSenderAccountId(), e);
        }
    }

    /**
     * Retrieves a list of transactions for a given receiver account ID within a specified date range.
     *
     * @param receiverAccountId The ID of the receiver account to fetch transactions for.
     * @param startDate         The start date of the date range.
     * @param endDate           The end date of the date range.
     * @return A list of {@link Transaction} entities matching the criteria.
     */
    public List<Transaction> getTransactionsForReceiverBetweenDates(Integer receiverAccountId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
                SELECT *
                FROM transaction
                WHERE receiver_account_id = ? AND DATE(transaction_date) BETWEEN ? AND ?""";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, receiverAccountId);
            ps.setDate(2, java.sql.Date.valueOf(startDate));
            ps.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = Transaction.builder()
                            .transactionId(rs.getInt("transaction_id"))
                            .amount(rs.getBigDecimal("amount"))
                            .transactionDate(rs.getTimestamp("transaction_date"))
                            .senderAccountId(rs.getInt("sender_account_id"))
                            .receiverAccountId(rs.getInt("receiver_account_id"))
                            .transactionType(TransactionType.valueOf(rs.getString("transaction_type")))
                            .build();

                    transactions.add(transaction);
                }
            }
            log.info("Retrieved transactions for receiver account ID: {} between {} and {}", receiverAccountId, startDate, endDate);
        } catch (SQLException e) {
            log.error("Error retrieving transactions for receiver account ID: {} between {} and {}", receiverAccountId, startDate, endDate, e);
        }

        return transactions;
    }
}
