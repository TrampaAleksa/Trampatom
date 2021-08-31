package com.trampatom.game.trampatom.activity;

import android.media.SoundPool;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.trampatom.game.trampatom.utils.SoundsAndEffects;

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
    //private ImageView ivNumberAtomsRed, ivNumberAtomsGreen, ivNumberAtomsPurple, ivNumberAtomsYellow;
    private ImageView[] topAtomImages ;
    ImageView ivPriceImage, ivSelectedImage;
    ImageButton ibTrade; CheckBox ibSelectedStatus;
    ImageView[] ivLevel = {null, null, null, null, null};
    ImageView[] ivPowerUps;
    Button bBuy, bSelect;
    FrameLayout frameSelectedPowerUp;

    //classes used to run the shop
    AtomPool atomPool;
    ShopHandler shopHandler;
    SoundsAndEffects soundsAndEffects;
    PowerUpPool[] powerUp;
    List<PowerUpPool> powerUpPool;
    int[] currentlySelectedPowerUpsIds = {0,0,0,0};


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
        initTradeViews();
        ibTrade = (ImageButton) findViewById(R.id.ibTrade);
        ibTrade.setOnClickListener(this);
        ibSelectedStatus = (CheckBox) findViewById(R.id.ibSelectedStatus);
        ibSelectedStatus.setOnClickListener(this);

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
        atomArray = shopHandler.setAtomPoolValues();
        //contains a list of power ups
        powerUpPool = shopHandler.loadPowerUpPool();
        //get every single power up in an array of power ups
        powerUp = new PowerUpPool[16];
        for(i=0; i<16; i++){
                powerUp[i] = powerUpPool.get(i);
        }

        //SOUNDS
        soundsAndEffects = new SoundsAndEffects(this).getShopSounds();
        //soundPool.play(soundsAndEffects.soundEnteredShopId,1,1,0,0,1);

        //FUNCTIONALITY

        //set the initial selected power up
        currentlySelectedPowerUpsIds = shopHandler.loadSelectedPowerUpsIds();
        tvDescription.setText(powerUp[0].getDescription());
        tvPrice.setText(Integer.toString(powerUp[0].getBaseCost()*powerUp[0].getCurrentLevel()));
        for(i=0;i<powerUp[0].getCurrentLevel();i++){
            ivLevel[i].setBackgroundColor(getResources().getColor(R.color.categoryRedLight));
        }
        ivSelectedImage.setBackground(getResources().getDrawable(powerUp[0].getImageId(),null));

        if(currentlySelectedPowerUpsIds[selectedCategory]== powerUp[0].getId()){
            ibSelectedStatus.setChecked(true);
        }

        //get the number of atoms we have and the number of blue atoms for shopping purposes
       //atomArray= atomPool.getAtoms();
        atomBlue = atomArray[Keys.CATEGORY_BLUE];


    }
    private void initTradeViews(){
        topAtomImages =new ImageView[4];
        tvNumberAtomsRed = (TextView) findViewById(R.id.tvAtomNumberRed);
        tvNumberAtomsGreen = (TextView) findViewById(R.id.tvAtomNumberGreen);
        tvNumberAtomsYellow = (TextView) findViewById(R.id.tvAtomNumberYellow);
        tvNumberAtomsPurple = (TextView) findViewById(R.id.tvAtomNumberPurple);
        tvNumberAtomsRed.setOnClickListener(this);
        tvNumberAtomsGreen.setOnClickListener(this);
        tvNumberAtomsYellow.setOnClickListener(this);
        tvNumberAtomsPurple.setOnClickListener(this);
        topAtomImages[Keys.KEY_RED_TRADE_INDEX]    = (ImageView) findViewById(R.id.ivAtomRed);
        topAtomImages[Keys.KEY_GREEN_TRADE_INDEX]  = (ImageView) findViewById(R.id.ivAtomGreen);
        topAtomImages[Keys.KEY_PURPLE_TRADE_INDEX] = (ImageView) findViewById(R.id.ivAtomPurple);
        topAtomImages[Keys.KEY_YELLOW_TRADE_INDEX] = (ImageView) findViewById(R.id.ivAtomYellow);
        topAtomImages[Keys.KEY_RED_TRADE_INDEX].setOnClickListener(this);
        topAtomImages[Keys.KEY_GREEN_TRADE_INDEX].setOnClickListener(this);
        topAtomImages[Keys.KEY_PURPLE_TRADE_INDEX] .setOnClickListener(this);
        topAtomImages[Keys.KEY_YELLOW_TRADE_INDEX].setOnClickListener(this);

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
        for(i=0; i<16; i++){
            ivPowerUps[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        //check every image view and if we clicked on one, set the selected power up
        for(i=0; i<16;i++) {
            if (v == ivPowerUps[i]) {
                atomPool.updateAtoms(atomArray);
                //soundPool.play(soundsAndEffects.soundSelectedPowerUp,1,1,0,0,1);

                selectedPowerUpIndex = i;
                categorySwitch(1,selectedPowerUpIndex);

                if(currentlySelectedPowerUpsIds[selectedCategory]== powerUp[selectedPowerUpIndex].getId()){
                    ibSelectedStatus.setChecked(true);
                }
                else ibSelectedStatus.setChecked(false);
                int a;
                 a = currentlySelectedPowerUpsIds[selectedCategory];
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

        for(i=0;i<Keys.KEY_PURPLE_TRADE_INDEX+1;i++) {

            if (v == topAtomImages[i]) {

                atomArray = atomPool.transferBlueAtoms(i, atomArray);
                //tvNumberAtomsBlue.setText(Integer.toString(atomArray[Keys.CATEGORY_BLUE]));
                tvNumberAtomsRed.setText(Integer.toString(atomArray[Keys.CATEGORY_RED]));
                tvNumberAtomsGreen.setText(Integer.toString(atomArray[Keys.CATEGORY_GREEN]));
                tvNumberAtomsYellow.setText(Integer.toString(atomArray[Keys.CATEGORY_YELLOW]));
                tvNumberAtomsPurple.setText(Integer.toString(atomArray[Keys.CATEGORY_PURPLE]));
            }
        }

        switch(v.getId()){
            //top text view cases
            case R.id.tvAtomNumberRed:
                atomArray = atomPool.transferBlueAtoms(Keys.KEY_RED_TRADE_INDEX, atomArray);
                tvNumberAtomsRed.setText(Integer.toString(atomArray[Keys.CATEGORY_RED]));
                break;
            case R.id.tvAtomNumberGreen:
                atomArray = atomPool.transferBlueAtoms(Keys.KEY_GREEN_TRADE_INDEX, atomArray);
                tvNumberAtomsGreen.setText(Integer.toString(atomArray[Keys.CATEGORY_GREEN]));
                break;
            case R.id.tvAtomNumberPurple:
                atomArray = atomPool.transferBlueAtoms(Keys.KEY_PURPLE_TRADE_INDEX, atomArray);
                tvNumberAtomsPurple.setText(Integer.toString(atomArray[Keys.CATEGORY_PURPLE]));
                break;
            case R.id.tvAtomNumberYellow:
                atomArray = atomPool.transferBlueAtoms(Keys.KEY_YELLOW_TRADE_INDEX, atomArray);
                tvNumberAtomsYellow.setText(Integer.toString(atomArray[Keys.CATEGORY_YELLOW]));
                break;

            case R.id.bBuy:

                categorySwitch(0,selectedPowerUpIndex);

                //if we have enough atoms and are not at max level, buy the power up
                if(powerUp[selectedPowerUpIndex].getCurrentLevel()<5
                        && (powerUp[selectedPowerUpIndex].getBaseCost()*powerUp[selectedPowerUpIndex].getCurrentLevel()+1)
                        <atomArray[category]) {
                    soundsAndEffects.play(soundsAndEffects.soundBoughtShopItemId);
                    //If we havent maxed the level and if we have enough atoms, buy the power up
                    atomArray[category]-= powerUp[selectedPowerUpIndex].getCurrentLevel()*powerUp[selectedPowerUpIndex].getBaseCost();
                   // atomArray[category]--;
                    atomPool.setSingleAtomNumber(categoryKey, atomArray[category]);
                    shopHandler.changeSingleAtomNumberDisplay(category, atomArray[category]);
                    powerUp[selectedPowerUpIndex].setCurrentLevel(powerUp[selectedPowerUpIndex].getCurrentLevel() + 1);
                    ivLevel[powerUp[selectedPowerUpIndex].getCurrentLevel()-1].setBackgroundColor(getResources().getColor(lightColor));
                    tvPrice.setText(String.format(Locale.getDefault(), "%d",
                            powerUp[selectedPowerUpIndex].getBaseCost()*powerUp[selectedPowerUpIndex].getCurrentLevel()));
                    shopHandler.updatePowerUp(powerUp[selectedPowerUpIndex]);

                }
                break;
            case R.id.ibTrade:
                //soundPool.play(soundsAndEffects.soundEnteredShopId,1,1,0,0,1);


                break;
            case R.id.ibSelectedStatus:
                if(ibSelectedStatus.isChecked()) {
                    //set the id of the currently selected power up to the power up we just selected
                    ibSelectedStatus.setChecked(true);
                    currentlySelectedPowerUpsIds[selectedCategory] = powerUp[selectedPowerUpIndex].getId();
                    shopHandler.saveSelectedPowerUps(powerUp[selectedPowerUpIndex]);
                    soundsAndEffects.play(soundsAndEffects.soundSelectedPowerUp);

                }
                else ibSelectedStatus.setChecked(true);

                atomPool.updateAtoms(atomArray);
              //  ibSelectedStatus.setBackground(getResources().getDrawable(android.R.drawable.checkbox_on_background,null));
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
                    selectedCategory = 3;
                    category = Keys.CATEGORY_PURPLE;
                    categoryKey = Keys.KEY_PURPLE_CURRENCY;
                }
                else if (selectedPowerUpIndex <16 && selectedPowerUpIndex >= 12) {
                    selectedCategory = 2;
                    category = Keys.CATEGORY_YELLOW;
                    categoryKey = Keys.KEY_YELLOW_CURRENCY;

                }
                break;

            case 1:
                //depending on what power up we selected changes the image of the currency next to the price
                if(selectedPowerUpIndex < 4) {
                    selectedCategory = 0;
                    priceImageID = R.drawable.atomcrvena;
                    resetColor = R.color.categoryRed;
                    lightColor = R.color.categoryRedLight;
                }
                else if (selectedPowerUpIndex < 8 && selectedPowerUpIndex >= 4) {
                    selectedCategory = 1;
                    priceImageID = R.drawable.atomzelena;
                    resetColor = R.color.categoryGreen;
                    lightColor = R.color.categoryGreenLight;
                }
                else if (selectedPowerUpIndex < 12 && selectedPowerUpIndex >= 8) {

                    selectedCategory = 3;
                    priceImageID = R.drawable.atomroze;
                    resetColor = R.color.categoryPurple;
                    lightColor = R.color.categoryPurpleLight;
                }
                else if (selectedPowerUpIndex <16 && selectedPowerUpIndex >= 12) {
                    selectedCategory = 2;
                    priceImageID = R.drawable.atomzuta;
                    resetColor = R.color.categoryYellow;
                    lightColor = R.color.categoryYellowLight;
                }
                break;


        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundsAndEffects.releaseSoundPool();
    }

    @Override
    protected void onPause() {
        super.onPause();
        atomPool.updateAtoms(atomArray);
    }
}
