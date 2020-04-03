package com.example.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by stf on 2018-11-13.
 */

public class PermissionsDialog {
    private Activity activity;
    private int requsetCode;


    public PermissionsDialog(Activity activity, int requsetCode) {
        this.activity = activity;
        this.requsetCode = requsetCode;
    }


    public AlertDialog.Builder createDialog(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("设置权限")
                .setMessage(msg)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                        activity.startActivityForResult(intent, requsetCode);
//                        PermissionIntentSetting.start(activity, false);
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        return dialog;
    }

    public void showDialog(AlertDialog.Builder dialog) {
        dialog.show();
    }
}
