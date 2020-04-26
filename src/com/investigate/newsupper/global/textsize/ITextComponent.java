package com.investigate.newsupper.global.textsize;

public interface ITextComponent {
	
	/**
	 * 设置原始字体大小
	 * @param originalTextSize
	 */
	public void setOriginalTextSize(float originalTextSize);
	
	/**
	 * 获取原始字体大小
	 * @return
	 */
	public float getOriginalTextSize();

	/**
	 * 当字体全局设置
	 */
	public void onTextSizeSetting(float scale);
}
