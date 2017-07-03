package com.trampatom.game.trampatom.ball;


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

    int i;
    int x,y,ballType;
    double angle;
    int ballSpeed, yellowBallSpeed, greenBallSpeed;
    int ballWidth, ballHeight;
    Keys keys;
    Ball mBall;
    Ball[] purpleBalls = {null,null,null};
    Ball[] waveBalls = {null,null,null,null,null,null,null};


    public static final int GOLD_BALL_DRAW = 1;
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
     * Method for getting the ball. It should be used to get a ball object witch is later manipulated with.
     * Only one ball object should be used at a time unless in special cases
     * @return a ball object to be used.
     */
    public Ball getFirstBallObject(){

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
}
