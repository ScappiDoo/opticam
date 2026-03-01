package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CameraQuitListener implements Listener {

    private final OptiCam plugin;

    public CameraQuitListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.cameraService().cleanup(event.getPlayer());
    }
}