package com.investigate.newsupper.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.EditTextListViewAdapter;
import com.investigate.newsupper.bean.Data;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.CharacterParser;
import com.investigate.newsupper.util.Util;

public class EditTextListView extends Activity implements OnClickListener {
	// 按钮静态缓存，该用法可以避免使用startActivityForResult来获取按钮返回的时间
	private EditText edit_search;
	private ListView lv;
	private EditTextListViewAdapter adapter;
	List<String> list = new ArrayList<String>();// 所有的数据list
	List<String> newlist = new ArrayList<String>();// 查询后的数据list
	private LinearLayout linearLatout;
	private Button btnAdd;
	private MyApp ma;
	private String newArr;
	private Data data;// 数据字典数据串
	private String classId;// 数据字典表id
	private ImageView cancel;
	private CharacterParser characterParser;

	// 数据字典确认
	private Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittextlistview);
		// 数据字典确认
		initDialog();
		ma = (MyApp) getApplication();
		classId = getIntent().getExtras().getString("classId");
		// System.out.println("classId:"+classId);
		data = ma.dbService.queryDataById(classId);
		characterParser = CharacterParser.getInstance();
		if (null == data) {
			EditTextListView.this.finish();
			return;
		}
		if (!Util.isEmpty(data.getDatas()) && !Util.isEmpty(data.getLocalDatas())) {
			newArr = data.getDatas() + data.getLocalDatas();
		} else if (!Util.isEmpty(data.getDatas()) && Util.isEmpty(data.getLocalDatas())) {
			newArr = data.getDatas();
		} else if (Util.isEmpty(data.getDatas()) && Util.isEmpty(data.getLocalDatas())) {
			newArr = "";
		}
		btnAdd = (Button) findViewById(R.id.btnAdd);
		linearLatout = (LinearLayout) findViewById(R.id.ll);
		linearLatout.setLayoutParams(
				new LayoutParams(LayoutParams.FILL_PARENT, getWindowManager().getDefaultDisplay().getHeight() / 2));
		btnAdd.setOnClickListener(this);
		// 数据字典确认
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		// 数据字典确认
		cancel = (ImageView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		init();
		initDefaultLists();

	}

	// 数据字典确认
	private void initDialog() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
		p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.8
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.7
		getWindow().setAttributes(p);
	}

	// 初始化控件
	private void init() {
		edit_search = (EditText) findViewById(R.id.edit_search);
		// 为输入添加TextWatcher监听文字的变化
		edit_search.addTextChangedListener(new TextWatcher_Enum());
		adapter = new EditTextListViewAdapter(this, list);
		lv = (ListView) findViewById(R.id.edittextListview);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new onclick());
	}

	// 添加数据
	private void initDefaultLists() {
		if (!Util.isEmpty(newArr)) {
			String[] split = newArr.split(",");
			for (int i = 0; i < split.length; i++) {
				list.add(split[i]);
			}
		}
	}

	// 当editetext变化时调用的方法，来判断所输入是否包含在所属数据中
	private List<String> getNewData(String input_info) {

		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			String domain = list.get(i).toLowerCase();
			// 如果遍历到的名字包含所输入字符串
			if (domain.contains(input_info.trim().toLowerCase())) {
				newlist.add(domain);
			}
		}
		return newlist;
//		// 遍历list
//		for (int i = 0; i < list.size(); i++) {
//			String domain = list.get(i);// 查到的字符串
//			// 如果遍历到的名字包含所输入字符串
//			// 汉字转拼音
//			String key, value;
//			StringBuilder buffer = new StringBuilder();
//			key = input_info.substring(0, 0 + 1);
//			if (key.getBytes().length < 2) {
//				for (int c = 0; c < domain.length(); c++) {
//					key = domain.substring(c, c + 1);
//					if (key.getBytes().length >= 2) {// 字节数大于等于2说明是汉字
//						value = (String) characterParser.convert(key);// 单字的拼音
//						if (value == null) {
//							value = "unknown";
//						}
//					} else {// 说明是字母
//						value = key;
//					}
//					buffer.append(value.toUpperCase().charAt(0));
//				}
//			}
//			if (domain.contains(input_info.trim()) || buffer.toString().contains(input_info.trim().toUpperCase())) {
//				newlist.add(domain);
//			}
//		}
//		return newlist;
	}

	// 数据字典确认
	private boolean isSearch = false;

	// button的点击事件
	class onclick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 数据字典确认
			isSearch = true;
			TextView text = (TextView) view.findViewById(R.id.tvData);
			String str = (String) text.getText();
			edit_search.setText(str);
			// 数据字典确认
			lv.setVisibility(View.GONE);
			btnSubmit.setVisibility(View.VISIBLE);
			// Intent resultIntent = new Intent();
			// Bundle bundle = new Bundle();
			// bundle.putString("result", str);
			// resultIntent.putExtras(bundle);
			// EditTextListView.this.setResult(2, resultIntent);
			// EditTextListView.this.finish();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				btnAdd.setVisibility(View.VISIBLE);
				// 数据字典确认
				btnSubmit.setVisibility(View.VISIBLE);
				lv.setVisibility(View.INVISIBLE);
				break;
			case 2:
				btnAdd.setVisibility(View.GONE);
				// 数据字典确认
				btnSubmit.setVisibility(View.GONE);
				adapter = new EditTextListViewAdapter(EditTextListView.this, newlist);
				lv.setAdapter(adapter);
				lv.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	// TextWatcher接口
	class TextWatcher_Enum implements TextWatcher {

		// 文字变化前
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		// 文字变化时
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			newlist.clear();
			// 数据字典确认
			if (isSearch) {
				isSearch = false;
			} else {
				if (!Util.isEmpty(edit_search.getText())) {
					String input_info = edit_search.getText().toString();
					newlist = getNewData(input_info);
					if (newlist != null && newlist.size() > 0) {
						// System.out.println("找到了");
						handler.sendEmptyMessage(2);
					} else {
						// System.out.println("没找到了");
						handler.sendEmptyMessage(1);
					}
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}

		// 文字变化后
		@Override
		public void afterTextChanged(Editable s) {

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 数据字典确认
		case R.id.btnSubmit:
			String trim_search = edit_search.getText().toString().trim();
			if (Util.isEmpty(trim_search)) {
				Toasts.makeText(EditTextListView.this, R.string.no_null, Toast.LENGTH_LONG).show();
				return;
			}
			Intent resultIntent1 = new Intent();
			Bundle bundle1 = new Bundle();
			bundle1.putString("result", trim_search);
			resultIntent1.putExtras(bundle1);
			EditTextListView.this.setResult(2, resultIntent1);
			EditTextListView.this.finish();
			break;
		case R.id.btnAdd:
			String trim = edit_search.getText().toString().trim();
			String LocalDatas = "";
			if (Util.isEmpty(trim)) {
				Toasts.makeText(EditTextListView.this, R.string.no_null, Toast.LENGTH_LONG).show();
				return;
			}
			if (Util.isEmpty(data.getLocalDatas())) {
				LocalDatas = trim;
			} else {
				LocalDatas = data.getLocalDatas() + "," + trim;
			}
			ma.dbService.updateDataLocalDatas(LocalDatas, classId);
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", trim);
			resultIntent.putExtras(bundle);
			EditTextListView.this.setResult(2, resultIntent);
			EditTextListView.this.finish();
			break;
		// 新增
		case R.id.cancel:
			Intent nullIntent = new Intent();
			Bundle nullBundle = new Bundle();
			nullBundle.putString("result", "");
			nullIntent.putExtras(nullBundle);
			EditTextListView.this.setResult(2, nullIntent);
			EditTextListView.this.finish();
			break;
		default:
			break;
		}
	}

}
