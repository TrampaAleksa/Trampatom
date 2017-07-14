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
 * Fragment containing the yellow category for the shop
 */

public class FragmentYellow extends Fragment implements View.OnClickListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Button bSlowDownBalls, bStartingEnergy;


    // newInstance constructor for creating fragment with arguments
    public static FragmentYellow newInstance(int category) {
        FragmentYellow fragmentYellow = new FragmentYellow();
        Bundle args = new Bundle();
        fragmentYellow.setArguments(args);
        return fragmentYellow;
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
        View view = inflater.inflate(R.layout.fragment_category_yellow, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvRed);
        bSlowDownBalls = (Button) view.findViewById(R.id.bSlowDownBalls);
        bStartingEnergy = (Button) view.findViewById(R.id.bStartingEnergy);
        bSlowDownBalls.setOnClickListener(this);
        bStartingEnergy.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bSlowDownBalls:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_PASSIVE_TWO, Keys.FLAG_YELLOW_SLOW_DOWN_BALLS);
                editor.apply();

                break;

            case R.id.bStartingEnergy:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_PASSIVE_TWO, Keys.FLAG_YELLOW_MORE_ENERGY_ON_START);
                editor.apply();

                break;
        }
    }
}
