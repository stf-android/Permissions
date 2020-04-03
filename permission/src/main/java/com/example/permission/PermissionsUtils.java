package com.example.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by stf on 2018-11-13.
 */

public class PermissionsUtils implements PermissionsImpl {
    private Activity activity;
    private PermissionsCall permissionimp;
    private List<String> mPermissions = null;

    public PermissionsUtils(Activity activity) {
        this.activity = activity;
        mPermissions = new ArrayList<>();
    }

    /**
     * @author stf
     * @time 2018-11-13 15:57
     * @remark 设置请求对象
     */
    public static PermissionsUtils with(Activity activity) {
        return new PermissionsUtils(activity);
    }

    /**
     * @author stf
     * @time 2018-11-13 15:58
     * @remark 设置权限组
     */
    public PermissionsUtils permissions(List<String> permList) {
        mPermissions.addAll(permList);
        return this;
    }


    public PermissionsUtils permissions(String[]... permissions) {
        for (String[] group : permissions) {
            mPermissions.addAll(Arrays.asList(group));
        }
        return this;
    }

    public PermissionsUtils permissions(String permissions) {
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    /**
     * @author stf
     * @time 2018-11-13 16:03
     * @remark 开始申请权限
     */
    public void request(int requestCode, final PermissionsCall permissionimp) {
        this.permissionimp = permissionimp;
        if (mPermissions != null && mPermissions.size() == 0) {
            permissionimp.errorRequest("无权限可申请");
            return;
        }

        if (activity == null) {
            permissionimp.errorRequest("传入的activity为空");
            return;
        }

        PermissionManger.getInstance(activity);

        String[] perms = new String[mPermissions.size()];
        for (int i = 0; i < mPermissions.size(); i++) {
            perms[i] = mPermissions.get(i);
        }
        mPermissions.clear();
        mPermissions = null;

        Boolean aBoolean = PermissionManger.hasPermission(perms); //判断是否拥有权限
        Log.i("stf", "--是否用权限-->" + aBoolean);
        if (PermissionManger.hasPermission(perms)) {
            permissionimp.granted();//拥有权限
        } else {
            PermissionManger.requestPermissions(requestCode, perms); //无权限，去申请
            PermissionManger.setPermissionCall(permissionimp);// 注册回调接口
            PermissionManger.setPermissionsImpl(this);// 注册回调接口
        }
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManger.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void denideList(List<String> list) {
        if (permissionimp != null) {
            PermissionManger.hasDeniedForever(list);
            permissionimp.denideList(list);
        }
    }
}
