package com.senither.lilypad.minigame;

import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Constants {

    public static final ItemStack WALL_SELECTOR;
    public static final String NETWORK_CHANNEL = "Lilypad-Minigame";

    public static final String SIGN_META_ONLINE = "MinigameBoardSign";
    public static final String SIGN_META_OFFLINE = "MinigameBoardSignOffline";

    static {
        WALL_SELECTOR = new ItemStack(Material.STICK, 1);
        ItemMeta meta = WALL_SELECTOR.getItemMeta();

        meta.setDisplayName(Envoyer.colorize("&a&lWall selector"));
        meta.setLore(Envoyer.colorize(Arrays.asList(
                "&7You can set the game walls",
                "&7location by using &bleft click",
                "&&7to set the first location,",
                "&7and &bright click &7to set the",
                "&7second location.",
                "",
                "&7Once you're done, type",
                "&a/LilypadMinigame next",
                "&7in the chat, and we'll start on",
                "&7the next stage of the setup process"
        )));

        WALL_SELECTOR.setItemMeta(meta);
    }
}
