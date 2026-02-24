package com.wunanc.DomFly.listener;

import com.wunanc.DomFly.service.ConfigManager;
import com.wunanc.DomFly.service.FlightService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlightListener implements Listener {
    private final FlightService service;
    private final ConfigManager config;
    private final Map<UUID, Long> lastCheckTimestamps = new ConcurrentHashMap<>();
    public FlightListener(FlightService service, ConfigManager config) {
        this.service = service;
        this.config = config;
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!hasLocationChanged(e) || !shouldCheck(e.getPlayer())) return;
        if (service.isFlying(e.getPlayer())) {
            service.checkTerritory(e.getPlayer());
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        lastCheckTimestamps.remove(e.getPlayer().getUniqueId());
    }
    private boolean hasLocationChanged(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getWorld() != to.getWorld()) return true;
        int threshold = config.getMoveDistanceThreshold();
        if (threshold <= 0) return !from.equals(to);
        return from.getBlockX() != to.getBlockX() ||
                from.getBlockY() != to.getBlockY() ||
                from.getBlockZ() != to.getBlockZ();
    }
    private boolean shouldCheck(Player player) {
        long now = System.currentTimeMillis();
        long interval = config.getCheckInterval() * 50L;
        UUID id = player.getUniqueId();
        Long lastCheck = lastCheckTimestamps.get(id);
        if (lastCheck == null || (now - lastCheck) >= interval) {
            lastCheckTimestamps.put(id, now);
            return true;
        }
        return false;
    }
}
