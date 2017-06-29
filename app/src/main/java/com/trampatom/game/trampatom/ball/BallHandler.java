package com.trampatom.game.trampatom.ball;


import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that should be used to handle ball types for game 1.
 * Contains methods for getting a new ball, and should contain ball action methods
 */
public class BallHandler {

    int x,y,ballType;
    double angle;

    public static final int GOLD_BALL_DRAW = 1;
    public static final int GOLD_BALL_DONT_DRAW = 0;
    private RandomBallVariables randomBallVariables;

    /**
     * Constructor for BallHandler. We should pass in an instance of RandomBallVariables.
     * This is used to get random coordinates, angles and new ball types.
     */
    public BallHandler(RandomBallVariables randomBallVariables){
        this.randomBallVariables = randomBallVariables;
    }

    /**
     * Method for "getting a new ball" by changing some parameters of the previous one
     * @return a ball object to be used.
     */
    public Ball getNewBall(Ball ball){
        //define a list with a set of information about the specific ball
        //Ball ball= new Ball();
        //the variables to fill the new ball with

        x= randomBallVariables.randomX();
        y = randomBallVariables.randomY();
        ballType = randomBallVariables.getRandomBallType();
        angle = randomBallVariables.randomAngle();


        ball.setX(x);
        ball.setY(y);
        ball.setBallType(ballType);
        ball.setAngle(angle);
        ball.setMoveX(1);  ball.setMoveY(1);
        return ball;
    }

    /**
     * Method for getting a set of x and y coordinates in an array
     * @return
     */
    public int[] getXY(){
        //get random x and y coordinate and return it as an array
        int[] XY = {randomBallVariables.randomX(),randomBallVariables.randomY()};
        return XY;
    }
}
