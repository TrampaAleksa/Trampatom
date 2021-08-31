package com.trampatom.game.trampatom.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.R;

/**
 * Important class containing methods that should be used to get the required
 * sounds for every activity with sounds.
 */
public class SoundsAndEffects {

    // public variables containing id's of every sound to be accessed by the sound pool
    public int soundClickedId = R.raw.clickedball;
    public int soundNearlyGameOverId = R.raw.nearly_out_of_time;
    public int soundGameOverId = R.raw.gameover;
    public int soundBoughtShopItemId = R.raw.money_spent;
    public int soundInvalidActionId = R.raw.invalid_purchase;
    public int soundEnteredShopId = R.raw.opening_shop;
    public int soundPowerUpUsedId = R.raw.power_up_used;
    public int soundSelectedPowerUp = R.raw.selected_power_up_successfully;

    SoundPool soundPool;
    Context context;

    //used to return all sounds only after they are loaded
    int loadedNumber;

    /**
     * constructor used to get the sound pool object created at the start of every activity
     * and the context of that activity to be used for loading sounds
     */
    public SoundsAndEffects(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        this.context = context;
    }

    public void play(int soundId){
        if (soundPool == null) return;
        soundPool.play(soundId, 1,1,0,0,1);
    }
    public void play(int soundId, int priority){
        if (soundPool == null) return;
        soundPool.play(soundId, 1,1,priority,0,1);
    }
    public void playBallClickedSound(Ball ball){
        play(soundClickedId);
    }


    /**
     * method that should be called every time the classic game activity is started.
     * Fills a sound pool with the required sounds
     * @return a sound pool object containing loaded sounds
     */
    public SoundPool getGameClassicSounds(){

        soundClickedId = soundPool.load(context, soundClickedId, 1);
        soundGameOverId = soundPool.load(context, soundGameOverId, 1);
        soundNearlyGameOverId = soundPool.load(context, soundNearlyGameOverId, 1);
        soundPowerUpUsedId = soundPool.load(context, soundPowerUpUsedId, 1);

        return soundPool;
    }

    /**
     * method that should be called every time the shop game activity is started.
     * Fills a sound pool with the required sounds
     * @return a sound pool object containing loaded sounds
     */
    public SoundPool getShopSounds(){

        soundEnteredShopId= soundPool.load(context, soundEnteredShopId, 1);
        soundInvalidActionId= soundPool.load(context, soundInvalidActionId, 1);
        soundBoughtShopItemId = soundPool.load(context, soundBoughtShopItemId, 1);
        soundSelectedPowerUp = soundPool.load(context, soundSelectedPowerUp, 1);

        return soundPool;
    }

    /**
     * method that should be called every time the survival game activity is started.
     * Fills a sound pool with the required sounds
     * @return a sound pool object containing loaded sounds
     */
    public SoundPool getGameSurvivalSounds(){

        soundClickedId =  soundPool.load(context, soundClickedId, 1);
        soundGameOverId = soundPool.load(context, soundGameOverId, 1);

        return soundPool;
    }


    /**
     * Method that SHOULD be called when the activity running/using a soundpool is done working
     * @param soundPool the sound pool currently using the sounds
     * @return a null sound pool object
     */
    public SoundPool releaseSoundPool(SoundPool soundPool){

        soundPool.release();
        soundPool = null;
        return soundPool;
    }

    /**
     * Method that SHOULD be called when the activity running/using a soundpool is done working
     * @return a null sound pool object
     */
    public void releaseSoundPool(){
        soundPool.release();
        soundPool = null;
    }
}
