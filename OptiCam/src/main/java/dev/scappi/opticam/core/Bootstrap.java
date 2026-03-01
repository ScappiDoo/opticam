package dev.scappi.opticam.core;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.camera.CameraManager;
import dev.scappi.opticam.camera.CameraService;
import dev.scappi.opticam.commands.CamCommand;
import dev.scappi.opticam.commands.CamShopCommand;
import dev.scappi.opticam.commands.OptiAdminCommand;
import dev.scappi.opticam.economy.EconomyProvider;
import dev.scappi.opticam.gui.GUIListener;
import dev.scappi.opticam.listeners.*;
import org.bukkit.plugin.PluginManager;

public final class Bootstrap {

    private final OptiCam plugin;
    private final OptiLogger log;
    private final OptiConfig config;

    private EconomyProvider economy;
    private CameraManager cameraManager;
    private CameraService cameraService;
    private ChatInputListener chatInputListener;

    public Bootstrap(OptiCam plugin) {
        this.plugin = plugin;
        this.log = new OptiLogger(plugin);
        this.config = new OptiConfig(plugin);
    }

    public boolean start() {

        plugin.saveDefaultConfig();
        config.reload();

        log.info("Starting OptiCam...");

        economy = new EconomyProvider(plugin);
        if (!economy.setup()) {
            log.severe("Vault economy not found.");
            return false;
        }

        cameraManager = new CameraManager(plugin);
        cameraManager.load();

        cameraService = new CameraService(plugin);

        registerCommands();
        registerListeners();

        log.info("OptiCam started.");
        return true;
    }

    public void stop() {
        if (cameraManager != null) {
            cameraManager.save();
        }
        log.info("OptiCam stopped.");
    }

    public void reloadAll() {
        plugin.reloadConfig();
        config.reload();

        if (cameraManager != null) cameraManager.load();
        if (economy != null) economy.reload();
    }

    private void registerCommands() {
        plugin.getCommand("camshop").setExecutor(new CamShopCommand(plugin));
        plugin.getCommand("cam").setExecutor(new CamCommand(plugin));
        plugin.getCommand("opticam").setExecutor(new OptiAdminCommand(plugin));
    }

    private void registerListeners() {

        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new ShopListener(plugin), plugin);
        pm.registerEvents(new CameraPlacementListener(plugin), plugin);
        pm.registerEvents(new CameraBreakListener(plugin), plugin);
        pm.registerEvents(new CameraExitListener(plugin), plugin);
        pm.registerEvents(new ComputerInteractListener(plugin), plugin);
        pm.registerEvents(new CameraViewListener(plugin), plugin);

        chatInputListener = new ChatInputListener(plugin);
        pm.registerEvents(chatInputListener, plugin);

        pm.registerEvents(new PowerListener(plugin), plugin);
        pm.registerEvents(new GUIListener(plugin), plugin);
    }

    public OptiLogger log() { return log; }
    public OptiConfig config() { return config; }

    public EconomyProvider economy() { return economy; }
    public CameraManager cameras() { return cameraManager; }
    public CameraService cameraService() { return cameraService; }
    public ChatInputListener chatInputListener() { return chatInputListener; }
}