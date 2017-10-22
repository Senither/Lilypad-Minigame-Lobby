package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.boards.GameSignFormat;
import com.senither.lilypad.minigame.boards.GameWall;
import com.senither.lilypad.minigame.network.Server;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LilypadMinigameCommand implements CommandExecutor {

    private final LilypadMinigameLobby plugin;

    private final HashMap<String, SetupInstance> setupPlayers = new HashMap<>();
    private final HashMap<String, Long> timedcheck = new HashMap<>();
    private final ItemStack selector;

    public LilypadMinigameCommand(LilypadMinigameLobby plugin) {
        this.plugin = plugin;

        this.selector = new ItemStack(Material.STICK, 1);
        ItemMeta meta = this.selector.getItemMeta();

        meta.setDisplayName(Envoyer.colorize("&a&lWall selector"));
        meta.setLore(Envoyer.colorize(Arrays.asList(
                "&7You can set the game walls",
                "&7location by using &bleft click",
                "&&7to set the first location,",
                "&7and &bright click &7to set the",
                "&7second location.",
                "",
                "&7Once you're done, type",
                "&a/lilyserverlobby next",
                "&7in the chat, and we'll start on",
                "&7the next stage of the setup process"
        )));

        this.selector.setItemMeta(meta);
    }

    public ItemStack getSelector() {
        return selector;
    }

    public SetupInstance getSetupInstance(String player) {
        if (setupPlayers.containsKey(player)) {
            return setupPlayers.get(player);
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't use this command on the console!");
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("lilypadminigame.use")) {
            Envoyer.missingPermission(player, "lilypadminigame.use");
            return false;
        }

        if (args.length == 0) {
            return helpMenu(player);
        }

        switch (args[0].toLowerCase()) {
            case "setup":
            case "create":
                return setupCommand(player);

            case "cancel":
                return cancelCommand(player);

            case "set":
                return setValueCommand(player, args);

            case "next":
                return nextCommand(player);

            case "list":
            case "server":
            case "servers":
                return serversCommand(player);

            case "wall":
            case "walls":
            case "board":
            case "boards":
                return wallsCommand(player);
        }

        GameWall board = plugin.getBoardManager().getBoard(args[0].toLowerCase());
        return board != null && wallObjectCommand(player, args, board);

    }

    private boolean setupCommand(Player player) {
        if (setupPlayers.containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're in the &aSetup &7mode!");
            Envoyer.sendMessage(player, " &7Use /LilyServerLobby cancel &7to cancel the setup mode, or");
            Envoyer.sendMessage(player, " &a/LilyLobby next &7to go to the next stage.");
            Envoyer.sendMessage(player, "");
            return true;
        }

        if (!timedcheck.containsKey(player.getName())) {
            aboutToSetupMessage(player);
            return true;
        }

        if (timedcheck.get(player.getName()) >= System.currentTimeMillis()) {
            setupPlayers.put(player.getName(), new SetupInstance(player.getName()));

            player.getInventory().clear();
            player.getInventory().setItem(4, new ItemStack(selector));

            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, "&2Stage #" + setupPlayers.get(player.getName()).getStage().getName());
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You have now entered the &asetup &amode!");
            Envoyer.sendMessage(player, " &7Use your wall selector to create the sign wall.");
            Envoyer.sendMessage(player, " &7When you're done, use &a/LilyServerLobby next");
            Envoyer.sendMessage(player, " &7to go to the next stage.");
            Envoyer.sendMessage(player, "");
            return true;
        }

        aboutToSetupMessage(player);
        return true;
    }

    private void aboutToSetupMessage(Player player) {
        timedcheck.put(player.getName(), System.currentTimeMillis() + 8 * 1000);
        Envoyer.sendMessage(player, "&7");
        Envoyer.sendMessage(player, "&7 "
                + "You're about to enter the &aGame Wall Setup Mode&7! This will clear your inventory and "
                + "give you the wall selector power tool, once you enter the setup mode you'll have to go "
                + "through three stages.");
        Envoyer.sendMessage(player, " &a > &2&l1&a: &7Board selection, creating the sign board.");
        Envoyer.sendMessage(player, " &a > &2&l2&a: &7Channeling, setting up the board channel listener.");
        Envoyer.sendMessage(player, " &a > &2&l3&a: &7Naming, giving the board a name.");
        Envoyer.sendMessage(player, "&7 More information will be provided for each stage once you get "
                + "there; if you're sure you want to continue, use &b/LilyLobby Setup&7, you can cancel "
                + "the setup process at anytime using &a/LilyLobby Cancel");
        Envoyer.sendMessage(player, "&7");
    }

    private boolean helpMenu(Player player) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLilypad Minigame Lobby");

        // Setup
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bsetup&9>");
        Envoyer.sendMessage(player, " &b&l- &7Enables setup mode, allowing you to create game-walls.");

        // List
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bservers&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the servers connected to the network.");

        // Walls
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9<&bwalls&9>");
        Envoyer.sendMessage(player, " &b&l- &7Displays all the game walls registered.");

        // Wall Editor
        Envoyer.sendMessage(player, " &8/&bLilypadMinigame &9[&bwall name&9] &9[&boption&9]");
        Envoyer.sendMessage(player, " &b&l- &7Allows you to edit a wall/board.");

        return true;
    }

    private boolean serversCommand(Player player) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLilypad Minigame Servers");
        Envoyer.sendMessage(player, " &7Statuses&8: &aAvailable&7, &eOnline but Hidden&7, &cOffline");

        HashMap<String, List<String>> servers = new HashMap<>();

        for (Server server : plugin.getNetwork().getServers().values()) {
            String channel = server.getGameChannel().toUpperCase().substring(0, 1)
                    + server.getGameChannel().toLowerCase().substring(1, server.getGameChannel().length());

            if (!servers.containsKey(channel)) {
                servers.put(channel, new ArrayList<String>());
            }

            char color = (!server.isOnline()) ? 'c' : ((server.isDisplay()) ? 'a' : 'e');

            servers.get(channel).add("&" + color + server.getName());
        }

        List<String> serversKey = new ArrayList<>(servers.keySet());
        Collections.sort(serversKey);

        for (String channel : serversKey) {
            List<String> list = servers.get(channel);

            StringBuilder text = new StringBuilder();
            for (String server : list) {
                text.append(server).append("&7, ");
            }

            if (text.length() != 0) {
                text = new StringBuilder(text.substring(0, text.length() - 4));
            }

            Envoyer.sendMessage(player, "&8[&b" + channel + "&8&m]--&8[&b" + list.size() + "&8]&b: " + text);
        }

        return true;
    }

    private boolean cancelCommand(Player player) {
        if (!setupPlayers.containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        setupPlayers.remove(player.getName());

        Envoyer.sendMessage(player, "");
        Envoyer.sendMessage(player, " &7You have &ccanceled &7your game wall setup!");
        Envoyer.sendMessage(player, "");

        player.getInventory().clear();
        return true;
    }

    private boolean setValueCommand(Player player, String[] args) {
        if (!setupPlayers.containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        if (args.length < 2) {
            Envoyer.sendMessage(player, "&c Invalid data set format!");
            Envoyer.sendMessage(player, "&c /LilyLobby set &4<&cdata&4>");
            return false;
        }

        SetupInstance instance = setupPlayers.get(player.getName());

        Envoyer.sendMessage(player, "");
        switch (instance.getStage()) {
            case CHANNEL:
                instance.setGameChannel(args[0].toLowerCase());
                Envoyer.sendMessage(player, " &7The game wall will nost listen for servers on the channel &a" + args[1] + "!");
                break;
            case NAME:
                instance.setName(args[1]);

                if (plugin.getBoardManager().getBoard(args[1]) == null) {
                    Envoyer.sendMessage(player, " &7The wall will now store data under the name &a" + args[1] + "&7!");
                } else {
                    Envoyer.sendMessage(player, " &4" + args[1] + " &cis already taken, please use another name!");
                }
                break;
            default:
                Envoyer.sendMessage(player, "&c You can't use this command in this setup stage!");
                break;
        }
        Envoyer.sendMessage(player, "");

        return true;
    }

    private boolean nextCommand(Player player) {
        if (!setupPlayers.containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're not in the &aSetup mode&7!");
            Envoyer.sendMessage(player, "");
            return false;
        }

        SetupInstance instance = setupPlayers.get(player.getName());
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

        SetupInstance.SetupStage stage = instance.getStage().next(instance.getStage());
        if (stage == null) {
            boolean created = plugin.getBoardManager().createGameWall(
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
                setupPlayers.remove(player.getName());
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

    private boolean wallsCommand(Player player) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLily Minigame Walls/Boards");

        HashMap<String, List<String>> walls = new HashMap<>();

        for (GameWall wall : plugin.getBoardManager().getBoards().values()) {
            String channel = wall.getGameChannel().toUpperCase().substring(0, 1)
                    + wall.getGameChannel().toLowerCase().substring(1, wall.getGameChannel().length());

            if (!walls.containsKey(channel)) {
                walls.put(channel, new ArrayList<String>());
            }

            walls.get(channel).add("&7" + wall.getWallName() + " &9(&b" + wall.getSigns().size() + "&9)");
        }

        List<String> wallsKey = new ArrayList<>(walls.keySet());
        Collections.sort(wallsKey);

        for (String channel : wallsKey) {
            List<String> list = walls.get(channel);

            String text = "";
            for (String wall : list) {
                text += wall + "&7, ";
            }

            if (text.length() != 0) {
                text = text.substring(0, text.length() - 4);
            }

            Envoyer.sendMessage(player, "&8[&b" + channel + "&8&m]--&8[&b" + list.size() + "&8]&b: " + text);
        }

        return true;
    }

    private boolean wallObjectCommand(Player player, String[] args, GameWall board) {
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
            plugin.getConfigurationManager().createBoardConfiguration(board);
            Envoyer.sendMessage(player, " &b" + board.getWallName() + " &7is now listening on the &b" + args[2] + "&7 channel!");
            return true;
        } else if (args[1].equalsIgnoreCase("format")) {
            if (args.length <= 3) {
                Envoyer.sendMessage(player, " &cMissing arguments for the given command!");
                Envoyer.sendMessage(player, " &4/LilyLobby &4<&c" + board.getWallName() + "&4> &4<&cformat&4> &4[&clineNumber&4] &4[&cformatText&4]");
                return false;
            }

            int index = -1;

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
            plugin.getConfigurationManager().createBoardConfiguration(board);
            Envoyer.sendMessage(player, " &b" + board.getWallName() + " &7will now render line &b" + index + "&7 using the following format&b:");
            Envoyer.sendMessage(player, " &b&l - &0" + message);
            return true;
        } else {
            Envoyer.sendMessage(player, " &cInvalid argument given for the board commands.");
            return false;
        }
    }
}
