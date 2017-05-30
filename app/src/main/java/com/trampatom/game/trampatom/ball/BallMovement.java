package com.trampatom.game.trampatom.ball;


import java.util.Random;

public class BallMovement {
    private static int RED_BALL_SPEED_LIMIT = 14;
    int width;
    int height;
    Random random;

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
     * Method for slowly speeding up red balls every 10 seconds
     * @param redBallSpeed the current speed that will be increased
     * @param seconds the time of our timer we use for speeding up
     * @return the new speed
     */
    public int redBallsSpeedUp(int redBallSpeed, int seconds){
        if(seconds %10 == 0){
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
