package jp.yude.mcwebintegration.mcwebintegration;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static spark.Spark.*;
import static spark.Spark.get;

public class HttpServer {
    public HttpServer(Connection connection) {
        // Specify port to expose for web API server.
        Integer web_port = MCWebIntegration.instance.getConfig().getInt("web_port");
        port(web_port);

        // Enable Cross-Origin Resource Sharing (CORS) policy.
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }
                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }
                    return "OK";
                });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // Provide whether the specified player is online.
        get("/online/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(req.params(":uuid")));
                if (player.isOnline()) {
                    return "true";
                } else {
                    return "false";
                }
            } else {
                return "false";
            }
        });

        // Provide when the specified player logged in to the server last time.
        get("/last/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                String sql = "SELECT * FROM `players` WHERE uuid = '" + req.params(":uuid") +"';";
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet results = stmt.executeQuery();
                if (results.next()) {
                    return results.getLong("last");
                } else {
                    return "not_found";
                }
            } else {
                return "invalid_uuid";
            }
        });

        // Provide when the specified player logged in to the server for the first time.
        get("/first/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                String sql = "SELECT * FROM `players` WHERE uuid = '" + req.params(":uuid") +"';";
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet results = stmt.executeQuery();
                if (results.next()) {
                    return results.getLong("first");
                } else {
                    return "not_found";
                }
            } else {
                return "invalid_uuid";
            }
        });

    }
}
