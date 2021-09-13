package com.trampatom.game.trampatom.score;

public class CurrentGameScore {

    private int score;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int toAdd){
        this.score += toAdd;
    }
    public void reduceScore(int toReduce){
        this.score -= toReduce;
    }
}
