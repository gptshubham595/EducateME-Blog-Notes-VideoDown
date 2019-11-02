package com.shubham.iitg;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shubham.iitg.activity.Splash1;

import maes.tech.intentanim.CustomIntent;

public class Splash2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.edu);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.release();
                Intent i=new Intent(getApplicationContext(), Splash1.class);
                startActivity(i);
                CustomIntent.customType(Splash2.this,"fadein-to-fadeout");
            }
        }, 7820);

    }

}
