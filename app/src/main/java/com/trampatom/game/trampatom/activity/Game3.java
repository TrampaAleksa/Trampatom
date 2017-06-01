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

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.BallMovement;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Canvas3;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Game3 extends AppCompatActivity implements Runnable, View.OnTouchListener{

    private static final int BALL_SPEED= 8;
    public static final int BALL_NEGATIVE_NUMBER = 7;
    //Arrays
    int[] ballMovementArray = {0,0,0,0};
    int[] randomSize={0,0,0,0,0,0,0};
    int[] negWidth={0,0,0,0,0,0,0};
    int[] negHeight={0,0,0,0,0,0,0};
    //14 coordinates ; 7 red balls
    //TODO use lists / arrays to get coordinates
    int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    //Classes
    GameTimeAndScore gameTimeAndScore;
    RandomBallVariables randomCoordinate;
    Canvas3 canvas;
    HighScore highScore;
    ClickedABall clickedABall;
    Keys keys;
    BallMovement ballMovement;
    Random randomRedSize;
    //For the SurfaceView to work
    SurfaceHolder ourHolder;
    SurfaceView mSurfaceView;
    Canvas mCanvas;
    Thread ourThread = null;
    boolean isRunning=true;
    //Balls and Background Bitmaps
    Bitmap ball;
    Bitmap[] negativeBall;
    Bitmap goldBall;
    //Other variables
    TextView tvScore, tvTime;
    int width, height, j,i;
    int ballWidth, ballHeight;
    //Used for scoring
    int score, previousHighScore;
    //coordinates of the currently drawn ball
    int x, clickedX;
    int y, clickedY;
    //used for golden ball
        //set at -1 to prevent clicking on it before it is drawn
        int goldX=-1; int goldY=-1;
        //time when the ball will be drawn
        int goldBallTime;
        //spare life
        boolean gotLife = false;
        //used for determining when to draw the ball
        boolean drawGoldBall= false;
    //used for moving balls
        int moveX= 1;int moveY = 1;
        int[] negMoveX={1,1,1,1,1,1,1};
        int[] negMoveY={1,1,1,1,1,1,1};
        double angle;
        double[] angles= {0,0,0,0,0,0,0,0};
        int redBallSpeed = 8;
    //used for drawing the first ball, start timer only after canvasLoads
    boolean initialDraw, startedTimer = false;
    //used for ending the game when the time ends
    boolean gameover, newHighScore;


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

    private void init() {
        //Classes
        keys = new Keys();
        ballMovement = new BallMovement(width, height);
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
        //used for gold ball
        randomRedSize = new Random();
        goldBallTime = randomRedSize.nextInt(55);
        //sizes of red balls
        for(i=0 ; i<BALL_NEGATIVE_NUMBER; i++) {randomSize[i] = randomRedSize.nextInt(55);}
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //get coordinates where we touched
            clickedX =(int) event.getX();
            clickedY =(int) event.getY();
            //if we click on the ball
            clickedPositive();
            clickedNegative();
            clickedGold();
        }
        return false;
    }

    //checks if we clicked some ball and does that balls action:
    //blue - score ; red - lose game ; gold - gain a life
    private void clickedPositive(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score+=100;
            angle =  randomCoordinate.randomAngle();
            x = randomCoordinate.randomX();
            y = randomCoordinate.randomY();
            gameTimeAndScore.setScore(score);
        }
    }
    private void clickedNegative(){

        for(j=0; j<BALL_NEGATIVE_NUMBER; j++){
        if(clickedABall.negativeBallClicked(XY[j], XY[j+BALL_NEGATIVE_NUMBER], clickedX, clickedY, negWidth[j], negHeight[j])) {
            if(gotLife)
                gotLife = false;
            else
            gameover = true;
            }
        }
    }
    private void clickedGold() {
        if(clickedABall.ballClicked(goldX,goldY,clickedX,clickedY)){
            gotLife = true;
            goldX = goldY = 0- ballWidth;
        }
    }


    @Override
    public void run() {
        canvas = new Canvas3(ourHolder,mCanvas);
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

    public void timedActions() {
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
                    new CountDownTimer(keys.GOLD_BALL_TIMER, 250) {
                        public void onTick(long millisUntilFinished) {
                            int seconds =(int) millisUntilFinished/1000;
                            int totalTimerTime = (int) keys.GOLD_BALL_TIMER/1000;
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
    private void firstDraw(){
        //setting the proper height of the canvas
        mCanvas = ourHolder.lockCanvas();
        height = mCanvas.getHeight();
        ourHolder.unlockCanvasAndPost(mCanvas);
        //prevents drawing over screen
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
        initialDraw= canvas.draw(ball,negativeBall,x,y, XY);
    }
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
            canvas.draw(ball,negativeBall,x,y, XY);
        if(drawGoldBall){
            canvas.drawGold(ball,negativeBall,goldBall,x,y, XY, goldX, goldY);
        }
    }
    private void endGame(){
        if (gameover) {
            GameOver gameover = new GameOver(ourHolder,mCanvas);
            newHighScore=highScore.isHighScore(HighScore.GAME_THREE_HIGH_SCORE_KEY, score);
            gameover.gameOver(score, newHighScore);
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    }







    //used for handling the game's Runnable thread
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

