package com.investigate.newsupper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限申请工具类
 * Created by EEH on 2018/1/19.
 */

//String [] press = new String[]{"android.permission.RECORD_AUDIO",Manifest.permission.READ_EXTERNAL_STORAGE};
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";
    private static final int  REQUEST_CODE = 1;

    public static PermissionUtil.Builder with(Activity activity) {
        return new PermissionUtil.Builder(activity);
    }

    public static class Builder {

        private Activity mActivity;
        private List<String> permissionList;

        public Builder(@NonNull Activity activity) {
            mActivity = activity;
            permissionList = new ArrayList<>();
        }


        /**
         * 申请一个权限
         * @param Permission    需要申请的权限
         * @param operation     权限申请之后的回调事件
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void isPermission(String Permission, PermissionUtil.Operation operation){
            if (operation == null){
                operation = new Operation() {
                    @Override
                    public void OnNext() {

                    }
                };
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //当前系统大于等于6.0
                if (ContextCompat.checkSelfPermission(mActivity, Permission) == PackageManager.PERMISSION_GRANTED) {
                    //具有权限，直接操作
                    operation.OnNext();
                } else {
                    //不具有权限，需要进行权限申请
                    //String[] 权限的集合
                    ActivityCompat.requestPermissions(mActivity, new String[]{Permission}, REQUEST_CODE);
                }
            } else {
                operation.OnNext();
            }
        }



        /**
         * 申请多个权限
         * @param Permission    需要申请的权限的集合
         * @param operation     权限申请之后的回调事件
         */
        public void isPermissions(String[] Permission, PermissionUtil.Operation operation){
            //        String pers[] = new String[Permission.length];
            if (operation == null){
                operation = new Operation() {
                    @Override
                    public void OnNext() {

                    }
                };
            }
            int index = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i(TAG, "isPermission: 当前系统高于或等于6.0");
                for (int i = 0; i < Permission.length; i++) {
                    if (ContextCompat.checkSelfPermission(mActivity, Permission[i]) != PackageManager.PERMISSION_GRANTED) {
                        Permission[index] = Permission[i];
                        index ++;
                    }
                }
                if (index == 0){
                    //具有权限，直接操作
                    operation.OnNext();
                }else {
                    ActivityCompat.requestPermissions(mActivity, Permission, REQUEST_CODE);
                }

            } else {
                Log.i(TAG, "isPermission: 当前系统小于6.0");
                operation.OnNext();
            }
        }



    }
    /**
     * 回调接口
     */
    public interface Operation{
        void OnNext();
    }

    /**
     *没有权限的时候弹出对话框让用户去设置里开启权限
     * @param context
     * @param message  对话框提示内容
     * @param listener  点击取消的时候的响应事件
     */
    public static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener listener) {
        if (message.equals("")){
            message = "请前往设置界面打开相应权限！";
        }
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",listener)
                .create()
                .show();

    }


    public static String getPremissionName(String permission){
        String premissionName = "";
        switch (permission) {
            case "android.permission.WRITE_CONTACTS":
            case "android.permission.GET_ACCOUNTS":
            case "android.permission.READ_CONTACTS":
                premissionName = "联系人";
                break;
            case "android.permission.READ_CALL_LOG":
            case "android.permission.READ_PHONE_STATE":
            case "android.permission.CALL_PHONE":
            case "android.permission.WRITE_CALL_LOG":
            case "android.permission.USE_SIP":
            case "android.permission.PROCESS_OUTGOING_CALLS":
            case "com.android.voicemail.permission.ADD_VOICEMAIL":
                premissionName = "电话";
                break;
            case "android.permission.READ_CALENDAR":
            case "android.permission.WRITE_CALENDAR":
                premissionName = "手机时间";
                break;
            case "android.permission.CAMERA":
                premissionName = "相机";
                break;
            case "android.permission.BODY_SENSORS":
                premissionName = "传感器";
                break;
            case "android.permission.ACCESS_FINE_LOCATION":
            case "android.permission.ACCESS_COARSE_LOCATION":
                premissionName = "定位";
                break;
            case "android.permission.READ_EXTERNAL_STORAGE":
            case "android.permission.WRITE_EXTERNAL_STORAGE":
                premissionName = "数据存储";
                break;
            case "android.permission.RECORD_AUDIO":
                premissionName = "麦克风";
                break;
            case "android.permission.READ_SMS":
            case "android.permission.RECEIVE_WAP_PUSH":
            case "android.permission.RECEIVE_MMS":
            case "android.permission.RECEIVE_SMS":
            case "android.permission.SEND_SMS":
            case "android.permission.READ_CELL_BROADCASTS":
                premissionName = "短信";
                break;
            default:
                premissionName = "相关";
                break;
        }
        return premissionName;
    }

}
