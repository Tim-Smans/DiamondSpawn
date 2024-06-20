package me.timdev.diamondchallenge.utils;

import me.timdev.diamondchallenge.DiamondChallenge;
import me.timdev.diamondchallenge.events.ChunkEvents;
import me.timdev.diamondchallenge.models.ChunkData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Utils {


    //Method to get a random location inside of the given chunk at the highest block level.
    public static Location getRandomSurfaceLocation(Chunk chunk) {
        Random random = new Random();
        World world = chunk.getWorld();

        //Creating random coords inside the selected chunk
        int blockX = (chunk.getX() << 4) + random.nextInt(16);
        int blockZ = (chunk.getZ() << 4) + random.nextInt(16);

        //Create the Y coord at the highest possible block level
        int blockY = world.getHighestBlockYAt(blockX, blockZ);

        //Get the block and check its type to see if it is water or lava, if it is not return the new location. Otherwise return null
        Block block = world.getBlockAt(blockX, blockY - 1, blockZ);
        if (block.getType() != Material.WATER && block.getType() != Material.LAVA) {
            return new Location(world, blockX, blockY, blockZ);
        }
        return null;
    }

    //Method to get the avarage location of where a player has spawned in diamonds by loading in chunks.
    public static Location getAverageLocation(Player player) {
        //Check if there are any loaded chunks saved..
        if (ChunkEvents.chunkDataMap.isEmpty()) {
            return null;
        }

        //Loop through the entire list of chunkData and if the player that quit is the same player that is saved in the chunkData, create a new location with the values from the chunk data.
        ArrayList<Location> spawnedItemLocations  = new ArrayList<>();
        for (ChunkData chunkData : ChunkEvents.chunkDataMap){
            if(chunkData.getPlayer() != player){
                continue;
            }
            Location loc = new Location(Bukkit.getWorld(chunkData.getWorldName()), chunkData.getX(), chunkData.getY(), chunkData.getZ());
            spawnedItemLocations.add(loc);
        }

        //When the player that quit did not load in any chunks spawnedItemLocations will be empty so i will return null.
        if(spawnedItemLocations.isEmpty()) {
            return null;
        }


        //Calculate the avarage location
        double totalX = 0, totalY = 0, totalZ = 0;
        for (Location loc : spawnedItemLocations) {
            totalX += loc.getX();
            totalY += loc.getY();
            totalZ += loc.getZ();
        }

        int count = spawnedItemLocations.size();
        return new Location(null, totalX / count, totalY / count, totalZ / count);
    }

    //Method to flush the chunk data of a player.
    public static void flushChunkData(Player player){
        if (ChunkEvents.chunkDataMap.isEmpty()) {
            return;
        }

        // Safely remove the element
        ChunkEvents.chunkDataMap.removeIf(chunkData -> chunkData.getPlayer() == player);
    }

    //Method to drop a diamond and remove it again after 60 seconds have gone by.
    public static void dropDiamond(Chunk chunk, Location location){
        // Create the diamond item
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemMeta diamondMeta = diamond.getItemMeta();
        diamondMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#a44fff") + "You " + net.md_5.bungee.api.ChatColor.of("#4fffdf") + "should " + net.md_5.bungee.api.ChatColor.of("#ff4f9b") + "definitely " + net.md_5.bungee.api.ChatColor.of("#ffd94f") + "accept " + net.md_5.bungee.api.ChatColor.of("#589e3c") + "me =)");
        // Lore
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.RED + "This is a");
        lore.add(net.md_5.bungee.api.ChatColor.BLUE + "Very nice looking");
        lore.add(net.md_5.bungee.api.ChatColor.DARK_PURPLE + "Lore if I do say so myself");
        lore.add(ChatColor.WHITE + "A bit colorful though.");
        diamondMeta.setLore(lore);
        // Enchants
        diamondMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        diamond.setItemMeta(diamondMeta);

        // Spawn the diamond in the world
        Item diamondItem = chunk.getWorld().dropItemNaturally(location, diamond);
        Bukkit.getLogger().info("World: " + chunk.getWorld().getName());
        Bukkit.getLogger().info("Coords: " + location.getX() + ", " + location.getY() + ", " + location.getZ());
        Bukkit.getLogger().info("Type: " + diamond.getType());

        // Schedule a task to remove the diamond after 60 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                diamondItem.remove();
            }
        }.runTaskLater(DiamondChallenge.plugin, 60 * 20);
    }
}
