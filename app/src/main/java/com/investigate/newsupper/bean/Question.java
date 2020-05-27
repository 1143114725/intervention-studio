package com.investigate.newsupper.bean;

import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;

import android.util.Log;

public class Question extends IBean {

	/**
	 * 唯一ID
	 */
	private static final long serialVersionUID = -2965102114024717789L;

	//隐藏拍照
	public String hidePhotoCheck;
	
	public String getHidePhotoCheck() {
			return hidePhotoCheck;
		}

		public void setHidePhotoCheck(String hidePhotoCheck) {
			this.hidePhotoCheck = hidePhotoCheck;
		}
	
	//触发强制拍照的功能设计
	public String photocheck;
	
	public String getPhotocheck() {
		return photocheck;
	}

	public void setPhotocheck(String photocheck) {
		this.photocheck = photocheck;
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
	/**组内随机开始**/
	
	public String itemGR_Flag;// 大组随机不随机
	public String gRSelectNum;//该Item属于哪个分组，组内不随机，组与组之间随机。
	public String getgRSelectNum() {
		return gRSelectNum;
	}

	public void setgRSelectNum(String gRSelectNum) {
		this.gRSelectNum = gRSelectNum;
	}

	public String getItemGR_Flag() {
		return itemGR_Flag;
	}

	public void setItemGR_Flag(String itemGR_Flag) {
		this.itemGR_Flag = itemGR_Flag;
	}


	/**组内随机结束**/
	

	/**
	 * 单行文本题是新还是旧
	 */
	public boolean isNew = true;
	/**
	 * 本题第几项
	 */
	public int currIndex = 0;
	public Integer id;

	/**
	 * 大树 双引用 添加 字段 一下：
	 */
	public String qSiteOption2;

	/**
	 * 大树 添加字段 题外关联 之 选项置顶 字段判断 是否 选项置顶
	 */
	public String isItemStick = "";
	/**
	 * 大树排序 选项排序 字段
	 */
	public int qSortChecked;
	/**
	 * 复选排序标识 1是true,0是false
	 */
	// public int qSortCheck;
	// 大树 添加 字段 以上部分
	/**
	 * 调查的问卷号
	 */
	public String surveyId;

	/**
	 * 下标号
	 */
	public int qIndex;

	/**
	 * 顺序号
	 */
	public int qOrder;

	/**
	 * 题型int标识
	 */
	public int qType;

	/**
	 * 题型字符说明
	 */
	public String qTypeStr;

	/**
	 * 题目标题
	 */
	public String qTitle;

	/**
	 * 题目的控件是否可用
	 */
	public int qTitleDisable;

	/**
	 * 题目文本的位置
	 */
	public String qTitlePosition;

	/**
	 * 上方追加说明
	 */
	public String qComment;

	/**
	 * 题目表述性文字的说明的位置
	 */
	public String qCommentPosition;

	/**
	 * 启用评分
	 */
	public int qScoreChecked;

	/**
	 * 启用权重
	 */
	public int qWeightChecked;

	/**
	 * 必答题
	 */
	public int qRequired;

	/**
	 * 问题选项的位置是随机的
	 */
	public int qRadomed;

	public int qDragChecked;

	public int isFixed; // 单复选矩阵固定 默认0为 false 1为true
	public QuestionItem item;
	/**
	 * 引用和排斥
	 */
	public String qInclusion;

	/**
	 * 特殊值
	 */
	public String special;
	/**
	 * 排他
	 */
	public int qExclude;

	/**
	 * 引用指定的题目, qSiteOption即等于qIndex的值
	 */
	public String qSiteOption;

	public boolean isConfig() {
		return isConfig;
	}

	public void setConfig(boolean isConfig) {
		this.isConfig = isConfig;
	}

	/**
	 * 是否为configration
	 */
	public boolean isConfig;

	/**
	 * 预选项, 也就是问题加载的时候, 题目中的选项已经被选上了
	 */
	public int qPreSelect;
	/**
	 * 哑题 是否是哑题
	 */
	public int qDumbFlag;// 1代表哑题 0代表不是哑题。

	/**
	 * 允许上传附件
	 */
	public int qAttach;

	/**
	 * 控件的宽度
	 */
	public int sizeWidth;

	/**
	 * 题目控件横向、纵向摆放
	 */
	public String deployment;

	/**
	 * 矩阵题目控件横向、纵向摆放
	 */
	public String qColumnsDirection;
	/** horizontal vertal **/

	public int ignoreFirstItem;
	public String firstText;

	/**
	 * 问题的上方追加说明
	 */
	public String qCaption;

	/**
	 * 问题前方的Qid
	 */
	public String qid;

	public int isRight;// 矩阵右侧 0代表没有，1代表有。

	// <sort sumNumber="50" Symbol=">=" maxNumber="30"
	// minNumber="10">figure</sort>
	/**
	 * 文本框的列数
	 */
	public int freeTextColumn;

	public String freeSumNumber;// ==>sumNumber="50"

	public String sumIndex;// 数字之和等于指定题目

	public String freeSymbol;// ==>Symbol=">="

	public String freeTextSort;// ==>figure

	public String freeMaxNumber;// ==>maxNumber="30"

	public String freeMinNumber;// ==>minNumber="10"
/**
 * 最外层。1就是并且。0就是或者，2就是自定义
 */
	public Integer qMatchQuestion;

	/**
	 * 文本框或下拉列表或进度条的值不能重复
	 */
	public int freeNoRepeat;
	/**
	 * 连续性选择判断，只适用于单选矩阵、复选矩阵
	 */
	public int qContinuous;
	/**
	 * 连续性选择提醒文字，只适用于单选矩阵、复选矩阵
	 */
	public String qContinuousText;
	/**
	 * 文本域的行数
	 */
	public int textAreaRows;

	/**
	 * 
	 */
	public int rowsNum;

	/**
	 * 媒体文件的位置
	 */
	public String qMediaPosition;

	/**
	 * 媒体文件源
	 */
	public String qMediaSrc;

	/**
	 * 媒体文件宽度
	 */
	public int qMediaWidth;

	/**
	 * 媒体文件高度
	 */
	public int qMediaHeight;

	/**
	 * 分数
	 */
	public String qScore;

	/**
	 * 有没有其他项 1有, 0无
	 */
	public int haveOther;

	/**
	 * 最少下限
	 */
	public int lowerBound;

	/**
	 * 最大上限
	 */
	public int upperBound;

	/**
	 * 引用其它问题的答案
	 */
	private String resctItemStr;
	/**
	 * 复杂逻辑引用
	 */
	private String QgRestItemStr;

	@Override
	public String toString() {
		return "Question [isNew=" + isNew + ", currIndex=" + currIndex
				+ ", id=" + id + ", qSiteOption2=" + qSiteOption2
				+ ", isItemStick=" + isItemStick + ", qSortChecked="
				+ qSortChecked + ", surveyId=" + surveyId + ", qIndex="
				+ qIndex + ", qOrder=" + qOrder + ", qType=" + qType
				+ ", qTypeStr=" + qTypeStr + ", qTitle=" + qTitle
				+ ", qTitleDisable=" + qTitleDisable + ", qTitlePosition="
				+ qTitlePosition + ", qComment=" + qComment
				+ ", qCommentPosition=" + qCommentPosition + ", qScoreChecked="
				+ qScoreChecked + ", qWeightChecked=" + qWeightChecked
				+ ", qRequired=" + qRequired + ", qRadomed=" + qRadomed
				+ ", qDragChecked=" + qDragChecked + ", isFixed=" + isFixed
				+ ", item=" + item + ", qInclusion=" + qInclusion
				+ ", special=" + special + ", qExclude=" + qExclude
				+ ", qSiteOption=" + qSiteOption + ", isConfig=" + isConfig
				+ ", qPreSelect=" + qPreSelect + ", qDumbFlag=" + qDumbFlag
				+ ", qAttach=" + qAttach + ", sizeWidth=" + sizeWidth
				+ ", deployment=" + deployment + ", qColumnsDirection="
				+ qColumnsDirection + ", ignoreFirstItem=" + ignoreFirstItem
				+ ", firstText=" + firstText + ", qCaption=" + qCaption
				+ ", qid=" + qid + ", isRight=" + isRight + ", freeTextColumn="
				+ freeTextColumn + ", freeSumNumber=" + freeSumNumber
				+ ", sumIndex=" + sumIndex + ", freeSymbol=" + freeSymbol
				+ ", freeTextSort=" + freeTextSort + ", freeMaxNumber="
				+ freeMaxNumber + ", freeMinNumber=" + freeMinNumber
				+ ", qMatchQuestion=" + qMatchQuestion + ", freeNoRepeat="
				+ freeNoRepeat + ", qContinuous=" + qContinuous
				+ ", textAreaRows=" + textAreaRows + ", rowsNum=" + rowsNum
				+ ", qMediaPosition=" + qMediaPosition + ", qMediaSrc="
				+ qMediaSrc + ", qMediaWidth=" + qMediaWidth
				+ ", qMediaHeight=" + qMediaHeight + ", qScore=" + qScore
				+ ", haveOther=" + haveOther + ", lowerBound=" + lowerBound
				+ ", upperBound=" + upperBound + ", resctItemStr="
				+ resctItemStr + ", QgRestItemStr=" + QgRestItemStr
				+ ", gRestStr=" + gRestStr + ", titleFrom=" + titleFrom
				+ ", qCamera=" + qCamera + ", qCameraName=" + qCameraName
				+ ", qSign=" + qSign + ", isHaveItemCap=" + isHaveItemCap
				+ ", qParentAssociatedCheck=" + qParentAssociatedCheck
				+ ", titleFromArr=" + titleFromArr + ", resctItemArr="
				+ resctItemArr + ", QgRestItemArr=" + QgRestItemArr
				+ ", gRestArr=" + gRestArr + ", rowItemStr=" + rowItemStr
				+ ", rowItemArr=" + rowItemArr + ", colItemStr=" + colItemStr
				+ ", colItemArr=" + colItemArr + ", qLinkage=" + qLinkage
				+ ", intervention=" + intervention + ", interventionStr="
				+ interventionStr + ", group=" + group + ", groupStr="
				+ groupStr + ", qStarCheck=" + qStarCheck + ", hideCount="
				+ hideCount + ", realRows=" + realRows + ", qStopTime="
				+ qStopTime + ", qContinuousText=" + qContinuousText + "]";
	}

	/**
	 * 复杂逻辑小题组集合
	 */
	private String gRestStr;

	/**
	 * 引用某些题目的标题 eg: titleFrom="1,3,5,7,9"
	 */
	private String titleFrom;

	/**
	 * 单题拍照是否本题有拍照，以及拍照的名字,
	 */
	public int qCamera;// 是否是单题拍照,0不支持，1支持。
	public String qCameraName;// 单题拍照的名字
	/**
	 * 单题签名是否有
	 */
	public int qSign;// 是否是单题签名,0不支持，1支持。

	/**
	 * 当前题目的选项中有没有追加说明
	 */
	public int isHaveItemCap;

	/**
	 * 当前题目题外之和关联的 标志性说明 2,2,2 三个数字代表什么意思 ？？ 题外关联 字段
	 */
	public String qParentAssociatedCheck;

	private ArrayList<Integer> titleFromArr = new ArrayList<Integer>();

	private ArrayList<Restriction> resctItemArr = new ArrayList<Restriction>();
	private ArrayList<QgRestriction> QgRestItemArr = new ArrayList<QgRestriction>();
	private ArrayList<GRestriction> gRestArr = new ArrayList<GRestriction>();
	private String rowItemStr;
	private ArrayList<QuestionItem> rowItemArr = new ArrayList<QuestionItem>();

	private String colItemStr;
	private ArrayList<QuestionItem> colItemArr = new ArrayList<QuestionItem>();

	public int qLinkage;// 三级联动字段 1代表是，0代表不是

	/**
	 * 干预
	 */
	private Intervention intervention = new Intervention();

	private String interventionStr;

	private QGroup group;

	private String groupStr;
	public int qStarCheck;

	public int hideCount;

	public int realRows;
	/**
	 * 每题停留时间
	 */
	public int qStopTime;

	public int itemRecording;
/**
 * 查重功能  t：本题查重 f(或者空):本题不查重
 */
	public String checkRepeat;
	
	public String getCheckRepeat() {
		return checkRepeat;
	}

	public void setCheckRepeat(String checkRepeat) {
		this.checkRepeat = checkRepeat;
	}

	public Object getResctItemArr;

	public String getResctItemStr() {
		return resctItemStr;
	}

	/**
	 * 设置逻辑集合
	 * 
	 * @param resctItemStr
	 */
	public void setResctItemStr(String resctItemStr) {
		this.resctItemStr = resctItemStr;
		if (!Util.isEmpty(resctItemStr)) {
			/**
			 * 从xml文件解析出逻辑的集合 然后转成json字符串 目的存在数据库中备以后拿出来用
			 */
			ArrayList<Restriction> rs = XmlUtil
					.restrictionStr2JsonArr(resctItemStr);
			if (!Util.isEmpty(rs)) {
				this.resctItemArr = rs;
			}
		}
	}

	public String getQgResctItemStr() {
		return QgRestItemStr;
	}

	/**
	 * 设置逻辑集合
	 * 
	 * @param resctItemStr
	 */
	public void setQgRestItemStr(String QgRestItemStr) {
		this.QgRestItemStr = QgRestItemStr;
		if (!Util.isEmpty(QgRestItemStr)) {
			/**
			 * 从xml文件解析出逻辑的集合 然后转成json字符串 目的存在数据库中备以后拿出来用
			 */
			ArrayList<QgRestriction> qgrs = XmlUtil
					.qGRestrictionStr2JsonArr(QgRestItemStr);
			if (!Util.isEmpty(qgrs)) {
				this.QgRestItemArr = qgrs;
			}
		}
	}

	public ArrayList<Restriction> getResctItemArr() {
		return resctItemArr;
	}

	public void setResctItemArr(ArrayList<Restriction> resctItemArr) {
		this.resctItemArr = resctItemArr;
		if (!Util.isEmpty(resctItemArr)) {
			this.resctItemStr = XmlUtil.jsonArr2RestrictionStr(resctItemArr);
			Log.i("@@@", "resctItemArr=" + resctItemArr.toString());
			Log.i("@@@", "resctItemStr=" + resctItemStr);
		}
	}

	public String getRowItemStr() {
		return rowItemStr;
	}

	public void setRowItemStr(String rowItemStr) {
		this.rowItemStr = rowItemStr;
		if (!Util.isEmpty(rowItemStr)) {
			/**
			 * 从数据库中取出json数组然后转成ArrayList集合在答题时使用
			 */
			ArrayList<QuestionItem> items = XmlUtil.str2JsonArr(rowItemStr);
			if (!Util.isEmpty(items)) {
				this.rowItemArr = items;
			}
		}
	}

	public ArrayList<QuestionItem> getRowItemArr() {
		return rowItemArr;
	}

	public void setRowItemArr(ArrayList<QuestionItem> rowItemArr) {
		this.rowItemArr = rowItemArr;
		if (!Util.isEmpty(rowItemArr)) {
			/**
			 * 将XML中解析出来的题目的行集合转换成json字符串保存在本地数据中
			 */
			this.rowItemStr = XmlUtil.jsonArr2Str(rowItemArr);
		}
	}

	public String getColItemStr() {
		return colItemStr;
	}

	public void setColItemStr(String colItemStr) {
		this.colItemStr = colItemStr;
		if (!Util.isEmpty(colItemStr)) {
			/**
			 * 将列的json数据转换成列集合 通常作用于从数据库中取出列字符串然后转成ArrayList集合 在答题的时候使用
			 */
			ArrayList<QuestionItem> items = XmlUtil.str2JsonArr(colItemStr);
			if (!Util.isEmpty(items)) {
				this.colItemArr = items;
			}
		}
	}

	public ArrayList<QuestionItem> getColItemArr() {
		return colItemArr;
	}

	/**
	 * 列的集合
	 * 
	 * @param colItemArr
	 */
	public void setColItemArr(ArrayList<QuestionItem> colItemArr) {
		this.colItemArr = colItemArr;
		if (!Util.isEmpty(colItemArr)) {
			/**
			 * 将列的集合转成JSON数组保存在colItemStr变量中 通常用于从XML中解析然后插入/更新到数据库中
			 */
			this.colItemStr = XmlUtil.jsonArr2Str(colItemArr);
		}
	}

	public ArrayList<Integer> getTitleFromArr() {
		return titleFromArr;
	}

	public String getTitleFrom() {
		return titleFrom;
	}

	public void setTitleFrom(String titleFrom) {
		this.titleFrom = titleFrom;
		titleFromArr.clear();
		/**
		 * 假如引用的标题index数组不为空, 则将其按照","拆分再装入ArrayList集合
		 */
		if (!Util.isEmpty(titleFrom)) {
			String[] arr = titleFrom.split(",");
			for (int i = 0; i < arr.length; i++) {
				// 引用受访者参数只保存数字 循环不是数字跳过
				if (!Util.isFormat(arr[i], -1)) {
					continue;
				}
				if (!Util.isEmpty(arr[i])) {
					titleFromArr.add(Integer.parseInt(arr[i]));
				}
			}
		}
	}

	public Intervention getIntervention() {
		return intervention;
	}

	/**
	 * 设置的同时将Intervention对象转换成Intervention的JSON字符串
	 * 
	 * @param intervention
	 */
	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
		if (null != intervention) {
			String iiStr = XmlUtil.parserIntervention2Json(intervention);
			if (!Util.isEmpty(iiStr)) {
				this.interventionStr = iiStr;
			}
		}
	}

	public String getInterventionStr() {
		return interventionStr;
	}

	/*
	 * 将Intervention的JSON字符串转换成Intervention对象
	 */
	public void setInterventionStr(String interventionStr) {
		this.interventionStr = interventionStr;
		if (!Util.isEmpty(interventionStr)) {
			Intervention ii = XmlUtil.parserJson2Intervention(interventionStr);
			if (null != ii) {
				this.intervention = ii;
			}
		}
	}

	public QGroup getGroup() {
		return group;
	}

	public void setGroup(QGroup group) {
		this.group = group;
		groupStr = null;
		if (null != group) {
			groupStr = XmlUtil.parserQGroup2Json(group);
		}
	}

	public String getGroupStr() {
		return groupStr;
	}

	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
		group = null;
		if (!Util.isEmpty(groupStr)) {
			group = XmlUtil.parserJson2QGroup(groupStr);
		}
	}

	public ArrayList<GRestriction> getGRestArr() {
		return gRestArr;
	}

	public void setGRestArr(ArrayList<GRestriction> gRestArr) {
		this.gRestArr = gRestArr;
		if (!Util.isEmpty(gRestArr)) {
			this.gRestStr = XmlUtil.parserGRestArr2Json(gRestArr);
			Log.i("@@@", "gRestArr=" + gRestArr.toString());
			Log.i("@@@", "gRestStr=" + gRestStr);
		}
	}

	public String getGRestStr() {
		return gRestStr;
	}

	public void setGRestStr(String gRestStr) {
		this.gRestStr = gRestStr;
		Log.i("@@@", gRestStr);
		if (!Util.isEmpty(gRestStr)) {
			ArrayList<GRestriction> _gRestArr = XmlUtil
					.parserJson2GRestArr(gRestStr);
			if (!Util.isEmpty(_gRestArr)) {
				this.gRestArr = _gRestArr;
			}
		}
	}

	public void setQgRestItemArr(ArrayList<QgRestriction> QgRestItemArr) {
		this.QgRestItemArr = QgRestItemArr;
		if (!Util.isEmpty(QgRestItemArr)) {
			Log.i("@@@", "QgRestItemArr=" + QgRestItemArr.toString());
			this.QgRestItemStr = XmlUtil
					.jsonArr2QgRestrictionStr(QgRestItemArr);
			Log.i("@@@", "QgRestItemStr=" + QgRestItemStr);
		}
	}

	public ArrayList<QgRestriction> getQgRestItemArr() {
		return QgRestItemArr;
	}

}
