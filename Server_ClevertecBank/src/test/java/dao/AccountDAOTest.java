package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import server.dao.AccountDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.dto.AccountDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountDAOTest {

    @Mock
    private DatabaseConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private AccountDAO accountDAO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        accountDAO = new AccountDAO(connectionManager);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testGetAccountDTOById() throws SQLException {
        int accountId = 123;
        String accountNumber = "ABC123";
        String bankName = "BankCorp";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("account_number")).thenReturn(accountNumber);
        when(resultSet.getString("name")).thenReturn(bankName);

        AccountDTO accountDTO = accountDAO.getAccountDTOById(accountId);

        assertNotNull(accountDTO);
        assertEquals(accountId, accountDTO.getAccountId());
        assertEquals(accountNumber, accountDTO.getAccountNumber());
        assertEquals(bankName, accountDTO.getBankName());

        verify(connectionManager, times(1)).getConnection();
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, accountId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).getString("account_number");
        verify(resultSet, times(1)).getString("name");
    }

    @Test
    public void testDeposit() throws SQLException {
        int accountId = 123;
        BigDecimal amount = new BigDecimal("100.00");

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.deposit(accountId, amount);

        assertTrue(result);
        verify(preparedStatement, times(1)).setBigDecimal(1, amount);
        verify(preparedStatement, times(1)).setInt(2, accountId);
    }

    @Test
    public void testWithdraw() throws SQLException {
        int accountId = 123;
        BigDecimal amount = new BigDecimal("50.00");

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.withdraw(accountId, amount);

        assertTrue(result);
        verify(preparedStatement, times(1)).setBigDecimal(1, amount);
        verify(preparedStatement, times(1)).setInt(2, accountId);
    }

    @Test
    public void testGetCurrentBalance() throws SQLException {
        int accountId = 123;
        BigDecimal balance = new BigDecimal("200.00");

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBigDecimal("balance")).thenReturn(balance);

        BigDecimal result = accountDAO.getCurrentBalance(accountId);

        assertNotNull(result);
        assertEquals(balance, result);
        verify(preparedStatement, times(1)).setInt(1, accountId);
    }

    @Test
    public void testGetAccountDTOByIdNotFound() throws SQLException {
        int accountId = 123;

        when(resultSet.next()).thenReturn(false);

        AccountDTO accountDTO = accountDAO.getAccountDTOById(accountId);

        assertNull(accountDTO);
    }

    @Test
    public void testGetAccountIdByClientLoginNotFound() throws SQLException {
        String login = "testUser";
        String password = "wrongPassword";

        when(resultSet.next()).thenReturn(false);

        Integer accountId = accountDAO.getAccountIdByClientLogin(login, password);

        assertNull(accountId);
    }
}
