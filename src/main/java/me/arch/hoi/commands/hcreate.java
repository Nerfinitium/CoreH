package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import me.arch.hoi.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static me.arch.hoi.Hoi.*;


public class hcreate {
    public hcreate() {
        new CommandBase("hcreate", true) {
            @Override
            public boolean onCommand(CommandSender sender, String [] arguments) {
                Player player = (Player) sender;
                try {
                    Connection connection = DriverManager.getConnection(url, user, password);
                    if(debugmode == true) {System.out.println("connected to database ");}
                    Statement statement = connection.createStatement();

                     //if the havecountry is true then it will send a message that you already have a country
                    String sql = "SELECT * FROM " + player.getName() + " WHERE havecountry = 1";
                    statement.execute(sql);
                    if(statement.getResultSet().next()) {
                        Msg.send(player, " &c &ks &r &cYou already have a country &k s");
                        return true;
                    }else {
                        Msg.send(player, "&aYour Country is Created");
                        sql = "INSERT INTO " + player.getName() + " (havecountry) VALUES (1)";
                        statement.execute(sql);
                        sql = "CREATE TABLE " + arguments[0] + "Mareşal varchar(255), Orgeneral varchar(255), KorGeneral varchar(255), Subay varchar(255), ER varchar(255), Sivil (255)";
                        statement.execute(sql);
                    }


                }catch (SQLException e) {if(debugmode == true) {System.out.println("unable to connect to the database hoi");}}

                return true;
            }

            @Override
            public String getUsage() {
                return "/hcreate";
            }
        }.enableDelay(2);
    }
}
