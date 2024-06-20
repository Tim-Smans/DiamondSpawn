package me.timdev.diamondchallenge.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class Utils {


    /*
    * This method gets a chunk as a parameter.
    *
    * Then it will use the Random object to create a random X and Z coördinate.
    * I use the getHighestBlockYAt method to get myself the Y coördinate.
    *
    * After that i check if the block that i found is suitable to spawn a diamond. (If it's not water or lava)
    *
    * */

    public static Location getRandomSurfaceLocation(Chunk chunk) {
        Random random = new Random();
        World world = chunk.getWorld();

        int blockX = (chunk.getX() << 4) + random.nextInt(16);
        int blockZ = (chunk.getZ() << 4) + random.nextInt(16);

        int blockY = world.getHighestBlockYAt(blockX, blockZ);

        Block block = world.getBlockAt(blockX, blockY - 1, blockZ);
        if (block.getType() != Material.WATER && block.getType() != Material.LAVA) {
            return new Location(world, blockX, blockY, blockZ);
        }
        return null;
    }
}
