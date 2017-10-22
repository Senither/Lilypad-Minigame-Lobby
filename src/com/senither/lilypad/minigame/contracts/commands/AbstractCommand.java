package com.senither.lilypad.minigame.contracts.commands;

import com.senither.lilypad.minigame.commands.LilypadMinigameCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractCommand {

    protected final LilypadMinigameCommand command;
    protected final boolean isHelp;
    protected final boolean isFallback;

    public AbstractCommand(LilypadMinigameCommand command, boolean isHelp, boolean isFallback) {
        this.command = command;
        this.isHelp = isHelp;
        this.isFallback = isFallback;
    }

    public AbstractCommand(LilypadMinigameCommand command, boolean isHelp) {
        this(command, isHelp, false);
    }

    public AbstractCommand(LilypadMinigameCommand command) {
        this(command, false, false);
    }

    public boolean isHelpCommand() {
        return isHelp;
    }

    public boolean isFallbackCommand() {
        return isFallback;
    }

    public abstract List<String> getTriggers();

    public abstract boolean onCommand(Player player, String[] args);

    protected boolean printItems(Player player, HashMap<String, List<String>> items, List<String> keys) {
        for (String key : keys) {
            List<String> list = items.get(key);

            StringBuilder text = new StringBuilder();
            for (String wall : list) {
                text.append(wall).append("&7, ");
            }

            if (text.length() != 0) {
                text = new StringBuilder(text.substring(0, text.length() - 4));
            }

            Envoyer.sendMessage(player, "&8[&b" + key + "&8&m]--&8[&b" + list.size() + "&8]&b: " + text);
        }
        return true;
    }
}
