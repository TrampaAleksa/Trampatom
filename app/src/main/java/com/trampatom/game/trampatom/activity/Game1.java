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

    //Lists and Arrays
        Ball b;
        //array for moving the ball
        int i;
        int[] moveArray= {0,0,0,0};
        //arrays for purple ball
        int [] purpleXY = {0,0,0,0,0,0};
        double [] purpleAngles= {0,0,0};
        int [] purpleMoveXY= {1,1,1,1,1,1};
        //array for wave balls
        Bitmap[] waveBall;
        int[] waveXY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] moveWaveXY = {1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    //Classes
        Keys keys;
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
        //for moving the background
        int backgroundHalf1 = 0;
    //Other variables
        TextView tvScore, tvTime;
        int width, height;
        static int ballWidth, ballHeight;
    //Used for scoring
        int ballType=4, currentBall;
        int timesClicked;
        int score, previousHighScore;
        String scoreS;
    //coordinates of the currently drawn ball, coordinates where we clicked
        int x, clickedX;
        int y, clickedY;
    //used for yellow ball;
        int yellowBallSpeed;
        boolean changedSize=false;
    //used for purple ball
        int ballPurpleNumber =1;
        int timesClickedPurple;
        boolean originalBallClicked= false; boolean secondBallClicked=false; boolean thirdBallClicked=false;
    //used for wave ball
        int currentWaveBall = 0;
    //used for moving the ball
        int moveX = 1;
        int moveY = 1;
        Random greenRandom;
        double angle;
    //used for drawing the first ball , used to start the timer once
        boolean initialDraw, startedTimer;
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
    }

    private void init() {
        keys = new Keys();
        timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
        yellowBallSpeed = keys.BALL_YELLOW_INITIAL_SPEED;
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
        currentBall= BALL_BLUE;
        initialDraw=true;
    }
    // TODO make method for initiating all bitmaps
    private void initiateBitmaps(){
        //initiate bitmaps
            waveBall = new Bitmap[7];
            waveBall[0] = BitmapFactory.decodeResource(getResources(),R.drawable.wave1);
            waveBall[1] = BitmapFactory.decodeResource(getResources(),R.drawable.wave2);
            waveBall[2] = BitmapFactory.decodeResource(getResources(),R.drawable.wave3);
            waveBall[3] = BitmapFactory.decodeResource(getResources(),R.drawable.wave4);
            waveBall[4] = BitmapFactory.decodeResource(getResources(),R.drawable.wave5);
            waveBall[5] = BitmapFactory.decodeResource(getResources(),R.drawable.wave6);
            waveBall[6] = BitmapFactory.decodeResource(getResources(),R.drawable.wave7);
            blueBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
            redBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomcrvena);
            greenBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomzelena);
            yellowBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            purpleBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomroze);
            background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
        //ball Height and Width
            ballHeight= blueBall.getHeight()+keys.BALL_SIZE_ADAPT;
            ballWidth= blueBall.getWidth()+keys.BALL_SIZE_ADAPT;
        //resize all balls to the new ball width and height
            blueBall = Bitmap.createScaledBitmap(blueBall, ballWidth, ballHeight, true);
            redBall = Bitmap.createScaledBitmap(redBall, ballWidth, ballHeight, true);
            greenBall = Bitmap.createScaledBitmap(greenBall, ballWidth, ballHeight, true);
            yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
            purpleBall = Bitmap.createScaledBitmap(purpleBall, ballWidth, ballHeight, true);
        for(i=0; i<keys.WAVE_BALL_NUMBER; i++){
            waveBall[i] =  Bitmap.createScaledBitmap(waveBall[i], ballWidth, ballHeight, true);
        }
    }

    @Override
    public void run() {
        canvas = new Canvas1(ourHolder,mCanvas, background);
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
        initialDraw= canvas.draw(blueBall,x,y, backgroundHalf1);
    }
    /**
     * draw a ball after the score changes depending on the type
     */
    public void moveAndDraw(){
        backgroundHalf1--;
        switch(currentBall)
        {
            case BALL_BLUE:
                moveBall();
                canvas.draw(blueBall, x, y, backgroundHalf1);
                break;
            case BALL_RED:
                moveBall();
                canvas.draw(redBall, x, y, backgroundHalf1);
                break;
            case BALL_YELLOW:
                if(!changedSize) {
                    //resets the yellow ball to its original size when first drawing it
                    ballWidth = ballHeight = ballWidth + keys.YELLOW_BALL_INITIAL_SIZE;
                    yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                    changedSize=true;
                }
                moveYellowBall();
                canvas.draw(yellowBall, x, y, backgroundHalf1);
                break;
            case BALL_GREEN:
                //this ball moves like crazy
                moveGreenBall();
                canvas.draw(greenBall, x, y, backgroundHalf1);
                break;
            case BALL_PURPLE:
                movePurpleBall();
                canvas.drawPurple(purpleBall, purpleXY, timesClickedPurple, backgroundHalf1);
                break;
            case BALL_WAVE:
                moveWave();
                canvas.drawWave(waveBall, waveXY,backgroundHalf1 );
                break;
        }
    }
    private void moveBall() {
       moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, keys.BALL_SPEED);
        x= moveArray[keys.NEW_BALL_X_COORDINATE];
        y = moveArray[keys.NEW_BALL_Y_COORDINATE];
        moveX = moveArray[keys.NEW_BALL_MOVEX_VALUE];
        moveY = moveArray[keys.NEW_BALL_MOVEY_VALUE];
    }
    /**
     * yellow ball moves very slowly but speeds up with every click
     */
    private void moveYellowBall(){
        moveArray = ballMovement.moveBall(x,y,ballWidth, ballHeight, moveX, moveY, angle, yellowBallSpeed);
        x= moveArray[keys.NEW_BALL_X_COORDINATE];
        y = moveArray[keys.NEW_BALL_Y_COORDINATE];
        moveX = moveArray[keys.NEW_BALL_MOVEX_VALUE];
        moveY = moveArray[keys.NEW_BALL_MOVEY_VALUE];
    }
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
     * manages movement of all purple balls using three movement methods
     */
    private void movePurpleBall(){

        //move every ball by its angle
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
                ballPurpleNumber = keys.PURPLE_BALL_NUMBER;
                //remove the balls that were clicked from the screen
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
    private void moveWave(){
        for(i=currentWaveBall; i<keys.WAVE_BALL_NUMBER; i++){
            moveArray = ballMovement.moveWave(waveXY[i], waveXY[i+keys.WAVE_BALL_NUMBER],
                    ballWidth, moveWaveXY[i], moveWaveXY[i+keys.WAVE_BALL_NUMBER],keys.BALL_SPEED+i);
            waveXY[i]= moveArray[keys.NEW_BALL_X_COORDINATE];
            waveXY[i+keys.WAVE_BALL_NUMBER] = moveArray[keys.NEW_BALL_Y_COORDINATE];
            moveWaveXY[i] = moveArray[keys.NEW_BALL_MOVEX_VALUE];
            moveWaveXY[i+keys.WAVE_BALL_NUMBER] = moveArray[keys.NEW_BALL_MOVEY_VALUE];
        }
    }
    private void endGame(){
        if (gameover) {
            GameOver gameover = new GameOver(ourHolder,mCanvas);
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

    /**
     * used to set the ball color to be drawn
     * @param ballType used to determine what color to draw
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
                timesClicked=keys.BALL_YELLOW_REQUIRED_CLICKS;
                ballWidth=ballHeight= blueBall.getWidth();
                yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
                yellowBallSpeed=keys.BALL_YELLOW_INITIAL_SPEED;
                changedSize=false;
            }
        }
    }
    /**
     * Method that reduces yellow ball's size and increases its speed every time it's called
     */
    private void clickedYellowBall(){
        ballWidth -= keys.BALL_YELLOW_SIZE_DECREASE;
        ballHeight -= keys.BALL_YELLOW_SIZE_DECREASE;
        yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
        yellowBallSpeed+= keys.BALL_YELLOW_SPEED_INCREASE;
    }
    private void greenBall(){
        if(clickedABall.ballClicked(x,y,clickedX,clickedY)){
            score+=400;
            getNewBall();
        }
    }
    private void purpleBall() {
        if (timesClickedPurple == keys.BALL_PURPLE_NO_CLICK) {
            //if we clicked on the first ball
            if (clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY1], purpleXY[keys.PURPLE_BALL_NUMBER], clickedX, clickedY)) {
                timesClickedPurple = keys.BALL_PURPLE_ONE_CLICK;
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
                originalBallClicked=true;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY2],purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY2]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY2+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                secondBallClicked=true;
            }
            if(clickedABall.ballClicked(purpleXY[keys.PURPLE_BALL_XY3],purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER],clickedX,clickedY)){
                purpleXY[keys.PURPLE_BALL_XY3]=0-ballWidth;
                purpleXY[keys.PURPLE_BALL_XY3+keys.PURPLE_BALL_NUMBER]=0-ballHeight;
                thirdBallClicked=true;
            }
            if(originalBallClicked && secondBallClicked && thirdBallClicked){
                originalBallClicked = secondBallClicked = thirdBallClicked = false;
                timesClickedPurple = keys.BALL_PURPLE_NO_CLICK;
                score +=500;
                getNewBall();
            }
        }
        }
    private void waveBall(){
    //TODO multiple click ball types should update score with every click
        //if we click the correct ball in the sequence remove it and get score
        if(clickedABall.ballClicked(waveXY[currentWaveBall], waveXY[currentWaveBall+keys.WAVE_BALL_NUMBER], clickedX, clickedY)){
            waveXY[currentWaveBall] = -ballWidth;
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