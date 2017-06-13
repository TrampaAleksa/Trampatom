package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.activity.Game3;

/**
 * Class containing methods for working with Survival game canvas,
 * mostly drawing every element.
 */
public class Canvas3 {
        SurfaceHolder ourHolder;
        Canvas ourCanvas;
        int i=0;


    /**
     * Constructor that should be used to get instance of the canvas and a background
     * so that we don't pass them as parameters every time
     * @param holder we need the holder for our canvas
     * @param canvas the canvas that we will draw on
     */
        public Canvas3(SurfaceHolder holder, Canvas canvas) {
            this.ourCanvas = canvas;
            this.ourHolder = holder;
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
    public boolean draw(Bitmap ball,Bitmap[] negativeBall, int x, int y, int[] XY){
        ourCanvas = ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
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
    public boolean drawGold(Bitmap ball,Bitmap[] negativeBall,Bitmap goldBall, int x, int y, int[] XY, int goldX, int goldY){
        ourCanvas = ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
        ourCanvas.drawBitmap(ball, x, y, null);
        ourCanvas.drawBitmap(goldBall, goldX, goldY, null);
        for(i=0; i< Game3.BALL_NEGATIVE_NUMBER; i++) {
            ourCanvas.drawBitmap(negativeBall[i], XY[i], XY[i + Game3.BALL_NEGATIVE_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }
    }

