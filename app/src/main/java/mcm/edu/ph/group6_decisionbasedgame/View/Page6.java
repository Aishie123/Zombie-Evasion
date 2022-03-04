package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page6 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    ImageView darkShade6;
    TextView txt6Dialogue, txt6Choice1, txt6Choice2, txt6Choice3, txt6Choice4, txt6Restart;
    ImageButton btn6Choice1, btn6Choice2, btn6Choice3, btn6Choice4, btn6Restart;
    VideoView death6;
    MediaController mediaController;
    MusicPlayerService musicPlayerService;
    Handler handler;
    Intent page7, intro;

    boolean inventory;
    String userName;
    String TAG = "Page6";

    AlphaAnimation fadeIn;

    ObjectAnimator darkFadeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page6);


        //initializing components
        darkShade6 = findViewById(R.id.darkShade6);


        btn6Choice1 = findViewById(R.id.btn6Choice1);
        btn6Choice2 = findViewById(R.id.btn6Choice2);
        btn6Choice3 = findViewById(R.id.btn6Choice3);
        btn6Choice4 = findViewById(R.id.btn6Choice4);
        btn6Restart = findViewById(R.id.btn6Restart);
        txt6Dialogue = findViewById(R.id.txt6Dialogue);
        txt6Choice1 = findViewById(R.id.txt6Choice1);
        txt6Choice2 = findViewById(R.id.txt6Choice2);
        txt6Choice3 = findViewById(R.id.txt6Choice3);
        txt6Choice4 = findViewById(R.id.txt6Choice4);
        txt6Restart = findViewById(R.id.txt6Restart);
        death6 = findViewById(R.id.death6);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");
        Log.d(TAG, "The user's name is " + userName + ".");

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn6Choice1.setOnClickListener(this);
        btn6Choice2.setOnClickListener(this);
        btn6Choice3.setOnClickListener(this);
        btn6Choice4.setOnClickListener(this);
        btn6Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        death6.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);
        mediaController = new MediaController(this); //link mediaController to videoView
        mediaController.setAnchorView(death6); //allow mediaController to control our videoView
        death6.setMediaController(mediaController);


        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f, 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade6, "alpha", 0.5f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)
    }

    // BRENT RIVERO'S TASK
    // YOU SEE SOMEONE IN A VAN THAT IS NOT A ZOMBIE

    // page 6 dialogue
    @SuppressLint("SetTextI18n")
    public void dialogue() {

        txt6Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt6Dialogue.setText(R.string.p6_dialogue1);

        // the code inside handler will run after the 4-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt6Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt6Dialogue.setText(R.string.p6_decision);

                showButtons(); //show choices
            }
        }, 2000); // 2 seconds delay
    }


    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {

            // 1. Ask for help.
            case R.id.btn6Choice1:
                page7 = new Intent(getApplicationContext(), Page7.class);
                finish();
                page7.putExtra("user", userName);
                page7.putExtra("supplies", inventory);
                startActivity(page7); // moves to page 7 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 2. Go back to the house.
            case R.id.btn6Choice2:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt6Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death6.setVisibility(View.VISIBLE);
                        death6.startAnimation(fadeIn); // video fades in
                        death6.start(); // play video

                        death6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp) {
                                musicPlayerService.unpauseMusic();
                                txt6Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt6Dialogue.setText(R.string.p6_death2);

                                death6.setVisibility(View.GONE); // removes video
                                showRestartButton();

                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

            // 3. Ignore them.
            case R.id.btn6Choice3:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt6Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death6.setVisibility(View.VISIBLE);
                        death6.startAnimation(fadeIn); // video fades in
                        death6.start(); // play video

                        death6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp) {
                                musicPlayerService.unpauseMusic();
                                txt6Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt6Dialogue.setText(R.string.p6_death3);

                                death6.setVisibility(View.GONE); // removes video
                                showRestartButton();

                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. Go to the supermarket.
            case R.id.btn6Choice4:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt6Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death6.setVisibility(View.VISIBLE);
                        death6.startAnimation(fadeIn); // video fades in
                        death6.start(); // play video

                        death6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp) {
                                musicPlayerService.unpauseMusic();
                                txt6Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt6Dialogue.setText(R.string.p6_death4);

                                death6.setVisibility(View.GONE); // removes video
                                showRestartButton();

                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // If restart button is pressed
            case R.id.btn6Restart:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }
    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton() {
        btn6Restart.setVisibility(View.VISIBLE);
        txt6Restart.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn6Choice1.setVisibility(View.GONE);
        btn6Choice2.setVisibility(View.GONE);
        btn6Choice3.setVisibility(View.GONE);
        btn6Choice4.setVisibility(View.GONE);
        txt6Choice1.setVisibility(View.GONE);
        txt6Choice2.setVisibility(View.GONE);
        txt6Choice3.setVisibility(View.GONE);
        txt6Choice4.setVisibility(View.GONE);
    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn6Choice1.setVisibility(View.VISIBLE);
        btn6Choice2.setVisibility(View.VISIBLE);
        btn6Choice3.setVisibility(View.VISIBLE);
        btn6Choice4.setVisibility(View.VISIBLE);
        txt6Choice1.setVisibility(View.VISIBLE);
        txt6Choice2.setVisibility(View.VISIBLE);
        txt6Choice3.setVisibility(View.VISIBLE);
        txt6Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {


        btn6Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn6Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn6Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });


        btn6Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn6Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn6Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });


        btn6Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn6Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn6Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });


        btn6Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn6Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn6Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });


        btn6Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn6Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn6Restart.setImageResource(R.drawable.btn_unpressed);
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