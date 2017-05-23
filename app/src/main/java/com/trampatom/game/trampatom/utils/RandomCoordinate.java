package com.trampatom.game.trampatom.utils;


import com.trampatom.game.trampatom.activity.MainActivity;

import java.util.Random;

public class RandomCoordinate {
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

    public RandomCoordinate(int width, int height, int ballWidth, int ballHeight){
        this.width=width;
        this.height=height;
        this.ballWidth=ballWidth;
        this.ballHeight=ballHeight;
    }
    boolean draw1,draw2,draw3, help3, crossover;

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

    public int[] randomThreeBallCoordinates(int ballWidth, int ballHeight){
        int x1,x2,x3,y1,y2,y3;
        x1=x2=x3=y1=y2=y3=0;
        int[] XY1 ={x1,y1};
        int[] XY2 ={x2,y2};
        int[] XY3 ={x3,y3};

        //draw the first ball
        XY1[0]=randomX();
        XY1[1]=randomY();
        //draw the second ball
        XY2[0]=randomX();
        XY2[1]=randomY();
        //move it if it is crossed with the first
        if(XY2[1]<(XY1[1]+ballHeight) && XY2[1]>(XY1[1]-ballHeight)) {
            if (XY2[0] > (XY1[0] - ballWidth) && XY2[0] < (XY1[0] + ballWidth)) {
               XY2= moveBall(XY2[0],XY2[1]);
            }
        }
        //draw the third ball
        XY3[0]=randomX();
        XY3[1]=randomY();
        //move it if its within the first ball
        if(XY3[1]<(XY1[1]+ballHeight) && XY3[1]>(XY1[1]-ballHeight)) {
                if (XY3[0] > (XY1[0] - ballWidth) && XY3[0] < (XY1[0] + ballWidth)) {
                    XY3= moveBall(XY3[0],XY3[1]);
                }
        }
        //move it if its within the second ball
        if(XY3[1]<(XY2[1]+ballHeight) && XY3[1]>(XY2[1]-ballHeight)) {
            if(XY3[0]>(XY2[0]-ballWidth) && XY3[0]<(XY2[0]+ballWidth)){
                XY3=  moveBall(XY3[0],XY3[1]);
            }
        }
        //safety. In case after moving out of second ball into the first
        if(XY3[1]<(XY1[1]+ballHeight) && XY3[1]>(XY1[1]-ballHeight)) {
            if (XY3[0] > (XY1[0] - ballWidth) && XY3[0] < (XY1[0] + ballWidth)) {
                XY3= moveBall(XY3[0],XY3[1]);
            }
        }
        int[] XY= {XY1[0],XY2[0],XY3[0],XY1[1], XY2[1], XY3[1]};
       // int[] XY = {x1,x2,x3,y1,y2,y3};
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
}
