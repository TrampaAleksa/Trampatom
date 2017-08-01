package com.trampatom.game.trampatom.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.ShopHandler;
import com.trampatom.game.trampatom.currency.fragments.FragmentGreen;
import com.trampatom.game.trampatom.currency.fragments.FragmentPurple;
import com.trampatom.game.trampatom.currency.fragments.FragmentRed;
import com.trampatom.game.trampatom.currency.fragments.FragmentYellow;

import java.util.List;

/**
 * Important activity that contains the shop. The shop is used for purchasing various power ups.
 * <p>Shop currency is based on Atoms and their types.</p>
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CATEGORY_NUMBER = 4;
    public int currentFragmentPossition;

    TextView tvTitle;
    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;
    ImageView ivAtomRed, ivAtomGreen, ivAtomYellow,ivAtomPurple;
    //view pager used to cycle between categories in the shop
   // MyPagerAdapter adapterViewPager
    ViewPager vpPager;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;
    PowerUpPool powerUp;
    List<PowerUpPool> powerUpPool;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        init();
    }

    private void init() {

        //initialize views
     /*   tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvNumberAtomsBlue = (TextView) findViewById(R.id.tvAtomNumberBlue);
        tvNumberAtomsRed = (TextView) findViewById(R.id.tvAtomNumberRed);
        tvNumberAtomsGreen = (TextView) findViewById(R.id.tvAtomNumberGreen);
        tvNumberAtomsYellow = (TextView) findViewById(R.id.tvAtomNumberYellow);
        tvNumberAtomsPurple = (TextView) findViewById(R.id.tvAtomNumberPurple);
        ivAtomRed = (ImageView) findViewById(R.id.ivAtomRed);
        ivAtomGreen = (ImageView) findViewById(R.id.ivAtomGreen);
        ivAtomYellow = (ImageView) findViewById(R.id.ivAtomYellow);
        ivAtomPurple = (ImageView) findViewById(R.id.ivAtomPurple);
        ivAtomRed.setOnClickListener(this);
        ivAtomGreen.setOnClickListener(this);
        ivAtomYellow.setOnClickListener(this);
        ivAtomPurple.setOnClickListener(this);


        //Classes
        atomPool = new AtomPool(this);
        shopHandler = new ShopHandler(atomPool, this);
        shopHandler.initializeAtomNumbersDisplay(
                tvNumberAtomsRed,
                tvNumberAtomsGreen,
                tvNumberAtomsYellow,
                tvNumberAtomsPurple);
        //set the currency levels
        shopHandler.setAtomPoolValues();
        //contains a list of power ups
        powerUpPool = shopHandler.loadPowerUpPool();

*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.ivAtomRed:
                vpPager.setCurrentItem(0);
                break;
            case R.id.ivAtomGreen:
                vpPager.setCurrentItem(1);
                break;
            case R.id.ivAtomYellow:
                vpPager.setCurrentItem(2);
                break;
            case R.id.ivAtomPurple:
                vpPager.setCurrentItem(3);
                break;

        }
    }


}
