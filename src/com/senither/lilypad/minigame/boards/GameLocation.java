package com.senither.lilypad.minigame.boards;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GameLocation {

    private final String world;
    private final int x, y, z;
    private float yaw = 0, pitch = 0;

    public GameLocation(Location l) {
        this.world = l.getWorld().getName();
        this.x = l.getBlockX();
        this.y = l.getBlockY();
        this.z = l.getBlockZ();
        this.yaw = l.getYaw();
        this.pitch = l.getPitch();
    }

    public GameLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GameLocation(String world, int x, int y, int z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public GameLocation(String in) {
        String[] args = in.split(",");
        world = args[0];
        x = Integer.parseInt(args[1]);
        y = Integer.parseInt(args[2]);
        z = Integer.parseInt(args[3]);
        if (args.length > 4) {
            yaw = Float.parseFloat(args[4]);
            pitch = Float.parseFloat(args[5]);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append(world).append(",").append(x).append(",").append(y).append(",").append(z).append(",").append(yaw).append(",").append(pitch)
                .toString();
    }

    public String stringify() {
        return new StringBuilder()
                .append("&8World&a: &7").append(world).append(" &9| ")
                .append("&8X&a: &7").append(x).append(" &9| ")
                .append("&8Y&a: &7").append(y).append(" &9| ")
                .append("&8Z&a: &7").append(z)
                .toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorld() {
        return world;
    }

    public Location getBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public double distance(Location l) {
        double dx = x - l.getX(), dy = y - l.getY(), dz = z - l.getZ();
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }
}