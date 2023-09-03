package dao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import server.dao.AccountDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.dto.AccountDTO;
import server.entity.CurrencyType;

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
        MockitoAnnotations.openMocks(this);
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        accountDAO = new AccountDAO(connectionManager);
    }

    @Test
    public void testGetAccountDTOById() throws SQLException {
        int accountId = 1;
        String accountNumber = "123456";
        String bankName = "Test Bank";
        String clientName = "John";
        BigDecimal balance = new BigDecimal("100.50");
        String currency = "USD";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("account_number")).thenReturn(accountNumber);
        when(resultSet.getString("name")).thenReturn(bankName);
        when(resultSet.getString("first_name")).thenReturn(clientName);
        when(resultSet.getBigDecimal("balance")).thenReturn(balance);
        when(resultSet.getString("currency")).thenReturn(currency);

        AccountDTO result = accountDAO.getAccountDTOById(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getAccountId());
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(bankName, result.getBankName());
        assertEquals(clientName, result.getClientName());
        assertEquals(balance, result.getAmount());
        assertEquals(CurrencyType.valueOf(currency), result.getCurrencyType());
    }

    @Test
    public void testGetAccountIdByClientLogin() throws SQLException {
        String login = "testUser";
        String password = "password123";
        int accountId = 1;

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("account_id")).thenReturn(accountId);

        Integer result = accountDAO.getAccountIdByClientLogin(login, password);

        assertNotNull(result);
        assertEquals(accountId, result);
    }

    @Test
    public void testWithdraw() throws SQLException {
        int accountId = 1;
        BigDecimal amount = new BigDecimal("50.50");

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.withdraw(accountId, amount);

        assertTrue(result);
    }

    @Test
    public void testGetCurrentBalance() throws SQLException {
        int accountId = 1;
        BigDecimal balance = new BigDecimal("200.00");

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBigDecimal("balance")).thenReturn(balance);

        BigDecimal result = accountDAO.getCurrentBalance(accountId);

        assertNotNull(result);
        assertEquals(balance, result);
    }

    @Test
    public void testGetAccountIdByNumber() throws SQLException {
        String number = "123456";
        int accountId = 1;

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("account_id")).thenReturn(accountId);

        Integer result = accountDAO.getAccountIdByNumber(number);

        assertNotNull(result);
        assertEquals(accountId, result);
    }

    @Test
    public void testAddPercentByAccount() throws SQLException {
        Double percent = 5.0;

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.addPercentByAccount(percent);

        assertTrue(result);
    }

    @Test
    public void testGetAccountDTOByIdNotFound() throws SQLException {
        int accountId = 1;

        when(resultSet.next()).thenReturn(false);

        AccountDTO result = accountDAO.getAccountDTOById(accountId);

        assertNull(result);
    }

    @Test
    public void testGetAccountIdByClientLoginInvalidCredentials() throws SQLException {
        String login = "invalidUser";
        String password = "wrongPassword";

        when(resultSet.next()).thenReturn(false);

        Integer result = accountDAO.getAccountIdByClientLogin(login, password);

        assertNull(result);
    }

    @Test
    public void testDepositNoRowsUpdated() throws SQLException {
        int accountId = 1;
        BigDecimal amount = new BigDecimal("100.50");

        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = accountDAO.deposit(accountId, amount);

        assertFalse(result);
    }

    @Test
    public void testGetCurrentBalanceAccountNotFound() throws SQLException {
        int accountId = 999;

        when(resultSet.next()).thenReturn(false);

        BigDecimal result = accountDAO.getCurrentBalance(accountId);

        assertNull(result);
    }

    @Test
    public void testGetAccountIdByNumberInvalidAccount() throws SQLException {
        String number = "999999";

        when(resultSet.next()).thenReturn(false);

        Integer result = accountDAO.getAccountIdByNumber(number);

        assertNull(result);
    }
}
