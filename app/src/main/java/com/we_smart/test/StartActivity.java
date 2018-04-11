package com.we_smart.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by zhaol on 2018/3/28.
 */

public class StartActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onTurnClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onSpeechClick(View view) {
        startActivity(new Intent(this, SpeechActivity.class));
    }

    public void onPermissionClick(View view) {
        startActivity(new Intent(this, PermissionActivity.class));
    }
}
