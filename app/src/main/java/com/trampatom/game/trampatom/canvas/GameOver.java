package com.trampatom.game.trampatom.canvas;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.utils.HighScore;


/**
 * Class that should contain methods for handling drawing on canvas at the end of the game.
 */
public class GameOver {
    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Paint paint;
    int xPos, yPos;

    /**
     * Constructor that should be used to get instance of the canvas and a background
     * so that we don't pass them as parameters every time
     * @param holder we need the holder for our canvas
     * @param canvas the canvas that we will draw on
     */
    public GameOver(SurfaceHolder holder, Canvas canvas){
        //set the paint for the GAMEOVER text
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        this.ourCanvas=canvas;
        this.ourHolder=holder;
    }

    /**
     * Method for drawing a screen that says Game Over and displays the final score
     * @param score we have to pass the score for it to be displayed
     * @param newHighScore if true we should also display text indicating that we got a new high score
     */
    public void gameOver(int score, boolean newHighScore){

        ourCanvas = ourHolder.lockCanvas();
        xPos = (ourCanvas.getWidth() / 2);
        yPos = (int) ((ourCanvas.getHeight() / 2) - ((paint.descent() + paint.ascent())));
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
        ourCanvas.drawText("GAME OVER", xPos, yPos, paint);
        ourCanvas.drawText("FINAL SCORE: "+ score,xPos, yPos+50, paint);
        if(newHighScore)
            ourCanvas.drawText("CONGRATULATIONS,NEW HIGH SCORE",xPos, yPos+100, paint);
        ourHolder.unlockCanvasAndPost(ourCanvas);

    }

    /**
     * Method for drawing a screen that says Game Over and displays the final score
     * @param score we have to pass the score for it to be displayed
     * @param newHighScore if true we should also display text indicating that we got a new high score
     */
    public void gameOver(int score, HighScore newHighScore){

        ourCanvas = ourHolder.lockCanvas();
        xPos = (ourCanvas.getWidth() / 2);
        yPos = (int) ((ourCanvas.getHeight() / 2) - ((paint.descent() + paint.ascent())));
        ourCanvas.drawRGB(0, 0, 200);
        // canvas.drawBitmap(background,0,0,null);
        ourCanvas.drawText("GAME OVER", xPos, yPos, paint);
        ourCanvas.drawText("FINAL SCORE: "+ score,xPos, yPos+50, paint);
        if(newHighScore.isHighScore(HighScore.GAME_ONE_HIGH_SCORE_KEY, score))
            ourCanvas.drawText("CONGRATULATIONS,NEW HIGH SCORE",xPos, yPos+100, paint);
        ourHolder.unlockCanvasAndPost(ourCanvas);

    }
}
