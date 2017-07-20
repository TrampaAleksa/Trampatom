package com.trampatom.game.trampatom.Model;

import android.widget.ImageView;

/**
 * Class containing the attributes of a single power up:
 <p>// DESCRIPTION : should say what the power up will do and what changes with its level up.</p>
 <p>// ID : used to determine what power up this is, for getting the right cost/ description etc. when this one is selected.</p>
 <p> // BASE COST : every power up has its base cost for level 1. Based on this every next level scales.</p>
 <p> // CURRENT LEVEL : every power up has levels. This will be used to indicate what is its current level or if its at max.</p>
 <p> // IMAGE : the icon used to distinguish this power up from the others to help the user visually.</p>
 <p> // CATEGORY : there are four categories , this will be used to sort the right power ups into the right category.</p>
 */

public class PowerUpPool {


    // DESCRIPTION : should say what the power up will do and what changes with its level up.
    // ID : used to determine what power up this is, for getting the right cost/ description etc. when this one is selected.
    // BASE COST : every power up has its base cost for level 1. Based on this every next level scales.
    // CURRENT LEVEL : every power up has levels. This will be used to indicate what is its current level or if its at max.
    // IMAGE : the icon used to distinguish this power up from the others to help the user visually.
    // CATEGORY : there are four categories , this will be used to sort the right power ups into the right category.

String descriptions;
int id;
int baseCost;
int currentLevel;
ImageView image;
int category;


    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
