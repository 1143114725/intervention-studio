package com.investigate.newsupper.util;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 使用本类时，请在当前Application的onCreate()方法中，
 * 调用本类的init(Context context)方法
 * @author 胡事成
 */
public final class SharedPreferencesManager {
	
	private static SharedPreferences sPreferences;
	
	private SharedPreferencesManager() {
	}
	
	public static void init(Context context) {
		sPreferences = context.getSharedPreferences("SharedPreferencesManager", Context.MODE_PRIVATE);
	}
	
	public static void putBooleanAsync(String key, boolean value) {
		sPreferences.edit().putBoolean(key, value).apply();
	}
	
	public static void putBooleanSync(String key, boolean value) {
		sPreferences.edit().putBoolean(key, value).commit();
	}
	
	public static void putIntAsync(String key, int value) {
		sPreferences.edit().putInt(key, value).apply();
	}
	
	public static void putIntSync(String key, int value) {
		sPreferences.edit().putInt(key, value).commit();
	}
	
	public static void putStringAsync(String key, String value) {
		sPreferences.edit().putString(key, value).apply();
	}
	
	public static void putStringSync(String key, String value) {
		sPreferences.edit().putString(key, value).commit();
	}
	
	public static void putAsync(String key, long value) {
		sPreferences.edit().putLong(key, value).apply();
	}
	
	public static void putLongSync(String key, long value) {
		sPreferences.edit().putLong(key, value).commit();
	}
	
	public static void putFloatAsync(String key, float value) {
		sPreferences.edit().putFloat(key, value).apply();
	}
	
	public static void putFloatSync(String key, float value) {
		sPreferences.edit().putFloat(key, value).commit();
	}
	
	public static void putStringSetSync(String key, Set<String> values) {
		sPreferences.edit().putStringSet(key, values).commit();
	}
	
	public static void putStringSetAsync(String key, Set<String> values) {
		sPreferences.edit().putStringSet(key, values).apply();
	}
	
	public static String getString(String key, String defValue) {
		return sPreferences.getString(key, defValue);
	}
	
	public static int getInt(String key, int defValue) {
		return sPreferences.getInt(key, defValue);
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		return sPreferences.getBoolean(key, defValue);
	}
	
	public static float getFloat(String key, float defValue) {
		return sPreferences.getFloat(key, defValue);
	}
	
	public static float getLong(String key, long defValue) {
		return sPreferences.getLong(key, defValue);
	}
	
	public static Set<String> getStringSet(String key, Set<String> defValues) {
		return sPreferences.getStringSet(key, defValues);
	}
}
