package com.trampatom.game.trampatom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.ShopHandler;

/**
 * Important activity that contains the shop. The shop is used for purchasing various power ups.
 * <p>Shop currency is based on Atoms and their types.</p>
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class ShopActivity extends AppCompatActivity{
    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        init();
    }

    private void init() {
        //initialize views
        tvNumberAtomsBlue = (TextView) findViewById(R.id.tvAtomNumberBlue);
        tvNumberAtomsRed = (TextView) findViewById(R.id.tvAtomNumberRed);
        tvNumberAtomsGreen = (TextView) findViewById(R.id.tvAtomNumberGreen);
        tvNumberAtomsYellow = (TextView) findViewById(R.id.tvAtomNumberYellow);
        tvNumberAtomsPurple = (TextView) findViewById(R.id.tvAtomNumberPurple);

        //Classes
        atomPool = new AtomPool(this);
        shopHandler = new ShopHandler();
    }
}
