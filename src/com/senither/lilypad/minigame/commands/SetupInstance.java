package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.boards.GameLocation;

import java.util.HashMap;

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

    public enum SetupStage {

        BOARD(1, "Wall Setup", true),
        CHANNEL(2, "Channel Link", true),
        NAME(3, "Wall Name", false);

        private final static HashMap<Integer, SetupStage> stages = new HashMap<>();

        static {
            for (SetupStage stage : values()) {
                stages.put(stage.getId(), stage);
            }
        }

        private final int id;
        private final String name;
        private final boolean more;

        SetupStage(int id, String name, boolean more) {
            this.name = name;
            this.id = id;
            this.more = more;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public boolean hasMore() {
            return more;
        }

        public SetupStage next(SetupStage stage) {
            if (stage != null && stage.hasMore()) {
                return stages.get(stage.getId() + 1);
            }
            return null;
        }
    }
}