package com.trampatom.game.trampatom.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.BallHandler;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Background;
import com.trampatom.game.trampatom.canvas.CanvasGameClassic;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import com.trampatom.game.trampatom.utils.Keys;
import static java.lang.Thread.sleep;

/**
 * Class used to run the Classic game mode. It uses energy that you get from balls to keep the game running
 */
public class GameClassicActivity extends AppCompatActivity implements Runnable, View.OnTouchListener{

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
                int x, clickedX;
                int y, clickedY;
                double angle;

        //Balls and Background Bitmaps
                Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall;


    // ------------------- Ball type variables ------------------------------------------ \\
        //used to determine what drawing method to initially use to avoid errors
        int initialDrawType =1;
        //used for green ball
            // for determining wether or not to change greens angle
            int changeAngleGreen;
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
        Random greenRandom;
        Background stars;
        AtomPool atomPool;
        Ball ballObject;
        BallHandler ballHandler;
    // ------------------- Arrays ------------------------------------------------------- \\

        Ball[] multipleBalls = {null,null,null,null,null,null,null};
    //array for moving the ball
            int i;
            int[] moveArray= {0,0,0,0};
        //arrays for purple ball
            // x1,x2,x3,y1,y2,y3
            int [] purpleXY = {0,0,0,0,0,0};
            double [] purpleAngles= {0,0,0};
        // for moving direction
            int [] purpleMoveXY= {1,1,1,1,1,1};
        //array for wave balls
            Bitmap[] waveBall;

    // ------------------- Game Variables ----------------------------------------------- \\

    //TODO remove these text views since tey are no longer used
            TextView tvScore, tvTime;
        //Progress bar used to display remaining energy; If we are without energy we lose the game
            ProgressBar energyProgress;
            int width, height;
        //every ball should have the same width and height
            static int ballWidth, ballHeight;
        //used for displaying the score and setting new highScore at the end of the game
            int score=0, previousHighScore;
        //used to determine how long we will play
            int currentEnergyLevel;
        //used for ending the game when the time ends, congratulations if new high score
            boolean gameover, newHighScore;


    // -------------------------------- Power Up and Shop ---------------------------------- \\

        //For adding atoms to the Atom pool at end of game
            //contains 5 elements, blue, red, green, yellow and purple atoms to be filled during game
            int[] poolArray = {0,0,0,0,0};



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
        keys = new Keys();
        atomPool = new AtomPool(this);
        //some initial ball set-up's
            //sets the required times to click a yellow ball and its speed
            timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
            yellowBallSpeed = keys.BALL_YELLOW_INITIAL_SPEED;
            // sets only one purple ball to be displayed initially
            timesClickedPurple=keys.BALL_PURPLE_NO_CLICK;
        //set up the SurfaceView
            mSurfaceView = (SurfaceView) findViewById(R.id.SV1);
            ourHolder = mSurfaceView.getHolder();
            mSurfaceView.setOnTouchListener(this);
        //progress bar and remaining energy
            energyProgress = (ProgressBar) findViewById(R.id.pbEnergy) ;
            gameTimeAndScore = new GameTimeAndScore(tvScore, tvTime, energyProgress);
            currentEnergyLevel = keys.STARTING_ENERGY;
        //get device's width and height
            width= MainActivity.getWidth();
            height = MainActivity.getHeight();
        //set and resize all the bitmaps
            initiateBitmaps();
        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
            newHighScore=false;
        //the first ball is always blue;
        currentBall= BALL_BLUE;
        initialDraw=true;
        //initiate different ball objects here
            for(i = 0; i< keys.WAVE_BALL_NUMBER; i++){

                multipleBalls[i] = new Ball();
            }
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
        //used for defining random movements of green ball
        greenRandom = new Random();
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
                            int seconds =(int) millisUntilFinished/1000;
                            int totalTimerTime = (int) keys.GAME_TIME/1000;
                            if(!gameover) {
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

        //get a new ball object when starting the game
        ballHandler = new BallHandler(randomCoordinate);
        ballObject = new Ball();
        //set a default width and height
        ballObject.setBallWidth(ballWidth);
        ballObject.setBallHeight(ballHeight);
        for(i=0; i<keys.WAVE_BALL_NUMBER; i++) {
            multipleBalls[i].setBallWidth(ballWidth);
            multipleBalls[i].setBallHeight(ballHeight);
        }
        //initialize the wave balls so they are at a distance
        multipleBalls[0].setY(ballHeight*2);
        for(i=1; i<keys.WAVE_BALL_NUMBER; i++){
            multipleBalls[i].setY(multipleBalls[i-1].getY()+ ballHeight+ 10);
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
                canvas.drawPurple(purpleBall, purpleXY, timesClickedPurple, score);
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
        // yellow ball speed will increase every time we click the ball
        ballObject = ballMovement.moveBall(ballObject);

    }
    /**
     * <p>Method for moving green ball by changing its x and y coordinates.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * This ball has a random chance to change its angle.
     */
    private void moveGreenBall(){
        //gets a random number and if its below 20 change the angle
        changeAngleGreen = greenRandom.nextInt(keys.GREEN_BALL_ANGLE_CHANGE_CHANCE);
        if(changeAngleGreen<=20){
            ballObject.setAngle(randomCoordinate.randomAngle());
        }
        ballObject = ballMovement.moveBall(ballObject);

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
            moveArray = ballMovement.moveBall(purpleXY[i],purpleXY[i+keys.PURPLE_BALL_NUMBER],
                    ballWidth, ballHeight, purpleMoveXY[i], purpleMoveXY[i+keys.PURPLE_BALL_NUMBER], purpleAngles[i], keys.DEFAULT_BALL_SPEED);
            purpleXY[i]= moveArray[keys.NEW_BALL_X_COORDINATE];
            purpleXY[i+keys.PURPLE_BALL_NUMBER] = moveArray[keys.NEW_BALL_Y_COORDINATE];
            purpleMoveXY[i] = moveArray[keys.NEW_BALL_MOVEX_VALUE];
            purpleMoveXY[i+keys.PURPLE_BALL_NUMBER] = moveArray[keys.NEW_BALL_MOVEY_VALUE];
        }
        //draw three balls when they are clicked
        if(timesClickedPurple==keys.BALL_PURPLE_ONE_CLICK) {
                //the above for loop will now move three set of coordinates instead of just one
                    ballPurpleNumber = keys.PURPLE_BALL_NUMBER;
                //remove the balls that were clicked from the screen and stop moving them
                if (originalBallClicked)
                    purpleXY[keys.PURPLE_BALL_XY1] = purpleXY[keys.PURPLE_BALL_NUMBER] = 0 - ballWidth - 10;
                if (secondBallClicked) {
                    purpleXY[keys.PURPLE_BALL_XY2] = purpleXY[keys.PURPLE_BALL_XY2 + keys.PURPLE_BALL_NUMBER] = 0 - ballWidth - 10;
                }
                if (thirdBallClicked) {
                    purpleXY[keys.PURPLE_BALL_XY3] = purpleXY[keys.PURPLE_BALL_XY3 + keys.PURPLE_BALL_NUMBER] = 0 - ballWidth - 10;
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

            //if we click on the ball do something depending on the ball type
                if(ballType<=keys.TYPE_BALL_RED_CHANCE){
                    redBall();
                }
                if(ballType>keys.TYPE_BALL_RED_CHANCE && ballType<=keys.TYPE_BALL_BLUE_CHANCE){
                    blueBall();
                }
                if(ballType>keys.TYPE_BALL_BLUE_CHANCE && ballType<=keys.TYPE_BALL_YELLOW_CHANCE){
                    yellowBall();
                }
                if(ballType>keys.TYPE_BALL_YELLOW_CHANCE && ballType<=keys.TYPE_BALL_GREEN_CHANCE){
                    greenBall();
                }
                if(ballType>keys.TYPE_BALL_GREEN_CHANCE && ballType<=keys.TYPE_BALL_PURPLE_CHANCE){
                    purpleBall();
                }
                if(ballType>keys.TYPE_BALL_PURPLE_CHANCE && ballType<=keys.TYPE_BALL_WAVE_CHANCE){
                    waveBall();
                }
            setCurrentBall(ballType);
        }
        return false;
    }


        // ------------------------------ Ball Actions --------------------------------- \\

    /**
     * used to set the ball type to be drawn
     * <p>@param ballType used to determine what color to draw.</p>
     */
    private void setCurrentBall(int ballType){
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
            getNewBall();
        }
        else {
            //add some energy and update it in the energy text view
            currentEnergyLevel +=100;
            score+=100;
            //add the atom to the atom pool
            poolArray[1]++;
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
                ballObject.setBallSpeed(ballObject.getBallSpeed() + keys.BALL_YELLOW_SPEED_INCREASE);
            }
            else {
                //add some energy and update it in the energy text view
                currentEnergyLevel +=500;
                score+=500;
                //add the atom to the atom pool
                poolArray[3]++;
                getNewBall();
                //reset the yellow ball to its first state for later use
                timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
                ballWidth=ballHeight= blueBall.getWidth();
                yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                ballObject.setBallSpeed(keys.BALL_YELLOW_INITIAL_SPEED);
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
            //if we clicked on the first ball
            if (clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY1], purpleXY[keys.PURPLE_BALL_NUMBER], clickedX, clickedY)) {
                //used to determine how many balls to draw
                timesClickedPurple = keys.BALL_PURPLE_ONE_CLICK;
                //get new angles and set every ball to start moving from the sae spot
                purpleAngles[keys.PURPLE_BALL_ANGLE_ONE] = randomCoordinate.randomAngle();
                purpleAngles[keys.PURPLE_BALL_ANGLE_TWO] = randomCoordinate.randomAngle();
                purpleAngles[keys.PURPLE_BALL_ANGLE_THREE] = randomCoordinate.randomAngle();
                purpleXY[keys.PURPLE_BALL_XY2] = purpleXY[keys.PURPLE_BALL_XY1];
                purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER] = purpleXY[keys.PURPLE_BALL_NUMBER];
                purpleXY[keys.PURPLE_BALL_XY3] = purpleXY[keys.PURPLE_BALL_XY1];
                purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER] = purpleXY[keys.PURPLE_BALL_NUMBER];

                //add the atom to the atom pool
                poolArray[4]++;
            }
        }
        //if we clicked on one of the split balls remove them from the screen
        else  {
            if(clickedABall.ballClicked(purpleXY[0],purpleXY[keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY1]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                originalBallClicked=true;

                //add the atom to the atom pool
                poolArray[4]++;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY2],purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY2]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                secondBallClicked=true;

                //add the atom to the atom pool
                poolArray[4]++;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY3],purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY3]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                thirdBallClicked=true;

                //add the atom to the atom pool
                poolArray[4]++;
            }
            //if we clicked all three, score and get a new ball
            if(originalBallClicked && secondBallClicked && thirdBallClicked){
                //reset to starting state
                originalBallClicked = secondBallClicked = thirdBallClicked = false;
                timesClickedPurple = keys.BALL_PURPLE_NO_CLICK;
                //add some energy and update it in the energy text view
                currentEnergyLevel +=500;
                //gameTimeAndScore.setEnergyRemaining(currentEnergyLevel);
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
            //remove this ball from the screen and don't move it
            multipleBalls[currentWaveBall].setX(- ballWidth);
            // next ball should be clicked
            currentWaveBall ++;
            //gain more and more score and energy the more balls you click
            currentEnergyLevel += currentBall*10;
            score += currentBall*10;
            //adds a random atom to the pool every time we click a wave ball
            poolArray[greenRandom.nextInt(4)]++;
            if(currentWaveBall == keys.WAVE_BALL_NUMBER){
                // to round up the gain; with *10 you gain 420 score total
                score -=20;
                getNewBall();
            }
        }
    }


        // ------------------------------ Upon Scoring --------------------------------- \\

    /**
     * Method used for getting a new ball and setting a score
     */
    private void getNewBall() {
        //get a new ball with new coordinates and angle of movement
        ballObject = ballHandler.getNewBall(ballObject);
        x= ballObject.getX();
        y= ballObject.getY();
        ballType = ballObject.getBallType();
        setBallObjectByType(ballType);
        angle = ballObject.getAngle();

        //we need to get a new purple ball for next time it appears
        purpleXY[keys.PURPLE_BALL_XY1]= randomCoordinate.randomX();
        purpleXY[keys.PURPLE_BALL_NUMBER]= randomCoordinate.randomY();
        purpleAngles[keys.PURPLE_BALL_ANGLE_ONE] = randomCoordinate.randomAngle();
    }

    /**
     * helper method used to load the right bitmap into the ball based on its type
     * @param ballType the value that determines what type the ball is
     */
    private void setBallObjectByType(int ballType){
            ballObject.setBallSpeed(keys.DEFAULT_BALL_SPEED);
        // set the ball to be the color of the appropriate ball type and set its speed
        if(ballType<=keys.TYPE_BALL_RED_CHANCE){
            ballObject.setBallColor(redBall);
        }
        if(ballType>keys.TYPE_BALL_RED_CHANCE && ballType<=keys.TYPE_BALL_BLUE_CHANCE){
            ballObject.setBallColor(blueBall);
        }
        if(ballType>keys.TYPE_BALL_BLUE_CHANCE && ballType<=keys.TYPE_BALL_YELLOW_CHANCE){
            ballObject.setBallColor(yellowBall);
            ballObject.setBallColor(Bitmap.createScaledBitmap(ballObject.getBallColor(), ballWidth + keys.YELLOW_BALL_INITIAL_SIZE, ballHeight+ keys.YELLOW_BALL_INITIAL_SIZE, true));
            ballObject.setBallSpeed(keys.BALL_YELLOW_INITIAL_SPEED);
        }
        if(ballType>keys.TYPE_BALL_YELLOW_CHANCE && ballType<=keys.TYPE_BALL_GREEN_CHANCE){
            ballObject.setBallColor(greenBall);
            ballObject.setBallSpeed(keys.GREEN_BALL_SPEED);
        }
        if(ballType>keys.TYPE_BALL_GREEN_CHANCE && ballType<=keys.TYPE_BALL_PURPLE_CHANCE){
            ballObject.setBallColor(purpleBall);
            initialDrawType = BALL_PURPLE;
        }
        if(ballType>keys.TYPE_BALL_PURPLE_CHANCE && ballType<=keys.TYPE_BALL_WAVE_CHANCE){
            initialDrawType = BALL_WAVE;
            for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
                //reset wave balls for next time it appears
                multipleBalls[i].setX(0);
                multipleBalls[i].setMoveX(1); multipleBalls[i].setMoveY(1);
                currentWaveBall = 0;
                multipleBalls[i].setBallColor(waveBall[i]);
                //every next ball moves faster
                multipleBalls[i].setBallSpeed(keys.DEFAULT_BALL_SPEED+i);
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
    }
}