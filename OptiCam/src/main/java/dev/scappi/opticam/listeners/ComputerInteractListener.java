package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.gui.ComputerGUI;
import dev.scappi.opticam.items.ItemFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ComputerInteractListener implements Listener {

    private final OptiCam plugin;
    private final ComputerGUI gui = new ComputerGUI();

    public ComputerInteractListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getItem() == null) return;
        if (!ItemFactory.isComputerItem(plugin, e.getItem())) return;

        e.setCancelled(true);
        gui.open(e.getPlayer());
    }
}