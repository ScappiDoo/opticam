package dev.scappi.opticam.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class SchedulerUtil {

    private SchedulerUtil() {}

    public static void runSync(Plugin plugin, Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    public static void runAsync(Plugin plugin, Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public static void runLater(Plugin plugin, Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    public static void runTimer(Plugin plugin, Runnable task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }
}