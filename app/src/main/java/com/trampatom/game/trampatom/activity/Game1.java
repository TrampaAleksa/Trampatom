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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.BallType;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Canvas1;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import com.trampatom.game.trampatom.utils.Keys;
import static java.lang.Thread.sleep;

public class Game1 extends AppCompatActivity implements Runnable, View.OnTouchListener{

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
                String scoreS;
        //coordinates of the currently drawn ball, coordinates where we clicked
                int x, clickedX;
                int y, clickedY;
        //used for moving the ball
                int moveX = 1;
                int moveY = 1;
                double angle;

        //Balls and Background Bitmaps
                Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall, background;
            //for moving the background
                int backgroundMove = 0;


    // ------------------- Ball type variables ------------------------------------------ \\

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
        Canvas1 canvas;
        HighScore highScore;
        BallType newBall;
        ClickedABall clickedABall;
        BallMovement ballMovement;
        Random greenRandom;

    // ------------------- Arrays ------------------------------------------------------- \\

            Ball b;
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
        //set of coordinates for every ball
            int[] waveXY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            int[] moveWaveXY = {1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    // ------------------- Game Variables ----------------------------------------------- \\

            TextView tvScore, tvTime;
            int width, height;
        //every ball should have the same width and height
            static int ballWidth, ballHeight;
        //used for displaying the score and setting new highScore at the end of the game
            int score, previousHighScore;
        //used for ending the game when the time ends, congratulations if new high score
            boolean gameover, newHighScore;

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
        //Current score and remaining time
            tvScore=(TextView) findViewById(R.id.tvScore);
            tvTime = (TextView) findViewById(R.id.tvTime);
            gameTimeAndScore = new GameTimeAndScore(tvScore, tvTime);
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
    }
    // TODO make method for initiating all bitmaps

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
            background = BitmapFactory.decodeResource(getResources(), R.drawable.atompozadina, options);
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
        canvas = new Canvas1(ourHolder,mCanvas, background);
        //used for defining random movements of green ball
        greenRandom = new Random();
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;
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
            //get a handler so that we can run the timer outside of the main ui thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new CountDownTimer(keys.GAME_TIME, 250) {
                        public void onTick(long millisUntilFinished) {
                            //seconds remaining and the starting time
                            int seconds =(int) millisUntilFinished/1000;
                            int totalTimerTime = (int) keys.GAME_TIME/1000;
                            //update the time with every tick
                            gameTimeAndScore.setTimeRemaining(millisUntilFinished);
                            startedTimer = true;
                        }

                        public void onFinish() {
                            //when the timer finishes, end the game
                            gameover = true;
                            //uncomment if the timer has to be reset every time it reaches zero
                            //startedTimer = false;
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
        newBall = new BallType(randomCoordinate);
        clickedABall = new ClickedABall(ballWidth, ballHeight);
        ballMovement = new BallMovement(width, height);
        //used for the first ball
        angle=randomCoordinate.randomAngle();
        x=randomCoordinate.randomX();
        y=randomCoordinate.randomY();
        waveXY[keys.WAVE_BALL_NUMBER]= ballHeight*2;
        for(i=1; i<keys.WAVE_BALL_NUMBER; i++){
            waveXY[i+keys.WAVE_BALL_NUMBER]= waveXY[i+keys.WAVE_BALL_NUMBER-1]+ ballHeight+10;
        }
        purpleXY[keys.PURPLE_BALL_XY1]= randomCoordinate.randomX();
        purpleXY[keys.PURPLE_BALL_XY1+keys.PURPLE_BALL_NUMBER]= randomCoordinate.randomY();
        purpleAngles[keys.PURPLE_BALL_ANGLE_ONE]= randomCoordinate.randomX();
        initialDraw= canvas.draw(blueBall,x,y, backgroundMove);
    }


            // ------------------------------- Ball Movement -------------------------- \\

    /**
     * <p>Method that should be run in a constant loop.</p>
     * <p>It moves and draws specific ball types depending on what is the current ball type.</p>
     * <p>All the move methods should be used here used here</p>
     */
    public void moveAndDraw(){
        backgroundMove--;
        switch(currentBall)
        {
            case BALL_BLUE:
                moveBall();
                canvas.draw(blueBall, x, y, backgroundMove);
                break;
            case BALL_RED:
                moveBall();
                canvas.draw(redBall, x, y, backgroundMove);
                break;
            case BALL_YELLOW:
                if(!changedSize) {
                    //resets the yellow ball to its original size when first drawing it
                    ballWidth = ballHeight = ballWidth + keys.YELLOW_BALL_INITIAL_SIZE;
                    yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                    changedSize=true;
                }
                moveYellowBall();
                canvas.draw(yellowBall, x, y, backgroundMove);
                break;
            case BALL_GREEN:
                //this ball moves like crazy
                moveGreenBall();
                canvas.draw(greenBall, x, y, backgroundMove);
                break;
            case BALL_PURPLE:
                movePurpleBall();
                canvas.drawPurple(purpleBall, purpleXY, timesClickedPurple, backgroundMove);
                break;
            case BALL_WAVE:
                moveWave();
                canvas.drawWave(waveBall, waveXY, backgroundMove);
                break;
        }
    }

    /**
     * <p>Method for moving most balls that use regular x and y coordinates.</p>
     * <p>it gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * These balls should move at a constant speed and don't change their angle
     */
    private void moveBall() {
       moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, keys.BALL_SPEED);
        //get a move array and set new coordinates to the current ball
        x= moveArray[keys.NEW_BALL_X_COORDINATE];
        y = moveArray[keys.NEW_BALL_Y_COORDINATE];
        moveX = moveArray[keys.NEW_BALL_MOVEX_VALUE];
        moveY = moveArray[keys.NEW_BALL_MOVEY_VALUE];
    }
    /**
     * <p>Method for moving yellow balls.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * Yellow balls reduce in size and increase its speed on every click
     */
    private void moveYellowBall(){
        //get a move array and set new coordinates to the current ball,
        // yellow ball speed will increase every time we click the ball
        moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, yellowBallSpeed);
        x= moveArray[keys.NEW_BALL_X_COORDINATE];
        y = moveArray[keys.NEW_BALL_Y_COORDINATE];
        moveX = moveArray[keys.NEW_BALL_MOVEX_VALUE];
        moveY = moveArray[keys.NEW_BALL_MOVEY_VALUE];
    }
    /**
     * <p>Method for moving green ball by changing its x and y coordinates.</p>
     * <p>It gets a moveArray and uses it to store new coordinates and a move direction for the ball.</p>
     * This ball has a random chance to change its angle.
     */
    private void moveGreenBall(){
        //gets a random number and if its below 20 change the angle
        int changeAngleGreen = greenRandom.nextInt(keys.GREEN_BALL_ANGLE_CHANGE_CHANCE);
        if(changeAngleGreen<=20){
            angle= randomCoordinate.randomAngle();
        }
        moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, keys.GREEN_BALL_SPEED);
        x= moveArray[keys.NEW_BALL_X_COORDINATE];
        y = moveArray[keys.NEW_BALL_Y_COORDINATE];
        moveX = moveArray[keys.NEW_BALL_MOVEX_VALUE];
        moveY = moveArray[keys.NEW_BALL_MOVEY_VALUE];

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
                    ballWidth, ballHeight, purpleMoveXY[i], purpleMoveXY[i+keys.PURPLE_BALL_NUMBER], purpleAngles[i], keys.BALL_SPEED);
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
            moveArray = ballMovement.moveWave(waveXY[i], waveXY[i+keys.WAVE_BALL_NUMBER],
                    ballWidth, moveWaveXY[i], moveWaveXY[i+keys.WAVE_BALL_NUMBER],keys.BALL_SPEED+i);
            waveXY[i]= moveArray[keys.NEW_BALL_X_COORDINATE];
            waveXY[i+keys.WAVE_BALL_NUMBER] = moveArray[keys.NEW_BALL_Y_COORDINATE];
            moveWaveXY[i] = moveArray[keys.NEW_BALL_MOVEX_VALUE];
            moveWaveXY[i+keys.WAVE_BALL_NUMBER] = moveArray[keys.NEW_BALL_MOVEY_VALUE];
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
     *<p> Method for handling red ball actions.</p>
     * The red ball should reduce score if we touch it, and increase score if we don't touch it
     */
    private void redBall(){
        //if we click the ball
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score -= 100;
            getNewBall();
        }
        else {
            score+=100;
            getNewBall();
        }
    }

    /**
     * <p> Method for handling red ball actions.</p>
     * Blue ball is classic, we get score by touching it
     */
    private void blueBall(){
        //if we clicked the ball should gain score
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)) {
            score += 100;
            getNewBall();
        }
    }

    /**
     * <p>Method for handling yellow ball actions.</p>
     * <p>The yellow ball should move really slow at first and speed up with
     * every click. It's size decreases also with every click</p>
     * After we have clicked it a certain amount of times, it should give us score
     */
    private void yellowBall(){

        //every time the ball is clicked decrease its size and increase speed
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)) {
            if (timesClicked > 0) {
                timesClicked--;
                //every time we click it, reduce its size and increase its speed
                ballWidth -= keys.BALL_YELLOW_SIZE_DECREASE;
                ballHeight -= keys.BALL_YELLOW_SIZE_DECREASE;
                yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                yellowBallSpeed+= keys.BALL_YELLOW_SPEED_INCREASE;

            }
            else {
                score+=500;
                getNewBall();
                //reset the yellow ball to its first state for later use
                timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
                ballWidth=ballHeight= blueBall.getWidth();
                yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                yellowBallSpeed=keys.BALL_YELLOW_INITIAL_SPEED;
                changedSize=false;
            }
        }
    }

    /**
     * <p>Method for handling green ball actions.</p>
     * The green ball should simply give us score when clicked, gives more score than blue ball
     */
    private void greenBall(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score+=400;
            getNewBall();
        }
    }

    /**
     *  <p>Method for handling purple ball actions.</p>
     *  <p>This ball is initially just one, but after we click it it should split into three.</p>
     *  <p>After we click it for the first time it should get random angles for every ball and then start moving them.</p>
     *  <p>Movement is handled in the main game loop with the movePurpleBall method</p>
     *  <p>After we click every ball it should disappear from the screen.</p>
     *  Score after we clicked all three balls
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
            }
        }
        //if we clicked on one of the split balls remove them from the screen
        else  {
            if(clickedABall.ballClicked(purpleXY[0],purpleXY[keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY1]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                originalBallClicked=true;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY2],purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY2]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                secondBallClicked=true;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY3],purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY3]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                //don't draw this ball
                thirdBallClicked=true;
            }
            //if we clicked all three, score and get a new ball
            if(originalBallClicked && secondBallClicked && thirdBallClicked){
                //reset to starting state
                originalBallClicked = secondBallClicked = thirdBallClicked = false;
                timesClickedPurple = keys.BALL_PURPLE_NO_CLICK;
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
     * we score and reset the current ball.</p>
     */
    private void waveBall(){
    //TODO multiple click ball types should update score with every click
        //if we click the correct ball in the sequence remove it and get score
        if(clickedABall.ballClicked(waveXY[currentWaveBall], waveXY[currentWaveBall+keys.WAVE_BALL_NUMBER], clickedX, clickedY)){
            //remove this ball from the screen and don't move it
            waveXY[currentWaveBall] = -ballWidth;
            // next ball should be clicked
            currentWaveBall ++;
            //gain more and more score the more balls you click
            score += currentBall*10;
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
        //set the score from the previous ball
        scoreS = "Score: " + score;
        tvScore.setText(scoreS);
        //get a new ball with new coordinates and angle of movement
        b = newBall.getNewBall();
        x= b.getX();
        y= b.getY();
        ballType = b.getBallType();
        angle = b.getAngle();
        for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
            //reset wave balls for next time it appears
            waveXY[i] = 0;
            moveWaveXY[i] = 1;
            currentWaveBall = 0;
        }
        //we need to get a new purple ball for next time it appears
        purpleXY[keys.PURPLE_BALL_XY1]= randomCoordinate.randomX();
        purpleXY[keys.PURPLE_BALL_NUMBER]= randomCoordinate.randomY();
        purpleAngles[keys.PURPLE_BALL_ANGLE_ONE] = randomCoordinate.randomAngle();
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
        //after leaving this game we need to clear the memory from the stored bitmaps
        if(background!=null)
        {
            background.recycle();
            background=null;
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