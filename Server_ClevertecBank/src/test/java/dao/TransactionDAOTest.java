package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.dao.TransactionDAO;
import server.dbConnection.DatabaseConnectionManager;
import server.entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionDAOTest {

    @Mock
    private DatabaseConnectionManager connectionManager;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private TransactionDAO transactionDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(connectionManager.getConnection()).thenReturn(mockConnection);
        transactionDAO = new TransactionDAO(connectionManager);
    }

    @Test
    void createTransaction_nullTransaction_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDAO.createTransaction(null));
    }
}
