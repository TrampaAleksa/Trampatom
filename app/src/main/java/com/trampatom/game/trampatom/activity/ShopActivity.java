package com.trampatom.game.trampatom.activity;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trampatom.game.trampatom.Model.PowerUpPool;
import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.currency.AtomPool;
import com.trampatom.game.trampatom.currency.ShopHandler;
import com.trampatom.game.trampatom.utils.Keys;

import java.util.List;
import java.util.Locale;

/**
 * Important activity that contains the shop. The shop is used for purchasing various power ups.
 * <p>Shop currency is based on Atoms and their types.</p>
 * <p>Blue atoms are used for transforming into different atoms, since they are the most often occurring atom.</p>
 * <p>Other atoms are used to get some power-ups. </p>
 * <p>Active power-ups: Red atom -> energy related ; Green atom -> Ball related</p>
 * <p>Passive power-ups: Yellow atom -> energy related ; Purple atom -> Ball related</p>
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    int selectedPowerUpIndex=0, selectedCategory=0, category;
    String categoryKey;
    int[] atomArray; int atomBlue;
    int priceImageID, resetColor, lightColor;
    int i,j;

    TextView tvDescription, tvSelectedPowerUpTitle, tvPrice;
    TextView tvNumberAtomsBlue, tvNumberAtomsRed, tvNumberAtomsGreen, tvNumberAtomsYellow, tvNumberAtomsPurple;
    ImageView ivPriceImage, ivSelectedImage;
    ImageButton ibTrade;
    ImageView[] ivLevel = {null, null, null, null, null};
    ImageView[] ivPowerUps;
    Button bBuy, bSelect;
    FrameLayout frameSelectedPowerUp;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;
    PowerUpPool[] powerUp;
    List<PowerUpPool> powerUpPool;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.shop);

        init();
    }

    private void init() {

        //VIEWS

        //currency
        tvNumberAtomsRed = (TextView) findViewById(R.id.tvAtomNumberRed);
        tvNumberAtomsGreen = (TextView) findViewById(R.id.tvAtomNumberGreen);
        tvNumberAtomsYellow = (TextView) findViewById(R.id.tvAtomNumberYellow);
        tvNumberAtomsPurple = (TextView) findViewById(R.id.tvAtomNumberPurple);
        ibTrade = (ImageButton) findViewById(R.id.ibTrade);
        ibTrade.setOnClickListener(this);

        //categories
        initCategories();

        //selected
        frameSelectedPowerUp = (FrameLayout) findViewById(R.id.frameSelectedPowerUp);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvSelectedPowerUpTitle = (TextView) findViewById(R.id.tvPowerUpTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivPriceImage = (ImageView) findViewById(R.id.ivPriceImage);
        ivSelectedImage = (ImageView) findViewById(R.id.ivSelectedPowerUp);
        ivLevel[0]= (ImageView) findViewById(R.id.ivLevel1);
        ivLevel[1]= (ImageView) findViewById(R.id.ivLevel2);
        ivLevel[2]= (ImageView) findViewById(R.id.ivLevel3);
        ivLevel[3]= (ImageView) findViewById(R.id.ivLevel4);
        ivLevel[4]= (ImageView) findViewById(R.id.ivLevel5);
        bBuy = (Button) findViewById(R.id.bBuy);
        bBuy.setOnClickListener(this);


        //CLASSES
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
        //get every single power up in an array of power ups
        powerUp = new PowerUpPool[16];
        for(i=0; i<16; i++){
                powerUp[i] = powerUpPool.get(i);
        }

        //FUNCTIONALITY

        //set the initial selected power up
        tvDescription.setText(powerUp[0].getDescription());
        tvPrice.setText(Integer.toString(powerUp[0].getBaseCost()*powerUp[0].getCurrentLevel()));
        for(i=0;i<powerUp[0].getCurrentLevel();i++){
            ivLevel[i].setBackgroundColor(getResources().getColor(R.color.categoryRedLight));
        }
        ivSelectedImage.setBackground(getResources().getDrawable(powerUp[0].getImageId(),null));

        //get the number of atoms we have and the number of blue atoms for shopping purposes
        atomArray= atomPool.getAtoms();
        atomBlue = atomPool.getSingleAtomValue(-1);

    }

    private void initCategories() {
        ivPowerUps = new ImageView[16];
        RelativeLayout[] rlCategoryHolder = {null,null,null,null}; //contains 4  included categories with power ups
        rlCategoryHolder[0] =(RelativeLayout) findViewById(R.id.categoryRed);
        rlCategoryHolder[1] =(RelativeLayout) findViewById(R.id.categoryGreen);
        rlCategoryHolder[2] =(RelativeLayout) findViewById(R.id.categoryYellow);
        rlCategoryHolder[3] =(RelativeLayout) findViewById(R.id.categoryPurple);
        ivPowerUps[0] = (ImageView) rlCategoryHolder[0].findViewById(R.id.ivPowerUp1);
        ivPowerUps[1] = (ImageView) rlCategoryHolder[0].findViewById(R.id.ivPowerUp2);
        ivPowerUps[2] = (ImageView) rlCategoryHolder[0].findViewById(R.id.ivPowerUp3);
        ivPowerUps[3] = (ImageView) rlCategoryHolder[0].findViewById(R.id.ivPowerUp4);

        ivPowerUps[4] = (ImageView) rlCategoryHolder[1].findViewById(R.id.ivPowerUp1);
        ivPowerUps[5] = (ImageView) rlCategoryHolder[1].findViewById(R.id.ivPowerUp2);
        ivPowerUps[6] = (ImageView) rlCategoryHolder[1].findViewById(R.id.ivPowerUp3);
        ivPowerUps[7] = (ImageView) rlCategoryHolder[1].findViewById(R.id.ivPowerUp4);

        ivPowerUps[8] = (ImageView) rlCategoryHolder[2].findViewById(R.id.ivPowerUp1);
        ivPowerUps[9] = (ImageView) rlCategoryHolder[2].findViewById(R.id.ivPowerUp2);
        ivPowerUps[10] = (ImageView) rlCategoryHolder[2].findViewById(R.id.ivPowerUp3);
        ivPowerUps[11] = (ImageView) rlCategoryHolder[2].findViewById(R.id.ivPowerUp4);

        ivPowerUps[12] = (ImageView) rlCategoryHolder[3].findViewById(R.id.ivPowerUp1);
        ivPowerUps[13] = (ImageView) rlCategoryHolder[3].findViewById(R.id.ivPowerUp2);
        ivPowerUps[14] = (ImageView) rlCategoryHolder[3].findViewById(R.id.ivPowerUp3);
        ivPowerUps[15] = (ImageView) rlCategoryHolder[3].findViewById(R.id.ivPowerUp4);

        //set on click listeners for every power up image so that the user can click on them to select power ups
        for(i=0; i<15; i++){
            ivPowerUps[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        //check every image view and if we clicked on one, set the selected power up
        for(i=0; i<16;i++) {
            if (v == ivPowerUps[i]) {

                selectedPowerUpIndex = i;
                categorySwitch(1,selectedPowerUpIndex);
                ivSelectedImage.setBackground(getResources().getDrawable(powerUp[i].getImageId(),null));
                ivPriceImage.setBackground(getResources().getDrawable(priceImageID,null));
                frameSelectedPowerUp.setBackgroundColor(getResources().getColor(resetColor));
                tvDescription.setText(powerUp[i].getDescription());
                tvPrice.setText(Integer.toString(powerUp[i].getBaseCost()*powerUp[i].getCurrentLevel()));
                //set all the colors to their "unleveled" values then set the correct level
                for(j=0; j<5; j++){
                    ivLevel[j].setBackgroundColor(getResources().getColor(resetColor));
                }
                for(j=0;j<powerUp[i].getCurrentLevel();j++){
                    ivLevel[j].setBackgroundColor(getResources().getColor(lightColor));
                }
                break;

            }
        }

        switch(v.getId()){

            case R.id.bBuy:

                categorySwitch(0,selectedPowerUpIndex);

                //if we have enough atoms and are not at max level, buy the power up
                if(powerUp[selectedPowerUpIndex].getCurrentLevel()<5
                        && (powerUp[selectedPowerUpIndex].getBaseCost()*powerUp[selectedPowerUpIndex].getCurrentLevel()+1)
                        <atomArray[selectedCategory]) {
                    //If we havent maxed the level and if we have enough atoms, buy the power up
                    atomArray[selectedCategory]--;
                    atomPool.setSingleAtomNumber(categoryKey, atomArray[selectedCategory]);
                    shopHandler.changeSingleAtomNumberDisplay(category, atomArray[selectedCategory]);
                    powerUp[selectedPowerUpIndex].setCurrentLevel(powerUp[selectedPowerUpIndex].getCurrentLevel() + 1);
                    ivLevel[powerUp[selectedPowerUpIndex].getCurrentLevel()-1].setBackgroundColor(getResources().getColor(lightColor));
                    tvPrice.setText(String.format(Locale.getDefault(), "%d",
                            powerUp[selectedPowerUpIndex].getBaseCost()*powerUp[selectedPowerUpIndex].getCurrentLevel()));
                }
                break;
            case R.id.ibTrade:
                //currently we dont have a select button so we are using the trade button for that
                shopHandler.saveSelectedPowerUps(powerUp[selectedPowerUpIndex]);

                break;
        }
    }

    /**
     * Depending on what flag we use it will change the values of certain variables
     * @param flag if 0 -> sets the selected category, the key for accessing the amount of atoms we have in
     *             a category, and another selected category variable
     *             <p>
     *             if 1 -> sets the drawable id of the corresponding price image, and the default/ light color for
     *             the levels of the selected power up
     *             </p>
     */
    private void categorySwitch(int flag, int selectedPowerUpIndex){

        switch(flag){

            case 0:
// we need to determine what atoms to remove from the pool based on the selected power up's category
                if(selectedPowerUpIndex < 4) {
                    selectedCategory = 0;
                    category = Keys.CATEGORY_RED;
                    categoryKey = Keys.KEY_RED_CURRENCY;
                }
                else if (selectedPowerUpIndex < 8 && selectedPowerUpIndex >= 4) {
                    selectedCategory = 1;
                    category = Keys.CATEGORY_GREEN;
                    categoryKey = Keys.KEY_GREEN_CURRENCY;
                }
                else if (selectedPowerUpIndex < 12 && selectedPowerUpIndex >= 8) {
                    selectedCategory = 2;
                    category = Keys.CATEGORY_YELLOW;
                    categoryKey = Keys.KEY_YELLOW_CURRENCY;
                }
                else if (selectedPowerUpIndex <16 && selectedPowerUpIndex >= 12) {
                    selectedCategory = 3;
                    category = Keys.CATEGORY_PURPLE;
                    categoryKey = Keys.KEY_PURPLE_CURRENCY;
                }
                break;

            case 1:
                //depending on what power up we selected changes the image of the currency next to the price
                if(selectedPowerUpIndex < 4) {
                    priceImageID = R.drawable.atomcrvena;
                    resetColor = R.color.categoryRed;
                    lightColor = R.color.categoryRedLight;
                }
                else if (selectedPowerUpIndex < 8 && selectedPowerUpIndex >= 4) {
                    priceImageID = R.drawable.atomzelena;
                    resetColor = R.color.categoryGreen;
                    lightColor = R.color.categoryGreenLight;
                }
                else if (selectedPowerUpIndex < 12 && selectedPowerUpIndex >= 8) {
                    priceImageID = R.drawable.atomzuta;
                    resetColor = R.color.categoryYellow;
                    lightColor = R.color.categoryYellowLight;
                }
                else if (selectedPowerUpIndex <16 && selectedPowerUpIndex >= 12) {
                    priceImageID = R.drawable.atomroze;
                    resetColor = R.color.categoryPurple;
                    lightColor = R.color.categoryPurpleLight;
                }
                break;


        }

    }


}
