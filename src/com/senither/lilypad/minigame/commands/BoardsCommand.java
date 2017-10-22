package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameWall;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;

import java.util.*;

public class BoardsCommand extends AbstractCommand {

    BoardsCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("boards", "board", "walls", "wall");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Envoyer.sendMessage(player, "&9[&b+&9&m]--&9[ &b&lLily Minigame Walls/Boards");

        HashMap<String, List<String>> walls = new HashMap<>();

        for (GameWall wall : command.getPlugin().getBoardManager().getBoards().values()) {
            String channel = wall.getGameChannel().toUpperCase().substring(0, 1)
                    + wall.getGameChannel().toLowerCase().substring(1, wall.getGameChannel().length());

            if (!walls.containsKey(channel)) {
                walls.put(channel, new ArrayList<String>());
            }

            walls.get(channel).add("&7" + wall.getWallName() + " &9(&b" + wall.getSigns().size() + "&9)");
        }

        List<String> wallKeys = new ArrayList<>(walls.keySet());
        Collections.sort(wallKeys);

        return printItems(player, walls, wallKeys);
    }
}
