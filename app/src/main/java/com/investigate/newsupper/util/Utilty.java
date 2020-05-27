package com.investigate.newsupper.util;

import com.investigate.newsupper.global.MyApp;

import android.text.TextUtils;

public final class Utilty {
	
	private Utilty() {
	} 
	
	public static int str2Int(String number) {
		if (TextUtils.isEmpty(number)) {
			return -1;
		}
		int result = -1;
		try {
			result = Integer.valueOf(number);
		} catch (Exception e) {
			if (MyApp.DEBUG) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
