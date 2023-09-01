package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.dao.BankDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class BankDAOTest {

    @Mock
    private DatabaseConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private BankDAO bankDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        bankDAO = new BankDAO(connectionManager);

        when(connectionManager.getConnection()).thenReturn(connection);
    }

    @Test
    void testCreateBank() throws SQLException {
        Bank bank = new Bank();
        bank.setName("Sample Bank");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        bankDAO.createBank(bank);

        verify(preparedStatement).setString(1, bank.getName());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetBank() throws SQLException {
        int id = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        bankDAO.getBank(id);

        verify(preparedStatement).setInt(1, id);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testUpdateBank() throws SQLException {
        Bank bank = new Bank();
        bank.setBankId(1);
        bank.setName("Updated Bank");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        bankDAO.updateBank(bank);

        verify(preparedStatement).setString(1, bank.getName());
        verify(preparedStatement).setInt(2, bank.getBankId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testDeleteBank() throws SQLException {
        int id = 1;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        bankDAO.deleteBank(id);

        verify(preparedStatement).setInt(1, id);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testSQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        try {
            bankDAO.createBank(new Bank());
        } catch (SQLException e) {
            assert true;
        }
    }
}
