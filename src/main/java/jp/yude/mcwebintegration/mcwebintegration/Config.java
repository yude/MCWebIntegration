package jp.yude.mcwebintegration.mcwebintegration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    private final Plugin plugin;
    private FileConfiguration config = null;

    private String host;
    private Integer db_port;
    private String database;
    private String user;
    private String password;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        // Save configuration to file
        plugin.saveDefaultConfig();
        if (config != null) { // When called via reload
            plugin.reloadConfig();
        }
        config = plugin.getConfig();

        host = config.getString("host");
        db_port = config.getInt("db_port");
        database = config.getString("database");
        user = config.getString("user");
        password = config.getString("password");
    }

    public String getHost() {
        return host;
    }
    public Integer getDbPort() {
        return db_port;
    }public String getDatabase() {
        return database;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
}
