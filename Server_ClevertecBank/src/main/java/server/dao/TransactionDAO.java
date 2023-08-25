package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class TransactionDAO {

    public void createTransaction(Transaction transaction) throws SQLException {
        if(transaction == null) {
            throw new IllegalArgumentException("Transaction object is null");
        }

        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            String sql = "INSERT INTO transaction (amount, transaction_date, sender_account_id, receiver_account_id, transaction_type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setBigDecimal(1, transaction.getAmount());
                pstmt.setTimestamp(2, transaction.getTransactionDate());
                if (transaction.getSenderAccountId() != null) {
                    pstmt.setInt(3, transaction.getSenderAccountId());
                } else {
                    pstmt.setNull(3, java.sql.Types.INTEGER);
                }
                if (transaction.getReceiverAccountId() != null) {
                    pstmt.setInt(4, transaction.getReceiverAccountId());
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }

                pstmt.setObject(5, transaction.getTransactionType().name(), Types.OTHER);
                pstmt.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Additional methods for fetching, updating, or deleting transactions can be added as required.

}
