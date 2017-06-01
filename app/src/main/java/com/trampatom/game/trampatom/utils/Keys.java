package com.trampatom.game.trampatom.utils;




public class Keys {
    //used for game functionality
        //time a game lasts in milliseconds
        public int GAME_TIME = 60000;
        public int BALL_SPEED = 8;
        //balls size correction
        public int BALL_SIZE_ADAPT = 18;

    //used for most ball's coordinates
        //ballArray indexes
        public int NEW_BALL_X_COORDINATE = 0;
        public int NEW_BALL_Y_COORDINATE = 1;
        public int NEW_BALL_MOVEX_VALUE = 2;
        public int NEW_BALL_MOVEY_VALUE = 3;

    //used for managing purple balls
        //angles
        public int PURPLE_BALL_ANGLE_ONE = 0;
        public int PURPLE_BALL_ANGLE_TWO = 1;
        public int PURPLE_BALL_ANGLE_THREE = 2;
        //coordinates
        public int PURPLE_BALL_XY1 = 0;
        public int PURPLE_BALL_XY2 = 1;
        public int PURPLE_BALL_XY3 = 2;
        //number of purple ball's
        public int PURPLE_BALL_NUMBER = 3;
        //movement
        public int PURPLE_BALL_MOVE_ONE = 0;
        public int PURPLE_BALL_MOVE_TWO = 1;
        public int PURPLE_BALL_MOVE_THREE = 2;
        //drawing
        public int BALL_PURPLE_NO_CLICK = 1;
        public int BALL_PURPLE_ONE_CLICK = 2;
    //used for managing yellow balls
        public int YELLOW_BALL_INITIAL_SIZE = 25;
        public int BALL_YELLOW_REQUIRED_CLICKS = 4;
        public int BALL_YELLOW_INITIAL_SPEED = 2;
        public int BALL_YELLOW_SIZE_DECREASE = 10;
        public int BALL_YELLOW_SPEED_INCREASE = 3;
    //used for managing green balls
        public int GREEN_BALL_ANGLE_CHANGE_CHANCE = 350;
        public int GREEN_BALL_SPEED = 16;
    //used for determining what ball will be drawn
    public int TYPE_BALL_RED_CHANCE = 2;
    public int TYPE_BALL_BLUE_CHANCE = 10;
    public int TYPE_BALL_YELLOW_CHANCE = 13;
    public int TYPE_BALL_GREEN_CHANCE = 15;
    public int TYPE_BALL_PURPLE_CHANCE = 18;


//SECOND GAME
    //gold ball timer
        public long GOLD_BALL_TIMER = 20000;
        public long GOLD_BALL_DURATION = 3;
        public int RED_SPEED_UP_TIMER = 10000;
    //negative ball
        public int BALL_NEGATIVE_NUMBER = 7;

}
