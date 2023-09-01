package server.service;

import server.dao.BankDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;

import java.sql.SQLException;
import java.util.Optional;

public class BankService {
    private final BankDAO bankDAO;

    /**
     * Constructs a new instance of the BankService with a given connection manager.
     *
     * @param connectionManager The database connection manager used for initializing the BankDAO.
     */
    public BankService(DatabaseConnectionManager connectionManager) {
        this.bankDAO = new BankDAO(connectionManager);
    }

    /**
     * Creates a new bank record in the database.
     *
     * @param bank The bank entity to be persisted.
     * @throws SQLException If there's an error during the database operation.
     */
    public void createBank(Bank bank) throws SQLException {
        bankDAO.createBank(bank);
    }

    /**
     * Retrieves a bank entity based on its ID.
     *
     * @param id The ID of the bank to be retrieved.
     * @return Returns the bank entity if found.
     * @throws SQLException If there's an error during the database operation.
     */
    public Bank getBank(int id) throws SQLException {
        Optional<Bank> bank = bankDAO.getBank(id);
        return bank.get();
    }

    /**
     * Updates the details of a given bank in the database.
     *
     * @param bank The bank entity with updated details.
     * @throws SQLException If there's an error during the database operation.
     */
    public void updateBank(Bank bank) throws SQLException {
        bankDAO.updateBank(bank);
    }

    /**
     * Deletes a bank record based on its ID.
     *
     * @param id The ID of the bank to be deleted.
     * @throws SQLException If there's an error during the database operation.
     */
    public void deleteBank(int id) throws SQLException {
        bankDAO.deleteBank(id);
    }
}
