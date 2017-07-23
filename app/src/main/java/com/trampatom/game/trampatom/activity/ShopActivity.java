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

    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;
    //view pager used to cycle between categories in the shop
    MyPagerAdapter adapterViewPager;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;
    PowerUpPool powerUp;
    List<PowerUpPool> powerUpPool;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_view);

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
        shopHandler = new ShopHandler(atomPool, this);
        shopHandler.initializeAtomNumbersDisplay(
                tvNumberAtomsBlue,
                tvNumberAtomsRed,
                tvNumberAtomsGreen,
                tvNumberAtomsYellow,
                tvNumberAtomsPurple);
        //set the currency levels
        shopHandler.setAtomPoolValues();
        //contains a list of power ups
        powerUpPool = shopHandler.loadPowerUpPool();

        //set up the view pager and the tabs for changing categories
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpCategory);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), powerUpPool);
        vpPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabCategories);
        tabLayout.setupWithViewPager(vpPager);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            //case R.id.bTrade:

               // break;

        }
    }


    /**
     * Inner class used to get the right category of the shop based on the selected one using an adapter
     */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        List<PowerUpPool> powerUpPoolFragment;
        private static int NUM_ITEMS = 4;

        /**
         * Class used to get the pager adapter used to cycle between fragments/categories.
         * @param fragmentManager needed to manage swapping and inflating of fragments
         * @param powerUpPoolFragment needed to get the power ups for each fragment and displaying them
         */
        public MyPagerAdapter(FragmentManager fragmentManager, List<PowerUpPool> powerUpPoolFragment) {
            super(fragmentManager);
            this.powerUpPoolFragment = powerUpPoolFragment;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // RED
                    return FragmentRed.newInstance(powerUpPoolFragment);
                case 1: // GREEN
                    return FragmentGreen.newInstance(powerUpPoolFragment);
                case 2: // YELLOW
                    return FragmentYellow.newInstance(powerUpPoolFragment);
                case 3: // PURPLE
                    return FragmentPurple.newInstance(powerUpPoolFragment);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }


}
