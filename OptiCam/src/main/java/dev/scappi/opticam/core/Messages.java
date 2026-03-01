package dev.scappi.opticam.core;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class Messages {

    private Messages() {}

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void send(CommandSender sender, String prefix, String message) {
        sender.sendMessage(color(prefix + message));
    }
}