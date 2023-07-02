package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import me.arch.hoi.Msg;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


import static me.arch.hoi.Hoi.*;
import static me.arch.hoi.Hoi.debugmode;

public class HClaim {


    public HClaim() {
        new CommandBase("HClaim", true) {
            @Override
            public boolean onCommand(CommandSender sender, String [] arguments) {
                Player player = (Player) sender;
                try {
                    Connection connection = DriverManager.getConnection(url, user, password);
                    if (debugmode) {
                        System.out.println("Connected to the database.");
                    }
                    Statement statement = connection.createStatement();



                    Chunk chunk = player.getLocation().getChunk();
                    int chunkX = chunk.getX();
                    int chunkZ = chunk.getZ();
                    String ChunkIDrws = chunkX + "," + chunkZ * 121;
                    String ChunkIDrw = ChunkIDrws.replace(",", "");
                    String ChunkID = ChunkIDrw.replace("-", "");
                    String sql =
                    statement.execute(sql);
                    if (statement.getResultSet().next()) {
                       player.sendActionBar("§4 This chunk is already claimed.");
                       Msg.send(player, "&c This chunk is already claimed.");
                        return true;
                    } else {

                        Msg.send(player, "&a You Claimed This Chunk.");
                        String sql = "CREATE TABLE " + "c" + ChunkID + " (ncountry varchar(255), isclaimed BIT DEFAULT 0)";
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
                return "/HClaim";
            }
        }.enableDelay(2);
    }
}
