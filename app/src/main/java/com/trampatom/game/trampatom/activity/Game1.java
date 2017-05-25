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

    private static final long GAME_TIME = 25000;
    private static final int BALL_SPEED= 8;
    private static final int BALL_SIZE_ADAPT= 18;
    private static final int BALL_YELLOW_REQUIRED_CLICKS= 4;
    private static final int BALL_GREEN_SPEED= 20;
    //used for handling drawing of purple balls
    public static final int BALL_PURPLE_NO_CLICK= 1;
    public static final int BALL_PURPLE_ONE_CLICK= 2;
    public static final int BALL_PURPLE_TWO_CLICK_ORIGINAL= 3;
    public static final int BALL_PURPLE_TWO_CLICK_SECOND= 4;

    //RED - don't click on the ball ; BLUE - click on the ball
    //GREEN - super crazy ball ; YELLOW - click it a few times
    //PURPLE splits into two after first click
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
        int timesClicked=BALL_YELLOW_REQUIRED_CLICKS;
        int score, previousHighScore;
    //coordinates of the currently drawn ball, coordinates where we clicked
        int x, clickedX;
        int y, clickedY;
    //used for purple ball
        int[] XY={0,0};
        int otherMoveX= 1; int otherMoveY = 1;
        int clicked=1;
        boolean originalBallClicked= false; boolean secondBallClicked=false;
    //used for moving the ball
        int moveX = 1;
        int moveY = 1;
        double angle, secondAngle;
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
        //TODO remove score getting updated in the timer
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
            greenBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomzelena);
            yellowBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            purpleBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomroze);
            background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            background = Bitmap.createScaledBitmap(background, width, height, true);
        //ball Height and Width
            ballHeight= blueBall.getHeight()+BALL_SIZE_ADAPT;
            ballWidth= blueBall.getWidth()+BALL_SIZE_ADAPT;
            blueBall = Bitmap.createScaledBitmap(blueBall, ballWidth, ballHeight, true);
            redBall = Bitmap.createScaledBitmap(redBall, ballWidth, ballHeight, true);
            greenBall = Bitmap.createScaledBitmap(greenBall, ballWidth, ballHeight, true);
            yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight+BALL_SIZE_ADAPT, true);
            purpleBall = Bitmap.createScaledBitmap(purpleBall, ballWidth, ballHeight, true);
        //Initial coordinates for the ball

        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
            newHighScore=false;
        currentBall= BALL_BLUE;
        initialDraw=true;
        scored=false;
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

            //draw a ball after the score changes depending on the type
            switch(currentBall)
            {
                case BALL_BLUE:
                    moveBall();
                    canvas.draw(blueBall, x, y);
                    break;
                case BALL_RED:
                    moveBall();
                    canvas.draw(redBall, x, y);
                    break;
                case BALL_YELLOW:
                    //this ball doesn't move
                    canvas.draw(yellowBall, x, y);
                    break;
                case BALL_GREEN:
                    //this ball moves like crazy
                    moveGreenBall();
                    canvas.draw(greenBall, x, y);
                    break;
                case BALL_PURPLE:
                    movePurpleBall();
                    canvas.drawPurple(purpleBall, x, y, XY, clicked);
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
    private void moveGreenBall(){
        x += moveX*BALL_GREEN_SPEED * Math.sin(angle);
        y += moveY*BALL_GREEN_SPEED * Math.cos(angle);

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
    private void movePurpleBall(){
        if(clicked==BALL_PURPLE_NO_CLICK){
            moveBall();
        }
        else{
            //move the original ball
            moveBall();
            //move the other ball
            XY[0] += otherMoveX*BALL_SPEED * Math.sin(secondAngle);
            XY[1] += otherMoveY*BALL_SPEED * Math.cos(secondAngle);

            //if the ball is off screen change its direction
            if(XY[0] > width-ballWidth) {
                XY[0] = width-ballWidth;
                otherMoveX = -otherMoveX;
                // too far right
            }
            if(XY[1] > height-ballHeight) {
                XY[1] = height-ballHeight;
                otherMoveY = -otherMoveY;
                // too far bottom
            }
            if(XY[0] < 0) {
                XY[0] = 0;
                otherMoveX = -otherMoveX;
                // too far left
            }
            if(XY[1] < 0) {
                XY[1] = 0;
                otherMoveY = -otherMoveY;
                // too far top
            }
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
                    yellowBall();
                }
                if(ballType>13 && ballType<=15){
                    greenBall();
                }
                if(ballType>15 && ballType<=18){
                    purpleBall();
                }
            setCurrentBall(ballType);
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
        ballType= number3.nextInt(18);
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
            currentBall=BALL_YELLOW;
        }
        if(ballType>13 && ballType<=15){
            currentBall=BALL_GREEN;
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
    }
    private void blueBall(){
        if(clickedABall(x,y,clickedX,clickedY)) {
            score += 100;
            getNewBall();
        }
    }
    private void yellowBall(){
        if(clickedABall(x,y,clickedX,clickedY)) {
            if (timesClicked > 0) {
                timesClicked--;
            }
            else {
                score+=500;
                getNewBall();
                timesClicked=BALL_YELLOW_REQUIRED_CLICKS;
            }
        }
    }
    private void greenBall(){
        if(clickedABall(x,y,clickedX,clickedY)){
            score+=400;
            getNewBall();
        }
    }
    private void purpleBall(){
        //if we clicked on the first ball
        if(clicked==BALL_PURPLE_NO_CLICK) {
            if (clickedABall(x, y, clickedX, clickedY)) {
                clicked = BALL_PURPLE_ONE_CLICK;
                angle = randomCoordinate.randomAngle();
                secondAngle= randomCoordinate.randomAngle();
                XY[0]=x;
                XY[1]=y;
            }
        }
        //if we clicked on one of the split balls
                else {
                    if(clickedABall(x,y,clickedX, clickedY)){
                        originalBallClicked= true;
                        clicked=BALL_PURPLE_TWO_CLICK_ORIGINAL;
                        //if this ball was the second one clicked draw a new ball and score
                        if(secondBallClicked){
                            score+=400;
                            getNewBall();
                            clicked=BALL_PURPLE_NO_CLICK;
                            originalBallClicked=secondBallClicked=false;
                        }
                    }
                    if(clickedABall(XY[0], XY[1], clickedX, clickedY)){
                        secondBallClicked= true;
                        clicked=BALL_PURPLE_TWO_CLICK_SECOND;
                        if(originalBallClicked){
                            score+=400;
                            getNewBall();
                            clicked=BALL_PURPLE_NO_CLICK;
                            originalBallClicked=secondBallClicked=false;
                        }
                    }
            }
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

