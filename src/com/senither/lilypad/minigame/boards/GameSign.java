package com.senither.lilypad.minigame.boards;

import org.bukkit.Location;

public class GameSign {

    private final GameLocation location;

    public GameSign(String world, int x, int y, int z) {
        location = new GameLocation(world, x, y, z);
    }

    public GameSign(Location location) {
        this.location = new GameLocation(location);
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location.getBukkitLocation();
    }

    @Override
    public String toString() {
        return location.getWorld() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }
}