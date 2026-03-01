package dev.scappi.opticam.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CameraNetwork {

    private final String name;
    private final UUID owner;
    private final Set<UUID> cameras = new HashSet<>();

    public CameraNetwork(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() { return name; }
    public UUID getOwner() { return owner; }

    public Set<UUID> getCameras() {
        return cameras;
    }

    public void addCamera(UUID cameraId) {
        cameras.add(cameraId);
    }

    public void removeCamera(UUID cameraId) {
        cameras.remove(cameraId);
    }
}