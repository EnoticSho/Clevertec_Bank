package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.dao.TransactionDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;
import server.entity.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class TransactionDAOTest {

    @Mock
    private DatabaseConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private TransactionDAO transactionDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
        transaction.setSenderAccountId(1);
        transaction.setReceiverAccountId(2);
        transaction.setTransactionType(TransactionType.Deposit);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        transactionDAO.createTransaction(transaction);

        verify(connectionManager).getConnection();
        verify(preparedStatement).setBigDecimal(1, transaction.getAmount());
        verify(preparedStatement).executeUpdate();
    }
}
