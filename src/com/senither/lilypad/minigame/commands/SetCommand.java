package com.senither.lilypad.minigame.commands;

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
            Envoyer.sendMessage(player, "&c /LilyLobby set &4<&cdata&4>");
            return false;
        }

        SetupInstance instance = command.getSetupPlayers().get(player.getName());

        Envoyer.sendMessage(player, "");
        switch (instance.getStage()) {
            case CHANNEL:
                instance.setGameChannel(args[0].toLowerCase());
                Envoyer.sendMessage(player, " &7The game wall will now listen for servers on the channel &a" + args[0] + "!");
                break;
            case NAME:
                instance.setName(args[0]);

                if (command.getPlugin().getBoardManager().getBoard(args[0]) == null) {
                    Envoyer.sendMessage(player, " &7The wall will now store data under the name &a" + args[0] + "&7!");
                } else {
                    Envoyer.sendMessage(player, " &4" + args[0] + " &cis already taken, please use another name!");
                }
                break;
            default:
                Envoyer.sendMessage(player, "&c You can't use this command in this setup stage!");
                break;
        }
        Envoyer.sendMessage(player, "");

        return true;
    }
}
