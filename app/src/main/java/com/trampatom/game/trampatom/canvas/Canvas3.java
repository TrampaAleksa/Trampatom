package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.Model.Star;
import com.trampatom.game.trampatom.activity.Game3;

/**
 * Class containing methods for working with Survival game canvas,
 * mostly drawing every element.
 */
public class Canvas3 {
        SurfaceHolder ourHolder;
        Canvas ourCanvas;
        int i=0;
        Background background;
        Paint paint;
        int score;


    /**
     * Constructor that should be used to get instance of the canvas and a background
     * so that we don't pass them as parameters every time
     * @param holder we need the holder for our canvas
     * @param canvas the canvas that we will draw on
     */
        public Canvas3(SurfaceHolder holder, Canvas canvas, Background background) {
            this.ourCanvas = canvas;
            this.ourHolder = holder;
            this.background = background;
            //default paint for drawing score
            paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setTextSize(38);
            paint.setTextAlign(Paint.Align.CENTER);
        }

    /**
     * Method for drawing every ball at new coordinates
     * @param ball the ball to draw
     * @param x the new x coordinate
     * @param y the new y coordinate
     * @param negativeBall contains bitmaps of every negative ball. They are different sizes
     *                     so its an array of bitmaps
     * @param XY set of coordinates for the negative balls
     * @return always returns false
     */
    public boolean draw(Bitmap ball,Bitmap[] negativeBall, int x, int y, int[] XY, int score){
        this.score = score;
        ourCanvas = ourHolder.lockCanvas();
        drawBackground();
        ourCanvas.drawBitmap(ball, x, y, null);
        for(i=0; i< Game3.BALL_NEGATIVE_NUMBER; i++) {
            ourCanvas.drawBitmap(negativeBall[i], XY[i], XY[i + Game3.BALL_NEGATIVE_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }

    /**
     * Method for drawing every ball plus a gold ball. The gold ball should be drawn only for a few seconds.
     * @param ball the ball to draw
     * @param x the new x coordinate
     * @param y the new y coordinate
     * @param negativeBall contains bitmaps of every negative ball. They are different sizes
     *                     so its an array of bitmaps
     * @param XY set of coordinates for the negative balls
     * @param goldBall contains the gold ball
     * @param goldX x coordinate of the gold ball
     * @param goldY y coordinate of the gold ball
     * @return always returns false
     */
    public boolean drawGold(Bitmap ball,Bitmap[] negativeBall,Bitmap goldBall, int x, int y, int[] XY, int goldX, int goldY, int score){
        this.score = score;
        ourCanvas = ourHolder.lockCanvas();
        drawBackground();
        ourCanvas.drawBitmap(ball, x, y, null);
        ourCanvas.drawBitmap(goldBall, goldX, goldY, null);
        for(i=0; i< Game3.BALL_NEGATIVE_NUMBER; i++) {
            ourCanvas.drawBitmap(negativeBall[i], XY[i], XY[i + Game3.BALL_NEGATIVE_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }
    /**
     * Method used to draw a black canvas and an array of stars on random locations
     */
    private void drawBackground(){
        ourCanvas.drawRGB(0, 0, 0);
        //for (j = 0; j < 4; j++) {
        for(i=0; i<background.NUMBER_OF_STARS;i++) {

            Star currentStar = background.stars.get(i);
            int starY = currentStar.getY();
            //draw every star object contained within an array of stars that we passed
            ourCanvas.drawCircle(currentStar.getX(), starY, currentStar.getRadius(), background.starPaint);
            currentStar.setY(starY - 1);
            if ((starY - 1) < -currentStar.getRadius())
                currentStar.setY(ourCanvas.getHeight());
            // }
        }
        //draw the current score
        ourCanvas.drawText(Integer.toString(score), ourCanvas.getWidth()/2, 50, paint);
    }
    }

