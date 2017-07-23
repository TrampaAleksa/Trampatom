package com.trampatom.game.trampatom.currency.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.Keys;

import java.util.List;


/**
 * Fragment containing the red category for the shop.
 * Red contains Consumable actives that can be used only once in the game.
 * We have the option to buy/ upgrade, select a power up, trade blue for the atom of the category.
 */

public class FragmentRed extends Fragment implements View.OnClickListener{

    int i=0, j=0;
    int selectedPowerUpIndex;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    static List<PowerUpPool> powerUpPool;
    PowerUpPool[] powerUp = {null, null, null, null};
    //two included views containing two power ups each
    RelativeLayout rlItemsView1, rlItemsView2;
    //view containing the power up we currently selected: image, buy button, before stat/ after stat, description and price
    RelativeLayout rlSingleItemView;

    //The views presented as arrays are views used for the four power ups image, price, and level
    Button bBuy, bSelect;
    ImageButton ibTrade;
    TextView tvDescription, tvBeforeAfter, tvCategoryDisplay, tvPrice;
    TextView[] tvPriceItem = {null,null,null,null};
    ImageView ivSelect, ivSelectedItemImage;
    ImageView[] ivItemImage = {null, null, null, null};
    CircularProgressBar[] pbUpgradeProgress = {null, null, null, null};


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
        init(view);
        setUpPowerUps();

        return view;
    }

    /**
     * Initialize all the views contained within the fragment to be accessed
     * @param view parameter needed to access the child views inside it
     * @return the same view we passed as a parameter, unchanged.
     */
    private void init(View view){

        //load all four power ups
        for(i=0; i<Keys.FLAG_RED_UNKNOWN2; i++) {
            powerUp[i] = powerUpPool.get(i);
        }
        //we need to initialize the views that we included in the xml so we can access the views inside it
        rlItemsView1 = (RelativeLayout) view.findViewById(R.id.layoutShopItem1);
        rlItemsView2 = (RelativeLayout) view.findViewById(R.id.layoutShopItem2);
        rlSingleItemView = (RelativeLayout) view.findViewById(R.id.layoutPowerUpView);
        //initialize views that are inside the red fragment and not in the included views
        bSelect = (Button) view.findViewById(R.id.bSelect);
        ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
        tvCategoryDisplay = (TextView) view.findViewById(R.id.tvCategoryName);
        ibTrade = (ImageButton) view.findViewById(R.id.ibTrade);
        //initialize views inside the included views
        bBuy = (Button) rlSingleItemView.findViewById(R.id.bBuy);
        ivSelectedItemImage = (ImageView) rlSingleItemView.findViewById(R.id.ivSelectedPowerUp);
        tvPrice = (TextView) rlSingleItemView.findViewById(R.id.tvPrice);
        tvDescription = (TextView) rlSingleItemView.findViewById(R.id.tvDescription);
        tvBeforeAfter = (TextView) rlSingleItemView.findViewById(R.id.tvBeforeAfter);
        //views of the power ups included
        tvPriceItem[0] = (TextView) rlItemsView1.findViewById(R.id.tvCost);
        tvPriceItem[1] = (TextView) rlItemsView1.findViewById(R.id.tvCost2);
        tvPriceItem[2] = (TextView) rlItemsView2.findViewById(R.id.tvCost);
        tvPriceItem[3] = (TextView) rlItemsView2.findViewById(R.id.tvCost2);
        ivItemImage[0] = (ImageView) rlItemsView1.findViewById(R.id.ivIcon);
        ivItemImage[1] = (ImageView) rlItemsView1.findViewById(R.id.ivIcon2);
        ivItemImage[2] = (ImageView) rlItemsView2.findViewById(R.id.ivIcon);
        ivItemImage[3] = (ImageView) rlItemsView2.findViewById(R.id.ivIcon2);
        pbUpgradeProgress[0] = (CircularProgressBar) rlItemsView1.findViewById(R.id.upgradeProgress);
        pbUpgradeProgress[1] = (CircularProgressBar) rlItemsView1.findViewById(R.id.upgradeProgress2);
        pbUpgradeProgress[2] = (CircularProgressBar) rlItemsView2.findViewById(R.id.upgradeProgress);
        pbUpgradeProgress[3] = (CircularProgressBar) rlItemsView2.findViewById(R.id.upgradeProgress2);

        //set on click listeners to all of the buttons
        setClickListeners();
    }

   private void setClickListeners(){

       bBuy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(powerUp[selectedPowerUpIndex].getCurrentLevel()<5) {
                   powerUp[selectedPowerUpIndex].setCurrentLevel(powerUp[selectedPowerUpIndex].getCurrentLevel() + 1);
                   pbUpgradeProgress[selectedPowerUpIndex].setProgress(powerUp[selectedPowerUpIndex].getCurrentLevel()*20);
               }

           }
       });

       bSelect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {



           }
       });

       ibTrade.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {



           }
       });


       ivItemImage[0].setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

               tvDescription.setText(powerUp[0].getDescription());
               tvPrice.setText(Integer.toString(powerUp[0].getBaseCost()*powerUp[0].getCurrentLevel()));
               ivSelectedItemImage.setBackground(getResources().getDrawable(powerUp[0].getImageId(),null));
                selectedPowerUpIndex =0;
           }
       });


       ivItemImage[1].setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

               tvDescription.setText(powerUp[1].getDescription());
               tvPrice.setText(Integer.toString(powerUp[1].getBaseCost()*powerUp[1].getCurrentLevel()));
               ivSelectedItemImage.setBackground(getResources().getDrawable(powerUp[1].getImageId(),null));
               selectedPowerUpIndex =1;
           }
       });

       ivItemImage[2].setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

               tvDescription.setText(powerUp[2].getDescription());
               tvPrice.setText(Integer.toString(powerUp[2].getBaseCost()*powerUp[2].getCurrentLevel()));
               ivSelectedItemImage.setBackground(getResources().getDrawable(powerUp[2].getImageId(),null));
               selectedPowerUpIndex =2;
           }
       });

       ivItemImage[3].setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

               tvDescription.setText(powerUp[3].getDescription());
               tvPrice.setText(Integer.toString(powerUp[3].getBaseCost()*powerUp[3].getCurrentLevel()));
               ivSelectedItemImage.setBackground(getResources().getDrawable(powerUp[3].getImageId(),null));
               selectedPowerUpIndex =3;
           }
       });

    }

    /**
     * Method used for setting up all 4 power ups with right images, costs and levels.
     * Sets power up 1 as the default selected item with its description, before/after and cost shown
     */
    private void setUpPowerUps(){
        //set all 4 power ups with their attributes
        for(i=0; i<4;i++){
            ivItemImage[i].setBackground(getResources().getDrawable(powerUp[i].getImageId(),null));
            tvPriceItem[i].setText(Integer.toString(i*10));
            pbUpgradeProgress[i].setProgress(powerUp[i].getCurrentLevel()*20);
        }
        tvDescription.setText(powerUp[0].getDescription());
        tvPrice.setText(Integer.toString(powerUp[0].getBaseCost()));

    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){


           /* case R.id.bFreeze:
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

                break;*/


        }
    }
}
