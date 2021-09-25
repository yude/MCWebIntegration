package jp.yude.mcwebintegration.mcwebintegration;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class MCWebIntegration extends JavaPlugin {

    public static Config config;
    public static Database database;
    public static MCWebIntegration instance;

    public static Connection connection;

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        getLogger().info("Enabled.");
        config = new Config(this);

        // MySQL connection
        database = new Database();

        // Register events
        getServer().getPluginManager().registerEvents(new Event(), this);

        // Run web server for REST API
        connection = Database.Database();
        new HttpServer(connection);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Close connection to MySQL server
        try {
            if (connection!=null && !connection.isClosed()){
                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        getLogger().info("Disabled.");
    }
}
