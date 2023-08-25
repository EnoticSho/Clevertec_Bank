package server.service;

import server.dao.AccountDAO;
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
    private final AccountDAO accountDAO;
    private final Connection connection = DatabaseConnectionManager.getConnection();

    public TransactionService(TransactionDAO transactionDAO, AccountDAO accountDAO) throws SQLException {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
    }

    public void deposit(int accountId, BigDecimal amount) throws SQLException {
        accountDAO.deposit(accountId, amount); // Assuming this method exists in AccountDAO.
        Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), null, accountId, TransactionType.Deposit);
        transactionDAO.createTransaction(transaction);
    }

    public void withdraw(int accountId, BigDecimal amount) throws SQLException {
        accountDAO.withdraw(accountId, amount); // Assuming this method exists in AccountDAO.
        Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), accountId, null, TransactionType.Withdrawal);
        transactionDAO.createTransaction(transaction);
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws SQLException {
        try {
            connection.setAutoCommit(false);

            accountDAO.withdraw(fromAccountId, amount);

            accountDAO.deposit(toAccountId, amount);

            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), fromAccountId, toAccountId, TransactionType.Transfer);
            transactionDAO.createTransaction(transaction);

            connection.commit();

        } catch (Exception ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
