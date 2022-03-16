package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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


public class Page7 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    ImageView darkShade7, bgPage7, btn7Home;
    TextView txt7Dialogue, txt7Choice1, txt7Choice2, txt7Choice3,txt7Choice4, txt7Restart;
    ImageButton btn7Choice1, btn7Choice2, btn7Choice3, btn7Choice4, btn7Restart;
    VideoView death7;
    MediaPlayer crashSFX;
    MusicPlayerService musicPlayerService;
    Handler handler;
    Intent intro, goToHome;

    boolean inventory;
    String userName;
    String TAG = "Page7";

    AlphaAnimation fadeIn;

    ObjectAnimator darkFadeIn;

    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page7);


        //initializing components
        darkShade7 = findViewById(R.id.darkShade7);
        bgPage7 = findViewById(R.id.bgPage7);
        btn7Home = findViewById(R.id.btn7Home);
        btn7Choice1 = findViewById(R.id.btn7Choice1);
        btn7Choice2 = findViewById(R.id.btn7Choice2);
        btn7Choice3 = findViewById(R.id.btn7Choice3);
        btn7Choice4 = findViewById(R.id.btn7Choice4);
        btn7Restart = findViewById(R.id.btn7Restart);
        txt7Dialogue = findViewById(R.id.txt7Dialogue);
        txt7Choice1 = findViewById(R.id.txt7Choice1);
        txt7Choice2 = findViewById(R.id.txt7Choice2);
        txt7Choice3 = findViewById(R.id.txt7Choice3);
        txt7Choice4 = findViewById(R.id.txt7Choice4);
        txt7Restart = findViewById(R.id.txt7Restart);
        death7 = findViewById(R.id.death7);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");
        Log.d(TAG, "The user's name is " + userName + ".");
        Log.d(TAG, "Inventory is" + String.valueOf(inventory));

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn7Home.setOnClickListener(this);
        btn7Choice1.setOnClickListener(this);
        btn7Choice2.setOnClickListener(this);
        btn7Choice3.setOnClickListener(this);
        btn7Choice4.setOnClickListener(this);
        btn7Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        death7.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);

        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade7,"alpha",0.5f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        if (inventory == false) {
            btn7Choice2.setAlpha(0.5f);
        }

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)
    }

// CRASHED VAN SCENE - STARTING PAGE

    // opening dialogue
    @SuppressLint("SetTextI18n")
    public void dialogue(){

        txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt7Dialogue.setText(R.string.p7_dialogue1);

        // the code inside handler will run after the 4-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                musicPlayerService.pauseMusic();
                playCrashSFX();

                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt7Dialogue.setText(R.string.p7_dialogue2);
                bgPage7.setImageResource(R.drawable.bg_burningvan); // change to burning van bg

                // the code inside handler will run after the 3-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.unpauseMusic();
                        crashSFX.release();

                        txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt7Dialogue.setText(R.string.p7_dialogue3);

                // the code inside handler will run after the 4-sec delay
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText(R.string.p7_decision);

                                showButtons(); //show choices
                             }
                         }, 4000); // 4 seconds delay
                     }
                 }, 3000); // 3 seconds delay
             }
         }, 4000); // 4 seconds delay
    }

    public void playCrashSFX(){
        crashSFX = MediaPlayer.create(this, R.raw.sfx_carcrash);
        crashSFX.setVolume(100,100);
        crashSFX.setLooping(false);
        crashSFX.start();
    }

    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressWarnings("ConstantConditions")
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
                        musicPlayerService.pauseMusic();

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                String strP7D1F = getResources().getString(R.string.p7_death1);
                                String strP7D1M = String.format(strP7D1F, userName);
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText(strP7D1M);

                                death7.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // 2. Grab your stuff and find some shelter.
            case R.id.btn7Choice2:

                if (inventory){
                    hideButtons(); // hide choices
                    darkFadeIn.start(); // covers the screen with a black shape
                    txt7Dialogue.setText(""); // makes dialogue empty

                    // the code inside handler will run after the 3-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                            txt7Dialogue.setText(R.string.p7win_dialogue1);

                            // the code inside handler will run after the 6-sec delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt7Dialogue.setText(R.string.p7win_dialogue2);

                                    // the code inside handler will run after the 6-sec delay
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                            txt7Dialogue.setText(R.string.p7win_dialogue3);

                                            // the code inside handler will run after the 6-sec delay
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                    txt7Dialogue.setText(R.string.p7win_dialogue4);

                                                    // the code inside handler will run after the 6-sec delay
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                            txt7Dialogue.setText(R.string.p7win_dialogue5);

                                                            // the code inside handler will run after the 6-sec delay
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                                    txt7Dialogue.setText(R.string.p7win_dialogue6);
                                                                    showRestartButton();
                                                                    txt7Restart.setText("Play Again");
                                                                }
                                                            }, 6000); // 6 seconds delay
                                                        }
                                                    }, 6000); // 6 seconds delay
                                                }
                                            }, 6000); // 6 seconds delay
                                        }
                                    }, 6000); // 6 seconds delay
                                }
                            }, 6000); // 6 seconds delay
                        }
                    }, 3000); // 3 seconds delay

                }
                else if (!inventory){
                    Toast.makeText(getApplicationContext(),"You didn't bring anything with you.",Toast.LENGTH_LONG).show();
                }
                break;

            // 3. Fight the zombies
            case R.id.btn7Choice3:

                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt7Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
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
                        musicPlayerService.pauseMusic();

                        death7.setVisibility(View.VISIBLE);
                        death7.startAnimation(fadeIn); // video fades in
                        death7.start(); // play video

                        death7.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt7Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt7Dialogue.setText(R.string.p7_death4);

                                death7.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // If home button is pressed
            case R.id.btn7Home:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(Page7.this)
                                    .setTitle("Exit Game")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(Page7.this, SplashScreen.class);
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

            // If reset button is pressed
            case R.id.btn7Restart:

                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn7Restart.setVisibility(View.VISIBLE);
        txt7Restart.setVisibility(View.VISIBLE);
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

        btn7Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn7Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn7Restart.setImageResource(R.drawable.btn_unpressed);
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