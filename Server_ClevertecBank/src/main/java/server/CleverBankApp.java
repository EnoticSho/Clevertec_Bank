package server;

import server.dao.AccountDAO;

import java.math.BigDecimal;

public class CleverBankApp {
    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.depositFunds(1, new BigDecimal("1000.00"));
        accountDAO.withdrawFunds(2, new BigDecimal("500.00"));
    }
}
