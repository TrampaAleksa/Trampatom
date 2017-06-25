package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.Model.Star;

/**
 * Class containing methods for working with Game 1 canvas,
 * mostly used for drawing every element and displaying score.
 */
public class CanvasGameClassic {

    private static final int BALL_PURPLE_NO_CLICK = 1;
    private static final int WAVE_BALL_NUMBER = 7;

    int i;
    int score;
    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Background background;
    //paint used for score
    Paint paint;

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
     * Method for drawing a new ball at new coordinates
     * @param ball the ball to draw
     * @param x the new x coordinate
     * @param y the new y coordinate
     * @return always returns false
     */
    public boolean draw(Bitmap ball,int x, int y, int score){
        this.score = score;
        ourCanvas=ourHolder.lockCanvas();
        drawBackground();
        ourCanvas.drawBitmap(ball, x, y, null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }


    /**
     * Method for drawing purple balls. It handles drawing only one ball until we clicked it.
     * Should draw three balls if we clicked the first one.
     * @param ball the bitmap to be drawing, in this case a purple ball
     * @param purpleXY set of coordinates for the balls
     * @param timesClicked parameter used to check if we clicked the first purple ball.
     *                Is int to simplify changing the code later if needed to handle more balls
     */
    public void drawPurple(Bitmap ball, int [] purpleXY, int timesClicked, int score){
        this.score = score;
        if(timesClicked == BALL_PURPLE_NO_CLICK) {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            ourCanvas.drawBitmap(ball, purpleXY[0], purpleXY[3], null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        else {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            ourCanvas.drawBitmap(ball, purpleXY[0], purpleXY[3], null);
            ourCanvas.drawBitmap(ball, purpleXY[1], purpleXY[4], null);
            ourCanvas.drawBitmap(ball, purpleXY[2], purpleXY[5], null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
    }

    /**
     * Method for drawing every wave ball. Even the ones offscreen because of code efficiency.
     * @param waveBall a set of bitmaps each containing a ball bitmap of different color
     * @param waveXY coordinate sets of every ball
     */
    public void drawWave(Bitmap[] waveBall, int[] waveXY, int score){
        this.score = score;
        ourCanvas = ourHolder.lockCanvas();
        drawBackground();
        //TODO refactor to not draw the balls we already clicked by parsing an int instead of an i
        for(i=0; i<WAVE_BALL_NUMBER; i++) {
            ourCanvas.drawBitmap(waveBall[i], waveXY[i], waveXY[i+WAVE_BALL_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
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
        //draw the current score
        ourCanvas.drawText(Integer.toString(score), ourCanvas.getWidth()/2, 50, paint);
    }
}
