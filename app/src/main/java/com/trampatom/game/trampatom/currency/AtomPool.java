package com.trampatom.game.trampatom.currency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.ball.AtomId;
import com.trampatom.game.trampatom.utils.Keys;

import java.util.Random;

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

    int[] poolArray = {0,0,0,0,0};
    private final Random random;

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
        random = new Random();
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
     * Method used to get the number of atoms we have of a single type
     * @param category determines what type of atoms are we returning
     *                   : categries contained as constants inside the Keys class
     * @return the number of atoms of a type we have in the atom pool
     */
    public int getSingleAtomValue(int category){
        switch (category){
            case Keys.CATEGORY_RED: return preferences.getInt(keys.KEY_RED_CURRENCY, 0);
            case Keys.CATEGORY_GREEN: return preferences.getInt(keys.KEY_GREEN_CURRENCY, 0);
            case Keys.CATEGORY_YELLOW: return preferences.getInt(keys.KEY_YELLOW_CURRENCY, 0);
            case Keys.CATEGORY_PURPLE: return  preferences.getInt(keys.KEY_PURPLE_CURRENCY, 0);
            default: return  preferences.getInt(keys.KEY_BLUE_CURRENCY, 0);
        }
    }

    /**
     * Adds the specified atom to the pool depending on the type of the Ball passed in.
     * @param ballObject determines how much atoms and what atoms to add
     */
    public void addAtom(Ball ballObject){
        AtomId type = ballObject.getBallType();
        int shopAtomIndex = 0;
        switch(type){
            case BALL_BLUE:
                shopAtomIndex = 0;
                break;
            case BALL_RED:
                shopAtomIndex = 1;
                break;
            case BALL_GREEN:
                shopAtomIndex = 2;
                break;
            case BALL_YELLOW:
                shopAtomIndex = 3;
                break;
            case BALL_PURPLE:
                shopAtomIndex = 4;
                break;
            case BALL_WAVE:
               shopAtomIndex = random.nextInt(4);
               break;
        }
        poolArray[shopAtomIndex] +=  ballObject.getBallAtomValue();
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
     * Method used to save into shared preferences the current atom array, updating the old one. Called after we changed the atm number
     * in some way and need to save it for the next time we open the shop.
     * @param atomArray the current atom array containing the blue,red,green,yellow and purple atoms.
     */
    public void updateAtoms(int[] atomArray){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(keys.KEY_BLUE_CURRENCY, atomArray[Keys.CATEGORY_BLUE]);
        editor.putInt(keys.KEY_RED_CURRENCY, atomArray[Keys.CATEGORY_RED]);
        editor.putInt(keys.KEY_GREEN_CURRENCY, atomArray[Keys.CATEGORY_GREEN]);
        editor.putInt(keys.KEY_YELLOW_CURRENCY, atomArray[Keys.CATEGORY_YELLOW]);
        editor.putInt(keys.KEY_PURPLE_CURRENCY, atomArray[Keys.CATEGORY_PURPLE]);
        editor.apply();
    }




    /**
     * Method used to trade blue atoms for any other atom type depending on what category we selected
     * @param selectedCategory the category we will trade blues into
     */
    public int[] transferBlueAtoms(int selectedCategory, int[] atomPool){

        // if we have blue atoms, trade them
        if(atomPool[0]>20 ) {
            atomPool[0]= atomPool[0]-20;
            // 0 - red ; 1 - green ; 2 - yellow ; 3 - purple
            switch (selectedCategory) {

                case 0:
                    atomPool[1]+=20;
                    break;
                case 1:
                    atomPool[2]+=20;
                    break;
                case 2:
                    atomPool[3]+=20;
                    break;
                case 3:
                    atomPool[4]+=20;
                    break;
            }
        }
        else if(atomPool[0]>0 && atomPool[0]<=20){

            switch (selectedCategory) {

                case 0:
                    atomPool[1]+=atomPool[0];
                    break;
                case 1:
                    atomPool[2]+=atomPool[0];
                    break;
                case 2:
                    atomPool[3]+=atomPool[0];
                    break;
                case 3:
                    atomPool[4]+=atomPool[0];
                    break;
            }
            atomPool[0]= 0;
        }

        return atomPool;
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
