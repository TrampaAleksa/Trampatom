package com.trampatom.game.trampatom.currency.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.Fragments;

/**
 * Fragment containing the purple category for the shop
 */

public class FragmentPurple extends Fragment{


    // Store instance variables
    private int category;

    // newInstance constructor for creating fragment with arguments
    public static FragmentPurple newInstance(int category) {
        FragmentPurple fragmentPurple = new FragmentPurple();
        Bundle args = new Bundle();
        args.putInt(Fragments.CATEGORY_BUNDLE, category);
        fragmentPurple.setArguments(args);
        return fragmentPurple;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set a byndle to be later cached by some activity
            category = getArguments().getInt(Fragments.CATEGORY_BUNDLE, 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_purple, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvRed);
        return view;
    }
}
