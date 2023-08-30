package server.dbConnection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import server.config.ConfigurationLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Manages database connections using HikariCP connection pool.
 */
@Slf4j
public class DatabaseConnectionManager {

    private final HikariDataSource dataSource;

    /**
     * Constructs a DatabaseConnectionManager and initializes HikariCP.
     * Configuration is loaded using ConfigurationLoader.
     *
     * @throws RuntimeException if the initialization fails.
     */
    public DatabaseConnectionManager() {
        try {
            Map<String, Object> config = new ConfigurationLoader().loadConfig();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl((String) config.get("dbUrl"));
            hikariConfig.setUsername((String) config.get("dbUsername"));
            hikariConfig.setPassword((String) config.get("dbPassword"));

            dataSource = new HikariDataSource(hikariConfig);

        } catch (IOException e) {
            log.error("Unable to initialize HikariCP", e);
            throw new RuntimeException("Unable to initialize HikariCP", e);
        }
    }

    /**
     * Provides a database connection from the pool.
     *
     * @return A Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the data source, releasing all resources.
     */
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
