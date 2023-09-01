package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dao.ClientDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class ClientDAOTest {

    private ClientDAO clientDAO;
    private DatabaseConnectionManager connectionManager;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        connectionManager = mock(DatabaseConnectionManager.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        clientDAO = new ClientDAO(connectionManager);
    }

    @Test
    public void testGetClient() throws SQLException {
        int testClientId = 1;
        when(connectionManager.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        when(mockResultSet.getInt("clientId")).thenReturn(testClientId);
        when(mockResultSet.getString("firstName")).thenReturn("John");
        when(mockResultSet.getString("lastName")).thenReturn("Doe");
        when(mockResultSet.getString("username")).thenReturn("johndoe");
        when(mockResultSet.getString("password")).thenReturn("password");

        Client retrievedClient = clientDAO.getClient(testClientId);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setInt(1, testClientId);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testUpdateClient() throws SQLException {
        Client testClient = new Client();
        testClient.setClientId(1);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setUsername("johndoe");
        testClient.setPassword("password");

        when(connectionManager.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        clientDAO.updateClient(testClient);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(1, "John");
        verify(mockPreparedStatement, times(1)).setString(2, "Doe");
        verify(mockPreparedStatement, times(1)).setString(3, "johndoe");
        verify(mockPreparedStatement, times(1)).setString(4, "password");
        verify(mockPreparedStatement, times(1)).setInt(5, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteClient() throws SQLException {
        int testClientId = 1;
        when(connectionManager.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        clientDAO.deleteClient(testClientId);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setInt(1, testClientId);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
