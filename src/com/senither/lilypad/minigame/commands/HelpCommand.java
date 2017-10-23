package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(LilypadMinigameCommand command) {
        super(command, true);
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("help");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLilypad Minigame Lobby");

        // Setup
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bsetup&9>");
        Envoyer.sendMessage(player, " &b&l- &7Enables setup mode, allowing you to create game boards.");

        // List
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bservers&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the servers connected to the network.");

        // Walls
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bboards&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the game boards registered.");

        // Wall Editor
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9[&bboard name&9] &9[&boption&9]");
        Envoyer.sendMessage(player, " &b&l- &7Allows you to edit a game board.");

        return true;
    }
}
