package com.senither.lilypad.minigame.config;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration {

    private final LilypadMinigameLobby plugin;
    private final String fileName;

    private File folder;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public Configuration(LilypadMinigameLobby plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("plugin must be initialized");
        }

        this.plugin = plugin;
        this.fileName = fileName;
        this.folder = plugin.getDataFolder();
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig() {
        if (configFile == null) {
            if (folder == null) {
                throw new IllegalStateException();
            }
            configFile = new File(folder, fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                Envoyer.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
