package dev.scappi.opticam.camera;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.core.Messages;
import dev.scappi.opticam.model.CameraState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraService {

    private final OptiCam plugin;
    private final Map<UUID, CameraSession> sessions = new HashMap<>();

    public CameraService(OptiCam plugin) {
        this.plugin = plugin;
    }

    /* ------------------------------------------------ */
    /* Start Viewing                                    */
    /* ------------------------------------------------ */

    public void startViewing(Player player, Camera camera) {

        if (!PowerController.hasPower(camera)) {
            Messages.send(player, plugin.cfg().prefix(),
                    "§cCamera has no power.");
            return;
        }

        stopViewing(player);

        CameraSession session = new CameraSession(player, camera);
        sessions.put(player.getUniqueId(), session);

        Location camLoc = camera.getLocation().clone().add(0, 1.6, 0);
        camLoc.setYaw(camera.getBaseYaw());
        camLoc.setPitch(0f);

        /* ---- VANISH ---- */

        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.hidePlayer(plugin, player);
            }
        }

        player.setCollidable(false);
        player.setInvulnerable(true);
        player.setSilent(true);
        player.setInvisible(true);

        /* ---- LOCK MOVEMENT ---- */

        player.setWalkSpeed(0f);

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(camLoc);

        Messages.send(player, plugin.cfg().prefix(),
                "§aViewing camera §d" + camera.getCode());

        /* ---- MOTOR LOOP ---- */

        int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {

                    CameraSession current = sessions.get(player.getUniqueId());
                    if (current == null) return;

                    if (!PowerController.hasPower(current.getCamera())) {
                        current.getCamera().setState(CameraState.OFFLINE);
                        stopViewing(player);
                        return;
                    }

                    MotorController.update(
                            current,
                            current.getTargetYaw(),
                            current.getTargetPitch()
                    );
                },
                0L,
                1L
        );

        session.setMotorTaskId(taskId);
    }

    /* ------------------------------------------------ */
    /* Stop Viewing                                     */
    /* ------------------------------------------------ */

    public void stopViewing(Player player) {

        CameraSession session = sessions.remove(player.getUniqueId());
        if (session == null) return;

        if (session.getMotorTaskId() != -1) {
            plugin.getServer().getScheduler().cancelTask(session.getMotorTaskId());
        }

        /* ---- RESTORE VISIBILITY ---- */

        for (Player online : plugin.getServer().getOnlinePlayers()) {
            online.showPlayer(plugin, player);
        }

        player.setCollidable(true);
        player.setInvulnerable(false);
        player.setSilent(false);
        player.setInvisible(false);

        /* ---- RESTORE MOVEMENT ---- */

        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.setAllowFlight(false);
        player.setFlying(false);

        /* ---- RESTORE POSITION ---- */

        player.teleport(session.getPreviousLocation());
        player.setGameMode(session.getPreviousMode());

        Messages.send(player, plugin.cfg().prefix(),
                "§7Exited camera.");
    }

    /* ------------------------------------------------ */

    public boolean isViewing(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public CameraSession getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }

    public void forceExit(Player player) {
        stopViewing(player);
    }

    public void cleanup(Player player) {
        stopViewing(player);
    }
}