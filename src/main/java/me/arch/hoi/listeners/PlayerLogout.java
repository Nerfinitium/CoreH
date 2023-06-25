package me.arch.hoi.listeners;

import me.arch.hoi.Hoi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static me.arch.hoi.Hoi.*;

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
