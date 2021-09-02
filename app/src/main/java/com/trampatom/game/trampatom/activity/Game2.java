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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.canvas.Canvas2;
import com.trampatom.game.trampatom.canvas.GameOver;
import com.trampatom.game.trampatom.utils.GameTimeAndScore;
import com.trampatom.game.trampatom.utils.HighScore;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import static java.lang.Thread.sleep;

public class Game2 extends AppCompatActivity implements View.OnTouchListener, Runnable{

    // CURRENTLY UNUSED

    private static final long GAME_TIME = 60000;
    private static final int BALL_NUMBER= 3;
    private static final int BALL_SPEED=8;

    //Classes
        GameTimeAndScore gameTimeAndScore;
        RandomBallVariables randomCoordinate;
        Canvas2 canvas;
        HighScore highScore;
        ClickedABall clickedABall;
        ProgressBar energyProgress;
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
        int width, height, i;
        static int ballWidth, ballHeight;
    //Used for scoring
        int ballType=4;
        int previous=4;
        int score, previousHighScore;
    //coordinates of the currently drawn balls, coordinates where we clicked
    //x1,x2,x3,y1,y2,y3
        int[] XY= {0,0,0,0,0,0};
        int clickedX, clickedY;
    //used for moving the balls
    int[] moveX= {1,1,1};
    int[] moveY= {1,1,1};
    double[] angles={0,0,0};
    //used for drawing the first ball
        boolean initialDraw;
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
        new CountDownTimer(GAME_TIME, 250) {
            public void onTick(long millisUntilFinished) {
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


            energyProgress = (ProgressBar) findViewById(R.id.pbEnergy) ;
            gameTimeAndScore = new GameTimeAndScore(energyProgress);
        //get device's width and height
            width= MainActivity.getWidth();
            height = MainActivity.getHeight();
        //Bitmaps
            ball = new Bitmap[4];
            ball[0] = BitmapFactory.decodeResource(getResources(),R.drawable.atomzuta);
            ball[1] = BitmapFactory.decodeResource(getResources(),R.drawable.atomnarandzasta);
            ball[2] = BitmapFactory.decodeResource(getResources(),R.drawable.atomcrvena);
            ball[3] = BitmapFactory.decodeResource(getResources(),R.drawable.atomsiva);
            //background = BitmapFactory.decodeResource(getResources(),R.drawable.atompozadina);
            //background = Bitmap.createScaledBitmap(background, width, deviceHeight, true);
        //ball Height and Width
            ballHeight=ball[0].getHeight()+15;
            ballWidth=ball[0].getWidth()+15;
            ball[0]=Bitmap.createScaledBitmap(ball[0],ballWidth, ballHeight, true);
            ball[1]=Bitmap.createScaledBitmap(ball[1],ballWidth, ballHeight, true);
            ball[2]=Bitmap.createScaledBitmap(ball[2],ballWidth, ballHeight, true);
            ball[3]=Bitmap.createScaledBitmap(ball[3],ballWidth, ballHeight, true);

        //Initial coordinates for the ball
        //Obtaining the highScore
            highScore = new HighScore(this);
            previousHighScore=highScore.getHighScore(HighScore.GAME_TWO_HIGH_SCORE_KEY);
            newHighScore=false;

        initialDraw=true;
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
        if( clickedABall.ballClicked(XY[0], XY[3], clickedX,clickedY) && !ballclick1){
            ballclick1=true;
        }
        //when we click the second ball, make it gray
        if(clickedABall.ballClicked(XY[1], XY[4], clickedX,clickedY) && ballclick1){
            ballclick2=true;
        }
        //when we click the third ball, make it gray
        if( clickedABall.ballClicked(XY[2], XY[5], clickedX,clickedY) && clickedY<(XY[5]+ballHeight) && ballclick2){
            ballclick3=true;
        }
        //after we have clicked all three balls, score and draw new balls
        if(ballclick1 && ballclick2 && ballclick3){
            ballclick1=ballclick2=ballclick3=false;
            score +=300;
            XY= randomCoordinate.randomNegativeBallsCoordinates();
            for(i=0; i<BALL_NUMBER; i++){
                angles[i]= randomCoordinate.randomAngle();
            }
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
                height = mCanvas.getHeight();
                ourHolder.unlockCanvasAndPost(mCanvas);
                //prevents drawing over screen
                randomCoordinate = new RandomBallVariables(width, height, ballWidth, ballHeight);
                clickedABall = new ClickedABall(ballWidth, ballHeight);
                XY= randomCoordinate.randomNegativeBallsCoordinates();
                for(i=0; i<BALL_NUMBER; i++){
                    angles[i]= randomCoordinate.randomAngle();
                }
                //initial draw
                initialDraw= false;
            }
            if(!ballclick1 && !ballclick2 && !ballclick3) {
                moveBalls(XY);
                canvas.draw(ball, XY, Canvas2.CLICKED_NONE);
            }
            if(ballclick1 && !ballclick2 && !ballclick3){
                moveBalls(XY);
                canvas.draw(ball,XY, Canvas2.CLICKED_ONE);
            }
            if(ballclick2 && !ballclick3){
                moveBalls(XY);
                canvas.draw(ball,XY, Canvas2.CLICKED_TWO);
            }
            if(ballclick3){
                canvas.draw(ball,XY, Canvas2.CLICKED_THREE);
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

    private void moveBalls(int[] XY){
        for(i=0; i<BALL_NUMBER; i++) {

            int x = XY[i];
            int y = XY[i + BALL_NUMBER];
            x += moveX[i] * BALL_SPEED * Math.sin(angles[i]);
            y += moveY[i] * BALL_SPEED * Math.cos(angles[i]);

            //if the ball is off screen change its direction
            if (x > width - ballWidth) {
                x = width - ballWidth;
                moveX[i] = -moveX[i];
                // too far right
            }
            if (y > height - ballHeight) {
                y = height - ballHeight;
                moveY[i] = -moveY[i];
                // too far bottom
            }
            if (x < 0) {
                x = 0;
                moveX[i] = -moveX[i];
                // too far left
            }
            if (y < 0) {
                y = 0;
                moveY[i] = -moveY[i];
                // too far top
            }
            XY[i] = x;
            XY[i + BALL_NUMBER] = y;
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
