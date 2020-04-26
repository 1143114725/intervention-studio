/**
 *
 */
package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.RecordActivity;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.util.Util;

/**
 * 张树锦与2016-1-27下午5:08:11TODO
 */
public class RecordAdapter extends BaseAdapter{
	private ArrayList<UploadFeed> vector;
	private LayoutInflater inflater;

	
	public RecordAdapter(RecordActivity _c ,ArrayList<UploadFeed> v) {
		this.vector = v;
		inflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public Object getItem(int position) {
		return vector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.visit_item, null);
			vh = new ViewHolder();
			vh.tvLocalId = (TextView) convertView.findViewById(R.id.local_id);
			vh.tvEndTime = (TextView) convertView.findViewById(R.id.end_time_tv);
			vh.tvState = (TextView) convertView.findViewById(R.id.visit_state_tv);
			
			vh.innerLL = (LinearLayout) convertView.findViewById(R.id.innerLL);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		UploadFeed feed = vector.get(position);
		if(feed!=null){
			vh.tvLocalId.setText(String.valueOf(feed.getId()));
			vh.tvEndTime.setText(0 < feed.getRegTime() ? Util.getTime(feed.getRegTime(), 7) : "");
			vh.tvState.setText("点击播放");
			vh.innerLL.setVisibility(View.GONE);
		}
		return convertView;
	}
	static class ViewHolder {
		/**
		 * 项目Id
		 */
		private TextView tvLocalId;
		/**
		 * 问卷的结束时间
		 */
		private TextView tvEndTime;

		/**
		 * 已完成
		 */
		private TextView tvState;
		
		
		private LinearLayout innerLL;// 内部名单布局

		
	}
	
}
