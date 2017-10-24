package com.senither.lilypad.minigame.boards;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.network.Server;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BoardManager implements Runnable {

    private final LilypadMinigameLobby plugin;
    private final HashMap<String, GameBoard> boards = new HashMap<>();

    public BoardManager(LilypadMinigameLobby plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 60, 20);
        this.plugin.getConfigurationManager().loadBoards(this);
    }

    public String getServerFromSign(Block block) {
        if (block.hasMetadata("MinigameBoardSign")) {
            return block.getMetadata("MinigameBoardSign").get(0).asString();
        }
        return null;
    }

    @Override
    public void run() {
        if (boards.isEmpty()) {
            return;
        }

        Map<String, Server> servers = plugin.getNetwork().getServers();

        for (GameBoard wall : boards.values()) {
            wall.update(servers);
        }
    }

    public boolean createGameBoard(String name, GameSignFormat format, GameLocation first, GameLocation second, boolean createConfig) {
        name = name.toLowerCase();
        if (boards.containsKey(name)) {
            return false;
        }
        GameBoard game = new GameBoard(this, name, format, first, second);
        boards.put(name, game);

        if (createConfig) {
            plugin.getConfigurationManager().createBoardConfiguration(game);
        }

        return true;
    }

    public boolean createGameBoard(String name, GameSignFormat format, GameLocation first, GameLocation second) {
        return createGameBoard(name, format, first, second, false);
    }

    public boolean createGameBoard(String name, String channel, GameSignFormat format, GameLocation first, GameLocation second, boolean createConfig) {
        name = name.toLowerCase();
        if (boards.containsKey(name)) {
            return false;
        }

        GameBoard game = new GameBoard(this, name, format, first, second);
        game.setGameChannel(channel);
        boards.put(name, game);

        if (createConfig) {
            plugin.getConfigurationManager().createBoardConfiguration(game);
        }

        return true;
    }

    public boolean createGameBoard(String wallName, String channel, GameSignFormat format, GameLocation first, GameLocation second) {
        return createGameBoard(wallName, channel, format, first, second, false);
    }

    public LilypadMinigameLobby getPlugin() {
        return plugin;
    }

    public HashMap<String, GameBoard> getBoards() {
        return boards;
    }

    public GameBoard getBoard(String board) {
        board = board.toLowerCase();
        if (boards.containsKey(board)) {
            return boards.get(board);
        }
        return null;
    }
}