package server.dao;

import server.entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TransactionDAO {

    private final Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    public void createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (amount, transaction_date, sender_account_id, receiver_account_id, transaction_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, transaction.getAmount());
            pstmt.setTimestamp(2, transaction.getTransactionDate());
            pstmt.setInt(3, transaction.getSenderAccountId());
            pstmt.setInt(4, transaction.getReceiverAccountId());
            pstmt.setString(5, transaction.getTransactionType().name());
            pstmt.executeUpdate();
        }
    }

    // Additional methods for fetching, updating, or deleting transactions can be added as required.

}
