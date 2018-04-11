package com.we_smart.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.we_smart.speech.SpeechManager;
import com.we_smart.speech.conts.BaiduSpeechKeyValues;
import com.we_smart.speech.listener.OnSpeechListener;

public class SpeechActivity extends AppCompatActivity {

    private TextView mTvResult;
    private TextView mTvVolume;
    private TextView mTvStatus;
    private Button mBtnSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        SpeechManager.getInstance().initWithBaidu(this);
        initView();
    }

    private void initView() {
        mTvResult = findViewById(R.id.tv_result);
        mBtnSpeech = findViewById(R.id.btn_start_speech);
        mTvVolume = findViewById(R.id.tv_vol);
        mTvStatus = findViewById(R.id.tv_status);

        mBtnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechManager.getInstance().startSpeechRecog();
            }
        });

        SpeechManager.getInstance().setOnSpeechListener(new OnSpeechListener() {
            @Override
            public void onBeginSpeech() {
                mTvStatus.setText("开始识别");
                mTvResult.setText("正在识别");
            }

            @Override
            public void onNotSpeech() {
                mTvResult.setText("没有说话");
            }

            @Override
            public void onError(String errorDesc, int errorCode) {
                if (errorCode == BaiduSpeechKeyValues.VALUE_NO_NETWORK_CODE){
                    mTvResult.setText("没有网络");
                }
            }

            @Override
            public void onResult(String result) {
                mTvResult.setText(result);
            }

            @Override
            public void onVolumeChange(int volume) {
                mTvVolume.setText(volume + "");
            }

            @Override
            public void onEndSpeech() {
                mTvStatus.setText("识别结束");
            }

            @Override
            public void onFinish() {
                mTvStatus.setText("识别结束");
            }

            @Override
            public void onWakeup() {
                mTvStatus.setText("唤起成功");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechManager.getInstance().destroy();
    }
}
