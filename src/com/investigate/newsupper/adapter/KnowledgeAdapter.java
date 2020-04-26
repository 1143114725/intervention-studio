package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.Knowledge;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Util;

public class KnowledgeAdapter extends BaseAdapter{
	
	private static final String TAG = KnowledgeAdapter.class.getSimpleName();
	
	private ArrayList<Knowledge> list;
	private Context context;
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	public void refresh() {
		notifyDataSetChanged();
	}
	
	public void refresh(ArrayList<Knowledge> knowArrayList) {
		if (!Util.isEmpty(knowArrayList)) {
			if (!Util.isEmpty(list)) {
				list.clear();
				list.addAll(knowArrayList);
			}
			notifyDataSetChanged();
		}
	}

	public KnowledgeAdapter(ArrayList<Knowledge> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Knowledge knowledge = list.get(position);		
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.knowledge_item, null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);			
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		// 大树知识库   获取附件数量  
		int accessory_num=0;
		if (knowledge.getAttach().contains(";")) {
			accessory_num=knowledge.getAttach().split(";").length;
		}
		// 
		viewHolder.btn_num.setText(accessory_num+"");
		viewHolder.tvId.setText(knowledge.getId());
		viewHolder.tvTitle.setText(knowledge.getTitle());
		viewHolder.tvContent.setText(Html.fromHtml(knowledge.getContent()));
		
		return convertView;
	}
	private ViewHolder viewHolder;
	
	
	private class ViewHolder {
		
		private UITextView tvId; // 知识库ID  
		
		private UITextView tvTitle;  //知识库标题 
		
		private UITextView tvContent; // 知识库内容
		
		private Button btn_num;  // 知识库附件个数 

		public ViewHolder(View convertView) {
			super();
			this.tvId = (UITextView) convertView.findViewById(R.id.tvId);
			TextSizeManager.getInstance().addTextComponent(TAG, this.tvId);
			this.tvTitle = (UITextView) convertView.findViewById(R.id.tvTitle);
			TextSizeManager.getInstance().addTextComponent(TAG, this.tvTitle);
			this.tvContent = (UITextView) convertView.findViewById(R.id.tvContent);
			TextSizeManager.getInstance().addTextComponent(TAG, this.tvContent);
			this.btn_num = (Button) convertView.findViewById(R.id.btn_num);
		}					
		
	}
	

}
