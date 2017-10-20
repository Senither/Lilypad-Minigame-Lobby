package com.senither.lilypad.minigame.network;

import java.util.HashMap;

public class NetworkRequest {

    private String type;
    private String channel;
    private HashMap<String, String> data;

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{type=" + type + ", gameChannel=" + channel + ", data=" + data.toString() + "}";
    }
}
