package com.senither.lilypad.minigame.listeners;

import com.senither.lilypad.minigame.Constants;
import com.senither.lilypad.minigame.LilypadMinigameLobby;
import com.senither.lilypad.minigame.boards.GameLocation;
import com.senither.lilypad.minigame.commands.setup.SetupInstance;
import com.senither.lilypad.minigame.commands.setup.SetupStage;
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block != null && !event.getPlayer().isSneaking() && block.getState() instanceof Sign) {
                String server = plugin.getBoardManager().getServerFromSign(block);
                if (server != null) {
                    plugin.getNetwork().teleportRequest(event.getPlayer().getName(), server);
                }
            }

            SetupInstance setupInstance = getSetupInstanceIfEventIsValid(event);
            if (setupInstance == null) {
                return;
            }

            GameLocation gameLoc = new GameLocation(block.getLocation());

            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                setupInstance.setFirstLocation(gameLoc);
                Envoyer.sendMessage(event.getPlayer(), "&8[&a+&8] &aThe first location has now been set to:");
            } else {
                setupInstance.setSecondLocation(gameLoc);
                Envoyer.sendMessage(event.getPlayer(), "&8[&a+&8] &aThe second location has now been set to:");
            }
            Envoyer.sendMessage(event.getPlayer(), "&8[&a+&8] &a" + gameLoc.stringify());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop() != null && isWallSelector(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
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

        return meta.hasDisplayName() && meta.hasLore() &&
                (meta.getDisplayName().equals(Constants.WALL_SELECTOR.getItemMeta().getDisplayName()));
    }

    private SetupInstance getSetupInstanceIfEventIsValid(PlayerInteractEvent event) {
        if (event.getItem() == null || !isWallSelector(event.getItem()) || event.getClickedBlock() == null) {
            return null;
        }

        SetupInstance setup = plugin.getCommand().getSetupInstance(event.getPlayer().getName());
        return setup != null && setup.getStage().equals(SetupStage.BOARD) ? setup : null;
    }
}
