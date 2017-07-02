package com.trampatom.game.trampatom.currency;

import android.widget.ProgressBar;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.utils.Keys;

/**
 * Important class that should contain all the methods for working with passive and active power Ups.
 * <p>
 *     Most power-ups work by getting a ball object/ object array and changing some attribute to the ball.
 *     Either for a limited time as determined inside the game class, or during the whole game as determined by a
 *     passive power up.
 * </p>
 * <p>
 *     Energy manipulation power - ups are a little different, and function by getting the game progress bar
 *     and changing it in a certain way, depending on what power up we used.
 * </p>
 */

/**
 * Since this class manipulates the progress bar in some cases we should pass the progress bar, and keys
 * for accessing flags of every power up.
 */
public class PowerUps {
    Keys keys;
    ProgressBar progressBar;

    public PowerUps(ProgressBar progressBar, Keys keys){

        this.progressBar = progressBar;
        this.keys = keys;
    }



    // --------------------------------------- RED ----------------------------------------- \\




    // --------------------------------------- GREEN -------------------------------------------\\



    // ---------------------------------------- YELLOW ------------------------------------------ \\



    // ---------------------------------------- PURPLE ------------------------------------------- \\



}
