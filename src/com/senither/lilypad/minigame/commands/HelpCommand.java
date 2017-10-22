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
        Envoyer.sendMessage(player, " &b&l- &7Enables setup mode, allowing you to create game-walls.");

        // List
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bservers&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the servers connected to the network.");

        // Walls
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bwalls&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the game walls registered.");

        // Wall Editor
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9[&bwall name&9] &9[&boption&9]");
        Envoyer.sendMessage(player, " &b&l- &7Allows you to edit a wall/board.");

        return true;
    }
}
