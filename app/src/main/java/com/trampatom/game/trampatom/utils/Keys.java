package com.trampatom.game.trampatom.utils;


/**
 * Important class used for storing every value that will be used during application runtime.
 * We access this class to get constant values and to chance some of the values during runtime.
 */
public class Keys {

// FIRST GAME

    // -------------------------------------- Game Functionality ------------------------------------ \\

        //time a game lasts in milliseconds
        public int GAME_TIME = 60000;

        public int DEFAULT_BALL_SPEED = 8;
        public static final int ATOM_DROP_INITIAL_VALUE = 1;
        //balls size correction
        public int BALL_SIZE_ADAPT = 18;

    //used for most ball's coordinates
        //ballArray indexes
        public int NEW_BALL_X_COORDINATE = 0;
        public int NEW_BALL_Y_COORDINATE = 1;
        public int NEW_BALL_MOVEX_VALUE = 2;
        public int NEW_BALL_MOVEY_VALUE = 3;

    // -------------------------------------- Power Ups ------------------------------------ \\

    public static final int NUMBER_OF_SELECTED_POWER_UPS =4;

    //used to determine energy increase with those power ups
        public static final int POWER_UPS_ENERGY_INCREASE_BASE_VALUE = 100;
        //determines how much will the balls size be increased if we use the active power up
    public int POWER_UP_BALL_SIZE_INCREASE = 30;
        //determines by how much will the energy level increase if we use the passive for it
    public int PASSIVE_STARTING_ENERGY_INCREASE = 1000;
        // how many times will the next ball be the same type if we used a power up for setting same type next few balls
    public int POWER_UP_SAME_TYPE_NEXT_BALL = 0;
        //determines the % chance of a passive power up event occurring
    public static final int MAX_CHANCE_FOR_EVENT = 100;
    public static final int TICKS_BEFORE_EVENT_TRIGGER_CHANCE = 5;
    public static final int TICKER_STARTING_VALUE = 0;

    public static final int ENERGY_CHANCE_EVENT_BONUS = 1200;

    public static final int ATOM_DROP_VALUE_INCREASE = 5;

    public boolean POWER_UP_LIMITING_SQUARE_ACTIVE = false;
    public int POWER_UP_LIMITING_SQUARE_BALL_COUNT_UNTIL_SQUARE_DISSAPEARS = 3;
    public static final int POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_WIDTH = 10;
    public static final int POWER_UP_LIMITING_SQUARE_REDUCTION_AMOUNT_HEIGHT = 10;




    // ----------------------------------------- Ball Types ------------------------------------------ \\

            // ------------------------------- Purple balls --------------------------------- \\

                //number of purple ball's
                public static int PURPLE_BALL_NUMBER = 3;
                //drawing
                public int BALL_PURPLE_NO_CLICK = 1;
                public int BALL_PURPLE_ONE_CLICK = 2;


            // ------------------------------- Yellow balls --------------------------------- \\

                public int TIMES_CLICKED_YELLOW = 0;
                public int BALL_YELLOW_REQUIRED_CLICKS = 4;
                public int BALL_YELLOW_INITIAL_SPEED = 2;
                public int BALL_YELLOW_SIZE_DECREASE = 12;
                public int BALL_YELLOW_SPEED_INCREASE = 3;


            // ------------------------------- Green balls --------------------------------- \\

               static public int GREEN_BALL_ANGLE_CHANGE_CHANCE = 350;
                public int GREEN_BALL_SPEED = 16;

            // ------------------------------- Wave balls --------------------------------- \\

                public static final int WAVE_BALL_NUMBER = 7;




//SECOND GAME
    //gold ball timer
        public long GOLD_BALL_TIMER = 20000;
        public long GOLD_BALL_DURATION = 3;
        public int RED_SPEED_UP_TIMER = 10000;
    //negative ball
        public int BALL_NEGATIVE_NUMBER = 7;
    //max time between clicks
        public int SURVIVAL_CLICK_TIMER = 5000;


//POWER-UPS

    // --------------------------------- Currency ----------------------------------------------- \\

            // -------------------------- Main currency variables ------------------------ \\

        public static final int KEY_RED_TRADE_INDEX = 0;
        public static final int KEY_GREEN_TRADE_INDEX = 1;
        public static final int KEY_YELLOW_TRADE_INDEX = 2;
        public static final int KEY_PURPLE_TRADE_INDEX = 3;

        //keys for getting and setting currency with shared preferences
        public static final String KEY_BLUE_CURRENCY = "blue currency";
        public static final String KEY_RED_CURRENCY = "red currency";
        public static final String KEY_GREEN_CURRENCY = "green currency";
        public static final String KEY_YELLOW_CURRENCY = "yellow currency";
        public static final String KEY_PURPLE_CURRENCY = "purple currency";

        //keys for getting a specific category for shop purposes
        public static final int CATEGORY_BLUE = 0;
        public static final int CATEGORY_RED = 1;
        public static final int CATEGORY_GREEN = 2;
        public static final int CATEGORY_YELLOW = 3;
        public static final int CATEGORY_PURPLE = 4;


        //used to store a preference to determine what power up we selected
        public static final String KEY_POWER_UP_ONE = "power up 1";
        public static final String KEY_POWER_UP_TWO = "power up 2";
        public static final String KEY_PASSIVE_ONE = "passive 1";
        public static final String KEY_PASSIVE_TWO = "passive 2";
        //Preference for accessing the power up pool object containing all info on our power ups
        public static final String KEY_POWER_UPS_POOL = "Power Up Pool";

        //used to determine what level of power up we are using


    // ----------------------------------- Flags -------------------------------------------- \\

    //flags are used to determine what power up we have selected inside the game
        //red
        public static final int FLAG_RED_FREEZE_BALLS = 1;
        public static final int FLAG_RED_BIG_ENERGY_BONUS = 2;
        public static final int FLAG_RED_SELECTIVE_TYPE = 3;
        public static final int FLAG_RED_GRAVITY_PULL = 4;
        //green
        public static final int FLAG_GREEN_SLOW_DOWN_BALLS = 5;
        public static final int FLAG_GREEN_SMALL_ENERGY_BONUS = 6;
        public static final int FLAG_GREEN_INCREASE_BALL_SIZE = 7;
        public static final int FLAG_GREEN_UNKNOWN2 = 8;
        //purple
        public static final int FLAG_PURPLE_BIGGER_BALLS = 9;
        public static final int FLAG_PURPLE_SLOWER_ENERGY_DECAY = 10;
        public static final int FLAG_PURPLE_UNKNOWN3 = 11;
        public static final int FLAG_PURPLE_UNKNOWN4 = 12;
        //yellow
        public static final int FLAG_YELLOW_RANDOM_EVENT_CHANCE = 13;
        public static final int FLAG_YELLOW_CHANCE_ATOM_DROP_BONUS = 14;
        public static final int FLAG_YELLOW_CHANCE_LIMITING_SQUARE = 15;
        public static final int FLAG_YELLOW_CHANCE_ENERGY_FILL = 16;

        //used to determine what the power up does
        // and do something in game based on that
        public static final int FLAG_PROGRESS_BAR_POWER_UP = 1;
        public static final int FLAG_BALL_POWER_UP = 2;

        public static final int FLAG_LIMITING_SQUARE_ACTIVE = 0;


    //-------------------- POWER UP INDEXES ------------------------\\

        public static final int POWER_UP_INDEX_ACIVE_COOLDOWN = 1;
        public static final int POWER_UP_INDEX_ACIVE_CONSUMABLE = 0;
        public static final int POWER_UP_INDEX_PASSIVE_EFFECT = 3;
        public static final int POWER_UP_INDEX_PASSIVE_CHANCE = 2;



}
