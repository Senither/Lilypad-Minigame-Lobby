package com.senither.lilypad.minigame.commands.setup;

import com.senither.lilypad.minigame.boards.GameLocation;

public class SetupInstance {

    private final String player;
    private String name;
    private String channel;
    private GameLocation firstLocation;
    private GameLocation secondLocation;

    private SetupStage stage = SetupStage.BOARD;

    public SetupInstance(String player) {
        this.player = player;

        this.name = null;
        this.channel = "GLOBAL";
        this.firstLocation = null;
        this.secondLocation = null;
    }

    public String getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameChannel() {
        return channel;
    }

    public void setGameChannel(String channel) {
        this.channel = channel;
    }

    public GameLocation getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(GameLocation firstLocation) {
        this.firstLocation = firstLocation;
    }

    public GameLocation getSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(GameLocation secondLocation) {
        this.secondLocation = secondLocation;
    }

    public SetupStage getStage() {
        return stage;
    }

    public void setStage(SetupStage stage) {
        this.stage = stage;
    }

    public boolean validate() {
        switch (stage) {
            case BOARD:
                return getFirstLocation() != null && getSecondLocation() != null;

            case CHANNEL:
                return getGameChannel() != null;

            case NAME:
                return getName() != null;
        }
        return false;
    }
}