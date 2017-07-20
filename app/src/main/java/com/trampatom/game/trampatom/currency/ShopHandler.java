package com.trampatom.game.trampatom.currency;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.utils.Keys;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Class that should contain methods for working with the in game shop.
 * Shop currency is based on Atoms and their types.
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class ShopHandler {
    AtomPool atomPool;
    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;
    Context context;

    /**
     * Constructor used for inserting an atom pool object for its methods to be used by the shop handler
     * @param atomPool an AtomPool object created elsewhere
     */
    public ShopHandler(AtomPool atomPool, Context context){

        this.atomPool = atomPool;
        this.context = context;
    }


    // --------------------------------------------- CURRENCY ------------------------------------ \\

    /**
     * Method used to insert currency display text views into the Shop handler so that it can easily display
     * the amount of atoms we have, upon buying stuff, or upon entering the shop etc.
     * <p>Parameters are the text views holding the number of atoms we have per atom type</p>
     */
    public void initializeAtomNumbersDisplay(TextView tvNumberAtomsBlue, TextView tvNumberAtomsRed, TextView tvNumberAtomsGreen
                                                ,TextView tvNumberAtomsYellow, TextView tvNumberAtomsPurple){

        this.tvNumberAtomsBlue = tvNumberAtomsBlue;
        this.tvNumberAtomsRed = tvNumberAtomsRed;
        this.tvNumberAtomsGreen = tvNumberAtomsGreen;
        this.tvNumberAtomsYellow = tvNumberAtomsYellow;
        this.tvNumberAtomsPurple = tvNumberAtomsPurple;

    }

    /**
     * Method for getting our atom pool in the shop to spend and for displaying the numbers we have in the shop
     * @return an array containing our atom pool to be used for transactions
     */
    public int[] setAtomPoolValues(){

        int[] atomArray = {0,0,0,0,0};
        atomArray = atomPool.getAtoms();

        tvNumberAtomsBlue.setText(Integer.toString(atomArray[0]));
        tvNumberAtomsRed.setText(Integer.toString(atomArray[1]));
        tvNumberAtomsGreen.setText(Integer.toString(atomArray[2]));
        tvNumberAtomsYellow.setText(Integer.toString(atomArray[3]));
        tvNumberAtomsPurple.setText(Integer.toString(atomArray[4]));

        return atomArray;
    }



    // --------------------------------------------- POWER UP POOL OBJECTS ------------------------------------------------ \\

    /**
     * Method for getting a object containing all of our power ups and its attributes for the shop.
     * : Image, Description, upgrade level, base cost, id and category.
     * Should be called when opening the shop to load every power up to use for displaying.
     * @return A PowerUpObject from shared preferences
     */
    public List<PowerUpPool> loadPowerUpPool(){

        //if the file wasn't loaded the string will be empty
        String ret = "";

        Gson gson = new Gson();
        //get a json string fro ma file
        ret = readFile(context, "PowerUps.txt");
        //since we are loading a list of class objects we need a type token for gson to properly work
        Type type = new TypeToken<List<PowerUpPool>>() {
        }.getType();
        List<PowerUpPool> powerUpPool = gson.fromJson(ret, type);

        return powerUpPool;
    }

    /**
     * Method for saving a power up pool that we have changed during the runtime of our shop.
     * Should be called when we exit the shop.
     * @param powerUpPool the power up pool that we are saving so we save any changes we made.
     */
    public void savePowerUpPool(List<PowerUpPool> powerUpPool){

        Gson gson = new Gson();
        String json = gson.toJson(powerUpPool);
        writeFile(context,json, "PowerUps.txt");

    }

    /**
     * Method for reading a file
     * @return the file as a String
     */
    public String readFile(Context context, String file){
        try {
            InputStream inputStream = context.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        return null;
    }

    public void writeFile(Context context, String data, String file){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    public void selectActivePowerUp(int flag){

        switch(flag){
            // RED
            case Keys.FLAG_RED_FREEZE_BALLS:

                break;
            case Keys.FLAG_RED_LIMITING_SQUARE:

                break;
            case Keys.FLAG_RED_UNKNOWN2:

                break;
            case Keys.FLAG_RED_BIG_ENERGY_BONUS:

                break;

            //GREEN
            case Keys.FLAG_GREEN_INCREASE_BALL_SIZE:

                break;
            case Keys.FLAG_GREEN_SLOW_DOWN_BALLS:

                break;

            case Keys.FLAG_GREEN_UNKNOWN2:

                break;

            case Keys.FLAG_GREEN_SMALL_ENERGY_BONUS:

                break;

        }

    }
}
