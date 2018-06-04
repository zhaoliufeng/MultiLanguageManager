package com.we_smart.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.we_smart.multilanguage.MultiLanguageManager;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private MultiLanguageManager mMultiLanguageManager;
    private Button mBtnEn, mBtnCN, mBtnTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiLanguageManager = new MultiLanguageManager(this);
        mBtnEn = findViewById(R.id.btn_en);
        mBtnCN = findViewById(R.id.btn_cn);
        mBtnTW = findViewById(R.id.btn_tw);

        Locale currLocale = mMultiLanguageManager.getCurrentLocale();
        if (currLocale.equals(Locale.ENGLISH)){
            mBtnEn.setTextColor(Color.GRAY);
        }else if (currLocale.equals(Locale.SIMPLIFIED_CHINESE)){
            mBtnCN.setTextColor(Color.GRAY);
        }else if (currLocale.equals( Locale.TRADITIONAL_CHINESE)){
            mBtnTW.setTextColor(Color.GRAY);
        }

    }

    public void onEnglishClick(View view) {
        mMultiLanguageManager.changeCurrentLocale(getResources(), Locale.ENGLISH);
        reStartActivity();
    }

    public void onSimpleChineseClick(View view) {
        mMultiLanguageManager.changeCurrentLocale(getResources(), Locale.SIMPLIFIED_CHINESE);
        reStartActivity();
    }

    public void onChineseClick(View view) {
        mMultiLanguageManager.changeCurrentLocale(getResources(), Locale.TRADITIONAL_CHINESE);
        reStartActivity();
    }

    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
