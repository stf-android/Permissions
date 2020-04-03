package com.example.permission;

/**
 * Created by stf on 2018-11-13.
 * 动态申请的权限没有在清单文件中注册会抛出的异常
 */

public class ManifestRegisterException extends RuntimeException {
    ManifestRegisterException(String permission) {
        super(permission == null ?
                "该权限没有在manifest中注册" :
                (permission + ": Permissions are not registered in the manifest file"));
    }
}
