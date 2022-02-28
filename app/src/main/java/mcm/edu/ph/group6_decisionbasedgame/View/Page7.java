package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page7 extends AppCompatActivity implements View.OnClickListener{

    ImageView darkShade7;
    TextView txt7Dialogue, txt7Choice1, txt7Choice2, txt7Choice3,txt7Choice4, txt7Reset;
    ImageButton btn7Choice1, btn7Choice2, btn7Choice3, btn7Choice4, btn7_1Reset;
    VideoView death7;
    MediaController mediaController;
    Handler handler;
    Intent svc, page4, intro;

    String userName;
    String TAG = "Page7";

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
        darkShade7 = findViewById(R.id.darkShade1);


        btn7Choice1 = findViewById(R.id.btn7Choice1);
        btn7Choice2 = findViewById(R.id.btn7Choice2);
        btn7Choice3 = findViewById(R.id.btn7Choice3);
        btn7Choice4 = findViewById(R.id.btn7Choice4);
        btn7_1Reset = findViewById(R.id.btn7_1Reset);
        txt7Dialogue = findViewById(R.id.txt7Dialogue);
        txt7Choice1 = findViewById(R.id.txt7Choice1);
        txt7Choice2 = findViewById(R.id.txt7Choice2);
        txt7Choice3 = findViewById(R.id.txt7Choice3);
        txt7Choice4 = findViewById(R.id.txt7Choice4);
        txt7Reset = findViewById(R.id.txt7Reset);
        death7 = findViewById(R.id.death7);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        Log.d(TAG, "The user's name is " + userName + ".");

        svc = new Intent(this, MediaPlayerService.class);

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn7Choice1.setOnClickListener(this);
        btn7Choice2.setOnClickListener(this);
        btn7Choice3.setOnClickListener(this);
        btn7Choice4.setOnClickListener(this);
        btn7_1Reset.setOnClickListener(this);

        death7.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);
        mediaController = new MediaController(this); //link mediaController to videoView
        mediaController.setAnchorView(death7); //allow mediaController to control our videoView
        death7.setMediaController(mediaController);


        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade7,"alpha",0.7f, 1f);
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

        txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt7Dialogue.setText(R.string.p7_dialogue1);

        // the code inside handler will run after the 4-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt7Dialogue.setText(R.string.p7_dialogue2);

                // the code inside handler will run after the 7-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt7Dialogue.setText(R.string.p7_decision);

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

            // 1. Stay in the car.
            case R.id.btn7Choice1:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt7Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopService(svc); // stop music

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText("Think" + userName + "Think!");

                                death7.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // 2. Grab your stuff and find a shelter.
            case R.id.btn7Choice2:

                break;

            // 3. Fight the zombies
            case R.id.btn7Choice3:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt7Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopService(svc); // stop music

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText(R.string.p7_death3);

                                death7.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. R U N.
            case R.id.btn7Choice4:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt7Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopService(svc); // stop music

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText(R.string.p7_death4);

                                death7.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // If reset button is pressed
            case R.id.btn7_1Reset:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn7_1Reset.setVisibility(View.VISIBLE);
        txt7Reset.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn7Choice1.setVisibility(View.GONE);
        btn7Choice2.setVisibility(View.GONE);
        btn7Choice3.setVisibility(View.GONE);
        btn7Choice4.setVisibility(View.GONE);
        txt7Choice1.setVisibility(View.GONE);
        txt7Choice2.setVisibility(View.GONE);
        txt7Choice3.setVisibility(View.GONE);
        txt7Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn7Choice1.setVisibility(View.VISIBLE);
        btn7Choice2.setVisibility(View.VISIBLE);
        btn7Choice3.setVisibility(View.VISIBLE);
        btn7Choice4.setVisibility(View.VISIBLE);
        txt7Choice1.setVisibility(View.VISIBLE);
        txt7Choice2.setVisibility(View.VISIBLE);
        txt7Choice3.setVisibility(View.VISIBLE);
        txt7Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn7Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn7Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn7Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn7Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn7_1Reset.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7_1Reset.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7_1Reset.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

    }


}