package com.trampatom.game.trampatom.ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.Keys;
import com.trampatom.game.trampatom.utils.PassivesManager;

public class BallBitmaps {

    public static final int HEIGHT_SCALE = 11;

    Bitmap blueBall, redBall, greenBall, yellowBall, purpleBall;
    Bitmap[] waveBall;

    //every ball should have the same width and height, but we can change this if needed
    static int ballWidth, ballHeight;
    //width and height of the canvas
    int width, height;

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
    private void initiateBitmaps(){
        //initiate bitmaps
        waveBall = new Bitmap[7];
        BitmapFactory.Options options = new BitmapFactory.Options();
        //used to rescale bitmaps without creating a new bitmap
        //options.inSampleSize = 4;
        //configure the color patter so that the balls take less space
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //loading the bitmaps
        //get the size of the device that is running the game and scale teh balls according to this

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

    public static int getBallWidth() {
        return ballWidth;
    }

    public static void setBallWidth(int ballWidth) {
        BallBitmaps.ballWidth = ballWidth;
    }

    public static int getBallHeight() {
        return ballHeight;
    }

    public static void setBallHeight(int ballHeight) {
        BallBitmaps.ballHeight = ballHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
}
