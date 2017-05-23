package com.trampatom.game.trampatom.activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trampatom.game.trampatom.R;
import com.trampatom.game.trampatom.utils.HighScore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MAX_NUMBER_OF_GAMES = 3;
    private static final int MIN_NUMBER_OF_GAMES = 1;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
//screen width and height
    public static int width;
    public static int height;
    int lastHighScore;
    //used for setting high score and seeing what game is selected
    int selectedGame;
    //used for muting/ unmuting music
    boolean soundOn;
    //used for starting music service
    Intent svc;
    TextView tvHighScore, tvSelectedGame;
    HighScore highScore;
    ImageButton sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For setting fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        init();

    }

    private void init(){
     highScore = new HighScore(this);
        //Views
            Button start = (Button) findViewById(R.id.bStart);
            start.setOnClickListener(this);
            ImageButton next = (ImageButton) findViewById(R.id.bNext);
            next.setOnClickListener(this);
            ImageButton previous = (ImageButton) findViewById(R.id.bPrev);
            previous.setOnClickListener(this);
            sound = (ImageButton) findViewById(R.id.bSound);
            sound.setOnClickListener(this);
            tvHighScore = (TextView) findViewById(R.id.tvHighScore1);
            tvSelectedGame = (TextView) findViewById(R.id.tvGame);
        //Variables
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
            selectedGame=1;
            soundOn=true;
        //music
           // svc=new Intent(this, MusicService.class);
           // startService(svc);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bStart:
                launchGame();
                break;
            case R.id.bNext:
                //select the next game
                if(selectedGame<MAX_NUMBER_OF_GAMES)
                selectedGame++;
                //sets the selected game and shows the high score for THAT game
                //TODO selectedGame and highScore text view method
                tvSelectedGame.setText(Integer.toString(selectedGame));
                highScore.textHighScore(tvHighScore, selectedGame);
                break;
            //TODO animation on button click change?
            case R.id.bPrev:
                //select the previous game
                if(selectedGame>MIN_NUMBER_OF_GAMES)
                    selectedGame--;
                //sets the selected game and shows the high score for THAT game
                tvSelectedGame.setText(Integer.toString(selectedGame));
                highScore.textHighScore(tvHighScore, selectedGame);
                break;
            case R.id.bSound:
                if(soundOn){
                    //TODO api <21 for drawable
                    //TODO fix drawable to not move on drawable change
                    //Mute and set sound button to "muted"
                    stopService(svc);
                    sound.setBackground(getDrawable(R.drawable.mutebuttonmuted));
                    soundOn=false;
                }
                else{
                    //Unmute and set sound button to "unmuted"
                    sound.setBackground(getDrawable(R.drawable.mutebuttonunmuted));
                    soundOn=true;
                    startService(svc);
                }
                break;
        }
    }

    /**
     * Method used for launching the selected game
     */
    private void launchGame(){
        if(selectedGame==1) {
            Intent start = new Intent(this, Game1.class);
            startActivity(start);
        }
        if(selectedGame==2){
            Intent start = new Intent(this, Game2.class);
            startActivity(start);
        }
        if(selectedGame==3){
            Intent start = new Intent(this, Game3.class);
            startActivity(start);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show the high score every time you return from a played game
        highScore.textHighScore(tvHighScore, selectedGame);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop the music after exitning the game
        stopService(svc);
    }
}
