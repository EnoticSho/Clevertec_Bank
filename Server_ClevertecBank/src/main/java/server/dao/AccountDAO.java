package server.dao;

import server.dbConnection.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public void depositFunds(int accountId, BigDecimal amount) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            // Проверка на существование счета
            if (isAccountExists(connection, accountId)) {
                String depositSQL = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(depositSQL)) {
                    preparedStatement.setBigDecimal(1, amount);
                    preparedStatement.setInt(2, accountId);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Средства успешно пополнены.");
                    } else {
                        System.out.println("Не удалось пополнить счет. Проверьте данные.");
                    }
                }
            } else {
                System.out.println("Счет с указанным ID не существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdrawFunds(int accountId, BigDecimal amount) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            // Проверка на существование счета
            if (isAccountExists(connection, accountId)) {
                BigDecimal currentBalance = getCurrentBalance(connection, accountId);
                if (currentBalance.compareTo(amount) >= 0) {
                    String withdrawSQL = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(withdrawSQL)) {
                        preparedStatement.setBigDecimal(1, amount);
                        preparedStatement.setInt(2, accountId);
                        int rowsUpdated = preparedStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Средства успешно сняты.");
                        } else {
                            System.out.println("Не удалось снять средства. Проверьте данные.");
                        }
                    }
                } else {
                    System.out.println("Недостаточно средств на счете.");
                }
            } else {
                System.out.println("Счет с указанным ID не существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isAccountExists(Connection connection, int accountId) throws SQLException {
        String checkAccountSQL = "SELECT account_id FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkAccountSQL)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static BigDecimal getCurrentBalance(Connection connection, int accountId) throws SQLException {
        String getBalanceSQL = "SELECT balance FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getBalanceSQL)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("balance");
                } else {
                    throw new SQLException("Счет не найден.");
                }
            }
        }
    }
}
