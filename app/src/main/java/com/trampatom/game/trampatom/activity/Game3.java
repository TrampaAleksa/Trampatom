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
import com.trampatom.game.trampatom.canvas.Canvas3;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomCoordinate;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Game3 extends AppCompatActivity implements Runnable, View.OnTouchListener{

    //Duration of the game
    private static final long GAME_TIME = 10000;
    //speed of the balls
    private static final int BALL_SPEED= 15;
    //Classes
    GameTimeAndScore gameTimeAndScore;
    RandomCoordinate randomCoordinate;
    Canvas3 canvas;
    HighScore highScore;
    //For the SurfaceView to work
    SurfaceHolder ourHolder;
    SurfaceView mSurfaceView;
    Canvas mCanvas;
    Thread ourThread = null;
    boolean isRunning=true;
    //Balls and Background Bitmaps
    Bitmap ball;
    //Other variables
    TextView tvScore, tvTime;
    int width, height;
    static int ballWidth, ballHeight;
    //Used for scoring
    int score, previousHighScore;
    //coordinates of the currently drawn ball
    int x, clickedX;
    int y, clickedY;
    //used for moving balls, angle of the balls movement
    int moveX, moveY;
    double angle;
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
        new CountDownTimer(GAME_TIME, 250) {
            public void onTick(long millisUntilFinished) {
                gameTimeAndScore.setTimeAndScore(millisUntilFinished, score);
            }

            public void onFinish() {
                //finish the game when the timer ends
                gameover = true;
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
        //movement of the ball
            moveX=1;
            moveY=1;
        //Bitmaps
            ball= BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
            //background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            //background = Bitmap.createScaledBitmap(background, width, deviceHeight, true);
        //ball Height and Width
            ballHeight=ball.getHeight();
            ballWidth=ball.getWidth();
        //Initial coordinates for the ball
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
            if( clickedX>x && clickedX<(x+ballWidth) && clickedY>y && clickedY<(y+ballHeight)) {
                score+=100;
                angle =  randomCoordinate.randomAngle();
                x = randomCoordinate.randomX();
                y = randomCoordinate.randomY();
                scored=true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        canvas = new Canvas3(ourHolder,mCanvas);
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
                    randomCoordinate = new RandomCoordinate(width, height, ballWidth, ballHeight);
                    angle = randomCoordinate.randomAngle();
                    x=randomCoordinate.randomX();
                    y=randomCoordinate.randomY();
                //first draw
                    initialDraw= canvas.draw(ball,x,y);
            }
            //draw a ball after the score changes


            moveBall();
            canvas.draw(ball, x, y);

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

