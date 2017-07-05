package com.trampatom.game.trampatom.activity;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.BallHandler;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Background;
import com.trampatom.game.trampatom.canvas.CanvasGameClassic;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.PowerUps;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.SoundsAndEffects;

import static java.lang.Thread.sleep;

/**
 * Class used to run the Classic game mode. It uses energy that you get from balls to keep the game running.
 * Whenever changing any ball's properties, probablly change code in: move method, draw method, new ball method
 */
public class GameClassicActivity extends AppCompatActivity implements Runnable, View.OnTouchListener, View.OnClickListener{

    //RED - don't click on the ball ; BLUE - click on the ball
    //GREEN - super crazy ball ; YELLOW - click it a few times
    //PURPLE splits into two after first click
    public static final int BALL_RED = 1;
    public static final int BALL_BLUE = 2;
    public static final int BALL_GREEN = 3;
    public static final int BALL_YELLOW = 4;
    public static final int BALL_PURPLE = 5;
    public static final int BALL_WAVE = 6;


    // ------------------- General Ball Variables --------------------------------------- \\

        //Used for scoring
            //determines what ball will be/is currently drawn
                int ballType=4, currentBall;
        //coordinates of the currently drawn ball, coordinates where we clicked
                int  clickedX;
                int  clickedY;
        //Balls and Background Bitmaps
                Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall;


    // ------------------- Ball type variables ------------------------------------------ \\

        int currentBallType;
        //used for yellow ball;
                int timesClicked;
                int yellowBallSpeed;
                boolean changedSize=false;
        //used for purple ball
            //initially there is only one purple ball
                int ballPurpleNumber =1;
            //used for either drawing only one ball or multiple balls
                int timesClickedPurple;
            //used to determine how many and what ball to draw on screen
                boolean originalBallClicked= false; boolean secondBallClicked=false; boolean thirdBallClicked=false;
        //used for wave ball
                int currentWaveBall = 0;


    // ------------------- Classes ------------------------------------------------------ \\

        Keys keys;
        GameTimeAndScore gameTimeAndScore;
        RandomBallVariables randomCoordinate;
        CanvasGameClassic canvas;
        HighScore highScore;
        ClickedABall clickedABall;
        BallMovement ballMovement;
        Random random;
        Background stars;
        AtomPool atomPool;
        Ball ballObject;
        BallHandler ballHandler;
        SoundsAndEffects soundsAndEffects;
        SoundPool soundPool;
        PowerUps powerUps;
        ProgressBar energyProgress;


    // ------------------- Arrays ------------------------------------------------------- \\

        Ball[] multipleBalls = {null,null,null,null,null,null,null};
        Ball[] purpleBallObjects = {null, null, null};
    //array for moving the ball
            int i;
        //array for wave balls
            Bitmap[] waveBall;


    // ------------------- Game Variables ----------------------------------------------- \\


        //width and height of the canvas
            int width, height;
        //every ball should have the same width and height, but we can change this if needed
            static int ballWidth, ballHeight;
        //used for displaying the score and setting new highScore at the end of the game
            int score=0, previousHighScore;
        //used to determine how long we will play
            int currentEnergyLevel;
        //used for ending the game when the time ends, congratulations if new high score
            boolean gameover, newHighScore;

        //used for sounds
            boolean lowEnergy = false;


    // -------------------------------- Power Up and Shop ---------------------------------- \\

        //For adding atoms to the Atom pool at end of game
            //contains 5 elements, blue, red, green, yellow and purple atoms to be filled during game
            int[] poolArray = {0,0,0,0,0};
        //For activating the power ups. One button has a cooldown the other one is a "one time use only"
            Button bPowerUp1, bPowerUp2;
            int selectedPowerUp1, selectedPowerUp2;
        //determines if the power up is energy or ball related
            int flagTypePowerUp1, flagTypePowerUp2;
        //For managing weather or not we can activate any of the power ups(in cooldown or used)
            boolean usedConsumable = false; boolean timeConsumable = false;
            boolean onCooldown = false;
            // the duration of every coolDown, timer used to help determine when the coolDown expired
            int coolDown, coolDownTimer=0, consumable, consumableCountDownTimer=0;



    // ------------------- Surface View ------------------------------------------------- \\

        //For the SurfaceView to work
            SurfaceHolder ourHolder;
            SurfaceView mSurfaceView;
            Canvas mCanvas;
            Thread ourThread = null;
            boolean isRunning=true;
        //used for drawing the first ball , used to start the timer once
            boolean initialDraw, startedTimer;


    // ---------------------------------------------------------------------------------- \\


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game);
        init();
    }


    // --------------------------- Initialization ------------------------------- \\

    private void init() {

        random = new Random();
        keys = new Keys();
        atomPool = new AtomPool(this);

        // SOUNDS
            //get a sound pool instance with all the required sounds
            //sound id's are located inside SoundsAndEffects object
            soundsAndEffects = new SoundsAndEffects(this);
            soundPool = soundsAndEffects.getGameClassicSounds();

        //GAME VIEWS
            //surface view
            mSurfaceView = (SurfaceView) findViewById(R.id.SV1);
            ourHolder = mSurfaceView.getHolder();
            mSurfaceView.setOnTouchListener(this);
            // progress bar
            energyProgress = (ProgressBar) findViewById(R.id.pbEnergy) ;
            gameTimeAndScore = new GameTimeAndScore(energyProgress);
            currentEnergyLevel = keys.STARTING_ENERGY;

        //buttons
            bPowerUp1 = (Button) findViewById(R.id.bPowerUp1);
            bPowerUp2 = (Button) findViewById(R.id.bPowerUp2);
            bPowerUp1.setOnClickListener(this);
            bPowerUp2.setOnClickListener(this);
        //TODO use different times for cooldown and duration
            coolDown = keys.POWER_UP_COOLDOWN;
            consumable = keys.POWER_UP_COOLDOWN;

        // OTHER
        //get device's width and height
            width= MainActivity.getWidth();
            height = MainActivity.getHeight();
        //set and resize all the bitmaps
            initiateBitmaps();

        //POWER UPS
        powerUps = new PowerUps(energyProgress,keys, ballWidth, ballHeight);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedPowerUp1 = preferences.getInt(Keys.KEY_POWER_UP_ONE, 0);
        selectedPowerUp2 = preferences.getInt(Keys.KEY_POWER_UP_TWO, 0);
        flagTypePowerUp2 = powerUps.checkCurrentFlagType(selectedPowerUp2);
        flagTypePowerUp1 = powerUps.checkCurrentFlagType(selectedPowerUp1);


        //HIGH SCORE
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
            newHighScore=false;

        //BALL
            //sets the required times to click a yellow ball and its speed
            timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
            yellowBallSpeed = keys.BALL_YELLOW_INITIAL_SPEED;
            // sets only one purple ball to be displayed initially
            timesClickedPurple=keys.BALL_PURPLE_NO_CLICK;
        //the first ball is always blue;
            currentBall= BALL_BLUE;
            initialDraw=true;
    }

    /**
     * Method for decoding every Bitmap into memory and
     * rescaling every ball into a certain size.
     * Gets a standard ball height and width variable to be used
     */
    private void initiateBitmaps(){
        //initiate bitmaps
            waveBall = new Bitmap[7];
        BitmapFactory.Options options = new BitmapFactory.Options();
        //used to rescale bitmaps without creating a new bitmap
            //options.inSampleSize = 4;
        //configure the color patter so that the balls take less space
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        //loading the bitmaps
        // TODO change so that the bitmaps are decoded inside Ball Handler, to prevent parsing every ball there

            waveBall[0] = BitmapFactory.decodeResource(getResources(), R.drawable.wave1, options);
            waveBall[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wave2, options);
            waveBall[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wave3, options);
            waveBall[3] = BitmapFactory.decodeResource(getResources(), R.drawable.wave4, options);
            waveBall[4] = BitmapFactory.decodeResource(getResources(), R.drawable.wave5, options);
            waveBall[5] = BitmapFactory.decodeResource(getResources(), R.drawable.wave6, options);
            waveBall[6] = BitmapFactory.decodeResource(getResources(), R.drawable.wave7, options);
            blueBall = BitmapFactory.decodeResource(getResources(), R.drawable.atomplava, options);
            redBall = BitmapFactory.decodeResource(getResources(), R.drawable.atomcrvena, options);
            greenBall = BitmapFactory.decodeResource(getResources(), R.drawable.atomzelena, options);
            yellowBall = BitmapFactory.decodeResource(getResources(), R.drawable.atomzuta, options);
            purpleBall = BitmapFactory.decodeResource(getResources(), R.drawable.atomroze, options);
            //ball Height and Width
            ballHeight = blueBall.getHeight() + keys.BALL_SIZE_ADAPT;
            ballWidth = blueBall.getWidth() + keys.BALL_SIZE_ADAPT;
            //resize all balls to the new ball width and height
            blueBall = Bitmap.createScaledBitmap(blueBall, ballWidth, ballHeight, true);
            redBall = Bitmap.createScaledBitmap(redBall, ballWidth, ballHeight, true);
            greenBall = Bitmap.createScaledBitmap(greenBall, ballWidth, ballHeight, true);
            yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
            purpleBall = Bitmap.createScaledBitmap(purpleBall, ballWidth, ballHeight, true);
            for (i = 0; i < keys.WAVE_BALL_NUMBER; i++) {
                waveBall[i] = Bitmap.createScaledBitmap(waveBall[i], ballWidth, ballHeight, true);
            }
    }


    // --------------------------- Main Game Loop ------------------------------- \\

    @Override
    public void run() {

        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;


            //constantly update the energy bar to display remaining energy
            gameTimeAndScore.updateEnergyBar(currentEnergyLevel);
            //start the game time
            timedActions();
            //The initial draw
            if(initialDraw) {
                initiateOnCanvas();
            }

            moveAndDraw();
            //after the timer runs out finish the game
                endGame();
        }
    }


            // ------------------------------- Start-Up -------------------------- \\

    /**
     * Method for handling the game's timer. Counts down and upon finishing the timer
     * we finish the game and check if we set a new high score.
     * <p><note> It's possible to put a loop on the timer by removing a comment in onFinish </note></p>
     */
    public void timedActions() {
        //timer has to be started once, but it resets every time it finishes
        if (!startedTimer) {
            startedTimer = true;
            //get a handler so that we can run the timer outside of the main ui thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    new CountDownTimer(keys.GAME_TIME, 100) {
                        public void onTick(long millisUntilFinished) {
                            //seconds remaining and the starting time
                            //int seconds =(int) millisUntilFinished/1000;
                            //int totalTimerTime = (int) keys.GAME_TIME/1000;

                            //COOLDOWN POWER UP
                            if(onCooldown ){

                                if(coolDown >  (coolDownTimer)/1000){
                                    // has to increment equally to the millis interval of ticks
                                    coolDownTimer += 100;
                                }
                                else{

                                    onCooldown = false;
                                    coolDownTimer = 0;
                                    //if the power up is ball related, reset the balls
                                    // , if its not, there is no need to reset the balls
                                    if(flagTypePowerUp1 == Keys.FLAG_BALL_POWER_UP) {
                                        //reset the ball objects after the power up expires
                                        ballObject = powerUps.resetBallState(ballObject, selectedPowerUp1, currentBallType);
                                        purpleBallObjects = powerUps.resetBallObjectArrayState(purpleBallObjects,
                                                selectedPowerUp1, keys.PURPLE_BALL_NUMBER, currentBallType);
                                        multipleBalls = powerUps.resetBallObjectArrayState(multipleBalls,
                                                selectedPowerUp1, keys.WAVE_BALL_NUMBER, currentBallType);
                                    }
                                    }
                            }

                            //CONSUMABLE POWER UP DURATION
                            //if the power up is ball related, reset the balls
                            // , if its not, there is no need to reset the balls
                            if(flagTypePowerUp2 == Keys.FLAG_BALL_POWER_UP){
                            if(timeConsumable ) {

                                if (consumable > (consumableCountDownTimer) / 1000) {
                                    // has to increment equally to the millis interval of ticks
                                    consumableCountDownTimer += 100;
                                } else {
                                    timeConsumable = false;
                                    consumableCountDownTimer = 0;
                                    //reset the ball objects after the power up expires
                                    ballObject = powerUps.resetBallState(ballObject, selectedPowerUp2, currentBallType);
                                    purpleBallObjects = powerUps.resetBallObjectArrayState(purpleBallObjects,
                                            selectedPowerUp2, keys.PURPLE_BALL_NUMBER, currentBallType);
                                    multipleBalls = powerUps.resetBallObjectArrayState(multipleBalls,
                                            selectedPowerUp2, keys.WAVE_BALL_NUMBER, currentBallType);
                                }
                            }
                            }

                            //Keep reducing energy until the game is over
                            if(!gameover) {
                                if (currentEnergyLevel < keys.STARTING_ENERGY/4 && !lowEnergy){
                                    soundPool.play(soundsAndEffects.soundNearlyGameOverId,1,1,3,0,1);
                                    lowEnergy = true;
                                }
                                //in case we exceed the maximum energy level, set it to the maximum
                                if(currentEnergyLevel >= GameTimeAndScore.MAX_BALL_CLICK_TIME)
                                    currentEnergyLevel = GameTimeAndScore.MAX_BALL_CLICK_TIME;
                                //until the game is finished keep lowering the energy levels
                                currentEnergyLevel -= keys.ENERGY_DECREASE;
                            }
                        }

                        public void onFinish() {
                            //when the timer finishes, end the game
                                //gameover = true;
                            //uncomment if the timer has to be reset every time it reaches zero
                                startedTimer = false;
                        }
                    }.start();
                }
            });
        }
    }

    /**
     * <p>Method for the initial canvas draw. Initiate most class instances here.</p>
     * <p>Set the initial angles and coordinates of balls, and draw everything.</p>
     * This method should be run only once
     */
    private void initiateOnCanvas(){
        //needed to get the height of the canvas
        mCanvas = ourHolder.lockCanvas();
        height = mCanvas.getHeight();
        ourHolder.unlockCanvasAndPost(mCanvas);
        // object instances
        randomCoordinate = new RandomBallVariables(width, height, ballWidth, ballHeight);
        stars = new Background(ourHolder, mCanvas, width, height);
        canvas = new CanvasGameClassic(ourHolder,mCanvas, stars);

        //get every ball object when starting a game
        ballHandler = new BallHandler(randomCoordinate, keys, ballWidth, ballHeight);
        ballHandler.parseBallBitmaps(redBall, blueBall, greenBall , yellowBall , purpleBall, waveBall);
        ballObject = ballHandler.getFirstBallObject();
        purpleBallObjects = ballHandler.getFirstBallObjectArray(keys.PURPLE_BALL_NUMBER);
        multipleBalls = ballHandler.getFirstBallObjectArray(keys.WAVE_BALL_NUMBER);
        //the colors of purple and wave don't have to be be changed so initiate them once
        for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
            multipleBalls[i].setBallColor(waveBall[i]);
        }
        for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
            purpleBallObjects[i].setBallColor(purpleBall);
        }
        getNewBall();

        clickedABall = new ClickedABall(ballWidth, ballHeight);
        ballMovement = new BallMovement(width, height);
       setCurrentBall(ballObject.getBallType());
       initialDraw = false;
    }


            // ------------------------------- Ball Movement -------------------------- \\

    /**
     * <p>Method that should be run in a constant loop.</p>
     * <p>It moves and draws specific ball types depending on what is the current ball type.</p>
     * <p>All the move methods should be used here used here</p>
     */
    public void moveAndDraw(){
        if(currentEnergyLevel<=0) {
            //after we run out of energy, end the game
            gameover = true;
        }
        switch(currentBall)
        {
            case BALL_BLUE:
                moveBall();
                canvas.draw(ballObject, -1, score);
                break;
            case BALL_RED:
                moveBall();
                canvas.draw(ballObject, -1, score);
                break;
            case BALL_YELLOW:
                if(!changedSize) {
                    //resets the yellow ball to its original size when first drawing it
                    ballObject.setBallWidth(ballWidth + keys.YELLOW_BALL_INITIAL_SIZE);
                    ballObject.setBallHeight(ballHeight + keys.YELLOW_BALL_INITIAL_SIZE);
                    ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(), ballObject.getBallWidth(), ballObject.getBallHeight(), true));
                    changedSize=true;
                }
                moveYellowBall();
                canvas.draw(ballObject, -1, score);
                break;
            case BALL_GREEN:
                //this ball moves like crazy
                moveGreenBall();
                canvas.draw(ballObject, -1, score);
                break;
            case BALL_PURPLE:
                movePurpleBall();
                canvas.drawPurple(purpleBallObjects,-1, score, timesClicked);
                break;
            case BALL_WAVE:
                moveWave();
                canvas.drawWave(multipleBalls, -1, score, currentWaveBall);
                break;
        }
    }

    /**
     * <p>Method for moving most balls that use regular x and y coordinates.</p>
     * <p>it gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * These balls should move at a constant speed and don't change their angle
     */
    private void moveBall() {
        ballObject = ballMovement.moveBall(ballObject);
    }
    /**
     * <p>Method for moving yellow balls.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * Yellow balls reduce in size and increase its speed on every click
     */
    private void moveYellowBall(){
        ballObject = ballMovement.moveBall(ballObject);
    }
    /**
     * <p>Method for moving green ball by changing its x and y coordinates.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * This ball has a random chance to change its angle.
     */
    private void moveGreenBall(){
        ballObject = ballMovement.moveGreenBall(ballObject);
    }
    /**
     * <p>Method for moving purple balls by changing every pair of x and y coordinates at the same time.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for every ball.</p>
     * <p>This ball is initially one, but after we click it, it should split into three balls.</p>
     * <p>If one of the three balls is clicked it should disappear</p>
     */
    private void movePurpleBall(){

        //depending on if we clicked the first purple ball we will move only one ball or all three balls
        for(i=0; i<ballPurpleNumber; i++){
            purpleBallObjects[i] = ballMovement.moveBall(purpleBallObjects[i]);
        }
        //draw three balls when they are clicked
        if(timesClickedPurple==keys.BALL_PURPLE_ONE_CLICK) {
                //the above for loop will now move three set of coordinates instead of just one
                    ballPurpleNumber = keys.PURPLE_BALL_NUMBER;
                //remove the balls that were clicked from the screen and stop moving them
                if (originalBallClicked)
                    purpleBallObjects[0].setX(-ballWidth-10);

                if (secondBallClicked) {
                    purpleBallObjects[1].setX(-ballWidth-10);
                }
                if (thirdBallClicked) {
                    purpleBallObjects[2].setX(-ballWidth-10);
                }
        }
    }

    /**
     * <p>Method for moving wave of balls by changing sets of x and y coordinates of balls that weren't yet clicked</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for every ball that wasn't clicked.</p>
     * After we click one ball it should disappear from the screen. Balls should be clicked in order.
     */
    private void moveWave(){
        //draw and move number of balls equal to the difference of total balls and the balls we clicked
        //current wave ball is the ball we should click, move every next ball after the current ball
        //every ball before the current ball will not be moved or drawn
        for(i=currentWaveBall; i<keys.WAVE_BALL_NUMBER; i++){
            multipleBalls[i] = ballMovement.moveWave(multipleBalls[i]);
        }
    }


            // ------------------------------ Game End --------------------------------- \\

    /**
     * <p>Should be called when we want to end the game.</p>
     * <p>After this method is called we should get a screen that shows us our score and if we got a new high score.</p>
     * <p>This screen should be shown for a few seconds and then we are returned to the main menu.</p>
     */
    private void endGame(){
        //if an event happens that changes gameover to true, end the game
        if (gameover) {
            GameOver gameover = new GameOver(ourHolder,mCanvas);
            //plat a sound once the game is over
            soundPool.play(soundsAndEffects.soundGameOverId,1,1,2,0,1);
            // check if we got a new high score
            newHighScore=highScore.isHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY, score);
            //display our score and if we got a new high score show a text to indicate that
            gameover.gameOver(score, newHighScore);
            //add the atoms we collected during the game to the atom pool
            //TODO save atoms in on pause or on stop method
            atomPool.addAtoms(poolArray);
            try {
                //delay before finishing the game
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    }


    // ---------------------------------- Main Game Action ----------------------------- \\

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //get coordinates where we touched
            clickedX = (int) event.getX();
            clickedY = (int) event.getY();
            currentBallType = setCurrentBall(ballType);
            //if we click on the ball do something depending on the ball type
                if(currentBallType == BALL_RED){
                    redBall();
                }
                if(currentBallType == BALL_BLUE){
                    blueBall();
                }
                if(currentBallType == BALL_YELLOW){
                    yellowBall();
                }
                if(currentBallType == BALL_GREEN){
                    greenBall();
                }
                if(currentBallType == BALL_PURPLE){
                    purpleBall();
                }
                if(currentBallType == BALL_WAVE){
                    waveBall();
                }
            setCurrentBall(ballType);
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bPowerUp1:
                //if the power up isn't in coolDown, activate it
                if(!onCooldown){
                    // DO SOMETHING

                    // if the power up does something to the ball
                    if(flagTypePowerUp1 == Keys.FLAG_BALL_POWER_UP) {
                        ballObject = powerUps.activateBallObjectConsumablePowerUp(ballObject, selectedPowerUp1);
                        purpleBallObjects = powerUps.activateBallObjectArrayConsumablePowerUp(purpleBallObjects, selectedPowerUp1, keys.PURPLE_BALL_NUMBER);
                        multipleBalls = powerUps.activateBallObjectArrayConsumablePowerUp(multipleBalls, selectedPowerUp1, keys.WAVE_BALL_NUMBER);
                        //put the power up on coolDown, MANAGED IN TIMED ACTIONS METHOD
                        onCooldown = true;
                    }
                    //if the power up does something to the energy bar
                    else{
                        currentEnergyLevel += powerUps.energyPowerUp(selectedPowerUp1);
                        onCooldown = true;
                    }
                }
                break;
            case R.id.bPowerUp2:

                if(!usedConsumable) {
                    //DO SOMETHING
                    // if the power up does something to the ball
                    if(flagTypePowerUp2 == Keys.FLAG_BALL_POWER_UP) {
                        ballObject = powerUps.activateBallObjectConsumablePowerUp(ballObject, selectedPowerUp2);
                        purpleBallObjects = powerUps.activateBallObjectArrayConsumablePowerUp(purpleBallObjects, selectedPowerUp2, keys.PURPLE_BALL_NUMBER);
                        multipleBalls = powerUps.activateBallObjectArrayConsumablePowerUp(multipleBalls, selectedPowerUp2, keys.WAVE_BALL_NUMBER);
                        timeConsumable = true;
                        usedConsumable = true;
                    }
                    //if the power up does something to the energy bar
                    else{
                        currentEnergyLevel += powerUps.energyPowerUp(selectedPowerUp2);
                        usedConsumable = true;
                    }
                }
                break;
        }
    }


        // ------------------------------ Ball Actions --------------------------------- \\

    /**
     * used to set the ball type to be drawn
     * <p>@param ballType used to determine what color to draw.</p>
     */
    private int setCurrentBall(int ballType){
        //if we click on the ball do something depending on the ball type
        if(ballType<=keys.TYPE_BALL_RED_CHANCE){
            currentBall=BALL_RED;
        }
        if(ballType>keys.TYPE_BALL_RED_CHANCE && ballType<=keys.TYPE_BALL_BLUE_CHANCE){
            currentBall=BALL_BLUE;
        }
        if(ballType>keys.TYPE_BALL_BLUE_CHANCE && ballType<=keys.TYPE_BALL_YELLOW_CHANCE){
            currentBall=BALL_YELLOW;
        }
        if(ballType>keys.TYPE_BALL_YELLOW_CHANCE && ballType<=keys.TYPE_BALL_GREEN_CHANCE){
            currentBall=BALL_GREEN;
        }
        if(ballType>keys.TYPE_BALL_GREEN_CHANCE && ballType<=keys.TYPE_BALL_PURPLE_CHANCE){
            currentBall=BALL_PURPLE;
        }
        if(ballType>keys.TYPE_BALL_PURPLE_CHANCE && ballType<=keys.TYPE_BALL_WAVE_CHANCE){
            currentBall=BALL_WAVE;
        }
        return currentBall;
    }


    /**
     * <p> Method for handling red ball actions.</p>
     * Blue ball is classic, we get score by touching it. Gives us energy
     */
    private void blueBall(){
        //if we clicked the ball should gain score
        if(clickedABall.ballClicked(ballObject.getX(),ballObject.getY(),clickedX,clickedY)) {
            //add some energy and update it in the energy text view
            currentEnergyLevel +=100;
            score += 100;
            //add the atom to the atom pool
            soundPool.play(ballObject.getSoundId(),1,1,0,0,1);
            poolArray[0]++;
            getNewBall();
        }
    }

    /**
     *<p> Method for handling red ball actions.</p>
     * The red ball should reduce score if we touch it, and increase score if we don't touch it. Gives us energy
     */
    private void redBall(){
        //if we click the ball
        if(clickedABall.ballClicked(ballObject.getX(),ballObject.getY(),clickedX,clickedY)){
            //add some energy and update it in the energy text view
            currentEnergyLevel -=100;
            score -= 100;
            //add the atom to the atom pool, even if we scored negative the atom is added to the pool
            poolArray[1]++;
            soundPool.play(ballObject.getSoundId(),1,1,0,0,1);
            getNewBall();
        }
        else {
            //add some energy and update it in the energy text view
            currentEnergyLevel +=100;
            score+=100;
            //add the atom to the atom pool
            poolArray[1]++;
            soundPool.play(ballObject.getSoundId(),1,1,0,0,1);
            getNewBall();
        }
    }

    /**
     * <p>Method for handling green ball actions.</p>
     * The green ball should simply give us score when clicked, gives more score than blue ball and energy
     */
    private void greenBall(){
        if(clickedABall.ballClicked(ballObject.getX(),ballObject.getY(),clickedX,clickedY)){
            //add some energy and update it in the energy text view
            currentEnergyLevel +=400;
            score+=400;
            //add the atom to the atom pool
            poolArray[2]++;
            soundPool.play(ballObject.getSoundId(),1,1,0,0,1);
            getNewBall();
        }
    }

    /**
     * <p>Method for handling yellow ball actions.</p>
     * <p>The yellow ball should move really slow at first and speed up with
     * every click. It's size decreases also with every click</p>
     * After we have clicked it a certain amount of times, it should give us score and energy
     */
    private void yellowBall(){

        //every time the ball is clicked decrease its size and increase speed
        if(clickedABall.ballClicked(ballObject.getX(),ballObject.getY(),clickedX,clickedY)) {
            if (timesClicked > 0) {
                timesClicked--;
                //every time we click it, reduce its size and increase its speed
                ballObject.setBallWidth(ballObject.getBallWidth() - keys.BALL_YELLOW_SIZE_DECREASE);
                ballObject.setBallHeight(ballObject.getBallHeight() - keys.BALL_YELLOW_SIZE_DECREASE);
                ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(), ballObject.getBallWidth(), ballObject.getBallHeight(), true));
                if(ballObject.isActiveChangesSpeed())
                ballObject.setBallSpeed(ballObject.getBallSpeed() + keys.BALL_YELLOW_SPEED_INCREASE);

            }
            else {
                //add some energy and update it in the energy text view
                currentEnergyLevel +=500;
                score+=500;
                //add the atom to the atom pool
                poolArray[3]++;
                soundPool.play(ballObject.getSoundId(),1,1,0,0,1);
                getNewBall();
                //reset the yellow ball to its first state for later use
                timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
                ballWidth=ballHeight= blueBall.getWidth();
                changedSize=false;
            }
        }
    }

    /**
     *  <p>Method for handling purple ball actions.</p>
     *  <p>This ball is initially just one, but after we click it it should split into three.</p>
     *  <p>After we click it for the first time it should get random angles for every ball and then start moving them.</p>
     *  <p>Movement is handled in the main game loop with the movePurpleBall method</p>
     *  <p>After we click every ball it should disappear from the screen.</p>
     *  Score after we clicked all three balls and gain energy.
     */
    private void purpleBall() {
        //TODO better way of removing purple balls from drawing
        //TODO fix bug related to drawing purple balls when size power up increased
        if (timesClickedPurple == keys.BALL_PURPLE_NO_CLICK) {
            //if we clicked on the first/ original ball
            if (clickedABall.ballClicked(purpleBallObjects[0].getX(), purpleBallObjects[0].getY(), clickedX, clickedY)) {
                //used to determine how many balls to draw
                timesClickedPurple = keys.BALL_PURPLE_ONE_CLICK;
                //get new angles and set every ball to start moving from the sae spot
                purpleBallObjects[0].setAngle(randomCoordinate.randomAngle());
                purpleBallObjects[1].setAngle(randomCoordinate.randomAngle());
                purpleBallObjects[2].setAngle(randomCoordinate.randomAngle());
                //set th balls to start splitting from the clicked ball
                purpleBallObjects[1].setX(purpleBallObjects[0].getX());
                purpleBallObjects[1].setY(purpleBallObjects[0].getY());
                purpleBallObjects[2].setX(purpleBallObjects[0].getX());
                purpleBallObjects[2].setY(purpleBallObjects[0].getY());
                //add the atom to the atom pool
                poolArray[4]++;
                soundPool.play(purpleBallObjects[0].getSoundId(),1,1,0,0,1);
            }
        }
        //if we clicked on one of the split balls remove them from the screen
        else  {
            if(clickedABall.ballClicked(purpleBallObjects[0].getX(), purpleBallObjects[0].getY(), clickedX, clickedY)){
                purpleBallObjects[0].setX(-purpleBallObjects[0].getBallWidth());
                purpleBallObjects[0].setY(-purpleBallObjects[0].getBallHeight());
                //don't draw this ball
                originalBallClicked=true;
                //add the atom to the atom pool
                poolArray[4]++;
                soundPool.play(purpleBallObjects[0].getSoundId(),1,1,0,0,1);
            }
            if(clickedABall.ballClicked(purpleBallObjects[1].getX(), purpleBallObjects[1].getY(), clickedX, clickedY)){
                //don't draw this ball
                secondBallClicked=true;
                purpleBallObjects[1].setX(-purpleBallObjects[1].getBallWidth());
                purpleBallObjects[1].setY(-purpleBallObjects[1].getBallHeight());
                //add the atom to the atom pool
                poolArray[4]++;
                soundPool.play(purpleBallObjects[1].getSoundId(),1,1,0,0,1);
            }
            if(clickedABall.ballClicked(purpleBallObjects[2].getX(), purpleBallObjects[2].getY(), clickedX, clickedY)){

                //don't draw this ball
                thirdBallClicked=true;
                purpleBallObjects[2].setX(-purpleBallObjects[2].getBallWidth());
                purpleBallObjects[2].setY(-purpleBallObjects[2].getBallHeight());
                //add the atom to the atom pool
                poolArray[4]++;
                soundPool.play(purpleBallObjects[2].getSoundId(),1,1,0,0,1);
            }
            //if we clicked all three, score and get a new ball
            if(originalBallClicked && secondBallClicked && thirdBallClicked){
                purpleBallObjects[1].setX(-ballWidth);
                purpleBallObjects[1].setY(-ballHeight);
                purpleBallObjects[2].setX(-ballWidth);
                purpleBallObjects[2].setY(-ballHeight);

                ballPurpleNumber = keys.BALL_PURPLE_NO_CLICK;
                //reset to starting state
                originalBallClicked = secondBallClicked = thirdBallClicked = false;
                timesClickedPurple = keys.BALL_PURPLE_NO_CLICK;
                //add some energy and update it in the energy text view
                currentEnergyLevel +=500;
                score +=500;
                getNewBall();
            }
        }
        }

    /**
     * <p>Method for handling wave ball actions.</p>
     * <p>The wave balls move left and right in slightly different speeds from each other.</p>
     * <p>Should register what ball we clicked and if we clicked the current ball it will disappear making the next
     * ball in line the current ball.</p>
     * <p>On initial draw the current ball should be the first ball, and after we click all balls,
     * we score, gain energy and reset the current ball.</p>
     */
    private void waveBall(){
        //if we click the correct ball in the sequence remove it and get score
        if(clickedABall.ballClicked(multipleBalls[currentWaveBall].getX(), multipleBalls[currentWaveBall].getY(), clickedX, clickedY)){

            //gain more and more score and energy the more balls you click
            currentEnergyLevel += currentWaveBall*10;
            score += currentWaveBall*10;
            //adds a random atom to the pool every time we click a wave ball
            poolArray[random.nextInt(4)]++;
            soundPool.play(multipleBalls[0].getSoundId(),1,1,0,0,1);
            // next ball should be clicked
            currentWaveBall ++;
            if(currentWaveBall == keys.WAVE_BALL_NUMBER){
                currentWaveBall = 0;
                // to round up the gain; with *10 you gain 420 score total
                score -=20;
                getNewBall();
            }
        }
    }


        // ------------------------------ New Ball --------------------------------- \\

    /**
     * Method used for getting a new ball and setting a score
     */
    private void getNewBall() {
        //get a new ball with new coordinates and angle of movement

        ballType = ballHandler.getNewBallType();
        currentBallType = setCurrentBall(ballType);
        setBallObjectByType();
    }

    /**
     * helper method used to load the right bitmap into the ball based on its type.
     * <p>IMPORTANT: every ball NEEDS to have its speed and color specified or it will not move or be drawn</p>
     */
    private void setBallObjectByType(){
        // BLUE BALL
        if(currentBallType == BALL_RED){
            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
            ballObject.setSoundId(soundsAndEffects.soundClickedId);
        }

        // RED BALL
        if(currentBallType == BALL_BLUE){

            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
            ballObject.setSoundId(soundsAndEffects.soundClickedId);
        }

        // YELLOW BALL
        if(currentBallType == BALL_YELLOW){
            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
            ballObject.setSoundId(soundsAndEffects.soundClickedId);
        }

        // GREEN BALL
        if(currentBallType == BALL_GREEN){
            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
            ballObject.setSoundId(soundsAndEffects.soundClickedId);
        }

        // PURPLE BALL
        if(currentBallType == BALL_PURPLE){
            purpleBallObjects = ballHandler.getNewBallObjectArray(keys.PURPLE_BALL_NUMBER,purpleBallObjects, currentBallType);

            for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
                //set the speeds of the balls
                purpleBallObjects[i].setSoundId(soundsAndEffects.soundClickedId);

            }

        }

        //WAVE BALL
        if(currentBallType == BALL_WAVE){
            multipleBalls = ballHandler.getNewBallObjectArray(keys.WAVE_BALL_NUMBER,multipleBalls, currentBallType);
            currentWaveBall = 0;
            for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
                multipleBalls[i].setSoundId(soundsAndEffects.soundClickedId);
            }

        }
    }




    // ----------------------------------- Handling Threads and Music -------------------- \\
    public void pause()
    {
        isRunning=false;
        try{
            ourThread.join();//unistava thread
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
    public void resume()
    {
        isRunning=true;
        ourThread = new Thread(this);
        ourThread.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(MainActivity.svc);
        //after leaving this game we need to clear the memory from the stored bitmaps
        if(blueBall!=null)
        {
            blueBall.recycle();
            blueBall=null;
            redBall.recycle();
            redBall=null;
            greenBall.recycle();
            greenBall=null;
            yellowBall.recycle();
            yellowBall=null;
            for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
                waveBall[i].recycle();
                waveBall[i] = null;
            }
        }
        soundsAndEffects.releaseSoundPool(soundPool);
    }

}