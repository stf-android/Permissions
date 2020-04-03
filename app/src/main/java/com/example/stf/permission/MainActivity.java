package com.example.stf.permission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.permission.PermissionsCall;
import com.example.permission.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int requestCode = 111;
    private static final String TAG = "stf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickListener(View view) {
        applyPermissions();
    }

    public void applyPermissions() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.add(Manifest.permission.CAMERA);
        list.add(Manifest.permission.CALL_PHONE);
        list.add(Manifest.permission.READ_PHONE_STATE);
        //        list.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        PermissionsUtils.with(MainActivity.this)
                .permissions(list)
                .request(requestCode, new PermissionsCall() {
                    @Override
                    public void errorRequest(String errorMsg) {
                        Log.i(TAG, "errorRequest: " + errorMsg);
                    }

                    @Override
                    public void granted() {
                        Log.i(TAG, "granted: 全部通过");
                        Toast.makeText(MainActivity.this, "全部通过", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void denideList(List<String> list) {
                        Log.i(TAG, "denideList: " + list);
                        Toast.makeText(MainActivity.this, "部分权限未通过,允许后重试", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
