package com.wunanc.domfly.service;
// 这个类不用看了 基本上没实现什么 有缘人来写

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public final class ConfigService {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigService(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public long getCacheExpiry() {
        return config.getLong("cache_expiry_seconds", 30) * 1000L;
    }

    public boolean isCacheEnabled() {
        return config.getBoolean("cache_enabled", true);
    }

    public int getDepartureMode() {
        return config.getInt("departure_mode", 0);
    }

    public int getBufferSeconds() {
        return config.getInt("departure_buffer_seconds", 10);
    }

    public boolean isAutoLand() {
        return config.getBoolean("auto_land_on_departure", true);
    }

    public boolean isWarningEnabled() {
        return config.getBoolean("warnings.enable", true);
    }

    public List<Integer> getWarningSeconds() {
        return config.getIntegerList("warnings.warn_at_seconds");
    }

    public int getCheckInterval() {
        return config.getInt("move_check_interval", 2);
    }

    public int getMoveDistanceThreshold() {
        return config.getInt("move_distance_threshold", 1);
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }

    public boolean isGameModeDisabled(GameMode mode) {
        return config.getStringList("disabled_gamemodes").contains(mode.name());
    }

    public boolean isCheckOwnerOnly() {
        return config.getBoolean("check_owner_only", false);
    }
}
