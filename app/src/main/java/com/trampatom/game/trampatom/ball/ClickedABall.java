package com.trampatom.game.trampatom.ball;


import com.trampatom.game.trampatom.Model.Ball;

/**
 * Class that should contain methods for handling most {@link Ball} object clicks.
 */
public class ClickedABall {

    int ballWidth, ballHeight;

    /**
     * Constructor that gets the fixed mBall width and height oh te balls,
     * so that they don't have to be passed in every method
     * @param ballWidth width of our balls
     * @param ballHeight height of our balls
     */
    public ClickedABall( int ballWidth,int ballHeight){
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
    }

    /**
     * Method used to determine if a certain mBall has been clicked
     * @param x x coordinate of the mBall
     * @param y y coordinate of the mBall
     * @param clickedX where we clicked x coordinate
     * @param clickedY where we clicked y coordinate
     * @return returns true if we clicked on the mBall we are checking
     */
    public boolean ballClicked(int x, int y , int clickedX, int clickedY){

        return clickedX > x && clickedX < (x + ballWidth) && clickedY > y && clickedY < (y + ballHeight);
    }

    /**
     * Method used to determine if we clicked a ball. We pass in a ball object because every ball could have different width's
     * or height's , or any other attribute and this way we ensure there isn't a hitBox bug
     * @param ball the object we clicked
     * @param clickedX the x coordinate we clicked
     * @param clickedY the y coordinate we clicked
     * @return true if we clicked the ball
     */
    public boolean ballClicked(Ball ball , int clickedX, int clickedY){

        return clickedX > ball.getX() && clickedX < ( ball.getX() + ball.getBallWidth())
                && clickedY >  ball.getY() && clickedY < ( ball.getY() + ball.getBallHeight());
    }

    private int clickedX, clickedY;

    public boolean ballClicked(Ball ball){
        return clickedX > ball.getX() && clickedX < ( ball.getX() + ball.getBallWidth())
                && clickedY >  ball.getY() && clickedY < ( ball.getY() + ball.getBallHeight());
    }
    public void setClickedX(int clickedX) {
        this.clickedX = clickedX;
    }
    public void setClickedY(int clickedY) {
        this.clickedY = clickedY;
    }

    /**
     * Method used to determine if any red mBall has been clicked
     * @param x x coordinate of the mBall
     * @param y y coordinate of the mBall
     * @param clickedX where we clicked x coordinate
     * @param clickedY where we clicked y coordinate
     *            since red balls have random sizes, we have to pass individual heights and widths
     * @param negWidth width of the passed mBall
     * @param negHeight height of the passed mBall
     * @return returns true if we clicked on the mBall we are checking
     */
    public boolean negativeBallClicked(int x, int y, int clickedX, int clickedY,int negWidth,int negHeight){
        return clickedX > x && clickedX < (x + negWidth) && clickedY > y && clickedY < (y + negHeight);
    }

    private void clickedYellowBall(){
       /* ballWidth -= BALL_YELLOW_SIZE_DECREASE;
        ballHeight -= BALL_YELLOW_SIZE_DECREASE;
        yellowBall = Bitmap.createScaledBitmap(yellowBall, ballWidth, ballHeight, true);
        yellowBallSpeed+= BALL_YELLOW_SPEED_INCREASE;*/
    }
}
