package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.activity.Game1;

public class Canvas1 {
    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Bitmap background;
    public Canvas1(SurfaceHolder holder, Canvas canvas, Bitmap background){
        this.ourCanvas=canvas;
        this.ourHolder=holder;
        this.background = background;
    }

    /**
     * Method for drawing a new ball at new coordinates
     * @param ball the ball to draw
     * @param x the new x coordinate
     * @param y the new y coordinate
     * @return always returns false
     */
    public boolean draw(Bitmap ball,int x, int y){
        ourCanvas=ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        ourCanvas.drawBitmap(background, 0,0, null);
        ourCanvas.drawBitmap(ball, x, y, null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }

   /* /**
     * method used when drawing a purple ball
     * @param ball color of the ball
     * @param x x coordinate
     * @param y y coordinate
     * @param XY1 XY coordinates for the other ball. not used until we click
     */
    /*public void drawPurple(Bitmap ball, int x, int y, int[] XY1, int timesClicked){
        //int memoriseFirstBall= 0;

        if(timesClicked == Game1.BALL_PURPLE_NO_CLICK) {
            ourCanvas = ourHolder.lockCanvas();
            ourCanvas.drawRGB(0, 0, 200);
            ourCanvas.drawBitmap(background, 0, 0, null);
            ourCanvas.drawBitmap(ball, x, y, null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        else {
            ourCanvas = ourHolder.lockCanvas();
            ourCanvas.drawRGB(0, 0, 200);
            ourCanvas.drawBitmap(background, 0, 0, null);
            ourCanvas.drawBitmap(ball, x, y, null);
            ourCanvas.drawBitmap(ball, XY1[0], XY1[1], null);
            ourCanvas.drawBitmap(ball, XY1[2], XY1[3], null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
    }*/
    public void drawPurple(Bitmap ball, int [] purpleXY, int timesClicked){
        //int memoriseFirstBall= 0;

        if(timesClicked == Game1.BALL_PURPLE_NO_CLICK) {
            ourCanvas = ourHolder.lockCanvas();
            ourCanvas.drawRGB(0, 0, 200);
            ourCanvas.drawBitmap(background, 0, 0, null);
            ourCanvas.drawBitmap(ball, purpleXY[0], purpleXY[3], null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        else {
            ourCanvas = ourHolder.lockCanvas();
            ourCanvas.drawRGB(0, 0, 200);
            ourCanvas.drawBitmap(background, 0, 0, null);
            ourCanvas.drawBitmap(ball, purpleXY[0], purpleXY[3], null);
            ourCanvas.drawBitmap(ball, purpleXY[1], purpleXY[4], null);
            ourCanvas.drawBitmap(ball, purpleXY[2], purpleXY[5], null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
    }
}
