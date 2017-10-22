package com.senither.lilypad.minigame.commands.setup;

import java.util.HashMap;

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