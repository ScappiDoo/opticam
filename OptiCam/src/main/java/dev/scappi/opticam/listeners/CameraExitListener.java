package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CameraExitListener implements Listener {

    private final OptiCam plugin;

    public CameraExitListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        // Only trigger when starting to sneak (pressing shift down)
        if (!event.isSneaking()) return;

        if (!plugin.cameraService().isViewing(event.getPlayer())) return;

        plugin.cameraService().stopViewing(event.getPlayer());
    }
}