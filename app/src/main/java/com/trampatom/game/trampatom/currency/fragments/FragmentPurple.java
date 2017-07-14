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
 * Fragment containing the purple category for the shop
 */

public class FragmentPurple extends Fragment implements View.OnClickListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Button bIncreaseBallSize, bSlowerEnergyConsumption;


    // newInstance constructor for creating fragment with arguments
    public static FragmentPurple newInstance(int category) {
        FragmentPurple fragmentPurple = new FragmentPurple();
        Bundle args = new Bundle();
        fragmentPurple.setArguments(args);
        return fragmentPurple;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_purple, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvRed);
        bIncreaseBallSize = (Button) view.findViewById(R.id.bIncreaseBallSize);
        bSlowerEnergyConsumption = (Button) view.findViewById(R.id.bSlowerEnergyConsumption);
        bIncreaseBallSize.setOnClickListener(this);
        bSlowerEnergyConsumption.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bIncreaseBallSize:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_PASSIVE_ONE, Keys.FLAG_PURPLE_BIGGER_BALLS);
                editor.apply();

                break;

            case R.id.bSlowerEnergyConsumption:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_PASSIVE_ONE, Keys.FLAG_PURPLE_SLOWER_ENERGY_DECAY);
                editor.apply();

                break;
        }
    }
}
