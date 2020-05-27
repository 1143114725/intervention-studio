package com.investigate.newsupper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.Util;

/**
 * 观看答卷的webview界面
 * 
 * @author EraJieZhang
 * @data 2018/11/6
 */
public class SurveyViewAnswerActivity extends Activity {

	private WebView webView;
	private String feedId;
	private String surveyID;

	/**
	 * 标题栏
	 */
	private LinearLayout visit_left_iv;
	private UITextView tvSurveyTile;
	private TextView refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_layout);

		webView = (WebView) findViewById(R.id.web_view);
		feedId = getIntent().getStringExtra("fid");
		surveyID = getIntent().getStringExtra("sid");
		BaseLog.w("feedId = " + feedId);
		BaseLog.w("surveyID = " + surveyID);

		String stringid = "数据异常！";
		if (Util.isEmpty(feedId)) {
			BaseToast.showLongToast(this, stringid);
			finish();
		}
		if (Util.isEmpty(surveyID)) {
			BaseToast.showLongToast(this, stringid);
			finish();
		}
		
		refresh = (TextView)findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initdata(feedId, surveyID);
			}
		});
		visit_left_iv = (LinearLayout) findViewById(R.id.visit_left_iv);
		//标题tv
		tvSurveyTile = (UITextView) findViewById(R.id.visit_survey_name_tv);
//		tvSurveyTile.setText("546465463546546");
		visit_left_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SurveyViewAnswerActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
			}
		});
		initdata(feedId, surveyID);
	}

	/**
	 * 设置一个webview
	 * 
	 * @param fid
	 *            Feedd卷号
	 * @param sid
	 *            SurveyId问卷编号
	 */
	public void initdata(String fid, String sid) {

		String url = "http://www.survey-expert.cn/newsurvey/surveyViewAnswer.asp?au_id="
				+ fid + "&surveyID=" + sid + "";
		BaseLog.w("loadurl is =" + url);
		webView.loadUrl(url);
		
		/*加载标题*/
		webView.setWebChromeClient(new WebChromeClient(){

		    @Override
		    public void onReceivedTitle(WebView view, String title) {
		    	tvSurveyTile.setText(title);
		    }
		});
		
		WebSettings webSettings = webView.getSettings();
		/* 设置支持javascript脚本 */
		webSettings.setJavaScriptEnabled(true);
		/* 设置可以支持缩放 */
		webSettings.setSupportZoom(true);
		/* 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false */
		webSettings.setBuiltInZoomControls(true);
		/* 隐藏缩放工具 */
		webSettings.setDisplayZoomControls(false);
		/* 扩大比例的缩放 */
		webSettings.setUseWideViewPort(true);
		/* 自适应屏幕 */
		webSettings
				.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);

		webSettings.setDatabaseEnabled(true);
		/*
		 * 是否开启本地DOM存储 鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android
		 * 默认是关闭该功能的。
		 */
		webSettings.setDomStorageEnabled(true);
		webView.setSaveEnabled(true);
		webView.setKeepScreenOn(true);
	}
}
