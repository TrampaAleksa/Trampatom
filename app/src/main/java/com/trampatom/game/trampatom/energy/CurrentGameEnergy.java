package com.trampatom.game.trampatom.energy;

public class CurrentGameEnergy {

    private static final int STARTING_ENERGY = 5000;
    private static final int ENERGY_SPEED_UP_TICKS = 4;

    int currentEnergyLevel;

    int energySpeedUpTicks;
    int energyDecrease = 1;

    boolean lowEnergy = false;
    boolean middleEnergy = false;

    public CurrentGameEnergy() {
        energySpeedUpTicks = ENERGY_SPEED_UP_TICKS;
    }

    // CURRENT ENERGY LEVEL SEGMENT

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

    // ENERGY DECAY SEGMENT

    public int getEnergyDecrease(){
        return energyDecrease;
    }
    public void updateEnergy(){
        energySpeedUpTicks++;

        if (shouldIncreaseEnergyDecrease()) {
            increaseEnergyDecrease(1);
            energySpeedUpTicks = 0;
        }
    }

    private boolean shouldIncreaseEnergyDecrease(){
        boolean isFourthTick = energySpeedUpTicks % 4 == 0;
        boolean isMax = getEnergyDecrease() >= 50;

        return isFourthTick && !isMax;
    }
    public void increaseEnergyDecrease(int toAdd){
        energyDecrease += toAdd;
    }
    public void reduceEnergyDecrease(int toReduce){ //todo - rename to decay instead of decrease
        energyDecrease -= toReduce;
    }


    // MIDDLE AND LOW ENERGY SEGMENT

    //todo - color doesn't return back to green if returned above the middle value
    public boolean triggeredMiddleEnergy(){
        if(getCurrentEnergyLevel() < getStartingEnergy() /2 && !middleEnergy)
        {
            middleEnergy = true;
            return true;
        }
        return false;
    }
    public boolean triggeredLowEnergy(){
        if(getCurrentEnergyLevel() < getStartingEnergy() /4 && !lowEnergy)
        {
            lowEnergy = true;
            return true;
        }
        return false;
    }

}
