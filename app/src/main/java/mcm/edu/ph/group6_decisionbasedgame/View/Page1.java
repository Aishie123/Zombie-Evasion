package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page1 extends AppCompatActivity {

    private String TAG = "Page1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_page1);


    }

//bedroom scene 1









}
