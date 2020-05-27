package com.investigate.newsupper.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtil {
	/**
     * 设置listview高度的方法
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i  = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //累加不解释
            System.out.println("syso:外部每个item的高度"+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离
        
        listView.setLayoutParams(params);
    }
}
