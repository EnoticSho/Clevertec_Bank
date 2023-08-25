package server.dbConnection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import server.config.ConfigurationLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnectionManager {

    private final HikariDataSource dataSource;

    public DatabaseConnectionManager() {
        try {
            ConfigurationLoader configLoader = new ConfigurationLoader("Server_ClevertecBank/src/main/resources/application.yml");
            Map<String, Object> config = configLoader.loadConfig();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl((String) config.get("dbUrl"));
            hikariConfig.setUsername((String) config.get("dbUsername"));
            hikariConfig.setPassword((String) config.get("dbPassword"));

            // Additional HikariCP settings can be set on the hikariConfig object if needed.

            dataSource = new HikariDataSource(hikariConfig);

        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize HikariCP", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Call this method when shutting down your application to release all resources
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
