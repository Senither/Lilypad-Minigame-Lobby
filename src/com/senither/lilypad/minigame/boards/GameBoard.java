package com.senither.lilypad.minigame.boards;

import com.senither.lilypad.minigame.Constants;
import com.senither.lilypad.minigame.network.Server;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class GameBoard {

    private final BoardManager bm;

    private final String name;
    private final GameSignFormat format;
    private final List<GameSign> signs = new ArrayList<>();
    private final GameLocation firstLocation;
    private final GameLocation secondLocation;

    private boolean flip = false;
    private String channel = "GLOBAL";

    public GameBoard(BoardManager bm, String boardName, GameSignFormat format, GameLocation first, GameLocation second, boolean flip) {
        this.bm = bm;
        this.flip = flip;

        this.name = boardName;
        this.format = format;

        this.firstLocation = first;
        this.secondLocation = second;

        this.buildLocationsObject(first, second);
    }

    public GameBoard(BoardManager bm, String boardName, GameSignFormat format, GameLocation first, GameLocation second) {
        this(bm, boardName, format, first, second, false);
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

                if (!list.isEmpty()) {
                    if (sign.hasMetadata(Constants.SIGN_META_OFFLINE)) {
                        sign.removeMetadata(Constants.SIGN_META_OFFLINE, bm.getPlugin());
                    }

                    Server server = list.remove(0);
                    List<String> signText = format.buildFrom(server.getName(), server.getPlaceholders());

                    sign.setMetadata(Constants.SIGN_META_ONLINE, new FixedMetadataValue(bm.getPlugin(), server.getName()));
                    writeToSign(sign, signText);
                } else {
                    if (sign.hasMetadata(Constants.SIGN_META_ONLINE)) {
                        sign.removeMetadata(Constants.SIGN_META_ONLINE, bm.getPlugin());
                    }
                    sign.setMetadata(Constants.SIGN_META_OFFLINE, new FixedMetadataValue(bm.getPlugin(), "offline"));
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

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public void reloadLocationObjects() {
        signs.clear();
        buildLocationsObject(firstLocation, secondLocation);
    }

    private void buildLocationsObject(GameLocation first, GameLocation second) {
        int minX = Math.min(first.getX(), second.getX());
        int maxX = Math.max(first.getX(), second.getX());

        int minY = Math.min(first.getY(), second.getY());
        int maxY = Math.max(first.getY(), second.getY());

        int minZ = Math.min(first.getZ(), second.getZ());
        int maxZ = Math.max(first.getZ(), second.getZ());

        World world = Bukkit.getWorld(first.getWorld());
        if (world == null) {
            return;
        }

        for (int y = maxY; y >= minY; y--) {
            List<GameSign> gameSigns = new ArrayList<>();
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();

                    if (block != null) {
                        gameSigns.add(new GameSign(location));
                    }
                }
            }

            if (flip) {
                Collections.reverse(gameSigns);
            }
            signs.addAll(gameSigns);
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