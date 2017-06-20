package com.trampatom.game.trampatom.utils;


import android.widget.TextView;

/**
 * Class used for setting the time and score
 */
public class GameTimeAndScore {
    private static final int BALL_GOLD_DURATION = 3;
    private String time,scoreS;
    private TextView tvScore, tvTime;
    public GameTimeAndScore(TextView tvScore, TextView tvTime){
        this.tvScore = tvScore;
        this.tvTime= tvTime;
    }

    /**
     *Method used for setting the current time and score
     * @param millisUntilFinished how many seconds are left
     * @param score pass the current score to be displayed
     */
    public void setTimeAndScore(long millisUntilFinished, int score){
        time=Long.toString(millisUntilFinished/1000);
        scoreS=Integer.toString(score);
        tvScore.setText("SCORE: "+scoreS);
         if(millisUntilFinished==1)
            tvTime.setText(time);

    }

    /**
     * Method for setting the current remaining time. Should be used in a timer
     * @param millisUntilFinished current time remaining in milliseconds
     * @return returns true if the tie remaining is equal to 1 millisecond (always false but can be changed if needed)
     */
    public boolean setTimeRemaining(long millisUntilFinished){
        time=Long.toString(millisUntilFinished/1000);
        tvTime.setText(time);
        //always false but if we change the value it could be true
        return millisUntilFinished == 1;

    }

    /**
     * Method that should be called whenever we want to set/update the current score
     * @param score the current score that we want to set
     */
    public void setScore(int score){
        scoreS = "SCORE: "+ Integer.toString(score);
        tvScore.setText(scoreS);
    }

}
