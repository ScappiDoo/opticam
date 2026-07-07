package dev.scappi.opticam.economy;

import dev.scappi.opticam.OptiCam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Method;
import java.util.UUID;

public final class EconomyProvider {

    private final OptiCam plugin;

    private Object economy; // Vault economy provider (reflection)

    private Method hasMethod;
    private Method withdrawMethod;
    private Method depositMethod;
    private Method balanceMethod;
    private Method formatMethod;

    public EconomyProvider(OptiCam plugin) {
        this.plugin = plugin;
    }

    /* ------------------------------------------------ */
    /* Setup (Reflection Hook)                          */
    /* ------------------------------------------------ */

    public boolean setup() {

        try {
            // Check Vault plugin exists
            if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                plugin.getLogger().warning("Vault not found.");
                return false;
            }

            // Load Vault Economy class dynamically
            Class<?> econClass =
                    Class.forName("net.milkbowl.vault.economy.Economy");

            RegisteredServiceProvider<?> rsp =
                    Bukkit.getServicesManager().getRegistration(econClass);

            if (rsp == null) {
                plugin.getLogger().warning("No economy provider registered.");
                return false;
            }

            economy = rsp.getProvider();
            if (economy == null) {
                plugin.getLogger().warning("Economy provider is null.");
                return false;
            }

            // Cache methods
            hasMethod = econClass.getMethod("has", OfflinePlayer.class, double.class);
            withdrawMethod = econClass.getMethod("withdrawPlayer", OfflinePlayer.class, double.class);
            depositMethod = econClass.getMethod("depositPlayer", OfflinePlayer.class, double.class);
            balanceMethod = econClass.getMethod("getBalance", OfflinePlayer.class);
            formatMethod = econClass.getMethod("format", double.class);

            plugin.getLogger().info("Hooked Vault economy successfully.");
            return true;

        } catch (Exception ex) {
            plugin.getLogger().warning("Failed to hook Vault economy.");
            return false;
        }
    }

    /* ------------------------------------------------ */
    /* Safe API                                         */
    /* ------------------------------------------------ */

    public boolean isAvailable() {
        return economy != null;
    }

    public boolean has(UUID uuid, double amount) {
        if (!isAvailable()) return false;

        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            return (boolean) hasMethod.invoke(economy, player, amount);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean withdraw(UUID uuid, double amount) {
        if (!isAvailable()) return false;

        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            Object response = withdrawMethod.invoke(economy, player, amount);
            return wasSuccessful(response);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deposit(UUID uuid, double amount) {
        if (!isAvailable()) return false;

        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            Object response = depositMethod.invoke(economy, player, amount);
            return wasSuccessful(response);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vault's EconomyResponse is non-null even on failure (e.g. insufficient
     * funds), so a null check is not enough. Reflectively read
     * {@code transactionSuccess()} to know if the transaction actually went
     * through.
     */
    private boolean wasSuccessful(Object response) {
        if (response == null) return false;
        try {
            Object ok = response.getClass().getMethod("transactionSuccess").invoke(response);
            return ok instanceof Boolean && (Boolean) ok;
        } catch (Exception e) {
            // If the provider doesn't expose the method, fall back to non-null.
            return true;
        }
    }

    public double getBalance(UUID uuid) {
        if (!isAvailable()) return 0.0;

        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            return (double) balanceMethod.invoke(economy, player);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String format(double amount) {
        if (!isAvailable()) return String.valueOf(amount);

        try {
            return (String) formatMethod.invoke(economy, amount);
        } catch (Exception e) {
            return String.valueOf(amount);
        }
    }

    public void reload() {
        economy = null;
        setup();
    }
}