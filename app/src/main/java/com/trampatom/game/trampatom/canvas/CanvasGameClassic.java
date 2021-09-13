package com.trampatom.game.trampatom.canvas;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.Model.Star;
import com.trampatom.game.trampatom.score.CurrentGameScore;

/**
 * Class containing methods for working with Game 1 canvas,
 * mostly used for drawing every element and displaying score.
 */
public class CanvasGameClassic {

    private static final int BALL_PURPLE_NO_CLICK = 1;
    private static final int WAVE_BALL_NUMBER = 7;

    int i;
    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Background background;
    //paint used for score
    private Paint paint;
    private CurrentGameScore currentGameScore;

    /**
     * Constructor that should be used to get instance of the canvas and a background
     * so that we don't pass them as parameters every time
     * @param holder we need the holder for our canvas
     * @param canvas the canvas that we will draw on
     * @param background the background that will be drawn and should be moving
     */
    public CanvasGameClassic(SurfaceHolder holder, Canvas canvas, Background background){
        this.ourCanvas=canvas;
        this.ourHolder=holder;
        this.background = background;
        //default paint for drawing score
        paint = new Paint();

        paint.setColor(Color.CYAN);
        paint.setTextSize(38);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * method used for drawing balls depending on the type of the ball.
     * @param ball ball object containing coordinates and other values used to draw it correctly
     * @return
     */
    public boolean draw(Ball ball){

        ourCanvas=ourHolder.lockCanvas();
        drawBackground();
        drawScore();

        //draw the ball that we passed
        if(ball.getBallColor() != null)
            ourCanvas.drawBitmap(ball.getBallColor(), ball.getX(), ball.getY(), null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;


    }

    /**
     * Method used for drawing purple balls based on an ball object array containing every balls info
     * @param purpleBalls the ball objects that we are drawing
     * @param timesClicked used to determine how many purple balls to draw
     * @return always false
     */
    public boolean drawPurple(Ball[] purpleBalls, int timesClicked, boolean[] ballClicked){
        if(timesClicked == BALL_PURPLE_NO_CLICK) {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            drawScore();

            ourCanvas.drawBitmap(purpleBalls[0].getBallColor(), purpleBalls[0].getX(), purpleBalls[0].getY(), null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        else {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            drawScore();
            if (!ballClicked[0])
                ourCanvas.drawBitmap(purpleBalls[0].getBallColor(), purpleBalls[0].getX(), purpleBalls[0].getY(), null);
            if (!ballClicked[1])
                ourCanvas.drawBitmap(purpleBalls[1].getBallColor(), purpleBalls[1].getX(), purpleBalls[1].getY(), null);
            if (!ballClicked[2])
                ourCanvas.drawBitmap(purpleBalls[2].getBallColor(), purpleBalls[2].getX(), purpleBalls[2].getY(), null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        return false;
    }


    /**
     * Method for drawing the wave ball type
     * @param multipleBalls an array of ball objects to be drawn
     * @param drawnBalls number indicating how many balls we have clicked, so that we don't draw those balls
     */
    public boolean drawWave(Ball[] multipleBalls, int drawnBalls){
        ourCanvas = ourHolder.lockCanvas();
        drawBackground();
        drawScore();

        for(i=drawnBalls; i<WAVE_BALL_NUMBER; i++) {
            //ourCanvas.drawBitmap(waveBall[i], waveXY[i], waveXY[i+WAVE_BALL_NUMBER], null);
            ourCanvas.drawBitmap(multipleBalls[i].getBallColor(), multipleBalls[i].getX(), multipleBalls[i].getY(), null );
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }



    /**
     * Method used to draw a black canvas and an array of stars on random locations, and used for displaying
     * the current score in top center
     */
    private void drawBackground(){
        ourCanvas.drawRGB(0, 0, 0);
        for(i=0; i<background.NUMBER_OF_STARS;i++) {

                Star currentStar = background.stars.get(i);
                int starY = currentStar.getY();
                //draw every star object contained within an array of stars that we passed
                ourCanvas.drawCircle(currentStar.getX(), starY, currentStar.getRadius(), background.starPaint);
                currentStar.setY(starY - 1);
                if ((starY - 1) < -currentStar.getRadius())
                    currentStar.setY(ourCanvas.getHeight());
        }

    }

    private void drawScore(){
        //draw the current score
        ourCanvas.drawText(Integer.toString(getScore()), ourCanvas.getWidth()/2, 50, paint);
    }
    public void setCurrentGameScore(CurrentGameScore gameScore){
        this.currentGameScore = gameScore;
    }
    private int getScore(){
        return currentGameScore.getScore();
    }
}
