package com.trampatom.game.trampatom.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallBitmaps;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.BallHandler;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Background;
import com.trampatom.game.trampatom.canvas.CanvasGameClassic;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.PowerUps;
import com.trampatom.game.trampatom.currency.ShopHandler;
import com.trampatom.game.trampatom.power.up.ChancePassivesAndEvents;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.PassivesManager;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.List;
import java.util.Random;

import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.SoundsAndEffects;

import static com.trampatom.game.trampatom.ball.AtomId.BALL_BLUE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_GREEN;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_PURPLE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_RED;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_WAVE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_YELLOW;
import static java.lang.Thread.sleep;

/**
 * Class used to run the Classic game mode. It uses energy that you get from balls to keep the game running.
 * Whenever changing any ball's properties, probablly change code in: move method, draw method, new ball method
 */
public class GameClassicActivity extends AppCompatActivity implements Runnable, View.OnTouchListener, View.OnClickListener{

    public static final int HEIGHT_SCALE = 11;


    // ------------------- General Ball Variables --------------------------------------- \\

        //Used for scoring
            //determines what ball will be/is currently drawn
                int ballType=4, currentBall;
        //coordinates of the currently drawn ball, coordinates where we clicked
                int  clickedX;
                int  clickedY;

    // ------------------- Ball type variables ------------------------------------------ \\

        int currentBallType;
        //used for yellow ball;
                int timesClicked;
                boolean changedSize=false;
        //used for purple ball
            //initially there is only one purple ball
                int ballPurpleNumber =1;
            //used for either drawing only one ball or multiple balls
                int timesClickedPurple=0;
            //used to determine how many and what ball to draw on screen
                boolean[] ballClicked = {false,false,false};
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
        PowerUps powerUps;
        ProgressBar energyProgress;
        List<PowerUpPool> powerUpPool;
        PowerUpPool[] powerUp= { null , null, null, null};
        PassivesManager passivesManager;
        ChancePassivesAndEvents chancePassivesAndEvents;
        BallBitmaps ballBitmaps;


    // ------------------- Arrays ------------------------------------------------------- \\

        Ball[] multipleBalls = {null,null,null,null,null,null,null};
        Ball[] purpleBallObjects = {null, null, null};
    //array for moving the ball
            int i;

    // ------------------- Game Variables ----------------------------------------------- \\


        //width and height of the canvas
            int width, height;
        //used for displaying the score and setting new highScore at the end of the game
            int score=0, previousHighScore;
        //used to determine how long we will play
            int currentEnergyLevel; int energySpeedUpTicks;
        //used for ending the game when the time ends, congratulations if new high score
            boolean gameover, newHighScore;

        //used for sounds
            boolean lowEnergy = false; boolean middleEnergy = false;


    // -------------------------------- Power Up and Shop ---------------------------------- \\

        //For adding atoms to the Atom pool at end of game
            //contains 5 elements, blue, red, green, yellow and purple atoms to be filled during game
            int[] poolArray = {0,0,0,0,0};
        //For activating the power ups. One button has a cooldown the other one is a "one time use only"
            Button bPowerUp1, bPowerUp2;
        //determines what power up and what passive effect we choose and does a certain action based on that
            int selectedPowerUp1, selectedPowerUp2;
            int selectedPassive1, selectedPassive2;
        //determines if the power up or passive is energy or ball related
            int flagTypePowerUp1, flagTypePowerUp2;
            int flagTypePassive1, flagTypePassive2;
        //For managing weather or not we can activate any of the power ups(in cooldown or used)
            boolean usedConsumable = false; boolean timeConsumable = false;
            boolean onCooldown = false;
        //determines if we have reset the power up. used to prevent zero cooldown on power up.
        boolean resettedPowerUp = false;
            // the duration of every coolDown, timer used to help determine when the coolDown expired
            int coolDown, coolDownTimer=0, powerUpDuration, consumable, consumableCountDownTimer=0;
        //For passive event chance power ups
            //chance of the passive event from happening; ticker used for the timer to prevent events happening too often
            int passivePowerUpEventChance, ticker;
            //used to activate an event if a passive event was triggered
            boolean isPassiveEventTriggerred= false;



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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        passivesManager = new PassivesManager();
        random = new Random();
        keys = new Keys();
        atomPool = new AtomPool(this);
        width= MainActivity.getWidth();
        height = MainActivity.getHeight();

        // SOUNDS
        initSounds();

        //GAME VIEWS
        initGameViews();

        //POWER UPS
        initPowerUps();

        //BALL BITMAPS
        initiateBitmaps();

        //HIGH SCORE
        initHighScore();

        //BALL
        initBall();
    }

    private void initSounds() {
        //get a sound pool instance with all the required sounds
        //sound id's are located inside SoundsAndEffects object
        soundsAndEffects = new SoundsAndEffects(this).getGameClassicSounds();
    }

    private void initGameViews() {
        //surface view
        mSurfaceView = (SurfaceView) findViewById(R.id.SV1);
        ourHolder = mSurfaceView.getHolder();
        //mSurfaceView.setOnTouchListener(ballTouchListener);
        mSurfaceView.setOnTouchListener(this);
        // progress bar
        energyProgress = (ProgressBar) findViewById(R.id.pbEnergy) ;
        energyProgress.getProgressDrawable().setTint(Color.GREEN);
        gameTimeAndScore = new GameTimeAndScore(energyProgress);
        currentEnergyLevel = keys.STARTING_ENERGY;
        energySpeedUpTicks = Keys.ENERGY_SPEED_UP_TICKS;
        //buttons
        bPowerUp1 = (Button) findViewById(R.id.bPowerUp1);
        bPowerUp2 = (Button) findViewById(R.id.bPowerUp2);
        bPowerUp1.setOnClickListener(this);
        bPowerUp2.setOnClickListener(this);
        coolDown = keys.POWER_UP_COOLDOWN;
        consumable = keys.POWER_UP_COOLDOWN;
    }

    private void initPowerUps() {
        ticker = Keys.TICKER_STARTING_VALUE;
        //load the power ups we selected in the shop from a file
        ShopHandler shopHandler = new ShopHandler(this);
        powerUpPool = shopHandler.loadSelectedPowerUps();
        for(i=0; i<4; i++){
            powerUp[i] = powerUpPool.get(i);
        }
        //set the selected power ups id-s to be used to determine what they do
        selectedPowerUp1 = powerUp[1].getId();
        selectedPowerUp2 =  powerUp[0].getId();
        selectedPassive1 =  powerUp[3].getId();
        selectedPassive2 =  powerUp[2].getId();
        //set flags to determine what type of power up it is (energy related, ball related etc)
        flagTypePowerUp2 = passivesManager.checkCurrentFlagType(selectedPowerUp2);
        flagTypePowerUp1 = passivesManager.checkCurrentFlagType(selectedPowerUp1);
        flagTypePassive1 = passivesManager.checkCurrentFlagType(selectedPassive1);
        flagTypePassive2 = passivesManager.checkCurrentFlagType(selectedPassive2);

        powerUpDuration = keys.POWER_UP_DURATION;

        chancePassivesAndEvents = new ChancePassivesAndEvents(powerUp[2]);
        //get the chance for an event bound to the selected power up to happen.
        passivePowerUpEventChance = chancePassivesAndEvents.getPassivePowerUpEventChance();
    }

    /**
     * Method for decoding every Bitmap into memory and
     * rescaling every ball into a certain size.
     * Gets a standard ball height and width variable to be used
     */
    private void initiateBitmaps(){

        int ballWidth, ballHeight;
        ballHeight = ballWidth = passivesManager.setNewBallSizeFromPassives(height/ HEIGHT_SCALE, selectedPassive1,
                selectedPassive2,powerUp[2].getCurrentLevel(), powerUp[3].getCurrentLevel());

        this.ballBitmaps = new BallBitmaps(this);
        ballBitmaps.setBallWidth(ballWidth);
        ballBitmaps.setBallHeight(ballHeight);
        ballBitmaps.initiateBitmaps();
    }

    private void initHighScore() {
        highScore = new HighScore(this);
        previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
        newHighScore=false;
    }

    private void initBall() {
        //sets the required times to click a yellow ball and its speed
        timesClicked=0;
        // sets only one purple ball to be displayed initially
        timesClickedPurple=keys.BALL_PURPLE_NO_CLICK;
        //the first ball is always blue;
        currentBall= BALL_BLUE;
        initialDraw=true;
        setCurrentBall(ballType);
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
     * Important method containing every timed events that happen in the game.
     * <p>
     *     Used to handle power up cooldown's and durations, reduction of the energy bar, finishing of the game or other timed events.
     * </p>
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
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void onTick(long millisUntilFinished) {
                            //seconds remaining and the starting time
                            //int seconds =(int) millisUntilFinished/1000;
                            //int totalTimerTime = (int) keys.GAME_TIME/1000;

                            //COOLDOWN POWER UP
                            cooldownPowerUpTimer();

                            //CONSUMABLE POWER UP DURATION
                            consumablePowerUpTimer();

                            //ENERGY AND GAME OVER
                            energyAndGameOverTimer();
                            //CHANCE EVENTS FROM PASSIVES AND RANDOM EVENTS
                            chanceEventTimer();

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
     * Method using a ticker and a chance variable to attempt to trigger a certain event from time to time. Used for random events and
     * passive events
     */
    private void chanceEventTimer(){
        ticker ++;
            if(ticker == Keys.TICKS_BEFORE_EVENT_TRIGGER_CHANCE){
                //once we tick enough times get a random number and if its lower than our chance for the event trigger the event
            if( (random.nextInt(Keys.MAX_CHANCE_FOR_EVENT)) < passivePowerUpEventChance){
                //used for triggering the chance event; for later development
                    isPassiveEventTriggerred = true;
                //if we selected the power up to give an energy boost, increase our current energy level.
                if(selectedPassive2 == Keys.FLAG_YELLOW_CHANCE_ENERGY_FILL){
                    currentEnergyLevel = currentEnergyLevel + Keys.ENERGY_CHANCE_EVENT_BONUS;
                }
            }
                ticker = Keys.TICKER_STARTING_VALUE;
            }


    }
    private void cooldownPowerUpTimer(){
        if(onCooldown ){

            if(coolDown >  (coolDownTimer)/1000){
                // has to increment equally to the millis interval of ticks
                coolDownTimer += 100;

            }
            else {

                onCooldown = false;
                coolDownTimer = 0;
            }
            if(coolDown-powerUpDuration <  (coolDownTimer)/1000 && !resettedPowerUp){

                resettedPowerUp = true;
                //if the power up is ball related, reset the balls
                // , if its not, there is no need to reset the balls
                if(flagTypePowerUp1 == Keys.FLAG_BALL_POWER_UP) {
                    //reset the ball objects after the power up expires
                    ballObject = powerUps.resetBallState(ballObject, selectedPowerUp1, currentBallType);
                    purpleBallObjects = powerUps.resetBallObjectArrayState(purpleBallObjects,
                            selectedPowerUp1, keys.PURPLE_BALL_NUMBER, currentBallType);
                    multipleBalls = powerUps.resetBallObjectArrayState(multipleBalls,
                            selectedPowerUp1, numberOfWaveAtoms(), currentBallType);
                }
            }

        }

    }
    private void consumablePowerUpTimer(){

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
                            selectedPowerUp2, numberOfWaveAtoms(), currentBallType);
                }
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void energyAndGameOverTimer(){

        //Keep reducing energy until the game is over
        if(!gameover) {
            if (currentEnergyLevel < keys.STARTING_ENERGY/2 && !middleEnergy){
                energyProgress.getProgressDrawable().setTint(Color.YELLOW);
               // soundPool.play(soundsAndEffects.soundNearlyGameOverId,1,1,3,0,1);
                middleEnergy = true;
            }
            if (currentEnergyLevel < keys.STARTING_ENERGY/4 && !lowEnergy){
                energyProgress.getProgressDrawable().setTint(Color.RED);
                soundsAndEffects.play(soundsAndEffects.soundNearlyGameOverId,3);
                lowEnergy = true;
            }
            //in case we exceed the maximum energy level, set it to the maximum
            if(currentEnergyLevel >= GameTimeAndScore.MAX_BALL_CLICK_TIME)
                currentEnergyLevel = GameTimeAndScore.MAX_BALL_CLICK_TIME;
            //until the game is finished keep lowering the energy levels
            currentEnergyLevel -= keys.ENERGY_DECREASE;
            energySpeedUpTicks++;
            if(energySpeedUpTicks%4==0 && keys.ENERGY_DECREASE <50) {
                keys.ENERGY_DECREASE += 1;
                energySpeedUpTicks = 0;
            }
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
        RandomBallVariables.setWidth(width);
        RandomBallVariables.setHeight(height);
        randomCoordinate = new RandomBallVariables(getBaseBallWidth(), getBaseBallHeight());

        stars = new Background(ourHolder, mCanvas, width, height);
        canvas = new CanvasGameClassic(ourHolder,mCanvas, stars, width, height);

        powerUps = new PowerUps(energyProgress,keys,powerUp, getBaseBallWidth(), getBaseBallHeight());

        // in case we selected the power up to increase the starting energy
        if(flagTypePassive2==4 || flagTypePassive1==4) {
            //set the energy bar in a certain way depending on the passives we selected
            currentEnergyLevel = powerUps.energyPassivePowerUp(selectedPassive2, currentEnergyLevel);
        }
        //if we selected passive that reduces speed of energy loss, this will reduce it
        if(selectedPassive1 == Keys.FLAG_PURPLE_SLOWER_ENERGY_DECAY)
            keys.ENERGY_DECREASE -= powerUps.energyPassivePowerUp(selectedPassive1, currentEnergyLevel);


        //get every ball object when starting a game
            ballHandler = new BallHandler(keys, getBaseBallWidth(), getBaseBallHeight());
            ballHandler.parseBallBitmaps(ballBitmaps);
            ballHandler.parseChancePassivesAndEventsObject(chancePassivesAndEvents);
        //set the ball attributes if the passives we selected affect the balls
            if(flagTypePassive1== 3 || flagTypePassive2 == 3)
            ballHandler.setDefaultValuesUponPassives(selectedPassive1,selectedPassive2);

        //get the ball objects for the first time with the default attributes
            ballObject = ballHandler.getFirstBallObject();
            purpleBallObjects = ballHandler.getFirstBallObjectArray(keys.PURPLE_BALL_NUMBER);
            multipleBalls = ballHandler.getFirstBallObjectArray(numberOfWaveAtoms());

        //the colors of purple and wave don't have to be be changed so initiate them once
        for(i=0; i<numberOfWaveAtoms(); i++){
            multipleBalls[i].setBallColor(ballBitmaps.getWaveBall()[i]);
        }
        for(i=0; i< keys.PURPLE_BALL_NUMBER; i++){
            purpleBallObjects[i].setBallColor(ballBitmaps.getPurpleBall());
        }
        getNewBall();

        clickedABall = new ClickedABall(getBaseBallWidth(), getBaseBallHeight());
        ballMovement = new BallMovement(width, height);
       initialDraw = false;


    }


            // ------------------------------- Ball Movement -------------------------- \\


    /**
     * Method that should be used to move every ball according to the type of the current ball.
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
                canvas.draw(ballObject, keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score);
                break;
            case BALL_RED:
                moveBall();
                canvas.draw(ballObject,  keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score);
                break;
            case BALL_YELLOW:
                moveYellowBall();
                canvas.draw(ballObject,  keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score);
                break;
            case BALL_GREEN:
                //this ball moves like crazy
                moveGreenBall();
                canvas.draw(ballObject,  keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score);
                break;
            case BALL_PURPLE:
                movePurpleBall();
                canvas.drawPurple(purpleBallObjects, keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score, timesClickedPurple, ballClicked);
                break;
            case BALL_WAVE:
                moveWave();
                canvas.drawWave(multipleBalls,  keys.POWER_UP_LIMITING_SQUARE_ACTIVE, score, currentWaveBall);
                break;
        }
    }

    /**
     * Method that should be used to move every ball that moves at the default speed.
     * It uses a ball object and then changes its coordinates depending on the ball's speed
     */
    private void moveBall() {
        ballObject = ballMovement.moveBall(ballObject);
    }
    /**
     * <p>Method for moving yellow balls.</p>
     * <p>
     *     Yellow balls slow down their speed after every click and are reduced in size.
     * </p>
     */
    private void moveYellowBall(){
        ballObject = ballMovement.moveBall(ballObject);
    }
    /**
     * <p>Method for moving green ball by changing its x and y coordinates.</p>
     * Green ball moves faster than the other balls
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
                if (ballClicked[0]) {
                    purpleBallObjects[0].setX(-purpleBallObjects[0].getBallWidth());
                }
                if (ballClicked[1]) {
                    purpleBallObjects[1].setX(-purpleBallObjects[1].getBallWidth());
                }
                if (ballClicked[2]) {
                    purpleBallObjects[2].setX(-purpleBallObjects[2].getBallWidth());
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
        for(int i = currentWaveBall; i< numberOfWaveAtoms(); i++){
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
            soundsAndEffects.play(soundsAndEffects.soundGameOverId, 2);
            // check if we got a new high score
            newHighScore=highScore.isHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY, score);
            //display our score and if we got a new high score show a text to indicate that
            gameover.gameOver(score, newHighScore);
            //add the atoms we collected during the game to the atom pool
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
                        multipleBalls = powerUps.activateBallObjectArrayConsumablePowerUp(multipleBalls, selectedPowerUp1, numberOfWaveAtoms());
                        //put the power up on coolDown, MANAGED IN TIMED ACTIONS METHOD
                        resettedPowerUp = false;
                        onCooldown = true;
                    }
                    //if the power up does something to the energy bar
                    else{
                        resettedPowerUp = false;
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
                        multipleBalls = powerUps.activateBallObjectArrayConsumablePowerUp(multipleBalls, selectedPowerUp2, numberOfWaveAtoms());
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
       // if(clickedABall.ballClicked(ballObject.getX(),ballObject.getY(),clickedX,clickedY)) {
        if(clickedABall.ballClicked(ballObject,clickedX,clickedY)) {
            //add some energy and update it in the energy text view
            currentEnergyLevel +=100;
            score += 100;
            //add the atom to the atom pool
            playBallClickedSound(ballObject);
            poolArray[0] = poolArray[0] + ballObject.getBallAtomValue();
            getNewBall();
        }
    }

    /**
     *<p> Method for handling red ball actions.</p>
     * The red ball should reduce score if we touch it, and increase score if we don't touch it. Gives us energy
     */
    private void redBall(){
        //if we click the ball
        if(clickedABall.ballClicked(ballObject,clickedX,clickedY)){
            //add some energy and update it in the energy text view
            currentEnergyLevel -=100;
            score -= 100;
            //add the atom to the atom pool, even if we scored negative the atom is added to the pool
            poolArray[1] = poolArray[1] + ballObject.getBallAtomValue();
            playBallClickedSound(ballObject);
            getNewBall();
        }
        else {
            //add some energy and update it in the energy text view
            currentEnergyLevel +=100;
            score+=100;
            //add the atom to the atom pool
            poolArray[1] = poolArray[1] + ballObject.getBallAtomValue();
            playBallClickedSound(ballObject);
            getNewBall();
        }
    }

    /**
     * <p>Method for handling green ball actions.</p>
     * The green ball should simply give us score when clicked, gives more score than blue ball and energy
     */
    private void greenBall(){
        if(clickedABall.ballClicked(ballObject,clickedX,clickedY)){
            //add some energy and update it in the energy text view
            currentEnergyLevel +=400;
            score+=400;
            //add the atom to the atom pool
            poolArray[2] = poolArray[2] + ballObject.getBallAtomValue();
            playBallClickedSound(ballObject);
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
        if(clickedABall.ballClicked(ballObject,clickedX,clickedY)) {
            if (keys.TIMES_CLICKED_YELLOW < keys.BALL_YELLOW_REQUIRED_CLICKS) {
                keys.TIMES_CLICKED_YELLOW++;
                //every time we click it, reduce its size and increase its speed
                ballObject.setBallWidth(ballObject.getBallWidth() - (ballObject.getBallWidth()/keys.BALL_YELLOW_SIZE_DECREASE));
                ballObject.setBallHeight(ballObject.getBallHeight() - (ballObject.getBallWidth()/keys.BALL_YELLOW_SIZE_DECREASE));
                ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(), ballObject.getBallWidth(), ballObject.getBallHeight(), true));
               //yellow balls speed should change if we haven't activated a power up
                if(!ballObject.isActiveChangesSpeed())
                ballObject.setBallSpeed(ballObject.getBallSpeed()+keys.BALL_YELLOW_SPEED_INCREASE);
                else if(selectedPowerUp1 == Keys.FLAG_GREEN_SLOW_DOWN_BALLS){
                    //if we have activated a power up, but it slows ball's down, increase the speed
                        BallHandler.yellowBallSpeedChangeActive = true;
                        ballObject.setBallSpeed(ballObject.getBallSpeed()+keys.BALL_YELLOW_SPEED_INCREASE);
                }
            }
            else {
                BallHandler.yellowBallSpeedChangeActive = true;
                //add some energy and update it in the energy text view
                currentEnergyLevel +=500;
                score+=500;
                //add the atom to the atom pool
                poolArray[3] = poolArray[3] + ballObject.getBallAtomValue();
                playBallClickedSound(ballObject);
                getNewBall();
                //reset the yellow ball to its first state for later use
                keys.TIMES_CLICKED_YELLOW=0;
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

        if (timesClickedPurple == keys.BALL_PURPLE_NO_CLICK) {
            //if we clicked on the first/ original ball
            if (clickedABall.ballClicked(purpleBallObjects[0], clickedX, clickedY)) {
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
                poolArray[4] = poolArray[4] + ballObject.getBallAtomValue();
                playBallClickedSound(purpleBallObjects[0]);
            }
        }
        //if we clicked on one of the split balls remove them from the screen
        else  {
            if(clickedABall.ballClicked(purpleBallObjects[0], clickedX, clickedY)){
                //don't draw this ball
                ballClicked[0]=true;
                //add the atom to the atom pool
                poolArray[4] = poolArray[4] + ballObject.getBallAtomValue();
                playBallClickedSound(purpleBallObjects[0]);
            }
            if(clickedABall.ballClicked(purpleBallObjects[1], clickedX, clickedY)){
                //don't draw this ball
                ballClicked[1]=true;
                //add the atom to the atom pool
                poolArray[4] = poolArray[4] + ballObject.getBallAtomValue();
                playBallClickedSound(purpleBallObjects[1]);
            }
            if(clickedABall.ballClicked(purpleBallObjects[2], clickedX, clickedY)){

                //don't draw this ball
                ballClicked[2]=true;

                //add the atom to the atom pool
                poolArray[4] = poolArray[4] + ballObject.getBallAtomValue();
                playBallClickedSound(purpleBallObjects[2]);
            }
            //if we clicked all three, score and get a new ball
            if(ballClicked[0] && ballClicked[1] && ballClicked[2]){

                ballPurpleNumber = keys.BALL_PURPLE_NO_CLICK;
                //reset to starting state
                ballClicked[0] = ballClicked[1] = ballClicked[2] = false;
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
        if(clickedABall.ballClicked(multipleBalls[currentWaveBall], clickedX, clickedY)){

            //gain more and more score and energy the more balls you click
            currentEnergyLevel += currentWaveBall*10;
            score += currentWaveBall*10;
            //adds a random atom to the pool every time we click a wave ball
            poolArray[random.nextInt(4)]+= multipleBalls[0].getBallAtomValue();
            playBallClickedSound(multipleBalls[0]);
            // next ball should be clicked
            currentWaveBall ++;

            if(currentWaveBall == numberOfWaveAtoms()){
                currentWaveBall = 0;
                // to round up the gain; with *10 you gain 420 score total
                score -=20;
                getNewBall();
            }
        }
    }


        // ------------------------------ New Ball --------------------------------- \\

    /**
     * Method used for getting a new ball and setting a score. Called whenever we clicked a ball for getting the next one.
     */
    private void getNewBall() {

        //If we used the "same type ball power up" on any ball, don't get a new type until it expires
        if(!isSameTypeBallPowerUpActive())
        ballType = ballHandler.getNewBallType();

        if(isLimitingSquareTriggered())
            triggerLimitingSquarePowerUp();

        if(isLimitingSquareExpired())
            turnOffLimitingSquarePowerUp();

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
        }

        // RED BALL
        if(currentBallType == BALL_BLUE){

            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
        }

        // YELLOW BALL
        if(currentBallType == BALL_YELLOW){
            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
        }

        // GREEN BALL
        if(currentBallType == BALL_GREEN){
            ballObject = ballHandler.getNewBallObject(ballObject, currentBallType);
        }

        // PURPLE BALL
        if(currentBallType == BALL_PURPLE){
            purpleBallObjects = ballHandler.getNewBallObjectArray(keys.PURPLE_BALL_NUMBER,purpleBallObjects, currentBallType);
        }

        //WAVE BALL
        if(currentBallType == BALL_WAVE){
            multipleBalls = ballHandler.getNewBallObjectArray(numberOfWaveAtoms(),multipleBalls, currentBallType);
            currentWaveBall = 0;
        }
    }

    //------------------- Refactor and New Helper methods ---------------------------- \\

    // BALL BITMAPS
    private void playBallClickedSound(Ball ball){
        soundsAndEffects.playBallClickedSound(ball);
    }
    private int numberOfWaveAtoms() {
        return Keys.WAVE_BALL_NUMBER;
    }
    private int getBaseBallWidth(){
        return ballBitmaps.getBallWidth();
    }
    private int getBaseBallHeight(){
        return ballBitmaps.getBallHeight();
    }

    // GET NEW BALL METHOD
    private boolean isLimitingSquareTriggered() {
        return keys.POWER_UP_LIMITING_SQUARE_ACTIVE && keys.POWER_UP_LIMITING_SQUARE_BALL_COUNT_UNTIL_SQUARE_DISSAPEARS==3;
    }
    private boolean isLimitingSquareExpired() {
        return keys.POWER_UP_LIMITING_SQUARE_BALL_COUNT_UNTIL_SQUARE_DISSAPEARS==0;
    }

    private void turnOffLimitingSquarePowerUp() {
        ballMovement.changeWidthAndHeight(width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH,
                height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT);
        randomCoordinate.changeWidthAndHeight(width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH,
                height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT);
        keys.POWER_UP_LIMITING_SQUARE_ACTIVE = false;
        keys.POWER_UP_LIMITING_SQUARE_BALL_COUNT_UNTIL_SQUARE_DISSAPEARS=3;
    }
    private void triggerLimitingSquarePowerUp() {
        // keys.POWER_UP_LIMITING_SQUARE_ACTIVE = false;
        //if the chance event for the limiting square is triggered , reduce the device's width and height
        ballMovement.changeWidthAndHeight(-width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH,
                -height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT);
        randomCoordinate.changeWidthAndHeight(-width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH,
                -height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT);
        keys.POWER_UP_LIMITING_SQUARE_BALL_COUNT_UNTIL_SQUARE_DISSAPEARS--;
    }

    private boolean isSameTypeBallPowerUpActive() {
        return ballObject.isActiveChangesType() && purpleBallObjects[0].isActiveChangesType()&& multipleBalls[0].isActiveChangesType();
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
        ballBitmaps.clearBitmapMemory();
        soundsAndEffects.releaseSoundPool();
    }

}