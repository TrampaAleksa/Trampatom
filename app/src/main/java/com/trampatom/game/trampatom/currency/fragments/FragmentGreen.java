package com.trampatom.game.trampatom.currency.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.Fragments;
import com.trampatom.game.trampatom.utils.Keys;

/**
 * Fragment containing the green category for the shop. Green contains CoolDown actives that can be used multiple times.
 */

public class FragmentGreen extends Fragment implements View.OnClickListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Button bSizeIncrease, bSmallEnergyBonus;


    // newInstance constructor for creating fragment with arguments
    public static FragmentGreen newInstance(int category) {
        FragmentGreen fragmentGreen = new FragmentGreen();
        Bundle args = new Bundle();
        fragmentGreen.setArguments(args);
        return fragmentGreen;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //set a bundle to be later cached by some activity

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_green, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvRed);

        bSizeIncrease = (Button) view.findViewById(R.id.bBiggerSize);
        bSmallEnergyBonus = (Button) view.findViewById(R.id.bSmallEnergyBoost);
        bSizeIncrease.setOnClickListener(this);
        bSmallEnergyBonus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bSmallEnergyBoost:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_POWER_UP_ONE, Keys.FLAG_GREEN_SMALL_ENERGY_BONUS);
                editor.apply();
                break;

            case R.id.bBiggerSize:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_POWER_UP_ONE, Keys.FLAG_GREEN_INCREASE_BALL_SIZE);
                editor.apply();
                break;


        }

    }
}
