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
    public boolean setTimeRemaining(long millisUntilFinished){
        time=Long.toString(millisUntilFinished/1000);
        tvTime.setText(time);
        return millisUntilFinished == 1;

    }

    public void setScore(int score){
        scoreS = "SCORE: "+ Integer.toString(score);
        tvScore.setText(scoreS);
    }

}
