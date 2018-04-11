package com.we_smart.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.we_smart.permissions.PermissionsListener;
import com.we_smart.permissions.PermissionsManager;

import java.util.Arrays;

/**
 * Created by zhaol on 2018/4/11.
 */

public class PermissionActivity extends AppCompatActivity {

    private PermissionsManager permissionsManager;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        permissionsManager = new PermissionsManager(this, permissions);
        permissionsManager.checkPermissions(new PermissionsListener() {

            @Override
            public void getAllPermissions() {

            }

            @Override
            public void PermissionsDenied(String... deniedPermissions) {
                Log.i("permission", "缺少权限" + Arrays.toString(deniedPermissions));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean b = shouldShowRequestPermissionRationale(deniedPermissions[0]);
                    if (b) {
                        permissionsManager.showDialogTipUserGoToAppSettting("存储权限不可用", "当前用户已经拒绝权限申请，没有该权限将无法继续使用");
                    } else {
                        permissionsManager.showRequestDialog("缺少权限", "需要内存读写权限来存储信息", deniedPermissions);
                    }
                }
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
        if (requestCode == PermissionsManager.REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //如果依然没有获取到需要的权限
                        if (shouldShowRequestPermissionRationale(permissions[i])){
                            //需要引导用户手动开启权限
                            permissionsManager.showDialogTipUserGoToAppSettting("存储权限不可用", "需要内存读写权限来存储信息");
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
