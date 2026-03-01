package dev.scappi.opticam.commands;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.core.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OptiAdminCommand implements CommandExecutor {

    private final OptiCam plugin;

    public OptiAdminCommand(OptiCam plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("opticam.admin")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§7Usage: §f/opticam reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            plugin.reloadAll();

            Messages.send(sender, plugin.cfg().prefix(), "§aOptiCam reloaded.");
            return true;
        }

        sender.sendMessage("§cUnknown subcommand.");
        return true;
    }
}