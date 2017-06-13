package com.trampatom.game.trampatom.ball;

/**
 * Class that should contain methods for handling wave balls
 */
public class WaveBall {

    int width, height;
    int ballWidth, ballHeight;

    public WaveBall(int width, int height, int ballWidth, int ballHeight){
        this.width = width;
        this.height = height;
        this.ballHeight = ballHeight;
        this.ballWidth = ballWidth;
    }



    /**
     * Method for doing something when a wave ball is clicked
     * @param score the score worth of that ball + current score
     * @return array with new coordinates and score
     */
    public int[] clickedWaveBall(int score){
        int[] removedWaveBall = {-ballWidth, -ballHeight, score};

        return removedWaveBall;
    }
}
