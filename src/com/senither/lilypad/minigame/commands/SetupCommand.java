package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.Constants;
import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.contracts.commands.AbstractCommand;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SetupCommand extends AbstractCommand {

    SetupCommand(LilypadMinigameCommand command) {
        super(command);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("setup", "create");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (command.getSetupPlayers().containsKey(player.getName())) {
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You're in the &aSetup &7mode!");
            Envoyer.sendMessage(player, " &7Use /LilypadMinigame cancel &7to cancel the setup mode, or");
            Envoyer.sendMessage(player, " &a/LilypadMinigame next &7to go to the next stage.");
            Envoyer.sendMessage(player, "");
            return true;
        }

        if (!command.getTimedcheck().containsKey(player.getName())) {
            aboutToSetupMessage(player);
            return true;
        }

        if (command.getTimedcheck().get(player.getName()) >= System.currentTimeMillis()) {
            command.getSetupPlayers().put(player.getName(), new SetupInstance(player.getName()));

            player.getInventory().clear();
            player.getInventory().setItem(4, new ItemStack(Constants.WALL_SELECTOR));

            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, "&2Stage #" + command.getSetupPlayers().get(player.getName()).getStage().getName());
            Envoyer.sendMessage(player, "");
            Envoyer.sendMessage(player, " &7You have now entered the &asetup &amode!");
            Envoyer.sendMessage(player, " &7Use your wall selector to create the sign wall.");
            Envoyer.sendMessage(player, " &7When you're done, use &a/LilypadMinigame next");
            Envoyer.sendMessage(player, " &7to go to the next stage.");
            Envoyer.sendMessage(player, "");
            return true;
        }

        aboutToSetupMessage(player);
        return true;
    }

    private void aboutToSetupMessage(Player player) {
        command.getTimedcheck().put(player.getName(), System.currentTimeMillis() + 8 * 1000);
        Envoyer.sendMessage(player, "&7");
        Envoyer.sendMessage(player, "&7 "
                + "You're about to enter the &aGame Wall Setup Mode&7! This will clear your inventory and "
                + "give you the wall selector power tool, once you enter the setup mode you'll have to go "
                + "through three stages.");
        Envoyer.sendMessage(player, " &a > &2&l1&a: &7Board selection, creating the sign board.");
        Envoyer.sendMessage(player, " &a > &2&l2&a: &7Channeling, setting up the board channel listener.");
        Envoyer.sendMessage(player, " &a > &2&l3&a: &7Naming, giving the board a name.");
        Envoyer.sendMessage(player, "&7 More information will be provided for each stage once you get "
                + "there; if you're sure you want to continue, use &b/LilypadMinigame Setup&7, you can cancel "
                + "the setup process at anytime using &a/LilypadMinigame Cancel");
        Envoyer.sendMessage(player, "&7");
    }
}
