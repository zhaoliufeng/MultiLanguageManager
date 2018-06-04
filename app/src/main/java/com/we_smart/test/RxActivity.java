package com.we_smart.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhaol on 2018/6/4.
 */

public class RxActivity extends Activity {

    private TextView tvRecInfo, tvSendInfo;
    private static final String TAG = "RxActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        tvSendInfo = findViewById(R.id.send_info);
        tvRecInfo = findViewById(R.id.rec_info);
    }

    private void rxSend(){
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 30; i++) {
                            System.out.println("发射线程 " + Thread.currentThread().getName() + "---->发射 " + i);
                            Thread.sleep(1000);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())

                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                        tvRecInfo.setText("accept: " + integer);
                    }
                });
    }

    public void onStartClick(View view) {
        rxSend();
    }
}
