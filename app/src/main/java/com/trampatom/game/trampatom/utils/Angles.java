package com.trampatom.game.trampatom.utils;

import java.util.Random;

public class Angles {
    private static final double RADIANS = 3.14/180;

    /**
     * method used for getting a random angle for a ball's movement
     * @return
     */
    public static double randomAngle(){
        Random random = new Random();
        int randomAngle;
        double angle;
        randomAngle = random.nextInt(360);
        //the angle has to be in radians if we are using sin or cos functions to determine movement
        angle= randomAngle * RADIANS;

        return angle;
    }
}
