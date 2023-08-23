package server.dbConnection;

import server.config.ConfigurationLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnectionManager {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                ConfigurationLoader configLoader = new ConfigurationLoader("Server_ClevertecBank/src/main/resources/application.yml");
                Map<String, Object> config = configLoader.loadConfig();
                String url = (String) config.get("dbUrl");
                String username = (String) config.get("dbUsername");
                String password = (String) config.get("dbPassword");
                connection = DriverManager.getConnection(url, username, password);
            } catch (IOException e) {
                e.printStackTrace();
                throw new SQLException("Unable to read application.yml file.");
            }
        }
        return connection;
    }
}
