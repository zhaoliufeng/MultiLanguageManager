package com.we_smart.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.we_smart.customview.seekbar.CustomSeekBar;
import com.we_smart.customview.seekbar.OnSeekBarDragListener;
import com.we_smart.permissions.PermissionsListener;
import com.we_smart.permissions.PermissionsManager;
import com.we_smart.test.model.Color;

import java.util.Random;

/**
 * Created by zhaol on 2018/4/11.
 */

public class PermissionActivity extends AppCompatActivity {

    private PermissionsManager permissionsManager;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private CustomSeekBar seekBar;
    private Button btnRandom;
    private View colorView;
    private TextView tvProcess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        seekBar = findViewById(R.id.seek_bar);
        btnRandom = findViewById(R.id.btn_set_process);
        colorView = findViewById(R.id.view_color);
        tvProcess = findViewById(R.id.tv_process);

        final Color color = new Color();
        seekBar.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @Override
            public void dragging(int process) {
                Log.i(CustomSeekBar.TAG, String.format("Process %d", process));
                tvProcess.setText(String.format("%d%%", process));
                if (process <= 20) {
                    //ff0
                    color.r = 255;
                    color.g = (int) (12.75 * process);
                    color.b = 0;
                } else if (process <= 40) {
                    //0f0
                    color.r = (int) (255 - (12.75 * (process - 20)));
                    color.g = 255;
                    color.b = 0;
                } else if (process <= 60) {
                    //0ff
                    color.r = 0;
                    color.g = 255;
                    color.b = (int) (12.75 * (process - 40));
                } else if (process <= 80) {
                    //00f
                    color.r = 0;
                    color.g = (int) (255 - 12.75 * (process - 60));
                    color.b = 255;
                } else if (process <= 100) {
                    //f0f
                    color.r = (int) Math.round(12.75 * (process - 80));
                    color.g = 0;
                    color.b = 255;
                }
                colorView.setBackgroundColor(android.graphics.Color.argb(255, color.r, color.g, color.b));
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random = (int) (Math.random() * 100);
                Log.i(CustomSeekBar.TAG, String.format("Random %d", random));
                seekBar.setProcessWithAnimation(random);
            }
        });
        int[] coloursColors = new int[]{
                0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF7FFF00,
                0xFF00FF00, 0xFF00FF7F, 0xFF00FFFF, 0xFF007FFF,
                0xFF0000FF, 0xFF7F00FF, 0xFFFF00FF, 0xFFFF007F,
                0xFFFF0000};
        seekBar.setProcessLinearBgShader(coloursColors);
        permissionsManager = new PermissionsManager(this, permissions);
        permissionsManager.checkPermissions(new PermissionsListener() {

            @Override
            public void getAllPermissions() {

            }

            @Override
            public void PermissionsDenied(String... deniedPermissions) {
                permissionsManager.startRequestPermissions(deniedPermissions);
            }

            @Override
            public void cancelPermissionRequest() {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int deniedCount = 0;
        if (requestCode == PermissionsManager.REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedCount++;
                    }
                }
                if (deniedCount != 0){
                    permissionsManager.showDialogTipUserGoToAppSettting("存储权限不可用", "需要内存读写权限来存储信息");
                }else {
                   //do something...
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsManager.REQUEST_SETTING_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    permissionsManager.showDialogTipUserGoToAppSettting("存储权限不可用", "需要内存读写权限来存储信息");
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
