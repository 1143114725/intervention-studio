package com.investigate.newsupper.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.SubItemsAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;


/**
 *  下级项目列表界面
 * Created by EEH on 2018/2/2.
 */

public class SubitemsListActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = SubitemsListActivity.class.getSimpleName();
    private ListView mListView;
    private SubItemsAdapter mSubItemsAdapter;
    public Survey survey;
	private MyApp ma;
	private String scid,scname;
    ArrayList<Survey> surverlist = new ArrayList<Survey>();
    private Button new_panel;
    //标题栏
    private LinearLayout visit_left_iv, more_opt_ll;
    private UITextView tvSurveyTile;
    //下拉栏
	private LinearLayout globle_search;// 全局搜索条
	private AutoCompleteTextView actvKeyWords;
	private ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
	private String selectitem;
	private String spinnertext;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.subitems_layout);
        ma = (MyApp) this.getApplication();
        survey = (Survey) getIntent().getExtras().get("s");
        scid = survey.getSCID();   
        scname = survey.getSCName(); 
        /**标题栏**/
        //返回按钮
        visit_left_iv = (LinearLayout) findViewById(R.id.visit_left_iv);
        visit_left_iv.setOnClickListener(this);
        //菜单按钮
        more_opt_ll = (LinearLayout) findViewById(R.id.more_opt_ll);
		more_opt_ll.setOnClickListener(this);
		//标题tv
		tvSurveyTile = (UITextView) findViewById(R.id.visit_survey_name_tv);
		TextSizeManager.getInstance().addTextComponent(TAG, tvSurveyTile);
		tvSurveyTile.setMaxLines(3);
		tvSurveyTile.setText(scname);
		//新建按钮
		new_panel = (Button) findViewById(R.id.new_panel);
		new_panel.setVisibility(View.GONE);
		/**下拉框**/
		tv_spinner = (UITextView) findViewById(R.id.spinner);
		TextSizeManager.getInstance().addTextComponent(TAG, tv_spinner);
		
		globle_search = (LinearLayout) findViewById(R.id.globle_search);
		actvKeyWords = (AutoCompleteTextView) findViewById(R.id.keyword_actv);
		actvKeyWords.setInputType(InputType.TYPE_NULL);
		actvKeyWords.setOnTouchListener(new FocusListener());
		
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	fs = ma.dbService.getAllXmlUploadFeedList(survey.surveyId, ma.userId);
    	System.out.println("onResume spinnertext------->"+spinnertext);
    	System.out.println("onResume selectitem------->"+selectitem);
    	System.out.println("onResume itemnum------->"+position);
    	System.out.println("onResume ma.userId------->"+ma.userId);
    	System.out.println("onResume scid------->"+scid);
    	
    	
		surverlist = ma.dbService.getAllScidSurvey(ma.userId,scid);
	    mListView = (ListView) findViewById(R.id.lv_subitems);
//	    mSubItemsAdapter = new SubItemsAdapter(SubitemsListActivity.this,surverlist,selectitem,spinnertext,position);
//	    mListView.setAdapter(mSubItemsAdapter);
		super.onResume();
    }
    
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.visit_left_iv://返回按钮
			SubitemsListActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		}
	}
 
 	/** 搜索框点击事件**/
 	
 	private String spinnerTv;
 	private int position;// 下拉位置
 	private UITextView tv_spinner;// 下拉选项
 	
 	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.ll_spinner:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SubitemsListActivity.this, AlertDialog.THEME_HOLO_LIGHT);
			builder.setIcon(R.drawable.ic_menu_archive);
			builder.setTitle(R.string.input_category);
			spinnerTv = getString(R.string.more_thing);
			// 指定下拉列表的显示数据
//			final String[] cities = { getString(R.string.input_category),
//					survey.getParameter1(), survey.getParameter2(),
//					survey.getParameter3(), survey.getParameter4() };
			final String[] cities = { getString(R.string.input_category),
					"PanelID", "姓名",
					"电话", "受访者编号" };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toasts.makeText(
							SubitemsListActivity.this,
							SubitemsListActivity.this.getString(R.string.choice_mode)
									+ cities[which], Toast.LENGTH_SHORT).show();
					position = which;
					if (which != 0) {
						spinnerTv = cities[which];
						tv_spinner.setText(spinnerTv);
					} else {
						tv_spinner.setText(spinnerTv);
					}
				}
			});
			builder.show();
			break;
		case R.id.search_btn:
			if (0 == position) {
				Util.viewShake(SubitemsListActivity.this, globle_search);
				Toasts.makeText(SubitemsListActivity.this,
						R.string.input_category_please, Toast.LENGTH_LONG)
						.show();
				return;
			}
			spinnertext = actvKeyWords.getText().toString().trim();
			 selectitem = tv_spinner.getText().toString().trim();
//			mSubItemsAdapter.updateListView(surverlist,selectitem,spinnertext,position);
			break;
		}
	}
 	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}


	//Autocompletetextview监听
		private final class FocusListener implements OnTouchListener {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
				}
				return false;
			}
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				SubitemsListActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
			}
			return super.onKeyDown(keyCode, event);
		}
}
