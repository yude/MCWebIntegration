package jp.yude.mcwebintegration.mcwebintegration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    // Connect to MySQL server

    public static Connection connection;

    public static Connection Database() {
        // MySQL configurations / variables
        String host = MCWebIntegration.config.getHost();
        Integer db_port = MCWebIntegration.config.getDbPort();
        String database = MCWebIntegration.config.getDatabase();
        String user = MCWebIntegration.config.getUser();
        String password = MCWebIntegration.config.getPassword();
        String url = "jdbc:mysql://" + host + ":" + db_port + "/" + database;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Initialize table
        String sql = "CREATE TABLE IF NOT EXISTS `timestamp` (" +
                " `uuid` VARCHAR(50) COLLATE utf8mb4_unicode_ci," +
                " `last` BIGINT(50) COLLATE utf8mb4_unicode_ci," +
                " `first` BIGINT(50) COLLATE utf8mb4_unicode_ci," +
                " UNIQUE (`uuid`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;" ;
            try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
    }
}
