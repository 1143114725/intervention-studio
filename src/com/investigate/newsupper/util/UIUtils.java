package com.investigate.newsupper.util;

import com.investigate.newsupper.global.MyApp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Toast;

public class UIUtils {

    /**
     * Private constructor
     */
    private UIUtils() {
    }
    
    public static void showToast(Context ctx, int msgResId, Object... formatArgs) {
        showToast(ctx, getString(msgResId, formatArgs));
    }
    
    public static void showToast(Context ctx, int msgResId) {
        showToast(ctx, getString(msgResId));
    }
    
    public static void showLongToast(Context ctx, int msgResId, Object... formatArgs) {
        showLongToast(ctx, getString(msgResId, formatArgs));
    }
    
    public static void showLongToast(Context ctx, int msgResId) {
        showLongToast(ctx, getString(msgResId));
    }

    private static void showToast(Context ctx, CharSequence msg, int duration) {
        if (ctx == null || TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(ctx, msg, duration).show();
    }

    public static void showToast(Context ctx, CharSequence msg) {
        showToast(ctx, msg, Toast.LENGTH_SHORT);
    }
    
    public static void showLongToast(Context ctx, CharSequence msg) {
        showToast(ctx, msg, Toast.LENGTH_LONG);
    }
    
    public static int getColor(int resId) {
        return MyApp.getAppContext().getResources().getColor(resId);
    }
    
    public static ColorStateList getColorStateList(int resId) {
        return MyApp.getAppContext().getResources().getColorStateList(resId);
    }
    
    public static String getString(int resId) {
        return MyApp.getAppContext().getString(resId);
    }
    
    public static String getString(int resId, Object... formatArgs) {
        return MyApp.getAppContext().getString(resId, formatArgs);
    }
    
    public static String[] getStringArray(int resId) {
        return MyApp.getAppContext().getResources().getStringArray(resId);
    }
    
    public static int getDimenPixelSize(int resId) {
        return MyApp.getAppContext().getResources().getDimensionPixelSize(resId);
    }
    
    public static int getInteger(int resId) {
    	return MyApp.getAppContext().getResources().getInteger(resId);
    }
    
    public static Drawable getDrawable(int resId) {
        return MyApp.getAppContext().getResources().getDrawable(resId);
    }
    
    public static int pixel2Dimen(int pix) {
    	return (int) (pix / MyApp.getAppContext().getResources().getDisplayMetrics().density);
    }
}
