package server.dao;

import server.dbConnection.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    private final DatabaseConnectionManager connectionManager;

    public AccountDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Integer getAccountIdByClientLogin(String login, String password) {
        String checkAccountSQL = """
                SELECT account_id
                FROM account
                JOIN client ON account.client_id = client.client_id
                WHERE client.username = ?
                AND client.password = ?
                AND account.bank_id = 1""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkAccountSQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("account_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deposit(int accountId, BigDecimal amount) {
        String depositSQL = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(depositSQL)) {
                preparedStatement.setBigDecimal(1, amount);
                preparedStatement.setInt(2, accountId);
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean withdraw(int accountId, BigDecimal amount) {
        String withdrawSQL = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(withdrawSQL)) {
                preparedStatement.setBigDecimal(1, amount);
                preparedStatement.setInt(2, accountId);
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasSufficientBalance(int accountId, BigDecimal amount) throws SQLException {
        String getBalanceSQL = "SELECT balance FROM account WHERE account_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getBalanceSQL)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    return balance.compareTo(amount) >= 0;
                }
            }
        }
        return false;
    }

    public BigDecimal getCurrentBalance(int accountId) {
        String getBalanceSQL = """
                SELECT balance
                FROM account
                WHERE account_id = ?""";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getBalanceSQL)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getAccountIdByNumber(String number) {
        String getAccountIdSQL = """
                SELECT account_id
                FROM account
                WHERE account.account_number = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAccountIdSQL)) {
            preparedStatement.setString(1, number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("account_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addPercentByAccount(Double per) {
        String update = "UPDATE account SET balance = balance / 100 * ( ? + 100 )";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setDouble(1, per);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
