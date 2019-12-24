package com.hoangpro.dotplaysapp.base;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public void openActivity(Class target,boolean hasFinish){
        Intent intent = new Intent(this,target);
        startActivity(intent);
        if (hasFinish){
            finish();
        }
    }
}
