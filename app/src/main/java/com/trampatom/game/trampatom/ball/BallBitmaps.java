package com.trampatom.game.trampatom.ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.Keys;

public class BallBitmaps {

    Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall;
    Bitmap[] waveBall;

    //every ball should have the same width and height, but we can change this if needed
    int ballWidth, ballHeight;

    private Context context;

    public BallBitmaps(Context context) {
        this.context = context;
    }

    /**
     * Since we will manipulate with ball types during the game, we have to pass every bitmap so that we can set it inside the handler
     * Theese bitmaps will later be used to change the balls sizes and colors depending on a power up or type of the ball
     */
    public void parseBallBitmaps(Bitmap redBall, Bitmap blueBall, Bitmap greenBall, Bitmap yellowBall, Bitmap purpleBall, Bitmap[] waveBall){
        this.redBall = redBall;
        this.blueBall = blueBall;
        this.greenBall = greenBall;
        this.yellowBall = yellowBall;
        this.purpleBall = purpleBall;
        this.waveBall = waveBall;
    }

    /**
     * Method for decoding every Bitmap into memory and
     * rescaling every ball into a certain size.
     * Gets a standard ball height and width variable to be used
     */
    public void initiateBitmaps(){

        waveBall = new Bitmap[7];
        BitmapFactory.Options options = new BitmapFactory.Options();
        //used to rescale bitmaps without creating a new bitmap
        //options.inSampleSize = 4;
        //configure the color patter so that the balls take less space
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        waveBall[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave1, options);
        waveBall[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave2, options);
        waveBall[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave3, options);
        waveBall[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave4, options);
        waveBall[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave5, options);
        waveBall[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave6, options);
        waveBall[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wave7, options);
        blueBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.atomplava, options);
        redBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.atomcrvena, options);
        greenBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.atomzelena, options);
        yellowBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.atomzuta, options);
        purpleBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.atomroze, options);

        //resize all balls to the new ball width and height
        blueBall = Bitmap.createScaledBitmap(blueBall, ballWidth, ballHeight, true);
        redBall = Bitmap.createScaledBitmap(redBall, ballWidth, ballHeight, true);
        greenBall = Bitmap.createScaledBitmap(greenBall, ballWidth, ballHeight, true);
        yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
        purpleBall = Bitmap.createScaledBitmap(purpleBall, ballWidth, ballHeight, true);
        for (int i = 0; i < Keys.WAVE_BALL_NUMBER; i++) {
            waveBall[i] = Bitmap.createScaledBitmap(waveBall[i], ballWidth, ballHeight, true);
        }
    }

    /**
     * After leaving this game we need to clear the memory from the stored bitmaps to avoid memory leaks
     */
    public void clearBitmapMemory(){
        if(blueBall!=null)
        {
            blueBall.recycle();
            blueBall=null;
            redBall.recycle();
            redBall=null;
            greenBall.recycle();
            greenBall=null;
            yellowBall.recycle();
            yellowBall=null;
            for(int i=0; i<Keys.WAVE_BALL_NUMBER; i++){
                waveBall[i].recycle();
                waveBall[i] = null;
            }
        }
    }

    public int getBallWidth() {
        return ballWidth;
    }

    public void setBallWidth(int ballWidth) {
        this.ballWidth = ballWidth;
    }

    public int getBallHeight() {
        return ballHeight;
    }

    public void setBallHeight(int ballHeight) {
        this.ballHeight = ballHeight;
    }

    // ---------------- BITMAPS -------------------------- \\

    public Bitmap getBlueBall() {
        return blueBall;
    }

    public void setBlueBall(Bitmap blueBall) {
        this.blueBall = blueBall;
    }

    public Bitmap getRedBall() {
        return redBall;
    }

    public void setRedBall(Bitmap redBall) {
        this.redBall = redBall;
    }

    public Bitmap getGreenBall() {
        return greenBall;
    }

    public void setGreenBall(Bitmap greenBall) {
        this.greenBall = greenBall;
    }

    public Bitmap getYellowBall() {
        return yellowBall;
    }

    public void setYellowBall(Bitmap yellowBall) {
        this.yellowBall = yellowBall;
    }

    public Bitmap getPurpleBall() {
        return purpleBall;
    }

    public void setPurpleBall(Bitmap purpleBall) {
        this.purpleBall = purpleBall;
    }

    public Bitmap[] getWaveBall() {
        return waveBall;
    }

    public void setWaveBall(Bitmap[] waveBall) {
        this.waveBall = waveBall;
    }
}
