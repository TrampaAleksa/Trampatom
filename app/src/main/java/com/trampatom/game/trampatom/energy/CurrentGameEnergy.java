package com.trampatom.game.trampatom.energy;

import com.trampatom.game.trampatom.utils.Keys;

public class CurrentGameEnergy {

    private static final int STARTING_ENERGY = 5000;

    int currentEnergyLevel;

    int energySpeedUpTicks;
    boolean lowEnergy = false; boolean middleEnergy = false;

    public CurrentGameEnergy() {
        energySpeedUpTicks = Keys.ENERGY_SPEED_UP_TICKS;
    }

    public int getCurrentEnergyLevel() {
        return currentEnergyLevel;
    }
    public void setCurrentEnergyLevel(int currentEnergyLevel) {
        this.currentEnergyLevel = currentEnergyLevel;
    }
    public void addEnergy(int toAdd){
        currentEnergyLevel += toAdd;
    }
    public void reduceEnergy(int toReduce){
        currentEnergyLevel -= toReduce;
    }

    public int getStartingEnergy(){
        return STARTING_ENERGY;
    }

}
