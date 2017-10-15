package com.trampatom.game.trampatom.power.up;

import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.utils.Keys;

/**
 * Class containing methods for setting chances of certain random events based on selected passive power up
 * and containing certain random events that can occur in the game.
 *
 */

public class ChancePassivesAndEvents {

        PowerUpPool selectedPassivePowerUp;
        int selectedPowerUpChance;
    private static final int CHANCE_ATOM_DROP_BONUS_MODIFIER= 5;


    /**
     * Constructor that sets the chance ofr the power up's event to happen based on the level of the power up we passed.
     * <p>
     *     NOTE: The passed power up is always a "chance" power up.
     * </p>
     * @param selectedPassivePowerUp required to set the chance of the event based on what power up we selected.
     */
    public ChancePassivesAndEvents(PowerUpPool selectedPassivePowerUp){

        this.selectedPassivePowerUp = selectedPassivePowerUp;
        calculateChanceOfPowerUpEvent();

    }

    /**
     * Method that sets the chance of a power up occurring based on the level of the selected power up and the selected power up.
     * Every power up has a different fixed chance of happening and the level just changes that chance.
     */
    private void calculateChanceOfPowerUpEvent() {

        selectedPowerUpChance = selectedPassivePowerUp.getCurrentLevel()*2;

        switch(selectedPassivePowerUp.getId()){

            case Keys.FLAG_YELLOW_RANDOM_EVENT_CHANCE:
                // TODO change chances of events from power up based on the selected one, now all are the same
                //selectedPowerUpChance = selectedPowerUpChance;
                break;
            case Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS:
                selectedPowerUpChance = selectedPowerUpChance+CHANCE_ATOM_DROP_BONUS_MODIFIER;
                break;
            case Keys.FLAG_YELLOW_CHANCE_LIMITING_SQUARE:
                //TODO power up disabled. chance is 0 change at later time
               // selectedPowerUpChance = selectedPowerUpChance+50;
                selectedPowerUpChance = 0;
                break;
            case Keys.FLAG_YELLOW_CHANCE_ENERGY_FILL:
               // selectedPowerUpChance = selectedPowerUpChance;
                break;

        }

    }

    /**
     * Method used to get the chance of a passive power up event. The higher this number is the bigger the chance that the
     * event cooresponding with the selected power up will happen.
     */
    public int getPassivePowerUpEventChance(){

        return selectedPowerUpChance;
    }
    public int getSelectedPassivePowerUpFlag(){
        return selectedPassivePowerUp.getId();
    }

}
