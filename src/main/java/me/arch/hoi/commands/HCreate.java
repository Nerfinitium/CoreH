package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import me.arch.hoi.Hoi;
import me.arch.hoi.Msg;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static me.arch.hoi.Hoi.*;

public class HCreate {

    public HCreate() {
        new CommandBase("HCreate", true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                try {
                    Connection connection = DriverManager.getConnection(url, user, password);
                    if (debugmode) {
                        System.out.println("Connected to the database.");
                    }
                    Statement statement = connection.createStatement();

                    // Check if the player already has a country
                    String sql = "SELECT * FROM " + player.getName() + " WHERE havecountry = 1";
                    statement.execute(sql);
                    if (statement.getResultSet().next()) {
                        Msg.send(player, "&cYou already have a country.");
                        return true;
                    } else {
                        Msg.send(player, "&aYour country has been created.");

                        // Insert player's country data into the database
                        sql = "INSERT INTO " + player.getName() + " (havecountry) VALUES (1)";
                        statement.execute(sql);

                        // Create a table for the player's country
                        sql = "CREATE TABLE " + arguments[0] + " (Mareşal varchar(255), Orgeneral varchar(255), " +
                                "KorGeneral varchar(255), Subay varchar(255), ER varchar(255), Sivil varchar(255))";
                        statement.execute(sql);

                        // Get the chunk coordinates the player is standing on and send them
                        Chunk chunk = player.getLocation().getChunk();
                        int chunkX = chunk.getX();
                        int chunkZ = chunk.getZ();
                        String ChunkID = chunkX + "," + chunkZ * 1000;

                        Msg.send(player, "&6You are standing on Chunk Coordinates: X=" + chunkX + ", Z=" + chunkZ + ChunkID);
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
                return "/HCreate";
            }
        }.enableDelay(2);
    }
}