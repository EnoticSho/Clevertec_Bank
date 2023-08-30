package server.service;

import lombok.extern.slf4j.Slf4j;
import server.dto.AccountDTO;
import server.dao.AccountDAO;
import server.dbConnection.DatabaseConnectionManager;

import java.math.BigDecimal;

/**
 * This service class provides methods related to account operations.
 */
@Slf4j
public class AccountService {

    private final AccountDAO accountDAO;

    /**
     * Constructor to initialize the AccountDAO.
     *
     * @param connectionManager The database connection manager.
     */
    public AccountService(DatabaseConnectionManager connectionManager) {
        this.accountDAO = new AccountDAO(connectionManager);
    }

    /**
     * Retrieves the current balance for a given account ID.
     *
     * @param accountId The ID of the account.
     * @return The current balance of the account.
     */
    public BigDecimal getCurrentBalance(int accountId) {
        try {
            return accountDAO.getCurrentBalance(accountId);
        } catch (Exception e) {
            log.error("Error getting current balance for accountId: {}", accountId, e);
            throw new RuntimeException("Error retrieving account balance", e);
        }
    }

    /**
     * Retrieves the account ID associated with the given client login credentials.
     *
     * @param login    The client login.
     * @param password The client password.
     * @return The ID of the account associated with the client credentials.
     */
    public Integer getAccountIdByClientLogin(String login, String password) {
        try {
            return accountDAO.getAccountIdByClientLogin(login, password);
        } catch (Exception e) {
            log.error("Error getting accountId by client login", e);
            throw new RuntimeException("Error retrieving accountId", e);
        }
    }

    /**
     * Deposits a specified amount into the account associated with the given ID.
     *
     * @param accountId The ID of the account.
     * @param amount    The amount to be deposited.
     */
    public void deposit(int accountId, BigDecimal amount) {
        try {
            accountDAO.deposit(accountId, amount);
        } catch (Exception e) {
            log.error("Error depositing to accountId: {}", accountId, e);
            throw new RuntimeException("Error during deposit", e);
        }
    }

    /**
     * Withdraws a specified amount from the account associated with the given ID.
     *
     * @param accountId The ID of the account.
     * @param amount    The amount to be withdrawn.
     */
    public void withdraw(int accountId, BigDecimal amount) {
        try {
            accountDAO.withdraw(accountId, amount);
        } catch (Exception e) {
            log.error("Error withdrawing from accountId: {}", accountId, e);
            throw new RuntimeException("Error during withdrawal", e);
        }
    }

    /**
     * Retrieves the account ID associated with the given account number.
     *
     * @param number The account number.
     * @return The ID of the account associated with the given number.
     */
    public Integer getAccountIdByNumber(String number) {
        try {
            return accountDAO.getAccountIdByNumber(number);
        } catch (Exception e) {
            log.error("Error getting accountId by account number", e);
            throw new RuntimeException("Error retrieving accountId by number", e);
        }
    }

    /**
     * Adds a specified percentage to all accounts in the system.
     *
     * @param per The percentage value to be added.
     */
    public void addPercentByAccount(Double per) {
        try {
            accountDAO.addPercentByAccount(per);
        } catch (Exception e) {
            log.error("Error adding percentage to accounts", e);
            throw new RuntimeException("Error adding percentage", e);
        }
    }

    /**
     * Retrieves the account details (as a DTO) for a given account ID.
     *
     * @param accountId The ID of the account.
     * @return The AccountDTO object containing details of the specified account.
     */
    public AccountDTO getAccountDTOById(int accountId) {
        try {
            return accountDAO.getAccountDTOById(accountId);
        } catch (Exception e) {
            log.error("Error getting AccountDTO by accountId: {}", accountId, e);
            throw new RuntimeException("Error retrieving account details", e);
        }
    }
}