package com.trampatom.game.trampatom.ball;

import java.util.Random;

import static com.trampatom.game.trampatom.ball.AtomId.BALL_BLUE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_GREEN;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_PURPLE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_RED;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_WAVE;
import static com.trampatom.game.trampatom.ball.AtomId.BALL_YELLOW;

public class BallTypeHandler {

    public int TYPE_BALL_RED_CHANCE = 2;
    public int TYPE_BALL_BLUE_CHANCE = 10;
    public int TYPE_BALL_YELLOW_CHANCE = 13;
    public int TYPE_BALL_GREEN_CHANCE = 15;
    public int TYPE_BALL_PURPLE_CHANCE = 18;
    public int TYPE_BALL_WAVE_CHANCE = 21;

    private int currentType;

    public BallTypeHandler(){
        //the first ball is always blue;
        currentType = BALL_BLUE;
    }

    public void setCurrentBallTypeBySeed(int ballType){
        //if we click on the ball do something depending on the ball type
        if(ballType<=TYPE_BALL_RED_CHANCE){
            currentType=BALL_RED;
        }
        if(ballType>TYPE_BALL_RED_CHANCE && ballType<=TYPE_BALL_BLUE_CHANCE){
            currentType=BALL_BLUE;
        }
        if(ballType>TYPE_BALL_BLUE_CHANCE && ballType<=TYPE_BALL_YELLOW_CHANCE){
            currentType=BALL_YELLOW;
        }
        if(ballType>TYPE_BALL_YELLOW_CHANCE && ballType<=TYPE_BALL_GREEN_CHANCE){
            currentType=BALL_GREEN;
        }
        if(ballType>TYPE_BALL_GREEN_CHANCE && ballType<=TYPE_BALL_PURPLE_CHANCE){
            currentType=BALL_PURPLE;
        }
        if(ballType>TYPE_BALL_PURPLE_CHANCE && ballType<=TYPE_BALL_WAVE_CHANCE){
            currentType=BALL_WAVE;
        }
    }
    public int getRandomBallTypeSeed(){
        int seed;
        seed= new Random().nextInt(21);
        return seed;
    }

    public int getCurrentType() {
        return currentType;
    }
    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }
}
