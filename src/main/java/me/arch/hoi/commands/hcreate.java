package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
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

import static me.arch.hoi.Hoi.debugmode;
import static me.arch.hoi.Hoi.password;
import static me.arch.hoi.Hoi.url;
import static me.arch.hoi.Hoi.user;

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
                        player.sendMessage("you already have a country");
                        return true;
                    }else {
                        //if the havecountry is false then it will create a country
                        sql = "INSERT INTO " + player.getName() + " (havecountry) VALUES (1)";
                        sql = "IF NOT EXISTS" + arguments + "CREATE TABLE " + arguments + "(id int, name varchar(255), population int, manpower int, factories int, dockyards int, oil int, steel int, aluminium int, tungsten int, chromium int, infantry int, cavalry int, artillery int, anti_tank int, anti_air int, rocket_artillery int, light_tank int, medium_tank int, heavy_tank int, super_heavy_tank int, tank_destroyer int, self_propelled_artillery int, motorized int, mechanized int, sp_artillery int, sp_anti_air int, sp_anti_tank int, sp_rocket_artillery int, sp_light_tank int, sp_medium_tank int, sp_heavy_tank int, sp_super_heavy_tank int, sp_tank_destroyer int, sp_motorized int";
                        statement.execute(sql);
                        player.sendMessage("you have created a country");
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
