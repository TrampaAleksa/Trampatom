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

    //used for checking if the ball is over a screen's edge
    private final static int OVER_LEFT = 1;
    private final static int OVER_TOP = 2;
    private final static int OVER_RIGHT = 3;
    private final static int OVER_BOTTOM = 4;
    private final static int OVER_NOTHING = 5;
    //used for determining what quarter is the ball in
    private final static int QUARTER_ONE = 1;
    private final static int QUARTER_TWO = 2;
    private final static int QUARTER_THREE = 3;
    private final static int QUARTER_FOUR = 4;
    private final static int QUARTER_UNKNOWN = 0;

        int y, x;
    //width and height of the device/canvas
    private int width , height;
    //left and top side of the screen, used to draw a ball within a certain limit if we used limiting square power up.
    private static int leftSide = 0;
    private static int topSide = 0;

    public static int getLeftSide(){

        return leftSide;
    }

    public static int getTopSide(){

        return topSide;
    }

    int ballWidth;
    int ballHeight;
    Random random;

    /**
     * Constructor for inserting device width, height and ball width and height
     * only once instead of on every method call
     * @param width device width
     * @param height canvas height
     * @param ballWidth general ball width
     * @param ballHeight general ball height
     */
    public RandomBallVariables(int width, int height, int ballWidth, int ballHeight){
        this.width=width;
        this.height=height;
        this.ballWidth=ballWidth;
        this.ballHeight=ballHeight;
        random = new Random();
    }

    /**
     * method used to change the width and height values of our canvas. not teh actual width/height but the parameters that are used
     * for ball movement and "edge" bouncing
     * @param addWidth
     * @param addHeight
     */
    public void changeWidthAndHeight(int addWidth, int addHeight){

        width = width + addWidth;
        height = height + addHeight;
        leftSide += addWidth;
        topSide += addHeight;

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
        x= leftSide + random.nextInt( width - leftSide);
        x= offscale(x, width, ballWidth);
        return x;
    }

    /**
     * Methodfor getting a random Y coordinate
     * @return a random Y coordinate
     */
    public int randomY() {
        y=topSide + random.nextInt( height - topSide);
        y=offscale(y, height, ballHeight);
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
        float deltaX = width/2 - x;
        float deltaY = height/2 - y;
        double angle = Math.atan2( deltaY, deltaX );

        return angle;
    }

    /**
     * Method that should be used in survival game. Gets random coordinates for every red ball
     * @return a set of coordinates for red balls: x1,x2,x3... xN, y1, y2 ,y3 ... yN
     */
    public int[] randomnegativeBallsCoordinates(){
        int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        //draw the first ball
        for(int i = 0; i< GameSurvivalActivity.BALL_NEGATIVE_NUMBER; i++){
            XY[i] = randomX();
            XY[i+ GameSurvivalActivity.BALL_NEGATIVE_NUMBER] = randomY();
        }

        // int[] XY = {x1,x2,x3,y1,y2,y3...};
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

    /**
     * Method used for determining in which quarter the ball is
     * @param x coordinate of the ball
     * @param y coordinate of the ball
     * @return the quarter in witch the ball is
     */
    public int quarter(int x, int y){

        if(x> width/2 && y<height/2){
            return QUARTER_ONE;
        }
        if(x< width/2 && y<height/2){
            return QUARTER_TWO;
        }
        if(x< width/2 && y>height/2){
            return QUARTER_THREE;
        }
        if(x> width/2 && y>height/2){
            return QUARTER_FOUR;
        }

        return QUARTER_UNKNOWN;
    }

    private int[] moveBall(int x, int y){
        int[] XY= {x,y};
        switch(quarter(x,y))
        {
            //move it left
            case QUARTER_ONE:
                XY[0]= x-ballWidth*2;
                break;
            //move it down
            case QUARTER_TWO:
                XY[1]= y+ballHeight*2;
                break;
            //move it right
            case QUARTER_THREE:
                XY[0]= x+ballHeight*2;
                break;
            //move it up
            case QUARTER_FOUR:
                XY[1]= y-ballHeight*2;
                break;
        }
        return XY;
    }

    /**
     * Method used for determining if the ball is over an edge
     * @param x coordinate of the ball
     * @param y coordinate of the ball
     * @return over what side of the screen is the ball
     */
    private int overEdge(int x, int y){
        if(x<0){
            return OVER_LEFT;
        }
        if(y<0){
            return OVER_TOP;
        }
        if(x>width-ballWidth){
            return OVER_RIGHT;
        }
        if(y>height-ballHeight){
            return OVER_BOTTOM;
        }
        return OVER_NOTHING;
    }
}
