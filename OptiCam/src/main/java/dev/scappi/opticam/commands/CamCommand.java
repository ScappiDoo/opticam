package dev.scappi.opticam.commands;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.core.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CamCommand implements CommandExecutor {

    private final OptiCam plugin;

    public CamCommand(OptiCam plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("opticam.use")) {
            Messages.send(player, plugin.cfg().prefix(), "§cNo permission.");
            return true;
        }

        if (args.length == 0) {
            Messages.send(player, plugin.cfg().prefix(), "§7Usage: §f/cam exit");
            return true;
        }

        if (args[0].equalsIgnoreCase("exit")) {
            plugin.cameraService().forceExit(player);
            return true;
        }

        Messages.send(player, plugin.cfg().prefix(), "§cUnknown subcommand.");
        return true;
    }
}