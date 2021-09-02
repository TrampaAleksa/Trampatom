package com.trampatom.game.trampatom.ball;


import android.graphics.Bitmap;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.power.up.ChancePassivesAndEvents;
import com.trampatom.game.trampatom.utils.Angles;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import static com.trampatom.game.trampatom.ball.AtomId.BALL_BLUE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_GREEN;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_PURPLE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_RED;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_WAVE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_YELLOW;

/**
 * Class that should be used to handle ball types.
 * Contains methods for getting a new ball, changing ablls whenevr we get a new one, setting ball speeds and sizes.
 */
public class BallHandler {

    private Bitmap redBall,  blueBall,  greenBall,  yellowBall,  purpleBall; Bitmap[] waveBall;
    public static boolean yellowBallSpeedChangeActive;

    int i;
    int x,y,ballType;
    double angle;
    int ballSpeed, yellowBallSpeed, greenBallSpeed;
    int ballWidth, ballHeight;
    Keys keys;
    Ball mBall;
    ChancePassivesAndEvents chancePassivesAndEvents;
    private Random random;
    private int eventTriggerChance;

    Ball[] purpleBalls = {null,null,null};
    Ball[] waveBalls = {null,null,null,null,null,null,null};

    private RandomBallVariables randomBallVariables;

    /**
     * Constructor for BallHandler. We should pass in an instance of RandomBallVariables and keys.
     * Also the mBall width and height initially used
     */
    public BallHandler(Keys keys, int ballWidth, int ballHeight){
        this.randomBallVariables = new RandomBallVariables(ballWidth, ballHeight);
        this.keys = keys;
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
        random = new Random();
        mBall = new Ball();
        ballSpeed = keys.DEFAULT_BALL_SPEED;
        yellowBallSpeed = keys.BALL_YELLOW_INITIAL_SPEED;
        greenBallSpeed = keys.GREEN_BALL_SPEED;

        //initialize the ball arrays here , because we need to create the ball
        // objects initially for the first create
        for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
            purpleBalls[i] = new Ball();
        }
        for(i=0; i< numberOfWaveAtoms(); i++){
            waveBalls[i] = new Ball();
        }
    }

    /**
     * Method that parses a ChanceAndEvents object so that we can use it to do a random event based on our selected power up or on
     * our pre-determined random events when handling the balls. Any event not related to balls but that happens in some time should be
     * done in the timer of the main game.
     * <p>
     *     Using this method also loads the chance to trigger the event related to the selected power up into the ball handler.
     * </p>
     * @param chancePassivesAndEvents object needed to use some of its methods within the BallHandler.
     */
    public void parseChancePassivesAndEventsObject(ChancePassivesAndEvents chancePassivesAndEvents){
        this.chancePassivesAndEvents = chancePassivesAndEvents;
        eventTriggerChance = chancePassivesAndEvents.getPassivePowerUpEventChance();
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
     * Since we will manipulate with ball types during the game, we have to pass every bitmap so that we can set it inside the handler
     * Theese bitmaps will later be used to change the balls sizes and colors depending on a power up or type of the ball
     */
    public void parseBallBitmaps(BallBitmaps ballBitmaps){
        this.redBall = ballBitmaps.getRedBall();
        this.blueBall = ballBitmaps.getBlueBall();
        this.greenBall = ballBitmaps.getGreenBall();
        this.yellowBall = ballBitmaps.getYellowBall();
        this.purpleBall = ballBitmaps.getPurpleBall();
        this.waveBall = ballBitmaps.getWaveBall();
    }

    /**
     * Method that is used to set the default values of balls, like width, height or speed, based on selected passives
     * <p>
     *     NOTE: This method is used to change default Ball attributes, for passives that do not affect the ball, use other methods.
     * </p>
     * @param flag1 determines the Purple passive used, a value from keys
     * @param flag2 determines the Yellow passive used, a value from keys
     */
    public void setDefaultValuesUponPassives(int flag1, int flag2){

        switch(flag1){
            case Keys.FLAG_PURPLE_BIGGER_BALLS:
                resizeBitmapsToNewDefaultSize();
                break;
        }
        switch (flag2){
            case Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS:
               /* keys.DEFAULT_BALL_SPEED = 2;
                keys.BALL_YELLOW_INITIAL_SPEED = 0;
                keys.GREEN_BALL_SPEED = 5;*/
                break;
        }

    }

    /**
     * Method used to resize every bitmaps default size so that they are displayed with that new size
     * should be called if we changed the default ball size using a passive effect
     */
    private void resizeBitmapsToNewDefaultSize() {
        redBall = Bitmap.createScaledBitmap(redBall,ballWidth,ballHeight,true);
        greenBall = Bitmap.createScaledBitmap(greenBall,ballWidth,ballHeight,true);
        yellowBall = Bitmap.createScaledBitmap(yellowBall,ballWidth,ballHeight,true);
        blueBall = Bitmap.createScaledBitmap(blueBall,ballWidth,ballHeight,true);
        purpleBall = Bitmap.createScaledBitmap(purpleBall, ballWidth, ballHeight, true);
        for(i=0; i<numberOfWaveAtoms(); i++){
            waveBall[i]=  Bitmap.createScaledBitmap(waveBall[i], ballWidth, ballHeight, true);
        }
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
        mBall.setActiveChangesType(false);
        mBall.setActiveChangesAngle(false);

        // set the first mBall object
        mBall.setBallAtomValue(Keys.ATOM_DROP_INITIAL_VALUE);
        mBall.setX(randomBallVariables.randomX());
        mBall.setY(randomBallVariables.randomY());
        mBall.setAngle(getRandomAngle());
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

        ball.setBallAtomValue(Keys.ATOM_DROP_INITIAL_VALUE);

        // random chance for triggering the event. if it is triggered do this block of code
        if( random.nextInt(Keys.MAX_CHANCE_FOR_EVENT) < eventTriggerChance){

            if(chancePassivesAndEvents.getSelectedPassivePowerUpFlag() == Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS){

                    ball.setBallAtomValue(ball.getBallAtomValue()+ Keys.ATOM_DROP_VALUE_INCREASE);

            }
        }

        //in case we selected the active that sets the next few balls the same type, reduce the countdown.
        if(ball.isActiveChangesType()){

            if((keys.POWER_UP_SAME_TYPE_NEXT_BALL > 0)){
                keys.POWER_UP_SAME_TYPE_NEXT_BALL --;
            }
            //if we used up all the same type balls set the active to false.
            else{
                ball.setActiveChangesType(false);
            }

        }

        // If the speed of the new ball should be changed, check what type the ball is and set the
        //adequate speed for that ball type
        if(!ball.isActiveChangesSpeed()){
            ball = setBallSpeedByType(currentBallType, ball);
        }
        //in case a power up is active that slows the ball down, to prevent a bug set the initial speed
        else if (currentBallType == BALL_YELLOW && yellowBallSpeedChangeActive ){
            ball.setBallSpeed(0);
        }

        //if the size of the ball should be changed with a power up, increase its size by an amount
        //if it shouldn't be increased, just get the ball in teh regular way
        if(!ball.isActiveChangesSize()){
            ball.setBallWidth(ballWidth);
            ball.setBallHeight(ballHeight);
            ball = setBallColorByType(currentBallType, ball);
        }
        else{
            ball.setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
            ball.setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
            ball = setBallColorByType(currentBallType, ball);
           /* ball.setBallColor(Bitmap.createScaledBitmap(ball.getBallColor(),
                    ball.getBallWidth(),ball.getBallHeight(),false));*/
        }

        // set the first mBall object
        ball.setX(randomBallVariables.randomX());
        ball.setY(randomBallVariables.randomY());
        //if the angle of the ball shouldn't be changed put a random angle. If it should then its the
        //gravity pull power up and the angle is set to the center
        if(!ball.isActiveChangesAngle()){
            ball.setAngle(getRandomAngle());
        }
        else {
            ball.setAngle(randomBallVariables.centeredAngle(ball.getX(), ball.getY()));
        }
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
                angle = getRandomAngle();
                purpleBalls[0].setAngle(angle);
                for(i=0; i< arraySize; i++) {
                    purpleBalls[i].setActiveChangesSpeed(false);
                    purpleBalls[i].setActiveChangesSize(false);
                    purpleBalls[i].setActiveChangesType(false);
                    purpleBalls[i].setActiveChangesAngle(false);

                    purpleBalls[i].setBallAtomValue(Keys.ATOM_DROP_INITIAL_VALUE);
                    purpleBalls[i].setBallSpeed(ballSpeed);
                    purpleBalls[i].setBallWidth(ballWidth);
                    purpleBalls[i].setBallHeight(ballHeight);
                    purpleBalls[i].setMoveX(1);
                    purpleBalls[i].setMoveY(1);
                }
                return purpleBalls;
            }
            else if(arraySize == numberOfWaveAtoms()){
                waveBalls[0].setY(ballHeight/3);
                waveBalls[0].setX(0);
                waveBalls[0].setBallWidth(ballWidth);
                waveBalls[0].setBallHeight(ballHeight);
                waveBalls[0].setBallSpeed(ballSpeed);
                for(i=1; i< arraySize; i++) {
                    waveBalls[i].setActiveChangesSpeed(false);
                    waveBalls[i].setActiveChangesSize(false);
                    waveBalls[i].setActiveChangesType(false);
                    waveBalls[i].setActiveChangesAngle(false);

                    waveBalls[i].setBallAtomValue(Keys.ATOM_DROP_INITIAL_VALUE);
                    //every wave ball starts at the left
                    waveBalls[i].setX(0);
                    //initialize the wave balls so they are at a distance
                    waveBalls[i].setBallWidth(ballWidth);
                    waveBalls[i].setBallHeight(ballHeight);
                    waveBalls[i].setY(waveBalls[i-1].getY() + ballHeight + ballHeight/10);

                    waveBalls[i].setMoveX(1);
                    waveBalls[i].setMoveY(1);
                    //every next ball moves faster
                    waveBalls[i].setBallSpeed(waveBalls[0].getBallSpeed() + i);
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
     * @param currentBallType used to determine power up behavior and setting of ball type
     * @return
     */
    public Ball[] getNewBallObjectArray(int arraySize, Ball[] balls, int currentBallType) {


        for(int i=0; i<arraySize; i++){
            balls[i].setBallAtomValue(Keys.ATOM_DROP_INITIAL_VALUE);

            // random chance for triggering the event. if it is triggered do this block of code
            if( random.nextInt(Keys.MAX_CHANCE_FOR_EVENT) < eventTriggerChance){

                if(chancePassivesAndEvents.getSelectedPassivePowerUpFlag() == Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS){

                    balls[i].setBallAtomValue(balls[i].getBallAtomValue()+ Keys.ATOM_DROP_VALUE_INCREASE);

                }
                else if(chancePassivesAndEvents.getSelectedPassivePowerUpFlag() == Keys.FLAG_YELLOW_CHANCE_LIMITING_SQUARE){

                }

            }
        }



        if(balls[0].isActiveChangesType()){

            if((keys.POWER_UP_SAME_TYPE_NEXT_BALL > 0)){
                keys.POWER_UP_SAME_TYPE_NEXT_BALL --;
            }
            else{
                balls[0].setActiveChangesType(false);
            }

        }

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



            //angle = randomBallVariables.randomAngle();
            //balls[0].setAngle(angle);
            for(i=0; i< arraySize; i++) {

                // if a power up changes the angle set it differently ( gravity pull )
                if(!balls[i].isActiveChangesAngle()){
                    balls[i].setAngle(getRandomAngle());
                }
                else {
                    balls[i].setAngle(randomBallVariables.centeredAngle(balls[i].getX(), balls[i].getY()));
                }

                balls[i].setMoveX(1);
                balls[i].setMoveY(1);


                // If the speed of the new ball should be changed, check what type the ball is and set the
                //adequate speed for that ball type
                if(!balls[i].isActiveChangesSpeed()){
                    balls[i] = setBallSpeedByType(currentBallType, balls[i]);
                }

                //if the size of the ball should be changed with a power up, increase its size by an amount
                //if it shouldn't be increased, just get the ball in the regular way
                if(!balls[i].isActiveChangesSize()){
                    balls[i].setBallWidth(ballWidth);
                    balls[i].setBallHeight(ballHeight);
                    balls = setBallArrayColorByType(currentBallType, balls);
                }
                else{
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                    balls[i].setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                            balls[i].getBallWidth(),balls[i].getBallHeight(),false));
                }
            }
        }
        else if(arraySize == numberOfWaveAtoms()){
            balls[0].setY(ballHeight/3);
            balls[0].setX(0);
            balls[0].setMoveX(1);
            balls[0].setMoveY(1);
            // If the speed of the new ball should be changed, check what type the ball is and set the
            //adequate speed for that ball type
            if(balls[0].isActiveChangesSize())
            balls = setWaveSpeed(balls);
            for(int i=1; i< arraySize; i++) {


                //every wave ball starts at the left
                balls[i].setX(0);

                balls[i].setY(balls[i - 1].getY() + ballHeight + ballHeight/10);

                balls[i].setMoveX(1);
                balls[i].setMoveY(1);

                // if a power up changes the angle set it differently ( gravity pull )
                if(balls[i].isActiveChangesAngle()){
                    balls[i].setAngle(randomBallVariables.centeredAngle(balls[i].getX(), balls[i].getY()));
                }

                //if the size of the ball should be changed with a power up, increase its size by an amount
                //if it shouldn't be increased, just get the ball in teh regular way
                if(!balls[i].isActiveChangesSize()){
                    balls[i].setBallWidth(ballWidth);
                    balls[i].setBallHeight(ballHeight);
                    balls = setBallArrayColorByType(currentBallType, balls);
                }
                else{
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                    balls[i].setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                            balls[i].getBallWidth(),balls[i].getBallHeight(),false));
                }

            }
        }


        return balls;
    }


        /**
         * Important method that should be called before getting a new ball object to determine what object to get
         */
    public int getNewBallType(){
        return randomBallVariables.getRandomBallType();
        //return 12;
    }



    /**
     * Method used to reset every ball object into its default state, aka removing power up's
     * @param balls the object that needs to be reset
     * @return a ball object that has the same angle and coordinates but its attributes are reset to its initial state;
     */
    public Ball[] resetBallArrayState(Ball[] balls, int arraySize, int currentBallType){


        if(arraySize == keys.PURPLE_BALL_NUMBER){
            for(i=0; i<arraySize; i++){

                // If the speed of the new ball should be changed, check what type the ball is and set the
                //adequate speed for that ball type
                if(!balls[i].isActiveChangesSpeed()){
                    balls[i] = setBallSpeedByType(currentBallType, balls[i]);
                }

                //if the size of the ball should be changed with a power up, increase its size by an amount
                //if it shouldn't be increased, just get the ball in teh regular way
                if(!balls[i].isActiveChangesSize()){
                    balls[i].setBallWidth(ballWidth);
                    balls[i].setBallHeight(ballHeight);
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                }
                else{
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                    balls[i].setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                            balls[i].getBallWidth(),balls[i].getBallHeight(),false));
                }
            }
            return balls;
        }
        else{
            for(i=0; i<arraySize; i++){

                // If the speed of the new ball should be changed, check what type the ball is and set the
                //adequate speed for that ball type
                if(!balls[i].isActiveChangesSpeed()){
                    balls[i] = setBallSpeedByType(currentBallType, balls[i]);
                }

                //if the size of the ball should be changed with a power up, increase its size by an amount
                //if it shouldn't be increased, just get the ball in teh regular way
                if(!balls[i].isActiveChangesSize()){
                    balls[i].setBallWidth(ballWidth);
                    balls[i].setBallHeight(ballHeight);
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                }
                else{
                    balls[i] = setBallColorByType(currentBallType, balls[i]);
                    balls[i].setBallWidth(ballWidth+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallHeight(ballHeight+keys.POWER_UP_BALL_SIZE_INCREASE);
                    balls[i].setBallColor(Bitmap.createScaledBitmap(balls[i].getBallColor(),
                            balls[i].getBallWidth(),balls[i].getBallHeight(),false));
                }
            }
            return balls;
        }

    }

    /**
     * Method used to set different speeds for yellow, green and other balls
     * @param currentBallType determines what seed to set
     * @param ball the ball to set the speed to
     * @return a ball object with the appropriate speed
     */
    private Ball setBallSpeedByType(int currentBallType, Ball ball){

        switch(currentBallType){

            case BALL_GREEN:
                ball.setBallSpeed(keys.GREEN_BALL_SPEED);
                break;
            case BALL_YELLOW:
                ball.setBallSpeed(keys.BALL_YELLOW_INITIAL_SPEED);
                break;
            //most balls have this default speed, the other two cases are for green and yellow ball's
            default:
                ball.setBallSpeed(keys.DEFAULT_BALL_SPEED);
        }
        return ball;
    }

    private Ball setBallColorByType(int currentBallType, Ball ball){

        if(currentBallType == BALL_RED){
            ball.setBallColor(redBall);
        }

        // RED BALL
        if(currentBallType == BALL_BLUE){
            ball.setBallColor(blueBall);

        }

        // YELLOW BALL
        if(currentBallType == BALL_YELLOW){
            //if its a yellow ball set the width and height to be bigger by 50%
            ball.setBallColor(yellowBall);
            ball.setBallWidth(ballWidth + (ballWidth/2));
            ball.setBallHeight(ballHeight + (ballHeight/2));
            ball.setBallColor(Bitmap.createScaledBitmap(yellowBall,
                    ball.getBallWidth(), ball.getBallHeight(), true));
        }

        // GREEN BALL
        if(currentBallType == BALL_GREEN){
            ball.setBallColor(greenBall);

        }
        return ball;

    }

    private Ball[] setBallArrayColorByType (int currentBallType, Ball[] balls){

        // PURPLE BALL
        if(currentBallType == BALL_PURPLE){
            for(i=0; i<keys.PURPLE_BALL_NUMBER; i++) {
                balls[i].setBallColor(purpleBall);
            }
        }

        // WAVE BALL
        if(currentBallType == BALL_WAVE){
            for(i=0; i<numberOfWaveAtoms(); i++) {
                balls[i].setBallColor(waveBall[i]);
            }
        }


        return balls;
    }

    private Ball[] setWaveSpeed(Ball[] balls){

            for(int i=0; i<numberOfWaveAtoms();i++){

                balls[i].setBallSpeed(keys.DEFAULT_BALL_SPEED+i);

            }

            return balls;
    }

    private double getRandomAngle(){
        return Angles.randomAngle();
    }


    private int numberOfWaveAtoms(){
        return Keys.WAVE_BALL_NUMBER;
    }
}
