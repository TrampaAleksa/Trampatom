package com.trampatom.game.trampatom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.ShopHandler;
import com.trampatom.game.trampatom.currency.fragments.FragmentGreen;
import com.trampatom.game.trampatom.currency.fragments.FragmentPurple;
import com.trampatom.game.trampatom.currency.fragments.FragmentRed;
import com.trampatom.game.trampatom.currency.fragments.FragmentYellow;

/**
 * Important activity that contains the shop. The shop is used for purchasing various power ups.
 * <p>Shop currency is based on Atoms and their types.</p>
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class ShopActivity extends AppCompatActivity{

    private static final int CATEGORY_NUMBER = 4;

    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;
    //view pager used to cycle between categories in the shop
    //ViewPager adapterViewPager;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_view);

        init();
    }

    private void init() {
        //initialize views
        /*tvNumberAtomsBlue = (TextView) findViewById(R.id.tvAtomNumberBlue);
        tvNumberAtomsRed = (TextView) findViewById(R.id.tvAtomNumberRed);
        tvNumberAtomsGreen = (TextView) findViewById(R.id.tvAtomNumberGreen);
        tvNumberAtomsYellow = (TextView) findViewById(R.id.tvAtomNumberYellow);
        tvNumberAtomsPurple = (TextView) findViewById(R.id.tvAtomNumberPurple);

        //Classes
        atomPool = new AtomPool(this);
        shopHandler = new ShopHandler(atomPool);
        shopHandler.initializeAtomNumbersDisplay(
                tvNumberAtomsBlue,
                tvNumberAtomsRed,
                tvNumberAtomsGreen,
                tvNumberAtomsYellow,
                tvNumberAtomsPurple);

        shopHandler.setAtomPoolValues();*/
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpCategory);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }


    /**
     * Inner class used to get the right category of the shop based on the selected one using an adapter
     */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
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
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FragmentRed.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FragmentGreen.newInstance(1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return FragmentYellow.newInstance(2);
                case 3: // Fragment # 1 - This will show SecondFragment
                    return FragmentPurple.newInstance(3);
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
