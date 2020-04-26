package com.investigate.newsupper.bean;

import java.util.ArrayList;

public class QuestionItem extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4181454795297171906L;

	/**
	 * 引用单行文本题答案
	 */
	public String includefromItem;

	public String getIncludefromItem() {
		return includefromItem;
	}

	public void setIncludefromItem(String includefromItem) {
		this.includefromItem = includefromItem;
	}

	/**
	 * 小数位数限制
	 */
	public String floatNum;

	public String getFloatNum() {
		return floatNum;
	}

	public void setFloatNum(String floatNum) {
		this.floatNum = floatNum;
	}

	/**
	 * 禁用逻辑
	 */
	public String limitList;

	public String getLimitList() {
		return limitList;
	}

	public void setLimitList(String limitList) {
		this.limitList = limitList;
	}

	/** 组内随机开始 **/
	public String gRandomNum;

	public String getgRandomNum() {
		return gRandomNum;
	}

	public void setgRandomNum(String gRandomNum) {
		this.gRandomNum = gRandomNum;
	}

	/** 组内随机结束 **/

	/**
	 * 引用排斥，固定显示
	 */
	public int itemShow;

	/**
	 * 配置隐藏
	 */
	public boolean isSetHide;
	/**
	 * 选项排他
	 */
	public String excludeIn;
	/**
	 * 大树 预览 选项置顶 标示
	 */
	public Integer padding = 0;

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
		this.padding = padding;
	}

	/**
	 * 大树 预览 是否隐藏
	 */
	public boolean isHide;
	/**
	 * 是否为其他项
	 */
	public int isOther;// 1是其他项, 0普通项
	/**
	 * 引用的其他项
	 */
	public int isAnOther = 0;// 1是引用的其他项，0非引用情况
	/**
	 * 哑题是否是该项选中
	 */
	public boolean isDumbOk = false;

	/**
	 * 选项的宽度
	 */
	public Integer itemSize;
	/**
	 * 配置类型 （1 是显示 2 是隐藏）
	 */
	public int hideType = 0;
	/**
	 * 隐藏选项
	 */
	public int hide = 0;

	/**
	 * 是否启用日期控件
	 */
	public int dateCheck;// 1表示启用了日历控件, 0表示没有启用

	// <item value="2">33</item>
	/**
	 * 选项的编号 eg value=2
	 */
	public Integer itemValue = -100;

	/**
	 * 选项后置文子 eg 33
	 */
	public String itemText = "";

	/**
	 * 选项父选项文字
	 */
	public String parentText = "";

	public String getParentText() {
		return parentText;
	}

	public void setParentText(String parentText) {
		this.parentText = parentText;
	}

	/**
	 * 矩阵右侧
	 */
	public String itemTextRight;
	/**
	 * 
	 */
	public String leftsideWord;

	/**
	 * 
	 */
	public String rightsideWord;

	/**
	 * 选项排斥, 也就是选了它之后, 其他的都不能选
	 */
	public String exclude;
	/**
	 * 选项间互斥
	 */
	public String itemSingle;

	public String getItemSingle() {
		return itemSingle;
	}

	public void setItemSingle(String itemSingle) {
		this.itemSingle = itemSingle;
	}

	/**
	 * 
	 */
	public int isFreeInput;

	/**
	 * 选项中的追加说明
	 */
	public String caption;

	public int caption_check;// 追加说明方法,是否是显示还是提示。0显示1提示 默认是0

	/**
	 * 是否被预选
	 */
	public int deft;// 1被预选, 0没有

	/**
	 * 启用权重
	 */
	public int weightValue = -1;

	/**
	 * dateSelect=1,表示时间格式是年月日; dateSelect=2,表示时间格式是年月日时
	 * dateSelect=3,表示时间格式是年月日时分 dateSelect=4,表示时间格式是年月日时分秒
	 */
	public int dateSelect;

	/**
	 * 启用积分
	 */
	public int scoreValue = -1;

	/**
	 * 自定义的并非服务器xml配置的, 标志选项被选中了,
	 */
	public boolean isCheck;

	/**
	 * 单行文本题的数据类型 即 默认-1 none=0, 无即文本类型 date=1 日期 ,figure=2 数字, alphabet=3
	 * 英文/数字, data=4 字典 ,email=5 电子邮件,
	 */
	public int type = -1;

	/**
	 * 是否允许小数点(针对数字题选项)
	 */
	public boolean isFloat;

	/**
	 * 最大值
	 */
	public String maxNumber;
	/**
	 * 特殊值
	 */
	public String specialNumber;
	/**
	 * 判断是否进行限制提醒
	 */
	public boolean isRange;
	/**
	 * 是否强制判断数字范围
	 */
	public String range;

	/**
	 * 字符串长度限制 len="1" lensymbol="="
	 */
	public String len;
	/**
	 * 字符串长度限制 len="1" lensymbol="="
	 */
	public String lensymbol;
	/**
	 * 最大值
	 */
	public String minNumber;

	/**
	 * 字典的id(针对字典选项)
	 */
	public String classid;
	/**
	 * 字典类型 0或空=模糊匹配、1：匹配后继续、2：匹配后终止。
	 */
	public String classtype;

	/**
	 * @index.@item引用了某一道题的某一个选项进行比较
	 */
	public String titlefrom;

	/**
	 * 比较符号
	 */
	public String symbol;

	/**
	 * 是否启用拖动条
	 */
	public boolean dragChecked;
	/**
	 * 是否是单项必填 true就是 false就不是
	 */
	public boolean required;
	/**
	 * 滑动条是否动过
	 */
	public boolean isChange = false;

	/**
	 * 哑题的引用选项
	 */
	public String dumbList;// ""空代表此题没哑题。"1,0:1"
							// 逗号前面的1代表引用哪个index,逗号后面代表引用那个index的哪个值。

	/**
	 * 条件隐藏选项
	 */
	public String hideList;

	public String dataDictionary;// 数据字典串 1,2,3,4,5,6

	public String popUp;

	public String actualAllBigSetsFirstIndex;// 预设题组的开始位置

	public String actualAllBigSetslastIndex; // 预设题组的结束位置

	public String theoryAllBigSetsName;// 预设题组的各小题组名称
	public int comparison;

	public Integer parentItem = -100;
	public boolean isChild;
	private ArrayList<QuestionItem> childRows = new ArrayList<QuestionItem>();
	// 引用选项的文本

	public String newLine;

	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	public int includeIndex = -1;

	public int getIncludeIndex() {
		return includeIndex;
	}

	public void setIncludeIndex(int includeIndex) {
		this.includeIndex = includeIndex;
	}

	@Override
	public String toString() {
		return "QuestionItem [itemShow=" + itemShow + ", isSetHide="
				+ isSetHide + ", excludeIn=" + excludeIn + ", padding="
				+ padding + ", isHide=" + isHide + ", isOther=" + isOther
				+ ", isAnOther=" + isAnOther + ", isDumbOk=" + isDumbOk
				+ ", itemSize=" + itemSize + ", hideType=" + hideType
				+ ", hide=" + hide + ", dateCheck=" + dateCheck
				+ ", itemValue=" + itemValue + ", itemText=" + itemText
				+ ", itemTextRight=" + itemTextRight + ", leftsideWord="
				+ leftsideWord + ", rightsideWord=" + rightsideWord
				+ ", exclude=" + exclude + ", itemSingle=" + itemSingle
				+ ", isFreeInput=" + isFreeInput + ", caption=" + caption
				+ ", caption_check=" + caption_check + ", deft=" + deft
				+ ", weightValue=" + weightValue + ", dateSelect=" + dateSelect
				+ ", scoreValue=" + scoreValue + ", isCheck=" + isCheck
				+ ", type=" + type + ", isFloat=" + isFloat + ", maxNumber="
				+ maxNumber + ", specialNumber=" + specialNumber + ", isRange="
				+ isRange + ", range=" + range + ", len=" + len
				+ ", lensymbol=" + lensymbol + ", minNumber=" + minNumber
				+ ", classid=" + classid + ", classtype=" + classtype
				+ ", titlefrom=" + titlefrom + ", symbol=" + symbol
				+ ", dragChecked=" + dragChecked + ", required=" + required
				+ ", isChange=" + isChange + ", dumbList=" + dumbList
				+ ", hideList=" + hideList + ", dataDictionary="
				+ dataDictionary + ", popUp=" + popUp
				+ ", actualAllBigSetsFirstIndex=" + actualAllBigSetsFirstIndex
				+ ", actualAllBigSetslastIndex=" + actualAllBigSetslastIndex
				+ ", theoryAllBigSetsName=" + theoryAllBigSetsName
				+ ", comparison=" + comparison + ", parentItem=" + parentItem
				+ ", isChild=" + isChild + ", childRows=" + childRows
				+ ", includeIndex=" + includeIndex + ", newLine=" + newLine
				+ "]";
	}

	public String getPopUp() {
		return popUp;
	}

	public void setPopUp(String popUp) {
		this.popUp = popUp;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getLen() {
		return len;
	}

	public void setLen(String len) {
		this.len = len;
	}

	public String getLensymbol() {
		return lensymbol;
	}

	public void setLensymbol(String lensymbol) {
		this.lensymbol = lensymbol;
	}

	public String getDumbList() {
		return dumbList;
	}

	public boolean getIsSetHide() {
		return isSetHide;
	}

	public void setIsSetHide(boolean isSetHide) {
		this.isSetHide = isSetHide;
	}

	public void setDumbList(String dumbList) {
		this.dumbList = dumbList;
	}

	public Integer getItemSize() {
		return itemSize;
	}

	public void setItemSize(Integer itemSize) {
		this.itemSize = itemSize;
	}

	public Integer getItemValue() {
		return itemValue;
	}

	public void setItemValue(Integer itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getLeftsideWord() {
		return leftsideWord;
	}

	public void setLeftsideWord(String leftsideWord) {
		this.leftsideWord = leftsideWord;
	}

	public String getRightsideWord() {
		return rightsideWord;
	}

	public void setRightsideWord(String rightsideWord) {
		this.rightsideWord = rightsideWord;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public int getDateCheck() {
		return dateCheck;
	}

	public void setDateCheck(int dateCheck) {
		this.dateCheck = dateCheck;
	}

	public int getIsFreeInput() {
		return isFreeInput;
	}

	public void setIsFreeInput(int isFreeInput) {
		this.isFreeInput = isFreeInput;
	}

	public int getIsOther() {
		return isOther;
	}

	public void setIsOther(int isOther) {
		this.isOther = isOther;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getDeft() {
		return deft;
	}

	public void setDeft(int deft) {
		this.deft = deft;
	}

	public int getWeightValue() {
		return weightValue;
	}

	public void setWeightValue(int weightValue) {
		this.weightValue = weightValue;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Integer getDateSelect() {
		return dateSelect;
	}

	public void setDateSelect(Integer dateSelect) {
		this.dateSelect = dateSelect;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isFloat() {
		return isFloat;
	}

	public void setFloat(boolean isFloat) {
		this.isFloat = isFloat;
	}

	public String getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(String maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getspecialNumber() {
		return specialNumber;

	}

	public void setspecialNumber(String specialNumber) {
		this.specialNumber = specialNumber;
	}

	public String getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(String minNumber) {
		this.minNumber = minNumber;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getTitlefrom() {
		return titlefrom;
	}

	public void setTitlefrom(String titlefrom) {
		this.titlefrom = titlefrom;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setDateSelect(int dateSelect) {
		this.dateSelect = dateSelect;
	}

	public boolean isDragChecked() {
		return dragChecked;
	}

	public void setDragChecked(boolean dragChecked) {
		this.dragChecked = dragChecked;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getCaption_check() {
		return caption_check;
	}

	public void setCaption_check(int caption_check) {
		this.caption_check = caption_check;
	}

	public String getItemTextRight() {
		return itemTextRight;
	}

	public void setItemTextRight(String itemTextRight) {
		this.itemTextRight = itemTextRight;
	}

	public ArrayList<QuestionItem> getChildRows() {
		return childRows;
	}

	public void setChildRows(ArrayList<QuestionItem> childRows) {
		this.childRows = childRows;
	}

}
