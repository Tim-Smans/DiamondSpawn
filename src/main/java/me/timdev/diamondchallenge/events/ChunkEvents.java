package me.timdev.diamondchallenge.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashSet;
import java.util.Set;

public class ChunkEvents implements Listener {


    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            return;
        }
        Bukkit.getLogger().info("Nieuwe chunk gevonden.");
    }
}
