package com.we_smart.permissions;

/**
 * Created by zhaol on 2018/4/11.
 * 权限申请状态监听器
 */

public interface PermissionsListener {
    //权限全部获取成功
    void getAllPermissions();

    //需要的权限没有全部获取
    void PermissionsDenied(String... deniedPermissions);

    //申请权限窗口时点击取消 不申请权限
    void cancelPermissionRequest();
}
