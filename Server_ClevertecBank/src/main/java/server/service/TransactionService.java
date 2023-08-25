package server.service;

import server.dao.TransactionDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;
import server.entity.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final AccountService accountService;

    public TransactionService() throws SQLException {
        this.transactionDAO = new TransactionDAO();
        this.accountService = new AccountService();
    }

    public boolean deposit(int accountId, BigDecimal amount) throws SQLException {
        accountService.deposit(accountId, amount);
        Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), null, accountId, TransactionType.Deposit);
        transactionDAO.createTransaction(transaction);
        return true;
    }

    public boolean withdraw(int accountId, BigDecimal amount) throws SQLException {
        accountService.withdraw(accountId, amount);
        Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), accountId, null, TransactionType.Withdrawal);
        transactionDAO.createTransaction(transaction);
        return true;
    }

    public boolean transfer(int fromAccountId, String number, BigDecimal amount) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);

            Integer accountIdByNumber = accountService.getAccountIdByNumber(number);

            accountService.withdraw(fromAccountId, amount);
            accountService.deposit(accountIdByNumber, amount);

            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), fromAccountId, accountIdByNumber, TransactionType.Transfer);
            transactionDAO.createTransaction(transaction);

            connection.commit();

        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return true;
    }
}
