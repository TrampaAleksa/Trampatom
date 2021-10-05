package com.trampatom.game.trampatom.power.up;

public class PowerUpCooldownHandler {

    public static final int POWER_UP_COOLDOWN = 5;
    public static final int POWER_UP_DURATION = 2;

    boolean onCooldown = false;
    boolean resettedPowerUp = false;

    int coolDown;
    int coolDownTimer = 0;

    public PowerUpCooldownHandler() {
        coolDown = POWER_UP_COOLDOWN;
    }

    public void updateCooldown() {
        if(coolDown > (coolDownTimer)/1000){
            // has to increment equally to the millis interval of ticks
            coolDownTimer += 100;
        }
        else {
            deactivateCooldown();
        }
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }
    public void activateCooldown() {
        resettedPowerUp = false;
        onCooldown = true;
    }
    private void deactivateCooldown() {
        onCooldown = false;
        coolDownTimer = 0;
    }

    public boolean isPowerUpExpired() {
        return coolDown - POWER_UP_DURATION < (coolDownTimer) / 1000 && !resettedPowerUp;
    }
    /**
     * Tells the Cooldown Handler that the power up has expired and triggers an event related to that
     * @param event defines what happens when the Power Up Expires
     */
    public void powerUpExpired(IPowerUpExpiredEvent event) {
        resettedPowerUp = true;
        event.onPowerUpExpired();
    }
}
