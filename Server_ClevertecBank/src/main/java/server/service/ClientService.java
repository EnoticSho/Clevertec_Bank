package server.service;

import server.dao.BankDAO;
import server.dao.ClientDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;
import server.entity.Client;

import java.sql.SQLException;

public class ClientService {

    private final ClientDAO clientDAO;

    public ClientService(DatabaseConnectionManager connectionManager) {
        this.clientDAO = new ClientDAO(connectionManager);
    }

    public void createClient(Client client) throws SQLException {
        clientDAO.createClient(client);
    }

    public Client getClient(int id) throws SQLException {
        return clientDAO.getClient(id);
    }

    public void updateClient(Client client) throws SQLException {
        clientDAO.updateClient(client);
    }

    public void deleteClient(int id) throws SQLException {
        clientDAO.deleteClient(id);
    }
}
