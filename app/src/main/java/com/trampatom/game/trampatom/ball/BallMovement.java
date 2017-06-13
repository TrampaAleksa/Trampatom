package com.trampatom.game.trampatom.ball;


import java.util.Random;


/**
 * This class should contain methods for handling all ball movement.
 * <p>It should handle ball bouncing on screen edges, as well as directions of movement.
 * Most methods will return an array containing an move array with new x, y coordinates</p>
 */
public class BallMovement {
    //used for determining in how many seconds will the red balls speed up
    private static final int RED_BALL_SPEED_UP_INTERVAL = 8;
    //used to set a speed liit to red balls in the survival game
    private static final int RED_BALL_SPEED_LIMIT = 16;
    int width;
    int height;
    Random random;

    //TODO inject ball width and height

    /**
     * Constructor that contains and gets the canvas's width adn height to
     * use so that balls don't fall off screen
     * @param width screen's width
     * @param height screen's height
     */
    public BallMovement(int width, int height) {
        this.width = width;
        this.height = height;
        random  = new Random();
    }

    /**
     * Method used for moving a ball
     * @param x x coordinate of the ball
     * @param y y coordinate of the ball
     * @param ballWidth width of the ball we are currently moving
     * @param ballHeight height of the ball we are currently moving
     * @param moveX used for ball bouncing, always 1 or -1
     * @param moveY used for ball bouncing, always 1 or -1
     * @param angle angle at witch the ball moves
     * @param ballSpeed speed of the ball
     * @return returns an integer array that holds x,y, moveX, moveY values
     */
    public int[] moveBall(int x, int y ,int ballWidth, int ballHeight, int moveX, int moveY, double angle,int ballSpeed){
        x += moveX*ballSpeed * Math.sin(angle);
        y += moveY*ballSpeed * Math.cos(angle);

        //if the ball is off screen change its direction
        if(x > width-ballWidth) {
            x = width-ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(y > height-ballHeight) {
            y = height-ballHeight;
            moveY = -moveY;
            // too far bottom
        }
        if(x < 0) {
            x = 0;
            moveX = -moveX;
            // too far left
        }
        if(y < 0) {
            y = 0;
            moveY = -moveY;
            // too far top
        }

        int helpArray[] = {x,y,moveX,moveY};
        return helpArray;
    }

    /**
     * method used for moving the wave balls on the y coordinate left and right
     * @param x current x coordinate
     * @param y current y coordinate
     * @param moveX direction of movement
     * @param moveY direcion of y movement
     * @param ballSpeed ball speed
     * @return x , y , moveX, moveY
     */
    public int[] moveWave(int x, int y ,int ballWidth, int moveX, int moveY,int ballSpeed){
        // wave moves just in x coordinate but y could be used later
        x += moveX*ballSpeed;

        //if the ball is off screen change its direction
        if(x > width-ballWidth) {
            x = width-ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(x < 0) {
            x = 0;
            moveX = -moveX;
            // too far left
        }

        int helpArray[] = {x,y,moveX,moveY};
        return helpArray;
    }

    /**
     * Method for slowly speeding up red balls every 10 seconds
     * @param redBallSpeed the current speed that will be increased
     * @param seconds the time of our timer we use for speeding up
     * @return the new speed
     */
    public int redBallsSpeedUp(int redBallSpeed, int seconds){
        if(seconds %RED_BALL_SPEED_UP_INTERVAL == 0){
            //every 10 seconds of the timer increase the speed of the red balls
            redBallSpeed ++;
            //limit the speed to prevent infinite speeding up on long survival times
            if(redBallSpeed >= RED_BALL_SPEED_LIMIT+1)
                redBallSpeed = RED_BALL_SPEED_LIMIT;
        }

        return redBallSpeed;
    }


    public int[] moveGreenBall(int x, int y ,int ballWidth, int ballHeight, int moveX, int moveY, double angle,int ballSpeed){
        int changeAngleGreen = random.nextInt(350);
        if(changeAngleGreen<=20){
            angle= random.nextInt(360);
        }

        x += moveX*ballSpeed * Math.sin(angle);
        y += moveY*ballSpeed * Math.cos(angle);

        //if the ball is off screen change its direction
        if(x > width-ballWidth) {
            x = width-ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(y > height-ballHeight) {
            y = height-ballHeight;
            moveY = -moveY;
            // too far bottom
        }
        if(x < 0) {
            x = 0;
            moveX = -moveX;
            // too far left
        }
        if(y < 0) {
            y = 0;
            moveY = -moveY;
            // too far top
        }

        int helpArray[] = {x,y,moveX,moveY,(int) angle};
        return helpArray;

    }
}
