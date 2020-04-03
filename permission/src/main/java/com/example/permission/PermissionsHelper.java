package com.example.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by stf on 2018-11-06.
 */

public class PermissionsHelper {
    private Activity activity;

    public PermissionsHelper(Activity activity) {
        this.activity = activity;
    }

    public Activity getHostAct() {
        return activity;
    }

    public void requestPermissions(int requsetCode, String[] perms) {
        Log.i("stf", "PermissionsHelper-辅助类来申请权限了-->");
        for (int i = 0; i < perms.length; i++) {
            Log.i("stf", "PermissionsHelper-perms--->" + perms[i]);
        }
        ActivityCompat.requestPermissions(getHostAct(), perms, requsetCode);
    }

    /**
     * @author stf
     * @time 2018-11-13 13:16
     * @remark 检查权限组中是否有点击了不在询问的权限
     * 上次弹出权限请求，点击了拒绝，但没有勾选“不在询问” 返回true ,继续询问
     * 上次弹出权限请求，点击了拒绝，但勾选“不在询问” 返回false ，弹出对话框
     */
    public boolean hasDeniedForever(@NonNull List<String> deniedPermissions) {
        for (String permission : deniedPermissions) {
            boolean b = ActivityCompat.shouldShowRequestPermissionRationale(getHostAct(), permission);
            if (b) {
                return false;
            }
        }
        return true;
    }

    /**
     * @author stf
     * @time 2018-11-13 15:22
     * @remark 返回清单文件中申请过的权限
     */
    public static List<String> getManifestPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @author  stf
     * @time    2018-11-13 15:30
     * @remark
     * 检测权限有没有在清单文件中注册
     */
    public static void checkPermissios(Activity activity, String[] requestPermissions) {
        List<String> manifest = getManifestPermissions(activity);
        if (manifest != null && manifest.size() != 0) {
            for (String permission : requestPermissions) {
                if (!manifest.contains(permission)) {
                    throw new ManifestRegisterException(permission);
                }
            }
        } else {
            throw new ManifestRegisterException(null);
        }
    }

}
