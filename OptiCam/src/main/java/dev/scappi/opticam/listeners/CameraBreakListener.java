package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.Camera;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class CameraBreakListener implements Listener {

    private final OptiCam plugin;

    public CameraBreakListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        if (e.getBlock().getType() != org.bukkit.Material.PLAYER_HEAD) return;

        var loc = e.getBlock().getLocation().add(0.5, 0, 0.5);

        Optional<Camera> cam = plugin.cameras().getAll().stream()
                .filter(c -> c.getLocation().distanceSquared(loc) < 0.01)
                .findFirst();

        if (cam.isEmpty()) return;

        plugin.cameras().remove(cam.get().getId());
        plugin.cameras().save();
    }
}