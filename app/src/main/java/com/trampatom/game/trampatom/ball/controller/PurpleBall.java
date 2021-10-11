package com.trampatom.game.trampatom.ball.controller;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.ball.ClickedABall;
import com.trampatom.game.trampatom.utils.Angles;

public class PurpleBall {

    public static int PURPLE_BALL_NUMBER = 3;
    //used for knowing how many balls to handle / draw
    public int BALL_PURPLE_NO_CLICK = 1; //todo - extract enum
    public int BALL_PURPLE_ONE_CLICK = 2;

    boolean[] ballClicked = {false,false,false};
    int timesClickedPurple=0;

    ClickedABall clickedABall;

    IBallFinishedEvent purpleFinishedEvent;
    IBallClickedEvent purpleBallClickedEvent;

    public PurpleBall(IBallFinishedEvent purpleFinishedEvent, IBallClickedEvent purpleBallClickedEvent) {
        this.purpleFinishedEvent = purpleFinishedEvent;
        this.purpleBallClickedEvent = purpleBallClickedEvent;
        resetToStartingState();
    }

    public void handlePurpleBall(Ball[] purpleBallObjects){
        if (getTimesClickedPurple() ==BALL_PURPLE_NO_CLICK)
            initialPurpleBall(purpleBallObjects);
        else
            multiplePurpleBalls(purpleBallObjects);
    }

    private void initialPurpleBall(Ball[] purpleBallObjects) {
        //if we clicked on the first/ original ball
        Ball initialPurpleBall = purpleBallObjects[0];
        boolean isInitialPurpleClicked = clickedABall.ballClicked(initialPurpleBall);

        if (!isInitialPurpleClicked)
            return;

        purpleBallInitialClick(initialPurpleBall, purpleBallObjects);
    }

    private void purpleBallInitialClick(Ball initialPurpleBall, Ball[] purpleBallObjects) {
        //used to determine how many balls to draw
        timesClickedPurple = BALL_PURPLE_ONE_CLICK;
        //get new angles and set every ball to start moving from the same spot
        for (Ball purpleBallObject : purpleBallObjects) {
            purpleBallObject.setAngle(Angles.randomAngle());
            purpleBallObject.setX(initialPurpleBall.getX());
            purpleBallObject.setY(initialPurpleBall.getY());
        }
        purpleBallClickedEvent.onBallClicked();
    }

    private void multiplePurpleBalls(Ball[] purpleBallObjects) {
        //if we clicked on one of the split balls remove them from the screen
        for(int i=0; i<purpleBallObjects.length; i++){
            multiplePurpleBallClicked(i, purpleBallObjects);
        }

        //if we clicked all three, score and get a new ball
        boolean allBallsClicked = ballClicked[0] && ballClicked[1] && ballClicked[2];
        if(allBallsClicked){
            purpleFinished();
        }
    }

    private void multiplePurpleBallClicked(int ballNumber, Ball[] purpleBallObjects) {
        if(clickedABall.ballClicked(purpleBallObjects[ballNumber])){
            //don't draw this ball
            ballClicked[ballNumber]=true;
            //add the atom to the atom pool
            purpleBallClickedEvent.onBallClicked();
        }
    }

    private void purpleFinished() {
        resetToStartingState();
        purpleFinishedEvent.onBallFinished();
    }

    private void resetToStartingState() {
        timesClickedPurple = BALL_PURPLE_NO_CLICK;
        ballClicked[0] =ballClicked[1] = ballClicked[2] = false;
    }



    public boolean[] getBallClickedArray() {
        return ballClicked;
    }
    public void setBallClicked(boolean[] ballClicked) {
        this.ballClicked = ballClicked;
    }

    public int getTimesClickedPurple() {
        return timesClickedPurple;
    }
    public void setTimesClickedPurple(int timesClickedPurple) {
        this.timesClickedPurple = timesClickedPurple;
    }

    public void setClickedABall(ClickedABall clickedABall) {
        this.clickedABall = clickedABall;
    }
}
