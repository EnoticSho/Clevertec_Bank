package server.service;

import server.dao.AccountDAO;

import java.math.BigDecimal;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
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
}
