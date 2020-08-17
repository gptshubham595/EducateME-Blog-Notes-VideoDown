package com.shubham.iitg;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.shubham.wowoviewpager.Enum.Ease;
import com.shubham.wowoviewpager.WoWoViewPager;
import com.shubham.wowoviewpager.WoWoViewPagerAdapter;

public abstract class WoWoActivity extends AppCompatActivity {

    protected WoWoViewPager wowo;

    protected int ease = Ease.Linear;
    protected boolean useSameEaseTypeBack = true;

    protected TextView pageNumber;

    protected abstract int contentViewRes();

    protected int fragmentNumber() {
        return 5;
    }

    protected Integer[] fragmentColorsRes() {
        return new Integer[]{
                R.color.blue_1,
                R.color.blue_2,
                R.color.blue_3,
                R.color.blue_4,
                R.color.blue_5
        };
    }

    protected int screenW;
    protected int screenH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(contentViewRes());

        init();

        wowo = (WoWoViewPager) findViewById(R.id.wowo_viewpager);
        wowo.setAdapter(WoWoViewPagerAdapter.builder()
                .fragmentManager(getSupportFragmentManager())
                .count(fragmentNumber())                       // Fragment Count
                .colorsRes(fragmentColorsRes())                // Colors of fragments
                .build());
        setPageTV(wowo);


        screenW = WoWoUtil.getScreenWidth(this);
        screenH = WoWoUtil.getScreenHeight(this);
    }

    protected int color(int colorRes) {
        return ContextCompat.getColor(this, colorRes);
    }

    private void setPageTV(WoWoViewPager wowo) {
        wowo.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
    }

    private void init() {
        useSameEaseTypeBack = getIntent().getBooleanExtra("useSameEaseTypeBack", true);
        int easeEnumNumber = getIntent().getIntExtra("easeType", -1);
        switch (easeEnumNumber) {
            case 0:
                ease = Ease.Linear;
                break;
        }
    }

    protected int dp2px(float dp) {
        return WoWoUtil.dp2px((int) dp, this);
    }

    protected int dp2px(double dp) {
        return WoWoUtil.dp2px((int) dp, this);
    }
}
