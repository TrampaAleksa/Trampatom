package com.trampatom.game.trampatom.currency.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.Keys;

import java.util.List;


/**
 * Fragment containing the red category for the shop. Red contains Consumable actives that can be used only once in the game.
 *
 */

public class FragmentRed extends Fragment implements View.OnClickListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    static List<PowerUpPool> powerUpPool;
    PowerUpPool powerUp;

    Button bFreeze, bBigEnergyBonus, bUpgrade, bReduce;

    // newInstance constructor for creating fragment with arguments
    public static FragmentRed newInstance(List<PowerUpPool> powerUpPool) {
        FragmentRed fragmentRed = new FragmentRed();
        Bundle args = new Bundle();
        fragmentRed.setArguments(args);
        FragmentRed.powerUpPool = powerUpPool;
        return fragmentRed;
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
        View view = inflater.inflate(R.layout.fragment_category_red, container, false);

        powerUp = powerUpPool.get(0);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvRed);
        bFreeze = (Button) view.findViewById(R.id.bFreeze);
        bBigEnergyBonus = (Button) view.findViewById(R.id.bBigEnergyBonus);
        bUpgrade = (Button) view.findViewById(R.id.bUpgradePowerOne);
        bReduce = (Button) view.findViewById(R.id.bReduceLevel);
        bFreeze.setOnClickListener(this);
        bBigEnergyBonus.setOnClickListener(this);
        bUpgrade.setOnClickListener(this);
        bReduce.setOnClickListener(this);
        TextView tvPowerUp1 = (TextView) view.findViewById(R.id.tvRedFirstPowerUp);
        tvPowerUp1.setText(powerUp.getDescription());

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bFreeze:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_POWER_UP_TWO, Keys.FLAG_RED_FREEZE_BALLS);
                editor.apply();

                break;

            case R.id.bBigEnergyBonus:
                editor = preferences.edit();
                editor.putInt(Keys.KEY_POWER_UP_TWO, Keys.FLAG_RED_BIG_ENERGY_BONUS);
                editor.apply();

                break;
            case R.id.bUpgradePowerOne:
                powerUp.setCurrentLevel(powerUp.getCurrentLevel()+1);

                break;

            case R.id.bReduceLevel:
                powerUp.setCurrentLevel(powerUp.getCurrentLevel()-1);

                break;


        }
    }
}
