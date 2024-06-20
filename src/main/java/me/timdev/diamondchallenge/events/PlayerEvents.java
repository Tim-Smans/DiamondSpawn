package me.timdev.diamondchallenge.events;

import me.timdev.diamondchallenge.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Get the avarage location
        Location avarageLoc = Utils.getAverageLocation(event.getPlayer());

        //Check if it is null
        if(avarageLoc == null) {
            return;
        }

        //Log the location
        Bukkit.getLogger().info("Avarage location of items for " + event.getPlayer().getName() + ": " + avarageLoc.getX() + ", " + avarageLoc.getY() + ", " + avarageLoc.getZ());

        //Flush the cashe
        Utils.flushChunkData(event.getPlayer());
    }
}
