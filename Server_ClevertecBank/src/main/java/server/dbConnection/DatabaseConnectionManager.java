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
            Map<String, Object> config = new ConfigurationLoader().loadConfig();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl((String) config.get("dbUrl"));
            hikariConfig.setUsername((String) config.get("dbUsername"));
            hikariConfig.setPassword((String) config.get("dbPassword"));

            dataSource = new HikariDataSource(hikariConfig);

        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize HikariCP", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
