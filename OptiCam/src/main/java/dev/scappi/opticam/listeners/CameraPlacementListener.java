package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.Camera;
import dev.scappi.opticam.core.Messages;
import dev.scappi.opticam.items.ItemFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class CameraPlacementListener implements Listener {

    private final OptiCam plugin;

    public CameraPlacementListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        // Safety: must have item
        if (e.getItemInHand() == null) return;

        // Must be our camera item
        if (!ItemFactory.isCameraItem(plugin, e.getItemInHand())) return;

        // Allow both floor and wall heads
        Material type = e.getBlockPlaced().getType();
        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
            return;
        }

        int fee = plugin.cfg().priceCameraPlaceFee();

        // Check balance
        if (!plugin.economy().has(e.getPlayer().getUniqueId(), fee)) {
            e.setCancelled(true);
            Messages.send(e.getPlayer(), plugin.cfg().prefix(),
                    "§cYou need $" + fee + " to place this camera.");
            return;
        }

        // Withdraw safely
        if (!plugin.economy().withdraw(e.getPlayer().getUniqueId(), fee)) {
            e.setCancelled(true);
            Messages.send(e.getPlayer(), plugin.cfg().prefix(),
                    "§cTransaction failed. Try again.");
            return;
        }

        // Center block location
        Location loc = e.getBlockPlaced().getLocation().add(0.5, 0, 0.5);

        float baseYaw = e.getPlayer().getLocation().getYaw();

        // If wall-mounted, adjust yaw properly
        BlockData data = e.getBlockPlaced().getBlockData();
        if (data instanceof Rotatable rot) {
            baseYaw = switch (rot.getRotation()) {
                case NORTH -> 180f;
                case SOUTH -> 0f;
                case EAST -> -90f;
                case WEST -> 90f;
                default -> baseYaw;
            };
        }

        // Create camera
        Camera cam = plugin.cameras().create(
                e.getPlayer().getUniqueId(),
                loc,
                baseYaw
        );

        plugin.cameras().save();

        // Start naming flow
        plugin.chatInputListener().awaitNaming(e.getPlayer(), cam.getId());

        Messages.send(e.getPlayer(), plugin.cfg().prefix(),
                "§aCamera placed. Type a code in chat.");
    }
}