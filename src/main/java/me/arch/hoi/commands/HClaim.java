package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import me.arch.hoi.Msg;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

import static me.arch.hoi.Hoi.*;
import static me.arch.hoi.Hoi.debugmode;

public class HClaim {

    public HClaim() {
        new CommandBase("HClaim", true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    if (debugmode) {
                        System.out.println("Connected to the database.");
                    }
                    Chunk chunk = player.getLocation().getChunk();
                    int chunkX = chunk.getX();
                    int chunkZ = chunk.getZ();
                    String chunkID = getChunkID(chunkX, chunkZ);

                    if (isChunkClaimed(connection, chunkID)) {
                        player.sendActionBar("§4 This chunk is already claimed.");
                        Msg.send(player, "&c This chunk is already claimed.");
                    } else {
                        String countryName = getPlayerCountryName(player);
                        createChunkTable(connection, chunkID, countryName);
                        Msg.send(player, "&a You claimed this chunk.");
                    }
                } catch (SQLException e) {
                    if (debugmode) {
                        System.out.println("Unable to connect to the database.");
                    }
                }
                return true;
            }

            @Override
            public String getUsage() {
                return "/HClaim";
            }
        }.enableDelay(2);
    }

    private String getChunkID(int chunkX, int chunkZ) {
        String chunkID = chunkX + "," + chunkZ * 121;
        return chunkID.replace(",", "").replace("-", "");
    }

    private boolean isChunkClaimed(Connection connection, String chunkID) throws SQLException {
        String sql = "SHOW  TABLES LIKE 'c" + chunkID + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next();
        }
    }

    private void createChunkTable(Connection connection, String chunkID, String countryName) throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS c" + chunkID + " (country varchar(255), isclaimed BIT DEFAULT 0)";
        String insertDataSql = "INSERT INTO c" + chunkID + " (country) VALUES (?)";

        try (Statement createTableStatement = connection.createStatement();
             PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSql)) {
            createTableStatement.execute(createTableSql);
            insertDataStatement.setString(1, countryName);
            insertDataStatement.execute();
        }
    }

    private String getPlayerCountryName(Player player) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT country FROM " + player.getName()+ " WHERE country IS NOT NULL LIMIT 1";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    return resultSet.getString("countryName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if the country value is not found or an error occurs
    }
}