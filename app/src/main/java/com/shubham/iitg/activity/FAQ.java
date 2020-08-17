package com.shubham.iitg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shubham.iitg.R;
import com.shubham.iitg.Url;

public class FAQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        TextView github = findViewById(R.id.lic7);
        TextView feed = findViewById(R.id.lic9);
        TextView main = findViewById(R.id.lic10);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                github();
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feed();
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail();
            }
        });
    }

    private void mail() {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gptshubham595@gmail.com", null));
        i.putExtra(Intent.EXTRA_SUBJECT, "Regarding DREAMIITG");
        i.putExtra(Intent.EXTRA_TEXT, "Hello Shubham, This is reagarding your DREAMIITG app, I wanted to contact you .");
        startActivity(Intent.createChooser(i, "Send email.."));
    }

    private void github() {
        String url = "https://bit.ly/gitshubham";
        Intent browser = new Intent(getApplicationContext(), Url.class);
        browser.putExtra("heading", "GITHUB SHUBHAM");
        browser.putExtra("url", url);
        startActivity(browser);
    }

    private void feed() {
        String url = "https://bit.ly/feediitg1";
        Intent browser = new Intent(getApplicationContext(), Url.class);
        browser.putExtra("heading", "FEEDBACK");
        browser.putExtra("url", url);
        startActivity(browser);
    }
}
