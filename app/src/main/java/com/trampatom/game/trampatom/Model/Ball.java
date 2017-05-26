package com.trampatom.game.trampatom.Model;


public class Ball {
    public Ball(){

    }


    int x;
    int y;
    int ballType;
    double angle;

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
