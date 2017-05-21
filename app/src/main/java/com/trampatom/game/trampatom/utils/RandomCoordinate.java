package com.trampatom.game.trampatom.utils;


import com.trampatom.game.trampatom.activity.MainActivity;

import java.util.Random;

public class RandomCoordinate {

        int y, x, offscale, fixX, fixY;
        int width  = MainActivity.getWidth();

    /**
     * Method for getting a random X coordinate
     * @param ballWidth parameter used so the ball isn't drawn off-screen
     * @return a random X coordinate
     */
    public int randomX(int ballWidth) {
        Random number= new Random();
        x= number.nextInt( width);
        offscale=width-x;
        if(offscale<ballWidth){
            fixX=ballWidth-offscale;
            x=x-fixX;

        }return x;}

    /**
     * Methodfor getting a random Y coordinate
     * @param ballHeight parameter used so the ball isn't drawn off-screen
     * @return a random Y coordinate
     */
    //TODO Use Canvas.getHeight method to fix offscale
    public int randomY(int ballHeight,int viewHeight) {
        Random number2= new Random();
        y= number2.nextInt( viewHeight);
        offscale=viewHeight-y;
        if(offscale<ballHeight) {
            fixY = ballHeight*2 - offscale;
            y = y - fixY;

        } return y;
    }
}
