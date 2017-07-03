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
    private ProgressBar energyProgress;

    /**
     * Constructor that should be used for survival game since it uses only a progress bar
     */
    public GameTimeAndScore(ProgressBar energyProgress){
        energyProgress.setMax(MAX_BALL_CLICK_TIME);
        this.energyProgress = energyProgress;
    }


    /**
     * Method that should be used to update the state of the energy bar to the current level of energy.
     * <p>
     *     API levels of 24+ support animation when changing the energy bar
     * </p>
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

}
