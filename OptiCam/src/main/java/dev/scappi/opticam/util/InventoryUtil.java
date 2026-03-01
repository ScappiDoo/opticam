package dev.scappi.opticam.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtil {

    private InventoryUtil() {}

    public static boolean hasEmptySlot(Inventory inv) {
        return inv.firstEmpty() != -1;
    }

    public static void fillBorders(Inventory inv, ItemStack filler) {
        int size = inv.getSize();

        for (int i = 0; i < size; i++) {
            int row = i / 9;
            int col = i % 9;

            if (row == 0 || row == (size / 9 - 1) || col == 0 || col == 8) {
                inv.setItem(i, filler);
            }
        }
    }
}