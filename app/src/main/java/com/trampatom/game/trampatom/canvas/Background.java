package com.trampatom.game.trampatom.canvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.trampatom.game.trampatom.Model.Star;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that should be used to animate a background for every game using methods for
 * drawing and moving sprites
 */
public class Background {
    public static final int MAX_STAR_RADIUS = 5;
    public static final int NUMBER_OF_STARS = 100;

    SurfaceHolder ourHolder;
    Canvas ourCanvas;
    Paint starPaint;
    Random random;
    int canvasWidth, canvasHeight;
    int i;
    int gottenStars;
    Star currentStar;
    List<Star> stars;

    /**
     * Constructor that should be used to insert a holder and a canvas to use for drawing, and a width and height to be
     * used for generating stars inside the screen
     * @param holder our holder object
     * @param canvas our canvas to draw on
     * @param canvasWidth width of the canvas so we don't draw off screen
     * @param canvasHeight height of the canvas so we don't draw off screen
     */
    public Background(SurfaceHolder holder, Canvas canvas, int canvasWidth, int canvasHeight) {
        //get a canvas and a holder object to use for drawing
        this.ourCanvas = canvas;
        this.ourHolder = holder;
        starPaint = new Paint();
        starPaint.setColor(Color.WHITE);
        random = new Random();
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        currentStar = new Star();
        stars = new ArrayList<>();
        stars = getBackgroundStars();
    }

    /**
     * Method used for getting a new star object that should be used for displaying stars on background
     * Star has random x, y coordinates and a random radius
     */
   private Star getNewStar(){

       Star star= new Star();
       //coordinates for the star and its radius
       int x,y, radius;
       x= random.nextInt(canvasWidth-MAX_STAR_RADIUS);
       y = random.nextInt(canvasHeight-MAX_STAR_RADIUS);
       radius = random.nextInt(MAX_STAR_RADIUS);
        // fill the star object with the coordinates and its radius
       star.setX(x);
       star.setY(y);
       star.setRadius(radius);

       return star;
   }

    /**
     * Method used for getting a list array of stars. Every star has a x,y coordinate and a radius
     * @return a list of star objects
     */
   public List<Star> getBackgroundStars(){
       //get a list of arrays and fill it with star objects
       List<Star> stars = new ArrayList<>();
       for(i=0; i<NUMBER_OF_STARS; i++) {
           stars.add(getNewStar());
       }
       gottenStars = 0;
       return stars;
   }

}
