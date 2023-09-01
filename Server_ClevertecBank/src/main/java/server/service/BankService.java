package server.service;

import server.dao.BankDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;

import java.sql.SQLException;

public class BankService {
    private final BankDAO bankDAO;

    public BankService(DatabaseConnectionManager connectionManager) {
        this.bankDAO = new BankDAO(connectionManager);
    }

    public void createBank(Bank bank) throws SQLException {
        bankDAO.createBank(bank);
    }

    public Bank getBank(int id) throws SQLException {
        return bankDAO.getBank(id);
    }

    public void updateBank(Bank bank) throws SQLException {
        bankDAO.updateBank(bank);
    }

    public void deleteBank(int id) throws SQLException {
        bankDAO.deleteBank(id);
    }
}
