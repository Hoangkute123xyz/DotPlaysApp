package com.hoangpro.dotplaysapp.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.base.BaseActivity;

import static com.hoangpro.dotplaysapp.morefunc.MyAnimation.setAnimAlpha;

public class SplashActivity extends BaseActivity {

    private TextView tvLogo;
    private TextView tvSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        setAnimAlpha(tvLogo);
        setAnimAlpha(tvSub);
        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                openActivity(MainActivity.class, true);
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initView() {
        tvLogo = findViewById(R.id.tvLogo);
        tvSub = findViewById(R.id.tvSub);
    }
}
