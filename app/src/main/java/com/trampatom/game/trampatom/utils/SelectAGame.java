package com.trampatom.game.trampatom.utils;

import android.widget.TextView;

public class SelectAGame {
    TextView tvSelectedGame;

    public SelectAGame(TextView tvSelectedGame){
        this.tvSelectedGame = tvSelectedGame;
    }

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
