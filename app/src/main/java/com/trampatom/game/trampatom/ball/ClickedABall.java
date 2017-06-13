package com.trampatom.game.trampatom.ball;


/**
 * Class that should contain methods for handling most ball clicks
 */
public class ClickedABall {

    int ballWidth, ballHeight;

    /**
     * Constructor that gets the fixed ball width and height oh te balls,
     * so that they don't have to be passed in every method
     * @param ballWidth width of our balls
     * @param ballHeight height of our balls
     */
    public ClickedABall( int ballWidth,int ballHeight){
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
    }

    /**
     * Method used to determine if a certain ball has been clicked
     * @param x x coordinate of the ball
     * @param y y coordinate of the ball
     * @param clickedX where we clicked x coordinate
     * @param clickedY where we clicked y coordinate
     * @return returns true if we clicked on the ball we are checking
     */
    public boolean ballClicked(int x, int y , int clickedX, int clickedY){

        return clickedX > x && clickedX < (x + ballWidth) && clickedY > y && clickedY < (y + ballHeight);
    }

    /**
     * Method used to determine if any red ball has been clicked
     * @param x x coordinate of the ball
     * @param y y coordinate of the ball
     * @param clickedX where we clicked x coordinate
     * @param clickedY where we clicked y coordinate
     *            since red balls have random sizes, we have to pass individual heights and widths
     * @param negWidth width of the passed ball
     * @param negHeight height of the passed ball
     * @return returns true if we clicked on the ball we are checking
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
