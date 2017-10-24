package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetCommand extends AbstractCommand {

    SetCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("set");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!command.getSetupPlayers().containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        if (args.length < 1) {
            Envoyer.sendMessage(player, "&c Invalid data set format!");
            Envoyer.sendMessage(player, "&c /LilypadMinigame set &4<&cdata&4>");
            return false;
        }

        SetupInstance instance = command.getSetupPlayers().get(player.getName());

        Envoyer.sendMessage(player, "");
        switch (instance.getStage()) {
            case CHANNEL:
                instance.setGameChannel(args[0].toLowerCase());
                Envoyer.sendMessage(player, " &7The game board will now listen for servers on the channel &a" + args[0] + "!");
                break;

            case NAME:
                Envoyer.sendMessage(player, getNameStatusMessage(instance, args[0]));
                break;

            default:
                Envoyer.sendMessage(player, "&c You can't use this command in this setup stage!");
                break;
        }
        Envoyer.sendMessage(player, "");

        return true;
    }

    private String getNameStatusMessage(SetupInstance instance, String boardName) {
        if (command.getPlugin().getBoardManager().getBoard(boardName) == null) {
            instance.setName(boardName);
            return " &7The board will now store data under the name &a" + boardName + "&7!";
        }
        return " &4" + boardName + " &cis already taken, please use another name!";
    }
}
