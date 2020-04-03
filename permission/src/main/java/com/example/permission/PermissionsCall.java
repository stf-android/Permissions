package com.example.permission;

import java.util.List;

/**
 * Created by stf on 2018-11-13.
 */

public interface PermissionsCall {
    void errorRequest(String errorMsg);

    void granted();

    void denideList(List<String> list);
}
