package com.trampatom.game.trampatom.utils;

public class GameWindow {
    private int width;
    private int height;

    //left and top side of the screen, used to draw a ball within a certain limit if we used limiting square power up.
    private int leftSide;
    private int topSide;

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


    public int getLeftSide() {
        return leftSide;
    }
    public void setLeftSide(int leftSide) {
        this.leftSide = leftSide;
    }
    public int getTopSide() {
        return topSide;
    }
    public void setTopSide(int topSide) {
        this.topSide = topSide;
    }
}
