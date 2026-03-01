package dev.scappi.opticam.camera;

import dev.scappi.opticam.OptiCam;
import dev.scappi.opticam.model.CameraAccessLevel;
import dev.scappi.opticam.model.CameraState;
import dev.scappi.opticam.model.CameraViewMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class CameraManager {

    private final OptiCam plugin;
    private final Map<UUID, Camera> cameras = new HashMap<>();

    private File file;
    private YamlConfiguration yaml;

    public CameraManager(OptiCam plugin) {
        this.plugin = plugin;
    }

    /* ------------------------------------------------ */
    /* Load / Save                                      */
    /* ------------------------------------------------ */

    public void load() {

        file = new File(plugin.getDataFolder(), "cameras.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create cameras.yml");
                return;
            }
        }

        yaml = YamlConfiguration.loadConfiguration(file);
        cameras.clear();

        var section = yaml.getConfigurationSection("cameras");
        if (section == null) return;

        for (String key : section.getKeys(false)) {

            try {
                UUID id = UUID.fromString(key);
                UUID owner = UUID.fromString(section.getString(key + ".owner"));

                String code = section.getString(key + ".code");
                String password = section.getString(key + ".password");

                String worldName = section.getString(key + ".world");
                var world = Bukkit.getWorld(worldName);
                if (world == null) continue;

                double x = section.getDouble(key + ".x");
                double y = section.getDouble(key + ".y");
                double z = section.getDouble(key + ".z");

                float baseYaw = (float) section.getDouble(key + ".baseYaw");

                Camera camera = new Camera(
                        id,
                        owner,
                        code,
                        new Location(world, x, y, z),
                        baseYaw
                );

                // Enums (safe parsing)
                camera.setAccessLevel(parseEnum(
                        section.getString(key + ".accessLevel"),
                        CameraAccessLevel.CODE_REQUIRED
                ));

                camera.setState(parseEnum(
                        section.getString(key + ".state"),
                        CameraState.ONLINE
                ));

                camera.setViewMode(parseEnum(
                        section.getString(key + ".viewMode"),
                        CameraViewMode.LIVE
                ));

                camera.setPassword(password);

                cameras.put(id, camera);

            } catch (Exception ex) {
                plugin.getLogger().warning("Failed to load camera: " + key);
            }
        }
    }

    public void save() {

        yaml.set("cameras", null);

        for (Camera cam : cameras.values()) {

            String path = "cameras." + cam.getId();

            yaml.set(path + ".owner", cam.getOwner().toString());
            yaml.set(path + ".code", cam.getCode());
            yaml.set(path + ".password", cam.getPassword());

            yaml.set(path + ".world", cam.getLocation().getWorld().getName());
            yaml.set(path + ".x", cam.getLocation().getX());
            yaml.set(path + ".y", cam.getLocation().getY());
            yaml.set(path + ".z", cam.getLocation().getZ());
            yaml.set(path + ".baseYaw", cam.getBaseYaw());

            yaml.set(path + ".accessLevel", cam.getAccessLevel().name());
            yaml.set(path + ".state", cam.getState().name());
            yaml.set(path + ".viewMode", cam.getViewMode().name());
        }

        try {
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save cameras.yml");
        }
    }

    /* ------------------------------------------------ */
    /* CRUD                                             */
    /* ------------------------------------------------ */

    public Camera create(UUID owner, Location loc, float baseYaw) {
        UUID id = UUID.randomUUID();
        Camera cam = new Camera(id, owner, null, loc, baseYaw);
        cameras.put(id, cam);
        return cam;
    }

    public void remove(UUID id) {
        cameras.remove(id);
    }

    public Optional<Camera> get(UUID id) {
        return Optional.ofNullable(cameras.get(id));
    }

    public Optional<Camera> getByCode(String code) {
        if (code == null) return Optional.empty();

        String search = code.toLowerCase(Locale.ROOT);

        return cameras.values().stream()
                .filter(c -> c.getCode() != null &&
                        c.getCode().toLowerCase(Locale.ROOT).equals(search))
                .findFirst();
    }

    public Collection<Camera> getAll() {
        return Collections.unmodifiableCollection(cameras.values());
    }

    public List<Camera> getOwned(UUID owner) {
        List<Camera> list = new ArrayList<>();
        for (Camera cam : cameras.values()) {
            if (cam.getOwner().equals(owner)) {
                list.add(cam);
            }
        }
        return list;
    }

    /* ------------------------------------------------ */
    /* Utility                                          */
    /* ------------------------------------------------ */

    private <T extends Enum<T>> T parseEnum(String value, T fallback) {
        if (value == null) return fallback;
        try {
            return Enum.valueOf(fallback.getDeclaringClass(), value);
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }
}