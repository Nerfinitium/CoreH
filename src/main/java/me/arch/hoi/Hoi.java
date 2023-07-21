package me.arch.hoi;


import me.arch.hoi.commands.HClaim;
import me.arch.hoi.commands.feed;
import me.arch.hoi.commands.HCreate;
import me.arch.hoi.listeners.ChunkLoad;
import me.arch.hoi.listeners.PlayerJoin;
import me.arch.hoi.util.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public final class Hoi extends JavaPlugin {
    private static Hoi instance;

    public static String url = "jdbc:mysql://localhost/hoi";
    public static String user = "root";
    public static String password = "Tureet45";
    public static boolean debugmode = true; //added a debugmode for only in development proccess going to be transfered to config.yml later

    String configFilePath = "configs/config.properties";

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        List<String> kitItems = (List<String>) getConfig().getList("kit");
        for (String itemName : kitItems) {
          getLogger().info(itemName);
        }

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("connected to database ");

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS player_stats(xp int, kills int, blocks_broken long, balance double, last_login DATE, last_logout DATE)";
            statement.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS chunk_data (chunkid varchar(255), cname varchar(255), isc BIT DEFAULT 0)";
            statement.execute(sql);
        }catch (SQLException e) {
            System.out.println("unable to connect to the database hoi");
        }



        ConfigUtil config = new ConfigUtil(this, "test.yml");
        config.getConfig().set("hello", "world");


        config.save();

        // Plugin startup logic
        getLogger().info("plugin started");
        new feed();
        new HCreate();
        new PlayerJoin(this);
        new ChunkLoad(this);
        new HClaim();


    }
    @Override
    public void onDisable() { System.out.println("shutting down...");}
    public static Hoi getInstance() {
        return instance;
    }
}

