package com.investigate.newsupper.sohuutils;

import android.content.Intent;

/**
 * @version 1.0
 * @author: mj
 * @date:2018/4/9 下午4:41
 * 描述：
 */
public interface StartActivityForResultCallback {
    void callback(int requestCode, int resultCode, Intent data);
}
