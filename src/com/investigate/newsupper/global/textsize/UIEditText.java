package com.investigate.newsupper.global.textsize;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

public class UIEditText extends EditText implements ITextComponent {

	private float mOriginalTextSize;
	
	public UIEditText(Context context) {
		super(context);
		init();
	}

	public UIEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	
	public UIEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setOriginalTextSize(getTextSize());
	}

	@Override
	public void setOriginalTextSize(float originalTextSize) {
		mOriginalTextSize = originalTextSize;
		updateTextSize();
	}

	@Override
	public float getOriginalTextSize() {
		return mOriginalTextSize;
	}

	@Override
	public void onTextSizeSetting(float scale) {
		setTextSize(TypedValue.COMPLEX_UNIT_PX, scale * getOriginalTextSize());
	}
	
	private void updateTextSize() {
		setTextSize(TypedValue.COMPLEX_UNIT_PX, TextSizeManager.getRealScale() * getOriginalTextSize());
	}

}
