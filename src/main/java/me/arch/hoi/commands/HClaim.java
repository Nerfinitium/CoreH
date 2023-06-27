package me.arch.hoi.commands;

import me.arch.hoi.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HClaim {


    public HClaim() {
        new CommandBase("HClaim", true) {
            @Override
            public boolean onCommand(CommandSender sender, String [] arguments) {
                Player player = (Player) sender;
                player.setFoodLevel(20);
                return true;
            }

            @Override
            public String getUsage() {
                return "/HClaim";
            }
        }.enableDelay(2);
    }
}
