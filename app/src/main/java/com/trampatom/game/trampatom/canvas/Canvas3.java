package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.activity.Game3;

public class Canvas3 {
        SurfaceHolder ourHolder;
        Canvas ourCanvas;
        int i=0;
        public Canvas3(SurfaceHolder holder, Canvas canvas) {
            this.ourCanvas = canvas;
            this.ourHolder = holder;
        }

    public boolean draw(Bitmap ball,Bitmap negativeBall, int x, int y, int[] XY){
        ourCanvas = ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
        ourCanvas.drawBitmap(ball, x, y, null);
        for(i=0; i< Game3.BALL_NEGATIVE_NUMBER; i++) {
            ourCanvas.drawBitmap(negativeBall, XY[i], XY[i + Game3.BALL_NEGATIVE_NUMBER], null);
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }
    }

