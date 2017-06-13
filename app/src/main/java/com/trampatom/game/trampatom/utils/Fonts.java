package com.trampatom.game.trampatom.utils;


import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class that should contain methods for setting different fonts
 */
public class Fonts {
    Typeface face;

    public Fonts(Typeface face){
        this.face = face;
    }

    /**
     * Method used for setting a font for the main menu
     */
    public void setFonts(TextView tvHighScoreValue, TextView tvSelectedGame, TextView tvHighScore, Button start){

        tvSelectedGame.setTypeface(face);
        tvHighScoreValue.setTypeface(face);
        tvHighScore.setTypeface(face);
        start.setTypeface(face);
    }
}
