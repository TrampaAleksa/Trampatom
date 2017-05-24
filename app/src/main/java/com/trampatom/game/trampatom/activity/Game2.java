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
import com.trampatom.game.trampatom.canvas.Canvas2;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomCoordinate;

import static java.lang.Thread.sleep;

public class Game2 extends AppCompatActivity implements View.OnTouchListener, Runnable{

    private static final long GAME_TIME = 30000;

    //Classes
        GameTimeAndScore gameTimeAndScore;
        RandomCoordinate randomCoordinate;
        Canvas2 canvas;
        HighScore highScore;
    //For the SurfaceView to work
        SurfaceHolder ourHolder;
        SurfaceView mSurfaceView;
        Canvas mCanvas;
        Thread ourThread = null;
        boolean isRunning=true;
    //Balls and Background Bitmaps
        Bitmap[] ball;
        Bitmap background;
    //Other variables
        TextView tvScore, tvTime;
        int deviceWidth, surfaceViewHeight;
        static int ballWidth, ballHeight;
    //Used for scoring
        int ballType=4;
        int previous=4;
        int score, previousHighScore;
    //coordinates of the currently drawn balls, coordinates where we clicked
    //x1,x2,x3,y1,y2,y3
        int[] XY= {0,0,0,0,0,0};
        int clickedX, clickedY;
    //used for drawing the first ball
        boolean initialDraw;
    //used for drawing a new ball whenever we scored
        boolean scored;
    //used for ending the game when the time ends
        boolean gameover, newHighScore;
    //used for handling changing balls to gray
        boolean ballclick1,ballclick2, ballclick3, clicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game);
        init();
        //TODO solve timer delay
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
            deviceWidth= MainActivity.getWidth();
            surfaceViewHeight = MainActivity.getHeight();
        //Bitmaps
            ball = new Bitmap[4];
            ball[0] = BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            ball[1] = BitmapFactory.decodeResource(getResources(),R.drawable.atomnarandzasta);
            ball[2] = BitmapFactory.decodeResource(getResources(),R.drawable.atomcrvena);
            ball[3] = BitmapFactory.decodeResource(getResources(),R.drawable.atomsiva);
            //background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            //background = Bitmap.createScaledBitmap(background, deviceWidth, deviceHeight, true);
        //ball Height and Width
            ballHeight=ball[0].getHeight();
            ballWidth=ball[0].getWidth();
        //Initial coordinates for the ball
        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_TWO_HIGH_SCORE_KEY);
            newHighScore=false;

        initialDraw=true;
        scored=false;
        ballclick1=ballclick2=ballclick3=clicked=false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        clickedX= (int) event.getX();
        clickedY= (int) event.getY();

        ballclick();
        return false;
    }


    private void ballclick() {

        //when we click the first ball, make it gray
        if( clickedX>XY[0] && clickedX<(XY[0]+ballWidth) && clickedY>XY[3] && clickedY<(XY[3]+ballHeight) && !ballclick1){
            ballclick1=true;
            clicked=true;
        }
        //when we click the second ball, make it gray
        if( clickedX>XY[1] && clickedX<(XY[1]+ballWidth) && clickedY>XY[4] && clickedY<(XY[4]+ballHeight) && ballclick1){
            ballclick2=true;
            clicked=true;
        }
        //when we click the third ball, make it gray
        if( clickedX>XY[2] && clickedX<(XY[2]+ballWidth) && clickedY>XY[5] && clickedY<(XY[5]+ballHeight) && ballclick2){
            ballclick3=true;
            clicked=true;
          //  ball4= BitmapFactory.decodeResource(getResources(),R.drawable.atomsiva);
        }
        //after we have clicked all three balls, score and draw new balls
        if(ballclick1 && ballclick2 && ballclick3){
            ballclick1=ballclick2=ballclick3=false;
            score +=300;
            XY= randomCoordinate.randomThreeBallCoordinates();
            scored=true;
        }
    }

    @Override
    public void run() {
        canvas = new Canvas2(ourHolder,mCanvas);
        while (isRunning) {
            //Do until you get the surface
            if (!ourHolder.getSurface().isValid())
                continue;


            //The initial draw
            if(initialDraw) {
                //gets the canvas height for drawing balls on screen
                mCanvas = ourHolder.lockCanvas();
                surfaceViewHeight = mCanvas.getHeight();
                ourHolder.unlockCanvasAndPost(mCanvas);
                //prevents drawing over screen
                randomCoordinate = new RandomCoordinate(deviceWidth, surfaceViewHeight, ballWidth, ballHeight);
                XY= randomCoordinate.randomThreeBallCoordinates();
                //initial draw
                initialDraw= canvas.draw(ball,XY, Canvas2.CLICKED_NONE);
            }
            //TODO possible error gray on redraw
            if(ballclick1 && !ballclick2 && !ballclick3 && clicked){
                scored= canvas.draw(ball,XY, Canvas2.CLICKED_ONE);
                clicked=false;
            }
            if(ballclick2 && !ballclick3&& clicked){
                scored= canvas.draw(ball,XY, Canvas2.CLICKED_TWO);
                clicked=false;
            }
            if(ballclick3&& clicked){
                canvas.draw(ball,XY, Canvas2.CLICKED_THREE);
                clicked=false;
            }
            //draw a ball after the score changes
            if(scored && !gameover){
                    scored= canvas.draw(ball, XY, Canvas2.CLICKED_NONE);
            }
            //after the timer runs out finish the game
            if (gameover) {
                GameOver gameover = new GameOver(ourHolder,mCanvas);
                newHighScore=highScore.isHighScore(HighScore.GAME_TWO_HIGH_SCORE_KEY, score);
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
