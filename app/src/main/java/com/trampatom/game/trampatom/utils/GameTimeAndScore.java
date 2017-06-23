package com.trampatom.game.trampatom.utils;


import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Class used for setting the time and score
 */
public class GameTimeAndScore {
    //in survival game this is the interval at witch we have to click the positive ball in milliseconds
    //Must be equal to SURVIVAL CLICK TIMER in Keys class
    private static final int MAX_BALL_CLICK_TIME = 5000;
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
     * Class constructor used to work with games not using a progress bar
     * @param tvScore text view that should be used for displaying score
     * @param tvTime text view that should be used for displaying time
     */
    public GameTimeAndScore(TextView tvScore, TextView tvTime){
        this.tvScore = tvScore;
        this.tvTime= tvTime;
    }

    /**
     * Constructor that should be used for survival game since it uses only a progress bar
     */
    public GameTimeAndScore(ProgressBar energyProgress){
        energyProgress.setMax(MAX_BALL_CLICK_TIME);
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

    /**
     * Method that should be used to update the state of the energy bar to the current level of energy.
     * <p>API levels of 24+ support animation when changing the energy bar</p>
     * @param energy sets the level of energy on the progress bar
     */
    public void updateEnergyBar(int energy){
        if (android.os.Build.VERSION.SDK_INT >= 24){
            //animating a progress is available only in API 24+
            energyProgress.setProgress(energy, true);
        } else{
            energyProgress.setProgress(energy);
        }
    }

    /**
     * Method used for updating the time in survival mode in the progress bar
     * @param time
     */
    public void updateTimeBar(int time){
        if (android.os.Build.VERSION.SDK_INT >= 24){
            //animating a progress is available only in API 24+
            energyProgress.setProgress(time, true);
        } else{
            energyProgress.setProgress(time);
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
