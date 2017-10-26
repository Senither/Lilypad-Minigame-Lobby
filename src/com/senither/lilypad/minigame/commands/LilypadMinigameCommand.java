package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LilypadMinigameCommand implements CommandExecutor {

    private final LilypadMinigameLobby plugin;

    private final List<AbstractCommand> commands;
    private final AuthorCommand authorCommand;

    private final HashMap<String, SetupInstance> setupPlayers = new HashMap<>();
    private final HashMap<String, Long> timedcheck = new HashMap<>();

    public LilypadMinigameCommand(LilypadMinigameLobby plugin) {
        this.plugin = plugin;

        this.commands = Arrays.asList(
                new HelpCommand(this),
                new SetupCommand(this),
                new CancelCommand(this),
                new SetCommand(this),
                new BoardsCommand(this),
                new NextCommand(this),
                new ServersCommand(this),
                new GameBoardCommand(this)
        );
        this.authorCommand = new AuthorCommand(this);
    }

    public SetupInstance getSetupInstance(String player) {
        if (setupPlayers.containsKey(player)) {
            return setupPlayers.get(player);
        }
        return null;
    }

    HashMap<String, Long> getTimedcheck() {
        return timedcheck;
    }

    LilypadMinigameLobby getPlugin() {
        return plugin;
    }

    HashMap<String, SetupInstance> getSetupPlayers() {
        return setupPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't use this command on the console!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length > 0 && authorCommand.getTriggers().contains(args[0].toLowerCase())) {
            return authorCommand.onCommand(player, args);
        }

        if (!player.hasPermission("lilypadminigame.use")) {
            Envoyer.missingPermission(player, "lilypadminigame.use");
            return false;
        }

        if (args.length == 0) {
            for (AbstractCommand command : commands) {
                if (command.isHelpCommand()) {
                    return command.onCommand(player, args);
                }
            }
        }

        for (AbstractCommand command : commands) {
            if (command.getTriggers() != null && command.getTriggers().contains(args[0].toLowerCase())) {
                return command.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        for (AbstractCommand command : commands) {
            if (command.isFallbackCommand() && args.length > 0) {
                return command.onCommand(player, args);
            }
        }


        return false;
    }
}
