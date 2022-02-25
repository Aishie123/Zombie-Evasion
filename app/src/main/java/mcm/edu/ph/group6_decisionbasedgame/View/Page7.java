package mcm.edu.ph.group6_decisionbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mcm.edu.ph.group6_decisionbasedgame.R;

public class Page7 extends AppCompatActivity {

    ImageView darkShade7;
    TextView txt7Dialogue, txt7Choice1, txt7Choice2, txt7Choice3, txt7Choice4;
    ImageButton btn7Choice1, btn7Choice2, btn7Choice3, btn7Choice4;

    ObjectAnimator txtFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_page7);

        darkShade7 = findViewById(R.id.darkShade7);

        btn7Choice1 = findViewById(R.id.btn7Choice1);
        btn7Choice2 = findViewById(R.id.btn7Choice2);
        btn7Choice3 = findViewById(R.id.btn7Choice3);
        btn7Choice4 = findViewById(R.id.btn7Choice4);

        txt7Dialogue = findViewById(R.id.txt7Dialogue);
        txt7Choice1 = findViewById(R.id.txt7Choice1);
        txt7Choice2 = findViewById(R.id.txt7Choice2);
        txt7Choice3 = findViewById(R.id.txt7Choice3);
        txt7Choice4 = findViewById(R.id.txt7Choice4);

        btn7Choice1.setVisibility(View.GONE);
        btn7Choice2.setVisibility(View.GONE);
        btn7Choice3.setVisibility(View.GONE);
        btn7Choice4.setVisibility(View.GONE);
        txt7Choice1.setVisibility(View.GONE);
        txt7Choice2.setVisibility(View.GONE);
        txt7Choice3.setVisibility(View.GONE);
        txt7Choice4.setVisibility(View.GONE);
    }

    //THEY KINDLY HELP YOU AND LET YOU IN THEIR VAN - ALFONSO
    // 7TH PAGE START - START

    @SuppressLint("SetTextI18n")
    public void opening() {

        txtFadeIn = ObjectAnimator.ofFloat(txt7Dialogue, "alpha", 0f, 1f);
        txtFadeIn.setDuration(2000);

        txtFadeIn.start();

        txt7Dialogue.setText("They kindly help you and let you in their van...");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtFadeIn.start();// dialogue fades in
                txt7Dialogue.setText("They made an accident and you are stranded...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtFadeIn.start();
                        txt7Dialogue.setText("What will you do? \n" +
                                "\n 1. Stay in the car." +
                                "\n 2. Bring your stuff and find a shelter." +
                                "\n 3. Fight the horde." +
                                "\n 4. Run away.");

                        btn7Choice1.setVisibility(View.VISIBLE);
                        btn7Choice2.setVisibility(View.VISIBLE);
                        btn7Choice3.setVisibility(View.VISIBLE);
                        btn7Choice4.setVisibility(View.VISIBLE);
                        txt7Choice1.setVisibility(View.VISIBLE);
                        txt7Choice2.setVisibility(View.VISIBLE);
                        txt7Choice3.setVisibility(View.VISIBLE);
                        txt7Choice4.setVisibility(View.VISIBLE);
                    }

                }, 7000);
            }
        }, 4000);


    }



    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){

            case R.id.btn7Choice1:

                break;

            case R.id.btn7Choice2:

                break;

            case R.id.btn7Choice3:

                break;

            case R.id.btn7Choice4:

                break;



        }

    }

}