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

import com.trampatom.game.trampatom.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int width;
    public static int height;
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
            Button start = (Button) findViewById(R.id.bStart);
            start.setOnClickListener(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bStart:
                Intent start = new Intent(this, Game1.class);
                startActivity(start);
                break;
        }
        }
    }
