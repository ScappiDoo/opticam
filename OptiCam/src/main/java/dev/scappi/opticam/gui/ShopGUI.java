package dev.scappi.opticam.gui;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.core.Messages;
import dev.scappi.opticam.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShopGUI {

    public static final String TITLE = "§dOptiCam Shop";

    private final OptiCam plugin;

    public ShopGUI(OptiCam plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(11, ItemFactory.cameraItem(plugin));
        inv.setItem(15, ItemFactory.camComputerItem(plugin));

        player.openInventory(inv);
    }
}