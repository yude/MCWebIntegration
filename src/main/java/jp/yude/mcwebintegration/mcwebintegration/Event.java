package jp.yude.mcwebintegration.mcwebintegration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Event implements Listener {
    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Connection connection = Database.Database();
        // Save player's data to db
        String sql_search = "SELECT * FROM `timestamp` WHERE uuid = '" + player.getUniqueId() +"';";
        try {
            PreparedStatement stmt_search = connection.prepareStatement(sql_search);
            ResultSet results_search = stmt_search.executeQuery();
            if (results_search.next()) {
                String sql = "UPDATE `timestamp` SET `last` = '" + System.currentTimeMillis() / 1000L + "' WHERE `uuid` = '" + player.getUniqueId() + "';";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setLong(1, System.currentTimeMillis() / 1000L);
                stmt.setString(2, player.getUniqueId().toString());

                stmt.executeUpdate();
            } else {
                String sql = "INSERT INTO `timestamp` (uuid, last, first) VALUES ('" + player.getUniqueId() + "', '" + System.currentTimeMillis() / 1000L + "', '" + player.getFirstPlayed() / 1000L + "');";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
