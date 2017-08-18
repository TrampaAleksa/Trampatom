package com.trampatom.game.trampatom.activity;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Aleksa on 16.8.2017.
 */

/**
 * Class for indicating if ball was clicked.
 */
public class SimpleBallTouchListener implements View.OnTouchListener {

    interface BallClickedCallback {
        void onClicked(boolean ballClicked);
    }

    BallClickedCallback ballClickedCallback;

    public SimpleBallTouchListener(BallClickedCallback ballClickedCallback) {
        this.ballClickedCallback = ballClickedCallback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //get coordinates where we touched
            /*clickedX = (int) event.getX();
            clickedY = (int) event.getY();
            if(ifBAllClicked){
                ballClickedCallback.onClicked(true);
            }else
                ballClickedCallback.onClicked(false);
        }*/
            return true;
        }
        return true;
    }
}
