package me.timdev.diamondchallenge;

import me.timdev.diamondchallenge.events.ChunkEvents;
import me.timdev.diamondchallenge.events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiamondChallenge extends JavaPlugin {


    public static Plugin plugin;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        plugin = this;

        registerEvents();
        registerConfig();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new ChunkEvents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }
    public void registerConfig() {
        //Creating config defaults
        config.addDefault("dropChance", 30);
        config.options().copyDefaults(true);
        saveConfig();
    }
}
