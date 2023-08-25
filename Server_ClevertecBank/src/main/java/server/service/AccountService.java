package server.service;

import server.dao.AccountDAO;
import server.dbConnection.DatabaseConnectionManager;
import java.math.BigDecimal;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(DatabaseConnectionManager connectionManager) {
        this.accountDAO = new AccountDAO(connectionManager);
    }

    public BigDecimal getCurrentBalance(String username) {
        return accountDAO.getCurrentBalance(username);
    }

    public Integer getAccountIdByClientLogin(String login, String password) {
        return accountDAO.getAccountIdByClientLogin(login, password);
    }

    public boolean deposit(int accountId, BigDecimal amount) {
        return accountDAO.deposit(accountId, amount);
    }

    public boolean withdraw(int accountId, BigDecimal amount) {
        return accountDAO.withdraw(accountId, amount);
    }

    public Integer getAccountIdByNumber(String number) {
        return accountDAO.getAccountIdByNumber(number);
    }

    public boolean addPercentByAccount(Double per) {
        return accountDAO.addPercentByAccount(per);
    }
}