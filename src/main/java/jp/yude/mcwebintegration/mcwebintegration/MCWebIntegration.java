package jp.yude.mcwebintegration.mcwebintegration;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public final class MCWebIntegration extends JavaPlugin {

    public static Config config;
    public static Database database;
    public static MCWebIntegration instance;
    public static LuckPerms api;
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

        // Obtaining an instance of LuckPerms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        // Run web server for REST API
        connection = Database.Database();
        new HttpServer(connection, this);
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

    public static LuckPerms getLuckPermsAPI() {
        return api;
    }

}
