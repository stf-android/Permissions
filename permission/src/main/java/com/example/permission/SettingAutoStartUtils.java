package com.example.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by stf on 2019-12-23.
 */

public class SettingAutoStartUtils {

    private Activity mActivity;
    private final static int requestCode = 96;

    public SettingAutoStartUtils() throws Exception {
        throw new Exception("该方法不能调用");
    }

    public SettingAutoStartUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }


    public PhoneSysBean testPhoneSys() {
        String name = Build.BRAND.toLowerCase();
        PhoneSysBean phoneSysBean = new PhoneSysBean();
        switch (name) {
            case "huawei":
            case "honor":
                phoneSysBean.setSysName("huawei");
                phoneSysBean.setSysMsg("应用启动管理 -> 关闭应用开关 -> 打开允许自启动");
                break;
            case "xiaomi":
                phoneSysBean.setSysName("xiaomi");
                phoneSysBean.setSysMsg("授权管理 -> 自启动管理 -> 允许应用自启动");
                break;
            case "oppo":
                phoneSysBean.setSysName("smartisan");
                phoneSysBean.setSysMsg("权限隐私 -> 自启动管理 -> 允许应用自启动");
                break;
            case "vivo":
                phoneSysBean.setSysName("vivo");
                phoneSysBean.setSysMsg("权限管理 -> 自启动 -> 允许应用自启动");
                break;
            case "meizu":
                phoneSysBean.setSysName("meizu");
                phoneSysBean.setSysMsg("权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行");
                break;
            case "samsung":
                phoneSysBean.setSysName("samsung");
                phoneSysBean.setSysMsg("自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用");
                break;
            case "letv":
                phoneSysBean.setSysName("oppo");
                phoneSysBean.setSysMsg("自启动管理 -> 允许应用自启动");
                break;
            case "smartisan":
                phoneSysBean.setSysName("smartisan");
                phoneSysBean.setSysMsg("权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动");
                break;
            default:
                phoneSysBean.setSysName("other");
                phoneSysBean.setSysMsg("在设置中将App设为自动");
                break;
        }

        return phoneSysBean;
    }

    public void goSetting() {
        String name = Build.BRAND.toLowerCase();
        switch (name) {
            case "huawei":
            case "honor":
                goHuaweiSetting();
                break;
            case "xiaomi":
                goXiaomiSetting();
                break;
            case "oppo":
                goOPPOSetting();
                break;
            case "vivo":
                goVIVOSetting();
                break;
            case "meizu":
                goMeizuSetting();
                break;
            case "samsung":
                goSamsungSetting();
                break;
            case "letv":
                goLetvSetting();
                break;
            case "smartisan":
                goSmartisanSetting();
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestIgnoreBatteryOptimizations();
                }
                break;
        }
    }

    public static String getBrandName() {
        return Build.BRAND.toLowerCase();
    }

    //跳转华为手机管家的启动管理页：
    public void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    //代码跳转小米安全中心的自启动管理页面：
    private void goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    //代码跳转 OPPO 手机管家：
    private void goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter");
                }
            }
        }
    }

    //代码跳转 VIVO 手机管家：
    private void goVIVOSetting() {
        showActivity("com.iqoo.secure");
    }

    //代码跳转魅族手机管家：
    private void goMeizuSetting() {
        showActivity("com.meizu.safe");
    }

    //代码跳转三星智能管理器：
    private void goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity("com.samsung.android.sm");
        }
    }

    //代码跳转乐视手机管家：
    private void goLetvSetting() {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity");
    }

    //代码跳转手机管理：
    private void goSmartisanSetting() {
        showActivity("com.smartisanos.security");
    }

    /**
     * 跳转到指定应用的首页
     */
    public void showActivity(@NonNull String packageName) {
        if (mActivity == null) {
            return;
        }
        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
        mActivity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    public void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        if (mActivity == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivityForResult(intent, requestCode);
    }


    //判断是否在白名单中
    //<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isIgnoringBatteryOptimizations() {
        Log.i("stf", "--isIgnoringBatteryOptimizations-mActivity->" + mActivity);
        if (mActivity == null) {
            return true;
        }

        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        Log.i("stf", "--isIgnoringBatteryOptimizations-powerManager->" + powerManager);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(mActivity.getPackageName());
        }
        Log.i("stf", "--isIgnoringBatteryOptimizations-isIgnoring->" + isIgnoring);
        return isIgnoring;
    }


    //不在白名单中申请加入白名单
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            if (mActivity == null) {
                return;
            }

            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
            mActivity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得各厂商的launcher
    public static String getLauncherPackageName(Context context) {
        //获取ApplicationContext
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed.
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    public class PhoneSysBean implements Serializable {
        private String sysName;
        private String sysMsg;
        private String sysTemmanager;
        private String sysActivityDir;
        private String sysActivityDir2;

        public String getSysActivityDir2() {
            return sysActivityDir2;
        }

        public void setSysActivityDir2(String sysActivityDir2) {
            this.sysActivityDir2 = sysActivityDir2;
        }

        public String getSysTemmanager() {
            return sysTemmanager;
        }

        public void setSysTemmanager(String sysTemmanager) {
            this.sysTemmanager = sysTemmanager;
        }

        public String getSysActivityDir() {
            return sysActivityDir;
        }

        public void setSysActivityDir(String sysActivityDir) {
            this.sysActivityDir = sysActivityDir;
        }

        public String getSysName() {
            return sysName;
        }

        public void setSysName(String sysName) {
            this.sysName = sysName;
        }

        public String getSysMsg() {
            return sysMsg;
        }

        public void setSysMsg(String sysMsg) {
            this.sysMsg = sysMsg;
        }
    }
}
