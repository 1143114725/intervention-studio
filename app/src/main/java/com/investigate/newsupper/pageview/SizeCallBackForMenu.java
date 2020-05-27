package com.investigate.newsupper.pageview;

import android.content.Context;
import android.provider.ContactsContract.Contacts;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SizeCallBackForMenu implements SizeCallBack {

	private LinearLayout menu;
	private int menuWidth;
	int morePx;

	public SizeCallBackForMenu(LinearLayout menu, int morePx) {
		super();
		this.morePx = morePx;
		this.menu = menu;
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		this.menuWidth = morePx;
	}

	@Override
	public void getViewSize(int idx, int width, int height, int[] dims) {
		// TODO Auto-generated method stub
		dims[0] = width;
		dims[1] = height;

		/* 视图不是中间视图 */
		if (idx != 1) {
			dims[0] = width - this.menuWidth;
		}
	}
}
