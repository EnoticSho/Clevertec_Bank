package server.dao;

import lombok.extern.slf4j.Slf4j;
import server.dto.AccountDTO;
import server.dbConnection.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account Data Access Object for handling database operations related to accounts.
 */
@Slf4j
public class AccountDAO {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructor for AccountDAO.
     *
     * @param connectionManager Database connection manager.
     */
    public AccountDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Retrieve AccountDTO by account ID.
     *
     * @param accountId The account ID.
     * @return The AccountDTO object or null if not found.
     */
    public AccountDTO getAccountDTOById(int accountId) {
        // SQL Query for fetching account details
        String query = """
                SELECT a.account_number, b.name
                FROM Account a
                JOIN bank b ON a.bank_id = b.bank_id
                WHERE a.account_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    AccountDTO accountDTO = new AccountDTO();
                    accountDTO.setAccountId(accountId);
                    accountDTO.setAccountNumber(resultSet.getString("account_number"));
                    accountDTO.setBankName(resultSet.getString("name"));
                    return accountDTO;
                }
            }
        } catch (SQLException e) {
            log.error("SQL exception encountered: ", e);
        }

        return null;
    }

    /**
     * Retrieves an account ID based on client login credentials.
     *
     * @param login    Client login username.
     * @param password Client password.
     * @return The account ID or null if not found.
     */
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
            log.error("SQL exception encountered while fetching account ID by client login: ", e);
        }

        return null;
    }

    /**
     * Deposits an amount to the account with the specified account ID.
     *
     * @param accountId Account ID.
     * @param amount    Amount to be deposited.
     * @return true if the deposit was successful, false otherwise.
     */
    public boolean deposit(int accountId, BigDecimal amount) {
        String depositSQL = """
                UPDATE account
                SET balance = balance + ?
                WHERE account_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(depositSQL)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, accountId);
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("SQL exception encountered while depositing: ", e);
        }

        return false;
    }

    /**
     * Withdraws an amount from the account with the specified account ID.
     *
     * @param accountId Account ID.
     * @param amount    Amount to be withdrawn.
     * @return true if the withdrawal was successful, false otherwise.
     */
    public boolean withdraw(int accountId, BigDecimal amount) {
        String withdrawSQL = """
                UPDATE account
                SET balance = balance - ?
                WHERE account_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(withdrawSQL)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, accountId);
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("SQL exception encountered during withdrawal: ", e);
        }

        return false;
    }

    /**
     * Gets the current balance of the specified account.
     *
     * @param accountId Account ID.
     * @return Current balance or null if not found.
     */
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
            log.error("SQL exception encountered fetching balance: ", e);
        }

        return null;
    }

    /**
     * Retrieves an account ID by account number.
     *
     * @param number Account number.
     * @return Account ID or null if not found.
     */
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
            log.error("SQL exception encountered fetching account ID by number: ", e);
        }

        return null;
    }

    /**
     * Adds a percentage to all account balances.
     *
     * @param per Percentage to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addPercentByAccount(Double per) {
        String update = """
                UPDATE account
                SET balance = balance / 100 * ( ? + 100 )""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {

            preparedStatement.setDouble(1, per);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("SQL exception encountered adding percentage to accounts: ", e);
        }

        return false;
    }
}
