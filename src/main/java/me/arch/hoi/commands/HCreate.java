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
                    String sql = "SELECT * FROM " + player.getName() + " WHERE havecountry = 1 LIMIT 1";
                    statement.execute(sql);
                    if (statement.getResultSet().next()) {
                        Msg.send(player, "&c You already have a country.");
                        player.sendActionBar("§4 You already have a country.");
                        return true;
                    } else {

                        Msg.send(player, "&aYour country has been created.");
                        sql = "CREATE TABLE " + arguments[0] + " (Mareşal varchar(255), Orgeneral varchar(255), KorGeneral varchar(255), Subay varchar(255), ER varchar(255), Sivil varchar(255))";
                        statement.execute(sql);
                        Chunk chunk = player.getLocation().getChunk();
                        int chunkX = chunk.getX();
                        int chunkZ = chunk.getZ();
                        String ChunkIDrws = chunkX + "," + chunkZ * 121;
                        String ChunkIDrw = ChunkIDrws.replace(",", "");
                        String ChunkID = ChunkIDrw.replace("-", "");

                        Msg.send(player, "&aChunk ID: " + ChunkID);
                        player.sendMessage(ChunkID);
                        sql = "CREATE TABLE " + "c" + ChunkID+ " (country varchar(255), isclaimed BIT DEFAULT 0)";
                        statement.execute(sql);
                        sql = "INSERT INTO " + "c" + ChunkID+ " SET isclaimed = 1";
                        statement.execute(sql);
                        sql = "INSERT INTO " + player.getName() + " SET havecountry = 1";
                        statement.execute(sql);
                        sql = "INSERT INTO " + player.getName() + " (country) VALUES ('" + arguments[0] + "')";
                        statement.execute(sql);
                        sql = "INSERT INTO " + "c" + ChunkID + " (country) VALUES ('" + arguments[0] + "')";
                        statement.execute(sql);




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