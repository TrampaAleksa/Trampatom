package com.trampatom.game.trampatom.Model;

import android.graphics.Bitmap;

/**
 * Class containing getters and setters for a lot of ball related stuff.
 * x,y ball coordinates.
 * ballType - determines what type the ball is, used for ball logic.
 * moveX, moveY - used for ball movement/ bouncing off walls
 * ballHeight, ballWidth - every ball has its width and height, usually the same.
 * ballSpeed 
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
    int ballSpeed;

    public boolean isActiveChangesAngle() {
        return activeChangesAngle;
    }

    public void setActiveChangesAngle(boolean activeChangesAngle) {
        this.activeChangesAngle = activeChangesAngle;
    }

    boolean activeChangesAngle;

    boolean activeChangesSpeed;

    boolean activeChangesType;

    public boolean isActiveChangesType() {
        return activeChangesType;
    }

    public void setActiveChangesType(boolean activeChangesType) {
        this.activeChangesType = activeChangesType;
    }

    public boolean isActiveChangesSize() {
        return activeChangesSize;
    }

    public void setActiveChangesSize(boolean activeChangesSize) {
        this.activeChangesSize = activeChangesSize;
    }

    boolean activeChangesSize;

    public boolean isActiveChangesSpeed() {
        return activeChangesSpeed;
    }

    public void setActiveChangesSpeed(boolean activeChangesSpeed) {
        this.activeChangesSpeed = activeChangesSpeed;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    int soundId;

    public int getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(int ballSpeed) {
        //used to prevent setting negative speed values(reversing the ball)
        if(ballSpeed <0)
            ballSpeed=0;
        this.ballSpeed = ballSpeed;
    }

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
