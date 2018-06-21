package com.we_smart.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.we_smart.test.model.Color;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


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

    @SuppressLint("CheckResult")
    private void rxSend() {
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + Thread.currentThread().getName() + " " + integer);
                        tvRecInfo.setText("accept: " + integer);
                    }
                });
    }

    public void onStartClick(View view) {
        String baseUrl = "http://we-smart.cn:8000/app/";
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IVersion iVersion = retrofit.create(IVersion.class);
        Call<Version> call = iVersion.getVersion("version",
                0,
                0,
                "1.1.0",
                "com.ws.mesh.mesh_life");
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if (response.code() == 200) {
                    Version version = response.body();

                    Log.i(TAG, "onResponse: " + version);
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface IVersion {
        @POST("{servletName}")
        Call<Version> getVersion(@Path("servletName") String servletName,
                                 @Query("op") int opCode,
                                 @Query("st") int systemType,
                                 @Query("ver") String version,
                                 @Query("apid") String appid);
    }

    private class Version {
        private String desc;
        private String updatePath;
        private String newestVersion;
        private boolean needUpdate;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUpdatePath() {
            return updatePath;
        }

        public void setUpdatePath(String updatePath) {
            this.updatePath = updatePath;
        }

        public String getNewestVersion() {
            return newestVersion;
        }

        public void setNewestVersion(String newestVersion) {
            this.newestVersion = newestVersion;
        }

        public boolean isNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
        }

        @Override
        public String toString() {
            return "desc " + desc + " version " + newestVersion + " updatePath " + updatePath + " needUpdate " + needUpdate;
        }
    }
}
