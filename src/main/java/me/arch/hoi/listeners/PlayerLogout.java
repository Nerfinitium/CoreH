package me.arch.hoi.listeners;

import me.arch.hoi.Hoi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static me.arch.hoi.Hoi.debugmode;
import static me.arch.hoi.Hoi.password;
import static me.arch.hoi.Hoi.url;
import static me.arch.hoi.Hoi.user;


public class PlayerLogout implements Listener {



    public PlayerLogout(Hoi plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if(debugmode == true) {
                System.out.println("Player logout database connected");
            }

            Statement statement = connection.createStatement();
            String sql = "UPDATE " + player.getName() + " SET last_logout = CURDATE()";
            statement.execute(sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
