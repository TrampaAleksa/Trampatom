package com.trampatom.game.trampatom.Model;


/**
 * Class used to store every star in background in an array list. Each star contains
 * getters and setters for stars x, y coordinates and their radius
 */
public class Star {

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    int x; int y; int radius;

}
