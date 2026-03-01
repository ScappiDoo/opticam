package dev.scappi.opticam.listeners;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.Camera;
import dev.scappi.opticam.core.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatInputListener implements Listener {

    private final OptiCam plugin;

    // Player → Camera ID (for naming)
    private final Map<UUID, UUID> pendingNaming = new HashMap<>();

    // Player → true (for code access)
    private final Map<UUID, Boolean> pendingCodeAccess = new HashMap<>();

    public ChatInputListener(OptiCam plugin) {
        this.plugin = plugin;
    }

    /* ---------------- Naming Flow ---------------- */

    public void awaitNaming(org.bukkit.entity.Player player, UUID camId) {
        pendingNaming.put(player.getUniqueId(), camId);
    }

    /* ---------------- Code Access Flow ---------------- */

    public void awaitCode(org.bukkit.entity.Player player) {
        pendingCodeAccess.put(player.getUniqueId(), true);
    }

    /* ---------------- Chat Listener ---------------- */

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        UUID playerId = event.getPlayer().getUniqueId();

        // Naming
        if (pendingNaming.containsKey(playerId)) {

            event.setCancelled(true);

            UUID camId = pendingNaming.remove(playerId);
            String code = event.getMessage().trim();

            if (code.length() < 3) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        Messages.send(event.getPlayer(), plugin.cfg().prefix(),
                                "§cCode must be at least 3 characters."));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.cameras().get(camId).ifPresent(camera -> {
                    camera.setCode(code);
                    plugin.cameras().save();

                    Messages.send(event.getPlayer(), plugin.cfg().prefix(),
                            "§aCamera code set to §d" + code);
                });
            });

            return;
        }

        // Code access
        if (pendingCodeAccess.containsKey(playerId)) {

            event.setCancelled(true);
            pendingCodeAccess.remove(playerId);

            String code = event.getMessage().trim();

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.cameras().getByCode(code).ifPresentOrElse(
                        camera -> plugin.cameraService().startViewing(event.getPlayer(), camera),
                        () -> Messages.send(event.getPlayer(), plugin.cfg().prefix(),
                                "§cCamera not found.")
                );
            });
        }
    }
}