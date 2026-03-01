package dev.scappi.opticam.camera;

import dev.scappi.opticam.model.CameraAccessLevel;
import dev.scappi.opticam.model.CameraState;
import dev.scappi.opticam.model.CameraViewMode;
import org.bukkit.Location;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Camera {

    private final UUID id;
    private final UUID owner;

    private String code;
    private String password; // optional

    private Location location;   // center of head block
    private float baseYaw;       // mount direction

    private CameraAccessLevel accessLevel;
    private CameraState state;
    private CameraViewMode viewMode;

    private final Instant createdAt;

    public Camera(UUID id,
                  UUID owner,
                  String code,
                  Location location,
                  float baseYaw) {

        this.id = Objects.requireNonNull(id);
        this.owner = Objects.requireNonNull(owner);
        this.code = code;
        this.location = Objects.requireNonNull(location).clone();
        this.baseYaw = baseYaw;

        this.accessLevel = CameraAccessLevel.CODE_REQUIRED;

        // 🔴 Redstone cameras start OFFLINE
        this.state = CameraState.OFFLINE;

        this.viewMode = CameraViewMode.LIVE;
        this.createdAt = Instant.now();
    }

    /* ---------------- Identity ---------------- */

    public UUID getId() { return id; }
    public UUID getOwner() { return owner; }

    public Instant getCreatedAt() { return createdAt; }

    /* ---------------- Access ---------------- */

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public CameraAccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(CameraAccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /* ---------------- State ---------------- */

    public CameraState getState() { return state; }

    public void setState(CameraState state) {
        this.state = state;
    }

    public boolean isOnline() {
        return state == CameraState.ONLINE;
    }

    public void powerOn() {
        this.state = CameraState.ONLINE;
    }

    public void powerOff() {
        this.state = CameraState.OFFLINE;
    }

    /* ---------------- View Mode ---------------- */

    public CameraViewMode getViewMode() { return viewMode; }

    public void setViewMode(CameraViewMode viewMode) {
        this.viewMode = viewMode;
    }

    /* ---------------- Position ---------------- */

    public Location getLocation() {
        return location.clone(); // prevent external mutation
    }

    public void setLocation(Location location) {
        this.location = Objects.requireNonNull(location).clone();
    }

    public float getBaseYaw() { return baseYaw; }

    public void setBaseYaw(float baseYaw) {
        this.baseYaw = baseYaw;
    }
}