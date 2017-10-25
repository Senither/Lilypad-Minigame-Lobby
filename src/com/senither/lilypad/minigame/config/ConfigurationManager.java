package com.senither.lilypad.minigame.config;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.boards.BoardManager;
import com.senither.lilypad.minigame.boards.GameBoard;

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

    public Configuration getConfigFromGameBoard(GameBoard gameBoard) {
        return new Configuration(plugin, folder,
                gameBoard.getName().toLowerCase().replace(" ", "_") + ".yml"
        );
    }

    public void loadBoards(BoardManager boardsInstance) {
        for (final File file : folder.listFiles()) {
            if (file.isFile()) {
                Configuration config = new Configuration(plugin, folder, file.getName());

                if (config.contains("name", "locations.first", "locations.second", "channel", "format")) {
                    boardsInstance.createGameBoardFromConfig(config);
                }
            }
        }
    }

    public boolean createBoardConfiguration(GameBoard board) {
        Configuration config = new Configuration(plugin, folder,
                board.getName().toLowerCase().replace(" ", "_") + ".yml"
        );

        config.getConfig().set("name", board.getName());
        config.getConfig().set("locations.first", board.getFirstLocation().toString());
        config.getConfig().set("locations.second", board.getSecondLocation().toString());
        config.getConfig().set("channel", board.getGameChannel());
        config.getConfig().set("format", board.getFormat().getFormat());
        config.getConfig().set("flip", false);
        config.saveConfig();

        return true;
    }
}
