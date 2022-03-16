package mcm.edu.ph.group6_decisionbasedgame.View;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import mcm.edu.ph.group6_decisionbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.group6_decisionbasedgame.R;

public class SettingsScreen extends AppCompatActivity implements ServiceConnection  {
    AudioManager audioManager;
    ImageView btnBack;
    SeekBar musicVolBar, brightBar;
    Intent goToHome;
    Context context;
    MusicPlayerService musicPlayerService;

    int brightness;

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

    private static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    private static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
    private static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_settings_screen);
        checkSystemWritePermission();

        btnBack = findViewById(R.id.btnSettingsBack);
        musicVolBar = findViewById(R.id.volumeBar);
        brightBar = findViewById(R.id.brightnessBar);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, this, BIND_AUTO_CREATE);

        context = getApplicationContext();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setVolume();
        setBrightness();

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                goToHome = new Intent(SettingsScreen.this, HomeScreen.class);
                startActivity(goToHome);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }
        );

    }

    // method for setting brightness manually --------------------------------------------------------------------------------------------------------------

    private void setBrightness(){

        //Gets the content resolver
        cResolver = getContentResolver();

        //Gets the current window
        window = getWindow();

        //Sets the seekbar range between 0 and 255
        brightBar.setMax(255);
        //Sets the seek bar progress to 1
        brightBar.setKeyProgressIncrement(1);

        try
        {
            //Gets the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            //Throws an error, in case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        //Sets the progress of the seek bar based on the system's brightness
        brightBar.setProgress(brightness);

        //Registers OnSeekBarChangeListener, so it can actually change values
        brightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Sets the system brightness to manual, so that it wont turn back to its original brightness after exiting settings
                Settings.System.putInt(cResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
                //Sets the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Gets the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Sets the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Applies attribute changes to this window
                window.setAttributes(layoutpars);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //Sets the minimal brightness level
                //If brightness bar is 20 or any value below
                if(progress<=20)
                {
                    //Sets the brightness to 20
                    brightness = 20;
                }
                else //If brightness is greater than 20
                {
                    //Sets brightness variable based on the progress bar
                    brightness = progress;
                }
            }
        });
    }

    // method for setting volume --------------------------------------------------------------------------------------------------------------
    private void setVolume()
    {
        try
        {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // s
            musicVolBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            musicVolBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            musicVolBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // checks if permission from user to write settings is granted --------------------------------------------------------------------------------------------------------------
    @SuppressLint("ObsoleteSdkInt")
    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d("TAG", "Can Write Settings: " + retVal);
            if(retVal){
                ///Permission granted by the user
            }else{
                //permission not granted navigate to permission screen
                openAndroidPermissionsMenu();
            }
        }
        return retVal;
    }

    // asks permission from user to write settings --------------------------------------------------------------------------------------------------------------
    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

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