package com.we_smart.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.we_smart.speech.BaiduSpeech;
import com.we_smart.speech.SpeechManager;
import com.we_smart.speech.listener.OnSpeechListener;

public class SpeechActivity extends AppCompatActivity {

    private TextView mTvResult;
    private Button mBtnSpeech;

    private SpeechManager speechManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        speechManager = new SpeechManager(this);
        initView();
    }

    private void initView() {
        mTvResult = findViewById(R.id.tv_result);
        mBtnSpeech = findViewById(R.id.btn_start_speech);

        mBtnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechManager.startSpeechRecog();
            }
        });

        speechManager.setOnSpeechListener(new OnSpeechListener() {
            @Override
            public void onBeginSpeech() {
                mTvResult.setText("开始识别");
            }

            @Override
            public void onNotSpeech() {
                mTvResult.setText("没有说话");
            }

            @Override
            public void onError(String errorDesc, int errorCode) {

            }

            @Override
            public void onResult(String result) {
                mTvResult.setText(result);
            }

            @Override
            public void onVolumeChange(int volume) {

            }

            @Override
            public void onEndSpeech() {

            }

            @Override
            public void onFinish(String text) {

            }

            @Override
            public void onWakeup() {

            }
        });
    }
}
