package me.arch.hoi;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
    private List<String> delayedPlayers = null;
    private int delay = 0;
    private final int minArguments;
    private final int maxArguments;
    private final boolean playerOnly;

    public CommandBase(String command) {
        this(command, 0);
    }

    public CommandBase(String command, boolean playerOnly) {
        this(command, 0, playerOnly);
    }

    public CommandBase(String command, int requiredArguments) {
        this(command, requiredArguments, requiredArguments);
    }

    public CommandBase(String command, int minArguments, int maxArguments) {
        this(command, minArguments, maxArguments, false);
    }

    public CommandBase(String command, int requiredArguments, boolean playeronly) {
        this(command, requiredArguments, requiredArguments, playeronly);
    }

    public CommandBase(String command, int minArguments, int maxArguents, boolean playerOnly) {
        super(command);

        this.minArguments = minArguments;
        this.maxArguments = maxArguents;
        this.playerOnly = playerOnly;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }
 public CommandMap getCommandMap(){

        try {
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            return (CommandMap) field.get(Bukkit.getPluginManager());
        }
        }catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CommandBase enableDelay(int delay) {
        this.delay = delay;
        this.delayedPlayers = new ArrayList<>();
        return this;
    }

  public void sendUsage(CommandSender sender) {
      Player player = (Player) sender;
      Msg.send(sender, getUsage());
  }

  public boolean execute(CommandSender sender, String alias, String [] arguments) {
        if(arguments.length < minArguments || (arguments.length < maxArguments && maxArguments != -1)){
            sendUsage(sender);
            return true;
        }
        if(playerOnly && !(sender instanceof Player)) {
            Msg.send(sender, "&c Only Players Can use this");
          return true;
        }

        String permission = getPermission();
                if (permission != null && !sender.hasPermission(permission)){
                    Msg.send(sender, "&cYou do not have permission to use this command.");
                    return true;

                }
                if(delayedPlayers != null && sender instanceof Player) {
                    Player player = (Player) sender;
                    if (delayedPlayers.contains(player.getName())) {
                        Msg.send(player, "&cPlease wait before using this command again.");
                        return true;
                    }

                    delayedPlayers.add(player.getName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Hoi.getInstance(), () -> {
                    delayedPlayers.remove(player.getName());
                },20L * delay);

                }

                if (!onCommand(sender, arguments)) {
                    sendUsage(sender);
                }
        return true;
  }

  public boolean onCommand(CommandSender sender, Command command, String alias, String [] arguments) {
   return this.onCommand(sender, arguments);
    }

  public abstract boolean onCommand(CommandSender sender, String [] arguments);

    public abstract String getUsage();

}

