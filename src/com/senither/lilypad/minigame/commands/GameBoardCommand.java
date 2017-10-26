package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameBoard;
import com.senither.lilypad.minigame.config.Configuration;
import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.List;

public class GameBoardCommand extends AbstractCommand {

    GameBoardCommand(LilypadMinigameCommand command) {
        super(command, false, true);
    }

    @Override
    public List<String> getTriggers() {
        return null;
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        GameBoard board = command.getPlugin().getBoardManager().getBoard(args[0].toLowerCase());
        if (board == null) {
            Envoyer.sendMessage(player, String.format("&4[&cError&4] &cInvalid board given, &4%s &cis not a valid game board.", args[0]));
            return false;
        }

        if (args.length <= 1) {
            return displayHelp(player, board);
        }

        switch (args[1].toLowerCase()) {
            case "dump":
                return displayDump(player, board);

            case "flip":
                return flip(player, board);

            case "channel":
                return channel(player, board, args);

            case "format":
                return format(player, board, args);

            default:
                Envoyer.sendMessage(player, " &cInvalid argument given for the board commands.");
        }

        return false;
    }

    private boolean displayHelp(Player player, GameBoard board) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLilypad Minigame Board &9#&3" + board.getName());

        // Channel
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&b" + board.getName() + "&9> &9<&bchannel&9> &9[&bchannel name&9]");
        Envoyer.sendMessage(player, " &b&l- &7Sets a new channel name for the board");

        // Format
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&b" + board.getName() + "&9> &9<&bformat&9> &9[&bline number&9] &9[&bformat text&9]");
        Envoyer.sendMessage(player, " &b&l- &7Sets a new channel name for the board");

        // Flip
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&b" + board.getName() + "&9> &9<&bflip&9>");
        Envoyer.sendMessage(player, " &b&l- &7Flips the direction the signs are rendered in (left to right, or right to left)");

        // Dump
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&b" + board.getName() + "&9> &9<&bdump&9>");
        Envoyer.sendMessage(player, " &b&l- &7Dumps the board objects information");

        return true;
    }

    private boolean displayDump(Player player, GameBoard board) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&Lilypad Minigame Board Dump for &9#&3" + board.getName());
        Envoyer.sendMessage(player, " &9&l- &7Channel&9: &b" + board.getGameChannel());
        Envoyer.sendMessage(player, " &9&l- &7Format&9:");

        int lineIndex = 1;
        for (String line : board.getFormat().getFormat()) {
            Envoyer.sendMessage(player, " &b &r &b &r &9- Line &9#&3" + (lineIndex++) + "&9: &0" + line);
        }
        return true;
    }

    private boolean flip(Player player, GameBoard board) {
        board.setFlip(!board.isFlip());
        board.reloadLocationObjects();

        Configuration configuration = command.getPlugin().getConfigurationManager().getConfigFromGameBoard(board);
        configuration.getConfig().set("flip", board.isFlip());
        configuration.saveConfig();

        Envoyer.sendMessage(player, " &b" + board.getName() + "'s &7sign render direction has been flipped.");

        return true;
    }

    private boolean channel(Player player, GameBoard board, String[] args) {
        if (args.length <= 2) {
            Envoyer.sendMessage(player, " &cMissing arguments for the given command!");
            Envoyer.sendMessage(player, " &4/LilypadMinigame &4<&c" + board.getName() + "&4> &4<&cchannel&4> &4[&cchannel name&4]");
            return false;
        }

        board.setGameChannel(args[2].toLowerCase());
        command.getPlugin().getConfigurationManager().createBoardConfiguration(board);
        Envoyer.sendMessage(player, " &b" + board.getName() + " &7is now listening on the &b" + args[2] + "&7 channel!");
        return true;
    }

    private boolean format(Player player, GameBoard board, String[] args) {
        if (args.length <= 3) {
            Envoyer.sendMessage(player, " &cMissing arguments for the given command!");
            Envoyer.sendMessage(player, " &4/LilypadMinigame &4<&c" + board.getName() + "&4> &4<&cformat&4> &4[&cline number&4] &4[&cformat text&4]");
            return false;
        }

        int index;
        try {
            index = Integer.parseInt(args[2]);
            if (index < 1 || index > 4) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            Envoyer.sendMessage(player, " &cInvalid number given, the line number has to be a valid number between 1 and 4!");
            Envoyer.sendMessage(player, " &4/LilypadMinigame &4<&c" + board.getName() + "&4> &4<&cformat&4> &4[&cline number&4] &4[&cformat text&4]");
            return false;
        }

        String message = "";
        for (int i = 3; i < args.length; i++) {
            message += args[i] + " ";
        }

        board.getFormat().setLine(index - 1, message.trim());
        command.getPlugin().getConfigurationManager().createBoardConfiguration(board);
        Envoyer.sendMessage(player, " &b" + board.getName() + " &7will now render line &b" + index + "&7 using the following format&b:");
        Envoyer.sendMessage(player, " &b&l - &0" + message);
        return true;
    }
}
