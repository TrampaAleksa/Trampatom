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
import com.trampatom.game.trampatom.ball.AtomId;
import com.trampatom.game.trampatom.ball.BallBitmaps;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.BallHandler;
import com.trampatom.game.trampatom.ball.BallTypeHandler;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.ball.controller.IBallClickedEvent;
import com.trampatom.game.trampatom.ball.controller.IBallFinishedEvent;
import com.trampatom.game.trampatom.ball.controller.PurpleBall;
import com.trampatom.game.trampatom.canvas.Background;
import com.trampatom.game.trampatom.canvas.CanvasGameClassic;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.PowerUps;
import com.trampatom.game.trampatom.currency.ShopHandler;
import com.trampatom.game.trampatom.energy.CurrentGameEnergy;
import com.trampatom.game.trampatom.power.up.ChancePassivesAndEvents;
import com.trampatom.game.trampatom.power.up.ConsumablePowerUpCooldownHandler;
import com.trampatom.game.trampatom.power.up.IPowerUpExpiredEvent;
import com.trampatom.game.trampatom.power.up.PowerUpCooldownHandler;
import com.trampatom.game.trampatom.score.CurrentGameScore;
import com.trampatom.game.trampatom.utils.Angles;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.GameWindow;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.PassivesManager;

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

    // ------------------- General Ball Variables --------------------------------------- \\

        //coordinates of the currently drawn ball, coordinates where we clicked
                int  clickedX;
                int  clickedY;

    // ------------------- Ball type variables ------------------------------------------ \\

        //used for wave ball
                int currentWaveBall = 0;

    // ------------------- Classes ------------------------------------------------------ \\

        Keys keys;
        GameTimeAndScore gameTimeAndScore;
        CanvasGameClassic canvas;
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
        BallTypeHandler ballTypeHandler;
        CurrentGameScore gameScore;
        CurrentGameEnergy gameEnergy;
        PowerUpCooldownHandler powerUpCooldownHandler;
        ConsumablePowerUpCooldownHandler consumableCooldownHandler;
        PurpleBall purpleBallHandler;

    // ------------------- Arrays ------------------------------------------------------- \\

        Ball[] multipleBalls = {null,null,null,null,null,null,null};
        Ball[] purpleBallObjects = {null, null, null};

    // -------------------------------- Power Up and Shop ---------------------------------- \\

        //For activating the power ups. One button has a cooldown the other one is a "one time use only"
            Button bPowerUp1, bPowerUp2;
        //determines what power up and what passive effect we choose and does a certain action based on that
            int selectedPowerUp1, selectedPowerUp2;
            int selectedPassive1, selectedPassive2;
        //determines if the power up or passive is energy or ball related
            int flagTypePowerUp1, flagTypePowerUp2;
            int flagTypePassive1, flagTypePassive2;
        //For passive event chance power ups
            // ticker used for the timer to prevent events happening too often
            int ticker;

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
        gameScore = new CurrentGameScore();
        setWidth(MainActivity.getWidth());
        setHeight(MainActivity.getHeight());

        // SOUNDS
        initSounds();

        //GAME VIEWS
        initGameViews();

        //POWER UPS
        initPowerUps();

        //BALL BITMAPS
        initiateBitmaps();

        //BALL
        initBall();
    }

    private void initSounds() {
        //get a sound pool instance with all the required sounds
        //sound id's are located inside SoundsAndEffects object
        soundsAndEffects = new SoundsAndEffects(this).getGameClassicSounds();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        gameEnergy = getGameEnergy();
        int startingEnergy = getGameEnergy().getStartingEnergy();
        setEnergyLevel(startingEnergy);

        //buttons
        bPowerUp1 = (Button) findViewById(R.id.bPowerUp1);
        bPowerUp2 = (Button) findViewById(R.id.bPowerUp2);
        bPowerUp1.setOnClickListener(this);
        bPowerUp2.setOnClickListener(this);
    }



    private void initPowerUps() {
        ticker = Keys.TICKER_STARTING_VALUE;
        //load the power ups we selected in the shop from a file
        ShopHandler shopHandler = new ShopHandler(this);
        powerUpPool = shopHandler.loadSelectedPowerUps();
        for(int i=0; i<4; i++){
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

        chancePassivesAndEvents = new ChancePassivesAndEvents(powerUp[2]);
    }

    /**
     * Method for decoding every Bitmap into memory and
     * rescaling every ball into a certain size.
     * Gets a standard ball height and width variable to be used
     */
    private void initiateBitmaps(){

        int ballWidth, ballHeight;

        int heightScale = 11;
        ballHeight = ballWidth = passivesManager.setNewBallSizeFromPassives(getHeight()/ heightScale, selectedPassive1,
                selectedPassive2,powerUp[2].getCurrentLevel(), powerUp[3].getCurrentLevel());

        this.ballBitmaps = new BallBitmaps(this);
        ballBitmaps.setBallWidth(ballWidth);
        ballBitmaps.setBallHeight(ballHeight);
        ballBitmaps.initiateBitmaps();
    }

    private void initBall() {

        ballTypeHandler = new BallTypeHandler();
        setCurrentBallTypeBySeed();

        initialDraw=true;
    }



    // --------------------------- Main Game Loop ------------------------------- \\

    @Override
    public void run() {

        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;


            //constantly update the energy bar to display remaining energy
            gameTimeAndScore.updateEnergyBar(getEnergyLevel());
            //start the game time
            timedActions();
            //The initial draw
            if(initialDraw) {
                initiateOnCanvas();
            }
            moveBalls();
            drawBalls();
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

                            //COOLDOWNS OF POWER UPS
                            updatePowerUpCooldown();
                            updateConsumablePowerUpCooldown();

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
                if( (random.nextInt(Keys.MAX_CHANCE_FOR_EVENT)) < chancePassivesAndEvents.getPassivePowerUpEventChance()){

                    //if we selected the power up to give an energy boost, increase our current energy level.
                    if(selectedPassive2 == Keys.FLAG_YELLOW_CHANCE_ENERGY_FILL){
                        addEnergy(Keys.ENERGY_CHANCE_EVENT_BONUS);
                }
            }
                ticker = Keys.TICKER_STARTING_VALUE;
            }


    }
    private void updatePowerUpCooldown(){
        PowerUpCooldownHandler cooldownHandler = getPowerUpCooldownHandler();
        cooldownHandler.tryUpdatingCooldown();
    }

    private void updateConsumablePowerUpCooldown(){
            getConsumableCooldownHandler().tryUpdatingConsumableCooldown();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void energyAndGameOverTimer(){
        if (isGameOver()) return;

        CurrentGameEnergy currentGameEnergy = getGameEnergy();

        if (currentGameEnergy.triggeredMiddleEnergy()){
            energyProgress.getProgressDrawable().setTint(Color.YELLOW);
        }
        if (currentGameEnergy.triggeredLowEnergy()){
            energyProgress.getProgressDrawable().setTint(Color.RED);
            soundsAndEffects.play(soundsAndEffects.soundNearlyGameOverId,3);
        }

        //until the game is finished keep lowering the energy levels
        reduceEnergy(currentGameEnergy.getEnergyDecrease());
        currentGameEnergy.updateEnergy();
    }


    /**
     * <p>Method for the initial canvas draw. Initiate most class instances here.</p>
     * <p>Set the initial angles and coordinates of balls, and draw everything.</p>
     * This method should be run only once
     */
    private void initiateOnCanvas(){
        //needed to get the height of the canvas
        mCanvas = ourHolder.lockCanvas();
        setHeight(mCanvas.getHeight());
        ourHolder.unlockCanvasAndPost(mCanvas);
        // object instances
        stars = new Background(ourHolder, mCanvas, getWidth(), getHeight());
        canvas = new CanvasGameClassic(ourHolder,mCanvas, stars);
        canvas.setCurrentGameScore(gameScore);

        powerUps = new PowerUps(energyProgress,keys,powerUp, getBaseBallWidth(), getBaseBallHeight());

        // in case we selected the power up to increase the starting energy
        if(flagTypePassive2==4 || flagTypePassive1==4) {
            //set the energy bar in a certain way depending on the passives we selected
            setEnergyLevel(powerUps.energyPassivePowerUp(selectedPassive2, getEnergyLevel()));
        }
        //if we selected passive that reduces speed of energy loss, this will reduce it
        if(selectedPassive1 == Keys.FLAG_PURPLE_SLOWER_ENERGY_DECAY)
            gameEnergy.reduceEnergyDecrease(powerUps.energyPassivePowerUp(selectedPassive1, getEnergyLevel()));

        //get every ball object when starting a game
            ballHandler = new BallHandler(keys, getBaseBallWidth(), getBaseBallHeight());
            ballHandler.parseBallBitmaps(ballBitmaps);
            ballHandler.parseChancePassivesAndEventsObject(chancePassivesAndEvents);
        //set the ball attributes if the passives we selected affect the balls
            if(flagTypePassive1== 3 || flagTypePassive2 == 3)
            ballHandler.setDefaultValuesUponPassives(selectedPassive1,selectedPassive2);

        //get the ball objects for the first time with the default attributes
            ballObject = ballHandler.getFirstBallObject();
            setPurpleBallObjects(ballHandler.getFirstBallObjectArray(Keys.PURPLE_BALL_NUMBER));
            setMultipleBalls(ballHandler.getFirstBallObjectArray(numberOfWaveAtoms()));

        //the colors of purple and wave don't have to be be changed so initiate them once
        for(int i=0; i<numberOfWaveAtoms(); i++){
            getMultipleBalls()[i].setBallColor(ballBitmaps.getWaveBall()[i]);
        }
        for(int i = 0; i< Keys.PURPLE_BALL_NUMBER; i++){
            getPurpleBallObjects()[i].setBallColor(ballBitmaps.getPurpleBall());
        }
        getNewBall();

        purpleBallHandler = new PurpleBall(
                new IBallFinishedEvent() {
                    @Override
                    public void onBallFinished() {
                        onPurpleFinishedEvent();
                    }
                },
                new IBallClickedEvent() {
                    @Override
                    public void onBallClicked() {
                        addAtomToPool(getPurpleBallObjects()[0]);
                        playBallClickedSound(getPurpleBallObjects()[0]);
                    }
                }
        );
        clickedABall = new ClickedABall(getBaseBallWidth(), getBaseBallHeight());
        purpleBallHandler.setClickedABall(clickedABall);
        ballMovement = new BallMovement(getWidth(), getHeight());
        initialDraw = false;

    }



    // ------------------------------- Ball Movement -------------------------- \\

    public void moveBalls(){
        switch(getCurrentBallType())
        {
            case BALL_BLUE:
                moveBall();
                break;
            case BALL_RED:
                moveBall();
                break;
            case BALL_YELLOW:
                moveYellowBall();
                break;
            case BALL_GREEN:
                moveGreenBall();
                break;
            case BALL_PURPLE:
                movePurpleBall();
                break;
            case BALL_WAVE:
                moveWave();
                break;
        }
    }
    public void drawBalls(){
        Ball ballObject = getBallObject();
        switch(getCurrentBallType())
        {
            case BALL_BLUE:
                canvas.draw(ballObject);
                break;
            case BALL_RED:
                canvas.draw(ballObject);
                break;
            case BALL_YELLOW:
                canvas.draw(ballObject);
                break;
            case BALL_GREEN:
                canvas.draw(ballObject);
                break;
            case BALL_PURPLE:
                canvas.drawPurple(getPurpleBallObjects(), purpleBallHandler);
                break;
            case BALL_WAVE:
                canvas.drawWave(getMultipleBalls(), currentWaveBall);
                break;
        }
    }

    /**
     * Method that should be used to move every ball that moves at the default speed.
     * It uses a ball object and then changes its coordinates depending on the ball's speed
     */
    private void moveBall() {
        ballMovement.moveBall(getBallObject());
    }
    /**
     * <p>Method for moving yellow balls.</p>
     * <p>
     *     Yellow balls slow down their speed after every click and are reduced in size.
     * </p>
     */
    private void moveYellowBall(){
        ballMovement.moveBall(getBallObject());
    }
    /**
     * <p>Method for moving green ball by changing its x and y coordinates.</p>
     * Green ball moves faster than the other balls
     */
    private void moveGreenBall(){
        ballMovement.moveGreenBall(getBallObject());
    }
    /**
     * <p>Method for moving purple balls by changing every pair of x and y coordinates at the same time.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for every ball.</p>
     * <p>This ball is initially one, but after we click it, it should split into three balls.</p>
     * <p>If one of the three balls is clicked it should disappear</p>
     */
    private void movePurpleBall(){
        //depending on if we clicked the first purple ball we will move only one ball or all three balls
        if (purpleBallHandler.getTimesClickedPurple() == keys.BALL_PURPLE_NO_CLICK)
                ballMovement.moveBall(getPurpleBallObjects()[0]);
        else
            moveEveryPurpleBall();
    }

    private void moveEveryPurpleBall() {
        for(int i = 0; i< getPurpleBallObjects().length; i++){
            ballMovement.moveBall(getPurpleBallObjects()[i]);

            if (purpleBallHandler.getBallClickedArray()[i])
                getPurpleBallObjects()[i].setX(-getPurpleBallObjects()[i].getBallWidth());
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
            getMultipleBalls()[i] = ballMovement.moveWave(getMultipleBalls()[i]);
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
        if (isGameOver()) {
            GameOver gameover = new GameOver(ourHolder,mCanvas);
            //plat a sound once the game is over
            soundsAndEffects.play(soundsAndEffects.soundGameOverId, 2);
            //display our score and if we got a new high score show a text to indicate that
            gameover.gameOver(gameScore, new HighScore(this));
            //add the atoms we collected during the game to the atom pool
            atomPool.saveAtoms();
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
            clickedABall.setClickedX(clickedX);
            clickedABall.setClickedY(clickedY);

            setCurrentBallTypeBySeed();
            //if we click on the ball do something depending on the ball type
                if(getCurrentBallType() == BALL_RED){
                    redBall();
                }
                if(getCurrentBallType() == BALL_BLUE){
                    blueBall();
                }
                if(getCurrentBallType() == BALL_YELLOW){
                    yellowBall();
                }
                if(getCurrentBallType() == BALL_GREEN){
                    greenBall();
                }
                if(getCurrentBallType() == BALL_PURPLE){
                    purpleBall();
                }
                if(getCurrentBallType() == BALL_WAVE){
                    waveBall();
                }
//            ballTypeHandler.setCurrentType(getCurrentBallType());
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bPowerUp1)
            triggerPowerUp1();
        if (v.getId() == R.id.bPowerUp2)
            triggerPowerUp2();
        }



    // ------------------------------ Ball Actions --------------------------------- \\


    /**
     * <p> Method for handling red ball actions.</p>
     * Blue ball is classic, we get score by touching it. Gives us energy
     */
    private void blueBall(){
        Ball ball = getBallObject();
        //if we clicked the ball should gain score
        if(clickedABall.ballClicked(ball,clickedX,clickedY)) {
            //add some energy and update it in the energy text view
            addEnergy(100);
            addScore(100);
            //add the atom to the atom pool
            playBallClickedSound(ball);
            addAtomToPool(ball);
            getNewBall();
        }
    }



    /**
     *<p> Method for handling red ball actions.</p>
     * The red ball should reduce score if we touch it, and increase score if we don't touch it. Gives us energy
     */
    private void redBall(){
        Ball ball = getBallObject();
        //if we click the ball
        if(clickedABall.ballClicked(ball,clickedX,clickedY)){
            //add some energy and update it in the energy text view
            reduceEnergy(100);
            reduceScore(100);
            //add the atom to the atom pool, even if we scored negative the atom is added to the pool
            addAtomToPool(ball);
            playBallClickedSound(ball);
            getNewBall();
        }
        else {
            //add some energy and update it in the energy text view
            addEnergy(100);
            addScore(100);
            //add the atom to the atom pool
            addAtomToPool(ball);
            playBallClickedSound(ball);
            getNewBall();
        }
    }

    /**
     * <p>Method for handling green ball actions.</p>
     * The green ball should simply give us score when clicked, gives more score than blue ball and energy
     */
    private void greenBall(){
        Ball ball = getBallObject();

        if(clickedABall.ballClicked(ball,clickedX,clickedY)){
            //add some energy and update it in the energy text view
            addEnergy(400);
            addScore(400);
            //add the atom to the atom pool
            addAtomToPool(ball);
            playBallClickedSound(ball);
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

        Ball ball = getBallObject();

        //every time the ball is clicked decrease its size and increase speed
        if(clickedABall.ballClicked(ball,clickedX,clickedY)) {
            if (keys.TIMES_CLICKED_YELLOW < keys.BALL_YELLOW_REQUIRED_CLICKS) {
                keys.TIMES_CLICKED_YELLOW++;
                //every time we click it, reduce its size and increase its speed
                ball.setBallWidth(ball.getBallWidth() - (ball.getBallWidth()/keys.BALL_YELLOW_SIZE_DECREASE));
                ball.setBallHeight(ball.getBallHeight() - (ball.getBallWidth()/keys.BALL_YELLOW_SIZE_DECREASE));
                ball.setBallColor(Bitmap.createScaledBitmap(ball.getBallColor(), ball.getBallWidth(), ball.getBallHeight(), true));
               //yellow balls speed should change if we haven't activated a power up
                if(!ball.isActiveChangesSpeed())
                    ball.setBallSpeed(ball.getBallSpeed()+keys.BALL_YELLOW_SPEED_INCREASE);
                else if(selectedPowerUp1 == Keys.FLAG_GREEN_SLOW_DOWN_BALLS){
                    //if we have activated a power up, but it slows ball's down, increase the speed
                        BallHandler.yellowBallSpeedChangeActive = true;
                    ball.setBallSpeed(ball.getBallSpeed()+keys.BALL_YELLOW_SPEED_INCREASE);
                }
            }
            else {
                BallHandler.yellowBallSpeedChangeActive = true;
                //add some energy and update it in the energy text view
                addEnergy(500);
                addScore(500);
                //add the atom to the atom pool
                addAtomToPool(ball);
                playBallClickedSound(ball);
                getNewBall();
                //reset the yellow ball to its first state for later use
                keys.TIMES_CLICKED_YELLOW=0;
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
        purpleBallHandler.handlePurpleBall(getPurpleBallObjects());
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
        if(clickedABall.ballClicked(getMultipleBalls()[currentWaveBall], clickedX, clickedY)){

            //gain more and more score and energy the more balls you click
            addEnergy(currentWaveBall*10);
            addScore(currentWaveBall*10);
            //adds a random atom to the pool every time we click a wave ball
            addAtomToPool(getMultipleBalls()[0]);
            playBallClickedSound(getMultipleBalls()[0]);
            // next ball should be clicked
            currentWaveBall ++;

            if(currentWaveBall == numberOfWaveAtoms()){
                currentWaveBall = 0;
                // to round up the gain; with *10 you gain 420 score total
                reduceScore(20);
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
        ballTypeHandler.setRandomBallTypeSeed();
        setCurrentBallTypeBySeed();
        setBallObjectByType(getCurrentBallType());
    }


    /**
     * helper method used to load the right bitmap into the ball based on its type.
     * <p>IMPORTANT: every ball NEEDS to have its speed and color specified or it will not move or be drawn</p>
     */
    private void setBallObjectByType(AtomId currentBallType){

        // SINGLE BALL
        boolean isSingleBall = currentBallType == BALL_RED || currentBallType == BALL_BLUE || currentBallType == BALL_YELLOW || currentBallType == BALL_GREEN;
        if(isSingleBall){
            ballObject = ballHandler.getNewBallObject(getBallObject(), currentBallType);
        }

        // PURPLE BALLS
        if(currentBallType == BALL_PURPLE){
            setPurpleBallObjects(ballHandler.getNewBallObjectArray(
                    Keys.PURPLE_BALL_NUMBER,
                    getPurpleBallObjects(),
                    currentBallType));
        }

        //WAVE BALLS
        if(currentBallType == BALL_WAVE){
            setMultipleBalls(ballHandler.getNewBallObjectArray(numberOfWaveAtoms(), getMultipleBalls(), currentBallType));
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

    private boolean isSameTypeBallPowerUpActive() {
        return getBallObject().isActiveChangesType() && getPurpleBallObjects()[0].isActiveChangesType()&& getMultipleBalls()[0].isActiveChangesType();
    }

    // WIDTH AND HEIGHT

    public int getWidth() {
        return getGameWindow().getWidth();
    }
    public void setWidth(int width) {
        getGameWindow().setWidth(width);
    }
    public int getHeight() {
        return getGameWindow().getHeight();
    }
    public void setHeight(int height) {
        getGameWindow().setHeight(height);
    }
    private GameWindow getGameWindow(){
        return GameWindow.getInstance();
    }
    private double getRandomAngle() {
        return Angles.randomAngle();
    }

    // BALL TYPE
    private AtomId getCurrentBallType() {
        return ballTypeHandler.getCurrentType();
    }
    private void setCurrentBallTypeBySeed() {
        ballTypeHandler.setCurrentBallTypeBySeed();
    }

    //SCORE
    private void addScore(int toAdd){
        gameScore.addScore(toAdd);
    }
    private void reduceScore(int toReduce){
        gameScore.reduceScore(toReduce);
    }
    private boolean isGameOver() {
        return getEnergyLevel() <= 0;
    }

    //ENERGY
    private CurrentGameEnergy getGameEnergy() {
        if (gameEnergy==null)
            gameEnergy = new CurrentGameEnergy();
        return gameEnergy;
    }
    private void setEnergyLevel(int energyLevel){
        getGameEnergy().setCurrentEnergyLevel(energyLevel);
    }
    private int getEnergyLevel() {
        return getGameEnergy().getCurrentEnergyLevel();
    }
    private void addEnergy(int toAdd){
        getGameEnergy().addEnergy(toAdd);
    }
    private void reduceEnergy(int toReduce){
        getGameEnergy().reduceEnergy(toReduce);
    }

    // ATOM POOL AND SHOP

    private void addAtomToPool(Ball ballObject) {
        atomPool.addAtom(ballObject);
    }

    // POWER UPS

        // Cooldown:

    private PowerUpCooldownHandler getPowerUpCooldownHandler(){
        if (powerUpCooldownHandler == null) {
            powerUpCooldownHandler = new PowerUpCooldownHandler(new IPowerUpExpiredEvent() {
                @Override
                public void onPowerUpExpired() {
                    powerUpExpired();
                }
            });
        }
        return powerUpCooldownHandler;
    }
    public void powerUpExpired() {
        if (flagTypePowerUp1 != Keys.FLAG_BALL_POWER_UP)
            return;
        //if the power up is ball related, reset the balls after the power up expires
        powerUps.resetBallState(getBallObject(), selectedPowerUp1);
        setPurpleBallObjects(
                powerUps.resetBallObjectArrayState(getPurpleBallObjects(),
                selectedPowerUp1,
                Keys.PURPLE_BALL_NUMBER
        ));
        setMultipleBalls(
                powerUps.resetBallObjectArrayState(getMultipleBalls(),
                selectedPowerUp1,
                numberOfWaveAtoms()
        ));
    }

    private ConsumablePowerUpCooldownHandler getConsumableCooldownHandler(){
        if (consumableCooldownHandler == null)
            consumableCooldownHandler = new ConsumablePowerUpCooldownHandler(new IPowerUpExpiredEvent() {
                @Override
                public void onPowerUpExpired() {
                    onConsumablePowerUpExpired();
                }
            });
        return consumableCooldownHandler;
    }
    private void onConsumablePowerUpExpired() {
        if(flagTypePowerUp2 == Keys.FLAG_BALL_POWER_UP) {
            powerUps.resetBallState(getBallObject(), selectedPowerUp2);
            setPurpleBallObjects(powerUps.resetBallObjectArrayState(getPurpleBallObjects(),
                    selectedPowerUp2, Keys.PURPLE_BALL_NUMBER));

            setMultipleBalls(powerUps.resetBallObjectArrayState(getMultipleBalls(),
                    selectedPowerUp2, numberOfWaveAtoms()));
        }
    }


        // Power Up 1 trigger

    private void triggerPowerUp1() {
        PowerUpCooldownHandler cooldownHandler = getPowerUpCooldownHandler();

        if (cooldownHandler.isOnCooldown())
            return;

        if(flagTypePowerUp1 == Keys.FLAG_BALL_POWER_UP)
            triggerBallPowerUp(cooldownHandler);
        else
            triggerEnergyPowerUp(cooldownHandler);
    }

    private void triggerEnergyPowerUp(PowerUpCooldownHandler cooldownHandler) {
        addEnergy(powerUps.energyPowerUp(selectedPowerUp1));
        cooldownHandler.activateCooldown();
    }
    private void triggerBallPowerUp(PowerUpCooldownHandler cooldownHandler) {
        powerUps.activateBallObjectConsumablePowerUp(getBallObject(), selectedPowerUp1);
        setPurpleBallObjects(powerUps.activateBallObjectArrayConsumablePowerUp(getPurpleBallObjects(), selectedPowerUp1, Keys.PURPLE_BALL_NUMBER));
        setMultipleBalls(powerUps.activateBallObjectArrayConsumablePowerUp(getMultipleBalls(), selectedPowerUp1, numberOfWaveAtoms()));
        //put the power up on coolDown, MANAGED IN TIMED ACTIONS METHOD
        cooldownHandler.activateCooldown();
    }

        // Power Up 2 trigger

    private void triggerPowerUp2() {
        ConsumablePowerUpCooldownHandler cooldownHandler = getConsumableCooldownHandler();

        if (cooldownHandler.isUsedConsumable())
            return;

        if(flagTypePowerUp2 == Keys.FLAG_BALL_POWER_UP)
            triggerConsumableBallPowerUp(cooldownHandler);
        else
            triggerConsumableEnergyPowerUp(cooldownHandler);
    }

    private void triggerConsumableEnergyPowerUp(ConsumablePowerUpCooldownHandler cooldownHandler) {
        addEnergy(powerUps.energyPowerUp(selectedPowerUp2));
        cooldownHandler.activateConsumableCooldown(false);
    }
    private void triggerConsumableBallPowerUp(ConsumablePowerUpCooldownHandler cooldownHandler) {
        powerUps.activateBallObjectConsumablePowerUp(getBallObject(), selectedPowerUp2);
        setPurpleBallObjects(powerUps.activateBallObjectArrayConsumablePowerUp(getPurpleBallObjects(), selectedPowerUp2, Keys.PURPLE_BALL_NUMBER));
        setMultipleBalls(powerUps.activateBallObjectArrayConsumablePowerUp(getMultipleBalls(), selectedPowerUp2, numberOfWaveAtoms()));
        cooldownHandler.activateConsumableCooldown(true);
    }



    // BALL OBJECTS

    private Ball getBallObject() {
        return ballObject;
    }
    private Ball[] getPurpleBallObjects() {
        return purpleBallObjects;
    }
    private void setPurpleBallObjects(Ball[] purpleBallObjects){
        this.purpleBallObjects = purpleBallObjects;
    }
    private Ball[] getMultipleBalls() {
        return multipleBalls;
    }
    public void setMultipleBalls(Ball[] multipleBalls) {
        this.multipleBalls = multipleBalls;
    }
    //PURPLE BALL

    private void onPurpleFinishedEvent() {
        addEnergy(500);
        addScore(500);
        getNewBall();
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