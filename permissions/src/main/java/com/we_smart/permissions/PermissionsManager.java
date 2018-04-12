package com.we_smart.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaol on 2018/4/11.
 */

public class PermissionsManager {
    private Activity mContext;
    private String[] mNeedPermissions;
    private List<String> mDeniedPermissions;
    private PermissionsListener mPermissionsListener;
    //请求权限回调码
    public static final int REQUEST_CODE = 100;
    //权限设置界面回调码
    public static final int REQUEST_SETTING_CODE = 101;

    public PermissionsManager(Activity context, String... permissions) {
        this.mContext = context;
        this.mNeedPermissions = permissions;
    }

    /**
     * 检查 needPermissions 中的权限是否全部已经成功申请
     */
    public void checkPermissions(PermissionsListener permissionsListener) {
        this.mPermissionsListener = permissionsListener;

        if (needCheck()) {
            if (mDeniedPermissions == null) {
                mDeniedPermissions = new ArrayList<>();
            }

            for (String permission : mNeedPermissions) {
                if (!selfPermissionGranted(permission)) {
                    //提示没有权限
                    mDeniedPermissions.add(permission);
                }
            }

            if (mDeniedPermissions.size() > 0) {
                String denied[] = new String[mDeniedPermissions.size()];
                int index = 0;
                for (String permission : mDeniedPermissions) {
                    denied[index] = permission;
                    index++;
                }
                mPermissionsListener.PermissionsDenied(denied);
            }
        }
    }

    /**
     * 弹窗提示申请权限
     */
    public void showRequestDialog(String title, String message, final String... permissions) {
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermissions(permissions);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissionsListener.cancelPermissionRequest();
                    }
                }).setCancelable(false).show();
    }

    // 提示用户去应用设置界面手动开启权限
    public void showDialogTipUserGoToAppSettting(String title, String message) {
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissionsListener.cancelPermissionRequest();
                    }
                }).setCancelable(false).show();
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);

        mContext.startActivityForResult(intent, REQUEST_SETTING_CODE);
    }

    /**
     * 开始请求权限
     */
    public void startRequestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(mContext, permissions, REQUEST_CODE);
    }

    /**
     * 只有大于 23 的系统才需要动态申请权限
     *
     * @return 是否需要动态申请权限
     */
    private boolean needCheck() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public boolean selfPermissionGranted(String permission) {
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                result = mContext.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(mContext, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

}
