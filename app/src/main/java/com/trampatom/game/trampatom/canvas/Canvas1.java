package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.activity.Game1;

public class Canvas1 {

    private static final int BALL_PURPLE_NO_CLICK = 1;
    private static final int WAVE_BALL_NUMBER = 7;

    int i;
    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Bitmap background;
    int backgroundHeight;
    public Canvas1(SurfaceHolder holder, Canvas canvas, Bitmap background){
        this.ourCanvas=canvas;
        this.ourHolder=holder;
        this.background = background;
        backgroundHeight = background.getHeight();
    }

    /**
     * Method for drawing a new ball at new coordinates
     * @param ball the ball to draw
     * @param x the new x coordinate
     * @param y the new y coordinate
     * @return always returns false
     */
    public boolean draw(Bitmap ball,int x, int y, int backgroundHalf1, int backgroundHalf2){
        ourCanvas=ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        ourCanvas.drawBitmap(background, 0,backgroundHalf1, null);
        ourCanvas.drawBitmap(background, 0,backgroundHalf2, null);
        ourCanvas.drawBitmap(ball, x, y, null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }

    public void drawPurple(Bitmap ball, int [] purpleXY, int timesClicked){
        //int memoriseFirstBall= 0;

        if(timesClicked == BALL_PURPLE_NO_CLICK) {
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
    public void drawWave(Bitmap[] waveBall, int[] waveXY){
        ourCanvas = ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        ourCanvas.drawBitmap(background, 0, 0, null);
        for(i=0; i<WAVE_BALL_NUMBER; i++) {
            ourCanvas.drawBitmap(waveBall[i], waveXY[i], waveXY[i+WAVE_BALL_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
    }
}
