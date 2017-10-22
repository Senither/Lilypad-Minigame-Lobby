package com.senither.lilypad.minigame.config;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.boards.BoardManager;
import com.senither.lilypad.minigame.boards.GameLocation;
import com.senither.lilypad.minigame.boards.GameSignFormat;
import com.senither.lilypad.minigame.boards.GameWall;

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

    public void loadBoards(BoardManager boardsInstance) {
        for (final File file : folder.listFiles()) {
            if (file.isFile()) {
                Configuration config = new Configuration(plugin, folder, file.getName());

                if (config.contains("name", "locations.first", "locations.second", "channel", "format")) {
                    String name = config.getConfig().getString("name");
                    String channel = config.getConfig().getString("channel");

                    GameLocation first = new GameLocation(config.getConfig().getString("locations.first"));
                    GameLocation second = new GameLocation(config.getConfig().getString("locations.second"));

                    GameSignFormat format = new GameSignFormat(config.getConfig().getStringList("format"));

                    boardsInstance.createGameWall(name, channel, format, first, second);
                }
            }
        }
    }

    public boolean createBoardConfiguration(GameWall board) {
        Configuration config = new Configuration(plugin, folder,
                board.getWallName().toLowerCase().replace(" ", "_") + ".yml"
        );

        config.getConfig().set("name", board.getWallName());
        config.getConfig().set("locations.first", board.getFirstLocation().toString());
        config.getConfig().set("locations.second", board.getSecondLocation().toString());
        config.getConfig().set("channel", board.getGameChannel());
        config.getConfig().set("format", board.getFormat().getFormat());
        config.saveConfig();

        return true;
    }
}
