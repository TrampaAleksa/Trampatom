package com.trampatom.game.trampatom.ball;


import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.utils.GameWindow;
import com.trampatom.game.trampatom.utils.Keys;

import java.util.Random;


/**
 * This class should contain methods for handling all mBall movement.
 * <p>It should handle mBall bouncing on screen edges, as well as directions of movement.
 * Most methods will return an array containing an move array with new x, y coordinates</p>
 */
public class BallMovement {
    //used for determining in how many seconds will the red balls speed up
    private static final int RED_BALL_SPEED_UP_INTERVAL = 8;
    //used to set a speed limit to red balls in the survival game
    private static final int RED_BALL_SPEED_LIMIT = 16;
    Random random;
    int changeAngleGreen;
    int x, y, moveX, moveY;
    double angle;
    int ballWidth, ballHeight;
    int ballSpeed;
    boolean shouldMove = true;


    /**
     * Constructor that contains and gets the canvas's width adn height to
     * use so that balls don't fall off screen
     * @param screenWidth screen's width
     * @param screenHeight screen's height
     */
    public BallMovement(int screenWidth, int screenHeight) {
        setWidth(screenWidth);
        setHeight(screenHeight);
        random  = new Random();
    }



    /**
     * Method that uses a passed mBall object to manipulate its coordinates and move it.
     * @param ballObject the mBall object we want to move
     * @return the now moved mBall
     */
    public Ball moveBall(Ball ballObject){

         x = ballObject.getX();
         y = ballObject.getY();
         moveX = ballObject.getMoveX();
         moveY = ballObject.getMoveY();
         angle = ballObject.getAngle();
         ballWidth = ballObject.getBallWidth();
         ballHeight = ballObject.getBallHeight();
         ballSpeed = ballObject.getBallSpeed();

        //if we used the Gravity Pull power up
        if(ballObject.isActiveChangesAngle()){
            if(checkIfCenter(ballObject.getX(), ballObject.getY())){
                return ballObject;
            }
        }

        x += moveX*ballSpeed * Math.cos(angle);
        y += moveY*ballSpeed * Math.sin(angle);

        //if the mBall is off screen change its direction
        if(x > getWidth() - getBallWidth()) {
            x = getWidth() - getBallWidth();
            moveX = -moveX;
            // too far right
        }
        if(y > getHeight() - getBallHeight()) {
            y = getHeight() - getBallHeight();
            moveY = -moveY;
            // too far bottom
        }
        if(x < getLeftSide()) {
            x = getLeftSide();
            moveX = -moveX;
            // too far left
        }
        if(y < getTopSide()) {
            y = getTopSide();
            moveY = -moveY;
            // too far top
        }

        ballObject.setX(x); ballObject.setY(y);
        ballObject.setMoveX(moveX); ballObject.setMoveY(moveY);
        return ballObject;
    }




    /**
     * Method that uses a passed mBall object to manipulate its coordinates and move it.
     * @param ballObject the mBall object we want to move
     * @return the now moved mBall
     */
    public Ball moveGreenBall(Ball ballObject){

            //gets a random number and if its below 20 change the angle
            changeAngleGreen = random.nextInt(Keys.GREEN_BALL_ANGLE_CHANGE_CHANCE);
            if (changeAngleGreen <= 20) {
                ballObject.setAngle((random.nextInt(360)) * (3.14 / 180));
            }

            x = ballObject.getX();
            y = ballObject.getY();
            moveX = ballObject.getMoveX();
            moveY = ballObject.getMoveY();
            angle = ballObject.getAngle();
            ballWidth = ballObject.getBallWidth();
            ballHeight = ballObject.getBallHeight();
            ballSpeed = ballObject.getBallSpeed();


            x += moveX * ballSpeed * Math.sin(angle);
            y += moveY * ballSpeed * Math.cos(angle);

            //if the mBall is off screen change its direction
            if (x > getWidth() - getBallWidth()) {
                x = getWidth() - getBallWidth();
                moveX = -moveX;
                // too far right
            }
            if (y > getHeight() - getBallHeight()) {
                y = getHeight() - getBallHeight();
                moveY = -moveY;
                // too far bottom
            }
            if (x < getLeftSide()) {
                x = getLeftSide();
                moveX = -moveX;
                // too far left
            }
            if (y < getTopSide()) {
                y = getTopSide();
                moveY = -moveY;
                // too far top
            }

            ballObject.setX(x);
            ballObject.setY(y);
            ballObject.setMoveX(moveX);
            ballObject.setMoveY(moveY);
            return ballObject;

    }


    /**
     * Method used for moving a mBall
     * @param x x coordinate of the mBall
     * @param y y coordinate of the mBall
     * @param ballWidth width of the mBall we are currently moving
     * @param ballHeight height of the mBall we are currently moving
     * @param moveX used for mBall bouncing, always 1 or -1
     * @param moveY used for mBall bouncing, always 1 or -1
     * @param angle angle at witch the mBall moves in radians
     * @param ballSpeed speed of the mBall
     * @return returns an integer array that holds x,y, moveX, moveY values
     */
    public int[] moveBall(int x, int y ,int ballWidth, int ballHeight, int moveX, int moveY, double angle,int ballSpeed){
        x += moveX*ballSpeed * Math.sin(angle);
        y += moveY*ballSpeed * Math.cos(angle);

        //if the mBall is off screen change its direction
        if(x > getWidth() -ballWidth) {
            x = getWidth() -ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(y > getHeight() -ballHeight) {
            y = getHeight() -ballHeight;
            moveY = -moveY;
            // too far bottom
        }
        if(x < getLeftSide()) {
            x = getLeftSide();
            moveX = -moveX;
            // too far left
        }
        if(y < getTopSide()) {
            y = getTopSide();
            moveY = -moveY;
            // too far top
        }

        int helpArray[] = {x,y,moveX,moveY};
        return helpArray;
    }


    /**
     * Method that uses a passed mBall object to manipulate its coordinates and move it.
     * @param ballObject the mBall object we want to move, waves will move horizontally
     * @return the now moved mBall
     */
    public Ball moveWave(Ball ballObject){

        //if we used the Gravity Pull power up
        if(ballObject.isActiveChangesAngle()){
            if(checkIfCenter(ballObject.getX(), ballObject.getY())){
                return ballObject;
            }
        }

        x = ballObject.getX();
        y = ballObject.getY();
        moveX = ballObject.getMoveX();
        moveY = ballObject.getMoveY();
        ballWidth = ballObject.getBallWidth();
        ballHeight = ballObject.getBallHeight();
        ballSpeed = ballObject.getBallSpeed();

        // wave moves just in x coordinate but y could be used later
        if(!ballObject.isActiveChangesAngle()) {
            x += moveX * ballSpeed;
        }
        //if we used gravity pull move all the balls towards the center
        else {
            angle = ballObject.getAngle();
            x += moveX*ballSpeed * Math.cos(angle);
            y += moveY*ballSpeed * Math.sin(angle);
        }

        //if the mBall is off screen change its direction
        if(x > getWidth() - getBallWidth()) {
            x = getWidth() - getBallWidth();
            moveX = -moveX;
            // too far right
        }
        if(x < getLeftSide()) {
            x = getTopSide();
            moveX = -moveX;
            // too far left
        }

        ballObject.setX(x); ballObject.setY(y);
        ballObject.setMoveX(moveX); ballObject.setMoveY(moveY);
        return ballObject;
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

        //if the mBall is off screen change its direction
        if(x > getWidth() -ballWidth) {
            x = getWidth() -ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(y > getHeight() -ballHeight) {
            y = getHeight() -ballHeight;
            moveY = -moveY;
            // too far bottom
        }
        if(x < getLeftSide()) {
            x = getLeftSide();
            moveX = -moveX;
            // too far left
        }
        if(y < getTopSide()) {
            y = getTopSide();
            moveY = -moveY;
            // too far top
        }

        int helpArray[] = {x,y,moveX,moveY,(int) angle};
        return helpArray;

    }


    /**
     * Method used for checking if the ball is near the center. used to get a boolean for stopping the ball's movement
     * @return true if its near the center
     */
    private boolean checkIfCenter(int x, int y) {
        if(x>(getWidth() /2- getBallWidth()) && x<(getWidth() /2+ getBallWidth()) && y>(getHeight() /2 - getBallHeight()) && y<(getHeight() /2 + getBallHeight()))
            return true;

        return false;
    }

    private int getBallWidth() {
        return ballWidth;
    }
    private int getBallHeight() {
        return ballHeight;
    }
    private int getWidth() {
        return getGameWindow().getWidth();
    }
    private int getHeight() {
        return getGameWindow().getHeight();
    }
    private void setWidth(int width) {
        getGameWindow().setWidth(width);
    }
    private void setHeight(int height) {
        getGameWindow().setHeight(height);
    }
    private GameWindow getGameWindow(){
        return GameWindow.getInstance();
    }

    public int getLeftSide() {
        return getGameWindow().getLeftSide();
    }
    public int getTopSide() {
        return getGameWindow().getTopSide();
    }
}
