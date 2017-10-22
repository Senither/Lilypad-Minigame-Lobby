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

    Configuration(LilypadMinigameLobby plugin, File folder, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.folder = folder;
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
        if (fileConfiguration == null || configFile == null) {
            return;
        } else {
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

    public boolean contains(String... fields) {
        for (String field : fields) {
            if (!getConfig().contains(field)) {
                return false;
            }
        }
        return true;
    }
}