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
    private static final int BALL_GOLD_DURATION= 3;

    private static final int BALL_NEGATIVE_SPEED= 10;
    public static final int BALL_NEGATIVE_NUMBER = 7;
    //Classes
    GameTimeAndScore gameTimeAndScore;
    RandomBallVariables randomCoordinate;
    Canvas3 canvas;
    HighScore highScore;
    ClickedABall clickedABall;
    Keys keys;
    BallMovement ballMovement;
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
    //14 coordinates ; 7 red balls
    //TODO use lists / arrays to get coordinates
    int[] XY= {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    //random sizes of balls
    Random randomRedSize;
    int[] randomSize={0,0,0,0,0,0,0};
    int[] negWidth={0,0,0,0,0,0,0};
    int[] negHeight={0,0,0,0,0,0,0};
    //used for golden ball
        //set at -1 to prevent clicking on it before it is drawn
        int goldX=-1; int goldY=-1;
        //time when the ball will be drawn
        int goldBallTime;
        //used for determining when to draw the ball
        boolean drawGoldBall= false;
        boolean drewGoldBall = false;
    //used for moving balls
        int moveX, moveY;
        int[] negMoveX={1,1,1,1,1,1,1};
        int[] negMoveY={1,1,1,1,1,1,1};
        double angle;
        double[] angles= {0,0,0,0,0,0,0,0};
        int redBallSpeed = 8;
    //used for drawing the first ball, start timer only after canvasLoads
    boolean initialDraw;
    //used for drawing a new ball whenever we scored
    boolean scored;
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
        new CountDownTimer(keys.GOLD_BALL_TIMER, 250) {
            public void onTick(long millisUntilFinished) {
                //increase red balls speed in a certain interval
               redBallSpeed = ballMovement.redBallsSpeedUp(redBallSpeed,(int) millisUntilFinished/1000);
              /*  if (((millisUntilFinished / 1000) < goldBallTime)) {
                    if ( (goldBallTime - BALL_GOLD_DURATION < (millisUntilFinished / 1000)) && !drewGoldBall) {
                        drawGoldBall = true;
                    } else drewGoldBall = true;
                }*/
            }

            public void onFinish() {

            }
        }.start();
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
        //movement of the ball
            moveX=1;
            moveY=1;
        //used for gold ball
        randomRedSize = new Random();
        goldBallTime = randomRedSize.nextInt(55);
        //sizes of red balls
        for(i=0 ; i<BALL_NEGATIVE_NUMBER; i++) {
            randomSize[i] = randomRedSize.nextInt(55);
        }
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
            scored=false;
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


    //TODO remove scored variable
    private void clickedGold() {
        if(clickedABall.ballClicked(goldX,goldY,clickedX,clickedY)){
            score+=800;
            scored=true;
            goldX = goldY = 0- ballWidth;
        }
    }

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
            gameover = true;
            }
        }
    }


    @Override
    public void run() {
        canvas = new Canvas3(ourHolder,mCanvas);
        int i;
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;

            //The initial draw
            if(initialDraw) {
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
            //draw a ball after the score changes
            moveBall();
            moveNegativeBalls(XY);
            if(!drawGoldBall)
            canvas.draw(ball,negativeBall,x,y, XY);
            if(drawGoldBall){
                canvas.drawGold(ball,negativeBall,goldBall,x,y, XY, goldX, goldY);
                if(!drewGoldBall)
                drawGoldBall = true;
                else {
                    drawGoldBall = false;
                    goldX = goldY = 0- ballWidth;
                }
            }
            //after the timer runs out finish the game
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
    private void moveNegativeBalls(int[] XY){
        int i=0;
        for(i=0; i<BALL_NEGATIVE_NUMBER; i++) {
            int negX = XY[i];
            int negY = XY[i + BALL_NEGATIVE_NUMBER];
            negX += negMoveX[i] * redBallSpeed * Math.sin(angles[i]);
            negY += negMoveY[i] * redBallSpeed * Math.cos(angles[i]);

            //if the ball is off screen change its direction
            if (negX > width - ballWidth) {
                negX = width - ballWidth;
                negMoveX[i] = -negMoveX[i];
                // too far right
            }
            if (negY > height - ballHeight) {
                negY = height - ballHeight;
                negMoveY[i] = -negMoveY[i];
                // too far bottom
            }
            if (negX < 0) {
                negX = 0;
                negMoveX[i] = -negMoveX[i];
                // too far left
            }
            if (negY < 0) {
                negY = 0;
                negMoveY[i] = -negMoveY[i];
                // too far top
            }
            XY[i]=negX;
            XY[i+BALL_NEGATIVE_NUMBER]=negY;
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

