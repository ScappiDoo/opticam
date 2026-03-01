package dev.scappi.opticam.core;

import dev.scappi.opticam.OptiCam;

public final class OptiConfig {

    private final OptiCam plugin;

    // Prices
    private int priceCameraItem;
    private int priceCameraPlaceFee;
    private int priceCamComputerItem;

    // Rotation/zoom realism
    private double yawLimitDegrees;
    private double zoomMin;
    private double zoomMax;
    private double zoomStep;

    // Messages
    private String prefix;

    public OptiConfig(OptiCam plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        var c = plugin.getConfig();

        priceCameraItem = c.getInt("prices.camera_item", 5000);
        priceCameraPlaceFee = c.getInt("prices.camera_place_fee", 500);
        priceCamComputerItem = c.getInt("prices.camcomputer_item", 5000);

        yawLimitDegrees = c.getDouble("rotation.limit_degrees", 90.0);

        zoomMin = c.getDouble("zoom.min", 0.0);
        zoomMax = c.getDouble("zoom.max", 4.0);
        zoomStep = c.getDouble("zoom.step", 0.35);

        prefix = c.getString("messages.prefix", "&8[&dOptiCam&8]&7 ");
    }

    // Prices
    public int priceCameraItem() { return priceCameraItem; }
    public int priceCameraPlaceFee() { return priceCameraPlaceFee; }
    public int priceCamComputerItem() { return priceCamComputerItem; }

    // Rotation/zoom
    public double yawLimitDegrees() { return yawLimitDegrees; }
    public double zoomMin() { return zoomMin; }
    public double zoomMax() { return zoomMax; }
    public double zoomStep() { return zoomStep; }

    // Messages
    public String prefix() { return prefix; }
}