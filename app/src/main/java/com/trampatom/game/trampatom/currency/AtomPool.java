package com.trampatom.game.trampatom.currency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.trampatom.game.trampatom.utils.Keys;

/**
 * Important class that should contain all the methods used for handling in-game currency -> atoms
 * (getting and setting currency with shared preferences)
 * <p>Shop currency is based on Atoms and their types.</p>
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class AtomPool {


    Keys keys;
    Context context;
    SharedPreferences preferences;
    /**
     * public constructor that should be used for inserting a context for using Shared Preferences
     * to get the amount of currency we have
     */
    public AtomPool(Context context){
        this.context = context;
        //get keys to be used for getting specific atoms via string keys for preferences
        keys = new Keys();
        //get a preferences manager only once
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * method for getting every atom type to use as currency
     * @return an array of atoms: red, green, yellow, purple
     */
    public int[] getAtoms(){


        //get the number of every atom we had from a shared Preference
        int [] array = {
                preferences.getInt(keys.KEY_BLUE_CURRENCY, 0),
                preferences.getInt(keys.KEY_RED_CURRENCY, 0),
                preferences.getInt(keys.KEY_GREEN_CURRENCY,0),
                preferences.getInt(keys.KEY_YELLOW_CURRENCY, 0),
                preferences.getInt(keys.KEY_PURPLE_CURRENCY, 0)
        };

        return array;
    }

    /**
     * Method used for setting the specified amount of atoms we have for a specified type
     * @param atomKey key value of currency shared preference to determine what currency/atom number to change
     * @param number the new number to set the atom number
     */
    public void setSingleAtomNumber(String atomKey, int number){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(atomKey, number);
        editor.apply();
    }

    /**
     * Important method that should be called at the end of every game to add every collected atom into
     * the currency pool/ total atom count to be used later
     * @param addArray in mid game an array is filled with every atom type we clicked.
     *                 indexes: 0 - blue ; 1 - red ; 2 - green ; 3 - yellow ; 4 - purple
     */
    public void addAtoms( int[] addArray){
        //Current pool that we have so that we add our additional atoms to the existing pool
        int[] atomPool = {0,0,0,0,0};
        atomPool = getAtoms();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(keys.KEY_BLUE_CURRENCY, addArray[0]+atomPool[0]);
        editor.putInt(keys.KEY_RED_CURRENCY, addArray[1]+atomPool[1]);
        editor.putInt(keys.KEY_GREEN_CURRENCY, addArray[2]+atomPool[2]);
        editor.putInt(keys.KEY_YELLOW_CURRENCY, addArray[3]+atomPool[3]);
        editor.putInt(keys.KEY_PURPLE_CURRENCY, addArray[4]+atomPool[4]);
        editor.apply();
    }

    /**
     * Method that should be used to retrieve an array of categories to be used to handle the shop better
     * @return indexes/ category : 0 - RED ; 1 - GREEN ; 2 - YELLOW ; 3 - PURPLE
     */
    public int[] getCategories(){

        int[] categories = {keys.CATEGORY_RED, keys.CATEGORY_GREEN, keys.CATEGORY_YELLOW, keys.CATEGORY_PURPLE};
        return categories;
    }
}
