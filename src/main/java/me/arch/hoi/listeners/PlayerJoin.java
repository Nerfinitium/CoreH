package me.arch.hoi.listeners;

import me.arch.hoi.Hoi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static me.arch.hoi.Hoi.*;

public class PlayerJoin implements Listener {

    public static String url = "jdbc:mysql://localhost/hoi";
    public static String user = "root";
    public static String password = "Tureet45";


    public PlayerJoin(Hoi plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage(ChatColor.AQUA + "[+]" + ChatColor.LIGHT_PURPLE + "Hoşgeldin " + ChatColor.RED + player.getDisplayName());


        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("connected to database ");

            String ip = player.getAddress().getAddress().getHostAddress();

            Statement statement = connection.createStatement();

           //create sql table with the name of the player if not exists
            String sql = "CREATE TABLE IF NOT EXISTS " + player.getName() + "(ip varchar(255), last_login DATE, last_logout DATE, command_power int, havecountry BIT DEFAULT 0, country varchar(255), kills int, wounds int)";
            statement.execute(sql);

            //insert the ip of the player into the table
            sql = "UPDATE " + player.getName() + "(ip) VALUES ('" + ip + "')";
            statement.execute(sql);
            sql = "UPDATE " + player.getName() + " SET last_login = CURDATE()";
            statement.execute(sql);

            //check if the ip of the player is the same as the one in the database
            sql = "SELECT * FROM " + player.getName() + " WHERE ip = '" + ip + "'";
            statement.execute(sql);



            //if the ip is not the same as the one in the database
            if (!statement.getResultSet().next()) {
               // player.kickPlayer("You have a different ip");
            }


        }catch (SQLException exception) {
                Bukkit.getLogger().warning("unable to connect to the database hoi");
        }

    }
}
