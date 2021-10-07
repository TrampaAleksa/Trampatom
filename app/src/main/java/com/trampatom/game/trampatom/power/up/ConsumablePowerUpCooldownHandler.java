package com.trampatom.game.trampatom.power.up;

public class ConsumablePowerUpCooldownHandler {

    private static final int POWER_UP_COOLDOWN = 5;

    //For managing weather or not we can activate any of the power ups(in cooldown or used)
    boolean usedConsumable = false;
    boolean isConsumableActive = false;
    // the duration of every coolDown, timer used to help determine when the coolDown expired
    int consumableDuration;
    int consumableCountDownTimer = 0;

    IPowerUpExpiredEvent powerUpExpiredEvent;

    public ConsumablePowerUpCooldownHandler(IPowerUpExpiredEvent powerUpExpiredEvent) {
        consumableDuration = POWER_UP_COOLDOWN;
        this.powerUpExpiredEvent = powerUpExpiredEvent;
    }

    public void tryUpdatingConsumableCooldown() {
        if(isConsumableActive) {
            boolean consumableNotExpired = consumableDuration > (consumableCountDownTimer) / 1000;
            if (consumableNotExpired) {
                // has to increment equally to the millis interval of ticks
                consumableCountDownTimer += 100;
                return;
            }

            deactivateConsumableCooldown();
            consumablePowerUpExpired();
        }
    }

    /**
     * Some Consumable Power Ups have an "instant" activation that does not have a duration
     * which means there is no need to set 'isConsumableActive' to true since its not a Power Up that 'Stays Active'.
     * @param consumableHasDuration determines if we should start a cooldown for how long the Power Up stays Activated.
     */
    public void activateConsumableCooldown(boolean consumableHasDuration) {
        isConsumableActive = consumableHasDuration;
        usedConsumable = true;
    }
    public void consumablePowerUpExpired() {
        deactivateConsumableCooldown();
        powerUpExpiredEvent.onPowerUpExpired();
    }
    public boolean isUsedConsumable(){
        return usedConsumable;
    }

    private void deactivateConsumableCooldown() {
        isConsumableActive = false;
        consumableCountDownTimer = 0;
    }

}
