package com.senither.lilypad.minigame.listeners;

import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.boards.GameLocation;
import com.senither.lilypad.minigame.commands.SetupInstance;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

    private final LilypadMinigameLobby plugin;

    public PlayerListener(LilypadMinigameLobby plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            if (block != null && !e.getPlayer().isSneaking() && block.getState() instanceof Sign) {
                String server = plugin.getBoardManager().getServerFromSign(block);
                if (server != null) {
                    plugin.getNetwork().teleportRequest(e.getPlayer().getName(), server);
                }
            }

            ItemStack item = e.getItem();
            if (item == null) {
                return;
            }

            if (block == null) {
                return;
            }

            if (!isWallSelector(item)) {
                return;
            }

            SetupInstance si = plugin.getCommand().getSetupInstance(e.getPlayer().getName());
            if (si == null) {
                return;
            }

            if (!si.getStage().equals(SetupInstance.SetupStage.BOARD)) {
                return;
            }

            GameLocation gameLoc = new GameLocation(block.getLocation());

            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                si.setFirstLocation(gameLoc);
                Envoyer.sendMessage(e.getPlayer(), "&8[&a+&8] &aThe first location has now been set to:");
            } else {
                si.setSecondLocation(gameLoc);
                Envoyer.sendMessage(e.getPlayer(), "&8[&a+&8] &aThe second location has now been set to:");
            }
            Envoyer.sendMessage(e.getPlayer(), "&8[&a+&8] &a" + gameLoc.stringify());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop() != null && isWallSelector(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    private boolean isWallSelector(ItemStack item) {
        if (!item.getType().equals(Material.STICK)) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName() && meta.hasLore()) {
            return (meta.getDisplayName().equals(plugin.getCommand().getSelector().getItemMeta().getDisplayName()));
        }
        return false;
    }
}
