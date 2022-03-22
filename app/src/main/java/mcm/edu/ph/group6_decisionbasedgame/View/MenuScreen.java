package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class MenuScreen extends AppCompatActivity implements ServiceConnection {

    private ImageButton btnContinue, btnSettings, btnRestart, btnExit;
    private boolean noRestart;

    private MusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar
        setContentView(R.layout.activity_menu_screen);

        btnContinue = findViewById(R.id.btnMenuContinue);
        btnSettings = findViewById(R.id.btnMenuSettings);
        btnRestart = findViewById(R.id.btnMenuRestart);
        btnExit = findViewById(R.id.btnMenuExit);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        noRestart = getIntent().getExtras().getBoolean("no restart");

        if (noRestart){
            btnRestart.setAlpha(0.5f);
        }

        press();

    }

    // onClick ---------------------------------------------------------------------------------------------------

    public void menuToContinue(View v){
        finish();
    }

    // goes to game info (how to play)
    public void menuToSettings(View v){
        Intent goToSettings = new Intent(MenuScreen.this, SettingsScreen.class);
        startActivity(goToSettings);
    }

    // goes to settings
    public void menuToRestart(View v){

        if (noRestart){
            Toast.makeText(getApplicationContext(),"Can't restart before the game has started.",Toast.LENGTH_LONG).show();
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        new AlertDialog.Builder(MenuScreen.this)
                                .setTitle("Restart Game")
                                .setMessage("Restart game now? You will lose ALL progress.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent restart = new Intent(MenuScreen.this, IntroScreen.class);
                                        startActivity(restart);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                    }
                }
            });
        }
    }

    // exits game
    public void menuToExit(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()){
                    new AlertDialog.Builder(MenuScreen.this)
                            .setTitle("Exit Game")
                            .setMessage("Exit game now? You will lose ALL progress.")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Intent goToStart = new Intent(MenuScreen.this, SplashScreen.class);
                                    startActivity(goToStart);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                }
            }
        });
    }

    //changing button shades when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnContinue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnContinue.setImageResource(R.drawable.btn_p);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnContinue.setImageResource(R.drawable.btn_up);
                }
                return false;
            }
        });

        btnSettings.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSettings.setImageResource(R.drawable.btn_p);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSettings.setImageResource(R.drawable.btn_up);
                }
                return false;
            }
        });

        btnRestart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnRestart.setImageResource(R.drawable.btn_p);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnRestart.setImageResource(R.drawable.btn_up);
                }
                return false;
            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnExit.setImageResource(R.drawable.btn_p);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnExit.setImageResource(R.drawable.btn_up);
                }
                return false;
            }
        });

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

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
            musicPlayerService.unpauseMusic();
        }
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) iBinder;
        if(binder != null) {
            musicPlayerService = binder.getService();
            musicPlayerService.unpauseMusic();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}
