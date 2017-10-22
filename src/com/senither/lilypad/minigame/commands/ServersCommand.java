package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.network.Server;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.*;

public class ServersCommand extends AbstractCommand {

    ServersCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("servers", "server", "list");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLilypad Minigame Servers");
        Envoyer.sendMessage(player, " &7Statuses&8: &aAvailable&7, &eOnline but Hidden&7, &cOffline");

        HashMap<String, List<String>> servers = new HashMap<>();

        for (Server server : command.getPlugin().getNetwork().getServers().values()) {
            String channel = server.getGameChannel().toUpperCase().substring(0, 1)
                    + server.getGameChannel().toLowerCase().substring(1, server.getGameChannel().length());

            if (!servers.containsKey(channel)) {
                servers.put(channel, new ArrayList<>());
            }

            char color = (!server.isOnline()) ? 'c' : ((server.isDisplay()) ? 'a' : 'e');

            servers.get(channel).add("&" + color + server.getName());
        }

        List<String> serversKey = new ArrayList<>(servers.keySet());
        Collections.sort(serversKey);

        return printItems(player, servers, serversKey);
    }
}
