package com.trampatom.game.trampatom.utils;


import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Class used for setting the time and score
 */
public class GameTimeAndScore {
    //TODO remove unneeded methods
    private static final int BALL_GOLD_DURATION = 3;
    private String time,scoreS;
    private TextView tvScore, tvTime;
    private ProgressBar energyProgress;
    public GameTimeAndScore(TextView tvScore, TextView tvTime, ProgressBar energyProgress){
        this.tvScore = tvScore;
        this.tvTime= tvTime;
        energyProgress.setMax(5000);
        this.energyProgress = energyProgress;
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
     * Method for setting the current remaining energy. Should be used in a timer
     * @param energy current energy remaining. if it reaches 0 we lose
     */
    public void setEnergyRemaining(int energy){
        //TODO fix potencial error regaring running on different thread
        time= Integer.toString(energy);
        tvTime.setText(time);

    }

    public void updateEnergyBar(int energy){
        if (android.os.Build.VERSION.SDK_INT >= 24){
            //animating a progress is available only in API 24+
            energyProgress.setProgress(energy, true);
        } else{
            energyProgress.setProgress(energy);
        }
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
