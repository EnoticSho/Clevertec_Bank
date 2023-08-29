package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class TransactionDAO {

    private final DatabaseConnectionManager connectionManager;

    public TransactionDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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
