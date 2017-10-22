package com.senither.lilypad.minigame.boards;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.network.Server;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BoardManager implements Runnable {

    private final LilypadMinigameLobby plugin;
    private final HashMap<String, GameWall> walls = new HashMap<>();

    public BoardManager(LilypadMinigameLobby plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 60, 20);
        this.plugin.getConfigurationManager().loadBoards(this);
    }

    public String getServerFromSign(Block block) {
        if (block.hasMetadata("LilyServerName")) {
            return block.getMetadata("LilyServerName").get(0).asString();
        }
        return null;
    }

    @Override
    public void run() {
        if (walls.isEmpty()) {
            return;
        }

        Map<String, Server> servers = plugin.getNetwork().getServers();

        for (GameWall wall : walls.values()) {
            wall.update(servers);
        }
    }

    public boolean createGameWall(String wallName, GameSignFormat format, GameLocation first, GameLocation second, boolean createConfig) {
        wallName = wallName.toLowerCase();
        if (walls.containsKey(wallName)) {
            return false;
        }
        GameWall game = new GameWall(this, wallName, format, first, second);
        walls.put(wallName, game);

        if (createConfig) {
            plugin.getConfigurationManager().createBoardConfiguration(game);
        }

        return true;
    }

    public boolean createGameWall(String wallName, GameSignFormat format, GameLocation first, GameLocation second) {
        return createGameWall(wallName, format, first, second, false);
    }

    public boolean createGameWall(String wallName, String channel, GameSignFormat format, GameLocation first, GameLocation second, boolean createConfig) {
        wallName = wallName.toLowerCase();
        if (walls.containsKey(wallName)) {
            return false;
        }
        GameWall game = new GameWall(this, wallName, format, first, second);
        game.setGameChannel(channel);
        walls.put(wallName, game);

        if (createConfig) {
            plugin.getConfigurationManager().createBoardConfiguration(game);
        }

        return true;
    }

    public boolean createGameWall(String wallName, String channel, GameSignFormat format, GameLocation first, GameLocation second) {
        return createGameWall(wallName, channel, format, first, second, false);
    }

    public LilypadMinigameLobby getPlugin() {
        return plugin;
    }

    public HashMap<String, GameWall> getBoards() {
        return walls;
    }

    public GameWall getBoard(String board) {
        board = board.toLowerCase();
        if (walls.containsKey(board)) {
            return walls.get(board);
        }
        return null;
    }
}