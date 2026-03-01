package dev.scappi.opticam.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ComputerGUI {

    public static final String TITLE = "§dCamComputer";

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack owned = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta1 = owned.getItemMeta();
        meta1.setDisplayName("§dMy Cameras");
        meta1.setLore(List.of("§7View your cameras"));
        owned.setItemMeta(meta1);

        ItemStack code = new ItemStack(Material.NAME_TAG);
        ItemMeta meta2 = code.getItemMeta();
        meta2.setDisplayName("§dEnter Camera Code");
        meta2.setLore(List.of("§7Access via shared code"));
        code.setItemMeta(meta2);

        inv.setItem(11, owned);
        inv.setItem(15, code);

        player.openInventory(inv);
    }
}