package server.dao;

import server.dbConnection.DatabaseConnectionManager;
import server.entity.Bank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAO {

    private final DatabaseConnectionManager connectionManager;

    public BankDAO(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

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

    public Bank getBank(int id) throws SQLException {
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
                bank.setBankId(rs.getInt("bankId"));
                bank.setName(rs.getString("name"));
                return bank;
            }
        }
        return null;
    }

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
