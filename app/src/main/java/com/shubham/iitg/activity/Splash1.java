package com.shubham.iitg.activity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.google.firebase.database.FirebaseDatabase;
import com.shubham.iitg.GuidePageActivity2;
import com.shubham.iitg.R;
import com.shubham.iitg.blogapp.MainActivity;

import maes.tech.intentanim.CustomIntent;
import pl.droidsonroids.gif.GifImageView;

public class Splash1 extends AppCompatActivity {
    private ViewPagerIndicator mViewPagerIndicator;
    private ViewPager mViewPager;
    GifImageView data,data2,data3;
    TextView txt;
    ImageView nxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);

        Toast.makeText(this, "FIRST TIME USER", Toast.LENGTH_LONG).show();
        data=findViewById(R.id.data);
        data2=findViewById(R.id.data2);
        data3=findViewById(R.id.data3);
        txt=findViewById(R.id.txt);
        nxt=findViewById(R.id.next);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPagerIndicator.setupWithViewPager(mViewPager);
        mViewPagerIndicator.addOnPageChangeListener(mOnPageChangeListener);
    }
    private
    class MyPagerAdapter
            extends PagerAdapter
    {
        @Override
        public
        int getCount() {
            return 3;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public
        Object instantiateItem(final ViewGroup container, final int position) {
            final TextView textView = new TextView(Splash1.this);
            final Handler handler = new Handler();
            //textView.setText("Page " + position);
            container.addView(textView);
            return textView;
        }

        @Override
        public
        boolean isViewFromObject(final View view, final Object object) {
            return view.equals(object);
        }

        @Override
        public
        void destroyItem(final ViewGroup container, final int position, final Object object) {
            container.removeView((View) object);
        }

        @Override
        public
        CharSequence getPageTitle(final int position) {
            return String.valueOf(position);
        }
    }

    @NonNull
    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public
        void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

        }

        @Override
        public
        void onPageSelected(final int position) {
            switch (position){
                case 0:
                    txt.setText("Easy Online Note making");

                    data.setVisibility(View.VISIBLE);
                    data2.setVisibility(View.INVISIBLE);
                    data3.setVisibility(View.INVISIBLE);
                    nxt.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    txt.setText("Easy Online Coaching");

                    data.setVisibility(View.INVISIBLE);
                    data2.setVisibility(View.VISIBLE);
                    data3.setVisibility(View.INVISIBLE);
                    nxt.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    txt.setText("Better Marks");

                    data.setVisibility(View.INVISIBLE);
                    data2.setVisibility(View.INVISIBLE);
                    data3.setVisibility(View.VISIBLE);
                    nxt.setVisibility(View.VISIBLE);
                    nxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            CustomIntent.customType(Splash1.this,"fadein-to-fadeout");
                        }
                    });
                    break;

            }
      //      Toast.makeText(Splash1.this, "Page selected " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public
        void onPageScrollStateChanged(final int state) {

        }
    };
}
