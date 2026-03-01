package dev.scappi.opticam.core;

import dev.scappi.opticam.OptiCam;
import org.bukkit.ChatColor;

public final class OptiLogger {

    private final OptiCam plugin;

    public OptiLogger(OptiCam plugin) {
        this.plugin = plugin;
    }

    public void info(String msg) {
        plugin.getLogger().info(stripColor(color(msg)));
    }

    public void warn(String msg) {
        plugin.getLogger().warning(stripColor(color(msg)));
    }

    public void severe(String msg) {
        plugin.getLogger().severe(stripColor(color(msg)));
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String stripColor(String s) {
        return ChatColor.stripColor(s);
    }
}