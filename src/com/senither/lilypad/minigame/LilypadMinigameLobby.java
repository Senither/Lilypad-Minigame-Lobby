package com.senither.lilypad.minigame;

import com.senither.lilypad.minigame.boards.BoardManager;
import com.senither.lilypad.minigame.commands.LilypadMinigameCommand;
import com.senither.lilypad.minigame.config.ConfigurationManager;
import com.senither.lilypad.minigame.listeners.BlockListener;
import com.senither.lilypad.minigame.listeners.PlayerListener;
import com.senither.lilypad.minigame.network.NetworkManager;
import com.senither.lilypad.minigame.utils.Envoyer;
import lilypad.client.connect.api.Connect;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class LilypadMinigameLobby extends JavaPlugin {

    private Connect connect;
    private NetworkManager network;
    private BoardManager boardManager;
    private ConfigurationManager config;
    private LilypadMinigameCommand command;

    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("LilyPad-Connect");
        if (plugin == null) {
            setEnabled(false);
            Envoyer.getLogger().log(Level.SEVERE, "LilypadMinigameLobby was shut down since LilyPad-Connect was not found!");
            return;
        }

        config = new ConfigurationManager(this);
        connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.registerEvents(network = new NetworkManager(this));
        boardManager = new BoardManager(this);

        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("lilypadminigame").setExecutor(command = new LilypadMinigameCommand(this));
    }

    @Override
    public void onDisable() {
        connect.unregisterEvents(network);
    }

    public Connect getConnect() {
        return connect;
    }

    public NetworkManager getNetwork() {
        return network;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return config;
    }

    public LilypadMinigameCommand getCommand() {
        return command;
    }
}
