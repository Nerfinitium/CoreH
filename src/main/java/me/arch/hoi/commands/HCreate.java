package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import me.arch.hoi.Msg;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

import static me.arch.hoi.Hoi.*;

public class HCreate {

    public HCreate() {

        new CommandBase("HCreate", true) {

            String[] cnames = {"cname1", "cname2", "cname3"};
            String[] chunkIDs = {"chunkid1", "chunkid2", "chunkid3"};
            boolean[] iscs = {true, false, true};

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                try {
                    try (Connection conn = DriverManager.getConnection(url, user, password)) {
                        // Parameterized query for table creation
                        String createTableSql = "CREATE TABLE IF NOT EXISTS " + arguments[0] + " (Mareşal varchar(255), Orgeneral varchar(255), KorGeneral varchar(255), Subay varchar(255), ER varchar(255), Sivil varchar(255))";
                        try (PreparedStatement createTableStmt = conn.prepareStatement(createTableSql)) {
                            createTableStmt.executeUpdate();
                        }

                        // Parameterized query for data insertion
                        String insertDataSql = "INSERT INTO " + arguments[0] + " (cname, chunkid, isc) VALUES (?, ?, ?)";
                        try (PreparedStatement insertDataStmt = conn.prepareStatement(insertDataSql)) {
                            // Loop through the arrays and insert each set of values
                            for (int i = 0; i < cnames.length; i++) {
                                insertDataStmt.setString(1, cnames[i]);
                                insertDataStmt.setString(2, chunkIDs[i]);
                                insertDataStmt.setBoolean(3, iscs[i]);
                                insertDataStmt.executeUpdate();
                            }
                        }

                        System.out.println("Values inserted successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Check if the player already has a country
                    try (Connection conn = DriverManager.getConnection(url, user, password);
                         Statement statement = conn.createStatement()) {
                        String sql = "SELECT * FROM " + player.getName() + " WHERE havecountry = 1 LIMIT 1";
                        try (ResultSet resultSet = statement.executeQuery(sql)) {
                            if (resultSet.next()) {
                                Msg.send(player, "&c You already have a country.");
                                player.sendActionBar("§4 You already have a country.");
                                return true;
                            } else {
                                Msg.send(player, "&aYour country has been created.");
                                Chunk chunk = player.getLocation().getChunk();
                                int chunkX = chunk.getX();
                                int chunkZ = chunk.getZ();
                                String ChunkIDrws = chunkX + "," + chunkZ * 121;
                                String ChunkIDrw = ChunkIDrws.replace(",", "");
                                String ChunkID = ChunkIDrw.replace("-", "");

                                Msg.send(player, "&aChunk ID: " + ChunkID);
                                player.sendMessage(ChunkID);

                                // Create a table for the chunk data
                                String chunkTableSql = "CREATE TABLE " + "c" + ChunkID + " (country varchar(255), isclaimed BIT DEFAULT b'0')";
                                statement.execute(chunkTableSql);
                            }
                        }
                    } catch (SQLException e) {
                        if (debugmode) {
                            System.out.println("Unable to connect to the database.");
                        }
                    }

                } catch (Exception e) {
                    if (debugmode) {
                        System.out.println("Unable to connect to the database.");
                    }
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "/HCreate";
            }
        }.enableDelay(2);
    }
}
