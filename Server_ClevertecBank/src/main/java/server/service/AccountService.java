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
}
