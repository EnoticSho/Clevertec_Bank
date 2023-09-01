package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO {

    private final DatabaseConnectionManager connectionManager;

    public ClientDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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
