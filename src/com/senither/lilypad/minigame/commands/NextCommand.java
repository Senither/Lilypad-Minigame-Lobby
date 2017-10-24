package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameSignFormat;
import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.commands.setup.SetupStage;
import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
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
            return sendInvalidStageMessage(player, instance);
        }

        SetupStage stage = instance.getStage().next(instance.getStage());
        if (stage == null) {
            return createBoardFromInstance(player, instance);
        }

        instance.setStage(stage);

        Envoyer.sendMessage(player, "");
        Envoyer.sendMessage(player, "&2Stage &a#" + stage.getName());
        Envoyer.sendMessage(player, "");

        switch (stage) {
            case CHANNEL:
                Envoyer.sendMessage(player, " &7Setup the channel the board should be listening on, by default the board will listen on th &bglobal &7channel, allowing all servers to be displayed on it; If you want to set custom channel, use the following format: &a/LilypadMinigame set &2[&achannel name&2]");
                break;
            case NAME:
                Envoyer.sendMessage(player, " &7Great! You're almost done, all you need now is to give the board a name, so we can store and fetch data from/to it. To give the board a name, simply use the following syntax: &a/LilypadMinigame set &2[&aboard name&2]");
                break;
        }

        Envoyer.sendMessage(player, " &7When you're done, use &a/LilypadMinigame next");
        Envoyer.sendMessage(player, " &7to go to the next stage.");
        Envoyer.sendMessage(player, "");

        return true;
    }

    private boolean sendInvalidStageMessage(Player player, SetupInstance instance) {
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

    private boolean createBoardFromInstance(Player player, SetupInstance instance) {
        if (command.getPlugin().getBoardManager().getBoard(instance.getName()) != null) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &cThe name seems to already be taken!");
            Envoyer.sendMessage(player, " &cSomeone must've taken the name while you were creating your game board, please select a new name instead of &4" + instance.getName() + "&c!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        player.getInventory().clear();
        command.getSetupPlayers().remove(player.getName());

        Envoyer.sendMessage(player, "");
        Envoyer.sendMessage(player, " &7You're now &adone!");
        Envoyer.sendMessage(player, " &7The board has been setup successfully under the name &a" + instance.getName() + "!");
        Envoyer.sendMessage(player, "");

        return command.getPlugin().getBoardManager().createGameBoard(
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
    }
}
