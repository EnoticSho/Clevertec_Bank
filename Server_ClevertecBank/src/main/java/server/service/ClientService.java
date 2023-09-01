package server.service;

import server.dao.ClientDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Client;

import java.sql.SQLException;


/**
 * This service class provides methods to manage client-related operations.
 * It serves as a bridge between the higher-level application logic and the data access layer, ensuring
 * that client-related requests are executed appropriately in the database.
 */
public class ClientService {

    private final ClientDAO clientDAO;

    /**
     * Constructs a new instance of the ClientService with a given connection manager.
     *
     * @param connectionManager The database connection manager used for initializing the ClientDAO.
     */
    public ClientService(DatabaseConnectionManager connectionManager) {
        this.clientDAO = new ClientDAO(connectionManager);
    }

    /**
     * Creates a new client record in the database.
     *
     * @param client The client entity to be persisted.
     * @throws SQLException If there's an error during the database operation.
     */
    public void createClient(Client client) throws SQLException {
        clientDAO.createClient(client);
    }

    /**
     * Retrieves a client entity based on its ID.
     *
     * @param id The ID of the client to be retrieved.
     * @return Returns the client entity if found.
     * @throws SQLException If there's an error during the database operation.
     */
    public Client getClient(int id) throws SQLException {
        return clientDAO.getClient(id);
    }

    /**
     * Updates the details of a given client in the database.
     *
     * @param client The client entity with updated details.
     * @throws SQLException If there's an error during the database operation.
     */
    public void updateClient(Client client) throws SQLException {
        clientDAO.updateClient(client);
    }

    /**
     * Deletes a client record based on its ID.
     *
     * @param id The ID of the client to be deleted.
     * @throws SQLException If there's an error during the database operation.
     */
    public void deleteClient(int id) throws SQLException {
        clientDAO.deleteClient(id);
    }
}
