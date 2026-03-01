package dev.scappi.opticam.gui;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.Camera;
import dev.scappi.opticam.core.Messages;
import dev.scappi.opticam.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class GUIListener implements Listener {

    private final OptiCam plugin;
    private final ShopGUI shopGUI;
    private final ComputerGUI computerGUI;
    private final CameraSelectGUI selectGUI;

    public GUIListener(OptiCam plugin) {
        this.plugin = plugin;
        this.shopGUI = new ShopGUI(plugin);
        this.computerGUI = new ComputerGUI();
        this.selectGUI = new CameraSelectGUI();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        String title = e.getView().getTitle();
        if (title == null) return;

        /* ---------------- SHOP GUI ---------------- */

        if (title.equals(ShopGUI.TITLE)) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            // Buy Camera
            if (ItemFactory.isCameraItem(plugin, e.getCurrentItem())) {
                int price = plugin.cfg().priceCameraItem();

                if (!plugin.economy().has(player.getUniqueId(), price)) {
                    Messages.send(player, plugin.cfg().prefix(), "§cNot enough money.");
                    return;
                }

                if (!plugin.economy().withdraw(player.getUniqueId(), price)) {
                    Messages.send(player, plugin.cfg().prefix(), "§cTransaction failed.");
                    return;
                }

                player.getInventory().addItem(ItemFactory.cameraItem(plugin));
                Messages.send(player, plugin.cfg().prefix(), "§aCamera purchased.");
                return;
            }

            // Buy CamComputer
            if (ItemFactory.isComputerItem(plugin, e.getCurrentItem())) {
                int price = plugin.cfg().priceCamComputerItem();

                if (!plugin.economy().has(player.getUniqueId(), price)) {
                    Messages.send(player, plugin.cfg().prefix(), "§cNot enough money.");
                    return;
                }

                if (!plugin.economy().withdraw(player.getUniqueId(), price)) {
                    Messages.send(player, plugin.cfg().prefix(), "§cTransaction failed.");
                    return;
                }

                player.getInventory().addItem(ItemFactory.camComputerItem(plugin));
                Messages.send(player, plugin.cfg().prefix(), "§aCamComputer purchased.");
                return;
            }

            return;
        }

        /* ---------------- COMPUTER GUI ---------------- */

        if (title.equals(ComputerGUI.TITLE)) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            Material type = e.getCurrentItem().getType();

            // My Cameras
            if (type == Material.ENDER_EYE) {
                List<Camera> cams = plugin.cameras().getOwned(player.getUniqueId());

                if (cams.isEmpty()) {
                    Messages.send(player, plugin.cfg().prefix(), "§cYou own no cameras.");
                    return;
                }

                selectGUI.open(player, cams);
                return;
            }

            // Enter Camera Code
            if (type == Material.NAME_TAG) {
                Messages.send(player, plugin.cfg().prefix(), "§7Type camera code in chat.");
                player.closeInventory();

                // ✅ Correct new API
                plugin.chatInputListener().awaitCode(player);
                return;
            }

            return;
        }

        /* ---------------- CAMERA SELECT GUI ---------------- */

        if (title.equals(CameraSelectGUI.TITLE)) {
            e.setCancelled(true);

            List<Camera> cams = plugin.cameras().getOwned(player.getUniqueId());
            int slot = e.getSlot();

            if (slot < 0 || slot >= cams.size()) return;

            Camera selected = cams.get(slot);

            // ✅ Correct new API
            plugin.cameraService().startViewing(player, selected);
        }
    }
}