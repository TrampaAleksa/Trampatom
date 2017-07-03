package com.trampatom.game.trampatom.ball;


import android.graphics.Bitmap;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.activity.GameClassicActivity;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

/**
 * Class that should be used to handle mBall types for game 1.
 * Contains methods for getting a new mBall, and should contain mBall action methods
 */
public class BallHandler {
    //TODO Arrays for balls refactoring in far future

    private Bitmap redBall,  blueBall,  greenBall,  yellowBall,  purpleBall; Bitmap[] waveBall;

    int i;
    int x,y,ballType;
    double angle;
    int ballSpeed, yellowBallSpeed, greenBallSpeed;
    int ballWidth, ballHeight;
    Keys keys;
    Ball mBall;
    Ball[] purpleBalls = {null,null,null};
    Ball[] waveBalls = {null,null,null,null,null,null,null};

    public static final int GOLD_BALL_DONT_DRAW = 0;
    private RandomBallVariables randomBallVariables;
    //TODO adequate java docs

    /**
     * Constructor for BallHandler. We should pass in an instance of RandomBallVariables and keys.
     * Also the mBall width and height initially used
     */
    public BallHandler(RandomBallVariables randomBallVariables, Keys keys,int ballWidth,int ballHeight){
        this.randomBallVariables = randomBallVariables;
        this.keys = keys;
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
        mBall = new Ball();
        ballSpeed = keys.DEFAULT_BALL_SPEED;
        yellowBallSpeed = keys.BALL_YELLOW_INITIAL_SPEED;
        greenBallSpeed = keys.GREEN_BALL_SPEED;

        //initialize the ball arrays here , because we need to create the ball
        // objects initially for the first create
        for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
            purpleBalls[i] = new Ball();
        }
        for(i=0; i< keys.WAVE_BALL_NUMBER; i++){
            waveBalls[i] = new Ball();
        }
    }

    /**
     * Since we will manipulate with ball types during the game, we have to pass every bitmap so that we can set it inside the handler
     * Theese bitmaps will later be used to change the balls sizes and colors depending on a power up or type of the ball
     */
    public void parseBallBitmaps(Bitmap redBall, Bitmap blueBall, Bitmap greenBall, Bitmap yellowBall, Bitmap purpleBall, Bitmap[] waveBall){
        this.redBall = redBall;
        this.blueBall = blueBall;
        this.greenBall = greenBall;
        this.yellowBall = yellowBall;
        this.purpleBall = purpleBall;
        this.waveBall = waveBall;
    }

    /**
     * Method used to get the first ball object with all of its attributes for later manipulating. Use this method to initialize
     * the first ball object in a game.
     * <p>
     *     Attributes contained in the first ball: default width/height, power up statuses to false(used to help with power up logic)
     *     , initial coordinates and angle, and the move direction attributes(moveX, moveY) .
     * </p>
     * @return a ball object to be used.
     */
    public Ball getFirstBallObject(){
        //set the parameters used to check if a power up is active to false at the start
        mBall.setActiveChangesSpeed(false);
        mBall.setActiveChangesSize(false);

        // set the first mBall object
        mBall.setX(randomBallVariables.randomX());
        mBall.setY(randomBallVariables.randomY());
        mBall.setAngle(randomBallVariables.randomAngle());
        mBall.setBallWidth(ballWidth);
        mBall.setBallHeight(ballHeight);
        mBall.setMoveX(1);  mBall.setMoveY(1);
        return mBall;
    }

    /**
     * Method that should be called whenever we want to get a new ball.
     * <p>
     *     This method should only change the ball's coordinates and angle, since some attributes
     *     should remain with the new ball if we used a specific power up.
     * </p>
     * @param currentBallType needs to be passed so we can set the right speed if we are setting the speed

     * @return a ball object with new coordinates and angle
     */
    public Ball getNewBallObject(Ball ball, int currentBallType){

        // If the speed of the new ball should be changed, check what type the ball is and set the
        //adequate speed for that ball type
        if(!ball.isActiveChangesSpeed()){
            ball = setBallSpeedByType(currentBallType, ball);
        }

        //if the size of the ball should be changed with a power up, increase its size by an amount
        //if it shouldn't be increased, just get the ball in teh regular way
        if(!ball.isActiveChangesSize()){
            ball.setBallWidth(ballWidth);
            ball.setBallHeight(ballHeight);
            ball = setBallColorByType(currentBallType, ball);
        }
        else{
            ball = setBallColorByType(currentBallType, ball);
            ball.setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
            ball.setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
            ball.setBallColor(Bitmap.createScaledBitmap(ball.getBallColor(),
                    ball.getBallWidth(),ball.getBallHeight(),false));
        }

        // set the first mBall object
        ball.setX(randomBallVariables.randomX());
        ball.setY(randomBallVariables.randomY());
        ball.setAngle(randomBallVariables.randomAngle());
        ball.setMoveX(1);
        ball.setMoveY(1);

        return ball;
    }

    /**
     * Method for getting an array of ballObjects based on the type of ball we passed, with coordinates, speed, angle, size.
     * Should be called only once to initialize the ball arrays
     * @param arraySize number of objects, 3 for purple and 7 for wave
     * @return
     */
    public Ball[] getFirstBallObjectArray(int arraySize){


            //if its a purple ball array
            if (arraySize == keys.PURPLE_BALL_NUMBER) {
                //if its purple we only need to set the first ball and set the other two to not be displayed
                x= randomBallVariables.randomX();
                y = randomBallVariables.randomY();
                purpleBalls[0].setX(x);
                purpleBalls[0].setY(y);
                purpleBalls[1].setX(-ballWidth);
                purpleBalls[1].setY(-ballHeight);
                purpleBalls[2].setX(-ballWidth);
                purpleBalls[2].setY(-ballHeight);
                angle = randomBallVariables.randomAngle();
                purpleBalls[0].setAngle(angle);
                for(i=0; i< arraySize; i++) {
                    purpleBalls[i].setBallSpeed(ballSpeed);
                    purpleBalls[i].setBallWidth(ballWidth);
                    purpleBalls[i].setBallHeight(ballHeight);
                    purpleBalls[i].setMoveX(1);
                    purpleBalls[i].setMoveY(1);
                }
                return purpleBalls;
            }
            else if(arraySize == keys.WAVE_BALL_NUMBER){
                waveBalls[0].setY(ballHeight*2);
                waveBalls[0].setX(0);
                waveBalls[0].setBallWidth(ballWidth);
                waveBalls[0].setBallHeight(ballHeight);
                purpleBalls[0].setBallSpeed(ballSpeed);
                for(i=1; i< arraySize; i++) {
                    //every wave ball starts at the left
                    waveBalls[i].setX(0);
                    //initialize the wave balls so they are at a distance
                    waveBalls[i].setBallWidth(ballWidth);
                    waveBalls[i].setBallHeight(ballHeight);
                    waveBalls[i].setY(waveBalls[i-1].getY() + ballHeight + 10);

                    waveBalls[i].setMoveX(1);
                    waveBalls[i].setMoveY(1);
                    //every next ball moves faster
                    waveBalls[i].setBallSpeed(keys.DEFAULT_BALL_SPEED + i);
                    }
                return waveBalls;
                }

       return null;
    }

    /**
     * Method for getting a new ball object array that has just new coordinates and angle, but any other attribute that
     * might have been affected by some power ups remain unchanged and should be changed elsewhere
     * @param arraySize determines if its a purple ball or a wave ball
     * @param balls the array of balls to be changed
     * @return
     */
    public Ball[] getNewBallObjectArray(int arraySize, Ball[] balls) {


        //if its a purple ball array
        if (arraySize == keys.PURPLE_BALL_NUMBER) {
            //if its purple we only need to set the first ball, and set the other two to not be displayed
            x= randomBallVariables.randomX();
            y = randomBallVariables.randomY();
            balls[0].setX(x);
            balls[0].setY(y);
            balls[1].setX(-ballWidth);
            balls[1].setY(-ballHeight);
            balls[2].setX(-ballWidth);
            balls[2].setY(-ballHeight);

            angle = randomBallVariables.randomAngle();
            balls[0].setAngle(angle);
            for(i=0; i< arraySize; i++) {
                balls[i].setMoveX(1);
                balls[i].setMoveY(1);
            }
        }
        else if(arraySize == keys.WAVE_BALL_NUMBER){
            balls[0].setY(ballHeight*2);
            balls[0].setX(0);
            balls[0].setMoveX(1);
            balls[0].setMoveY(1);
            for(i=1; i< arraySize; i++) {
                //every wave ball starts at the left
                balls[i].setX(0);

                balls[i].setY(balls[i - 1].getY() + ballHeight + 10);

                balls[i].setMoveX(1);
                balls[i].setMoveY(1);

            }
        }


        return balls;
    }
        /**
         * Important method that should be called before getting a new ball object to determine what object to get
         */
    public int getNewBallType(){
        return randomBallVariables.getRandomBallType();
    }



    /**
     * Method used to reset every ball object into its default state, aka removing power up's
     * @param balls the object that needs to be reset
     * @return a ball object that has the same angle and coordinates but its attributes are reset to its initial state;
     */
    public Ball[] resetBallArrayState(Ball[] balls, int arraySize){


        if(arraySize == keys.PURPLE_BALL_NUMBER){
            for(i=0; i<arraySize; i++){
                balls[i].setBallWidth(ballWidth);
                balls[i].setBallHeight(ballHeight);
                balls[i].setBallSpeed(ballSpeed);

            }
            return balls;
        }
        else{
            for(i=0; i<arraySize; i++){
                balls[i].setBallWidth(ballWidth);
                balls[i].setBallHeight(ballHeight);
                balls[i].setBallSpeed(ballSpeed);
            }
            return balls;
        }

    }

    private Ball setBallSpeedByType(int currentBallType, Ball ball){

        switch(currentBallType){

            case GameClassicActivity.BALL_GREEN:
                ball.setBallSpeed(keys.GREEN_BALL_SPEED);
                break;
            case GameClassicActivity.BALL_YELLOW:
                ball.setBallSpeed(keys.BALL_YELLOW_INITIAL_SPEED);
                break;
            //most balls have this default speed, the other two cases are for green and yellow ball's
            default:
                ball.setBallSpeed(keys.DEFAULT_BALL_SPEED);
        }
        return ball;
    }

    private Ball setBallColorByType(int currentBallType, Ball ball){

        if(currentBallType == GameClassicActivity.BALL_RED){
            ball.setBallColor(redBall);
        }

        // RED BALL
        if(currentBallType == GameClassicActivity.BALL_BLUE){
            ball.setBallColor(blueBall);

        }

        // YELLOW BALL
        if(currentBallType == GameClassicActivity.BALL_YELLOW){
            ball.setBallColor(yellowBall);
            ball.setBallColor(Bitmap.createScaledBitmap(ball.getBallColor(),
                    ballWidth + keys.YELLOW_BALL_INITIAL_SIZE, ballHeight+ keys.YELLOW_BALL_INITIAL_SIZE, true));
        }

        // GREEN BALL
        if(currentBallType == GameClassicActivity.BALL_GREEN){
            ball.setBallColor(greenBall);

        }
        return ball;

    }
}
