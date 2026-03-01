package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.CameraSession;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record CameraViewListener(OptiCam plugin) implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (!plugin.cameraService().isViewing(event.getPlayer())) return;

        CameraSession session =
                plugin.cameraService().getSession(event.getPlayer());

        if (session == null) return;

        // Freeze XYZ
        Location from = event.getFrom();
        Location to = event.getTo();

        to.setX(from.getX());
        to.setY(from.getY());
        to.setZ(from.getZ());

        // Update rotation target
        session.setTargetYaw(to.getYaw());
        session.setTargetPitch(to.getPitch());
    }
}