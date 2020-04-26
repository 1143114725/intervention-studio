package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.UploadActivity;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.Util;

public class UploadAdapter extends BaseAdapter {
	private String TAG;
	private ArrayList<Survey> ss;
	private LayoutInflater inflater;

	private MyApp ma;
	private Context context;
	private int width, height;

	@Override
	public int getCount() {
		return ss.size();
	}

	// 刷新
	public void refresh(ArrayList<Survey> surveys) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(ss)) {
				ss.clear();
				ss.addAll(surveys);
			}
			notifyDataSetChanged();
		}
	}

	// check状态
	public void check(boolean isAll) {
		if (null != ss) {
			if (isAll) {
				for (Survey survey : ss) {
					survey.setIsCheck(1);
				}
			} else {
				for (Survey survey : ss) {
					survey.setIsCheck(0);
				}
			}
			notifyDataSetChanged();
		}
	}

	public boolean updateCheck() {
		for (Survey survey : ss) {
			if (1 != survey.getIsCheck()) {
				return false;
			}
		}
		return true;
	}

	// 获得所有问卷状态
	public ArrayList<Survey> getAllSurvey() {
		ArrayList<Survey> sList = new ArrayList<Survey>();
		for (Survey s : ss) {
			int isCheck = s.getIsCheck();
			if (1 == isCheck) {
				sList.add(s);
			}
		}
		return sList;
	}

	public UploadAdapter(Context _c, MyApp mApp, ArrayList<Survey> surveys,
			String TAG) {
		this.ma = mApp;
		this.ss = surveys;
		this.context = _c;
		this.TAG = TAG;
		inflater = (LayoutInflater) _c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
	}

	@Override
	public Object getItem(int position) {
		return ss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		vh = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.upload_item, null);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.iv = (CheckBox) convertView.findViewById(R.id.rbSelect);
		// LinearLayout.LayoutParams prams = new
		// LinearLayout.LayoutParams(width/20, height/35);
		// vh.iv.setLayoutParams(prams);
		vh.tvIns = (UITextView) convertView.findViewById(R.id.tvMsg);
		vh.tvSurveyTitle = (UITextView) convertView.findViewById(R.id.tvTitle);
		vh.tvUnUpload = (Button) convertView.findViewById(R.id.btn_num);
		vh.upload_ll = (LinearLayout) convertView
				.findViewById(R.id.upload_layout);
		Survey s = ss.get(position);
		if (null != s) {
			if (Util.isEmpty(ma.userId)) {
				ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(context))
						: (ma.cfg)).getString("UserId", "");
			}
			/**
			 * 已完成的(包含上报和未上报的)
			 */
			long c = ma.dbService.feedCompletedCount(s.surveyId, ma.userId);
			/**
			 * 已完成未上报的
			 */
			long u = ma.dbService.feedUnUploadCounts(s.surveyId, ma.userId);
			vh.tvUnUpload.setText(u + "");

			vh.tvSurveyTitle.setText(Html.fromHtml(s.surveyTitle
					+ "<font color='#1576ce'><b>(" + c + ")</b></font>"));
			/**
			 * 有已完成但未上报的记录
			 */
			int isCheck = s.getIsCheck();
			if (1 == isCheck) {
				vh.iv.setChecked(true);
				// vh.iv.setBackgroundResource(R.drawable.icon_upload_select);
			} else {
				// vh.iv.setChecked(false);
			}

			if (0 < u) {
				vh.tvIns.setText(R.string.unupload_num);
			} else {
				vh.tvIns.setText(R.string.uploaded_all);
			}
			// vh.iv.setOnClickListener(new ivClickListener(s));
			// 大树 访问前说明
			// if (!Util.isEmpty(s.getWord())) {
			// Spannable sp = (Spannable) Html.fromHtml(s.getWord());
			// vh.tvIns.setText(sp.toString());
			// } else {
			// vh.tvIns.setText(R.string.no_explain);
			// }
			// 将来可能有用
			// if (0 < u) {
			//
			// } else {
			//
			// }
		}
		vh.upload_ll.setOnClickListener(new ivClickListener(s) {

		});
		TextSizeManager.getInstance().addTextComponent(TAG, vh.tvIns)
				.addTextComponent(TAG, vh.tvSurveyTitle);

		return convertView;
	}

	private final class ViewHolder {
		/**
		 * listviewitem
		 */
		private LinearLayout upload_ll;
		/**
		 * 项目名称
		 */
		private UITextView tvSurveyTitle;
		/**
		 * 未上报
		 */
		private Button tvUnUpload;
		/**
		 * 说明
		 */
		private UITextView tvIns;
		/**
		 * 选择项目按钮
		 */
		private CheckBox iv;
	}

	boolean isAll = false;

	class ivClickListener implements OnClickListener {
		private Survey survey;

		public ivClickListener(Survey s) {
			this.survey = s;
		}

		@Override
		public void onClick(View v) {
			LinearLayout llLayout = (LinearLayout) v;
			int isCheck = survey.getIsCheck();
			if (1 == isCheck) {
				survey.setIsCheck(0);
				isAll = updateCheck();
				if (llLayout.getChildAt(1) instanceof CheckBox) {
					CheckBox iView = (CheckBox) llLayout.getChildAt(1);
					iView.setChecked(false);
					// iView.setBackgroundResource(R.drawable.icon_upload_normal);
				}
			} else {
				survey.setIsCheck(1);
				isAll = updateCheck();
				if (llLayout.getChildAt(1) instanceof CheckBox) {
					CheckBox iView = (CheckBox) llLayout.getChildAt(1);
					iView.setChecked(true);
					// iView.setBackgroundResource(R.drawable.icon_upload_select);

				}
			}
			((UploadActivity) context).updateCheck(isAll);
		}

	}

}
