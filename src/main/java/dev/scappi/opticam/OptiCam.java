package dev.scappi.opticam;

import dev.scappi.opticam.camera.CameraManager;
import dev.scappi.opticam.camera.CameraService;
import dev.scappi.opticam.core.Bootstrap;
import dev.scappi.opticam.core.OptiConfig;
import dev.scappi.opticam.core.OptiLogger;
import dev.scappi.opticam.economy.EconomyProvider;
import dev.scappi.opticam.listeners.ChatInputListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OptiCam extends JavaPlugin {

    private static OptiCam instance;
    private Bootstrap bootstrap;

    @Override
    public void onEnable() {
        instance = this;

        bootstrap = new Bootstrap(this);

        if (!bootstrap.start()) {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (bootstrap != null) bootstrap.stop();
    }

    public static OptiCam get() {
        return instance;
    }

    public OptiLogger log() {
        return bootstrap.log();
    }

    public OptiConfig cfg() {
        return bootstrap.config();
    }

    public EconomyProvider economy() {
        return bootstrap.economy();
    }

    public CameraManager cameras() {
        return bootstrap.cameras();
    }

    public CameraService cameraService() {
        return bootstrap.cameraService();
    }

    public ChatInputListener chatInputListener() {
        return bootstrap.chatInputListener();
    }

    public void reloadAll() {
        bootstrap.reloadAll();
    }
}