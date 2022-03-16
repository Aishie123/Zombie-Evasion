package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.VideoView;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page2 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    ImageView darkShade2, btn2Home;
    TextView txt2Dialogue, txt2Choice1, txt2Choice2, txt2Choice3,txt2Choice4, txt2Restart;
    ImageButton btn2Choice1, btn2Choice2, btn2Choice3, btn2Choice4, btn2Restart;
    VideoView death2;
    MusicPlayerService musicPlayerService;
    Handler handler;
    Intent page4, intro, goToHome;

    boolean inventory;
    String userName;
    String TAG = "Page4";

    AlphaAnimation fadeIn;

    ObjectAnimator darkFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page2);

        //initializing components
        darkShade2 = findViewById(R.id.darkShade2);
        btn2Home = findViewById(R.id.btn2Home);
        btn2Choice1 = findViewById(R.id.btn2Choice1);
        btn2Choice2 = findViewById(R.id.btn2Choice2);
        btn2Choice3 = findViewById(R.id.btn2Choice3);
        btn2Choice4 = findViewById(R.id.btn2Choice4);
        btn2Restart = findViewById(R.id.btn2Restart);
        txt2Dialogue = findViewById(R.id.txt2Dialogue);
        txt2Choice1 = findViewById(R.id.txt2Choice1);
        txt2Choice2 = findViewById(R.id.txt2Choice2);
        txt2Choice3 = findViewById(R.id.txt2Choice3);
        txt2Choice4 = findViewById(R.id.txt2Choice4);
        txt2Restart = findViewById(R.id.txt2Restart);
        death2 = findViewById(R.id.death2);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");
        Log.d(TAG, "The user's name is " + userName + ".");

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn2Home.setOnClickListener(this);
        btn2Choice1.setOnClickListener(this);
        btn2Choice2.setOnClickListener(this);
        btn2Choice3.setOnClickListener(this);
        btn2Choice4.setOnClickListener(this);
        btn2Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        death2.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);

        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade2,"alpha",0.8f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)

    }

    @SuppressLint("SetTextI18n")
    public void dialogue(){

        txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt2Dialogue.setText(R.string.p2_dialogue1);

        // the code inside handler will run after the 3-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt2Dialogue.setText(R.string.p2_dialogue2);

                // the code inside handler will run after the 3-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt2Dialogue.setText(R.string.p2_decision);

                        showButtons(); //show choices
                    }
                }, 3000); // 3 seconds delay
            }
        }, 3000); // 3 seconds delay
    }


    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){

            // 1. Go to the backyard.
            case R.id.btn2Choice1:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt2Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death2.setVisibility(View.VISIBLE);
                        death2.startAnimation(fadeIn); // video fades in
                        death2.start(); // play video

                        death2.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt2Dialogue.setText(R.string.p2_death1);

                                death2.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // 2. Go to the basement.
            case R.id.btn2Choice2:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt2Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death2.setVisibility(View.VISIBLE);
                        death2.startAnimation(fadeIn); // video fades in
                        death2.start(); // play video

                        death2.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt2Dialogue.setText(R.string.p2_death2);

                                death2.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 3. Stay in the kitchen.
            case R.id.btn2Choice3:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt2Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death2.setVisibility(View.VISIBLE);
                        death2.startAnimation(fadeIn); // video fades in
                        death2.start(); // play video

                        death2.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt2Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt2Dialogue.setText(R.string.p2_death3);

                                death2.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. Search for supplies.
            case R.id.btn2Choice4:
                inventory = true;
                page4 = new Intent(getApplicationContext(), Page4.class);
                finish();
                page4.putExtra("user", userName);
                page4.putExtra("supplies", inventory);
                startActivity(page4); // moves to page 4 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // If home button is pressed
            case R.id.btn2Home:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(Page2.this)
                                    .setTitle("Exit Game")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(Page2.this, SplashScreen.class);
                                            startActivity(goToHome);
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getApplicationContext(),"You remained in game.",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
                break;

            // If restart button is pressed
            case R.id.btn2Restart:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn2Restart.setVisibility(View.VISIBLE);
        txt2Restart.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn2Choice1.setVisibility(View.GONE);
        btn2Choice2.setVisibility(View.GONE);
        btn2Choice3.setVisibility(View.GONE);
        btn2Choice4.setVisibility(View.GONE);
        txt2Choice1.setVisibility(View.GONE);
        txt2Choice2.setVisibility(View.GONE);
        txt2Choice3.setVisibility(View.GONE);
        txt2Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn2Choice1.setVisibility(View.VISIBLE);
        btn2Choice2.setVisibility(View.VISIBLE);
        btn2Choice3.setVisibility(View.VISIBLE);
        btn2Choice4.setVisibility(View.VISIBLE);
        txt2Choice1.setVisibility(View.VISIBLE);
        txt2Choice2.setVisibility(View.VISIBLE);
        txt2Choice3.setVisibility(View.VISIBLE);
        txt2Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn2Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn2Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn2Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn2Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn2Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn2Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
        btn2Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn2Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn2Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn2Restart.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

    }

    // --------------------------------------------------------------------------------------------------------

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


