package com.senither.lilypad.minigame;

import com.senither.lilypad.minigame.network.NetworkManager;
import com.senither.lilypad.minigame.utils.Envoyer;
import lilypad.client.connect.api.Connect;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class LilypadMinigameLobby extends JavaPlugin {

    private Connect connect;
    private NetworkManager network;

    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("LilyPad-Connect");
        if (plugin == null) {
            setEnabled(false);
            Envoyer.getLogger().log(Level.SEVERE, "LilypadMinigameLobby was shut down since LilyPad-Connect was not found!");
            return;
        }

        connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.registerEvents(network = new NetworkManager(this));
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
}
