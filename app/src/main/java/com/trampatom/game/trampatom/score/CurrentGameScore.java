package com.trampatom.game.trampatom.score;

public class CurrentGameScore {

    private int score;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    private void addScore(int toAdd){
        this.score += toAdd;
    }
    private void reduceScore(int toReduce){
        this.score -= toReduce;
    }
}
