package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mcm.edu.ph.group6_decisionbasedgame.Model.GameData;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page1 extends AppCompatActivity {

    ImageView darkShade;
    TextView txtDialogue, txtChoice1, txtChoice2, txtChoice3,txtChoice4;
    ImageButton btnChoice1, btnChoice2, btnChoice3, btnChoice4;
    String userName;
    String TAG = "Page1";

    GameData game = new GameData();

    ObjectAnimator txtFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page1);


        darkShade = findViewById(R.id.darkShade);

        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        btnChoice4 = findViewById(R.id.btnChoice4);

        txtDialogue = findViewById(R.id.txtDialogue);
        txtChoice1 = findViewById(R.id.txtChoice1);
        txtChoice2 = findViewById(R.id.txtChoice2);
        txtChoice3 = findViewById(R.id.txtChoice3);
        txtChoice4 = findViewById(R.id.txtChoice4);

        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        Log.d(TAG, "The user's name is " + userName + ".");

        game = new GameData(userName);

        // choices to be shown later on
        btnChoice1.setVisibility(View.GONE);
        btnChoice2.setVisibility(View.GONE);
        btnChoice3.setVisibility(View.GONE);
        btnChoice4.setVisibility(View.GONE);
        txtChoice1.setVisibility(View.GONE);
        txtChoice2.setVisibility(View.GONE);
        txtChoice3.setVisibility(View.GONE);
        txtChoice4.setVisibility(View.GONE);

        opening();
    }

// BEDROOM SCENE - STARTING PAGE

    // starting dialogue
    @SuppressLint("SetTextI18n")
    public void opening(){

        txtFadeIn = ObjectAnimator.ofFloat(txtDialogue,"alpha",0f, 1f);
        txtFadeIn.setDuration(2000); // fades in for 2 seconds

        txtFadeIn.start(); // dialogue fades in

        txtDialogue.setText("You wake up at your bed... ");

        final Handler handler = new Handler(Looper.getMainLooper()); // for delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtFadeIn.start(); // dialogue fades in
                txtDialogue.setText("You hear from your room's radio that the global pandemic made humans turn into ZOMBIES.");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtFadeIn.start(); // dialogue fades in
                        txtDialogue.setText("What will you do? \n"+
                                "\n 1. Call somebody for help." +
                                "\n 2. Go to the kitchen." +
                                "\n 3. Go back to sleep." +
                                "\n 4. Go downstairs and look for people around.");

                        btnChoice1.setVisibility(View.VISIBLE);
                        btnChoice2.setVisibility(View.VISIBLE);
                        btnChoice3.setVisibility(View.VISIBLE);
                        btnChoice4.setVisibility(View.VISIBLE);
                        txtChoice1.setVisibility(View.VISIBLE);
                        txtChoice2.setVisibility(View.VISIBLE);
                        txtChoice3.setVisibility(View.VISIBLE);
                        txtChoice4.setVisibility(View.VISIBLE);
                    }
                }, 7000); // 7 seconds delay
            }
        }, 4000); // 4 seconds delay


    }


    // actions after player makes a decision -------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){

            // 1. Call somebody for help
            case R.id.btnChoice1:
                Intent page3 = new Intent(getApplicationContext(), Page3.class);
                startActivity(page3); // moves to page 3 activity
                break;

            // 2. Go to the kitchen
            case R.id.btnChoice2:
                Intent page2 = new Intent(getApplicationContext(), Page2.class);
                startActivity(page2); // moves to page 2 activity
                break;

            // 3. Go back to sleep
            case R.id.btnChoice3:

                break;

            // 4. Go downstairs and look for people around.
            case R.id.btnChoice4:
                Intent page6 = new Intent(getApplicationContext(), Page6.class);
                startActivity(page6); // moves to page 6 activity
                break;

        }
        String "nikol"

    }

}




