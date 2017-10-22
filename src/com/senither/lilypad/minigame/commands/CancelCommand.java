package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CancelCommand extends AbstractCommand {

    CancelCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("cancel");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!command.getSetupPlayers().containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        command.getSetupPlayers().remove(player.getName());

        Envoyer.sendMessage(player, "");
        Envoyer.sendMessage(player, " &7You have &ccanceled &7your game wall setup!");
        Envoyer.sendMessage(player, "");

        player.getInventory().clear();
        return true;
    }
}
