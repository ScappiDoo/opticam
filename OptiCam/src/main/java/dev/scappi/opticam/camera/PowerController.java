package dev.scappi.opticam.camera;

import org.bukkit.block.Block;

public final class PowerController {

    public static boolean hasPower(Camera camera) {
        Block block = camera.getLocation().getBlock();
        return block.isBlockPowered() || block.isBlockIndirectlyPowered();
    }
}