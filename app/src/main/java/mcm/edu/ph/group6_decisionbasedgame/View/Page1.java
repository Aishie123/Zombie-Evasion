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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MediaPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.Model.GameData;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page1 extends AppCompatActivity implements View.OnClickListener{

    ImageView darkShade;
    TextView txtDialogue, txtChoice1, txtChoice2, txtChoice3,txtChoice4, txtReset;
    ImageButton btnChoice1, btnChoice2, btnChoice3, btnChoice4, btnReset;
    VideoView videoView;
    MediaController mediaController;
    Handler handler;
    Intent svc, page2, page3, page6, intro;

    String userName;
    String TAG = "Page1";


    GameData game = new GameData();

    ObjectAnimator txtFadeIn, vidFadeIn,shapeFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page1);


        //initializing components
        darkShade = findViewById(R.id.darkShade);
        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        btnChoice4 = findViewById(R.id.btnChoice4);
        btnReset = findViewById(R.id.btnReset);
        txtDialogue = findViewById(R.id.txtDialogue);
        txtChoice1 = findViewById(R.id.txtChoice1);
        txtChoice2 = findViewById(R.id.txtChoice2);
        txtChoice3 = findViewById(R.id.txtChoice3);
        txtChoice4 = findViewById(R.id.txtChoice4);
        txtReset = findViewById(R.id.txtReset);
        videoView = findViewById(R.id.videoView);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        Log.d(TAG, "The user's name is " + userName + ".");

        svc = new Intent(this, MediaPlayerService.class);
        game = new GameData(userName); // informing the database about user's name


        // choices to be shown later on
        btnChoice1.setOnClickListener(this);
        btnChoice2.setOnClickListener(this);
        btnChoice3.setOnClickListener(this);
        btnChoice4.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);
        mediaController = new MediaController(this); //link mediaController to videoView
        mediaController.setAnchorView(videoView); //allow mediaController to control our videoView
        videoView.setMediaController(mediaController);

        handler = new Handler(Looper.getMainLooper()); // for delay

        hideButtons();
        opening();
        press();
    }

// BEDROOM SCENE - STARTING PAGE

    // starting dialogue
    @SuppressLint("SetTextI18n")
    public void opening(){

        txtFadeIn = ObjectAnimator.ofFloat(txtDialogue,"alpha",0f, 1f);
        // this creates a fade in text transition by turning the transparency (alpha) of the text from 0% (0f) to 100% (1f)
        txtFadeIn.setDuration(2000); // fades in for 2 seconds

        txtFadeIn.start(); // dialogue fades in

        txtDialogue.setText("You wake up at your bed... ");

        // the code inside handler will run after the 4-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtFadeIn.start(); // dialogue fades in
                txtDialogue.setText("You hear from your room's radio that the global pandemic made humans turn into ZOMBIES.");

                // the code inside handler will run after the 7-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtFadeIn.start(); // dialogue fades in
                        txtDialogue.setText("What will you do? \n"+
                                "\n 1. Call somebody for help." +
                                "\n 2. Go to the kitchen." +
                                "\n 3. Go back to sleep." +
                                "\n 4. Go downstairs and look for people around.");

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
            case R.id.btnChoice1:
                page3 = new Intent(getApplicationContext(), Page3.class);
                startActivity(page3); // moves to page 3 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 2. Go to the kitchen
            case R.id.btnChoice2:
                page2 = new Intent(getApplicationContext(), Page2.class);
                startActivity(page2); // moves to page 2 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 3. Go back to sleep
            case R.id.btnChoice3:

                hideButtons(); // hide choices

                shapeFadeIn = ObjectAnimator.ofFloat(darkShade,"alpha",0.7f, 1f);
                // this shape was originally used for making the background darker,
                // but I also used this to cover the screen when the choice leads to death
                // so I turned the transparency (alpha) slowly from 70% (0.7f) to 100% (1f).

                shapeFadeIn.setDuration(1000); // fades in for 1 second
                shapeFadeIn.start(); // covers the screen with a black shape
                txtDialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopService(svc);

                        vidFadeIn = ObjectAnimator.ofFloat(videoView,"alpha",0f, 1f);
                        vidFadeIn.setDuration(2000); // fades in for 2 seconds

                        videoView.setVisibility(View.VISIBLE);
                        vidFadeIn.start(); // video fades in
                        videoView.start(); // play video

                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                txtDialogue.setText("It's the start of the game and you're already dead..." +
                                        "\n You are not good at this.");
                                videoView.setVisibility(View.GONE); // removes video
                                txtFadeIn.start(); // dialogue fades in

                                btnReset.setVisibility(View.VISIBLE); // show reset button
                                txtReset.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. Go downstairs and look for people around.
            case R.id.btnChoice4:
                page6 = new Intent(getApplicationContext(), Page6.class);
                startActivity(page6); // moves to page 6 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // If reset button is pressed
            case R.id.btnReset:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method every time you need to hide choice buttons  ------------------------------------
    public void hideButtons() {
        btnChoice1.setVisibility(View.GONE);
        btnChoice2.setVisibility(View.GONE);
        btnChoice3.setVisibility(View.GONE);
        btnChoice4.setVisibility(View.GONE);
        txtChoice1.setVisibility(View.GONE);
        txtChoice2.setVisibility(View.GONE);
        txtChoice3.setVisibility(View.GONE);
        txtChoice4.setVisibility(View.GONE);

    }

    // call this method every time you need to show choice buttons  -----------------------------------
    public void showButtons() {
        btnChoice1.setVisibility(View.VISIBLE);
        btnChoice2.setVisibility(View.VISIBLE);
        btnChoice3.setVisibility(View.VISIBLE);
        btnChoice4.setVisibility(View.VISIBLE);
        txtChoice1.setVisibility(View.VISIBLE);
        txtChoice2.setVisibility(View.VISIBLE);
        txtChoice3.setVisibility(View.VISIBLE);
        txtChoice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnChoice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnChoice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnChoice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnChoice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnChoice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnChoice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnChoice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnChoice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnChoice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnChoice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnChoice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnChoice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btnReset.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnReset.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnReset.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

    }

}




