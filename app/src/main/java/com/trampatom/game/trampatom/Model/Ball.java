package com.trampatom.game.trampatom.Model;

import android.graphics.Bitmap;

/**
 * Class containing getters and setters for a lot of ball related stuff
 */
public class Ball {
    public Ball(){

    }


    int x;
    int y;
    int ballType;
    double angle;
    int moveX, moveY;
    int ballHeight, ballWidth;

    public int getBallHeight() {
        return ballHeight;
    }

    public void setBallHeight(int ballHeight) {
        this.ballHeight = ballHeight;
    }

    public int getBallWidth() {
        return ballWidth;
    }

    public void setBallWidth(int ballWidth) {
        this.ballWidth = ballWidth;
    }

    public int getMoveX() {
        return moveX;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public Bitmap getBallColor() {
        return ballColor;
    }

    public void setBallColor(Bitmap ballColor) {
        this.ballColor = ballColor;
    }

    Bitmap ballColor;

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

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

}
