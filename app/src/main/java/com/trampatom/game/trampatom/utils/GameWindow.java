package com.trampatom.game.trampatom.utils;

public class GameWindow {
    private int width;
    private int height;

    private static GameWindow instance;

    private GameWindow(){
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static GameWindow getInstance() {
        if (instance == null)
            instance = new GameWindow();

        return instance;
    }
}
