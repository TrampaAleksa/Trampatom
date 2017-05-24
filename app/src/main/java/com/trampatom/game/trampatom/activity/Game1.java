package com.trampatom.game.trampatom.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
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
    private static final long GAME_TIME = 10000;
    private static final int BALL_SPEED= 8;

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
        Bitmap possitiveBall, negativeBall, background;
    //Other variables
        TextView tvScore, tvTime;
        int width, height;
        static int ballWidth, ballHeight;
    //Used for scoring
        int ballType=4;
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
            possitiveBall= BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
            negativeBall = BitmapFactory.decodeResource(getResources(),R.drawable.atomcrvena);
            background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            background = Bitmap.createScaledBitmap(background, width, height, true);
        //ball Height and Width
            ballHeight=possitiveBall.getHeight();
            ballWidth=possitiveBall.getWidth();
        //Initial coordinates for the ball

        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY);
            newHighScore=false;
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
                initialDraw= canvas.draw(possitiveBall,x,y);
            }
            //draw a ball after the score changes
                moveBall();
                if(ballType >2)
                    scored= canvas.draw(possitiveBall, x, y);
                else {
                    scored = canvas.draw(negativeBall, x, y);
                }
            //after the timer runs out finish the game
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
            clickedX =(int) event.getX();
            clickedY =(int) event.getY();
            //if we click on the ball
            if( clickedX>x && clickedX<(x+ballWidth) && clickedY>y && clickedY<(y+ballHeight)){
                ballclick();
                score();
            }
            //if we click anywhere else
            else{
                outsideClick();
            }
        }
        return false;
    }

    /**
     * Draw a new ball after a click
     */
    private void ballclick() {
        //sets new coordinates
        angle=randomCoordinate.randomAngle();
        x = randomCoordinate.randomX();
        y = randomCoordinate.randomY();

        //determines if the ball will be positive or negative
        Random number3= new Random();
        ballType= number3.nextInt(10);
    }
    /**
     * Add or substract score depending on the ballType
     */
    private void score() {
        //sets previous depending on the new ballType
        //and scores depending on the previous type
        if(ballType>2 && previous>2)
        {
            score +=100;
            previous=3;
        }
        else if(ballType>2 && previous<=2)
        {
            score -=100;
            previous=3;
        }
        else if(ballType<=2 && previous >2 )
        {
            score +=100;
            previous=1;
        }
        else if(ballType<=2 && previous <=2 )
        {
            score -=100;
            previous=1;
        }
        scored = true;
    }
    /**
     * Method called In case we clicked outside the ball
     */
    private void outsideClick() {
        if(previous<=2){
            ballclick();
            if(ballType<=2 )
            {

                score +=100;
                previous=1;

            }
            else if(ballType>2 )
            {

                score +=100;
                previous=5;
            }
            scored=true;
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

