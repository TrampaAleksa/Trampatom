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

import static java.lang.Thread.sleep;

public class Game3 extends AppCompatActivity implements Runnable, View.OnTouchListener{

    //Duration of the game
    private static final long GAME_TIME = 10000;

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
    int deviceWidth, surfaceViewHeight;
    static int ballWidth, ballHeight;
    //Used for scoring
    int score, previousHighScore;
    //coordinates of the currently drawn ball, coordinates where we clicked
    int x, clickedX;
    int y, clickedY;
    //used for drawing the first ball, start timer only after canvasLoads
    boolean initialDraw, canvasLoaded;
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
        deviceWidth= MainActivity.getWidth();
        surfaceViewHeight = MainActivity.getHeight();
        //Bitmaps
        ball= BitmapFactory.decodeResource(getResources(),R.drawable.atomplava);
        //background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
        //background = Bitmap.createScaledBitmap(background, deviceWidth, deviceHeight, true);
        //ball Height and Width
        ballHeight=ball.getHeight();
        ballWidth=ball.getWidth();
        //Initial coordinates for the ball
        //Obtaining the highScore
        highScore = new HighScore(this);
        previousHighScore=highScore.getHighScore(HighScore.GAME_THREE_HIGH_SCORE_KEY);
        newHighScore=false;

        canvasLoaded=true;
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
                surfaceViewHeight = mCanvas.getHeight();
                ourHolder.unlockCanvasAndPost(mCanvas);
                //prevents drawing over screen
                randomCoordinate = new RandomCoordinate(deviceWidth, surfaceViewHeight, ballWidth, ballHeight);
                x=randomCoordinate.randomX();
                y=randomCoordinate.randomY();
                //first draw
                initialDraw= canvas.draw(ball,x,y);
            }
            //draw a ball after the score changes
            if(scored && !gameover){
                    scored= canvas.draw(ball, x, y);
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

