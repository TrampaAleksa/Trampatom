package com.trampatom.game.trampatom.utils;

/**
 * Class used for setting some initial ball stats based on passives
 */

public class PassivesManager {


    public PassivesManager(){



    }


    /**
     * Method that should be called before any other method or code using ball width and height. This changes the ball width and height
     * based on the level of teh passive power ups we selected and sets them to work with later.
     * @param ballSize original size of the ball
     * @param flag1 flag of the first power up we selected
     * @param flag2 flag of the second power up we selected
     * @param level1 level of the first passive power up
     * @param level2 level of the second passive power up
     * @return the new size value for the ball
     */
    public int setNewBallSizeFromPassives(int ballSize, int flag1, int flag2,int  level1, int level2){

        double scale1 = (ballSize/15) * level1;
        double scale2 = (ballSize/15) * level2;
        switch(flag1){
            case Keys.FLAG_PURPLE_BIGGER_BALLS:
               ballSize = ballSize +(int) scale1;
                break;
        }
        switch (flag2){
            case Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS:

                break;
        }



        return ballSize;
    }


    /**
     * Method used for checking if our selected power up is progress bar related or ball related
     * so that we can use different methods in the game for power ups.
     *
     * @return an int value used to determine what type of power up we selected
     */
    public int checkCurrentFlagType(int flag) {
        //if its an active power up/ lower than the key value of the first passive power up
        //active
        if(flag < Keys.FLAG_PURPLE_BIGGER_BALLS) {
            if (flag == Keys.FLAG_RED_BIG_ENERGY_BONUS || flag == Keys.FLAG_GREEN_SMALL_ENERGY_BONUS || flag == Keys.FLAG_YELLOW_RANDOM_EVENT_CHANCE)
                //its energy related
                return 1;
                //its ball related
            else return 2;
        }

        //passive
        else {
            if (flag == Keys.FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS || flag == Keys.FLAG_PURPLE_BIGGER_BALLS)
                //its ball related
                return 3;
                // its energy related
            else return 4;
        }

    }
}
