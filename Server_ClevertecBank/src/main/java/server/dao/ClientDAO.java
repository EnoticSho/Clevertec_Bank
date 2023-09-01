package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing client-related data in the database.
 * Provides methods to create, retrieve, update, and delete client records.
 * <p>
 * Utilizes the {@link DatabaseConnectionManager} to manage and perform database operations.
 * </p>
 */
public class ClientDAO {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructs a new ClientDAO instance with the specified database connection manager.
     *
     * @param connectionManager The manager handling database connections.
     */
    public ClientDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Inserts a new client record into the database.
     *
     * @param client The {@link Client} entity to be inserted.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void createClient(Client client) throws SQLException {
        String sql = """
                INSERT INTO client (first_name, last_name, username, password)
                VALUES (?, ?, ?, ?)""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getUsername());
            preparedStatement.setString(4, client.getPassword());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Retrieves a client record by its ID.
     *
     * @param id The ID of the client to be fetched.
     * @return The {@link Client} entity if found, or null if not found.
     * @throws SQLException If any SQL-related error occurs.
     */
    public Client getClient(int id) throws SQLException {
        String sql = """
                SELECT client_id, first_name, last_name, username, password
                FROM client
                WHERE client_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setClientId(rs.getInt("clientId"));
                    client.setFirstName(rs.getString("firstName"));
                    client.setLastName(rs.getString("lastName"));
                    client.setUsername(rs.getString("username"));
                    client.setPassword(rs.getString("password"));
                    return client;
                }
            }
        }
        return null;
    }

    /**
     * Updates the details of a client in the database.
     *
     * @param client The {@link Client} entity with updated details.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void updateClient(Client client) throws SQLException {
        String sql = """
                UPDATE client
                SET first_name = ?, last_name = ?, username = ?, password = ?
                WHERE client_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getUsername());
            preparedStatement.setString(4, client.getPassword());
            preparedStatement.setInt(5, client.getClientId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Deletes a client record from the database by its ID.
     *
     * @param id The ID of the client to be deleted.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void deleteClient(int id) throws SQLException {
        String sql = """
                DELETE FROM client
                WHERE client_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
