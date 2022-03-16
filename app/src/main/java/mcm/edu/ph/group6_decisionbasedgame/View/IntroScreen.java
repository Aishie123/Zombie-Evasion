package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.Model.GameData;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class IntroScreen extends AppCompatActivity implements ServiceConnection {

    private EditText userInput;
    private ImageButton btnNext;
    private String userName;
    private final String TAG = "IntroScreen";
    MusicPlayerService musicPlayerService;

    GameData game = new GameData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_intro_screen);

        userInput = findViewById(R.id.userInput);
        btnNext = findViewById(R.id.btnNext);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        press();
        userInput();

    }

    public void userInput() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.btnNext) {
                    userName = userInput.getText().toString();
                    game = new GameData(userName);
                    Log.d(TAG, "The user's name is " + userName + ".");
                    Intent i = new Intent(getApplicationContext(), Page1.class);
                    i.putExtra("user", userName);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

            }
        });
    }

    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnNext.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnNext.setImageResource(R.drawable.btn_pressed);
                    Log.d(TAG, "btnNext pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnNext.setImageResource(R.drawable.btn_unpressed);
                    Log.d(TAG, "btnNext unpressed");
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