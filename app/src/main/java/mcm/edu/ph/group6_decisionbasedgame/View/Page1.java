package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MediaPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.Model.GameData;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page1 extends AppCompatActivity implements View.OnClickListener{

    ImageView darkShade1;
    TextView txt1Dialogue, txt1Choice1, txt1Choice2, txt1Choice3,txt1Choice4, txt1Restart;
    ImageButton btn1Choice1, btn1Choice2, btn1Choice3, btn1Choice4, btn1Restart;
    VideoView death1;
    MediaController mediaController;
    Handler handler;
    Intent svc, page2, page3, page6, intro;

    String userName;
    String TAG = "Page1";

    GameData game = new GameData();

    AlphaAnimation fadeIn;

    ObjectAnimator darkFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page1);


        //initializing components
        darkShade1 = findViewById(R.id.darkShade1);


        btn1Choice1 = findViewById(R.id.btn1Choice1);
        btn1Choice2 = findViewById(R.id.btn1Choice2);
        btn1Choice3 = findViewById(R.id.btn1Choice3);
        btn1Choice4 = findViewById(R.id.btn1Choice4);
        btn1Restart = findViewById(R.id.btn1Restart);
        txt1Dialogue = findViewById(R.id.txt1Dialogue);
        txt1Choice1 = findViewById(R.id.txt1Choice1);
        txt1Choice2 = findViewById(R.id.txt1Choice2);
        txt1Choice3 = findViewById(R.id.txt1Choice3);
        txt1Choice4 = findViewById(R.id.txt1Choice4);
        txt1Restart = findViewById(R.id.txt1Restart);
        death1 = findViewById(R.id.death1);


        //receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        Log.d(TAG, "The user's name is " + userName + ".");

        svc = new Intent(this, MediaPlayerService.class);
        // game = new GameData(userName); // informing the database about user's name


        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn1Choice1.setOnClickListener(this);
        btn1Choice2.setOnClickListener(this);
        btn1Choice3.setOnClickListener(this);
        btn1Choice4.setOnClickListener(this);
        btn1Restart.setOnClickListener(this);

        death1.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);
        mediaController = new MediaController(this); //link mediaController to videoView
        mediaController.setAnchorView(death1); //allow mediaController to control our videoView
        death1.setMediaController(mediaController);


        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
                                            // by turning the opacity (alpha) of the
                                            // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade1,"alpha",0.7f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        opening(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
                // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)
    }

// BEDROOM SCENE - STARTING PAGE

    // opening dialogue
    @SuppressLint("SetTextI18n")
    public void opening(){

        txt1Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt1Dialogue.setText(R.string.p1_dialogue1);

        // the code inside handler will run after the 4-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt1Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt1Dialogue.setText(R.string.p1_dialogue2);

                // the code inside handler will run after the 7-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt1Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt1Dialogue.setText(R.string.p1_decision);

                        showButtons(); //show choices
                    }
                }, 7000); // 7 seconds delay
            }
        }, 4000); // 4 seconds delay
    }


    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){

            // 1. Call somebody for help
            case R.id.btn1Choice1:
                page3 = new Intent(getApplicationContext(), Page3.class);
                finish();
                page3.putExtra("user", userName);
                startActivity(page3); // moves to page 3 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 2. Go to the kitchen
            case R.id.btn1Choice2:
                page2 = new Intent(getApplicationContext(), Page2.class);
                finish();
                page2.putExtra("user", userName);
                startActivity(page2); // moves to page 2 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 3. Go back to sleep
            case R.id.btn1Choice3:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt1Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopService(svc);

                        death1.setVisibility(View.VISIBLE);
                        death1.startAnimation(fadeIn); // video fades in
                        death1.start(); // play video

                        death1.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                txt1Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt1Dialogue.setText(R.string.p1_death);

                                death1.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. Go downstairs and look for people around.
            case R.id.btn1Choice4:
                page6 = new Intent(getApplicationContext(), Page6.class);
                finish();
                page6.putExtra("user", userName);
                startActivity(page6); // moves to page 6 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // If reset button is pressed
            case R.id.btn1Restart:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn1Restart.setVisibility(View.VISIBLE);
        txt1Restart.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn1Choice1.setVisibility(View.GONE);
        btn1Choice2.setVisibility(View.GONE);
        btn1Choice3.setVisibility(View.GONE);
        btn1Choice4.setVisibility(View.GONE);
        txt1Choice1.setVisibility(View.GONE);
        txt1Choice2.setVisibility(View.GONE);
        txt1Choice3.setVisibility(View.GONE);
        txt1Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn1Choice1.setVisibility(View.VISIBLE);
        btn1Choice2.setVisibility(View.VISIBLE);
        btn1Choice3.setVisibility(View.VISIBLE);
        btn1Choice4.setVisibility(View.VISIBLE);
        txt1Choice1.setVisibility(View.VISIBLE);
        txt1Choice2.setVisibility(View.VISIBLE);
        txt1Choice3.setVisibility(View.VISIBLE);
        txt1Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn1Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn1Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn1Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn1Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn1Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn1Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
        btn1Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        btn1Choice4.setImageResource(R.drawable.btn_pressed);
                    }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn1Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn1Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn1Restart.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

    }

}




