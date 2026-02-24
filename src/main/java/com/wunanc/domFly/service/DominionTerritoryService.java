package com.wunanc.DomFly.service;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import com.wunanc.DomFly.model.WorldCoordinate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DominionTerritoryService implements TerritoryService {

    private final DominionAPI api;
    private final ConfigManager config;
    private final Map<WorldCoordinate, Long> cache = new ConcurrentHashMap<>();
    private final Logger logger;
    public DominionTerritoryService(JavaPlugin plugin, ConfigManager config) {
        this.api = DominionAPI.getInstance();
        this.config = config;
        this.logger = plugin.getLogger();
        if (this.api == null) {
            this.logger.warning("Failed to hook into Dominion API! Features relying on territory checks will not work.");
        }
        if (config.isCacheEnabled()) {
            scheduleCacheCleanup(plugin);
        }
    }
    @Override
    public boolean isInOwnTerritory(Player player) {
        long now = System.currentTimeMillis();
        Location loc = player.getLocation();
        WorldCoordinate key = new WorldCoordinate(loc);
        if (config.isCacheEnabled()) {
            Long cachedTime = cache.get(key);
            if (cachedTime != null && (now - cachedTime) < config.getCacheExpiryMillis()) {
                return true;
            }
        }
        boolean result = check(player, loc);
        if (config.isCacheEnabled() && result) {
            cache.put(key, now);
        }
        return result;
    }
    private boolean check(Player player, Location loc) {
        if (api == null) {
            return false;
        }
        try {
            DominionDTO dominion = api.getDominion(loc);
            if (dominion == null) return false;
            if (player.getUniqueId().equals(dominion.getOwner())) return true;
            if (config.isCheckOwnerOnly()) return false;
            return api.checkPrivilegeFlag(loc, Flags.ADMIN, player);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format(
                    "Error while checking territory privilege for player %s at location %s",
                    player.getName(), loc
            ), e);
            return false;
        }
    }
    private void scheduleCacheCleanup(JavaPlugin plugin) {
        long cleanupInterval = 60 * 20L;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long now = System.currentTimeMillis();
            long expiry = config.getCacheExpiryMillis();
            cache.entrySet().removeIf(entry -> {
                Long timestamp = entry.getValue();
                return timestamp == null || (now - timestamp) >= expiry;
            });
        }, cleanupInterval, cleanupInterval);
    }
}