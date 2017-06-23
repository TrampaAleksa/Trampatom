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

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Background;
import com.trampatom.game.trampatom.canvas.Canvas3;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Game3 extends AppCompatActivity implements Runnable, View.OnTouchListener{
    //TODO remove progress bar in this game and display spare life for better UX

    private static final int BALL_SPEED= 8;
    public static final int BALL_NEGATIVE_NUMBER = 7;

    // ------------------- General Ball Variables --------------------------------------- \\

        //Balls and Background Bitmaps
            Bitmap ball;
            Bitmap[] negativeBall;
            Bitmap goldBall;

        //coordinates of the currently drawn ball, coordinates where we clicked
            int x, clickedX;
            int y, clickedY;
        //used for moving the ball
            int moveX = 1;
            int moveY = 1;
            double angle;


    // ------------------- Ball type variables ------------------------------------------ \\


    //used for golden ball
        //set at -1 to prevent clicking on it before it is drawn
            int goldX=-1; int goldY=-1;
        //spare life
            boolean gotLife = false;
        //used for determining when to draw the ball
            boolean drawGoldBall= false;
    //used for red ball
            int redBallSpeed = 8;

    // ------------------- Classes ------------------------------------------------------ \\

    GameTimeAndScore gameTimeAndScore;
    RandomBallVariables randomCoordinate;
    Canvas3 canvas;
    HighScore highScore;
    ClickedABall clickedABall;
    Keys keys;
    BallMovement ballMovement;
    Random randomNumber;
    Background stars;


    // ------------------- Arrays ------------------------------------------------------- \\

    int[] ballMovementArray = {0,0,0,0};
    int[] randomSize={0,0,0,0,0,0,0};
    int[] negWidth={0,0,0,0,0,0,0};
    int[] negHeight={0,0,0,0,0,0,0};
    //14 coordinates ; 7 red balls
    //TODO use lists / arrays to get coordinates
    int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    //Used to move neg balls in a cirtain direction
    int[] negMoveX={1,1,1,1,1,1,1};
    int[] negMoveY={1,1,1,1,1,1,1};
    double[] angles= {0,0,0,0,0,0,0,0};


    // ------------------- Game Variables ----------------------------------------------- \\

    int width, height, j,i;
    //every ball should have the same width and height
    int ballWidth, ballHeight;
    //used for displaying the score and setting new highScore at the end of the game
    int score, previousHighScore;
    //used for ending the game when the time ends, congratulations if new high score
    boolean gameover, newHighScore;
    //Progress bar used to display remaining energy; If we are without energy we lose the game
    ProgressBar remainingTime;
    //used for determining how much tme we have left in the progress bar
    int remainingClickTime=5000;

    // ------------------- Surface View ------------------------------------------------- \\

    //For the SurfaceView to work
    SurfaceHolder ourHolder;
    SurfaceView mSurfaceView;
    Canvas mCanvas;
    Thread ourThread = null;
    boolean isRunning=true;
    //used for drawing the first ball , used to start the timer once
    boolean initialDraw, startedTimer, startedBallTimer=false;

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

    // --------------------------------------- Initialization ------------------------------ \\

    private void init() {
        //Classes
            keys = new Keys();
            ballMovement = new BallMovement(width, height);
            randomNumber = new Random();
        //set up the SurfaceView
            mSurfaceView = (SurfaceView) findViewById(R.id.SV1);
            ourHolder = mSurfaceView.getHolder();
            mSurfaceView.setOnTouchListener(this);
        //set up the time between clicks
            remainingTime = (ProgressBar) findViewById(R.id.pbEnergy) ;
            gameTimeAndScore = new GameTimeAndScore(remainingTime);
        //get device's width and height
            width= MainActivity.getWidth();
            height = MainActivity.getHeight();
        //sizes of red balls, this will get random sizes for every red ball
            for(i=0 ; i<BALL_NEGATIVE_NUMBER; i++) {randomSize[i] = randomNumber.nextInt(55);}
        //Bitmaps
            ball= BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
            goldBall =BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            ballHeight=ball.getHeight()+15;
            ballWidth=ball.getWidth()+15;
            negativeBall = new Bitmap[7];
        for(i=0; i<BALL_NEGATIVE_NUMBER; i++){
            negWidth[i]=negHeight[i] =  ballWidth-15+ randomSize[i];
            negativeBall[i]= BitmapFactory.decodeResource(getResources(), R.drawable.atomcrvena);
            negativeBall[i]=Bitmap.createScaledBitmap(negativeBall[i],negWidth[i], negHeight[i], true);
        }
            ball=Bitmap.createScaledBitmap(ball,ballWidth, ballHeight, true);
            goldBall=Bitmap.createScaledBitmap(goldBall,ballWidth, ballHeight, true);
            //background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            //background = Bitmap.createScaledBitmap(background, width, deviceHeight, true);
        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_THREE_HIGH_SCORE_KEY);
            newHighScore=false;

            initialDraw=true;
    }


    // --------------------------------------Main Game Action ------------------------------- \\

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //get coordinates where we touched
                clickedX =(int) event.getX();
                clickedY =(int) event.getY();
            //checks if we clicked some ball and does that balls action:
            //blue - score ; red - lose game ; gold - gain a life
                clickedPositive();
                clickedNegative();
                clickedGold();
        }
        return false;
    }

        // ------------------------------------------ Ball Click Actions ------------------------ \\

    /**
     * Method for doing positive ball's action on click: Positive gives score if clicked
     */
    private void clickedPositive(){
        //check if you clicked positive ball
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            //after we clicked this ball the progress bar is reset
            remainingClickTime = keys.SURVIVAL_CLICK_TIMER;
            //get score and a new ball
            score+=100;
            angle =  randomCoordinate.randomAngle();
            x = randomCoordinate.randomX();
            y = randomCoordinate.randomY();
        }
    }


    /**
     * Method for doing negative ball's action on click: Negative makes you lose the game
     * if you don't have a spare life, if you have a spare life, the life is lost.
     */
    private void clickedNegative(){
        //check if you clicked any negative ball
        for(j=0; j<BALL_NEGATIVE_NUMBER; j++){
        if(clickedABall.negativeBallClicked(XY[j], XY[j+BALL_NEGATIVE_NUMBER], clickedX, clickedY, negWidth[j], negHeight[j])) {
            //if you had a life lose it, but don't lose the game
            if(gotLife)
                gotLife = false;
            else
            gameover = true;
            }
        }
    }
    /**
     * Method for doing gold ball's action on click: Gold gives you a spare life.
     * A spare life should stop you from loosing the game if you click on a negative ball
     */
    private void clickedGold() {
        if(clickedABall.ballClicked(goldX,goldY,clickedX,clickedY)){
            //variable used to determine if gold ball will be drawn later, and if you should lose the game
            //upon clicking a negative ball
            gotLife = true;
            //remove the ball from screen
            goldX = goldY = 0- ballWidth;
        }
    }


     // ------------------------------------------- Main Game Loop ---------------------------------------- \\

    @Override
    public void run() {
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;

            //sets a timer for speeding up red balls, and for randomizing gold ball's appereance
            timedActions();
            //The initial draw
            if(initialDraw) {
              firstDraw();
            }
            //move every ball
            moveBall();
            moveNegativeBalls(XY);
            //at some random time a gold ball will be drawn
            goldDraw();
            //after the timer runs out finish the game
            endGame();
        }
    }

        // ---------------------------------- On Game Start ---------------------------------------- \\

    /**
     * Method for handling the game's timer. The timer runs in a endless loop.
     * <p>It should be used to draw a gold ball if we don't have a spare life, and to
     * periodically change the speed of red balls making the game harder.</p>
     */
    public void timedActions() {
        final int totalTimerTime = (int) keys.GOLD_BALL_TIMER/1000;
        //timer has to be started once, but it resets every time it finishes
        if (!startedTimer) {
            if(randomCoordinate != null) {
                //in case we haven't clicked the ball get new coordinates in every timer cycle
                goldX = randomCoordinate.randomX();
                goldY = randomCoordinate.randomY();
            }
            //get a handler so that we can run the timer outside of the main ui thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    if(!startedBallTimer){
                        positiveBallTimer();
                    }
                    new CountDownTimer(keys.GOLD_BALL_TIMER, 250) {
                        public void onTick(long millisUntilFinished) {
                            int seconds =(int) millisUntilFinished/1000;
                            //increase red balls speed in a certain interval
                            redBallSpeed = ballMovement.redBallsSpeedUp(redBallSpeed, seconds);
                            //5 seconds after the timer restarts, draw the gold ball
                            if(seconds < totalTimerTime-5){
                                //show gold ball for the duration of GOLD BALL DURATION, only if we don't have a life
                                if ( (totalTimerTime-5 - keys.GOLD_BALL_DURATION < seconds) && !gotLife) {
                                    //condition that determines if the gold ball should be drawn
                                    drawGoldBall = true;
                                } else drawGoldBall = false;
                            }
                            startedTimer = true;
                        }

                        public void onFinish() {
                            //after the timer reaches zero, it will be reset
                            startedTimer = false;
                        }
                    }.start();
                }
            });
        }
    }

    private void positiveBallTimer(){

                new CountDownTimer(keys.SURVIVAL_CLICK_TIMER, 50) {
                    public void onTick(long millisUntilFinished) {
                        //Every time the timer ticks reduce the progress bar a bit
                        //if a certain time passes and a positive ball isn't clicked, the game ends
                        remainingClickTime -= 50;
                        gameTimeAndScore.updateTimeBar(remainingClickTime);
                        if(remainingClickTime <=0){
                            //if we run out of time to click the ball, end the game
                            gameover = true;
                        }
                        startedTimer = true;
                    }

                    public void onFinish() {
                        //after the timer reaches zero, it will be reset
                        startedTimer = false;
                    }
                }.start();

    }

    /**
     * Method that should be called on every start of the game. Initializes most variables.
     * <p>Canvas, width and height, starting coordinates and angles, and the first draw should be
     * started here, only once.</p>
     */
    private void firstDraw(){
        //setting the proper height of the canvas
        mCanvas = ourHolder.lockCanvas();
        height = mCanvas.getHeight();
        ourHolder.unlockCanvasAndPost(mCanvas);
        //prevents drawing over screen
        stars = new Background(ourHolder, mCanvas, width, height);
        canvas = new Canvas3(ourHolder,mCanvas, stars);
        randomCoordinate = new RandomBallVariables(width, height, ballWidth, ballHeight);
        clickedABall= new ClickedABall(ballWidth, ballHeight);
        goldX = randomCoordinate.randomX();
        goldY = randomCoordinate.randomY();
        angle = randomCoordinate.randomAngle();
        for(i=0; i<BALL_NEGATIVE_NUMBER; i++){
            angles[i]= randomCoordinate.randomAngle();
        }
        x=randomCoordinate.randomX();
        y=randomCoordinate.randomY();
        XY=randomCoordinate.randomnegativeBallsCoordinates();
        //first draw
        initialDraw= canvas.draw(ball,negativeBall,x,y, XY, score);
    }


        // ---------------------------------- Ball Movement and Drawing ---------------------------- \\

    private void moveBall() {
            //need to create local because of a width and height problem. Fix later
            BallMovement positiveBallMovement = new BallMovement(width, height);
            //update blue ball's position
            ballMovementArray = positiveBallMovement.moveBall(x, y, ballWidth, ballHeight, moveX, moveY, angle, BALL_SPEED);
            //set x,y, moveX, moveY to new values
            x = ballMovementArray[keys.NEW_BALL_X_COORDINATE];
            y = ballMovementArray[keys.NEW_BALL_Y_COORDINATE];
            moveX = ballMovementArray[keys.NEW_BALL_MOVEX_VALUE];
            moveY = ballMovementArray[keys.NEW_BALL_MOVEY_VALUE];
    }
    private void moveNegativeBalls(int[] XY){
        //need to create local because of a width and height problem. Fix later
        BallMovement negBallMovement = new BallMovement(width, height);
        for(i=0; i<BALL_NEGATIVE_NUMBER; i++) {
            //for every red ball update its position
            ballMovementArray = negBallMovement.moveBall(XY[i], XY[i+BALL_NEGATIVE_NUMBER], ballWidth, ballHeight, negMoveX[i], negMoveY[i], angles[i], redBallSpeed);
           //set x,y, moveX, moveY to new values
            XY[i] = ballMovementArray[keys.NEW_BALL_X_COORDINATE];
            XY[i+BALL_NEGATIVE_NUMBER] = ballMovementArray[keys.NEW_BALL_Y_COORDINATE];
            negMoveX[i] = ballMovementArray[keys.NEW_BALL_MOVEX_VALUE];
            negMoveY[i] = ballMovementArray[keys.NEW_BALL_MOVEY_VALUE];
        }

    }
    private void goldDraw(){
        if(!drawGoldBall)
            canvas.draw(ball,negativeBall,x,y, XY, score);
        if(drawGoldBall){
            canvas.drawGold(ball,negativeBall,goldBall,x,y, XY, goldX, goldY, score);
        }
    }

            // --------------------------------- Game End ------------------------------------------ \\

    private void endGame(){
        if (gameover) {
            //if an event happens that changes gameover to true, end the game
            GameOver gameover = new GameOver(ourHolder,mCanvas);
            // check if we got a new high score
            newHighScore=highScore.isHighScore(HighScore.GAME_THREE_HIGH_SCORE_KEY, score);
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


}

