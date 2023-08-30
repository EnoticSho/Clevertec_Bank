package server.service;

import lombok.extern.slf4j.Slf4j;
import server.dto.AccountDTO;
import server.dao.TransactionDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.CurrencyType;
import server.entity.Transaction;
import server.entity.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * This service class provides methods related to transaction operations.
 */
@Slf4j
public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final AccountService accountService;
    private final ReceiptService receiptService;
    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructor to initialize the DAOs and services.
     *
     * @param connectionManager The database connection manager.
     */
    public TransactionService(DatabaseConnectionManager connectionManager) {
        this.receiptService = new ReceiptService();
        this.connectionManager = connectionManager;
        this.transactionDAO = new TransactionDAO(connectionManager);
        this.accountService = new AccountService(connectionManager);
    }

    /**
     * Deposits a specified amount into an account.
     *
     * @param accountId The ID of the account.
     * @param amount The amount to deposit.
     */
    public void deposit(int accountId, BigDecimal amount) {
        try {
            accountService.deposit(accountId, amount);
            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), null, accountId, TransactionType.Deposit);
            transactionDAO.createTransaction(transaction);
            AccountDTO accountDTOById = accountService.getAccountDTOById(accountId);
            receiptService.generateReceipt(TransactionType.Deposit, accountDTOById.getBankName(), accountDTOById.getAccountNumber(), accountDTOById.getBankName(), accountDTOById.getAccountNumber(), amount, CurrencyType.BYN);
        } catch (IOException ex) {
            log.error("Error during deposit operation for accountId: {}", accountId, ex);
            throw new RuntimeException("Error processing deposit", ex);
        }
    }

    /**
     * Withdraws a specified amount from an account.
     *
     * @param accountId The ID of the account.
     * @param amount The amount to withdraw.
     */
    public void withdraw(int accountId, BigDecimal amount) {
        try {
            accountService.withdraw(accountId, amount);
            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), accountId, null, TransactionType.Withdrawal);
            transactionDAO.createTransaction(transaction);
            AccountDTO accountDTOById = accountService.getAccountDTOById(accountId);
            receiptService.generateReceipt(TransactionType.Withdrawal, accountDTOById.getBankName(), accountDTOById.getAccountNumber(), accountDTOById.getBankName(), accountDTOById.getAccountNumber(), amount, CurrencyType.BYN);
        } catch (IOException ex) {
            log.error("Error during withdrawal operation for accountId: {}", accountId, ex);
            throw new RuntimeException("Error processing withdrawal", ex);
        }
    }

    /**
     * Transfers a specified amount from one account to another and ensures transactional integrity.
     * Generates a receipt for the transaction upon successful completion.
     *
     * @param fromAccountId The ID of the source account.
     * @param number The account number of the target account to which the funds should be transferred.
     * @param amount The amount to be transferred.
     */
    public void transfer(int fromAccountId, String number, BigDecimal amount) {
        try (var connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            Integer accountIdByNumber = accountService.getAccountIdByNumber(number);
            accountService.withdraw(fromAccountId, amount);
            accountService.deposit(accountIdByNumber, amount);

            AccountDTO accountDTOSender = accountService.getAccountDTOById(fromAccountId);
            AccountDTO accountDTORecipient = accountService.getAccountDTOById(accountIdByNumber);

            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), fromAccountId, accountIdByNumber, TransactionType.Transfer);
            transactionDAO.createTransaction(transaction);
            connection.commit();
            receiptService.generateReceipt(TransactionType.Transfer, accountDTOSender.getBankName(), accountDTOSender.getAccountNumber(), accountDTORecipient.getBankName(), accountDTORecipient.getAccountNumber(), amount, CurrencyType.BYN);
        } catch (Exception ex) {
            log.error("Error during transfer operation from accountId: {} to account number: {}", fromAccountId, number, ex);
            try {
                connectionManager.getConnection().rollback();
            } catch (SQLException e) {
                log.error("Failed to rollback transaction", e);
            }
            throw new RuntimeException("Error processing transfer", ex);
        }
    }
}