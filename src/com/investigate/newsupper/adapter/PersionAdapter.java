package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.base.MyBaseAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UserList;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.Util;


/**
 * 人员列表adapter
 * Created by EEH on 2018/3/28.
 */

public class PersionAdapter extends MyBaseAdapter<UserList,ListView> {
	private MyApp ma;
    public PersionAdapter(Context context, ArrayList<UserList> list,MyApp ma) {
        super(context, list);
        this.ma = ma;
    }
    private UserList userbean;

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_persion, null);
            viewHoder = new ViewHoder(convertView);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        userbean = list.get(position);
        viewHoder.pid.setText(userbean.getPanelID());
        viewHoder.name.setText(userbean.getUserName());
        viewHoder.tel.setText(userbean.getPhone());
        System.out.println("renyuanliebiao ==list"+list.size());
        System.out.println("renyuanliebiao ==position"+position+userbean.toString());
        String BackReturnType = userbean.getBackReturnType();
        String type = "";
        if (BackReturnType.equals("0")) {
        	//未访问
        	type = "未访问";
		}else if (BackReturnType.equals("1")) {
			//条件不符
			type = "条件不符";
		}else if (BackReturnType.equals("2")) {
			//访问成功
			type = "访问成功";
		}else if (BackReturnType.equals("3")) {
			//访问成功
			type = "中断待续";
		}
        
        
        if (!Util.isEmpty(userbean.getBackSurveyID())) {
        	Survey s = ma.dbService.getSurvey(userbean.getBackSurveyID());
        	if (!Util.isEmpty(s.getSurveyTitle()+type)) {
        		viewHoder.nowstate.setText(s.getSurveyTitle()+"--"+type);
			}else {
				viewHoder.nowstate.setText("");
			}
        	if (!Util.isEmpty(userbean.getNextSurveyName())) {
        		viewHoder.nextproject.setText(userbean.getNextSurveyName());
			}else {
				viewHoder.nextproject.setText("");
			}
		}
        
        
        return convertView;
    }


    private ViewHoder viewHoder;

    private class ViewHoder {
        private TextView pid;// 名字
        private TextView name;// 已上传数量
        private TextView tel; // 未上传数量
        private TextView nowstate; // 未上传数量
        private TextView nextproject; // 未上传数量

        public ViewHoder(View convertView) {
            super();
            this.pid = (TextView) convertView.findViewById(R.id.tv_item_pid);
            this.name = (TextView) convertView.findViewById(R.id.tv_item_name);
            this.tel = (TextView) convertView.findViewById(R.id.tv_item_tel);
            this.nowstate = (TextView) convertView.findViewById(R.id.tv_item_nowstate);
            this.nextproject = (TextView) convertView.findViewById(R.id.tv_item_nextproject);
        }
    }

}
