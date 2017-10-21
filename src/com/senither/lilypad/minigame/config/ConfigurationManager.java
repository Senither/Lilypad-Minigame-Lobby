package com.senither.lilypad.minigame.config;

import com.senither.lilypad.minigame.LilypadMinigameLobby;

import java.io.File;

public class ConfigurationManager {

    private final LilypadMinigameLobby plugin;
    private final File folder;

    public ConfigurationManager(LilypadMinigameLobby plugin) {
        this.plugin = plugin;
        this.folder = new File(this.plugin.getDataFolder(), "Boards");

        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
    }
}
