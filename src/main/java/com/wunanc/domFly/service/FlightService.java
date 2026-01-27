package com.wunanc.domfly.service;

import com.wunanc.domfly.core.Context;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlightService {
    private final TerritoryService territoryService;
    private final LandingService landingService;
    private final ConfigManager config;
    private final Context context;

    private final Map<UUID, FlightState> playerStates = new ConcurrentHashMap<>();

    public FlightService(Context context, TerritoryService territoryService, LandingService landingService, ConfigManager config) {
        this.context = context;
        this.territoryService = territoryService;
        this.landingService = landingService;
        this.config = config;
    }

    public void toggle(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerStates.containsKey(uuid)) {
            disable(player);
            context.send(player, "flight.disabled");
        } else {
            if (territoryService.isInOwnTerritory(player)) {
                enable(player);
                context.send(player, "flight.enabled");
            } else {
                context.send(player, "flight.denied");
            }
        }
    }

    public void enable(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerStates.put(uuid, new ActiveState()) == null) {
            player.setAllowFlight(true);
            player.setFlying(true);
            if (config.isDebugEnabled()) context.log("debug.enabled", Map.of("player", player.getName()));
        }
    }

    public void disable(Player player) {
        UUID uuid = player.getUniqueId();
        FlightState removed = playerStates.remove(uuid);
        if (removed != null) {
            removed.cancel();
            player.setAllowFlight(false);
            player.setFlying(false);
            if (config.isDebugEnabled()) context.log("debug.disabled", Map.of("player", player.getName()));
        }
    }

    public void forceDisable(Player player) {
        disable(player);
        context.send(player, "flight.force_disabled");
    }

    public boolean isFlying(Player player) {
        return playerStates.containsKey(player.getUniqueId());
    }

    public void checkTerritory(Player player) {
        UUID uuid = player.getUniqueId();
        FlightState state = playerStates.get(uuid);
        if (state == null) return;

        if (!territoryService.isInOwnTerritory(player)) {
            handleExit(player, uuid, state);
        }
    }

    private void handleExit(Player player, UUID uuid, FlightState currentState) {
        currentState.cancel();

        if (DepartureMode.fromInt(config.getDepartureMode()) == DepartureMode.IMMEDIATE) {
            forceLand(player);
        } else {
            startBuffer(player, uuid);
        }
    }

    private void forceLand(Player player) {
        if (config.isAutoLand()) landingService.land(player);
        disable(player);
        context.send(player, "flight.auto_disabled");
    }

    private void startBuffer(Player player, UUID uuid) {
        int seconds = config.getBufferSeconds();
        BufferingState buffer = new BufferingState(uuid, seconds);
        playerStates.put(uuid, buffer);
        context.send(player, "flight.warning");

        buffer.task = Bukkit.getScheduler().runTaskTimer(context.getPlugin(), () -> {
            if (!player.isOnline()) {
                playerStates.remove(uuid);
                buffer.cancel();
                return;
            }

            if (territoryService.isInOwnTerritory(player)) {
                enable(player);
                return;
            }

            if (config.getWarningSeconds().contains(buffer.remaining)) {
                context.send(player, "flight.buffer_remains", Map.of("sec", String.valueOf(buffer.remaining)));
            }

            if (buffer.tick()) {
                forceLand(player);
            }
        }, 20L, 20L);
    }

    public void disableAll() {
        playerStates.values().forEach(FlightState::cancel);
        playerStates.forEach((uuid, state) -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        });
        playerStates.clear();
    }

    private enum DepartureMode {
        IMMEDIATE, BUFFERED;

        static DepartureMode fromInt(int mode) {
            return mode == 1 ? BUFFERED : IMMEDIATE;
        }
    }

    private interface FlightState {
        void cancel();
    }

    private static class ActiveState implements FlightState {
        @Override
        public void cancel() {}
    }

    private static class BufferingState implements FlightState {
        final UUID uuid;
        int remaining;
        BukkitTask task;

        BufferingState(UUID uuid, int seconds) {
            this.uuid = uuid;
            this.remaining = seconds;
        }

        boolean tick() {
            return --remaining < 0;
        }

        @Override
        public void cancel() {
            if (task != null) {
                task.cancel();
            }
        }
    }
}
