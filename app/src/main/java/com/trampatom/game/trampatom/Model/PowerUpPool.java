package com.trampatom.game.trampatom.Model;

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

private String description;
private int id , baseCost, currentLevel, imageId, category, before;


    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    int after;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
