package server.dao;

import lombok.extern.slf4j.Slf4j;
import server.dto.AccountDTO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.CurrencyType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for interacting with account-related data in the database.
 * Provides methods to retrieve, update, and manipulate account data.
 * <p>
 * This DAO utilizes the {@link DatabaseConnectionManager} to manage database connections and operations.
 * </p>
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
     * Retrieves the account details by account ID.
     *
     * @param accountId The unique ID of the account to be retrieved.
     * @return An {@link AccountDTO} instance with the account details or null if not found.
     */
    public AccountDTO getAccountDTOById(int accountId) {
        String query = """
                SELECT a.account_number, a.balance, a.currency, b.name, c.first_name
                FROM Account a
                JOIN bank b ON a.bank_id = b.bank_id
                JOIN client c ON a.client_id = c.client_id
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
                    accountDTO.setClientName(resultSet.getString("first_name"));
                    accountDTO.setAmount(resultSet.getBigDecimal("balance"));
                    accountDTO.setCurrencyType(CurrencyType.valueOf(resultSet.getString("currency")));
                    return accountDTO;
                }
            }
        } catch (SQLException e) {
            log.error("SQL exception encountered: ", e);
        }

        return null;
    }

    /**
     * Fetches the account ID associated with the provided client login credentials.
     *
     * @param login    The client's login username.
     * @param password The client's password.
     * @return The account ID associated with the client or null if not found.
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
     * Adds a deposit to a specified account.
     *
     * @param accountId The unique ID of the account.
     * @param amount    The amount to deposit.
     * @return true if the deposit is successful, false otherwise.
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
     * Deducts a withdrawal amount from a specified account.
     *
     * @param accountId The unique ID of the account.
     * @param amount    The amount to withdraw.
     * @return true if the withdrawal is successful, false otherwise.
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
     * Fetches the current balance of a specified account.
     *
     * @param accountId The unique ID of the account.
     * @return The current balance of the account or null if not found.
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
     * Gets the account ID associated with a provided account number.
     *
     * @param number The account number to search for.
     * @return The account ID associated with the account number or null if not found.
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
     * Increases the balance of all accounts by a specified percentage.
     *
     * @param per The percentage by which to increase the balances.
     * @return true if the operation is successful, false otherwise.
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
