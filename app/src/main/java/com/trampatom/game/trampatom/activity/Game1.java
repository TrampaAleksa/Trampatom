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

import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Game1 extends AppCompatActivity implements Runnable, View.OnTouchListener{

    private static final long GAME_TIME = 60000;
    private static final int BALL_SPEED= 8;
    private static final int BALL_SIZE_ADAPT= 18;
    //used for handling drawing of yellow ball
    private static final int BALL_YELLOW_REQUIRED_CLICKS= 4;
    private static final int BALL_YELLOW_SPEED_INCREASE= 3;
    private static final int BALL_YELLOW_INITIAL_SPEED= 2;
    private static final int BALL_YELLOW_SIZE_DECREASE= 10;

    private static final int BALL_GREEN_SPEED= 16;
    //used for handling drawing of purple balls
    public static final int BALL_PURPLE_NO_CLICK= 1;
    public static final int BALL_PURPLE_ONE_CLICK= 2;

    //RED - don't click on the ball ; BLUE - click on the ball
    //GREEN - super crazy ball ; YELLOW - click it a few times
    //PURPLE splits into two after first click
    public static final int BALL_RED = 1;
    public static final int BALL_BLUE = 2;
    public static final int BALL_GREEN = 3;
    public static final int BALL_YELLOW = 4;
    public static final int BALL_PURPLE = 5;

    //Lists and Arrays
        Ball b;
        //array for moving the ball
        int[] moveArray= {0,0,0,0};
        int[] moveArrayGreen = {0,0,0,0,0};
    //Classes
        GameTimeAndScore gameTimeAndScore;
        RandomBallVariables randomCoordinate;
        Canvas1 canvas;
        HighScore highScore;
        BallType newBall;
        ClickedABall clickedABall;
        BallMovement ballMovement;
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
    //used for yellow ball;
        int yellowBallSpeed = BALL_YELLOW_INITIAL_SPEED;
        boolean changedSize=false;
    //used for purple ball
        int[] XY={0,0,0,0};
        int thirdMoveX= 1; int thirdMoveY=1;
        int otherMoveX= 1; int otherMoveY = 1;
        int timesClickedPurple=BALL_PURPLE_NO_CLICK;
        boolean originalBallClicked= false; boolean secondBallClicked=false; boolean thirdBallClicked=false;
    //used for moving the ball
        int moveX = 1;
        int moveY = 1;
        Random greenRandom;
        int changeAngleGreen;
        double angle, secondAngle, thirdAngle;
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
            yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
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
        initialDraw= canvas.draw(blueBall,x,y);
    }

    @Override
    public void run() {
        canvas = new Canvas1(ourHolder,mCanvas, background);
        greenRandom = new Random();
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;

            //The initial draw
            if(initialDraw) {
                initiateOnCanvas();
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
                    if(!changedSize) {
                        //resets the yellow ball to its original size when first drawing it
                        ballWidth = ballHeight = ballWidth + 25;
                        yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                        changedSize=true;
                    }
                    moveYellowBall();
                    canvas.draw(yellowBall, x, y);
                    break;
                case BALL_GREEN:
                    //this ball moves like crazy
                    moveGreenBall();
                    canvas.draw(greenBall, x, y);
                    break;
                case BALL_PURPLE:
                    movePurpleBall();
                    canvas.drawPurple(purpleBall, x, y, XY, timesClickedPurple);
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

    //TODO use arrays and for loop to move all three balls!
    private void moveBall() {
       moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, BALL_SPEED);
        x= moveArray[0];
        y = moveArray[1];
        moveX = moveArray[2];
        moveY = moveArray[3];
    }
    private void moveSecondBall() {
        moveArray = ballMovement.moveBall(XY[0],XY[1],ballWidth, ballHeight, otherMoveX, otherMoveY, secondAngle, BALL_SPEED);
        XY[0]= moveArray[0];
        XY[1] = moveArray[1];
        otherMoveX = moveArray[2];
        otherMoveY = moveArray[3];
    }
    private void moveThirdBall() {
        moveArray = ballMovement.moveBall(XY[2],XY[3],ballWidth, ballHeight, thirdMoveX, thirdMoveY, thirdAngle, BALL_SPEED);
        XY[2]= moveArray[0];
        XY[3] = moveArray[1];
        thirdMoveX = moveArray[2];
        thirdMoveY = moveArray[3];
    }

    /**
     * yellow ball moves very slowly but speeds up with every click
     */
    private void moveYellowBall(){
        moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, yellowBallSpeed);
        x= moveArray[0];
        y = moveArray[1];
        moveX = moveArray[2];
        moveY = moveArray[3];
    }
    private void moveGreenBall(){
        int changeAngleGreen = greenRandom.nextInt(350);
        if(changeAngleGreen<=20){
            angle= randomCoordinate.randomAngle();
        }
        moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, BALL_GREEN_SPEED);
        x= moveArray[0];
        y = moveArray[1];
        moveX = moveArray[2];
        moveY = moveArray[3];

    }
    private void movePurpleBall(){
        if(timesClickedPurple==BALL_PURPLE_NO_CLICK){
            moveBall();
        }
        else {
            //move the original ball
            if (!originalBallClicked)
                moveBall();
            if (!secondBallClicked) {
                //move the other ball
                moveSecondBall();
            }
            if(!thirdBallClicked){
                moveThirdBall();
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

    private void getNewBall() {
        b = newBall.getNewBall();
        x= b.getX();
        y= b.getY();
        ballType = b.getBallType();
        angle = b.getAngle();
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


    private void redBall(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score -= 100;
            getNewBall();
        }
        else {
            score+=100;
            getNewBall();
        }
    }
    private void blueBall(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)) {
            score += 100;
            getNewBall();
        }
    }
    private void yellowBall(){

        //every time the ball is clicked decrease its size and increase speed
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)) {
            if (timesClicked > 0) {
                timesClicked--;
                clickedYellowBall();
            }
            else {
                score+=500;
                getNewBall();
                //reset the yellow ball to its first state for later use
                timesClicked=BALL_YELLOW_REQUIRED_CLICKS;
                ballWidth=ballHeight= blueBall.getWidth();
                yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                yellowBallSpeed=BALL_YELLOW_INITIAL_SPEED;
                changedSize=false;
            }
        }
    }
    /**
     * Method that reduces yellow ball's size and increases its speed every time it's called
     */
    private void clickedYellowBall(){
        ballWidth -= BALL_YELLOW_SIZE_DECREASE;
        ballHeight -= BALL_YELLOW_SIZE_DECREASE;
        yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
        yellowBallSpeed+= BALL_YELLOW_SPEED_INCREASE;
    }
    private void greenBall(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score+=400;
            getNewBall();
        }
    }
    private void purpleBall() {
        //if we clicked on the first ball
        if (timesClickedPurple == BALL_PURPLE_NO_CLICK) {
            if (clickedABall.ballClicked(x, y, clickedX, clickedY)) {
                timesClickedPurple = BALL_PURPLE_ONE_CLICK;
                angle = randomCoordinate.randomAngle();
                secondAngle = randomCoordinate.randomAngle();
                thirdAngle = randomCoordinate.randomAngle();
                XY[0] = x;          XY[1] = y;
                XY[2] = x;          XY[3] = y;
            }
        }
        //if we clicked on one of the split balls
        else  {
            if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            x=0-ballWidth;
            y=0-ballHeight;
            originalBallClicked=true;
            }
            if(clickedABall.ballClicked(XY[0],XY[1],clickedX,clickedY)){
                XY[0]=0-ballWidth;
                XY[1]=0-ballHeight;
                secondBallClicked=true;
            }
            if(clickedABall.ballClicked(XY[2],XY[3],clickedX,clickedY)){
                XY[2]=0-ballWidth;
                XY[3]=0-ballHeight;
                thirdBallClicked=true;
            }
            if(originalBallClicked && secondBallClicked && thirdBallClicked){
                timesClickedPurple = BALL_PURPLE_NO_CLICK;
                originalBallClicked = secondBallClicked = thirdBallClicked = false;
                score +=500;
                getNewBall();
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