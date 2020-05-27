package com.investigate.newsupper.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.investigate.newsupper.R;

/**
 * 用于创建对话框
 * 
 * @author MJ   自定义对话框的实现  在这里体现出来      
 */
public class DialogBulder {
	Dialog dialog;  
	Context context;
	public int dialogId;
	private int screenHeight;
	public DialogBulder(Context context, int dialogId,int width,int height) {
		Dialog dialog = new Dialog(context, R.style.Dialog);
		screenHeight=height;
		dialog.setContentView(R.layout.dialog_builder);// 先设置要显示的内容
		dialog.getWindow().getAttributes().width = (int) (width * 18 / 37);// 调节对话框的宽度  280   屏幕自适应  
		this.dialog = dialog;  
		this.context = context;  
		this.dialogId = dialogId;
		TextView title=getView(R.id.title);
		LinearLayout messageLL=getView(R.id.message_layout);
		//TextView tv = new TextView(context);
		//LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//messageLL.addView(tv,lp);
		messageLL.setPadding(5,5,5,5);
		//LinearLayout messageLLB=getView(R.id.btns_layout);
		//messageLLB.setPadding(0, 0, 0, 0);
		LinearLayout btnLL=getView(R.id.btns_layout);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		btnLL.setLayoutParams(params);
		//btnLL.setMinimumHeight((int) (screenHeight / 8 * 1 / 2));
		messageLL.setGravity(Gravity.CENTER);
		btnLL.setGravity(Gravity.CENTER);
		messageLL.setLayoutParams(params); 
	}
	public DialogBulder(Context context,int width,int height) {
		this(context, 0,width,height);
	}

	/**
	 * 设置标题   
	 * 
	 * @param title
	 *            标题内容(字符串 或者 资源id)
	 */
	public DialogBulder setTitle(Object title) {
		// setTitle("提示")
		TextView titleView = getView(R.id.title);
		titleView.setText(parseParam(title));
		return this;
	}

	/**
	 * 设置中间的内容
	 */
	public DialogBulder setMessage(Object message,int textsize) {
		TextView messageView = getView(R.id.message);
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		messageView.setMinHeight((int) (screenHeight / 8 * 1 / 2));
		//messageView.setMaxWidth((int)(display.getWidth()*0.2));
		messageView.setTextSize(textsize);
		messageView.setText(parseParam(message));
		return this;
	}
	TextView left;
	public DialogBulder setButtons(Object leftBtn, Object rightBtn, final OnDialogButtonClickListener listener,boolean isHaveRight) {
		// 设置左边按钮的文字
		TextView left = getView(R.id.left_tv);
		left.setText(parseParam(leftBtn));
//		// 给按钮绑定监听器
//		left.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (listener != null) { // 有监听器
//					listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_LEFT);
//				}
//			}
//		});
		this.left = left;

		// 设置右边按钮的文字
		TextView right = getView(R.id.right_tv);
		View view=getView(R.id.dialog_builder_view);
		right.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		if (isHaveRight) {
			right.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
		}
		right.setText(parseParam(rightBtn));
		LinearLayout btns_layout=getView(R.id.btns_layout);
		// 给按钮绑定监听器
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) { // 假如listener不为空
					listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_LEFT);
				}
				
			}
		});
		right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) { // 假如listener不为空
					listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_RIGHT);
				}
				
			}
		});
		
		
//		// 给按钮绑定监听器
//				right.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View v) {
//						dialog.dismiss();
//						if (listener != null) { // 假如listener不为空
//							listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_RIGHT);
//						}
//						
//					}
//				});
		return this;
	}

	/**
	 * 通过id找到对话框中对应的子控件
	 * 
	 * @param id
	 *            子控件的id
	 * @return 子控件
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int id) {
		return (T) dialog.findViewById(id);
	}

	/**
	 * 解析参数
	 * 
	 * @param param
	 *            字符串 或者 资源id
	 * @return 统一返回字符串
	 */
	private String parseParam(Object param) {
		if (param instanceof Integer) {
			return context.getString((Integer) param);
		} else if (param instanceof String) {
			return param.toString();
		}
		return null;
	}

	/**
	 * 说明已经确定对话框的界面参数    添加一个方法   如果不存在 则 剔除掉  这个  按钮布局   
	 * 
	 * @return
	 */
	public Dialog create() {
		if (left == null) { // 说明不需要按钮
			// 得到按钮所在的布局
			ViewGroup btnsLayout = getView(R.id.btns_layout);
			// 得到根节点
			ViewGroup root = getView(R.id.root);
			// 移除按钮所在的布局
			// ViewGroup root = (ViewGroup) btnsLayout.getParent();
			root.removeView(btnsLayout);
		}
		return dialog;
		
	}

	public interface OnDialogButtonClickListener {
		public static final int BUTTON_LEFT = 0;
		public static final int BUTTON_RIGHT = 1;

		public void onDialogButtonClick(Context context, DialogBulder builder, Dialog dialog, int dialogId, int which);
	}

	/**
	 * 将view放进对话框中     信息内容  单独定制     一个 view  
	 */
	public DialogBulder setView(View view) {
		// 1.获得message所在的布局
		ViewGroup messageLayout = getView(R.id.message_layout);
		// 2.移除TextView
		messageLayout.removeAllViews();
		// 3.添加新的View
		messageLayout.addView(view);
		return this;
	}

	/**
	 * 将布局文件对应的View放进对话框中   
	 * 
	 * @param layout
	 * @return
	 */
	public DialogBulder setView(int layout) {
		ViewGroup parent = getView(R.id.message_layout);
		View view = LayoutInflater.from(context).inflate(layout, parent, false);
		return setView(view);
	}

	//  设置  下拉列表   listview   放到这里头    
	public DialogBulder setItems(String[] items, final OnDialogItemClickListener listener) {
//		// 1.把ListView放进对话框中
//		setView(R.layout.list_view);
//		// 2.设置ListView的数据
//		ListView listView = getView(android.R.id.list);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_text_view, items);
//		listView.setAdapter(adapter);
//		// 3.给ListView绑定监听器
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				dialog.dismiss();
//				if (listener != null) {
//					listener.onDialogItemClick(context, DialogBulder.this, dialog, position);
//				}
//			}
//		});
		return this;
	}

	public DialogBulder setItems(int arrayId, OnDialogItemClickListener listener) {
		String[] items = context.getResources().getStringArray(arrayId);
		return setItems(items, listener);
	}

	public interface OnDialogItemClickListener {
		public void onDialogItemClick(Context context, DialogBulder builder, Dialog dialog, int position);
	}
}
