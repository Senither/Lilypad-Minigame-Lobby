package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameWall;
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
        GameWall board = command.getPlugin().getBoardManager().getBoard(args[0].toLowerCase());
        if (board == null) {
            return false;
        }

        if (args.length <= 1) {
            Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLily Wall &9#&3" + board.getWallName());

            // Channel
            Envoyer.sendMessage(player, " &8/&bLilyLobby &9<&b" + board.getWallName() + "&9> &9<&bchannel&9> &9[&bnewChannelName&9]");
            Envoyer.sendMessage(player, " &b&l- &7Sets a new channel name for the board");

            // Format
            Envoyer.sendMessage(player, " &8/&bLilyLobby &9<&b" + board.getWallName() + "&9> &9<&bformat&9> &9[&blineNumber&9] &9[&bformatText&9]");
            Envoyer.sendMessage(player, " &b&l- &7Sets a new channel name for the board");

            // Dump
            Envoyer.sendMessage(player, " &8/&bLilyLobby &9<&b" + board.getWallName() + "&9> &9<&bdump&9>");
            Envoyer.sendMessage(player, " &b&l- &7Dumps the game-wall objects information");

            return true;
        }

        if (args[1].equalsIgnoreCase("dump")) {
            Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLily Wall Dump for &9#&3" + board.getWallName());
            Envoyer.sendMessage(player, " &9&l- &7Channel&9: &b" + board.getGameChannel());
            Envoyer.sendMessage(player, " &9&l- &7Format&9:");

            int lineIndex = 1;
            for (String line : board.getFormat().getFormat()) {
                Envoyer.sendMessage(player, " &b &r &b &r &9- Line &9#&3" + (lineIndex++) + "&9: &0" + line);
            }
            return true;
        } else if (args[1].equalsIgnoreCase("channel")) {
            if (args.length <= 2) {
                Envoyer.sendMessage(player, " &cMissing arguments for the given command!");
                Envoyer.sendMessage(player, " &4/LilyLobby &4<&c" + board.getWallName() + "&4> &4<&cchannel&4> &4[&cnewChannelName&4]");
                return false;
            }

            board.setGameChannel(args[2].toLowerCase());
            command.getPlugin().getConfigurationManager().createBoardConfiguration(board);
            Envoyer.sendMessage(player, " &b" + board.getWallName() + " &7is now listening on the &b" + args[2] + "&7 channel!");
            return true;
        } else if (args[1].equalsIgnoreCase("format")) {
            if (args.length <= 3) {
                Envoyer.sendMessage(player, " &cMissing arguments for the given command!");
                Envoyer.sendMessage(player, " &4/LilyLobby &4<&c" + board.getWallName() + "&4> &4<&cformat&4> &4[&clineNumber&4] &4[&cformatText&4]");
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
                Envoyer.sendMessage(player, " &4/LilyLobby &4<&c" + board.getWallName() + "&4> &4<&cformat&4> &4[&clineNumber&4] &4[&cformatText&4]");
                return false;
            }

            String message = "";
            for (int i = 3; i < args.length; i++) {
                message += args[i] + " ";
            }

            board.getFormat().setLine(index - 1, message.trim());
            command.getPlugin().getConfigurationManager().createBoardConfiguration(board);
            Envoyer.sendMessage(player, " &b" + board.getWallName() + " &7will now render line &b" + index + "&7 using the following format&b:");
            Envoyer.sendMessage(player, " &b&l - &0" + message);
            return true;
        }

        Envoyer.sendMessage(player, " &cInvalid argument given for the board commands.");
        return false;
    }
}
