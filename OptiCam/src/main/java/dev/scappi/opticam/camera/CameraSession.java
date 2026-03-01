package dev.scappi.opticam.camera;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CameraSession {

    private final Player player;
    private final Camera camera;

    private final Location previousLocation;
    private final GameMode previousMode;

    // Smooth motor fields
    private float currentYaw;
    private float currentPitch;

    private float targetYaw;
    private float targetPitch;

    private double zoom;

    // Motor task id
    private int motorTaskId = -1;

    public CameraSession(Player player, Camera camera) {
        this.player = player;
        this.camera = camera;

        this.previousLocation = player.getLocation().clone();
        this.previousMode = player.getGameMode();

        this.currentYaw = camera.getBaseYaw();
        this.currentPitch = 0f;

        this.targetYaw = camera.getBaseYaw();
        this.targetPitch = 0f;

        this.zoom = 0.0;
    }

    /* ---------------- Core ---------------- */

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    /* ---------------- Restore State ---------------- */

    public Location getPreviousLocation() {
        return previousLocation.clone();
    }

    public GameMode getPreviousMode() {
        return previousMode;
    }

    /* ---------------- Rotation ---------------- */

    public float getCurrentYaw() {
        return currentYaw;
    }

    public void setCurrentYaw(float currentYaw) {
        this.currentYaw = currentYaw;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    public void setCurrentPitch(float currentPitch) {
        this.currentPitch = currentPitch;
    }

    public float getTargetYaw() {
        return targetYaw;
    }

    public void setTargetYaw(float targetYaw) {
        this.targetYaw = targetYaw;
    }

    public float getTargetPitch() {
        return targetPitch;
    }

    public void setTargetPitch(float targetPitch) {
        this.targetPitch = targetPitch;
    }

    /* ---------------- Zoom ---------------- */

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    /* ---------------- Motor Task ---------------- */

    public int getMotorTaskId() {
        return motorTaskId;
    }

    public void setMotorTaskId(int motorTaskId) {
        this.motorTaskId = motorTaskId;
    }
}