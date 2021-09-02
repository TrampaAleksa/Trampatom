package com.trampatom.game.trampatom.utils;


import com.trampatom.game.trampatom.activity.GameSurvivalActivity;

import java.util.Random;


/**
 * <p>Important class containing methods used to get every random variable: angles, coordinates, ball type; </p>
 * Also contains methods for checking at what quarter of the screen an object is,
 * and methods for preventing sprite off screen bugs.
 */
public class RandomBallVariables {
    //used for converting an angle into radians
    private static final double RADIANS = 3.14/180;
    //left and top side of the screen, used to draw a ball within a certain limit if we used limiting square power up.
    private static int leftSide = 0;
    private static int topSide = 0;

    int ballWidth;
    int ballHeight;
    Random random;

    /**
     * Constructor for inserting ball width and height
     * only once instead of on every method call
     * @param ballWidth general ball width
     * @param ballHeight general ball height
     */
    public RandomBallVariables(int ballWidth, int ballHeight){
        this.ballWidth=ballWidth;
        this.ballHeight=ballHeight;
        random = new Random();
    }

    /**
     * method used to change the width and height values of our canvas. not teh actual width/height but the parameters that are used
     * for ball movement and "edge" bouncing
     * @param widthToAdd
     * @param heightToAdd
     */
    public void changeWidthAndHeight(int widthToAdd, int heightToAdd){
        getGameWindow().setWidth(getWidth() + widthToAdd);
        getGameWindow().setHeight(getHeight() + heightToAdd);
        leftSide += widthToAdd;
        topSide += heightToAdd;
    }


    /**
     * Method that should get a new ball type : red, blue, green, yellow, purple or wave depending on what
     * number it returns.
     * @return a random int value that is used to get a new ball
     */
    public int getRandomBallType(){
        int ballType;
        ballType= random.nextInt(21);
        return ballType;
    }

    /**
     * Method for getting a random X coordinate
     * @return a random X coordinate
     */
    public int randomX() {
        int x;
        x= getLeftSide() + random.nextInt( getWidth() - getLeftSide());
        x= offscale(x, getWidth(), ballWidth);
        return x;
    }

    /**
     * Methodfor getting a random Y coordinate
     * @return a random Y coordinate
     */
    public int randomY() {
        int y;
        y=getTopSide() + random.nextInt( getHeight() - getTopSide());
        y=offscale(y, getHeight(), ballHeight);
         return y;
    }

    /**
     * method used for getting a random angle for a ball's movement
     * @return
     */
    public double randomAngle(){
        int randomAngle;
        double angle;
        randomAngle = random.nextInt(360);
        //the angle has to be in radians if we are using sin or cos functions to determine movement
        angle= randomAngle * RADIANS;

        return angle;
    }

    /**
     * Method used for when the gravity pull power up is used. For moving the ball to the center of the screen
     * @param x current x position of the ball
     * @param y current y position of the ball
     * @return an angle pointing to the center of the screen
     */
    public double centeredAngle(int x, int y){
        float deltaX = getWidth()/2 - x;
        float deltaY = getHeight()/2 - y;
        double angle = Math.atan2( deltaY, deltaX );

        return angle;
    }

    /**
     * Method that should be used in survival game. Gets random coordinates for every red ball
     * @return a set of coordinates for red balls: x1,x2,x3... xN, y1, y2 ,y3 ... yN
     */
    public int[] randomNegativeBallsCoordinates(){
        int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        //draw the first ball
        for(int i = 0; i< GameSurvivalActivity.BALL_NEGATIVE_NUMBER; i++){
            XY[i] = randomX();
            XY[i+ GameSurvivalActivity.BALL_NEGATIVE_NUMBER] = randomY();
        }

        return XY;
    }

    /**
     * Method for fixing an object's position in case it's offscreen
     * @param value coordinate of the object
     * @param totalLength lenght of the surface we are checking
     * @param objectLength lenght of the object we are checking
     * @return fixed value if it was offscreen
     */
    private int offscale(int value, int totalLength, int objectLength){

        int offscale;
        int fix;
        offscale= totalLength-value;
        if(offscale<objectLength){
            fix = objectLength - offscale;
            value = value - fix;
        }

        return value;
    }

    public static int getWidth() {
        return getGameWindow().getWidth();
    }
    public static int getHeight() {
        return getGameWindow().getHeight();
    }

    public static int getLeftSide(){
        return leftSide;
    }
    public static int getTopSide(){
        return topSide;
    }

    private static GameWindow getGameWindow(){
        return GameWindow.getInstance();
    }
}
