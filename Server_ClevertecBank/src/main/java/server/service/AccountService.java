package server.service;

import server.AccountDTO;
import server.dao.AccountDAO;
import server.dbConnection.DatabaseConnectionManager;
import java.math.BigDecimal;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(DatabaseConnectionManager connectionManager) {
        this.accountDAO = new AccountDAO(connectionManager);
    }

    public BigDecimal getCurrentBalance(int accountId) {
        return accountDAO.getCurrentBalance(accountId);
    }

    public Integer getAccountIdByClientLogin(String login, String password) {
        return accountDAO.getAccountIdByClientLogin(login, password);
    }

    public void deposit(int accountId, BigDecimal amount) {
        accountDAO.deposit(accountId, amount);
    }

    public void withdraw(int accountId, BigDecimal amount) {
        accountDAO.withdraw(accountId, amount);
    }

    public Integer getAccountIdByNumber(String number) {
        return accountDAO.getAccountIdByNumber(number);
    }

    public void addPercentByAccount(Double per) {
        accountDAO.addPercentByAccount(per);
    }

    public AccountDTO getAccountDTOById(int accountId) {
        return accountDAO.getAccountDTOById(accountId);
    }
}