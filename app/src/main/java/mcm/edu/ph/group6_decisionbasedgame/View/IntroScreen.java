package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MediaPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.Model.GameData;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class IntroScreen extends AppCompatActivity {

    private MediaPlayerService player;
    private EditText userInput;
    private ImageButton btnNext;
    private String userName;
    private String TAG = "IntroScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        Intent svc=new Intent(this, MediaPlayerService.class);
        startService(svc);

        setContentView(R.layout.activity_intro_screen);

        userInput = findViewById(R.id.userInput);
        btnNext = findViewById(R.id.btnNext);


        press();
        userInput();

    }

    public void userInput() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.btnNext) {

                    userName = userInput.getText().toString();
                    Log.d(TAG, "The user's name is " + userName + ".");

                    Intent i = new Intent(getApplicationContext(), Page1.class);
                    i.putExtra("user", userName);
                    startActivity(i);
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

}