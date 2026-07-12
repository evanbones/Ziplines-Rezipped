package com.evandev.zipline.config;

import com.evandev.zipline.Constants;
import com.evandev.zipline.platform.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = Services.PLATFORM.getConfigDirectory().resolve("zipline.json").toFile();

    private static ModConfig INSTANCE;
    private static ModConfig BACKUP;
    public transient boolean isServerConfig = false;

    public double snapRadius = 2.0;
    public double clickReach = 3.0;
    public boolean useAnywhere = false;
    public double maxTurnAngle = 0.707;
    public double hangOffset = 2.3;
    public double speedMultiplier = 1.0;
    public boolean realisticPhysics = false;
    public double exitJumpMultiplier = 1.4;
    public boolean consumeDurability = true;
    public int releaseCooldown = 10;
    public boolean jumpRequiredToDismount = true;

    public static ModConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                Constants.LOG.error("Failed to load zipline.json", e);
                INSTANCE = new ModConfig();
            }
        } else {
            INSTANCE = new ModConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save zipline.json", e);
        }
    }

    /**
     * Applies the server configuration and backs up the local one.
     */
    public static void setServerConfig(ModConfig serverConfig) {
        if (BACKUP == null) {
            BACKUP = INSTANCE;
        }
        serverConfig.isServerConfig = true;
        INSTANCE = serverConfig;
        Constants.LOG.info("Applied server configuration for Zipline.");
    }

    /**
     * Restores the local configuration when disconnecting from a server.
     */
    public static void restoreLocalConfig() {
        if (BACKUP != null) {
            INSTANCE = BACKUP;
            BACKUP = null;
            Constants.LOG.info("Restored local configuration for Zipline.");
        }
    }
}