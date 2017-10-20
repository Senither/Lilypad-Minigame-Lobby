package com.senither.lilypad.minigame.network;

import java.util.HashMap;

public class Server implements Comparable<Server> {

    private final String name;

    private boolean display = false;
    private String channel = "GLOBAL";
    private HashMap<String, String> placeholders = new HashMap<>();
    private long lastCheck;

    public Server(String name) {
        this.name = name;
        this.lastCheck = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Server compare) {
        int compareValue = Integer.parseInt(compare.getPlaceholder("playersOnline"));
        int currentValue = Integer.parseInt(getPlaceholder("playersOnline"));

        return compareValue - currentValue;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isOnline() {
        return lastCheck >= System.currentTimeMillis();
    }

    public void setOnline(boolean online) {
        lastCheck = online ? System.currentTimeMillis() + 3 * 1000 : -1;
    }

    public String getGameChannel() {
        return channel;
    }

    public void setGameChannel(String channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(HashMap<String, String> placeholders) {
        this.placeholders = placeholders;

        if (this.placeholders.containsKey("display")) {
            setDisplay(this.placeholders.get("display").equalsIgnoreCase("true"));
        }

        if (this.placeholders.containsKey("channel") && this.placeholders.get("channel").length() > 0) {
            setGameChannel(this.placeholders.get("channel"));
        }
    }

    public String getPlaceholder(String placeholder) {
        return placeholders.getOrDefault(placeholder, "");
    }
}
