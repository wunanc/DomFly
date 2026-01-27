package com.wunanc.domfly.service;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import com.wunanc.domfly.model.WorldCoordinate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DominionTerritoryService implements TerritoryService {

    private final DominionAPI api;
    private final ConfigManager config;
    private final Map<WorldCoordinate, Long> cache = new ConcurrentHashMap<>();

    public DominionTerritoryService(JavaPlugin plugin, ConfigManager config) {
        this.api = DominionAPI.getInstance();
        this.config = config;
        if (config.isCacheEnabled()) {
            scheduleCacheCleanup(plugin);
        }
    }

    @Override
    public boolean isInOwnTerritory(Player player) {
        Location loc = player.getLocation();
        WorldCoordinate key = new WorldCoordinate(loc);

        if (config.isCacheEnabled()) {
            Long cachedTime = cache.get(key);
            long now = System.currentTimeMillis();

            if (cachedTime != null && (now - cachedTime) < config.getCacheExpiryMillis()) {
                return true;
            }
        }

        boolean result = check(player, loc);

        if (config.isCacheEnabled() && result) {
            cache.put(key, System.currentTimeMillis());
        }

        return result;
    }

    private boolean check(Player player, Location loc) {
        try {
            DominionDTO dominion = api.getDominion(loc);
            if (dominion == null) return false;

            if (player.getUniqueId().equals(dominion.getOwner())) return true;
            if (config.isCheckOwnerOnly()) return false;

            return api.checkPrivilegeFlag(loc, Flags.ADMIN, player);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void scheduleCacheCleanup(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long now = System.currentTimeMillis();
            long expiry = config.getCacheExpiryMillis();

            cache.entrySet().removeIf(entry -> {
                Long timestamp = entry.getValue();
                return timestamp == null || (now - timestamp) >= expiry;
            });
        }, 60 * 20L, 60 * 20L);
    }
}
