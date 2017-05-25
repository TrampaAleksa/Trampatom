package com.trampatom.game.trampatom.utils;


public class ClickedBall {


    public static boolean clickedABall(int x, int y, int clickedX, int clickedY, int ballWidth, int ballHeight){
        if(clickedX>x && clickedX<(x+ballWidth) && clickedY>y && clickedY<(y+ballHeight))
            return true;
        else
        return false;
    }
}
