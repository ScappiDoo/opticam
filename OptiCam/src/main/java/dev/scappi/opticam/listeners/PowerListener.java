package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.Camera;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class PowerListener implements Listener {

    private final OptiCam plugin;

    public PowerListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {

        Location blockLoc = event.getBlock().getLocation();

        // Check this block and all adjacent blocks for cameras
        for (Camera cam : plugin.cameras().getAll()) {

            Location camLoc = cam.getLocation().getBlock().getLocation();

            // Check if redstone change affects camera block
            if (camLoc.equals(blockLoc) ||
                    camLoc.clone().add(1,0,0).equals(blockLoc) ||
                    camLoc.clone().add(-1,0,0).equals(blockLoc) ||
                    camLoc.clone().add(0,1,0).equals(blockLoc) ||
                    camLoc.clone().add(0,-1,0).equals(blockLoc) ||
                    camLoc.clone().add(0,0,1).equals(blockLoc) ||
                    camLoc.clone().add(0,0,-1).equals(blockLoc)) {

                boolean powered = event.getNewCurrent() > 0;

                if (powered) {
                    cam.powerOn();
                } else {
                    cam.powerOff();
                }
                plugin.cameras().save();
            }
        }
    }
}