package com.senither.lilypad.minigame.boards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameSignFormat {

    private List<String> format = new ArrayList<>();

    public GameSignFormat(List<String> format) {
        this.format = format;
    }

    public void setLine(int index, String line) {
        format.set(index, line);
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public List<String> buildFrom(String server, HashMap<String, String> placeholders) {
        List<String> lines = new ArrayList<>();

        for (String line : getFormat()) {
            String text = line.replace("{server}", server);

            for (String placeholder : placeholders.keySet()) {
                text = text.replace(
                        String.format("{%s}", placeholder),
                        placeholders.get(placeholder)
                );
            }

            lines.add(text);
        }

        return lines;
    }
}