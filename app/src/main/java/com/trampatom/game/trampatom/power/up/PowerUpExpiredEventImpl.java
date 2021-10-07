package com.trampatom.game.trampatom.power.up;

import com.trampatom.game.trampatom.Model.Ball;
import com.trampatom.game.trampatom.currency.PowerUps;
import com.trampatom.game.trampatom.utils.Keys;

public class PowerUpExpiredEventImpl implements IPowerUpExpiredEvent{

    int flagTypePowerUp1;
    private int selectedPowerUp1;
    private final PowerUps powerUps;

    private Ball ballObject;
    private Ball[] purpleBallObjects;
    private Ball[] multipleBalls;


    public PowerUpExpiredEventImpl(PowerUps powerUps) {
        this.powerUps = powerUps;
    }

    public PowerUpExpiredEventImpl setFlags(int flagTypePowerUp1 , int selectedPowerUp1){
        this.flagTypePowerUp1 = flagTypePowerUp1;
        this.selectedPowerUp1 = selectedPowerUp1;

        return this;
    }

    public PowerUpExpiredEventImpl setBallReferences(Ball ballObject, Ball[] purpleBallObjects, Ball[] multipleBalls){

        this.ballObject = ballObject;
        this.purpleBallObjects = purpleBallObjects;
        this.multipleBalls = multipleBalls;

        return this;
    }

    @Override
    public void onPowerUpExpired() {
        if (flagTypePowerUp1 != Keys.FLAG_BALL_POWER_UP)
            return;
        //if the power up is ball related, reset the balls after the power up expires
        ballObject = powerUps.resetBallState(ballObject, selectedPowerUp1);
        purpleBallObjects = powerUps.resetBallObjectArrayState(purpleBallObjects,
                selectedPowerUp1, Keys.PURPLE_BALL_NUMBER);
        multipleBalls = powerUps.resetBallObjectArrayState(multipleBalls,
                selectedPowerUp1, Keys.WAVE_BALL_NUMBER);
    }
}
