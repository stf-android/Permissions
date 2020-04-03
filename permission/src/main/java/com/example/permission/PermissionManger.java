package com.example.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stf on 2018-11-06.
 */

public class PermissionManger {
    private static Activity activity;
    private static int requsetCodes;
    private static PermissionManger permissionManger;

    private static void init(Activity context) {
        PermissionManger.activity = context;
    }


    public static Activity getActivityContext() {
        if (activity != null) {
            return activity;
        }
        throw new NullPointerException("context为null");
    }

    public static PermissionManger getInstance(@NonNull Activity activity) {
        init(activity);
        if (permissionManger == null) {
            synchronized (PermissionManger.class) {
                if (permissionManger == null) {
                    permissionManger = new PermissionManger();
                }
            }
        }
        return permissionManger;
    }

    public static PermissionsImpl permissionsImpl;
    public static PermissionsCall call;

    public static void setPermissionsImpl(PermissionsImpl impl) {
        permissionsImpl = impl;
    }

    public static void setPermissionCall(PermissionsCall callBack) {
        call = callBack;
    }

    public static int getRequsetCode() {
        return requsetCodes;
    }

    public static void setRequsetCode(int requsetCode) {
        requsetCodes = requsetCode;
    }

    /**
     * @author stf
     * @time 2018-11-13 16:53
     * @remark 判断是否拥有此权限
     */
    public static Boolean hasPermission(@NonNull String[] perms) {
        if (getActivityContext() == null) {
            throw new NullPointerException("传入的Activity为null");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            if (perm.equals("android.permission.REQUEST_INSTALL_PACKAGES")) {
                if (isHasInstallPermission(getActivityContext())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (ContextCompat.checkSelfPermission(getActivityContext(), perm) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }

        }
        return true;
    }


    /**
     * @author stf
     * @time 2018-11-06 11:54
     * @remark 开始请求权限，弹出对话框
     */
    public static void requestPermissions(int requsetCode, @NonNull String[] perms) {
        if (requsetCode >= 256) {
            throw new IllegalArgumentException("requsetCode不能大于等于256");
        } else {
            setRequsetCode(requsetCode);
        }
        PermissionsHelper.checkPermissios(getActivityContext(), perms);
        //检查一下是否全部通过
        Log.i("stf", "--权限是否都通过了-->" + hasPermission(perms));
        if (hasPermission(perms)) {
            return;
        } else {
            Log.i("stf", "--部分权限没有通过-->");
        }

        // 帮助类去申请权限
        PermissionsHelper helper = new PermissionsHelper(getActivityContext());
        helper.requestPermissions(requsetCode, perms);
    }


    /**
     * @author stf
     * @time 2018-11-13 14:38
     * @remark 监听权限的申请结果，并且分类
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedList = new ArrayList<>();//拒绝的

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedList.add(permission);
            }
        }
        if (deniedList.isEmpty()) {
            call.granted();
        } else { // 不通过再去处理是否勾选不再询问了，弹对话框
            permissionsImpl.denideList(deniedList);
        }
    }

    /**
     * @author stf
     * @time 2018-11-13 15:20
     * @remark 如果权限拒绝了，根据是否勾选了“不在询问”
     * 勾选了，跳转设置页面
     * 未勾选，循环申请
     */
    public static void hasDeniedForever(List<String> list) {
        boolean b = new PermissionsHelper(getActivityContext()).hasDeniedForever(list);
        if (b) { // 弹出对话框 ，跳转到设置页面
            PermissionsDialog permissionsDialog = new PermissionsDialog(getActivityContext(), getRequsetCode());
            AlertDialog.Builder dialog = permissionsDialog.createDialog(appendMsg(list));
            dialog.show();
        } else { // 继续申请
            String[] perm = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                perm[i] = list.get(i);
            }
            requestPermissions(getRequsetCode(), perm);
        }
    }

    /**
     * @author stf
     * @time 2018-11-13 18:39
     * @remark 拼接提示框中的内容
     */
    private static String appendMsg(List<String> list) {
//        String msg = "在该应用设置中允许申请的权限，否则某些功能将无法正常使用";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String s = Permission.enumsPermission(list.get(i));
            sb.append(s);
        }
        String msg = sb.toString();
        Log.i("stf", "--msg-->" + msg);
        if (msg.contains("-1")) {
            msg = "请允许申请列表中所有的权限，否则某些功能将无法正常使用";
        } else {
            msg = "请允许" + msg + "权限，否则某些功能将无法正常使用";
        }
        return msg;
    }

    /**
     * @author stf
     * @time 2018-11-13 15:19
     * @remark 跳转到app权限设置页面
     */
    public static void IntentSettings(Activity activity) {
        PermissionIntentSetting.start(activity, false);
    }


    /**
     * @author stf
     * @time 2018-11-13 18:10
     * @remark 8.0的安装权限
     */
    static boolean isHasInstallPermission(Context context) {
        if (isOverOreo()) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }


    /**
     * @author  stf
     * @time    2018-11-14 09:40
     * @remark  获取自启动权限
     *
     */

    static  boolean isAutoStartApp(){
        return false;
    }


    static boolean isOverOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
