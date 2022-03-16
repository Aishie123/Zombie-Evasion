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

public class Page3 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    ImageView darkShade3, btn3Home;
    TextView txt3Dialogue, txt3Choice1, txt3Choice2, txt3Choice3,txt3Choice4, txt3Restart;
    ImageButton btn3Choice1, btn3Choice2, btn3Choice3, btn3Choice4, btn3Restart;
    VideoView death3;
    MediaPlayer endCallSFX, zombieSFX;
    MusicPlayerService musicPlayerService;
    Handler handler;
    Intent page5, goToHome;

    boolean inventory, response;
    String userName, sibling;
    final String TAG = "Page3";

    final GameController randomizer = new GameController();

    AlphaAnimation fadeIn;
    ObjectAnimator darkFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page3);

        //initializing components
        darkShade3 = findViewById(R.id.darkShade3);
        btn3Home = findViewById(R.id.btn3Home);
        btn3Choice1 = findViewById(R.id.btn3Choice1);
        btn3Choice2 = findViewById(R.id.btn3Choice2);
        btn3Choice3 = findViewById(R.id.btn3Choice3);
        btn3Choice4 = findViewById(R.id.btn3Choice4);
        btn3Restart = findViewById(R.id.btn3Restart);
        txt3Dialogue = findViewById(R.id.txt3Dialogue);
        txt3Choice1 = findViewById(R.id.txt3Choice1);
        txt3Choice2 = findViewById(R.id.txt3Choice2);
        txt3Choice3 = findViewById(R.id.txt3Choice3);
        txt3Choice4 = findViewById(R.id.txt3Choice4);
        txt3Restart = findViewById(R.id.txt3Restart);
        death3 = findViewById(R.id.death3);



        // receiving user input from intro screen
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
        inventory = i.getExtras().getBoolean("supplies");
        Log.d(TAG, "The user's name is " + userName + ".");

        // setting listeners for the choice buttons
        // this will detect whether a button is clicked or not
        btn3Home.setOnClickListener(this);
        btn3Choice1.setOnClickListener(this);
        btn3Choice2.setOnClickListener(this);
        btn3Choice3.setOnClickListener(this);
        btn3Choice4.setOnClickListener(this);
        btn3Restart.setOnClickListener(this);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        handler = new Handler(Looper.getMainLooper()); // for delay

        fadeIn = new AlphaAnimation(0f , 1f); // this creates a fade in transition
        // by turning the opacity (alpha) of the
        // text from 0% (0f) to 100% (1f)
        fadeIn.setDuration(2000); // setting duration of transition, which is 2 seconds

        darkFadeIn = ObjectAnimator.ofFloat(darkShade3,"alpha",0.8f, 1f);
        // transition to make the dark screen at the front of BG even darker after death
        darkFadeIn.setDuration(1000); // setting the fade in duration to 1 second for the black screen

        hideButtons(); // hide choices
        dialogue(); // start opening dialogue
        press(); // calls method that detects if buttons are pressed,
        // and if pressed, the button's image will change (from an unpressed btn to a pressed btn)

    }

    public void dialogue(){
        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
        txt3Dialogue.setText(R.string.p3_dialogue1);

                // the code inside handler will run after the 3-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt3Dialogue.setText(R.string.p3_decision);

                        showButtons(); //show choices
                    }
                }, 3000); // 3 seconds delay
    }


    // actions after player makes a decision and clicks a button -------------------------------------------------------
    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClick(View v) {
        switch (v.getId()) {


            // 1. The police ------------------------------------------------------------------------------
            case R.id.btn3Choice1:
                hideButtons(); // hide choices
                response = randomizer.policeResponse();

                // if police responds
                if (response == true) {
                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt3Dialogue.setText(R.string.p3_policeR1);

                    // the code inside handler will run after the 3-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                            txt3Dialogue.setText(R.string.p3_policeR2);

                            // the code inside handler will run after the 7-sec delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String strP3PR3F = getResources().getString(R.string.p3_policeR3);
                                    String strP3PR3M = String.format(strP3PR3F, userName);
                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt3Dialogue.setText(strP3PR3M);

                                    // the code inside handler will run after the 3-sec delay
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                            txt3Dialogue.setText(R.string.p3_policeR4);

                                            // the code inside handler will run after the 3-sec delay
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    musicPlayerService.pauseMusic();
                                                    playEndCallSFX();
                                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                    txt3Dialogue.setText(R.string.p3_policeR5);

                                                    // the code inside handler will run after the 3-sec delay
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            endCallSFX.release();
                                                            musicPlayerService.unpauseMusic();
                                                            moveToP5();
                                                        }
                                                    }, 3000); // 3 seconds delay
                                                }
                                            }, 3000); // 3 seconds delay

                                        }
                                    }, 7000); // 7 seconds delay

                                }
                            }, 3000); // 3 seconds delay

                        }
                    }, 3000); // 3 seconds delay

                }

                // if police doesn't respond
                else if (response == false) {
                    musicPlayerService.pauseMusic();
                    playEndCallSFX();
                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt3Dialogue.setText(R.string.p3_policeI);

                    // the code inside handler will run after the 4-sec delay
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            endCallSFX.release();
                            musicPlayerService.unpauseMusic();
                            moveToP5();
                        }
                    }, 4000); // 4 seconds delay
                }

            break;



            // 2. Your friend ------------------------------------------------------------------------------
            case R.id.btn3Choice2:
                hideButtons(); // hide choices
                txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt3Dialogue.setText(R.string.p3_friend1);

                // the code inside handler will run after the 3-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt3Dialogue.setText(R.string.p3_friend2);

                        // the code inside handler will run after the 7-sec delay
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                musicPlayerService.pauseMusic();
                                playEndCallSFX();
                                txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt3Dialogue.setText(R.string.p3_friend3);

                                // the code inside handler will run after the 3-sec delay
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        endCallSFX.release();
                                        musicPlayerService.unpauseMusic();
                                        moveToP5();
                                    }
                                }, 3000); // 3 seconds delay
                            }
                        }, 7000); // 7 seconds delay

                    }
                }, 3000); // 3 seconds delay

            break;



            // 3. Your sibling ------------------------------------------------------------------------------
            case R.id.btn3Choice3:
                hideButtons(); // hide choices
                sibling = randomizer.randomizeSibling();

                if (sibling.equals("sister")){
                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt3Dialogue.setText(R.string.p3_sister1);

                    // the code inside handler will run after the 3-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                            txt3Dialogue.setText(R.string.p3_sister2);

                            // the code inside handler will run after the 3-sec delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt3Dialogue.setText(R.string.p3_sister3);

                                    // the code inside handler will run after the 3-sec delay
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String strP3S4F = getResources().getString(R.string.p3_sister4);
                                            String strP3S4M = String.format(strP3S4F, userName);
                                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                            txt3Dialogue.setText(strP3S4M);

                                            // the code inside handler will run after the 4-sec delay
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String strP3S5F = getResources().getString(R.string.p3_sister5);
                                                    String strP3S5M = String.format(strP3S5F, userName);
                                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                    txt3Dialogue.setText(strP3S5M);

                                                    // the code inside handler will run after the 3-sec delay
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            musicPlayerService.pauseMusic();
                                                            playEndCallSFX();
                                                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                            txt3Dialogue.setText(userName + R.string.p3_sister6);

                                                            // the code inside handler will run after the 3-sec delay
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    endCallSFX.release();
                                                                    musicPlayerService.unpauseMusic();
                                                                    moveToP5();
                                                                }
                                                            }, 3000); // 3 seconds delay

                                                        }
                                                    }, 3000); // 3 seconds delay

                                                }
                                            }, 4000); // 4 seconds delay

                                        }
                                    }, 3000); // 3 seconds delay

                                }
                            }, 3000); // 3 seconds delay

                        }
                    }, 3000); // 3 seconds delay

                }

                if (sibling.equals("brother")){
                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                    txt3Dialogue.setText(R.string.p3_brother1);

                    // the code inside handler will run after the 3-sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                            txt3Dialogue.setText(R.string.p3_brother2);

                            // the code inside handler will run after the 4-sec delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                    txt3Dialogue.setText(R.string.p3_brother3);

                                    // the code inside handler will run after the 3-sec delay
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String strP3B4F = getResources().getString(R.string.p3_brother4);
                                            String strP3B4M = String.format(strP3B4F, userName);
                                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                            txt3Dialogue.setText(strP3B4M);

                                            // the code inside handler will run after the 4-sec delay
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String strP3B5F = getResources().getString(R.string.p3_brother5);
                                                    String strP3B5M = String.format(strP3B5F, userName);
                                                    txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                    txt3Dialogue.setText(strP3B5M);

                                                    // the code inside handler will run after the 3-sec delay
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            musicPlayerService.pauseMusic();
                                                            playEndCallSFX();
                                                            txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                            txt3Dialogue.setText(R.string.p3_brother6);

                                                            // the code inside handler will run after the 3-sec delay
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    endCallSFX.release();
                                                                    musicPlayerService.unpauseMusic();
                                                                    moveToP5();
                                                                }
                                                            }, 3000); // 3 seconds delay

                                                        }
                                                    }, 3000); // 3 seconds delay

                                                }
                                            }, 4000); // 4 seconds delay

                                        }
                                    }, 3000); // 3 seconds delay

                                }
                            }, 4000); // 4 seconds delay

                        }
                    }, 3000); // 3 seconds delay

                }

            break;

            // 4. Your parents ------------------------------------------------------------------------------
            case R.id.btn3Choice4:
                hideButtons(); // hide choices
                txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                txt3Dialogue.setText(R.string.p3_parents1);

                // the code inside handler will run after the 3-sec delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String strP3P2F = getResources().getString(R.string.p3_parents2);
                        String strP3P2M = String.format(strP3P2F, userName);
                        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                        txt3Dialogue.setText(strP3P2M);

                        // the code inside handler will run after the 3-sec delay
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                txt3Dialogue.setText(R.string.p3_parents3);

                                // the code inside handler will run after the 3-sec delay
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        playZombieSFX();
                                        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                        txt3Dialogue.setText(R.string.p3_parents4);

                                        // the code inside handler will run after the 3-sec delay
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                zombieSFX.release();
                                                txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                txt3Dialogue.setText(R.string.p3_parents5);

                                                // the code inside handler will run after the 4-sec delay
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        musicPlayerService.pauseMusic();
                                                        playEndCallSFX();
                                                        txt3Dialogue.startAnimation(fadeIn); // dialogue fades in
                                                        txt3Dialogue.setText(R.string.p3_parents6);

                                                        // the code inside handler will run after the 3-sec delay
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                endCallSFX.release();
                                                                musicPlayerService.unpauseMusic();
                                                                moveToP5();
                                                            }
                                                        }, 3000); // 3 seconds delay
                                                    }
                                                }, 4000); // 4 seconds delay
                                            }
                                        }, 3000); // 3 seconds delay
                                    }
                                }, 3000); // 3 seconds delay
                            }
                        }, 3000); // 3 seconds delay
                    }
                }, 3000); // 3 seconds delay

            break;

            // If home button is pressed
            case R.id.btn3Home:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(Page3.this)
                                    .setTitle("Exit Game")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(Page3.this, SplashScreen.class);
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

        }
    }

    public void playZombieSFX(){
        zombieSFX = MediaPlayer.create(this, R.raw.sfx_zombiebanging);
        zombieSFX.setVolume(100,100);
        zombieSFX.setLooping(false);
        zombieSFX.start();
    }

    public void playEndCallSFX(){
        endCallSFX = MediaPlayer.create(this, R.raw.sfx_endcall);
        endCallSFX.setVolume(100,100);
        endCallSFX.setLooping(false);
        endCallSFX.start();
    }

    // call this method to proceed to page 5 ------------------------------------------------------------------------
    public void moveToP5(){
        page5 = new Intent(getApplicationContext(), Page5.class);
        finish();
        page5.putExtra("user", userName);
        page5.putExtra("supplies", inventory);
        startActivity(page5); // moves to page 5 activity
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // fade transitions when moving to the next activity
    }

    // call this method to hide choice buttons  ------------------------------------------------------------------------
    public void hideButtons() {
        btn3Choice1.setVisibility(View.GONE);
        btn3Choice2.setVisibility(View.GONE);
        btn3Choice3.setVisibility(View.GONE);
        btn3Choice4.setVisibility(View.GONE);
        txt3Choice1.setVisibility(View.GONE);
        txt3Choice2.setVisibility(View.GONE);
        txt3Choice3.setVisibility(View.GONE);
        txt3Choice4.setVisibility(View.GONE);

    }

    // call this method to show choice buttons  -----------------------------------------------------------------------
    public void showButtons() {
        btn3Choice1.setVisibility(View.VISIBLE);
        btn3Choice2.setVisibility(View.VISIBLE);
        btn3Choice3.setVisibility(View.VISIBLE);
        btn3Choice4.setVisibility(View.VISIBLE);
        txt3Choice1.setVisibility(View.VISIBLE);
        txt3Choice2.setVisibility(View.VISIBLE);
        txt3Choice3.setVisibility(View.VISIBLE);
        txt3Choice4.setVisibility(View.VISIBLE);
    }


    //changing button images when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btn3Choice1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn3Choice1.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn3Choice1.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn3Choice2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn3Choice2.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn3Choice2.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn3Choice3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn3Choice3.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn3Choice3.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
        btn3Choice4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn3Choice4.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn3Choice4.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });

        btn3Restart.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // when pressed
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn3Restart.setImageResource(R.drawable.btn_pressed);
                }
                // when not pressed
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn3Restart.setImageResource(R.drawable.btn_unpressed);
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

