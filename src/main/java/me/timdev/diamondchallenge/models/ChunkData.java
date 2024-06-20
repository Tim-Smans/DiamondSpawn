package me.timdev.diamondchallenge.models;

import org.bukkit.entity.Player;

public class ChunkData {
    private final String worldName;
    private final int x;
    private final int z;
    private final int y;
    private final Player player;

    public ChunkData(String worldName, int x, int y, int z, Player player) {
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        this.y = y;
        this.player = player;
    }

    public String getWorldName() {
        return worldName;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}