package com.trampatom.game.trampatom.utils;


import com.trampatom.game.trampatom.activity.Game3;
import com.trampatom.game.trampatom.activity.MainActivity;

import java.util.Random;

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
    int width ;
    int height;

    int ballWidth;
    int ballHeight;
    Random random;

    public RandomBallVariables(int width, int height, int ballWidth, int ballHeight){
        this.width=width;
        this.height=height;
        this.ballWidth=ballWidth;
        this.ballHeight=ballHeight;
        random = new Random();
    }

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
        Random number= new Random();
        x= number.nextInt( width);
        x=offscale(x, width, ballWidth);
        return x;
    }

    /**
     * Methodfor getting a random Y coordinate
     * @return a random Y coordinate
     */
    public int randomY() {
        Random number2= new Random();
        y= number2.nextInt( height);
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
        Random number = new Random();
        randomAngle = number.nextInt(360);
        angle= randomAngle * RADIANS;

        return angle;
    }

    public int[] randomnegativeBallsCoordinates(){
        int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        //draw the first ball
        for(int i=0; i< Game3.BALL_NEGATIVE_NUMBER; i++){
            XY[i] = randomX();
            XY[i+Game3.BALL_NEGATIVE_NUMBER] = randomY();
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
