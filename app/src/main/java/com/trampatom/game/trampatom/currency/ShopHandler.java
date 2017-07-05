package com.trampatom.game.trampatom.currency;


import android.widget.TextView;

import com.trampatom.game.trampatom.utils.Keys;

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

    /**
     * Constructor used for inserting an atom pool object for its methods to be used by the shop handler
     * @param atomPool an AtomPool object created elsewhere
     */
    public ShopHandler(AtomPool atomPool){

        this.atomPool = atomPool;
    }

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
