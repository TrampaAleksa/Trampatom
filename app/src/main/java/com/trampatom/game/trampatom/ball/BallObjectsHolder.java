package com.trampatom.game.trampatom.ball;

import com.trampatom.game.trampatom.Model.Ball;

public class BallObjectsHolder {

    private Ball ballObject;
    private Ball[] purpleBallObjects;
    private Ball[] multipleBalls;

    public Ball getBallObject() {
        return ballObject;
    }
    public void setBallObject(Ball ballObject) {
        this.ballObject = ballObject;
    }

    public Ball[] getPurpleBallObjects() {
        return purpleBallObjects;
    }
    public void setPurpleBallObjects(Ball[] purpleBallObjects){
        this.purpleBallObjects = purpleBallObjects;
    }

    public Ball[] getMultipleBalls() {
        return multipleBalls;
    }
    public void setMultipleBalls(Ball[] multipleBalls) {
        this.multipleBalls = multipleBalls;
    }

}
