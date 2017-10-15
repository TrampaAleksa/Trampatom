package com.trampatom.game.trampatom.canvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.Model.Star;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

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
    private Paint paint, limitingSquarePaint;
    private Path path;

    /**
     * Constructor that should be used to get instance of the canvas and a background
     * so that we don't pass them as parameters every time
     * @param holder we need the holder for our canvas
     * @param canvas the canvas that we will draw on
     * @param background the background that will be drawn and should be moving
     */
    public CanvasGameClassic(SurfaceHolder holder, Canvas canvas, Background background, int width, int height){
        this.ourCanvas=canvas;
        this.ourHolder=holder;
        this.background = background;
        //default paint for drawing score
        paint = new Paint();
        limitingSquarePaint = new Paint();

        paint.setColor(Color.CYAN);
        paint.setTextSize(38);
        paint.setTextAlign(Paint.Align.CENTER);

        int aX, aY,bX,bY, cX,cY, dX,dY;
        aX=width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH;
        aY= height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT;
        bX= width - width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH;
        bY=  height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT;
        cX=  width - width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH;
        cY= height - height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT;
        dX= width/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH;
        dY = height - height/Keys.POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT;

        /*aX= RandomBallVariables.getLeftSide();
        aY= RandomBallVariables.getTopSide();
        bX= width - RandomBallVariables.getLeftSide();
        bY=  RandomBallVariables.getTopSide();
        cX=  width - RandomBallVariables.getLeftSide();
        cY= height - RandomBallVariables.getTopSide();
        dX= RandomBallVariables.getLeftSide();
        dY = height - RandomBallVariables.getTopSide();*/
        limitingSquarePaint.setColor(Color.YELLOW);
        limitingSquarePaint.setStrokeWidth(3);
        limitingSquarePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        path.moveTo(aX, aY);
        path.lineTo(bX,bY);
        path.lineTo(cX,cY);
        path.lineTo(dX,dY);
        path.lineTo(aX,aY);
    }

    /**
     * Called after we got a limiting square from a power up. Sets the path for the square to be drawn.
     * The right and bottom sade are the same as the top and left.
     * @param leftSide needed to get the left side
     * @param rightSide needed to get the top side
     */
    public void setPath(int leftSide, int rightSide, int width, int height){

        int aX, aY,bX,bY, cX,cY, dX,dY;

        aX= RandomBallVariables.getLeftSide();
        aY= RandomBallVariables.getTopSide();
        bX= width - RandomBallVariables.getLeftSide();
        bY=  RandomBallVariables.getTopSide();
        cX=  width - RandomBallVariables.getLeftSide();
        cY= height - RandomBallVariables.getTopSide();
        dX= RandomBallVariables.getLeftSide();
        dY = height - RandomBallVariables.getTopSide();
        limitingSquarePaint.setColor(Color.YELLOW);
        limitingSquarePaint.setStrokeWidth(3);
        limitingSquarePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        path.moveTo(aX, aY);
        path.lineTo(bX,bY);
        path.lineTo(cX,cY);
        path.lineTo(dX,dY);
        path.lineTo(aX,aY);

    }

    /**
     * method used for drawing balls depending on the type of the ball.
     * @param ball ball object containing coordinates and other values used to draw it correctly
     * @param score
     * @return
     */
    public boolean draw(Ball ball, boolean limitingSquareActive, int score){

        this.score = score;
        ourCanvas=ourHolder.lockCanvas();
        drawBackground();
        drawScore();
        /*if(limitingSquareActive){
            drawLimitingSquare();
        }*/

        //draw the ball that we passed
        if(ball.getBallColor() != null)
            ourCanvas.drawBitmap(ball.getBallColor(), ball.getX(), ball.getY(), null);
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;


    }

    /**
     * Method used for drawing purple balls based on an ball object array containing every balls info
     * @param purpleBalls the ball objects that we are drawing
     * @param score the score to display
     * @param timesClicked used to determine how many purple balls to draw
     * @return always false
     */
    public boolean drawPurple(Ball[] purpleBalls, boolean limitingSquareActive, int score, int timesClicked, boolean[] ballClicked){
        this.score = score;
        if(timesClicked == BALL_PURPLE_NO_CLICK) {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            drawScore();
           /* if(limitingSquareActive){
                drawLimitingSquare();
            }*/

            ourCanvas.drawBitmap(purpleBalls[0].getBallColor(), purpleBalls[0].getX(), purpleBalls[0].getY(), null);
            ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        else {
            ourCanvas = ourHolder.lockCanvas();
            drawBackground();
            drawScore();
            if (!ballClicked[0])
                ourCanvas.drawBitmap(purpleBalls[0].getBallColor(), purpleBalls[0].getX(), purpleBalls[0].getY(), null);
            if (!ballClicked[1])
                ourCanvas.drawBitmap(purpleBalls[1].getBallColor(), purpleBalls[1].getX(), purpleBalls[1].getY(), null);
            if (!ballClicked[2])
                ourCanvas.drawBitmap(purpleBalls[2].getBallColor(), purpleBalls[2].getX(), purpleBalls[2].getY(), null);
                ourHolder.unlockCanvasAndPost(ourCanvas);
        }
        return false;
    }


    /**
     * Method for drawing the wave ball type
     * @param multipleBalls an array of ball objects to be drawn
     * @param score the current score to be shown
     * @param drawnBalls number indicating how many balls we have clicked, so that we don't draw those balls
     */
    public boolean drawWave(Ball[] multipleBalls,boolean limitingSquareActive, int score,int drawnBalls){
        this.score = score;
        ourCanvas = ourHolder.lockCanvas();
        drawBackground();
        drawScore();
        /*if(limitingSquareActive){
            drawLimitingSquare();
        }*/
        for(i=drawnBalls; i<WAVE_BALL_NUMBER; i++) {
            //ourCanvas.drawBitmap(waveBall[i], waveXY[i], waveXY[i+WAVE_BALL_NUMBER], null);
            ourCanvas.drawBitmap(multipleBalls[i].getBallColor(), multipleBalls[i].getX(), multipleBalls[i].getY(), null );
        }
        ourHolder.unlockCanvasAndPost(ourCanvas);
        return false;
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

    }

    private void drawScore(){

        //draw the current score
        ourCanvas.drawText(Integer.toString(score), ourCanvas.getWidth()/2, 50, paint);

    }

    private void drawLimitingSquare() {
        ourCanvas.drawPath(path, limitingSquarePaint);
    }

}
