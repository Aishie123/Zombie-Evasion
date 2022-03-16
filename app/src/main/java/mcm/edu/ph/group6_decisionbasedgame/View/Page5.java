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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import mcm.edu.ph.group6_decisionbasedgame.Controller.GameController;
import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

@SuppressWarnings("FieldCanBeLocal")
public class Page5 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private ImageView darkShade5, btn5Home;
    private TextView txt5Dialogue, txt5Choice1, txt5Choice2, txt5Choice3,txt5Choice4, txt5Restart;
    private ImageButton btn5Choice1, btn5Choice2, btn5Choice3, btn5Choice4, btn5Restart;
    private VideoView death5;
    private MediaPlayer zombieSFX;
    private MusicPlayerService musicPlayerService;
    private Handler handler;
    private Intent page6, intro, goToHome;
    private boolean inventory, alive;
    private String userName;
    private final String TAG = "Page5";
    private final GameController randomizer = new GameController();

    AlphaAnimation fadeIn;
    ObjectAnimator darkFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page5);

        //initializing components
        darkShade5 = findViewById(R.id.darkShade5);
        btn5Home = findViewById(R.id.btn5Home);
        btn5Choice1 = findViewById(R.id.btn5Choice1);
        btn5Choice2 = findViewById(R.id.btn5Choice2);
        btn5Choice3 = findViewById(R.id.btn5Choice3);
        btn5Choice4 = findViewById(R.id.btn5Choice4);
        btn5Restart = findViewById(R.id.btn5Restart);
        txt5Dialogue = findViewById(R.id.txt5Dialogue);
        txt5Choice1 = findViewById(R.id.txt5Choice1);
        txt5Choice2 = findViewById(R.id.txt5Choice2);
        txt5Choice3 = findViewById(R.id.txt5Choice3);
        txt5Choice4 = findViewById(R.id.txt5Choice4);
        txt5Restart = findViewById(R.id.txt5Restart);
        death5 = findViewById(R.id.death5);

        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");
        Log.d(TAG, "The user's name is " + userName + ".");

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn5Home.setOnClickListener(this);
        btn5Choice1.setOnClickListener(this);
        btn5Choice2.setOnClickListener(this);
        btn5Choice3.setOnClickListener(this);
        btn5Choice4.setOnClickListener(this);
        btn5Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        death5.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);

        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade5,"alpha",0.8f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)

    }

    @SuppressLint("SetTextI18n")
    public void dialogue(){
        playZombieSFX();
        txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt5Dialogue.setText(R.string.p5_dialogue1);

        // the code inside handler will run after the 3-sec delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zombieSFX.release();
                txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt5Dialogue.setText(R.string.p5_decision);
                showButtons(); //show choices
            }
        }, 3000); // 3 seconds delay

    }

    public void playZombieSFX(){
        zombieSFX = MediaPlayer.create(this, R.raw.sfx_zombiebanging2);
        zombieSFX.setVolume(100,100);
        zombieSFX.setLooping(false);
        zombieSFX.start();
    }

    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {

            // 1. Fight.
            case R.id.btn5Choice1:

                if (inventory == true) {
                    hideButtons(); // hide choices
                    darkFadeIn.start(); // covers the screen with a black shape
                    txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt5Dialogue.setText(R.string.p5_alive1);

                    // the code inside handler will run after the 2-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goOutside();
                        }
                    }, 3000); // 3 seconds delay

                }

                else {
                    hideButtons(); // hide choices
                    darkFadeIn.start(); // covers the screen with a black shape
                    txt5Dialogue.setText(""); // makes dialogue empty

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            musicPlayerService.pauseMusic();
                            death5.setVisibility(View.VISIBLE);
                            death5.startAnimation(fadeIn); // video fades in
                            death5.start(); // play video

                            death5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @SuppressLint("SetTextI18n")
                                public void onCompletion(MediaPlayer mp) {
                                    musicPlayerService.pauseMusic();
                                    txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt5Dialogue.setText(R.string.p5_death1);

                                    death5.setVisibility(View.GONE); // removes video
                                    showRestartButton();
                                }
                            });
                        }
                    }, 1500); // 1 and a half seconds delay
                }

                break;

            // 2. Jump from your room's window.
            case R.id.btn5Choice2:

                alive = randomizer.randomizeSurvival();

                if (alive == true) {
                    hideButtons(); // hide choices
                    darkFadeIn.start(); // covers the screen with a black shape
                    txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt5Dialogue.setText(R.string.p5_alive2);

                    // the code inside handler will run after the 2-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goOutside();
                        }
                    }, 3000); // 3 seconds delay
                }

                else if (alive == false){ // If user dies
                    hideButtons(); // hide choices
                    darkFadeIn.start(); // covers the screen with a black shape
                    txt5Dialogue.setText(""); // makes dialogue empty

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            musicPlayerService.pauseMusic();

                            death5.setVisibility(View.VISIBLE);
                            death5.startAnimation(fadeIn); // video fades in
                            death5.start(); // play video

                            death5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @SuppressLint("SetTextI18n")
                                public void onCompletion(MediaPlayer mp) {
                                    musicPlayerService.unpauseMusic();
                                    txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt5Dialogue.setText(R.string.p5_death2);

                                    death5.setVisibility(View.GONE); // removes video
                                    showRestartButton();
                                }
                            });
                        }
                    }, 1500); // 1 and a half seconds delay
                }

                break;


            // 3. Pray.
            case R.id.btn5Choice3:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt5Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death5.setVisibility(View.VISIBLE);
                        death5.startAnimation(fadeIn); // video fades in
                        death5.start(); // play video

                        death5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp) {
                                musicPlayerService.unpauseMusic();
                                txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt5Dialogue.setText(R.string.p5_death3);

                                death5.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 4. R U N.
            case R.id.btn5Choice4:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt5Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death5.setVisibility(View.VISIBLE);
                        death5.startAnimation(fadeIn); // video fades in
                        death5.start(); // play video

                        death5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp) {
                                musicPlayerService.unpauseMusic();
                                txt5Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt5Dialogue.setText(R.string.p5_death4);

                                death5.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // If home button is pressed
            case R.id.btn5Home:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(Page5.this)
                                    .setTitle("Exit Game")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(Page5.this, SplashScreen.class);
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
            case R.id.btn5Restart:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }
    }


    // call this method to proceed to page 6
    public void goOutside(){
        page6 = new Intent(getApplicationContext(), Page6.class);
        finish();
        page6.putExtra("user", userName);
        page6.putExtra("supplies", inventory);
        startActivity(page6); // moves to page 6 activity
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        // fade transitions when moving to the next activity
    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn5Restart.setVisibility(View.VISIBLE);
        txt5Restart.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn5Choice1.setVisibility(View.GONE);
        btn5Choice2.setVisibility(View.GONE);
        btn5Choice3.setVisibility(View.GONE);
        btn5Choice4.setVisibility(View.GONE);
        txt5Choice1.setVisibility(View.GONE);
        txt5Choice2.setVisibility(View.GONE);
        txt5Choice3.setVisibility(View.GONE);
        txt5Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn5Choice1.setVisibility(View.VISIBLE);
        btn5Choice2.setVisibility(View.VISIBLE);
        btn5Choice3.setVisibility(View.VISIBLE);
        btn5Choice4.setVisibility(View.VISIBLE);
        txt5Choice1.setVisibility(View.VISIBLE);
        txt5Choice2.setVisibility(View.VISIBLE);
        txt5Choice3.setVisibility(View.VISIBLE);
        txt5Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn5Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn5Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn5Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn5Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn5Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn5Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn5Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn5Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn5Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
        btn5Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn5Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn5Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn5Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn5Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn5Restart.setImageResource(R.drawable.btn_unpressed);
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