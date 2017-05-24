package com.trampatom.game.trampatom.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.trampatom.game.trampatom.canvas.Canvas1;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomCoordinate;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Game1 extends AppCompatActivity implements Runnable, View.OnTouchListener{
    //Duration of the game
    private static final long GAME_TIME = 25000;
    private static final int BALL_SPEED= 8;

    private static final int BALL_RED = 1;
    private static final int BALL_BLUE = 2;
    private static final int BALL_GREEN = 3;
    private static final int BALL_YELLOW = 4;
    private static final int BALL_PURPLE = 5;

    //Classes
        GameTimeAndScore gameTimeAndScore;
        RandomCoordinate randomCoordinate;
        Canvas1 canvas;
        HighScore highScore;
    //For the SurfaceView to work
        SurfaceHolder ourHolder;
        SurfaceView mSurfaceView;
        Canvas mCanvas;
        Thread ourThread = null;
        boolean isRunning=true;
    //Balls and Background Bitmaps
        Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall, background;
    //Other variables
        TextView tvScore, tvTime;
        int width, height;
        static int ballWidth, ballHeight;
    //Used for scoring
        int ballType=4, currentBall;
        int previous=4;
        int score, previousHighScore;
    //coordinates of the currently drawn ball, coordinates where we clicked
        int x, clickedX;
        int y, clickedY;
    //used for moving the ball
        int moveX = 1;
        int moveY = 1;
        double angle;
    //used for drawing the first ball
        boolean initialDraw;
    //used for drawing a new ball whenever we scored
        boolean scored;
    //used for ending the game when the time ends, congratulations if new high score
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
        new CountDownTimer(GAME_TIME, 250) {
            public void onTick(long millisUntilFinished) {
                gameTimeAndScore.setTimeAndScore(millisUntilFinished, score);
            }
            public void onFinish() {
                //finish the game when the timer ends
                gameover= true;
            }
        }.start();

    }

    private void init() {

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
        //Bitmaps
            blueBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
            redBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomcrvena);
            greenBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomnarandzasta);
            yellowBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            purpleBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomsiva);
            background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            background = Bitmap.createScaledBitmap(background, width, height, true);
        //ball Height and Width
            ballHeight= blueBall.getHeight();
            ballWidth= blueBall.getWidth();
        //Initial coordinates for the ball

        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
            newHighScore=false;
        currentBall= BALL_BLUE;
        initialDraw=true;
        scored=false;
    }

    void startTimer(){

    }


    @Override
    public void run() {
        canvas = new Canvas1(ourHolder,mCanvas, background);
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;

            //The initial draw
            if(initialDraw) {
                mCanvas = ourHolder.lockCanvas();
                height = mCanvas.getHeight();
                ourHolder.unlockCanvasAndPost(mCanvas);
                randomCoordinate = new RandomCoordinate(width, height, ballWidth, ballHeight);
                angle=randomCoordinate.randomAngle();
                x=randomCoordinate.randomX();
                y=randomCoordinate.randomY();
                initialDraw= canvas.draw(blueBall,x,y);
            }

            moveBall();
            //draw a ball after the score changes depending on the type
            switch(currentBall)
            {
                case BALL_BLUE:
                    canvas.draw(blueBall, x, y);
                    break;
                case BALL_RED:
                    canvas.draw(redBall, x, y);
                    break;
                case BALL_GREEN:
                    canvas.draw(blueBall, x, y);
                    break;
                case BALL_YELLOW:
                    canvas.draw(blueBall, x, y);
                    break;
                case BALL_PURPLE:
                    canvas.draw(blueBall, x, y);
                    break;
            }
            //after the timer runs out finish the game
            if (gameover) {
                GameOver gameover = new GameOver(ourHolder,mCanvas);
                //set the high score if there is a new one
                newHighScore=highScore.isHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY, score);
                gameover.gameOver(score, newHighScore);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }
    private void moveBall() {
        x += moveX*BALL_SPEED * Math.sin(angle);
        y += moveY*BALL_SPEED * Math.cos(angle);

        //if the ball is off screen change its direction
        if(x > width-ballWidth) {
            x = width-ballWidth;
            moveX = -moveX;
            // too far right
        }
        if(y > height-ballHeight) {
            y = height-ballHeight;
            moveY = -moveY;
            // too far bottom
        }
        if(x < 0) {
            x = 0;
            moveX = -moveX;
            // too far left
        }
        if(y < 0) {
            y = 0;
            moveY = -moveY;
            // too far top
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //get coordinates where we touched
            clickedX = (int) event.getX();
            clickedY = (int) event.getY();
            //if we click on the ball do something depending on the ball type
                if(ballType<=2){
                    redBall();
                }
                if(ballType>2 && ballType<=10){
                    blueBall();
                }
                if(ballType>10 && ballType<=13){

                }
                if(ballType>13 && ballType<=15){

                }
                if(ballType>15 && ballType<=18){

                }
        }
        return false;
    }

// TODO Create a class for working with balls/part of ball logic
    private void getNewBall() {
        //sets new coordinates
        angle=randomCoordinate.randomAngle();
        x = randomCoordinate.randomX();
        y = randomCoordinate.randomY();

        //determines if the ball will be positive or negative
        Random number3= new Random();
        ballType= number3.nextInt(10);
    }

    /**
     * used to set the ball color to be drawn
     * @param ballType used to determine what color to draw
     */
    private void setCurrentBall(int ballType){
        //if we click on the ball do something depending on the ball type
        if(ballType<=2){
            currentBall=BALL_RED;
        }
        if(ballType>2 && ballType<=10){
            currentBall=BALL_BLUE;
        }
        if(ballType>10 && ballType<=13){
            currentBall=BALL_GREEN;
        }
        if(ballType>13 && ballType<=15){
            currentBall=BALL_YELLOW;
        }
        if(ballType>15 && ballType<=18){
            currentBall=BALL_PURPLE;
        }
    }

    /**
     * checks if a ball was clicked
     * @param x x coordinate of the ball
     * @param y y coordinate of the ball
     * @param clickedX x where we clicked
     * @param clickedY y where we clicked
     * @return returns true if we clicked the ball
     */
    private boolean clickedABall(int x, int y, int clickedX, int clickedY){
        if(clickedX>x && clickedX<(x+ballWidth) && clickedY>y && clickedY<(y+ballHeight))
            return true;
        else
            return false;
    }

    private void redBall(){
        if(clickedABall(x,y,clickedX,clickedY)){
            score -= 100;
            getNewBall();
        }
        else {
            score+=100;
            getNewBall();
        }
        setCurrentBall(ballType);
    }
    private void blueBall(){
        if(clickedABall(x,y,clickedX,clickedY)) {
            score += 100;
            getNewBall();
        }
        setCurrentBall(ballType);
    }
    private void greenBall(){

    }
    private void yellowBall(){

    }
    private void purpleBall(){

    }




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

