package com.senither.lilypad.minigame.listeners;

import com.senither.lilypad.minigame.Constants;
import com.senither.lilypad.minigame.LilypadMinigameLobby;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

    private final LilypadMinigameLobby plugin;

    public BlockListener(LilypadMinigameLobby plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) {
            return;
        }

        if (plugin.getCommand().getSetupInstance(event.getPlayer().getName()) != null) {
            event.setCancelled(true);
            return;
        }

        Sign sign = (Sign) event.getBlock().getState();
        if (sign.hasMetadata(Constants.SIGN_META_OFFLINE) || sign.hasMetadata(Constants.SIGN_META_ONLINE)) {
            if (!(event.getPlayer().isOp() && event.getPlayer().isSneaking())) {
                event.setCancelled(true);
            }
        }
    }
}
