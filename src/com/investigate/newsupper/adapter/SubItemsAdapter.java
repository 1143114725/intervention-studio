package com.investigate.newsupper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.base.MyBaseAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;


/**
 * 项目列表 的adapter
 * Created by EEH on 2018/2/2.
 */

public class SubItemsAdapter extends MyBaseAdapter<Survey,ListView> {
	public String language;
	MyApp ma;
	private ArrayList<UploadFeed> fs;
    public SubItemsAdapter(Context context, List<Survey> list) {
        super(context, list);
        language = context.getResources().getConfiguration().locale.getLanguage();
        ma = (MyApp)((Activity) context).getApplication();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_subitemlist, null);
            viewHoder = new ViewHoder(convertView);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        final Survey s = list.get(position);
		fs = ma.dbService.getAllXmlUploadFeedList(s.surveyId,ma.userId);
        //第一列
        viewHoder.surveyname.setText(list.get(position).getSurveyTitle());
        //第二列
        viewHoder.auploaded.setText(getUploadedCount()+"");
        //第三列
        viewHoder.tuploaded.setText(getUnUploadedCount()+"");
//        viewHoder.ll_item_rootll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //intent跳转页面
//            	
//            	
//            	Intent it = new Intent();
//				Bundle bundle = new Bundle();
//				if ("zh".equals(language)) {
//					// language
//					it.setClass(context, VisitActivity.class);
//					s.mapType = "Baidu";
//				} else {
//					it.setClass(context, VisitActivity.class);
//					s.mapType = "Baidu";
//				}
//				bundle.putSerializable("s", s);
//				bundle.putString("type", "1");
////				Toast.makeText(context, "getIntervalTime"+s.getIntervalTime(), Toast.LENGTH_SHORT).show();
//				it.putExtras(bundle);
//				context.startActivity(it);
//				((Activity) context).overridePendingTransition(R.anim.zzright, R.anim.zzleft);
//            }
//        });


        return convertView;
    }


    private ViewHoder viewHoder;

    private class ViewHoder {
        private TextView surveyname;// 名字
        private TextView auploaded;// 已上传数量
        private TextView tuploaded; // 未上传数量
        private LinearLayout ll_item_rootll;

        public ViewHoder(View convertView) {
            super();
            this.surveyname = (TextView) convertView.findViewById(R.id.surveyname);
            this.auploaded = (TextView) convertView.findViewById(R.id.auploaded);
            this.tuploaded = (TextView) convertView.findViewById(R.id.tuploaded);
            this.ll_item_rootll = (LinearLayout) convertView.findViewById(R.id.ll_item_rootll);
        }
    }
    
    
    /**
     * 获得已上传数据
     * @return
     */
    public int getUploadedCount() {
    	
		int counts = 0;
		for (UploadFeed uf : fs) {
			if (9 == uf.getIsUploaded()) {
				counts++;
			}
		}
		return counts;
	}
    
    /**
	 * 未上传的数据
	 * 
	 * @return
	 */
	public int getUnUploadedCount() {
		return getCompletedCount() - getUploadedCount();
	}
	/**
	 * 获取所有数据
	 * @return
	 */
	public int getCompletedCount() {
		int counts = 0;
		for (UploadFeed uf : fs) {
			if (1 == uf.getIsCompleted()) {
				counts++;
			}
		}
		return counts;
	}
	
	public void updateListView(List<Survey> list) {
		// TODO Auto-generated method stub
		super.updateListView(list);
	}
}
