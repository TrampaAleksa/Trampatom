package com.trampatom.game.trampatom.currency;

import android.graphics.Bitmap;
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

public class PowerUps {
    Keys keys;
    ProgressBar progressBar;
    int i;
    int ballSpeed;
    int ballWidth, ballHeight;


    Ball[] purpleBalls = {null,null,null};
    Ball[] waveBalls = {null,null,null,null,null,null,null};



    /**
     * Since this class manipulates the progress bar in some cases we should pass the progress bar, and keys
     * for accessing flags of every power up.
     */
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

 // ---------------------------------------- ACTIVE BALL RELATED ------------------------------- \\

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

                    ballObject.setBallWidth(ballObject.getBallWidth()+keys.POWER_UP_BALL_SIZE_INCREASE);
                    ballObject.setBallHeight(ballObject.getBallHeight()+keys.POWER_UP_BALL_SIZE_INCREASE);
                    ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(),
                            ballObject.getBallWidth(),ballObject.getBallHeight(),false));
                    ballObject.setActiveChangesSize(true);

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
                        balls[i].setActiveChangesSpeed(true);
                    }
                    return balls;
                }
                else {
                    //set every ball's speed to 0 until it's reset
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallSpeed(0);
                        balls[i].setActiveChangesSpeed(true);
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
                        balls[i].setBallWidth( balls[i].getBallWidth()+keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallHeight( balls[i].getBallHeight()+keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallColor(Bitmap.createScaledBitmap( balls[i].getBallColor(),
                                balls[i].getBallWidth(), balls[i].getBallHeight(),false));
                        balls[i].setActiveChangesSize(true);
                    }
                    return balls;
                }
                else {
                    //set every ball's speed to 0 until it's reset
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallWidth( balls[i].getBallWidth()+keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallHeight( balls[i].getBallHeight()+keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallColor(Bitmap.createScaledBitmap( balls[i].getBallColor(),
                                balls[i].getBallWidth(), balls[i].getBallHeight(),false));
                        balls[i].setActiveChangesSize(true);
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

                ballObject.setBallWidth(ballObject.getBallWidth()-keys.POWER_UP_BALL_SIZE_INCREASE);
                ballObject.setBallHeight(ballObject.getBallHeight()-keys.POWER_UP_BALL_SIZE_INCREASE);
                    ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(),
                            ballObject.getBallWidth(), ballObject.getBallHeight(), false));
                    ballObject.setActiveChangesSize(false);

                break;
            case Keys.FLAG_GREEN_SLOW_DOWN_BALLS:
                ballObject.setBallSpeed(ballObject.getBallSpeed()-3);

                break;

            case Keys.FLAG_GREEN_UNKNOWN2:

                break;
        }

        return ballObject;
    }


    public Ball[] resetBallObjectArrayState(Ball[] balls, int flag, int arraySize, int currentBallType){

        switch(flag){
            // RED
            case Keys.FLAG_RED_FREEZE_BALLS:
                if(arraySize == keys.PURPLE_BALL_NUMBER){
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallSpeed(keys.DEFAULT_BALL_SPEED);
                        balls[i].setActiveChangesSpeed(false);
                    }
                    return balls;
                }
                else {
                    //reset every ball's speed to its default speed
                    for (i = 0; i < arraySize; i++) {
                        balls[i].setBallSpeed(keys.DEFAULT_BALL_SPEED+i);
                        balls[i].setActiveChangesSpeed(false);
                    }
                    return balls;
                }


            case Keys.FLAG_RED_LIMITING_SQUARE:



                break;
            case Keys.FLAG_RED_UNKNOWN2:

                break;

            //GREEN
            case Keys.FLAG_GREEN_INCREASE_BALL_SIZE:

                //reset every ball's size to its previous size
                if(arraySize == keys.PURPLE_BALL_NUMBER){
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallWidth(balls[i].getBallWidth()-keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallHeight(balls[i].getBallHeight()-keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                                balls[i].getBallWidth(), balls[i].getBallHeight(), false));
                        balls[i].setActiveChangesSize(false);
                    }
                    return balls;
                }
                else {
                    //reset every ball's size to its previous size
                    for(i=0; i< arraySize; i++){
                        balls[i].setBallWidth(balls[i].getBallWidth()-keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallHeight(balls[i].getBallHeight()-keys.POWER_UP_BALL_SIZE_INCREASE);
                        balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                                balls[i].getBallWidth(), balls[i].getBallHeight(), false));
                        balls[i].setActiveChangesSize(false);
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
     * Method used to set different speeds for yellow, green and other balls
     * @param currentBallType determines what seed to set
     * @param ball the ball to set the speed to
     * @return a ball object with the appropriate speed
     */
    //TODO we have a duplicate method in ball handler, perhaps refactor that
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



// ---------------------------------------- ACTIVE ENERGY BAR RELATED ------------------------------- \\

    /**
     * Method called when using a power up that manipulates the energy bar. returns the new energy value
     * @param flag the flag to determine what we are doing with the energy bar depending on the power up related
     *             to the flag we passed
     * @return an int value that is the new energy value for the energy bar
     */
    public int energyPowerUp(int flag) {
        int energyValue = 0;

        switch(flag){

            case Keys.FLAG_GREEN_SMALL_ENERGY_BONUS:
                energyValue += 500;
                break;
            case Keys.FLAG_RED_BIG_ENERGY_BONUS:
                energyValue += 2000;
                break;
        }


        return energyValue;
    }

    }







    // --------------------------------------- RED ----------------------------------------- \\




    // --------------------------------------- GREEN -------------------------------------------\\



    // ---------------------------------------- YELLOW ------------------------------------------ \\



    // ---------------------------------------- PURPLE ------------------------------------------- \\




