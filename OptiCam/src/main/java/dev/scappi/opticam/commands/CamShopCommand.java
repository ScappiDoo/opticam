package dev.scappi.opticam.commands;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.core.Messages;
import dev.scappi.opticam.gui.ShopGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CamShopCommand implements CommandExecutor {

    private final OptiCam plugin;

    public CamShopCommand(OptiCam plugin) {
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

        new ShopGUI(plugin).open(player);
        return true;
    }
}