package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AuthorCommand extends AbstractCommand {

    AuthorCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("author", "creator", "plugin", "version");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Envoyer.sendMessage(player, String.format(
                "&bLilypad Minigame Lobby &3v&b%s &3was developed by &bSenither",
                command.getPlugin().getDescription().getVersion()
        ));
        Envoyer.sendMessage(player, "&3Plugin: &bhttps://github.com/Senither/Lilypad-Minigame-Lobby");
        return true;
    }
}
