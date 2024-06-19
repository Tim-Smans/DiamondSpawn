package me.timdev.diamondchallenge;

import me.timdev.diamondchallenge.events.ChunkEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiamondChallenge extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Plugin successfully started! Stinky.");

        registerEvents();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new ChunkEvents(), this);
    }
}
