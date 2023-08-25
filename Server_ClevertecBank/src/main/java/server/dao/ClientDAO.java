package server.dao;

import server.dbConnection.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO {

    public boolean isClientExists(String login, String password) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            String checkAccountSQL = "SELECT username FROM client WHERE username = ? and Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(checkAccountSQL)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
