package server.dao;

import server.dbConnection.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public Integer getAccountIdByClientLogin(String login, String password) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            String checkAccountSQL = """
                    SELECT account_id\s
                    FROM account
                    JOIN client ON account.client_id = client.client_id
                    WHERE client.username = ?
                    AND client.password = ?
                    AND account.bank_id = 1""";
            try (PreparedStatement preparedStatement = connection.prepareStatement(checkAccountSQL)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("account_id");
                    } else {
                        throw new SQLException("Счет не найден.");
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deposit(int accountId, BigDecimal amount) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            if (isAccountExists(connection, accountId)) {
                String depositSQL = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(depositSQL)) {
                    preparedStatement.setBigDecimal(1, amount);
                    preparedStatement.setInt(2, accountId);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Средства успешно пополнены.");
                        return true;
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
        return false;
    }

    public boolean withdraw(int accountId, BigDecimal amount) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            // Проверка на существование счета
            if (isAccountExists(connection, accountId)) {
//                BigDecimal currentBalance = getCurrentBalance(accountId);
                if (0 == 0) {
                    String withdrawSQL = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(withdrawSQL)) {
                        preparedStatement.setBigDecimal(1, amount);
                        preparedStatement.setInt(2, accountId);
                        int rowsUpdated = preparedStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Средства успешно сняты.");
                            return true;
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
        return false;
    }

    private boolean isAccountExists(Connection connection, int accountId) throws SQLException {
        String checkAccountSQL = "SELECT account_id FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkAccountSQL)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public BigDecimal getCurrentBalance(String username) {
        String getBalanceSQL = """
                SELECT a.balance
                FROM account a
                JOIN client c ON a.client_id = c.client_id
                WHERE c.username = ?LIMIT 1""";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getBalanceSQL)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("balance");
                } else {
                    throw new SQLException("Счет не найден.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getAccountIdByNumber(String number) {
        String getBalanceSQL = """
                SELECT account_id
                FROM account
                WHERE account.account_number = ?""";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getBalanceSQL)) {
            preparedStatement.setString(1, number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("account_id");
                } else {
                    throw new SQLException("Счет не найден.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
