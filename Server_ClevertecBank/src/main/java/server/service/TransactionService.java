    package server.service;

    import server.AccountDTO;
    import server.dao.TransactionDAO;
    import server.dbConnection.DatabaseConnectionManager;
    import server.entity.CurrencyType;
    import server.entity.Transaction;
    import server.entity.TransactionType;

    import java.io.IOException;
    import java.math.BigDecimal;
    import java.sql.SQLException;
    import java.sql.Timestamp;

    public class TransactionService {

        private final TransactionDAO transactionDAO;
        private final AccountService accountService;
        private final ReceiptService receiptService;
        private final DatabaseConnectionManager connectionManager;

        public TransactionService(DatabaseConnectionManager connectionManager) {
            this.receiptService = new ReceiptService();
            this.connectionManager = connectionManager;
            this.transactionDAO = new TransactionDAO(connectionManager);
            this.accountService = new AccountService(connectionManager);
        }

        public void deposit(int accountId, BigDecimal amount) throws SQLException, IOException {
            accountService.deposit(accountId, amount);
            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), null, accountId, TransactionType.Deposit);
            transactionDAO.createTransaction(transaction);
            AccountDTO accountDTOById = accountService.getAccountDTOById(accountId);
            receiptService.generateReceipt(TransactionType.Deposit, accountDTOById.getBankName(), accountDTOById.getAccountNumber(), accountDTOById.getBankName(), accountDTOById.getAccountNumber(), amount, CurrencyType.BYN);
        }

        public void withdraw(int accountId, BigDecimal amount) throws SQLException, IOException {
            accountService.withdraw(accountId, amount);
            Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), accountId, null, TransactionType.Withdrawal);
            transactionDAO.createTransaction(transaction);
            AccountDTO accountDTOById = accountService.getAccountDTOById(accountId);
            receiptService.generateReceipt(TransactionType.Withdrawal, accountDTOById.getBankName(), accountDTOById.getAccountNumber(), accountDTOById.getBankName(), accountDTOById.getAccountNumber(), amount, CurrencyType.BYN);
        }

        public void transfer(int fromAccountId, String number, BigDecimal amount) throws SQLException, IOException {
            try (var connection = connectionManager.getConnection()) {
                connection.setAutoCommit(false);

                Integer accountIdByNumber = accountService.getAccountIdByNumber(number);

                accountService.withdraw(fromAccountId, amount);
                accountService.deposit(accountIdByNumber, amount);
                AccountDTO accountDTOSender = accountService.getAccountDTOById(fromAccountId);
                AccountDTO accountDTOReceipent = accountService.getAccountDTOById(fromAccountId);
                Transaction transaction = new Transaction(null, amount, new Timestamp(System.currentTimeMillis()), fromAccountId, accountIdByNumber, TransactionType.Transfer);
                transactionDAO.createTransaction(transaction);

                connection.commit();
                receiptService.generateReceipt(TransactionType.Transfer, accountDTOSender.getBankName(), accountDTOSender.getAccountNumber(), accountDTOReceipent.getBankName(), accountDTOReceipent.getAccountNumber(), amount, CurrencyType.BYN);
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
        }
    }