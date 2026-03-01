package dev.scappi.opticam.items;

import dev.scappi.opticam.OptiCam;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class ItemFactory {

    private static final String CAMERA_KEY = "camera_item";
    private static final String COMPUTER_KEY = "camcomputer_item";

    private ItemFactory() {}

    /* ---------------------------------------------------------------- */
    /* Camera Item                                                      */
    /* ---------------------------------------------------------------- */

    public static ItemStack cameraItem(OptiCam plugin) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§d§lOptiCam Camera");
        meta.setLore(List.of(
                "§7Place this head to create",
                "§7a surveillance camera.",
                "",
                "§8Placement fee: §a$" + plugin.cfg().priceCameraPlaceFee()
        ));

        tag(meta, plugin, CAMERA_KEY);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCameraItem(OptiCam plugin, ItemStack stack) {
        return hasTag(plugin, stack, CAMERA_KEY);
    }

    /* ---------------------------------------------------------------- */
    /* CamComputer Item                                                 */
    /* ---------------------------------------------------------------- */

    public static ItemStack camComputerItem(OptiCam plugin) {

        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§d§lCamComputer");
        meta.setLore(List.of(
                "§7Right-click to access",
                "§7your camera network."
        ));

        tag(meta, plugin, COMPUTER_KEY);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isComputerItem(OptiCam plugin, ItemStack stack) {
        return hasTag(plugin, stack, COMPUTER_KEY);
    }

    /* ---------------------------------------------------------------- */
    /* Internal Tag Utilities                                           */
    /* ---------------------------------------------------------------- */

    private static void tag(ItemMeta meta, OptiCam plugin, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(namespacedKey, PersistentDataType.BYTE, (byte) 1);
    }

    private static boolean hasTag(OptiCam plugin, ItemStack stack, String key) {

        if (stack == null) return false;
        if (!stack.hasItemMeta()) return false;

        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer container =
                stack.getItemMeta().getPersistentDataContainer();

        return container.has(namespacedKey, PersistentDataType.BYTE);
    }
}