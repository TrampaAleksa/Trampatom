package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

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
}
