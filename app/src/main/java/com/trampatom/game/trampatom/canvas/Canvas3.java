package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Canvas3 {
        SurfaceHolder ourHolder;
        Canvas ourCanvas;

        public Canvas3(SurfaceHolder holder, Canvas canvas) {
            this.ourCanvas = canvas;
            this.ourHolder = holder;
        }

    public boolean draw(Bitmap ball, int x, int y){
        ourCanvas = ourHolder.lockCanvas();
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
        ourCanvas.drawBitmap(ball, x, y, null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
    }
    }

