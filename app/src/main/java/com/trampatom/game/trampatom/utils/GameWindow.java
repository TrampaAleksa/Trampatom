package com.trampatom.game.trampatom.utils;

public class GameWindow {
    private static int width;
    private static int height;

    private static GameWindow instance;

    private GameWindow(){
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        GameWindow.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        GameWindow.height = height;
    }

    public static GameWindow getInstance() {
        if (instance == null)
            instance = new GameWindow();

        return instance;
    }
}
