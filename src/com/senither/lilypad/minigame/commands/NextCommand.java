package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameSignFormat;
import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.commands.setup.SetupStage;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NextCommand extends AbstractCommand {

    NextCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("next");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!command.getSetupPlayers().containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        SetupInstance instance = command.getSetupPlayers().get(player.getName());
        if (!instance.validate()) {

            Envoyer.sendMessage(player, "");
            switch (instance.getStage()) {
                case BOARD:
                    Envoyer.sendMessage(player, "&c You must set both locations before you can proceed to the next stage!");
                    break;
                case CHANNEL:
                    Envoyer.sendMessage(player, "&c You have to set a channel for the game wall! If you don't want to use a custom game channel, just use &4GLOBAL&c!");
                    break;
                case NAME:
                    Envoyer.sendMessage(player, "&c You have to give the game wall a name so that it can be stored and loaded into/from a config, it will also make it a lot easier later on to find, edit or delete.");
            }
            Envoyer.sendMessage(player, "");

            return false;
        }

        SetupStage stage = instance.getStage().next(instance.getStage());
        if (stage == null) {
            boolean created = command.getPlugin().getBoardManager().createGameWall(
                    instance.getName(),
                    new GameSignFormat(
                            Arrays.asList(
                                    "&1[&9{server}&1]",
                                    "{playersOnline} / {playersMax}",
                                    "",
                                    "&oClick to Join"
                            )
                    ),
                    instance.getFirstLocation(),
                    instance.getSecondLocation(),
                    true
            );

            if (created) {
                player.getInventory().clear();
                command.getSetupPlayers().remove(player.getName());
                Envoyer.sendMessage(player, "");
                Envoyer.sendMessage(player, " &7You're now &adone!");
                Envoyer.sendMessage(player, " &7The wall has been setup successfully under the name &a" + instance.getName() + "!");
                Envoyer.sendMessage(player, "");
                return true;
            }

            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &cThe name seems to already be taken!");
            Envoyer.sendMessage(player, " &cSomeone must've taken the name while you were creating your game board, please select a new name instead of &4" + instance.getName() + "&c!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        instance.setStage(stage);

        Envoyer.sendMessage(player, "");
        Envoyer.sendMessage(player, "&2Stage &a#" + stage.getName());
        Envoyer.sendMessage(player, "");

        switch (stage) {
            case CHANNEL:
                Envoyer.sendMessage(player, " &7Setup the channel the wall should be listening on, by default the wall will listen on th &bglobal &7channel, allowing all servers to be displayed on it; If you want to set custom channel, use the following format: &a/LilyLobby set &2[&achannelName&2]");
                break;
            case NAME:
                Envoyer.sendMessage(player, " &7Great! You're almost done, all you need now is to give the wall a name, so we can store and fetch data from/to it. To give the wall a name, simply use the following syntax: &a/LilyLobby set &2[&awallName&2]");
                break;
        }

        Envoyer.sendMessage(player, " &7When you're done, use &a/LilyLobby next");
        Envoyer.sendMessage(player, " &7to go to the next stage.");
        Envoyer.sendMessage(player, "");

        return true;
    }
}
