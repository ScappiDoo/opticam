package dev.scappi.opticam.util;

import org.bukkit.Location;

public final class LocationUtil {

    private LocationUtil() {}

    public static Location centered(Location loc) {
        return loc.clone().add(0.5, 0, 0.5);
    }

    public static Location headHeight(Location loc) {
        return loc.clone().add(0, 1.6, 0);
    }

    public static boolean equalsBlockCenter(Location a, Location b) {
        return a.getWorld().equals(b.getWorld())
                && a.distanceSquared(b) < 0.01;
    }
}