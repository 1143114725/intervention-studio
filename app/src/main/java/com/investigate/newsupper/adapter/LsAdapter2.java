package com.investigate.newsupper.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.base.MyBaseAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.util.Util;


/**
 * Created by EEH on 2018/5/16.
 */
public class LsAdapter2 extends MyBaseAdapter<Survey,ListView> {
   

    public LsAdapter2(Context context, List<Survey> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_full_conent, null);
            viewHoder = new ViewHoder(convertView);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        Survey s = list.get(position);
        viewHoder.name.setText(s.surveyTitle);
        
        
        
		//这个项目我下载过 显示最近更新时间
		if (null != s && 1 == s.isDowned) {
			if (!Util.isEmpty(s.getLastGeneratedTime())) {
				viewHoder.data.setVisibility(View.VISIBLE);
				viewHoder.data.setText("最近更新：" + s.getLastGeneratedTime());
				s.isUpdate = 1;
			} else {
				viewHoder.data.setVisibility(View.GONE);
			}

		} else {
			s.isUpdate = 0;
			viewHoder.data.setVisibility(View.GONE);
		}
        
        if (!Util.isEmpty(s.getGeneratedTime())) {
        	viewHoder.time.setText("最新发布：" + s.getGeneratedTime());
		}
      //详细说明
		if (!Util.isEmpty(s.getWord())) {
			Spannable sp = (Spannable) Html.fromHtml(s.getWord());
			viewHoder.instructions.setText(sp.toString());
		} else {
			viewHoder.instructions.setText(R.string.no_explain);
		}
        return convertView;
    } 


    private ViewHoder viewHoder;

    private class ViewHoder {
        private TextView name,data,time,instructions;// 名字

        public ViewHoder(View convertView) {
            super();
            this.name = (TextView) convertView.findViewById(R.id.textView1);
            this.data = (TextView) convertView.findViewById(R.id.textView4);
            this.time = (TextView) convertView.findViewById(R.id.textView2);
            this.instructions = (TextView) convertView.findViewById(R.id.textView3);
        }
    }
}
