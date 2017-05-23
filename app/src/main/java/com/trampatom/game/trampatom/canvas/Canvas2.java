package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Canvas2 {

    //used for determining which balls are gray
    public static final int CLICKED_NONE = 0;
    public static final int CLICKED_ONE = 1;
    public static final int CLICKED_TWO = 2;
    public static final int CLICKED_THREE = 3;

    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    public Canvas2(SurfaceHolder holder, Canvas canvas){
        this.ourCanvas=canvas;
        this.ourHolder=holder;
    }


    /**
     * Method for drawing balls at new coordinates
     * ,some gray depending on how many we clicked
     * @param ball the balls to draw
     * @param XY the new xy coordinates
     * @param clicked how many balls will be drawn gray
     * @return always returns false
     */
    public boolean draw(Bitmap[] ball, int[] XY, int clicked){
        ourCanvas = ourHolder.lockCanvas();
        //must draw the background
        ourCanvas.drawRGB(0, 0, 200);

        //depending on how many balls we clicked draws that many gray balls
        //balls are always at the same coordinates, color just changes
        switch(clicked)
        {
            //ball[3] = gray ball
            case CLICKED_NONE:
                ourCanvas.drawBitmap(ball[0], XY[0], XY[3], null);
                ourCanvas.drawBitmap(ball[1], XY[1], XY[4], null);
                ourCanvas.drawBitmap(ball[2], XY[2], XY[5], null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
                return false;
            case CLICKED_ONE:
                ourCanvas.drawBitmap(ball[3], XY[0], XY[3], null);
                ourCanvas.drawBitmap(ball[1], XY[1], XY[4], null);
                ourCanvas.drawBitmap(ball[2], XY[2], XY[5], null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
                return false;
            case CLICKED_TWO:
                ourCanvas.drawBitmap(ball[3], XY[0], XY[3], null);
                ourCanvas.drawBitmap(ball[3], XY[1], XY[4], null);
                ourCanvas.drawBitmap(ball[2], XY[2], XY[5], null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
                return false;
            case CLICKED_THREE:
                ourCanvas.drawBitmap(ball[3], XY[0], XY[3], null);
                ourCanvas.drawBitmap(ball[3], XY[1], XY[4], null);
                ourCanvas.drawBitmap(ball[3], XY[2], XY[5], null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
                return false;

        }
        return false;
    }

}
