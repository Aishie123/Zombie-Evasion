package mcm.edu.ph.group6_decisionbasedgame.View;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    ImageButton btnStart, btnSettings, btnCredits;
    Intent goToGame, goToSettings, goToCredits;
    MusicPlayerService musicPlayerService;

    String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar
        setContentView(R.layout.activity_home_screen);

        btnStart = findViewById(R.id.btnStart);
        btnSettings = findViewById(R.id.btnSettings);
        btnCredits = findViewById(R.id.btnCredits);

        btnStart.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnCredits.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        press();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnStart:

                goToGame = new Intent(HomeScreen.this, IntroScreen.class);
                startActivity(goToGame);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.btnSettings:

                goToSettings = new Intent(HomeScreen.this, SettingsScreen.class);
                startActivity(goToSettings);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.btnCredits:

                goToCredits = new Intent(HomeScreen.this, CreditsScreen.class);
                startActivity(goToCredits);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

        }
    }

    //changing button shades when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnStart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnStart.setImageResource(R.drawable.btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnStart.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnSettings.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSettings.setImageResource(R.drawable.btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSettings.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnCredits.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCredits.setImageResource(R.drawable.btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCredits.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        if(musicPlayerService!=null){
            musicPlayerService.pauseMusic();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(musicPlayerService!=null){
            if(musicPlayerService.currentTrack == 1){
                musicPlayerService.unpauseMusic();
            }else{
                musicPlayerService.playMusic(1);
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) iBinder;
        if(binder != null) {
            musicPlayerService = binder.getService();
            musicPlayerService.playMusic(1);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}