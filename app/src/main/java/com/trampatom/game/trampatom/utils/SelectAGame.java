package com.trampatom.game.trampatom.utils;

import android.widget.TextView;

/**
 * Class used for changing selected games. Contains methods for setting a selected game and changing
 * a text view to show what game we have selected.
 */
public class SelectAGame {
    TextView tvSelectedGame;

    /**
     * Constructor that should be used to insert a tet view so we don't pass it in a method every time.
     * @param tvSelectedGame the text view that we will be changing to say what game we currently selected
     */
    public SelectAGame(TextView tvSelectedGame){
        this.tvSelectedGame = tvSelectedGame;
    }


    /**
     * Method that should be called every time we want to change the selected game
     * @param selectedGame currently selected game, parameter used to determine what is the game we selected,
     * is required to know to what game do we change to.
     * @return the new selected game
     */
    public int setSelectedGame(int selectedGame){

        //select the next game
        if(selectedGame == 1) {
            selectedGame++;
            //sets the selected game and shows the high score for THAT game
            tvSelectedGame.setText("SURVIVAL");
        }
        else{
            selectedGame--;
            //sets the selected game and shows the high score for THAT game
            tvSelectedGame.setText("CLASSIC");
        }

        return selectedGame;
    }
}
