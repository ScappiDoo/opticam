package dev.scappi.opticam.gui;

import dev.scappi.opticam.camera.Camera;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CameraSelectGUI {

    public static final String TITLE = "§dSelect Camera";

    public void open(Player player, List<Camera> cameras) {

        Inventory inv = Bukkit.createInventory(null, 54, TITLE);

        int slot = 0;

        for (Camera cam : cameras) {
            if (slot >= inv.getSize()) break;

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§d" + cam.getCode());
            meta.setLore(List.of(
                    "§7World: §f" + cam.getLocation().getWorld().getName(),
                    "§7X: §f" + cam.getLocation().getBlockX(),
                    "§7Y: §f" + cam.getLocation().getBlockY(),
                    "§7Z: §f" + cam.getLocation().getBlockZ(),
                    "§8Click to view"
            ));
            item.setItemMeta(meta);

            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }
}