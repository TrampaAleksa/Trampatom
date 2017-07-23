package com.trampatom.game.trampatom.currency;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.Keys;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.ContentValues.TAG;

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
     * <p>
     *     NOTE: In case we did not find a file with the power ups this method will
     *     return a new default power up pool that NEEDS to be saved , if not it will
     *     be created anew again.
     * </p>
     * @return A PowerUpObject from a json file within the phone
     */
    public List<PowerUpPool> loadPowerUpPool(){

        //if the file wasn't loaded the string will be empty
        String ret = "";

        Gson gson = new Gson();
        //get a json string fro ma file
        ret = readFile(context, "PowerUps.txt");
        if(ret != null) {
            //since we are loading a list of class objects we need a type token for gson to properly work
            Type type = new TypeToken<List<PowerUpPool>>() {
            }.getType();
            List<PowerUpPool> powerUpPool = gson.fromJson(ret, type);
            savePowerUpPool(powerUpPool);
            return powerUpPool;
        }
        else {
            //in case we don't have the file with the power ups create a new
            //power up object with the default values for power ups
            List<PowerUpPool> firstTimePowerUpPool = new ArrayList<>();

            firstTimePowerUpPool = firstTimeLoading();
            return firstTimePowerUpPool;
        }
    }

    /**
     * Method that should be used to load a list of power up objects if they
     * were not found in a file or weren't created before.
     * <p>
     *     NOTE: inside the method every id should be set according to a flag
     *     contained within the Keys class to make power up functions in the game
     *     work properly.
     * </p>
     * @return a list of power up objects with initial values set in this method
     */
    private List<PowerUpPool> firstTimeLoading(){
        List<PowerUpPool> initialPowerUpPool = new ArrayList<>();
        //used as a help object to store unique objects into the list
        PowerUpPool powerUp = new PowerUpPool();
        // First power up
                //add every attribute to the power up
         powerUp = new PowerUpPool();
            powerUp.setId(Keys.FLAG_RED_FREEZE_BALLS);
            powerUp.setCategory(Keys.CATEGORY_RED);
            powerUp.setImageId(R.drawable.wave5);
            powerUp.setDescription(context.getString(R.string.description_red_freeze));
            powerUp.setBaseCost(100);
            powerUp.setCurrentLevel(3);
                //after adding every attribute add that power up to the pool
            initialPowerUpPool.add(powerUp);
        // Second power up
                //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_RED_BIG_ENERGY_BONUS);
        powerUp.setCategory(Keys.CATEGORY_RED);
        powerUp.setImageId(R.drawable.atomcrvena);
        powerUp.setDescription(context.getString(R.string.description_red_energy_boost));
        powerUp.setBaseCost(50);
        powerUp.setCurrentLevel(2);
                //after adding every attribute add that power up to the pool
            initialPowerUpPool.add(powerUp);

        // Third power up
                //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_RED_LIMITING_SQUARE);
        powerUp.setCategory(Keys.CATEGORY_RED);
        powerUp.setImageId(R.drawable.wave3);
        powerUp.setDescription(context.getString(R.string.description_red_limiting_Square));
        powerUp.setBaseCost(25);
        powerUp.setCurrentLevel(1);
                //after adding every attribute add that power up to the pool
            initialPowerUpPool.add(powerUp);

        // Fourth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_RED_UNKNOWN2);
        powerUp.setCategory(Keys.CATEGORY_RED);
        powerUp.setImageId(R.drawable.wave2);
        powerUp.setDescription(context.getString(R.string.description_red_unknown4));
        powerUp.setBaseCost(10);
        powerUp.setCurrentLevel(5);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Fifth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_GREEN_SLOW_DOWN_BALLS);
        powerUp.setCategory(Keys.CATEGORY_GREEN);
        powerUp.setImageId(R.drawable.wave1);
        powerUp.setDescription(context.getString(R.string.description_green_slow_balls));
        powerUp.setBaseCost(160);
        powerUp.setCurrentLevel(0);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Sixth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_GREEN_SMALL_ENERGY_BONUS);
        powerUp.setCategory(Keys.CATEGORY_GREEN);
        powerUp.setImageId(R.drawable.wave7);
        powerUp.setDescription(context.getString(R.string.description_green_energy_boost));
        powerUp.setBaseCost(90);
        powerUp.setCurrentLevel(2);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Seventh power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_GREEN_INCREASE_BALL_SIZE);
        powerUp.setCategory(Keys.CATEGORY_GREEN);
        powerUp.setImageId(R.drawable.wave6);
        powerUp.setDescription(context.getString(R.string.description_green_increase_size));
        powerUp.setBaseCost(400);
        powerUp.setCurrentLevel(4);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Eight power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_GREEN_UNKNOWN2);
        powerUp.setCategory(Keys.CATEGORY_GREEN);
        powerUp.setImageId(R.drawable.wave5);
        powerUp.setDescription(context.getString(R.string.description_green_unknown4));
        powerUp.setBaseCost(15);
        powerUp.setCurrentLevel(4);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Ninth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_PURPLE_BIGGER_BALLS);
        powerUp.setCategory(Keys.CATEGORY_PURPLE);
        powerUp.setImageId(R.drawable.wave4);
        powerUp.setDescription(context.getString(R.string.description_purple_bigger_balls));
        powerUp.setBaseCost(100);
        powerUp.setCurrentLevel(5);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Tenth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_PURPLE_SLOWER_ENERGY_DECAY);
        powerUp.setCategory(Keys.CATEGORY_PURPLE);
        powerUp.setImageId(R.drawable.wave3);
        powerUp.setDescription(context.getString(R.string.description_purple_slow_decay));
        powerUp.setBaseCost(30);
        powerUp.setCurrentLevel(2);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Eleventh power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_PURPLE_UNKNOWN3);
        powerUp.setCategory(Keys.CATEGORY_PURPLE);
        powerUp.setImageId(R.drawable.wave2);
        powerUp.setDescription(context.getString(R.string.description_purple_unknown3));
        powerUp.setBaseCost(200);
        powerUp.setCurrentLevel(1);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Twelfth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_PURPLE_UNKNOWN4);
        powerUp.setCategory(Keys.CATEGORY_PURPLE);
        powerUp.setImageId(R.drawable.wave1);
        powerUp.setDescription(context.getString(R.string.description_purple_unknown4));
        powerUp.setBaseCost(10);
        powerUp.setCurrentLevel(5);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Thirteenth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_YELLOW_MORE_ENERGY_ON_START);
        powerUp.setCategory(Keys.CATEGORY_YELLOW);
        powerUp.setImageId(R.drawable.atomplava);
        powerUp.setDescription(context.getString(R.string.description_yellow_energy_on_start));
        powerUp.setBaseCost(15);
        powerUp.setCurrentLevel(4);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Fourteenth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_YELLOW_SLOW_DOWN_BALLS);
        powerUp.setCategory(Keys.CATEGORY_YELLOW);
        powerUp.setImageId(R.drawable.atomroze);
        powerUp.setDescription(context.getString(R.string.description_yellow_slow_balls));
        powerUp.setBaseCost(250);
        powerUp.setCurrentLevel(5);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Fifteenth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_YELLOW_UNKNOWN3);
        powerUp.setCategory(Keys.CATEGORY_YELLOW);
        powerUp.setImageId(R.drawable.atomzelena);
        powerUp.setDescription(context.getString(R.string.description_yellow_unknown3));
        powerUp.setBaseCost(40);
        powerUp.setCurrentLevel(2);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        // Sixteenth power up
        //add every attribute to the power up
        powerUp = new PowerUpPool();
        powerUp.setId(Keys.FLAG_YELLOW_UNKNOWN4);
        powerUp.setCategory(Keys.CATEGORY_YELLOW);
        powerUp.setImageId(R.drawable.plus);
        powerUp.setDescription(context.getString(R.string.description_yellow_unknown4));
        powerUp.setBaseCost(25);
        powerUp.setCurrentLevel(2);
        //after adding every attribute add that power up to the pool
        initialPowerUpPool.add(powerUp);

        savePowerUpPool(initialPowerUpPool);
        return initialPowerUpPool;


    }
    /**
     * Method that should return only two power ups, the ones we selected to be used within the game.
     * <p>
     *     NOTE: this method should be used within the game for performance reasons so that we don't have to iterate through
     *     the whole power up list but only two.
     * </p>
     * @return a array list with two power up pool objects to be used.
     */
    public List<PowerUpPool> loadSelectedPowerUps(){

        //if the file wasn't loaded the string will be empty
        String ret = "";

        Gson gson = new Gson();
        //get a json string fro ma file
        ret = readFile(context, "SelectedPowerUps.txt");
        if(ret != null) {
            //since we are loading a list of class objects we need a type token for gson to properly work
            Type type = new TypeToken<List<PowerUpPool>>() {
            }.getType();
            List<PowerUpPool> powerUpPool = gson.fromJson(ret, type);

            return powerUpPool;
        }
        return null;
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
    public void saveSelectedPowerUps(List<PowerUpPool> powerUpPool){

        Gson gson = new Gson();
        String json = gson.toJson(powerUpPool);
        writeFile(context,json, "SelectedPowerUps.txt");

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
