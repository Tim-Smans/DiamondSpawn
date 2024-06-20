package me.timdev.diamondchallenge.events;

import me.timdev.diamondchallenge.DiamondChallenge;
import me.timdev.diamondchallenge.models.ChunkData;
import me.timdev.diamondchallenge.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ChunkEvents implements Listener {


    private final Set<UUID> processedChunks = new HashSet<>();
    public static final ArrayList<ChunkData> chunkDataMap = new ArrayList<>();
    private final int MAX_CHUNKS_PER_TICK = 10;
    private final Random random = new Random();

    FileConfiguration config = DiamondChallenge.plugin.getConfig();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            return;
        }

        // Only progress if the random number is lower than the given amount (30%)
        if (random.nextInt(100) >= config.getInt("dropChance")) {
            return;
        }

        Chunk chunk = event.getChunk();
        UUID chunkUUID = UUID.nameUUIDFromBytes((chunk.getWorld().getName() + chunk.getX() + "," + chunk.getZ()).getBytes());

        // Check if the chunk has already been processed
        if (processedChunks.contains(chunkUUID)) {
            return;
        }

        // Find the nearest player to the chunk
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Player player : chunk.getWorld().getPlayers()) {
            double distance = player.getLocation().distance(chunk.getBlock(8, 64, 8).getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }

        // No player found near the chunk
        if (nearestPlayer == null) {
            return;
        }

        // Process the chunk asynchronously
        Player finalNearestPlayer = nearestPlayer;
        CompletableFuture.runAsync(() -> {
            Location location = Utils.getRandomSurfaceLocation(chunk);

            if(location == null){
                return;
            }


            // Hold onto the chunk coordinates and world name
            String worldName = chunk.getWorld().getName();
            int chunkX = location.getBlockX();
            int chunkY = location.getBlockY();
            int chunkZ = location.getBlockZ();

            ChunkData chunkData = new ChunkData(worldName, chunkX, chunkY, chunkZ, finalNearestPlayer);
            chunkDataMap.add(chunkData);

            // Schedule the task on the main thread to spawn the diamond
            Bukkit.getScheduler().runTask(DiamondChallenge.plugin, () -> {
                //Drop the diamond and remove it after 60 seconds..
                Utils.dropDiamond(chunk, location);

                // Mark the chunk as processed
                processedChunks.add(chunkUUID);

                // Limit the size of the processed chunks set to prevent memory leaks
                if (processedChunks.size() > MAX_CHUNKS_PER_TICK) {
                    processedChunks.remove(processedChunks.iterator().next());
                }
            });
        });
    }
}
