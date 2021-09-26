package jp.yude.mcwebintegration.mcwebintegration;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static jp.yude.mcwebintegration.mcwebintegration.MCWebIntegration.api;
import static jp.yude.mcwebintegration.mcwebintegration.MCWebIntegration.getLuckPermsAPI;
import static spark.Spark.*;
import static spark.Spark.get;

public class HttpServer {
    public HttpServer(Connection connection, Plugin plugin) {
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

        // Provide the specified player's the most important permission group on LuckPerms
        get("/group/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                // Convert UUID string to player object
                String result = "default";
                CompletableFuture<User> userFuture = getLuckPermsAPI().getUserManager().loadUser(UUID.fromString(req.params(":uuid")));
                Set<String> groups = userFuture.join().getNodes().stream()
                        .filter(NodeType.INHERITANCE::matches)
                        .map(NodeType.INHERITANCE::cast)
                        .map(InheritanceNode::getGroupName)
                        .collect(Collectors.toSet());
                for (Iterator<String> group = groups.iterator(); group.hasNext(); ) {
                    String g = group.next();
                    if (g != "default") {
                        result = g;
                    }
                }
                return result;
            } else {
                return "invalid_uuid";
            }
        });

        // Provide the specified player's permission groups on LuckPerms
        get("/groups/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                // Convert UUID string to player object
                String result = "null";
                CompletableFuture<User> userFuture = getLuckPermsAPI().getUserManager().loadUser(UUID.fromString(req.params(":uuid")));
                Set<String> groups = userFuture.join().getNodes().stream()
                        .filter(NodeType.INHERITANCE::matches)
                        .map(NodeType.INHERITANCE::cast)
                        .map(InheritanceNode::getGroupName)
                        .collect(Collectors.toSet());
                result = groups.toString();
                return result;
            } else {
                return "invalid_uuid";
            }
        });

        // Provide when the specified player logged in to the server last time.
        get("/last/:uuid", (req, res) -> {
            // Check if query is truly UUID in order to avoid SQL injection
            if (req.params(":uuid").matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                String sql = "SELECT * FROM `timestamp` WHERE uuid = '" + req.params(":uuid") +"';";
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
                String sql = "SELECT * FROM `timestamp` WHERE uuid = '" + req.params(":uuid") +"';";
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
