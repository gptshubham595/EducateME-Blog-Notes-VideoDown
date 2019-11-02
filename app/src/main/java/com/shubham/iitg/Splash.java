package com.shubham.iitg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.shubham.iitg.activity.Splash1;

import maes.tech.intentanim.CustomIntent;

public class Splash extends AppCompatActivity {
ImageView image;
TextView text,text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        image=findViewById(R.id.logo);
        text=findViewById(R.id.title);
        text2=findViewById(R.id.title2);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image.performClick();
            }
        }, 50);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
            }
        }, 50);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), Splash2.class);
                startActivity(i);
                CustomIntent.customType(Splash.this,"fadein-to-fadeout");
            }
        }, 4000);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_to_top);
                Animation animationi = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade_in);
                image.startAnimation(animation);
                text.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);

                text.startAnimation(animationi);
                text2.startAnimation(animationi);
            }
        });

    }
}

