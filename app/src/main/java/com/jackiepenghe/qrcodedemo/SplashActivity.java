package com.jackiepenghe.qrcodedemo;

import android.content.Intent;

import com.sscl.baselibrary.activity.BaseSplashActivity;

public class SplashActivity extends BaseSplashActivity {


    /**
     * 在本界面第一次启动时执行的操作
     */
    @Override
    protected void onCreate() {
        Intent intent = new Intent(this,WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
