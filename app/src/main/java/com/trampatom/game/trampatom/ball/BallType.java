package com.trampatom.game.trampatom.ball;


import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.utils.RandomBallVariables;

import java.util.ArrayList;
import java.util.List;

public class BallType {
    private RandomBallVariables randomBallVariables;

    public BallType(RandomBallVariables randomBallVariables){
        this.randomBallVariables = randomBallVariables;
    }

    /**
     * Method for getting a new ball
     * @return a ball object
     */

    public Ball getNewBall(){
        //define a list with a set of information about the specific ball
        Ball ball= new Ball();
        //the variables to fill the new ball with
        int x,y,ballType;
        double angle;
        x= randomBallVariables.randomX();
        y = randomBallVariables.randomY();
        ballType = randomBallVariables.getRandomBallType();
        angle = randomBallVariables.randomAngle();

        ball.setX(x);
        ball.setY(y);
        ball.setBallType(ballType);
        ball.setAngle(angle);

        return ball;
    }
}
