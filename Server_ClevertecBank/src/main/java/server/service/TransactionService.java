package server.service;

import server.dao.TransactionDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;
import server.entity.TransactionType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final AccountService accountService;
    private final DatabaseConnectionManager connectionManager;

    public TransactionService(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.transactionDAO = new TransactionDAO(connectionManager);
        this.accountService = new AccountService(connectionManager);
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
        try (var connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            Integer accountIdByNumber = accountService.getAccountIdByNumber(number);

            accountService.withdraw(fromAccountId, amount);
            accountService.deposit(accountIdByNumber, amount);

            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), fromAccountId, accountIdByNumber, TransactionType.Transfer);
            transactionDAO.createTransaction(transaction);

            connection.commit();

        } catch (Exception ex) {
            try {
                if (connectionManager != null) {
                    connectionManager.getConnection().rollback();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            throw ex;
        }
        return true;
    }
}