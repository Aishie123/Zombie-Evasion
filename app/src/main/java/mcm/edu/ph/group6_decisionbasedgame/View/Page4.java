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

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public class Page4 extends AppCompatActivity implements View.OnClickListener, ServiceConnection{

    private ImageView darkShade4, btn4Home;
    private TextView txt4Dialogue, txt4Choice1, txt4Choice2, txt4Choice3,txt4Choice4, txt4Restart;
    private ImageButton btn4Choice1, btn4Choice2, btn4Choice3, btn4Choice4, btn4Restart;
    private VideoView death4;
    private MusicPlayerService musicPlayerService;
    private Handler handler;
    private Intent page5, page6, intro, goToHome;
    private boolean inventory;
    private String userName;
    private final String TAG = "Page4";
    private AlphaAnimation fadeIn;
    private ObjectAnimator darkFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page4);
        //initializing components
        darkShade4 = findViewById(R.id.darkShade4);
        btn4Home = findViewById(R.id.btn4Home);
        btn4Choice1 = findViewById(R.id.btn4Choice1);
        btn4Choice2 = findViewById(R.id.btn4Choice2);
        btn4Choice3 = findViewById(R.id.btn4Choice3);
        btn4Choice4 = findViewById(R.id.btn4Choice4);
        btn4Restart = findViewById(R.id.btn4Restart);
        txt4Dialogue = findViewById(R.id.txt4Dialogue);
        txt4Choice1 = findViewById(R.id.txt4Choice1);
        txt4Choice2 = findViewById(R.id.txt4Choice2);
        txt4Choice3 = findViewById(R.id.txt4Choice3);
        txt4Choice4 = findViewById(R.id.txt4Choice4);
        txt4Restart = findViewById(R.id.txt4Restart);
        death4 = findViewById(R.id.death4);


        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");

        Log.d(TAG, "The user's name is " + userName + ".");

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn4Home.setOnClickListener(this);
        btn4Choice1.setOnClickListener(this);
        btn4Choice2.setOnClickListener(this);
        btn4Choice3.setOnClickListener(this);
        btn4Choice4.setOnClickListener(this);
        btn4Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        death4.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.secret);

        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)

        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade4,"alpha",0.7f, 1f);
        // transition to make the dark screen at the front of BG even darker after death

        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)

    }

 @SuppressLint("SetTextI18n")
 public void dialogue(){

     txt4Dialogue.startAnimation(fadeIn); // dialogue fades in
     txt4Dialogue.setText(R.string.p4_dialogue1);

     // the code inside handler will run after the 2-sec delay
     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             txt4Dialogue.startAnimation(fadeIn); // dialogue fades in
             txt4Dialogue.setText(R.string.p4_decision);

             showButtons(); //show choices
         }
     }, 3000); // 3 seconds delay
 }


    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){

            // 1. Go to the basement.
            case R.id.btn4Choice1:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt4Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();
                        death4.setVisibility(View.VISIBLE);
                        death4.startAnimation(fadeIn); // video fades in
                        death4.start(); // play video

                        death4.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt4Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt4Dialogue.setText(R.string.p4_death1);

                                death4.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay

                break;

            // 2. Go to the backyard.
            case R.id.btn4Choice2:
                hideButtons(); // hide choices
                darkFadeIn.start(); // covers the screen with a black shape
                txt4Dialogue.setText(""); // makes dialogue empty

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        musicPlayerService.pauseMusic();

                        death4.setVisibility(View.VISIBLE);
                        death4.startAnimation(fadeIn); // video fades in
                        death4.start(); // play video

                        death4.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onCompletion(MediaPlayer mp)
                            {
                                musicPlayerService.unpauseMusic();
                                txt4Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt4Dialogue.setText(R.string.p4_death2);

                                death4.setVisibility(View.GONE); // removes video
                                showRestartButton();
                            }
                        });
                    }
                }, 1500); // 1 and a half seconds delay
                break;

            // 3. Go to your room.
            case R.id.btn4Choice3:
                page5 = new Intent(getApplicationContext(), Page5.class);
                finish();
                page5.putExtra("user", userName);
                page5.putExtra("supplies", inventory);
                startActivity(page5); // moves to page 5 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // 4. Go outside your house.
            case R.id.btn4Choice4:
                page6 = new Intent(getApplicationContext(), Page6.class);
                finish();
                page6.putExtra("user", userName);
                page6.putExtra("supplies", inventory);
                startActivity(page6); // moves to page 6 activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

            // If home button is pressed
            case R.id.btn4Home:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(Page4.this)
                                    .setTitle("Exit Game")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(Page4.this, SplashScreen.class);
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
            case R.id.btn4Restart:
                intro = new Intent(getApplicationContext(), IntroScreen.class);
                finish();
                startActivity(intro); // moves back to intro screen
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
                break;

        }

    }

    // call this method to show restart button  ------------------------------------------------------------------------
    public void showRestartButton(){
        btn4Restart.setVisibility(View.VISIBLE);
        txt4Restart.setVisibility(View.VISIBLE);
    }


    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn4Choice1.setVisibility(View.GONE);
        btn4Choice2.setVisibility(View.GONE);
        btn4Choice3.setVisibility(View.GONE);
        btn4Choice4.setVisibility(View.GONE);
        txt4Choice1.setVisibility(View.GONE);
        txt4Choice2.setVisibility(View.GONE);
        txt4Choice3.setVisibility(View.GONE);
        txt4Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn4Choice1.setVisibility(View.VISIBLE);
        btn4Choice2.setVisibility(View.VISIBLE);
        btn4Choice3.setVisibility(View.VISIBLE);
        btn4Choice4.setVisibility(View.VISIBLE);
        txt4Choice1.setVisibility(View.VISIBLE);
        txt4Choice2.setVisibility(View.VISIBLE);
        txt4Choice3.setVisibility(View.VISIBLE);
        txt4Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn4Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn4Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn4Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn4Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn4Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn4Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn4Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn4Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn4Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
        btn4Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn4Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn4Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn4Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn4Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn4Restart.setImageResource(R.drawable.btn_unpressed);
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
