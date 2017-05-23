package com.trampatom.game.trampatom.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class HighScore {

    //keys for sharedPreference; contains a high score for the specific game
    public static final String GAME_ONE_HIGH_SCORE_KEY = "HighScore1";
    public static final String GAME_TWO_HIGH_SCORE_KEY = "HighScore2";
    public static final String GAME_THREE_HIGH_SCORE_KEY = "HighScore3";

    private Context context;
    public HighScore(Context context){
        this.context=context;
    }

    /**
     * Gets the highScore
     * @param game the game we want to get the high score from
     * @return the value of the high score
     */
    public int getHighScore(String game) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(game, 0);
    }

    /**
     * Checks if the passed score is bigger than the high score
     * if it is, it sets the new high score
     * @param game the game we are checking the high score for
     * @param score the current score we got from the game
     */
    public boolean isHighScore(String game, int score){
                 int oldHighScore = getHighScore(game);
        //If the new score is bigger than the high score, set a new high score
        if(score>oldHighScore) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(game, score);
            editor.apply();
            return true;
        }
        return false;
    }

    /**
     * Method for setting the high score on a text view
     * @param tvHighScore the text view to display the high score
     * @param game the game we are getting the high score from
     */
    public void textHighScore(TextView tvHighScore,int game){

        switch (game)
        {
            case 1:
                tvHighScore.setText(Integer.toString(getHighScore(GAME_ONE_HIGH_SCORE_KEY)));
                break;
            case 2:
                tvHighScore.setText(Integer.toString(getHighScore(GAME_TWO_HIGH_SCORE_KEY)));
                break;
            case 3:
                tvHighScore.setText(Integer.toString(getHighScore(GAME_THREE_HIGH_SCORE_KEY)));
                break;
        }
       //int highScore = getHighScore(game);

        //tvHighScore.setText(Integer.toString(highScore));
    }
}
