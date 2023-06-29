package me.arch.hoi.listeners;

import me.arch.hoi.Hoi;
import me.arch.hoi.Msg;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkLoad {

    private final Hoi plugin;
    private final Map<UUID, Chunk> lastChunk;

    public ChunkLoad(Hoi plugin) {
        this.plugin = plugin;
        this.lastChunk = new HashMap<>();
        startChunkCheckTask();
    }

    private void startChunkCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    Chunk currentChunk = player.getLocation().getChunk();
                    UUID playerId = player.getUniqueId();
                    Chunk previousChunk = lastChunk.get(playerId);

                    if (previousChunk != null && !previousChunk.equals(currentChunk)) {
                        if (isChunkClaimed(currentChunk)) {
                            // Chunk is claimed
                           // player.sendTitle("§c§lBu parça zaten birisi tarafından alınmış" , "§7§lLütfen başka bir parça deneyin" , 10 , 70 , 20);
                        } else {
                            // Chunk is wilderness
                           // player.sendTitle("§c§lBu parça uygun" , "Evet aynen" ,  10 , 70 , 20);
                        }
                    }

                    lastChunk.put(playerId, currentChunk);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second (20 ticks)
    }

    private boolean isChunkClaimed(Chunk chunk) {
        // Implement your logic to check if the chunk is claimed
        // You can use your plugin's claim system or any other mechanism you have in place
        // Return true if the chunk is claimed, false if it's wilderness
        return false;
    }

}