package com.wunanc.DomFly.model;

import org.bukkit.Location;
import java.util.Objects;
import java.util.UUID;

public final class WorldCoordinate {
    private final UUID worldUid;
    private final int x;
    private final int y;
    private final int z;

    public WorldCoordinate(Location location) {
        this.worldUid = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldCoordinate that = (WorldCoordinate) o;
        return x == that.x && y == that.y && z == that.z && Objects.equals(worldUid, that.worldUid);
    }
    @Override
    public int hashCode() {
        return Objects.hash(worldUid, x, y, z);
    }
}
