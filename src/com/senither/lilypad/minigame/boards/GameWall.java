package com.senither.lilypad.minigame.boards;

import com.senither.lilypad.minigame.network.Server;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class GameWall {

    private final BoardManager bm;

    private final String name;
    private final GameSignFormat format;
    private final List<GameSign> signs = new ArrayList<>();
    private final GameLocation firstLocation;
    private final GameLocation secondLocation;
    private String channel = "GLOBAL";

    public GameWall(BoardManager bm, String wallName, GameSignFormat format, GameLocation first, GameLocation second) {
        this.bm = bm;

        this.name = wallName;
        this.format = format;

        this.firstLocation = first;
        this.secondLocation = second;

        this.buildLocationsObject(first, second);
    }

    public void update(Map<String, Server> servers) {
        List<Server> list = new ArrayList<>();

        for (Server server : servers.values()) {
            if (!server.isOnline()) {
                continue;
            }

            if (getGameChannel().equalsIgnoreCase("GLOBAL")) {
                list.add(server);
                continue;
            }

            if (server.getGameChannel().equalsIgnoreCase(getGameChannel())) {
                list.add(server);
            }
        }

        Collections.sort(list);

        for (GameSign gameSign : signs) {
            Location location = gameSign.getLocation();
            Block block = location.getBlock();

            if (block.getState() instanceof Sign) {
                Sign sign = (Sign) block.getState();

                if (sign == null) {
                    continue;
                }

                Server server = null;

                if (!list.isEmpty()) {
                    server = list.remove(0);
                    List<String> signText = format.buildFrom(server.getName(), server.getPlaceholders());

                    sign.setMetadata("LilyServerName", new FixedMetadataValue(bm.getPlugin(), server.getName()));
                    writeToSign(sign, signText);
                } else {
                    if (sign.hasMetadata("LilyServerName")) {
                        sign.removeMetadata("LilyServerName", bm.getPlugin());
                    }
                    writeToSign(sign, "", "&cThe server is", "&cOFFLINE", "");
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public GameSignFormat getFormat() {
        return format;
    }

    public List<GameSign> getSigns() {
        return signs;
    }

    public GameLocation getFirstLocation() {
        return firstLocation;
    }

    public GameLocation getSecondLocation() {
        return secondLocation;
    }

    public String getGameChannel() {
        return channel;
    }

    public void setGameChannel(String channel) {
        this.channel = channel;
    }

    private void buildLocationsObject(GameLocation first, GameLocation second) {
        int minx, maxx, miny, maxy, minz, maxz;

        if (first.getX() > second.getX()) {
            minx = second.getX();
            maxx = first.getX();
        } else {
            minx = first.getX();
            maxx = second.getX();
        }

        if (first.getY() > second.getY()) {
            miny = second.getY();
            maxy = first.getY();
        } else {
            miny = first.getY();
            maxy = second.getY();
        }

        if (first.getZ() > second.getZ()) {
            minz = second.getZ();
            maxz = first.getZ();
        } else {
            minz = first.getZ();
            maxz = second.getZ();
        }

        World world = Bukkit.getWorld(first.getWorld());
        if (world == null) {
            return;
        }

        for (int y = maxy; y >= miny; y--) {
            for (int x = minx; x <= maxx; x++) {
                for (int z = minz; z <= maxz; z++) {
                    Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();

                    if (block != null) {
                        signs.add(new GameSign(location));
                    }
                }
            }
        }
    }

    private void writeToSign(Sign sign, List<String> lines) {
        lines = Envoyer.colorize(lines, '0');

        int index = 0;
        for (String line : lines) {
            if (index > 3) {
                continue;
            }

            sign.setLine(index++, line);
        }

        for (int i = index; i < 4; i++) {
            sign.setLine(i, "");
        }

        sign.update();
    }

    private void writeToSign(Sign sign, String... lines) {
        List<String> text = new ArrayList<>();
        text.addAll(Arrays.asList(lines));
        writeToSign(sign, text);
    }
}