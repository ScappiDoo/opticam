package dev.scappi.opticam.util;

import org.bukkit.inventory.ItemStack;

public final class ItemUtil {

    private ItemUtil() {}

    public static boolean isNullOrAir(ItemStack stack) {
        return stack == null || stack.getType().isAir();
    }

    public static boolean hasMeta(ItemStack stack) {
        return stack != null && stack.hasItemMeta();
    }
}