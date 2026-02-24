package com.wunanc.DomFly.service;

import com.wunanc.DomFly.core.Context;
import java.util.List;

public final class ConfigManager {
    private final Context context;

    public ConfigManager(Context context) {
        this.context = context;
    }
    public int getCheckInterval() {
        return context.getInt("move_check_interval", 2);
    }
    public int getMoveDistanceThreshold() {
        return context.getInt("move_distance_threshold", 1);
    }
    public boolean isCacheEnabled() {
        return context.getBool("cache.enabled", true);
    }
    public long getCacheExpiryMillis() {
        return context.getLong("cache.expiry_seconds", 30) * 1000L;
    }
    public boolean isCheckOwnerOnly() {
        return context.getBool("check_owner_only", false);
    }
    public int getDepartureMode() {
        return context.getInt("flight.departure_mode", 0);
    }
    public int getBufferSeconds() {
        return context.getInt("flight.buffer_seconds", 10);
    }
    public boolean isAutoLand() {
        return context.getBool("flight.auto_land", true);
    }
    public List<Integer> getWarningSeconds() {
        return context.getFileConfig().getIntegerList("flight.warnings");
    }
    public boolean isDebugEnabled() {
        return context.getBool("debug", false);
    }
}
