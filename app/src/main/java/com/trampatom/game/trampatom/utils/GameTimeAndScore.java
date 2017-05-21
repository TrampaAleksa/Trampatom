package com.trampatom.game.trampatom.utils;


import android.widget.TextView;

/**
 * Class used for setting the time and score
 */
public class GameTimeAndScore {
    private String time,scoreS;
    private int i=0;
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
       // if(millisUntilFinished==1)
            i++;
        if(i%4==0)
            tvTime.setText(time);

    }
}
