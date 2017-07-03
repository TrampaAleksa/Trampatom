package com.trampatom.game.trampatom.currency;

import android.widget.ProgressBar;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.activity.GameClassicActivity;
import com.trampatom.game.trampatom.utils.Keys;

/**
 * Important class that should contain all the methods for working with passive and active power Ups.
 * <p>
 *     Most power-ups work by getting a ball object/ object array and changing some attribute to the ball.
 *     Either for a limited time as determined inside the game class, or during the whole game as determined by a
 *     passive power up.
 * </p>
 * <p>
 *     Energy manipulation power - ups are a little different, and function by getting the game progress bar
 *     and changing it in a certain way, depending on what power up we used.
 * </p>
 */

/**
 * Since this class manipulates the progress bar in some cases we should pass the progress bar, and keys
 * for accessing flags of every power up.
 */
public class PowerUps {
    Keys keys;
    ProgressBar progressBar;
    int i;
    int ballSpeed;
    int ballWidth, ballHeight;


    Ball[] purpleBalls = {null,null,null};
    Ball[] waveBalls = {null,null,null,null,null,null,null};

    public PowerUps(ProgressBar progressBar, Keys keys, int ballWidth, int ballHeight){
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
        ballSpeed = keys.DEFAULT_BALL_SPEED;


        this.progressBar = progressBar;
        this.keys = keys;

        //initialize the ball arrays here to be used in methods
        for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
            purpleBalls[i] = new Ball();
        }
        for(i=0; i< keys.WAVE_BALL_NUMBER; i++){
            waveBalls[i] = new Ball();
        }
    }


    /**
     * Method that should be called when a power up is activated to change the attributes of every ball object
     * for a short time, so that any new ball object that appears has the same traits
     * @param ballObject the ball object to be changed
     * @param flag a flag used to determine what power up we used and based on that what to change in a ball
     * @return a changed ball object
     */
        public Ball activateBallObjectConsumablePowerUp(Ball ballObject, int flag){

            switch(flag){
        // RED
                case Keys.FLAG_RED_FREEZE_BALLS:
                    //set every ball's speed to 0 until it's reset
                    ballObject.setBallSpeed(0);
                    ballObject.setActiveChangesSpeed(true);

                    break;

                case Keys.FLAG_RED_LIMITING_SQUARE:



                    break;
                case Keys.FLAG_RED_UNKNOWN2:

                    break;

        //GREEN
                case Keys.FLAG_GREEN_INCREASE_BALL_SIZE:

                    ballObject.setBallWidth(ballObject.getBallWidth()+15);
                    ballObject.setBallHeight(ballObject.getBallHeight()+15);
                    break;
                case Keys.FLAG_GREEN_SLOW_DOWN_BALLS:
                    ballObject.setBallSpeed(ballObject.getBallSpeed()-3);

                    break;

                case Keys.FLAG_GREEN_UNKNOWN2:

                    break;
            }

            return ballObject;
        }

    public Ball[] activateBallObjectArrayConsumablePowerUp(Ball[] balls, int flag, int arraySize){

        switch(flag){
            // RED
            case Keys.FLAG_RED_FREEZE_BALLS:
                if(arraySize == keys.PURPLE_BALL_NUMBER){
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallSpeed(0);
                    }
                    return balls;
                }
                else {
                    //set every ball's speed to 0 until it's reset
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallSpeed(0);
                    }
                    return balls;
                }


            case Keys.FLAG_RED_LIMITING_SQUARE:



                break;
            case Keys.FLAG_RED_UNKNOWN2:

                break;

            //GREEN
            case Keys.FLAG_GREEN_INCREASE_BALL_SIZE:

                if(arraySize == keys.PURPLE_BALL_NUMBER){
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallWidth(balls[i].getBallWidth()+15);
                        balls[i].setBallHeight(balls[i].getBallHeight()+15);
                    }
                    return balls;
                }
                else {
                    //set every ball's speed to 0 until it's reset
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallWidth(balls[i].getBallWidth()+15);
                        balls[i].setBallHeight(balls[i].getBallHeight()+15);
                    }
                    return balls;
                }


            case Keys.FLAG_GREEN_SLOW_DOWN_BALLS:
                if(arraySize == keys.PURPLE_BALL_NUMBER){
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallSpeed(balls[i].getBallSpeed()-3);
                    }
                    return balls;
                }
                else {
                    //set every ball's speed to 0 until it's reset
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallSpeed(balls[i].getBallSpeed()-3);
                    }
                    return balls;
                }

            case Keys.FLAG_GREEN_UNKNOWN2:

                break;
        }

        return balls;
    }


    /**
     * Method used to reset every ball object into its default state, aka removing power up's
     * @param ballObject the object that needs to be reset.
     *                   <p>
     *                   NOTE: This method reverses any change made by the method for activating a power up.
     *                   </p>
     * @return a ball object that has the same angle and coordinates but its attributes are reset to its initial state;
     */
    public Ball resetBallState(Ball ballObject, int flag, int currentBallType){


        switch(flag){
            // RED
            case Keys.FLAG_RED_FREEZE_BALLS:
                    ballObject = setBallSpeedByType(currentBallType, ballObject);
                    ballObject.setActiveChangesSpeed(false);
                return ballObject;


            case Keys.FLAG_RED_LIMITING_SQUARE:



                break;
            case Keys.FLAG_RED_UNKNOWN2:

                break;

            //GREEN
            case Keys.FLAG_GREEN_INCREASE_BALL_SIZE:

                ballObject.setBallWidth(ballObject.getBallWidth()+15);
                ballObject.setBallHeight(ballObject.getBallHeight()+15);
                break;
            case Keys.FLAG_GREEN_SLOW_DOWN_BALLS:
                ballObject.setBallSpeed(ballObject.getBallSpeed()-3);

                break;

            case Keys.FLAG_GREEN_UNKNOWN2:

                break;
        }

        return ballObject;
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
    // --------------------------------------- RED ----------------------------------------- \\




    // --------------------------------------- GREEN -------------------------------------------\\



    // ---------------------------------------- YELLOW ------------------------------------------ \\



    // ---------------------------------------- PURPLE ------------------------------------------- \\




