package jp.yude.mcwebintegration.mcwebintegration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Event implements Listener {
    @EventHandler
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Connection connection = Database.Database();
        // Save player's data to db
        String sql_search = "SELECT * FROM `timestamp` WHERE uuid = '" + player.getUniqueId() +"';";
        try {
            PreparedStatement stmt_search = connection.prepareStatement(sql_search);
            ResultSet results_search = stmt_search.executeQuery();
            if (results_search.next()) {
                String sql = "UPDATE `timestamp` SET `last` = ? WHERE `uuid` = ?;";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setLong(1, System.currentTimeMillis() / 1000L);
                stmt.setString(2, player.getUniqueId().toString());
                stmt.executeUpdate();
            } else {
                String sql = "INSERT INTO `timestamp` (uuid, last, first) VALUES (?, ?, ?);";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setLong(2, System.currentTimeMillis() / 1000L);
                stmt.setLong(3, player.getFirstPlayed() / 1000L);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // In case MCWebIntegration is loaded after server starts
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Connection connection = Database.Database();
        // Save player's data to db
        String sql_search = "SELECT * FROM `timestamp` WHERE uuid = '" + player.getUniqueId() +"';";
        try {
            PreparedStatement stmt_search = connection.prepareStatement(sql_search);
            ResultSet results_search = stmt_search.executeQuery();
            if (results_search.next()) {
                String sql = "UPDATE `timestamp` SET `last` = ? WHERE `uuid` = ?;";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setLong(1, System.currentTimeMillis() / 1000L);
                stmt.setString(2, player.getUniqueId().toString());
            } else {
                String sql = "INSERT INTO `timestamp` (uuid, last, first) VALUES (?, ?, ?);";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setLong(2, System.currentTimeMillis() / 1000L);
                stmt.setLong(3, player.getFirstPlayed() / 1000L);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
