package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing bank-related data in the database.
 * Provides methods to create, retrieve, update, and delete banks.
 * <p>
 * Utilizes the {@link DatabaseConnectionManager} to manage and perform database operations.
 * </p>
 */
public class BankDAO {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructs a new BankDAO instance with the specified database connection manager.
     *
     * @param connectionManager The manager handling database connections.
     */
    public BankDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Inserts a new bank record into the database.
     *
     * @param bank The {@link Bank} entity to be inserted.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void createBank(Bank bank) throws SQLException {
        String SQL = """
             INSERT INTO bank(name)
             VALUES (?)""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setString(1, bank.getName());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Retrieves a bank record by its ID.
     *
     * @param id The ID of the bank to be fetched.
     * @return An {@link Optional} containing the bank entity if found, or empty if not found.
     * @throws SQLException If any SQL-related error occurs.
     */
    public Optional<Bank> getBank(int id) throws SQLException {
        String SQL = """
            SELECT *
            FROM bank
            WHERE bank_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Bank bank = new Bank();
                bank.setBankId(rs.getInt("bank_id")); // Assuming column name is "bank_id"
                bank.setName(rs.getString("name"));
                return Optional.of(bank);
            }
        }
        return Optional.empty();
    }

    /**
     * Updates the details of a bank in the database.
     *
     * @param bank The {@link Bank} entity with updated details.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void updateBank(Bank bank) throws SQLException {
        String SQL = """
             UPDATE bank
             SET name = ?
             WHERE bank_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setString(1, bank.getName());
            preparedStatement.setInt(2, bank.getBankId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Deletes a bank record from the database by its ID.
     *
     * @param id The ID of the bank to be deleted.
     * @throws SQLException If any SQL-related error occurs.
     */
    public void deleteBank(int id) throws SQLException {
        String SQL = """
                DELETE FROM bank
                WHERE bank_id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
