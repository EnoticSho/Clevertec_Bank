package server.dao;

import lombok.extern.slf4j.Slf4j;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Provides functionality to interact with the transaction data in the database.
 */
@Slf4j
public class TransactionDAO {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructor to initialize with a connection manager.
     *
     * @param connectionManager The database connection manager.
     */
    public TransactionDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Creates a new transaction record in the database.
     *
     * @param transaction The transaction object with the details to be saved.
     * @throws SQLException if there's an error during the database operation.
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
