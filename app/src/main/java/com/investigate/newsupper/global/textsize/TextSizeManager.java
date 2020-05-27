package com.investigate.newsupper.global.textsize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.investigate.newsupper.dialog.CustomDialog.Builder;
import com.investigate.newsupper.util.SharedPreferencesManager;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.view.adapterlinearlayout.AdapterLinearLayout;
import com.investigate.newsupper.view.adapterlinearlayout.AdapterLinearLayout.OnItemClickListener;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.investigate.newsupper.R;

/**
 * 应用字体业务
 * @author 胡事成
 */
public final class TextSizeManager {

	private static final String APP_TEXT_SIZE_SCALE = "TextSizeManager_APP_TEXT_SIZE_SCALE";
	
	private static final int TEXT_SIZE_DEFAULT = UIUtils.getDimenPixelSize(R.dimen.text_size_default);
	
	private static final float SCALE = 0.1f;
	private static final int TEXT_SIZE_SCALE_SMALL 	 = 8; // 小
	private static final int TEXT_SIZE_SCALE_DEFAULT = 10; // 默认
	private static final int TEXT_SIZE_SCALE_LARGE   = 12; // 大
	
	private ArrayMap<String, ArrayList<ITextComponent>> sTextComponents;
	
	private static int mTextSizeScale = TEXT_SIZE_SCALE_DEFAULT;
	
	private static TextSizeManager sInstance = new TextSizeManager();
	
	private TextSizeManager() {
		mTextSizeScale = SharedPreferencesManager.getInt(APP_TEXT_SIZE_SCALE, TEXT_SIZE_SCALE_DEFAULT);
	}
	
	public static TextSizeManager getInstance() {
		return sInstance;
	}
	
	public TextSizeManager addTextComponent(String groupKey, ITextComponent component) {
		if (TextUtils.isEmpty(groupKey) || component == null) {
			return this;
		}
		if (sTextComponents == null) {
			sTextComponents = new ArrayMap<String, ArrayList<ITextComponent>>();
		}
		ArrayList<ITextComponent> components = sTextComponents.get(groupKey);
		if (components == null) {
			components = new ArrayList<ITextComponent>();
			sTextComponents.put(groupKey, components);
		}
		if (!components.contains(component)) {
			components.add(component);
		}
		return this;
	}
	
	public void removeTextComponent(String groupKey) {
		if (sTextComponents != null) {
			sTextComponents.remove(groupKey);
		}
	}
	
	/**
	 * 通知所有的控件改变字体
	 * @param scale
	 */
	private void notifyAllComponent(float scale) {
		if (sTextComponents != null) {
			Collection<ArrayList<ITextComponent>> all = sTextComponents.values();
			if (all != null) {
				for (ArrayList<ITextComponent> components : all) {
					if (components == null || components.isEmpty()) {
						continue;
					}
					for (ITextComponent component : components) {
						if (component != null) {
							component.onTextSizeSetting(scale);
						}
					}
				}
			}
		}
	}
	
	private void setTextSizeScale(int textSizeScale) {
		SharedPreferencesManager.putIntAsync(APP_TEXT_SIZE_SCALE, textSizeScale);
		mTextSizeScale = textSizeScale;
	}
	
	/**
	 * 默认大小
	 * @return
	 */
	public int getTextSize() {
		return getRealSize(mTextSizeScale);
	}
	
	/**
	 * 显示时的倍数
	 * @return
	 */
	public static float getRealScale() {
		return mTextSizeScale * SCALE;
	}
	
	public static int getRealSize(int textSizeScale) {
		return (int) (TEXT_SIZE_DEFAULT * textSizeScale * SCALE);
	}
	
	public int getText() {
		switch (mTextSizeScale) {
		case TEXT_SIZE_SCALE_SMALL:
			return R.string.text_size_small;
		case TEXT_SIZE_SCALE_DEFAULT:
			return R.string.text_size_default;
		case TEXT_SIZE_SCALE_LARGE:
			return R.string.text_size_large;
		}
		return R.string.text_size_default;
	}
	
	public void showTextSizeSelector(Context context, final OnTextSizeListener listener) {
		final List<TextSize> data = new ArrayList<TextSize>();
		data.add(new TextSize(TEXT_SIZE_SCALE_SMALL, R.string.text_size_small));
		data.add(new TextSize(TEXT_SIZE_SCALE_DEFAULT, R.string.text_size_default));
		data.add(new TextSize(TEXT_SIZE_SCALE_LARGE, R.string.text_size_large));
		final Builder b = Builder.create(context, R.layout.text_size_selector_layout);
		b.setCanceledOnTouchOutside(true);
		b.setGravity(Gravity.BOTTOM);
		b.setAnimations(R.anim.slide_in_from_bottom);
		AdapterLinearLayout listView = (AdapterLinearLayout) b.getView(R.id.list);
		final TextSizeAdapter adapter = new TextSizeAdapter(context, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterLinearLayout adapterView, View view, int position) {
				TextSize item = adapter.getItem(position);
				if (item != null) {
					setTextSizeScale(item.size);
					adapter.notifyDataSetChanged();
					notifyAllComponent(item.size * SCALE);
					if (listener != null) {
						listener.onSelected(item.size, item.text);
					}
				}
				b.dismiss();
			}
		});
		b.show();
	}
	
	private static class TextSize {
		int size;
		int text;
		public TextSize(int size, int text) {
			this.size = size;
			this.text = text;
		}
	}
	
	private class TextSizeAdapter extends BaseAdapter {
		private Context mContext;
		private List<TextSize> mData;
		public TextSizeAdapter(Context context, List<TextSize> data) {
			mContext = context;
			mData = data;
		}
		
		@Override
		public int getCount() {
			if (mData != null) {
				return mData.size();
			}
			return 0;
		}

		@Override
		public TextSize getItem(int position) {
			if (mData != null) {
				return mData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.text_size_item, parent, false);
			}
			if (convertView instanceof TextView) {
				TextView text = (TextView) convertView;
				TextSize item = getItem(position);
				if (item != null) {
					text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getRealSize(item.size));
					if (item.size == mTextSizeScale) {
						text.setTextColor(Color.BLUE);
					} else {
						text.setTextColor(Color.GRAY);
					}
					text.setText(item.text);
				}
			}
			return convertView;
		}


	}
	
	public static interface OnTextSizeListener {
		void onSelected(int textSize, int text);
	}

}