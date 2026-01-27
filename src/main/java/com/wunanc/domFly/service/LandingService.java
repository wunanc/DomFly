package com.wunanc.domfly.service;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class LandingService {
    private static final int MAX_SEARCH_DEPTH = 60;
    private static final double BLOCK_CENTER_OFFSET = 0.5;
    private static final int SAFE_FALL_DISTANCE_THRESHOLD = 3;
    private static final int HEAD_SPACE_ABOVE_BLOCK = 1;

    public void land(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int startY = loc.getBlockY();

        int safeY = findSafeY(world, x, startY, z);

        if (safeY != -1 && (startY - safeY) > SAFE_FALL_DISTANCE_THRESHOLD) {
            Location target = new Location(world, x + BLOCK_CENTER_OFFSET, safeY + HEAD_SPACE_ABOVE_BLOCK, z + BLOCK_CENTER_OFFSET);

            if (isSafe(world, x, safeY + HEAD_SPACE_ABOVE_BLOCK, z)) {
                player.teleport(target);
            }
        }
    }

    private int findSafeY(World world, int x, int startY, int z) {
        int minY = Math.max(world.getMinHeight(), startY - MAX_SEARCH_DEPTH);

        for (int y = startY; y > minY; y--) {
            if (isSolid(world, x, y, z) && isAir(world, x, y + 1, z) && isAir(world, x, y + 2, z)) {
                return y;
            }
        }
        return -1;
    }

    private boolean isSafe(World world, int x, int y, int z) {
        return isAir(world, x, y, z) && isAir(world, x, y + 1, z);
    }

    private boolean isSolid(World world, int x, int y, int z) {
        return world.getBlockAt(x, y, z).getType().isSolid();
    }

    private boolean isAir(World world, int x, int y, int z) {
        return world.getBlockAt(x, y, z).getType() == Material.AIR;
    }
}
