package com.investigate.newsupper.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.ImageAdapter;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.bean.CstmMatcher;
import com.investigate.newsupper.bean.Data;
import com.investigate.newsupper.bean.Group;
import com.investigate.newsupper.bean.Logic;
import com.investigate.newsupper.bean.LogicItem;
import com.investigate.newsupper.bean.LogicList;
import com.investigate.newsupper.bean.MSG.DataBean;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.QGroup;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.bean.Repeat;
import com.investigate.newsupper.bean.Restriction;
import com.investigate.newsupper.bean.ReturnType;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.Task;
import com.investigate.newsupper.bean.TempGroup;
import com.investigate.newsupper.bean.TempLogic;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.TaskType;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.pageview.MenuHorizontalScrollView;
import com.investigate.newsupper.pageview.MenuListAdapter;
import com.investigate.newsupper.service.FeedXml;
import com.investigate.newsupper.service.FileUpLoad;
import com.investigate.newsupper.service.MyLocation;
import com.investigate.newsupper.service.VersionInfo;
import com.investigate.newsupper.service.XmlService;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.CheckAudioPermission;
import com.investigate.newsupper.util.CheckGps;
import com.investigate.newsupper.util.ComUtil;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.DialogListener;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.ImsIntervetion;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtils;
import com.investigate.newsupper.util.Publisher;
import com.investigate.newsupper.util.Publisher.Subscriber;
import com.investigate.newsupper.util.Publisher.SubscriberKey;
import com.investigate.newsupper.util.SDCardUtils;
import com.investigate.newsupper.util.SystemUtil;
import com.investigate.newsupper.util.ThreeLeverUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.WritePadDialog;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.util.limitlis.AshingBean;
import com.investigate.newsupper.util.limitlis.AshingQidBean;
import com.investigate.newsupper.util.limitlis.LimitlistUtil;
import com.investigate.newsupper.view.DialogBulder;
import com.investigate.newsupper.view.EditTextListView;
import com.investigate.newsupper.view.HotalkMenuView;
import com.investigate.newsupper.view.PopMenu;
import com.investigate.newsupper.view.Toasts;
import com.investigate.newsupper.view.UIEditText;
import com.investigate.newsupper.xhttp.Xutils;


/**
 * @author kejunyao 原生模式访问
 */
public class NativeModeNoPageActivity extends BaseActivity implements
		com.investigate.newsupper.view.PopMenu.OnItemClickListener, Subscriber {
	GestureDetector mGesture = null;
	//	自动上传相关   访问中98   访问结束100
	String uploadtype = "98";
	/**
	 * 显示定位成功与否的图标
	 */
	private ImageView locationshow;
	/**
	 * 有没有bos账号
	 */
	String isbos = "";
	private BDLocation mBDLocation;
	private Intent locIntent;
	/**
	 * 答卷信息实体
	 */
	private UploadFeed feed;

	/**
	 * IMS 皮炎湿疹
	 */
	ImsIntervetion imsIntervetion;
	/**
	 * 皮炎湿疹 提示 逻辑错误
	 */
	private final int LUO_JI_WRONG = 85;
	/**
	 * 某一张问卷的所有问题
	 */
	ArrayList<Question> qss;
	/**
	 * 当前问题实体
	 */
	private Question question;
	/**
	 * 一页多题某一张问卷的所有问题
	 */
	ArrayList<ArrayList<Question>> aryQs = new ArrayList<ArrayList<Question>>();
	/**
	 * 某一张问卷的所有问题
	 */
	ArrayList<Question> qs;

	private MyApp ma;

	/**
	 * 当前题目在ArrayList<Question> qs中的下标
	 */
	private int index;
	/**
	 * 一页多题当前页码
	 */
	private int pageNum;
	private int adpter_text_size;
	/**
	 * 当前问题实体
	 */
	/**
	 * 答卷界面顶部的说明信息
	 */
	TextView tvBar;
	/**
	 * 保存问题中所有有效的控件 是每道题所有有效控件的集合
	 */
	ArrayList<ArrayList<View>> vss = new ArrayList<ArrayList<View>>();

	/**
	 * 引用其它问题的答案
	 */
	/**
	 * 问题横向、纵向包裹界面 用于界面震动
	 */
	// private ListView ll;
	private LinearLayout ll;
	private Button nq_btn, bq_btn;
	/**
	 * 设备屏幕的宽
	 */
	private int screenWidth;
	private float popUpWidth;
	/**
	 * 设备屏幕的高
	 */
	private int screenHeight;

	/**
	 * 显示器设备
	 */
	private Display dis;
	/**
	 * 录音
	 */
	private volatile MediaRecorder mRecorder;
	/**
	 * 录音按钮是否点了
	 */
	private volatile boolean isClicked = false;

	/**
	 * 当前录音文件
	 */
	private volatile File recordFile;

	/**
	 * 顶部
	 */
	private View vTopBar;

	/**
	 * 底部经纬度及录音按钮、拍照按钮
	 */

	/**
	 * 问卷访问结果显示视图
	 */
	private View vResult;
	/**
	 * 弹窗提示视图
	 */
	private View vPopupLL;
	private TextView vPopupTv;
	/**
	 * 访问界面包含题干等界面在内
	 */
	private View vVisit;

	/**
	 * 上一页
	 */
	private final int MSG_PRE = 1;

	/**
	 * 下一页
	 */
	private final int MSG_NEXT = 2;

	/**
	 * 录音
	 */
	private final int MSG_RECORD = 3;

	/**
	 * 中间保存答卷的xml
	 */
	private final int MSG_SAVE = 4;

	/**
	 * 最终保存答卷的xml
	 */
	private final int MSG_WRITE = 5;

	/**
	 * 新建当前问卷的访问
	 */
	private final int MSG_NEW_FEED = 6;

	/**
	 * 重现,即继续完成没有完成的答卷
	 */
	private final int MSG_REDIRECT = 7;

	/**
	 * 题目选项答的个数小于预定最小值,非矩阵题
	 */
	private final int STATE_BOUND_LOWER = 8;

	/**
	 * 题目选项答的个数超过预定最大值,非矩阵题
	 */
	private final int STATE_BOUND_UPPER = 9;

	/**
	 * 选项选中的个数小于给定的最小值,矩阵题
	 */
	private final int STATE_BOUND_MATRIX_LOWER = 10;
	/**
	 * 选项选中的个数大于给定的最大值,矩阵题
	 */
	private final int STATE_BOUND_MATRIX_UPPER = 11;

	/**
	 * 同一列中选项连续选择的个数不在预先给定的范围内
	 */
	private final int STATE_CONTINUOUS = 12;

	/**
	 * 小于文本给定的最小值
	 */
	private final int STATE_SYB_MIN = 13;

	/**
	 * 大于文本框给定的最大值
	 */
	private final int STATE_SYB_MAX = 14;

	/**
	 * 文本框要求填写数字,而填写的不是数字
	 */
	private final int FORMAT_NO_NUMBER = 15;

	/**
	 * 每一行选择的个数小于给定的个数
	 */
	private final int STATE_ROW_LESS = 16;

	/**
	 * 什么都没做,用于必答题什么都没答的情况
	 */
	private final int STATE_NOTHING = 17;

	/**
	 * 关闭界面
	 */
	private final int MSG_FINISH = 18;

	private final int STATE_SYB_REPEAT = 19;

	/**
	 * 和上面比较等于日期不正确。
	 */
	private final int STATE_SYB_DATE_EQUAL = 20;
	/**
	 * 和上面比较大于等于日期不正确。
	 */
	private final int STATE_SYB_DATE_UPPER_EQUAL = 21;
	/**
	 * 和上面比较不等于日期不正确。
	 */
	private final int STATE_SYB_DATE_NO_EQUAL = 22;

	/**
	 * 和上面比较大于日期不正确。
	 */
	private final int STATE_SYB_DATE_UPPER = 23;
	/**
	 * 和上面比较小于日期不正确。
	 */
	private final int STATE_SYB_DATE_LOW_EQUAL = 24;
	/**
	 * 和上面比较小于等于日期不正确。
	 */
	private final int STATE_SYB_DATE_LOW = 25;
	/**
	 * 不是整数或者小数
	 */
	private final int FORMAT_NO_FLOAT_NUMBER = 26;
	/**
	 * 总和是不是满足条件
	 */
	private final int STATE_SYB_SUM_VALUE = 27;
	/**
	 * 不是数字或者英文
	 */
	private final int FORMAT_NO_NUMBER_AND_WORD = 28;
	/**
	 * 不是标准邮箱
	 */
	private final int FORMAT_NO_EMAIL = 29;
	/**
	 * 是否是单题拍照的验证码
	 */
	private final int FORMAT_NO_CAMERA = 30;
	/**
	 * 是否单题签名验证码
	 */
	private final int FORMAT_NO_SIGN = 33;
	/**
	 * 旧版本的数字之和
	 */
	private final int TOTAL_SUM_TOAST = 31;
	/**
	 * 单项是否满足
	 */
	private final int ITEM_NO_REQUIRED = 32;

	/**
	 * 题外关联 之和 的 判断 ！ 如果不想等 则给出提示 大树 外部关联 1
	 */
	private final int QUESTION_OUT_YING_RELEVANCE = 50;

	/**
	 * 题外关联之 内部关联 判断 提示 大树 内部关联 1
	 */
	private final int QUESTION_INTERIOR_RELEVANCE = 51;
	/**
	 * IMS临时干预价格 小数点后必须保留两位有效数字
	 */
	private final int DECIMAL_PLACES_NO_MEET = 52;
	/**
	 * 大树排序 判断是否重复 如果选项重复提示
	 */
	private final int QUESTION_ITEM_ORDER_REPEAT = 53;
	public static final int ADD_BODY = 54;
	public static final int REFRESH = 55;
	public static final int HIDE_BODY = 56;
	public static final int SHOW_BODY = 556;
	public static final int ADD_ALL_BODY = 57;
	public static final int ADD_ALL = 58;
	private final int UPLOAD_AGIN = 59;
	private final int MSG_REDIRECT_BACK = 60;
	ExecutorService es = Executors.newSingleThreadExecutor();
	/**
	 * 排序题若无限制则必须全选
	 */
	private final int STATE_SORT_CHECK = 61;
	/**
	 * 是否强制判断文本框的值
	 */
	private final int STATE_SYB_MIN_RANGE = 62;
	private final int STATE_SYB_MAX_RANGE = 63;
	/**
	 * 和上面比较等于长度不正确。
	 */
	private final int STATE_SYB_LEN_EQUAL = 64;
	/**
	 * 和上面比较大于等于长度不正确。
	 */
	private final int STATE_SYB_LEN_UPPER_EQUAL = 65;
	/**
	 * 和上面比较不等于长度不正确。
	 */
	private final int STATE_SYB_LEN_NO_EQUAL = 66;

	/**
	 * 和上面比较大于长度不正确。
	 */
	private final int STATE_SYB_LEN_UPPER = 67;
	/**
	 * 和上面比较小于长度不正确。
	 */
	private final int STATE_SYB_LEN_LOW_EQUAL = 68;
	/**
	 * 和上面比较小于等于长度不正确。
	 */
	private final int STATE_SYB_LEN_LOW = 69;
	/**
	 * 单选矩阵每列选择数超过限制
	 */
	private final int STATE_CHECK_COUNT = 70;
	/**
	 * 连续选择的个数到达给定数量提醒新版
	 */
	private final int STATE_CONTINUOUS_NEW = 71;

	/**
	 * 大树排序 重复选项值
	 */
	private ArrayList<Integer> orderRepeatList;
	private HashMap<Integer, String> orderRepeatMap;
	/**
	 * 文本框最大值、最小值的中间保存变量
	 */
	private int syb;

	/**
	 * 写XML文件用到的类
	 */
	private FeedXml fm = new FeedXml();

	/**
	 * 提示性对话框
	 */
	private Dialog mDialog;

	/**
	 * 控制结果界面是否显示
	 */
	private boolean isShow = true;

	/**
	 * 下方追缴说明图片显示布局
	 */

	private final LayoutParams FILL_WRAP = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private final LayoutParams WRAP_WRAP = new LinearLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private SlidingDrawer sdImages;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private ImageAdapter mImageAdapter;

	private TextView tvImageCount;

	private int screen;

	// private boolean mapMonitor;

	// 存放下拉的键值
	/** 之前某某题目的项 **/
	int preIndex = 0;
	/** 之前某某题目的A1号 **/
	String preTitle = "";
	/** 指定字符串长度 **/
	String strLen = "";
	/**
	 ** 总数值为0
	 **/
	double sum = 0;
	// 屏幕的宽度
	private int maxCWidth;
	/** 全局进度条 **/
	private ProgressBar globleProgress;

	/**
	 * 辅助题组随机 根据问题的index值找到该题目在ArrayList<Question> qs中的位置
	 */
	HashMap<Integer, Integer> iiMap = new HashMap<Integer, Integer>();

	/**
	 * 用于记录一个题组随机是否完成
	 */
	private QGroup qGroup = new QGroup();

	private boolean isGroup;

	private ArrayList<Integer> tempIndexArr = new ArrayList<Integer>();

	/**
	 * 判断是否是单题拍照
	 */
	private boolean isHaveSingle;
	/*** ZZ题组随机 */
	List<String> arrList = new ArrayList<String>();
	// 手动循环题组随机的顺序
	private String groupSequence;

	/*** ZZ题组随机 */

	// 问卷字号动态设置
	private int surveySize;// 最小 的字体
	private int lowSurveySize;// 说明字体大小
	private int middleSueveySize;// 中等字体
	private int bigSurveySize;// 大字体

	/**
	 * 逻辑次数跳转
	 */
	private LogicList logicList;

	// 目录开始
	private MenuHorizontalScrollView scrollView; // 侧滑
	private LinearLayout my_btn; // 侧滑效果
	private ListView menuList; // 显示的目录list
	private MenuListAdapter menuListAdapter;// 目录 adapter
	private View[] children; // 左右两边
	private LayoutInflater inflater;// 条件器
	private ArrayList<Integer> dtOrderList = new ArrayList<Integer>();// 存取目录显示值
	private boolean isQgroupOrLogic = false;// 判断是否是题组随机题目 或者是逻辑题

	// get set方法
	/*
	 * public MenuHorizontalScrollView getScrollView() { return scrollView; }
	 * 
	 * public void setScrollView(MenuHorizontalScrollView scrollView) {
	 * this.scrollView = scrollView; }
	 */
	// 目录结束

	// 单复选矩阵固定开始
	// private TextView tvTitle_new;
	// private TextView tvCaption_new;
	// private RelativeLayout rlQuestion_new;
	// private LinearLayout llCaption_new;
	// LinearLayout bodyView_new;
	// private LinearLayout ltitle_tv_new;
	// private ImageView ivSign_new;
	// 单复选矩阵固定结束

	// 三级联动 开始
	/**
	 * 一级 城市 集合
	 */
	private ArrayList<String> city = null;

	/**
	 * 二级 区域 集合 键值对
	 */
	private static HashMap<String, ArrayList<String>> area = null;

	/**
	 * 三维 街道 集合 兼职对
	 */
	private static HashMap<String, ArrayList<String>> way = null;

	/**
	 * 二级 区部 集合 临时 变量
	 */
	private ArrayList<String> areaListTemp = null;
	/**
	 * 三级 街道 集合 临时变量
	 */
	private ArrayList<String> wayListTemp = null;

	private String s1 = "";
	private String s2 = "";
	private String s3 = "";

	int cityPos = 0;
	int areaPos = 0;

	private Spinner provinceSpinner = null;
	private Spinner citySpinner = null;
	private Spinner countrySpinner = null;
	private ArrayAdapter<String> provinceAdapter = null;
	private ArrayAdapter<String> countryAdapter = null;
	private ArrayAdapter<String> cityAdapter = null;
	private RelativeLayout re_btn;
	/**
	 * 大树拒访 定义数据类型
	 */
	private Button btn_title_choice; // 大树拒访
	private PopMenu popMenu; // 大树拒访
	private ArrayList<ReturnType> rtList; // 拒绝访问 返回类型 集合
	private int returnTypeId = -2; // 初始为-2证明没选。 访问状态
	private String returnName = "";// 访问状态名称

	// 三级联动 结束
	private static final String TAG = "zrl1"; // 本地测试
	public int UPLOAD_COUNT = 0;
	private ProgressBar proBar;
	private ScrollView scroll_view;
	private Survey survey;
	private int verifyFlag;
	private int dialogBtnSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/**
		 * 应用的上下文
		 */
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(NativeModeNoPageActivity.this);
		}
		/**
		 * 0竖屏 1横屏 默认横屏 显示
		 */
		screen = ma.cfg.getInt("ScreenOrientation", 0);

		/**
		 * 0竖屏 1横屏 默认横屏 显示
		 */
		// mapMonitor = ma.cfg.getBoolean("mapMonitor", false);
		dialogBtnSize = (int) (UIUtils.getDimenPixelSize(R.dimen.sry_text_big) * TextSizeManager
				.getRealScale());
		surveySize = (int) (UIUtils.getDimenPixelSize(R.dimen.survey_text_size) * TextSizeManager
				.getInstance().getRealScale());
		lowSurveySize = (int) (surveySize);
		middleSueveySize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.survey_middle_text_size) * TextSizeManager
				.getInstance().getRealScale());
		bigSurveySize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.survey_big_text_size) * TextSizeManager
				.getInstance().getRealScale());
		/**
		 * 初始化问卷字号动态设置完毕
		 */
		/**
		 * 横竖屏设置
		 */
		if (1 == screen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// 目录开始 所有的acbuwaPage
		setContentView(R.layout.activity_native_nopage);
		inflater = LayoutInflater.from(this);
		Publisher.getInstance().addSubscriber(this);
		// this.scrollView = (MenuHorizontalScrollView)
		// findViewById(R.id.mScrollView);
		// this.menuList = (ListView) findViewById(R.id.menuList);
		my_btn = (LinearLayout) findViewById(R.id.my_btn);
		// mGesture = new GestureDetector(this, new GestureListener());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		LayoutParams params = new LayoutParams(screenWidth / 12,
				screenHeight / 12);
		popUpWidth = (float) (((float) screenWidth) * 0.85);
		// 目录 滑动横竖屏bug。
		/*
		 * if (1 == screen) { // 1280*752 // 假如宽小于高，没有真正横屏 if (screenWidth <
		 * screenHeight) { this.scrollView.setLayoutParams(new
		 * FrameLayout.LayoutParams(screenHeight, screenWidth)); } }
		 */
		// 目录 滑动横竖屏bug结束

		// my_btn.setLayoutParams(params);
		my_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// scrollView.clickMenuBtn();
				if (View.VISIBLE != vResult.getVisibility()) {
					if (isRecording) {// 如果是正在录音则等待0.1秒
						closeQRecord();
						new Handler().postDelayed(new Runnable() {
							public void run() {
								// execute the task
								isShow = false;
								handler.sendEmptyMessage(MSG_SAVE);
							}
						}, 100);
					} else {// 否则直接下一页
						isShow = false;
						handler.sendEmptyMessage(MSG_SAVE);
					}

				}
			}
		});
		/*
		 * View leftView = new View(this);
		 * leftView.setBackgroundColor(Color.TRANSPARENT);
		 * leftView.setBackgroundResource(R.drawable.menu_bg); //children = new
		 * View[] { leftView, acbuwaPage }; children = new View[] { null,
		 * acbuwaPage }; this.scrollView.initViews(children, new
		 * SizeCallBackForMenu(this.my_btn), this.menuList);
		 * this.scrollView.initViews(children, new
		 * SizeCallBackForMenu(this.my_btn), null);
		 * this.scrollView.setMenuBtn(this.my_btn);
		 */
		// 目录结束
		/**
		 * 获取屏幕设备
		 */
		dis = getWindowManager().getDefaultDisplay();
		maxCWidth = (int) (screenWidth * 0.94);
		/**
		 * 问题限制提示对话框
		 */
		mDialog = new Dialog(NativeModeNoPageActivity.this, R.style.question_ds);
		/**
		 * 设置其界面
		 */
		mDialog.setContentView(R.layout.question_promot_dialog);

		/**
		 * 按键禁用,比如点返回按钮时,对话框不消失
		 */
		mDialog.setOnKeyListener(new MyOnKeyListener());

		/**
		 * 获取上一个界面传过来的对象
		 */
		feed = (UploadFeed) getIntent().getExtras().get("feed");
		survey = feed.getSurvey();
		
		if(survey.getAppAutomaticUpload() == 1){
			uploadtype = "100";
		}
		
		verifyFlag = feed.isTestMode();
		locIntent = new Intent(ma, MyLocation.class);
		if (2 != survey.forceGPS && !Util.isEmpty(feed.getLat())
				&& Cnt.LOC_SERVICE_START) {
			if (!(1 == survey.openGPS && 0 < survey.timeInterval)) {
				System.out.println("问卷创建");
				ma.stopService(locIntent);
				Cnt.LOC_SERVICE_START = false;
			}
		}
		
		
		/**
		 * 获取bos信息
		 */
		String AccessKeyID = ma.cfg.getString(Cnt.AK, "");
		if (!Util.isEmpty(AccessKeyID)) {
			isbos = "true";
		}else{
			isbos = "false";
		}
		/**
		 * 答题的界面ll.set
		 */
		
		//定位显示图标
		locationshow = (ImageView) findViewById(R.id.img_locationshow);
		/**
		 * 显示隐藏定位图标
		 */
		showlocationicon(survey);
		vVisit = findViewById(R.id.visit_sv);
		// ll = (ListView) findViewById(R.id.quesiont_ll);
		scroll_view = (ScrollView) findViewById(R.id.scroll_view);
		ll = (LinearLayout) findViewById(R.id.nopage_ll);
		nq_btn = (Button) findViewById(R.id.nq_btn);
		bq_btn = (Button) findViewById(R.id.bq_btn);
		RelativeLayout.LayoutParams bqParams = new RelativeLayout.LayoutParams(
				(int) (screenWidth * 13 / 37), LayoutParams.WRAP_CONTENT);
		// bqParams.topMargin = screenHeigh / 8 * 1 / 5;
		bqParams.leftMargin = (screenWidth * 3 / 37);
		bqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		// bqParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bq_btn.setLayoutParams(bqParams);
		RelativeLayout.LayoutParams nqParams = new RelativeLayout.LayoutParams(
				(int) (screenWidth * 15 / 37), LayoutParams.WRAP_CONTENT);
		nqParams.addRule(RelativeLayout.RIGHT_OF, R.id.bq_btn);
		// nqParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		nqParams.addRule(RelativeLayout.CENTER_VERTICAL);
		// nqParams.topMargin = screenHeigh / 8 * 1 / 5;

		nqParams.leftMargin = (screenWidth * 3 / 37);
		nq_btn.setLayoutParams(nqParams);
		nq_btn.setClickable(false);

		/**
		 * 进度条 控制
		 */
		globleProgress = (ProgressBar) findViewById(R.id.visit_progressBar);
		/**
		 * 显示标题
		 */

		/**
		 * 顶部上一页、下一页、标题等界面
		 */
		vTopBar = findViewById(R.id.title_rl);
		re_btn = (RelativeLayout) findViewById(R.id.re_btn);
		/**
		 * 顶部拍照、录音、经纬度等界面
		 */

		/**
		 * 访问完成后的结果显示界面
		 */
		vResult = findViewById(R.id.result_ll);
		/**
		 * 弹窗提示窗口
		 */
		vPopupLL = findViewById(R.id.popup_ll);
		vPopupLL.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("anxia");
				if (MotionEvent.ACTION_UP == event.getAction()) {
					vPopupLL.setVisibility(View.GONE);
					vPopupTv.setText("");
					vPopupTv.setVisibility(View.GONE);
				}
				return true;
			}
		});
		vPopupTv = (TextView) findViewById(R.id.popup_tv);
		/**
		 * 大树拒访 访问状态
		 */
		btn_title_choice = (Button) findViewById(R.id.btn_title_choice);
		btn_title_choice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择状态
				popMenu.showAsDropDown(btn_title_choice);
			}
		});
		popMenu = new PopMenu(this);
		rtList = feed.getSurvey().getRlist();
		// 有返回接口并且有内部名单
		if (!Util.isEmpty(rtList) && feed.getSurvey().openStatus == 1) {
			String[] arr = new String[rtList.size()];
			for (int i = 0; i < rtList.size(); i++) {
				arr[i] = rtList.get(i).getReturnName();
			}
			popMenu.addItems(arr);
			popMenu.setOnItemClickListener(this);
		} else {
			btn_title_choice.setVisibility(View.GONE);
		}

		/**
		 * //逻辑次数跳转JSON串
		 */
		String strLogicList = feed.getSurvey().strLogicList;
		if (!Util.isEmpty(strLogicList)) {
			logicList = XmlUtil.parserJsonToLogicList(strLogicList);
			/** 逻辑次数跳转开始 **/
			if (logicList != null) {
				isQgroupOrLogic = true;
				ArrayList<Logic> logics = logicList.getLogics();
				if (!Util.isEmpty(logics)) {
					for (Logic logic : logics) {
						// System.out.println("累加几次后跳:"+logic.getCountJump()+"/t跳到哪个index中:"+logic.getJumpIndex());
						int countJump = logic.getCountJump();
						int jumpIndex = logic.getJumpIndex();
						if (countJump == 0 || jumpIndex == 0) {
							continue;
						} else {
							indexList.add(jumpIndex);
						}
					}
				}
			}
		}
		/**
		 * //逻辑次数跳转JSON串结束
		 */
		if (null == feed) {
			finish();
		} else {
			groupSequence = feed.getGroupSequence();
		}
		tvBar = (TextView) findViewById(R.id.survey_title_tv);
		proBar = (ProgressBar) findViewById(R.id.pb_progressbar);
		LinearLayout.LayoutParams barparams = new LinearLayout.LayoutParams(
				(int) (screenWidth / 4 * 3),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvBar.setMaxLines(2);
		tvBar.setLayoutParams(barparams);
		/**
		 * 实例化抽屉控件 <!-- 目录开始 -->
		 */
		sdImages = (SlidingDrawer) findViewById(R.id.pic_sd);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				screenWidth, screenHeight);
		sdImages.setLayoutParams(layoutParams);
		/**
		 * 实例化抽屉控件的图标 <!-- 目录结束 -->
		 */
		// ivDragIcon = (ImageView) findViewById(R.id.drag_icon_iv);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(new CustomViewFactor());
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		g = (Gallery) findViewById(R.id.gallery);
		// g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(new CustomItemSelectListener());

		/**
		 * 实例化抽屉控件装载图片的GridView，控制平板本地预览
		 */
		sdImages.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				// System.out.println("onDrawerOpened()");
				/**
				 * 顶部的上一题、下一题界面隐藏掉
				 */
				vTopBar.setVisibility(View.GONE);
				/**
				 * 打开抽屉控件
				 */
				ArrayList<UploadFeed> images = ma.dbService.getImages(
						feed.getUuid(), feed.getSurveyId());
				if (null == tvImageCount) {
					tvImageCount = (TextView) findViewById(R.id.img_count_tv);
				}
				if (0 < images.size()) {
					// System.out.println("0<images.size()===" + images.size());
					if (null == mImageAdapter) {
						mImageAdapter = new ImageAdapter(
								NativeModeNoPageActivity.this, images);
						g.setAdapter(mImageAdapter);
					} else {
						mImageAdapter.refresh(images);
					}
					tvImageCount.setText("(" + 1 + "/" + images.size() + ")");
				} else {
					tvImageCount.setText("No Pictures");
				}
			}
		});
		sdImages.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				/**
				 * 关闭抽屉控件
				 */
				// ivDragIcon.setImageResource(R.drawable.arrow_up_float);
				/**
				 * 显示顶部的上一题、下一题按钮
				 */
				vTopBar.setVisibility(View.VISIBLE);
			}
		});
		// 目录判断结束
		new OrderTask().execute();
	}

	// -------------------------------------隐藏输入法-----------------------------------------------------
	// 获取点击事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	// 判定是否需要隐藏
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	// 小组差
	private ArrayList<String> diffList = new ArrayList<String>();
	// 大组差
	private ArrayList<String> qDiffList = new ArrayList<String>();
	// 每组的集合顺序
	private static HashMap<Integer, ArrayList<String>> orderMap = new HashMap<Integer, ArrayList<String>>();
	// 手动随机
	private static HashMap<Integer, TempGroup> dialogMap = new HashMap<Integer, TempGroup>();
	// 题组随机标识符
	int tempGroupRow = 0;
	// 记录有几个大组
	int groupSize = 0;
	// 为手动循环暂存的每组的集合顺序
	private static HashMap<Integer, ArrayList<String>> tempOrderMap = new HashMap<Integer, ArrayList<String>>();
	// 选择了几次
	private int choiceNum = -1;
	// 第一次的排序
	private ArrayList<String> allGroupOrder;
	// 第一次的排序位置
	private int firstGroupOrder = 0;
	// 最后一组的位置
	private int lastGroupOrder = 0;
	// 手动已经循环了几次
	private int handGroupNum = 0;
	// 记录每一次手动随机的位置。就是dialogMap key值
	private ArrayList<Integer> handList = new ArrayList<Integer>();
	// 每一次选择前的临时记录。
	private static HashMap<Integer, TempGroup> preDialogMap = new HashMap<Integer, TempGroup>();
	// 有几个手动循环组
	private int handGroupSum = 0;
	// 是否循环结束
	private boolean isFinishProgress;

	private final class OrderTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			/**
			 * 隐藏题目显示界面
			 */
			vVisit.setVisibility(View.GONE);
			globleProgress.setVisibility(View.VISIBLE);

			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			/**
			 * 获取本答卷中所有的问题
			 */
			qss = ma.dbService.getQuestionList(feed.getSurveyId());
			Log.i("zrl1", qss + "");

			// List<QGroup> qlist=new ArrayList<QGroup>();
			// 记录上一组的最后一个位置的数字
			int lastTemp = -1;
			// 记录上一大组最后一个位置的数字
			int bigLastTemp = -1;
			// 记录大组走的轨迹
			int x = 0;
			for (Question question : qss) {
				QGroup qGroup = question.getGroup();
				// 大组不为空
				if (null != qGroup) {
					// isQgroupOrLogic = true;
					if (x == 0) {
						firstGroupOrder = Integer.parseInt(qGroup.getIndex());
					}
					x += 1;
					groupSize += 1;
					if (x > 1) {
						// 得到触发大组的第一个位置
						String firstOrder = qGroup.getIndex();
						// 每个大组之间的间距
						int bigDiff = Integer.parseInt(firstOrder)
								- bigLastTemp;
						// 每个大组间距的值,每个值用','隔开
						String bigDiffContinue = "";
						for (int y = 1; y < bigDiff; y++) {
							int bigDiffSum = bigLastTemp + y;
							if (y == bigDiff - 1) {
								bigDiffContinue += bigDiffSum;
							} else {
								bigDiffContinue += bigDiffSum + ",";
							}
						}
						// 加入大组间距集合中
						if (!"".equals(bigDiffContinue)) {
							// 赋给全局变量
							qDiffList.add(bigDiffContinue);
						}
					}
					ArrayList<Group> groups = qGroup.getGroups();
					for (int j = 0; j < groups.size(); j++) {
						Group tempGroup = groups.get(j);
						// 得到每个小组的顺序号
						ArrayList<Integer> tempOrder = tempGroup.getIndexArr();
						if (!Util.isEmpty(tempOrder)) {
							// 小组第一个位置
							int first = tempOrder.get(0);
							// 小组最后一个位置
							int last = tempOrder.get(tempOrder.size() - 1);
							// 大于1才能进行比较
							if (j > 0) {
								String diffContinue = "";
								// 相邻小组的差
								int diff = first - lastTemp;
								// 遍历 得到小组中间差值
								for (int k = 1; k < diff; k++) {
									int diffSum = lastTemp + k;
									if (k == diff - 1) {
										diffContinue += diffSum;
									} else {
										diffContinue += diffSum + ",";
									}
								}
								// 加入小组间距集合中
								if (!"".equals(diffContinue)) {
									// 赋给全局变量
									diffList.add(diffContinue);
								}
							}
							lastTemp = last;
							if (j == groups.size() - 1) {
								// 得出最后的值
								bigLastTemp = last;
							}
						}
					}
				}
			}
			// 得到最后位置的序号
			lastGroupOrder = bigLastTemp;

			for (Question tpQuestion : qss) {
				QGroup tempQGroup = tpQuestion.getGroup();
				if (null != tempQGroup) {
					// 每一个大组中的所有小组list
					ArrayList<String> orderList = new ArrayList<String>();
					ArrayList<String> tempOrderList = new ArrayList<String>();
					ArrayList<Group> tempGroups = tempQGroup.getGroups();
					// 假如大体组随机，进行随机操作
					if (QGroup.GROUP_TYPE_AUTO == tempQGroup.getGroupType()) {
						Collections.shuffle(tempGroups);
						// 手动循环情况下
					} else if (QGroup.GROUP_TYPE_HAND == tempQGroup
							.getGroupType()) {
						/*
						 * handGroupSum++; // 获得大题组名字 String bigGroupName =
						 * tempQGroup.getGroupName(); // 获得大题组顺序号 int bigOrder =
						 * tempQGroup.getOrder(); // 获得小题组的顺序号
						 * ArrayList<Integer> smallOrder = new
						 * ArrayList<Integer>(); // 获得对应该小题组号的小题名字
						 * HashMap<Integer, String> smallGroupMap = new
						 * HashMap<Integer, String>(); // 循环大题组 for (int zz = 0;
						 * zz < tempGroups.size(); zz++) { // 获得小题组 Group
						 * zzGroup = tempGroups.get(zz); // 获得小题组号 Integer
						 * zzOrder = zzGroup.getOrder(); // 获得小题组名字 String
						 * zzOrderName = zzGroup.getGroupName();
						 * smallOrder.add(zzOrder); smallGroupMap.put(zzOrder,
						 * zzOrderName); } // 初始化暂存随机题组 TempGroup tg = new
						 * TempGroup(bigGroupName, smallGroupMap, bigOrder,
						 * smallOrder); // 加入大集合
						 * dialogMap.put(Integer.parseInt(tempQGroup.getIndex())
						 * , tg);
						 * handList.add(Integer.parseInt(tempQGroup.getIndex())
						 * - 1);
						 */}
					// 遍历小题组
					for (int i = 0; i < tempGroups.size(); i++) {
						// 获得组内序号
						Group group = tempGroups.get(i);
						// 小组循环的话，进行循环
						if (group.isRandom()) {
							Collections.shuffle(group.getIndexArr());
							group.setIndexArr(group.getIndexArr());
						}
						orderList.add(group.getIndexStr());
						tempOrderList.add(group.getIndexStr());
					}
					// 通过每个大组唯一的order号给hashMap赋值
					orderMap.put(tempQGroup.getOrder(), orderList);
					// 给暂存组赋值
					tempOrderMap.put(tempQGroup.getOrder(), tempOrderList);
				}
			}

			System.out.println("大体组个数:" + groupSize);
			allGroupOrder = Util.getAllGroupOrder(diffList, qDiffList,
					orderMap, groupSize);
			System.out.println("最后的排序:" + allGroupOrder);
			// 标识符，作用只添加一次集合
			boolean isOnlyOne = true;
			// System.out.println("firstGroupOrder:" + firstGroupOrder +
			// "lastGroupOrder:" + lastGroupOrder);
			for (int i = 0; i < qss.size(); i++) {
				// 得到每个题目的order顺序号
				String orderStr = i + "";
				// 假如在上一个排序组中找到顺序号了，就按照排序组的顺序号走
				if (firstGroupOrder <= i && lastGroupOrder >= i) {
					if (isOnlyOne) {
						arrList.addAll(allGroupOrder);
						isOnlyOne = false;
					}
				} else {
					// 否则就按照已有的顺序走
					arrList.add(orderStr);
				}
			}
			System.out.println("整体最后的排序:" + arrList);
			// 题组随机结束

			// 目录处理判断有没有题组随机
			// 没有题组随机才能显示
			/*
			 * if (!isQgroupOrLogic) { if (!Util.isEmpty(feed.getDirectory())) {
			 * String[] arrDirectory = feed.getDirectory().split(","); for
			 * (String strDirectory : arrDirectory) {
			 * dtOrderList.add(Integer.parseInt(strDirectory)); }
			 * recodeOrderList.add(0); } else { dtOrderList.add(0);
			 * recodeOrderList.add(0); } }
			 */
			return 0 < qss.size();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			new QuestionTask().execute();
			// 目录添加布局结束
			super.onPostExecute(result);
		}
	}

	/**
	 * 考虑到问题过多会影响界面的流畅 比如有的问卷有1000多道题,此时从数据库中将这些记录查出时耗时较多, 故在子线程中完成,然后在主线程中刷新界面
	 */
	private final class QuestionTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			/**
			 * 隐藏题目显示界面
			 */
			vVisit.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			int headNum = 0;
			Log.i("zrl1", qss + "");
			ArrayList<Question> aq = null;
			for (int i = 0; i < qss.size(); i++) {
				Question question = qss.get(i);
				if (0 == i) {
					aryQs.add(aryQs.size(), new ArrayList<Question>());
					aq = aryQs.get(aryQs.size() - 1);
				}
				if (Cnt.TYPE_PAGE == question.qType) {
					aryQs.add(aryQs.size(), new ArrayList<Question>());
					aq = aryQs.get(aryQs.size() - 1);
				}
				if (null != aq) {
					aq.add(question);
				}
				if (1 == question.qCamera) {
					isHaveSingle = true;
				}
				if (Cnt.TYPE_HEADER == question.qType) {
					headNum += 1;
				}
				if (headNum > 1) {
					// 目录右侧新增
					if (Util.isEmpty(question.qid)
							&& Cnt.TYPE_HEADER != question.qType) {
						question.qid = "Q" + (question.qOrder - headNum + 1);
					}
				} else {
					// 目录右侧新增
					if (Util.isEmpty(question.qid)
							&& Cnt.TYPE_HEADER != question.qType) {
						question.qid = "Q" + (question.qOrder);
					}
				}
				iiMap.put(question.qIndex, i);

				// System.out.println(question.qCamera + question.qCameraName);

			}
			if (pageNum < aryQs.size()) {
				qs = aryQs.get(pageNum);
			}
			return 0 < qss.size();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (1 < aryQs.size()) {
				bq_btn.setText(R.string.back_page);
				nq_btn.setText(R.string.next_page);
			}
			System.out.println("添加目录");
			// 目录添加布局
			/**
			 * 问卷中所有的问题查询完成后, 隐藏进度条
			 */
			// vProgress.setVisibility(View.GONE);
			// globleProgress.setVisibility(View.GONE);
			isFinishProgress = true;
			nq_btn.setClickable(true);
			if (result) {
				/**
				 * 刷新界面出现答题
				 */
				initData();
				// IMS 皮炎湿疹 临时干预
				imsIntervetion = new ImsIntervetion(Integer.valueOf(feed
						.getSurveyId()), ma, feed.getUuid());
			} else {
				/**
				 * 没有Android模式下的问卷, 可能问卷的zip包中没有xml形式的问卷
				 */
				Message msg = Message.obtain();
				msg.what = MSG_FINISH;
				msg.obj = getResources().getString(R.string.null_questions);
				handler.sendMessage(msg);
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 开启全局录音
	 * 
	 * @param params
	 */
	private void openGlobalRecord(HashMap<String, Object> params) {
		// 判断有没有录音权限
		if (CheckAudioPermission.isHasPermission(NativeModeNoPageActivity.this)) {
			MainService.newTask(new Task(TaskType.TS_RECORED, params));
		} else {
			Toasts.makeText(this, "请检查录音权限", Toast.LENGTH_LONG).show();
			if (View.VISIBLE != vResult.getVisibility()) {
				if (1 == feed.getIsCompleted()) {
					handler.sendEmptyMessage(MSG_WRITE);
				} else {
					isShow = false;
					handler.sendEmptyMessage(MSG_SAVE);
				}
			}
		}
	}

	/**
	 * 关闭单题录音
	 */
	private void closeQRecord() {
		// 关闭单题录音
		if (isRecording) {// 如果开启单题录音
			/**
			 * 关闭录音
			 */
			stop();
			isRecording = false;
			if (ivRecord != null) {
				ivRecord.setBackgroundResource(R.drawable.record_on);
				// ivRecord.setVisibility(View.GONE);
				// ivRecord.setOnClickListener(null);
			}
			Toasts.makeText(NativeModeNoPageActivity.this,
					R.string.record_close, Toast.LENGTH_SHORT).show();
			ma.dbService.updateRecord(recordFile.getName(),
					System.currentTimeMillis(), recordFile.length());
		}
	}

	/**
	 * 开启单题录音
	 * 
	 * @param params
	 */
	private void openQRecord(String num, ImageView selectIv, Question q) {
		if (null != selectIv) {
			new QRecordTask(isRecording, num, selectIv, q).execute();
		} else {
			new QRecordTask(isRecording, num, q).execute();
		}
	}

	/**
	 * 开启录音
	 * 
	 * @param params
	 */
	private void openRecord(String num, ImageView selectIv) {
		// 判断有没有录音权限
		if (CheckAudioPermission.isHasPermission(NativeModeNoPageActivity.this)) {
			if (null != selectIv) {
				new RecordTask(isClicked, num, selectIv).execute();
			} else {
				new RecordTask(isClicked, num).execute();
			}
		} else {
			Toasts.makeText(this, "请检查录音权限", Toast.LENGTH_LONG).show();
			if (View.VISIBLE != vResult.getVisibility()) {
				if (1 == feed.getIsCompleted()) {
					handler.sendEmptyMessage(MSG_WRITE);
				} else {
					isShow = false;
					handler.sendEmptyMessage(MSG_SAVE);
				}
			}
		}
	}

	private void initData() {
		if (null != feed) {

			/**
			 * 显示答题界面
			 */
			showVisitView();

			/**
			 * 初始化顶部控件的信息
			 */
			initBar();
			initProgress();
			/**
			 * 新建的
			 */
			if (Util.isEmpty(feed.getName())) {
				/**
				 * 数据库中新增一条答卷信息
				 */
				newFeed(System.currentTimeMillis());
				/**
				 * recordView、cameraView 根据问卷的要求设置其显示或隐藏
				 */
				if (null != feed.getSurvey()) {
					if (1 == feed.getSurvey().globalRecord) {
						// 注释录音
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("feed", feed);
						params.put("content", NativeModeNoPageActivity.this);
						params.put("isClicked", isClicked);
						params.put("q", qs.get(0));
						openGlobalRecord(params);
						// MainService.newTask(new Task(TaskType.TS_RECORED,
						// params));
					}
				}
				handler.sendEmptyMessage(ADD_ALL);
				// System.out.println("新建访问FileName=" + feed.getName());
				// ThreadPoolUtils.execute(new Runnable() {
				// public void run() {
				// if (!Util.isEmpty(qss)) {
				// for (int i = 0; i < qss.size(); i++) {
				// Message viewMsg = Message.obtain();
				// viewMsg.what = ADD_ALL_BODY;
				// viewMsg.arg1 = i;
				// viewMsg.obj = createQuestionBodyView(i);
				// handler.sendMessage(viewMsg);
				// }
				// }
				// }
				// });
			} else {
				/**
				 * 继续访问
				 */
				// System.out.println("继续访问FileName=" + feed.getName());
				feed.setStartTime(System.currentTimeMillis());
				/**
				 * recordView、cameraView 根据问卷的要求设置其显示或隐藏
				 */
				if (null != feed.getSurvey()) {
					if (1 == feed.getSurvey().globalRecord) {
						// 注释录音
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("feed", feed);
						params.put("content", NativeModeNoPageActivity.this);
						params.put("isClicked", isClicked);
						params.put("q", qs.get(0));
						openGlobalRecord(params);
						// MainService.newTask(new Task(TaskType.TS_RECORED,
						// params));
					}
				}
				handler.sendEmptyMessage(ADD_ALL);
				// ThreadPoolUtils.execute(new Runnable() {
				// public void run() {
				// if (!Util.isEmpty(qss)) {
				// for (int i = 0; i < qss.size(); i++) {
				// Message viewMsg = Message.obtain();
				// viewMsg.what = ADD_ALL_BODY;
				// viewMsg.arg1 = i;
				// viewMsg.obj = createQuestionBodyView(i);
				// handler.sendMessage(viewMsg);
				// }
				// }
				// }
				// });
			}
		} else {
			// 异常情况
			dismiss();
			return;
		}
	}

	private void showVisitView() {
		isShow = true;
		/**
		 * 显示顶部上一下、下一页、标题界面
		 */
		vTopBar.setVisibility(View.VISIBLE);
		re_btn.setVisibility(View.VISIBLE);
		/**
		 * 结果界面缩小隐藏
		 */
		Util.showView(NativeModeNoPageActivity.this, vResult, R.anim.zoom_out);
		vResult.setVisibility(View.GONE);
		/**
		 * 显示答题界面
		 */
		vVisit.setVisibility(View.VISIBLE);
		proBar.setVisibility(View.VISIBLE);
		/**
		 * 答题界面放大显示
		 */
		Util.showView(NativeModeNoPageActivity.this, vVisit, R.anim.zoom_in);
		/**
		 * 显示底部拍照、录音、经纬度
		 */
		// vBottomBar.setVisibility(View.VISIBLE);
		// z1加的
		sdImages.setVisibility(View.GONE);
		// 目录控制按钮
		// scrollView.isScorll = 0;
	}

	private void showResultView() {
		re_btn.setVisibility(View.GONE);
		vTopBar.setVisibility(View.GONE);
		proBar.setVisibility(View.GONE);
		Util.showView(NativeModeNoPageActivity.this, vVisit, R.anim.zoom_out);
		vVisit.setVisibility(View.GONE);
		vResult.setVisibility(View.VISIBLE);
		Util.showView(NativeModeNoPageActivity.this, vResult, R.anim.zoom_in);
		// vBottomBar.setVisibility(View.GONE);
		// z1加的
		sdImages.setVisibility(View.GONE);
		if (null != miPhoto) {
			miPhoto.setTitle(this.getString(R.string.open_photo));
		}
		// 单复选矩阵固定
		// rlQuestion_new.setVisibility(View.GONE);
		// 目录控制按钮
		// scrollView.isScorll = 1;
	}

	/**
	 * 初始化并显示顶部、底部Bar的数据信息
	 */
	private void initBar() {
		if (!Util.isEmpty(feed.getSurveyTitle())) {
			// 大树 修改
			// tvBar.setText(feed.getSurveyTitle() + "(1/" + qs.size() + ")");
			tvBar.setText(feed.getSurveyTitle());
		}
	}

	/**
	 * 初始化并显示顶部、底部Bar的数据信息
	 */
	private void initProgress() {
		if (!Util.isEmpty(feed.getSurveyTitle())) {
			// 大树 修改
			proBar.setProgress(1);
			proBar.setMax(aryQs.size());
		}
	}

	private void newFeed(long start) {
//		String text = "不分页：定位点信息是-lat:"+feed.getLat()+"lon:"+feed.getLng();
//		BaseToast.showLongToast(NativeModeNoPageActivity.this, text);
		feed.setIsUploaded(0);
		feed.setIsCompleted(0);
		feed.setFeedId("0");
		feed.setManyPlaces(null);
		feed.setManyTimes(null);
		feed.setSpent(0);
		feed.setLotsCoord(null);
		feed.setSize(0);
		feed.setPath(null);
		feed.setName(null);
		String uuid = UUID.randomUUID().toString();
		feed.setUuid(uuid);
		// 增加pid
		feed.setPid("0");
		// 命名规则

		String AUTHORID = ma.cfg.getString(Cnt.AUTHORID, "");
		String name = Util.getXmlName(AUTHORID, feed.getUserId(),
				feed.getSurveyId(), feed.getUuid(), feed.getPid(),
				feed.getParametersContent());

		feed.setName(name);// System.nanoTime()
		feed.setCreateTime(start);
		feed.setStartTime(start);
		feed.setReturnType("0");
		feed.setType(Cnt.FILE_TYPE_XML);
		String path = Util.getXmlPath(NativeModeNoPageActivity.this,
				feed.getSurveyId());
		feed.setPath(path);
		if (Util.isEmpty(feed.getUserId())) {
			feed.setUserId(ma.cfg.getString(Cnt.USER_ID, ""));
		}
		ma.dbService.addUploadFeed(feed);
	}

	@Override
	protected void onStart() {
		refreshView();
		super.onStart();
	}

	public void btnClick(View v) {
		if (Util.validateSyncClick()) {
			Toasts.makeText(NativeModeNoPageActivity.this,
					R.string.too_frequently, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.my_setting:
			super.openOptionsMenu();
			break;
		case R.id.right_btn:
			NativeModeNoPageActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.left_btn:
			showVisitView();
			break;
		// 退出
		case R.id.bq_btn:
			if (isRecording) {// 如果是正在录音则等待0.1秒
				closeQRecord();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// execute the task
						backPage();
					}
				}, 100);
			} else {// 否则直接下一页
				backPage();
			}
			break;
		// 提交
		case R.id.nq_btn: // 哑题封装
			
			
//			if (qs.size()) {
//				
//			}
			
			if (isRecording) {// 如果是正在录音则等待0.1秒
				closeQRecord();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// execute the task
						nextPage(false);
					}
				}, 100);
			} else {// 否则直接下一页
				nextPage(false);
			}
			break;
		}
	}

	/**
	 * 哑题返回上一页
	 */
	private void backPage() {
		operType = 1;
		if (1 < aryQs.size()) {
			scroll_view.scrollTo(1, 1);
			if (0 == pageNum) {
				Toasts.makeText(this, R.string.is_one_page, Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (0 < pageNum) {
				globleProgress.setVisibility(View.VISIBLE);
				setTopClick(false);
				pageNum--;
				es.execute(new Runnable() {
					public void run() {
						handler.sendEmptyMessage(ADD_ALL);
					}
				});
				// synchronized (ma) {
				// qs = aryQs.get(pageNum);
				// ll.removeAllViews();
				// handler.sendEmptyMessage(ADD_ALL);
				// }
			}
		} else {
			isShow = false;
			handler.sendEmptyMessage(MSG_SAVE);
		}
	}

	/**
	 * 哑题无验证的方法
	 */
	private void nextPage(boolean isNoValidate) {
		operType = 0;
		if (Util.isEmpty(qs)) {
			return;
		}
		for (int i = 0; i < qs.size(); i++) {
			Question q = qs.get(i);
			Answer tempAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex + "");
			
			if (tempAnswer != null) {
				BaseLog.w("nextPage-tempAnswer:"+tempAnswer.toString());
			}
			
			if (!Util.isEmpty(q.getResctItemArr())) {
				boolean isMatcher = Util.isMatcher(ma, q, feed.getUuid());
				if (!isMatcher) {
					if (null != tempAnswer) {
						/**
						 * 置为无效的答案
						 */
						ma.dbService.updateAnswerUnEnable(tempAnswer);
					}
					continue;
				}
			}
			/*获取题目编号*/
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid;
			} else {
				strTilte = getResources().getString(R.string.question_order,
						q.qOrder);
			}
			
			int state = getValidate(isNoValidate, q);// 题目验证
			System.out.println("state--------->" + state);
			


			
			
			
			// 为false就验证 不让过。
			if (!isNoValidate) {
				if (Cnt.STATE_SUCCESS != state) {
					switch (state) {
					case STATE_NOTHING:
						Message msg = Message.obtain();
						msg.what = STATE_NOTHING;
						msg.obj = strTilte
								+ getResources().getString(R.string.msg_must);
						handler.sendMessage(msg);
						break;
					case Cnt.STATE_CLASS_STOP:// 数据字典题判断不通过
					case STATE_SYB_MIN:// 文本框中填写的值小于给定的最小值
					case STATE_SYB_MAX:// 文本框中的值大于给定的最大值
					case Cnt.STATE_FAIL:// 选了其他项, 但是没有填写文本框里的内容
					case STATE_BOUND_LOWER:// 复选题目选项选择的个数小于给定的最小个数
					case STATE_BOUND_UPPER:// 复选题目选项选择的个数大于给定的最大个数
					case STATE_SORT_CHECK:// 复选排序题若无限制则需全部排序
					case STATE_BOUND_MATRIX_LOWER:// 复选矩阵一行选择的选项小于给定的下限
					case STATE_BOUND_MATRIX_UPPER:// 复选矩阵一行选择的选项大于给定的上限
					case STATE_CONTINUOUS:// 单选矩阵同一列中连续选择选项的个数大于给定的个数
					case STATE_ROW_LESS:// 必答题没有答完,例如本来有3行,只答1行或2行
						break;
					}
					return;
				}
			}
		}
		//f：没查重  t：有查重
		boolean checkrepeat = false;
			//本页有没有查重题
		for (int i = 0; i < qs.size(); i++) {
			Question thisq = qs.get(i);
			if (thisq.checkRepeat.equals("true")) {
				checkrepeat = true;
			}
		}
		
		
		if (checkrepeat) {
			//有没有网
			if (NetUtils.isConnected(NativeModeNoPageActivity.this)) {
				//查重上传
				uploadhttp(uploadtype);
			}else{
				//没网提示
				BaseToast.showLongToast(NativeModeNoPageActivity.this,R.string.exp_net);
				//设置按钮可用
				setTopClick(true);
			}
		}else {
			//不用查重就往下走
			loadnextpage();
		}
		
				

	}

	/**
	 * 单选哑题
	 * 
	 * @param item
	 * @param rb
	 * @param isDumbOk
	 * @param operType
	 * @param tempAnswer
	 */
	private HashMap<String, Object> setDumbRb(QuestionItem item,
			CompoundButton rb, boolean dumbOk, int operType, Answer tempAnswer,
			int qType) {
		QuestionItem iTem = item;
		boolean isDumbOk = dumbOk;
		item.isCheck = false;
		rb.setChecked(false);

		HashMap<String, Object> map = new HashMap<String, Object>();
		// 假如是哑题就清空
		if (!isDumbOk || 1 == qType) {
			String itemDumbList = iTem.dumbList;
			if (!Util.isEmpty(itemDumbList)) {
				String[] dumbLists = null;
				int match = 0;// 0为单个条件,1表示并且,2表示或者;
				if (-1 != itemDumbList.indexOf("&&")) {
					dumbLists = itemDumbList.split("&&");
					match = 1;
				} else {
					dumbLists = itemDumbList.split("\\|\\|");
					match = 2;
				}
				// 5,2:3:4 第一种题型
				// Region,1,北京 受访者属性题型 不包含为 1 包含为2
				// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
				// 哑题的List串不为空
				if (!Util.isEmpty(dumbLists)) {
					boolean isAllMatcher = true;
					boolean isOneMatcher = false;
					for (String dumbList : dumbLists) {
						dumbList = dumbList.trim();
						if (dumbList.indexOf(",") != -1) {
							if (dumbList.split(",").length > 2) {
								// 每项有两个逗号
								if (dumbList.indexOf("|") != -1) {
									// 文本题或的关系
									// 1:0,=,北京|1:0,=,天津 单行文本题型 符号
									// >、>=
									// 、<、=、!=
									// 受访者属性关系
									// Company,2,北京|Company,=,北京
									String[] twoCondition = dumbList
											.split("\\|");
									String firstCondition = twoCondition[0];
									String secondCondition = twoCondition[1];
									String[] dumbFirstArr = firstCondition
											.split(",");
									String[] dumbSecondArr = secondCondition
											.split(",");
									boolean isOneOk = false;
									boolean isTwoOk = false;
									if (dumbFirstArr.length == 3) {
										String[] dumbOneFirstArr = dumbFirstArr[0]
												.split(":");
										String[] dumbOneSecondArr = dumbSecondArr[0]
												.split(":");
										if (dumbOneFirstArr.length == 2
												&& dumbOneSecondArr.length == 2) {
											// 第一个条件
											String textIndex = dumbOneFirstArr[0];// 引用的题目index
											String textItemOrder = dumbOneFirstArr[1];// 引用的位置
											if (!Util.isEmpty(textIndex)
													&& !Util.isEmpty(textItemOrder)) {
												Answer textAnswer = ma.dbService
														.getAnswer(
																feed.getUuid(),
																textIndex);
												// 存取值的list
												ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
												if (null != textAnswer) {
													amTextList = textAnswer
															.getAnswerMapArr();
												}
												// 假如存取值的List不为空
												if (!Util.isEmpty(amTextList)) {
													// AnswerMap answerMap =
													// amTextList
													// .get(Integer.parseInt(textItemOrder.trim()));
													AnswerMap answerMap = new AnswerMap();
													for (AnswerMap am : amTextList) {
														if (am.getRow() == Integer
																.parseInt(textItemOrder
																		.trim())) {
															answerMap = am;
															break;
														}
													}
													String answerText = answerMap
															.getAnswerText();
													// 哑题改动
													Question question = ma.dbService
															.getQuestion(
																	feed.getSurveyId(),
																	textIndex);
													int type = -1;
													if (null != question) {
														ArrayList<QuestionItem> colItemArr = question
																.getColItemArr();
														if (!Util
																.isEmpty(colItemArr)) {
															QuestionItem questionItem = colItemArr
																	.get(Integer
																			.parseInt(textItemOrder
																					.trim()));
															if (null != questionItem) {
																type = questionItem
																		.getType();
															}
														}
													}
													if (-1 != type) {
														dumbFirstArr[1] = dumbFirstArr[1]
																+ ":" + type;
													}
													// 哑题改动匹配成功
													if (Util.isPass(answerText,
															dumbFirstArr[1],
															dumbFirstArr[2])) {
														isOneOk = true;
													} else {
														// 不满足什么都不做
													}
												} else {
													// 答案为空什么也不做
												}
											} else {
												// 服务器传的字符串为空什么也不做
											}

											// 第二个条件
											String textTwoIndex = dumbOneSecondArr[0];// 引用的index
											String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
											if (!Util.isEmpty(textTwoIndex)
													&& !Util.isEmpty(textTwoItemOrder)) {
												Answer textAnswer = ma.dbService
														.getAnswer(
																feed.getUuid(),
																textTwoIndex);
												// 存取值的list
												ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
												if (null != textAnswer) {
													amTextList = textAnswer
															.getAnswerMapArr();
												}
												// 假如存取值的List不为空
												if (!Util.isEmpty(amTextList)) {
													// AnswerMap answerMap =
													// amTextList
													// .get(Integer.parseInt(textTwoItemOrder.trim()));
													AnswerMap answerMap = new AnswerMap();
													for (AnswerMap am : amTextList) {
														if (am.getRow() == Integer
																.parseInt(textTwoItemOrder
																		.trim())) {
															answerMap = am;
															break;
														}
													}
													String answerText = answerMap
															.getAnswerText();
													// 哑题改动
													Question question = ma.dbService
															.getQuestion(
																	feed.getSurveyId(),
																	textIndex);
													int type = -1;
													if (null != question) {
														ArrayList<QuestionItem> colItemArr = question
																.getColItemArr();
														if (!Util
																.isEmpty(colItemArr)) {
															QuestionItem questionItem = colItemArr
																	.get(Integer
																			.parseInt(textItemOrder
																					.trim()));
															if (null != questionItem) {
																type = questionItem
																		.getType();
															}
														}
													}
													if (-1 != type) {
														dumbSecondArr[1] = dumbSecondArr[1]
																+ ":" + type;
													}
													// 哑题改动
													if (Util.isPass(answerText,
															dumbSecondArr[1],
															dumbSecondArr[2])) {
														isTwoOk = true;
													} else {
														// 不满足什么都不做
													}
												} else {
													// 答案为空什么也不做
												}
											} else {
												// 服务器传的字符串为空什么也不做
											}
										} else if (dumbOneFirstArr.length == 1
												&& dumbOneSecondArr.length == 1) {
											/**
											 * 引用受访者参数
											 */
											String parametersStr = feed
													.getParametersStr();
											ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
											if (!Util.isEmpty(parametersStr)) {
												parameterList.clear();
												ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
														.parseArray(
																parametersStr,
																Parameter.class);
												if (!Util.isEmpty(tParameters)) {
													parameterList
															.addAll(tParameters);
												}
											}
											String content = "";
											if (null != parameterList
													&& parameterList.size() > 0) {
												for (Parameter parameter : parameterList) {
													if (parameter
															.getSid()
															.trim()
															.equals(dumbFirstArr[0])) {
														System.out
																.println("content:"
																		+ content);
														content = parameter
																.getContent();
														break;
													}
												}
											} else {
												content = "";
											}
											// 假如查到content了，在判断是什么符号
											if (!Util.isEmpty(content)) {
												// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
												// 假如是数字
												if (Util.isFormat(
														dumbFirstArr[2], 9)
														&& Util.isFormat(
																content, 9)
														&& !Util.isFormat(
																dumbFirstArr[1],
																9)) {
													if (Util.isMath(content,
															dumbFirstArr[1],
															dumbFirstArr[2])) {
														isOneOk = true;
													}
												}
												// 日期
												else if (dumbFirstArr[1]
														.indexOf("-") != -1
														&& dumbFirstArr[1]
																.length() > 5) {
													try {
														if (Util.getDateCompare(
																dumbFirstArr[2],
																content,
																dumbFirstArr[1])) {
															isOneOk = true;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
												// 文本格式
												else {
													if (Util.isRespondentsMatching(
															content,
															dumbFirstArr[1],
															dumbFirstArr[2])) {
														isOneOk = true;
													}
												}
											}

											if (null != parameterList
													&& parameterList.size() > 0) {
												for (Parameter parameter : parameterList) {
													if (parameter
															.getSid()
															.trim()
															.equals(dumbSecondArr[0])) {
														content = parameter
																.getContent();
														break;
													}
												}
											} else {
												content = "";
											}

											// 假如查到content了，在判断是什么符号
											if (!Util.isEmpty(content)) {
												// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
												// 假如是数字
												if (Util.isFormat(
														dumbSecondArr[2], 9)
														&& Util.isFormat(
																content, 9)
														&& !Util.isFormat(
																dumbSecondArr[1],
																9)) {
													if (Util.isMath(content,
															dumbSecondArr[1],
															dumbSecondArr[2])) {
														isTwoOk = true;
													}
												}
												// 日期
												else if (dumbSecondArr[1]
														.indexOf("-") != -1
														&& dumbSecondArr[1]
																.length() > 5) {
													try {
														if (Util.getDateCompare(
																dumbSecondArr[2],
																content,
																dumbSecondArr[1])) {
															isTwoOk = true;
														}
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
												// 文本格式
												else {
													if (Util.isRespondentsMatching(
															content,
															dumbSecondArr[1],
															dumbSecondArr[2])) {
														isTwoOk = true;
													}
												}
											}

										}
									} else {

									}
									// 两者 满足一个
									if (isOneOk || isTwoOk) {
										if (1 == match) {// 并且关系

										} else {// 或者关系
											isOneMatcher = true;
											break;
										}
									} else {// 两者都不满足
										if (1 == match) {// 并且关系
											isAllMatcher = false;
											break;
										} else {// 或者关系

										}
									}
								} else if (dumbList.indexOf("&") != -1) {
									// 文本题与的关系
									// 1:0,=,北京&1:0,=,天津 单行文本题型 符号
									// >、>=
									// 、<、=、!=
									String[] twoCondition = dumbList.split("&");
									String firstCondition = twoCondition[0];
									String secondCondition = twoCondition[1];
									String[] dumbFirstArr = firstCondition
											.split(",");
									String[] dumbSecondArr = secondCondition
											.split(",");
									boolean isOneOk = false;
									boolean isTwoOk = false;
									String[] dumbOneFirstArr = dumbFirstArr[0]
											.split(":");// 题号和答案下标
									String[] dumbOneSecondArr = dumbSecondArr[0]
											.split(":");// 题号和答案下标
									if (dumbOneFirstArr.length == 2
											&& dumbOneSecondArr.length == 2) {
										// 第一个条件
										String textIndex = dumbOneFirstArr[0];// 引用的index
										String textItemOrder = dumbOneFirstArr[1];// 引用的位置
										if (!Util.isEmpty(textIndex)
												&& !Util.isEmpty(textItemOrder)) {
											Answer textAnswer = ma.dbService
													.getAnswer(feed.getUuid(),
															textIndex);
											// 存取值的list
											ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
											if (null != textAnswer) {
												amTextList = textAnswer
														.getAnswerMapArr();
											}
											// 假如存取值的List不为空
											if (!Util.isEmpty(amTextList)) {
												// AnswerMap answerMap =
												// amTextList
												// .get(Integer.parseInt(textItemOrder.trim()));
												AnswerMap answerMap = new AnswerMap();
												for (AnswerMap am : amTextList) {
													if (am.getRow() == Integer
															.parseInt(textItemOrder
																	.trim())) {
														answerMap = am;
														break;
													}
												}
												String answerText = answerMap
														.getAnswerText();
												// 哑题改动
												Question question = ma.dbService
														.getQuestion(feed
																.getSurveyId(),
																textIndex);
												int type = -1;
												if (null != question) {
													ArrayList<QuestionItem> colItemArr = question
															.getColItemArr();
													if (!Util
															.isEmpty(colItemArr)) {
														QuestionItem questionItem = colItemArr
																.get(Integer
																		.parseInt(textItemOrder
																				.trim()));
														if (null != questionItem) {
															type = questionItem
																	.getType();
														}
													}
												}
												if (-1 != type) {
													dumbFirstArr[1] = dumbFirstArr[1]
															+ ":" + type;
												}
												// 哑题改动
												if (Util.isPass(answerText,
														dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												} else {
													// 不满足什么都不做
												}
											} else {
												// 答案为空什么也不做
											}
										} else {
											// 服务器传的字符串为空什么也不做
										}

										// 第二个条件
										String textTwoIndex = dumbOneSecondArr[0];// 引用的index
										String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
										if (!Util.isEmpty(textTwoIndex)
												&& !Util.isEmpty(textTwoItemOrder)) {
											Answer textAnswer = ma.dbService
													.getAnswer(feed.getUuid(),
															textTwoIndex);
											// 存取值的list
											ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
											if (null != textAnswer) {
												amTextList = textAnswer
														.getAnswerMapArr();
											}
											// 假如存取值的List不为空
											if (!Util.isEmpty(amTextList)) {
												// AnswerMap answerMap =
												// amTextList
												// .get(Integer.parseInt(textTwoItemOrder.trim()));
												AnswerMap answerMap = new AnswerMap();
												for (AnswerMap am : amTextList) {
													if (am.getRow() == Integer
															.parseInt(textTwoItemOrder
																	.trim())) {
														answerMap = am;
														break;
													}
												}
												String answerText = answerMap
														.getAnswerText();
												// 哑题改动
												Question question = ma.dbService
														.getQuestion(feed
																.getSurveyId(),
																textIndex);
												int type = -1;
												if (null != question) {
													ArrayList<QuestionItem> colItemArr = question
															.getColItemArr();
													if (!Util
															.isEmpty(colItemArr)) {
														QuestionItem questionItem = colItemArr
																.get(Integer
																		.parseInt(textTwoItemOrder
																				.trim()));
														if (null != questionItem) {
															type = questionItem
																	.getType();
														}
													}
												}
												if (-1 != type) {
													dumbSecondArr[1] = dumbSecondArr[1]
															+ ":" + type;
												}
												System.out
														.println("dumbSecondArr[1]="
																+ dumbSecondArr[1]);
												// 哑题改动
												if (Util.isPass(answerText,
														dumbSecondArr[1],
														dumbSecondArr[2])) {
													isTwoOk = true;
												} else {
													// 不满足什么都不做
												}
											} else {
												// 答案为空什么也不做
											}
										} else {
											// 服务器传的字符串为空什么也不做
										}
									} else if (dumbOneFirstArr.length == 1
											&& dumbOneSecondArr.length == 1) {
										// 受访者属性题目

										/**
										 * 引用受访者参数
										 */
										String parametersStr = feed
												.getParametersStr();
										ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
										if (!Util.isEmpty(parametersStr)) {
											parameterList.clear();
											ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
													.parseArray(parametersStr,
															Parameter.class);
											if (!Util.isEmpty(tParameters)) {
												parameterList
														.addAll(tParameters);
											}
										}
										String content = "";
										if (null != parameterList
												&& parameterList.size() > 0) {
											for (Parameter parameter : parameterList) {
												if (parameter
														.getSid()
														.trim()
														.equals(dumbFirstArr[0])) {
													content = parameter
															.getContent();
													break;
												}
											}
										} else {
											content = "";
										}
										// 假如查到content了，在判断是什么符号
										if (!Util.isEmpty(content)) {
											// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
											// 假如是数字
											if (Util.isFormat(dumbFirstArr[2],
													9)
													&& Util.isFormat(content, 9)
													&& !Util.isFormat(
															dumbFirstArr[1], 9)) {
												if (Util.isMath(content,
														dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												}
											}
											// 日期
											else if (dumbFirstArr[1]
													.indexOf("-") != -1
													&& dumbFirstArr[1].length() > 5) {
												try {
													if (Util.getDateCompare(
															dumbFirstArr[2],
															content,
															dumbFirstArr[1])) {
														isOneOk = true;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 文本格式
											else {
												if (Util.isRespondentsMatching(
														content,
														dumbFirstArr[1],
														dumbFirstArr[2])) {
													isOneOk = true;
												}
											}
										}

										if (null != parameterList
												&& parameterList.size() > 0) {
											for (Parameter parameter : parameterList) {
												if (parameter
														.getSid()
														.trim()
														.equals(dumbSecondArr[0])) {
													content = parameter
															.getContent();
													break;
												}
											}
										} else {
											content = "";
										}

										// 假如查到content了，在判断是什么符号
										if (!Util.isEmpty(content)) {
											// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
											// 假如是数字
											if (Util.isFormat(dumbSecondArr[2],
													9)
													&& Util.isFormat(content, 9)
													&& !Util.isFormat(
															dumbSecondArr[1], 9)) {
												if (Util.isMath(content,
														dumbSecondArr[1],
														dumbSecondArr[2])) {
													isTwoOk = true;
												}
											}
											// 日期
											else if (dumbSecondArr[1]
													.indexOf("-") != -1
													&& dumbSecondArr[1]
															.length() > 5) {
												try {
													if (Util.getDateCompare(
															dumbSecondArr[2],
															content,
															dumbSecondArr[1])) {
														isTwoOk = true;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 文本格式
											else {
												if (Util.isRespondentsMatching(
														content,
														dumbSecondArr[1],
														dumbSecondArr[2])) {
													isTwoOk = true;
												}
											}
										}

									}
									// 两者 满足一个
									if (isOneOk && isTwoOk) {
										if (1 == match) {// 并且关系

										} else {// 或者关系
											isOneMatcher = true;
											break;
										}
									} else {// 两者都不满足
										if (1 == match) {// 并且关系
											isAllMatcher = false;
											break;
										} else {// 或者关系

										}
									}
								} else {
									String[] dumbArr = dumbList.split(",");
									// Company,2,北京 受访者属性题型 不包含为 1
									// 包含为2
									// 1:0,=,北京 单行文本题型
									String dumbOneStr = dumbArr[0];
									String[] dumbOneArr = dumbOneStr.split(":");
									// 1为受访者属性题型
									boolean isOneOk = false;
									if (dumbOneArr.length == 1) {
										/**
										 * 引用受访者参数
										 */
										String parametersStr = feed
												.getParametersStr();
										ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
										if (!Util.isEmpty(parametersStr)) {
											parameterList.clear();
											ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
													.parseArray(parametersStr,
															Parameter.class);
											if (!Util.isEmpty(tParameters)) {
												parameterList
														.addAll(tParameters);
											}
										}
										String content = "";
										if (null != parameterList
												&& parameterList.size() > 0) {
											for (Parameter parameter : parameterList) {
												if (parameter.getSid().trim()
														.equals(dumbArr[0])) {
													content = parameter
															.getContent();
													break;
												}
											}
										} else {
											content = "";
										}
										// System.out.println("0:"+content+"1:"+dumbArr[1]+"2:"+dumbArr[2]);
										// 假如查到content了，在判断是什么符号
										if (!Util.isEmpty(content)) {
											// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
											// 假如是数字
											if (Util.isFormat(dumbArr[2], 9)
													&& Util.isFormat(content, 9)
													&& !Util.isFormat(
															dumbArr[1], 9)) {
												if (Util.isMath(content,
														dumbArr[1], dumbArr[2])) {
													isOneOk = true;
												}
											}
											// 日期
											else if (dumbArr[1].indexOf("-") != -1
													&& dumbArr[1].length() > 5) {
												try {
													if (Util.getDateCompare(
															dumbArr[2],
															content, dumbArr[1])) {
														isOneOk = true;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 文本格式
											else {
												if (Util.isRespondentsMatching(
														content, dumbArr[1],
														dumbArr[2])) {
													isOneOk = true;
												}
											}
										}
									}
									// 2为单行文本题型 ,单选,复选,单选矩阵
									// 格式一样(1:0,=,北京)
									else if (dumbOneArr.length == 2) {
										// 1:0,=,北京 单行文本题型 符号 >、>=
										// 、<、=、!=
										String textIndex = dumbOneArr[0];// 引用的index
										String textItemOrder = dumbOneArr[1];// 引用的位置
										if (!Util.isEmpty(textIndex)
												&& !Util.isEmpty(textItemOrder)) {
											Answer textAnswer = ma.dbService
													.getAnswer(feed.getUuid(),
															textIndex);
											// 存取值的list
											ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
											if (null != textAnswer) {
												amTextList = textAnswer
														.getAnswerMapArr();
											}
											// 假如存取值的List不为空
											if (!Util.isEmpty(amTextList)) {
												// AnswerMap answerMap =
												// amTextList
												// .get(Integer.parseInt(textItemOrder.trim()));
												AnswerMap answerMap = new AnswerMap();
												for (AnswerMap am : amTextList) {
													if (am.getRow() == Integer
															.parseInt(textItemOrder
																	.trim())) {
														answerMap = am;
														break;
													}
												}
												String answerText = answerMap
														.getAnswerText();
												// 哑题改动
												Question question = ma.dbService
														.getQuestion(feed
																.getSurveyId(),
																textIndex);
												int type = -1;
												if (null != question) {
													ArrayList<QuestionItem> colItemArr = question
															.getColItemArr();
													if (!Util
															.isEmpty(colItemArr)) {
														QuestionItem questionItem = colItemArr
																.get(Integer
																		.parseInt(textItemOrder
																				.trim()));
														if (null != questionItem) {
															// 单行文本题的数据类型
															type = questionItem
																	.getType();
														}
													}
												}
												if (-1 != type) {
													dumbArr[1] = dumbArr[1]
															+ ":" + type;
												}
												// 哑题改动 该选项是否满足条件
												if (Util.isPass(answerText,
														dumbArr[1], dumbArr[2])) {
													isOneOk = true;
												}
											} else {
												// 答案为空什么也不做
											}
										} else {
											// 服务器传的字符串为空什么也不做
										}
									}
									if (isOneOk) {
										if (1 == match) {// 并且关系

										} else {// 或者关系
											isOneMatcher = true;
											break;
										}
									} else {// 两者都不满足
										if (1 == match) {// 并且关系
											isAllMatcher = false;
											break;
										} else {// 或者关系

										}
									}
								}
							} else {
								// 只有一个逗号
								// 单选、复选： dumbList只有一个逗号
								// >>规则：（index,itemvalue:itemvalue）
								String[] dumbArr = dumbList.split(",");
								// 哑题有引用的index和value值。
								// 5,2:3:4 第一种题型 (满足2:3:4中的一项即可)
								boolean isOneOk = false;
								if (dumbArr.length == 2) {
									String dumbIndex = dumbArr[0];// index
									String dumbValueStr = dumbArr[1];// value
									// 引用的那道哑题的答案
									Answer dumbAnswer = ma.dbService.getAnswer(
											feed.getUuid(), dumbIndex);
									// 存取值的list
									ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
									if (null != dumbAnswer) {
										dumbAmList = dumbAnswer
												.getAnswerMapArr();
									}
									// 假如存取值的List不为空
									if (!Util.isEmpty(dumbAmList)) {
										String[] dumbValueArr = dumbValueStr
												.split(":");// 数组
										// 循环储存的值
										for (AnswerMap dumbAm : dumbAmList) {
											String dumbValue = dumbAm
													.getAnswerValue();// 原有的值
											System.out.println("上一道的值asd:"
													+ dumbValue);
											// 假如答案和引用的value值相同
											for (String str : dumbValueArr) {
												System.out.println("本道逻辑可跳的值:"
														+ str);
											}
											if (Util.isHave(dumbValueArr,
													dumbValue)) {
												isOneOk = true;
												break;
											} else {

											}
										}
									} else {
										// 为空,什么都不做。
									}
								}
								if (isOneOk) {
									if (1 == match) {// 并且关系

									} else {// 或者关系
										isOneMatcher = true;
										break;
									}
								} else {// 两者都不满足
									if (1 == match) {// 并且关系
										isAllMatcher = false;
										break;
									} else {// 或者关系

									}
								}
							}
						} else {
							// 单复选矩阵题
							// index:rowvalue:cloumnvalue:cloumnvalue|rowvalue:cloumnvalue:cloumnvalue|..
							// 第一个:所在位置
							int firstStr = dumbList.indexOf(":");
							String dumbIndex = dumbList.substring(0, firstStr);// 所引用题的index
							String dumbValueArr = dumbList
									.substring(firstStr + 1);// 引用的条件
							// 引用的那道哑题的答案
							Answer dumbAnswer = ma.dbService.getAnswer(
									feed.getUuid(), dumbIndex);
							// 存取值的list
							ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
							if (null != dumbAnswer) {
								dumbAmList = dumbAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							boolean isOneOk = false;
							if (!Util.isEmpty(dumbAmList)) {
								if (dumbValueArr.indexOf("|") != -1) {
									String[] dumbValueStr = dumbValueArr
											.split("\\|");// 矩阵每行的条件
									// 循环储存的值
									for (int aw = 0; aw < dumbValueStr.length; aw++) {
										int firstValueStr = dumbValueStr[aw]
												.indexOf(":");
										String rowvalue = dumbValueStr[aw]
												.substring(0, firstValueStr);// 所引用题的行
										String cloumnvalue = dumbValueStr[aw]
												.substring(firstValueStr + 1);// 引用的条件
										String[] dumbValueAry = cloumnvalue
												.split(":");
										// AnswerMap dumbAm =
										// dumbAmList.get(Integer.parseInt(rowvalue.trim()));
										AnswerMap dumbAm = new AnswerMap();
										for (AnswerMap am : dumbAmList) {
											if (am.getRow() == Integer
													.parseInt(rowvalue.trim())) {
												dumbAm = am;
												break;
											}
										}
										String dumbValue = dumbAm
												.getAnswerValue();// 原有的值
										System.out
												.println("上一道的值:" + dumbValue);
										// 假如答案和引用的value值相同
										for (String str : dumbValueAry) {
											System.out.println("本道逻辑可跳的值:"
													+ str);
										}
										if (Util.isHave(dumbValueAry, dumbValue)) {
											isOneOk = true;
											break;
										} else {

										}

									}
								} else {
									int firstValueStr = dumbValueArr
											.indexOf(":");
									String rowvalue = dumbValueArr.substring(0,
											firstValueStr);// 所引用题的行
									String cloumnvalue = dumbValueArr
											.substring(firstValueStr + 1);// 引用的条件
									String[] dumbValueAry = cloumnvalue
											.split(":");// 配置的条件
									// AnswerMap dumbAm =
									// dumbAmList.get(Integer.parseInt(rowvalue.trim()));
									AnswerMap dumbAm = new AnswerMap();
									for (AnswerMap am : dumbAmList) {
										if (am.getRow() == Integer
												.parseInt(rowvalue.trim())) {
											dumbAm = am;
											break;
										}
									}
									String dumbValue = dumbAm.getAnswerValue();// 答案原有的值
									System.out.println("上一道的值:" + dumbValue);

									// 假如答案和引用的value值相同
									for (String str : dumbValueAry) {
										System.out.println("本道逻辑可跳的值:" + str);
									}
									if (Util.isHave(dumbValueAry, dumbValue)) {
										isOneOk = true;
									}
									// 为空,什么都不做。
								}
							}
							if (isOneOk) {
								if (1 == match) {// 并且关系

								} else {// 或者关系
									isOneMatcher = true;
									break;
								}
							} else {// 两者都不满足
								if (1 == match) {// 并且关系
									isAllMatcher = false;
									break;
								} else {// 或者关系

								}
							}
						}
					}
					if (1 == match && isAllMatcher || 2 == match
							&& isOneMatcher) {
						// 给清空答案
						// 只有下一页才清空
						if (MSG_NEXT == operType) {
							ma.dbService.updateAnswer2Null(tempAnswer);
							System.out.println("清空答案1");
						}
						iTem.isDumbOk = true;
						// 给rb预设选中
						rb.setChecked(true);
						iTem.isCheck = true;
						rb.setTag(iTem);
						// 设为OK证明此哑题
						isDumbOk = true;
					}
				}
			}
		}
		map.put("item", iTem);
		map.put("isDumbOk", isDumbOk);
		return map;
	}
/**
 * 配置型隐藏选项
 * @param hideList
 * @return
 */
	private boolean checkTypeShow(String hideList) {
		boolean isSetHide = true;
		System.out.println("hideList:" + hideList);
		// 5,2:3:4 第一种题型
		// Region,1,北京 受访者属性题型 不包含为 1 包含为2
		// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
		// 哑题的List串不为空
		if (!Util.isEmpty(hideList)) {
			if (hideList.indexOf("|") != -1) {
				// 文本题或的关系
				// 1:0,=,北京|1:0,=,天津 单行文本题型 符号 >、>=
				// 、<、=、!=
				// 受访者属性关系
				// Company,2,北京|Company,=,北京
				String[] twoCondition = hideList.split("\\|");
				String firstCondition = twoCondition[0];
				String secondCondition = twoCondition[1];
				String[] dumbFirstArr = firstCondition.split(",");
				String[] dumbSecondArr = secondCondition.split(",");

				boolean isOneOk = false;
				boolean isTwoOk = false;
				String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
				String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
				if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
					// 第一个条件
					String textIndex = dumbOneFirstArr[0];// 引用的index
					String textItemOrder = dumbOneFirstArr[1];// 引用的位置
					if (!Util.isEmpty(textIndex)
							&& !Util.isEmpty(textItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
							}
							// 哑题改动
							if (2 < dumbFirstArr.length) {
								if (Util.isPass(answerText, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbFirstArr[1], "")) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}

					// 第二个条件
					String textTwoIndex = dumbOneSecondArr[0];// 引用的index
					String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
					if (!Util.isEmpty(textTwoIndex)
							&& !Util.isEmpty(textTwoItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textTwoIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textTwoItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbSecondArr[1] = dumbSecondArr[1] + ":"
										+ type;
							}
							// 哑题改动
							if (2 < dumbSecondArr.length) {
								if (Util.isPass(answerText, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}

							} else {

								if (Util.isPass(answerText, dumbSecondArr[1],
										"")) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							}
						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}
					// 两者 满足一个
					if (isOneOk || isTwoOk) {
						isSetHide = false;
					} else {
					}
				} else if (dumbOneFirstArr.length == 1
						&& dumbOneSecondArr.length == 1) {
					/**
					 * 引用受访者参数
					 */
					String parametersStr = feed.getParametersStr();
					ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
					if (!Util.isEmpty(parametersStr)) {
						parameterList.clear();
						ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
								.parseArray(parametersStr, Parameter.class);
						if (!Util.isEmpty(tParameters)) {
							parameterList.addAll(tParameters);
						}
					}
					String content = "";
					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbFirstArr[0].trim())) {
								System.out.println("content:" + content);
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}
					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbFirstArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbFirstArr[1], 9)) {
							if (Util.isMath(content, dumbFirstArr[1],
									dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
						// 日期
						else if (dumbFirstArr[1].indexOf("-") != -1
								&& dumbFirstArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbFirstArr[2],
										content, dumbFirstArr[1])) {
									isOneOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbFirstArr[1], dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
					}

					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbSecondArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}

					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbSecondArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbSecondArr[1], 9)) {
							if (Util.isMath(content, dumbSecondArr[1],
									dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
						// 日期
						else if (dumbSecondArr[1].indexOf("-") != -1
								&& dumbSecondArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbSecondArr[2],
										content, dumbSecondArr[1])) {
									isTwoOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbSecondArr[1], dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
					}

					// 两者 满足一个
					if (isOneOk || isTwoOk) {
						isSetHide = false;
					} else {
					}
				}
			} else if (hideList.indexOf("&") != -1) {
				// 文本题与的关系
				// 文本题或的关系
				// 1:0,=,北京&1:0,=,天津 单行文本题型 符号 >、>=
				// 、<、=、!=
				String[] twoCondition = hideList.split("&");
				String firstCondition = twoCondition[0];
				String secondCondition = twoCondition[1];
				String[] dumbFirstArr = firstCondition.split(",");
				String[] dumbSecondArr = secondCondition.split(",");
				boolean isOneOk = false;
				boolean isTwoOk = false;
				String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
				String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
				if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
					// 第一个条件
					String textIndex = dumbOneFirstArr[0];// 引用的index
					String textItemOrder = dumbOneFirstArr[1];// 引用的位置
					if (!Util.isEmpty(textIndex)
							&& !Util.isEmpty(textItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
							}
							// 哑题改动
							if (2 < dumbFirstArr.length) {
								if (Util.isPass(answerText, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbFirstArr[1], "")) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}

					// 第二个条件
					String textTwoIndex = dumbOneSecondArr[0];// 引用的index
					String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
					if (!Util.isEmpty(textTwoIndex)
							&& !Util.isEmpty(textTwoItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textTwoIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textTwoItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbSecondArr[1] = dumbSecondArr[1] + ":"
										+ type;
							}
							// 哑题改动
							if (2 < dumbSecondArr.length) {
								if (Util.isPass(answerText, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbSecondArr[1],
										"")) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}
					// 两者 满足一个
					if (isOneOk && isTwoOk) {
						isSetHide = false;
					} else {
					}
				} else if (dumbOneFirstArr.length == 1
						&& dumbOneSecondArr.length == 1) {
					// 受访者属性题目

					/**
					 * 引用受访者参数
					 */
					String parametersStr = feed.getParametersStr();
					ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
					if (!Util.isEmpty(parametersStr)) {
						parameterList.clear();
						ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
								.parseArray(parametersStr, Parameter.class);
						if (!Util.isEmpty(tParameters)) {
							parameterList.addAll(tParameters);
						}
					}
					String content = "";
					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbFirstArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}
					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbFirstArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbFirstArr[1], 9)) {
							if (Util.isMath(content, dumbFirstArr[1],
									dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
						// 日期
						else if (dumbFirstArr[1].indexOf("-") != -1
								&& dumbFirstArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbFirstArr[2],
										content, dumbFirstArr[1])) {
									isOneOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbFirstArr[1], dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
					}

					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbSecondArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}

					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbSecondArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbSecondArr[1], 9)) {
							if (Util.isMath(content, dumbSecondArr[1],
									dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
						// 日期
						else if (dumbSecondArr[1].indexOf("-") != -1
								&& dumbSecondArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbSecondArr[2],
										content, dumbSecondArr[1])) {
									isTwoOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbSecondArr[1], dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
					}

					// 两者 满足一个
					if (isOneOk && isTwoOk) {
						isSetHide = false;
					} else {
					}

				}
			} else {
				String[] dumbArr = hideList.split(",");
				// 哑题有引用的index和value值。
				// 单复选5,2:3:4 第一种题型
				// 单选矩阵题型1,0_2:0_3
				if (dumbArr.length == 2) {
					String dumbIndex = dumbArr[0];// index
					String dumbValueStr = dumbArr[1];// value
					// 引用的那道哑题的答案
					Answer dumbAnswer = ma.dbService.getAnswer(feed.getUuid(),
							dumbIndex);
					// 存取值的list
					ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
//					if (!Util.isEmpty(ansertlist)) {
//						for (int i = 0; i < ansertlist.size(); i++) {
//							System.out.println("ansertlist的取值："+(ansertlist.get(i).getqIndex()-1));
//							if (ansertlist.get(i).getqIndex()-1==Integer.parseInt(dumbIndex)) {
//								dumbAmList = ansertlist.get(i).getAnswerMapArr();
//							}
//						}
//					}
					
					if (null != dumbAnswer) {
						dumbAmList = dumbAnswer.getAnswerMapArr();
					}
					// 假如存取值的List不为空
					if (!Util.isEmpty(dumbAmList)) {
						String[] dumbValueArr = dumbValueStr.split(":");// 数组
						// 循环储存的值
						for (AnswerMap dumbAm : dumbAmList) {
							// 单选矩阵题型1,0_2:0_3
							String dumbValue = dumbAm.getAnswerValue();// 原有的值
							System.out.println("上一道的值123:" + dumbValue);
							// 假如答案和引用的value值相同
							for (String str : dumbValueArr) {
								System.out.println("本道逻辑可跳的值1:" + str);
							}
							if (dumbValueStr.indexOf("_") != -1) {
								int dumbRow = dumbAm.getRow();
								int dumbCol = dumbAm.getCol();
								if (Util.isHave(dumbValueArr, dumbValue,
										dumbRow, dumbCol)) {
									isSetHide = false;
								} else {
								}

							} else {
								if (Util.isHave(dumbValueArr, dumbValue)) {
									isSetHide = false;
								} else {
								}
							}
						}
					} else {
						// 为空,什么都不做。
						System.out.println("上一道的值123:为空");
					}
					System.out.println("上一道的值456:为空");
				}
				// 二三种题型
				else if (dumbArr.length == 3) {
					// Company,2,北京 受访者属性题型 不包含为 1 包含为2
					// 1:0,=,北京 单行文本题型
					String dumbOneStr = dumbArr[0];
					String[] dumbOneArr = dumbOneStr.split(":");
					// 1为受访者属性题型
					if (dumbOneArr.length == 1) {
						/**
						 * 引用受访者参数
						 */
						String parametersStr = feed.getParametersStr();
						ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
						if (!Util.isEmpty(parametersStr)) {
							parameterList.clear();
							ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
									.parseArray(parametersStr, Parameter.class);
							if (!Util.isEmpty(tParameters)) {
								parameterList.addAll(tParameters);
							}
						}
						String content = "";
						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().trim()
										.equals(dumbArr[0].trim())) {
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}
						// System.out.println("0:"+content+"1:"+dumbArr[1]+"2:"+dumbArr[2]);
						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbArr[2], 9)
									&& Util.isFormat(content, 9)
									&& !Util.isFormat(dumbArr[1], 9)) {
								if (Util.isMath(content, dumbArr[1], dumbArr[2])) {
									isSetHide = false;
								} else {
								}
							}
							// 日期
							else if (dumbArr[1].indexOf("-") != -1
									&& dumbArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbArr[2],
											content, dumbArr[1])) {
										isSetHide = false;
									} else {
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content,
										dumbArr[1], dumbArr[2])) {
									isSetHide = false;
								} else {
								}
							}
						}
					}
					// 2为单行文本题型
					else if (dumbOneArr.length == 2) {
						// 1:0,=,北京 单行文本题型 符号 >、>=
						// 、<、=、!=
						String textIndex = dumbOneArr[0];// 引用的index
						String textItemOrder = dumbOneArr[1];// 引用的位置
						if (!Util.isEmpty(textIndex)
								&& !Util.isEmpty(textItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(
									feed.getUuid(), textIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = new AnswerMap();
								for (AnswerMap am : amTextList) {
									if (am.getRow() == Integer
											.parseInt(textItemOrder.trim())) {
										answerMap = am;
										break;
									}
								}
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(
										feed.getSurveyId(), textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question
											.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer
														.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbArr[1] = dumbArr[1] + ":" + type;
								}
								// 哑题改动
								if (2 < dumbArr.length) {
									if (Util.isPass(answerText, dumbArr[1],
											dumbArr[2])) {
										isSetHide = false;
									}
								} else {
									if (Util.isPass(answerText, dumbArr[1], "")) {
										isSetHide = false;
									}
								}

							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}
					}
				}
			}
		}
		return isSetHide;
	}

	private boolean checkTypeHide(String hideList) {
		boolean isSetHide = false;
		if (!Util.isEmpty(hideList)) {
			if (hideList.indexOf("|") != -1) {
				// 文本题或的关系
				// 1:0,=,北京|1:0,=,天津 单行文本题型 符号 >、>=
				// 、<、=、!=
				// 受访者属性关系
				// Company,2,北京|Company,=,北京
				String[] twoCondition = hideList.split("\\|");
				String firstCondition = twoCondition[0];
				String secondCondition = twoCondition[1];
				String[] dumbFirstArr = firstCondition.split(",");
				String[] dumbSecondArr = secondCondition.split(",");

				boolean isOneOk = false;
				boolean isTwoOk = false;
				String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
				String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
				if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
					// 第一个条件
					String textIndex = dumbOneFirstArr[0];// 引用的index
					String textItemOrder = dumbOneFirstArr[1];// 引用的位置
					if (!Util.isEmpty(textIndex)
							&& !Util.isEmpty(textItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
							}
							// 哑题改动
							if (2 < dumbFirstArr.length) {
								if (Util.isPass(answerText, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbFirstArr[1], "")) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							}
						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}

					// 第二个条件
					String textTwoIndex = dumbOneSecondArr[0];// 引用的index
					String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
					if (!Util.isEmpty(textTwoIndex)
							&& !Util.isEmpty(textTwoItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textTwoIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textTwoItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbSecondArr[1] = dumbSecondArr[1] + ":"
										+ type;
							}
							// 哑题改动
							if (2 < dumbSecondArr.length) {
								if (Util.isPass(answerText, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbSecondArr[1],
										"")) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}
					// 两者 满足一个
					if (isOneOk || isTwoOk) {
						isSetHide = true;
					}
				} else if (dumbOneFirstArr.length == 1
						&& dumbOneSecondArr.length == 1) {
					/**
					 * 引用受访者参数
					 */
					String parametersStr = feed.getParametersStr();
					ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
					if (!Util.isEmpty(parametersStr)) {
						parameterList.clear();
						ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
								.parseArray(parametersStr, Parameter.class);
						if (!Util.isEmpty(tParameters)) {
							parameterList.addAll(tParameters);
						}
					}
					String content = "";
					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbFirstArr[0].trim())) {
								System.out.println("content:" + content);
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}
					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbFirstArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbFirstArr[1], 9)) {
							if (Util.isMath(content, dumbFirstArr[1],
									dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
						// 日期
						else if (dumbFirstArr[1].indexOf("-") != -1
								&& dumbFirstArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbFirstArr[2],
										content, dumbFirstArr[1])) {
									isOneOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbFirstArr[1], dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
					}

					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbSecondArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}

					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbSecondArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbSecondArr[1], 9)) {
							if (Util.isMath(content, dumbSecondArr[1],
									dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
						// 日期
						else if (dumbSecondArr[1].indexOf("-") != -1
								&& dumbSecondArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbSecondArr[2],
										content, dumbSecondArr[1])) {
									isTwoOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbSecondArr[1], dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
					}

					// 两者 满足一个
					if (isOneOk || isTwoOk) {
						isSetHide = true;
					}
				}
			} else if (hideList.indexOf("&") != -1) {
				// 文本题与的关系
				// 文本题或的关系
				// 1:0,=,北京&1:0,=,天津 单行文本题型 符号 >、>=
				// 、<、=、!=
				String[] twoCondition = hideList.split("&");
				String firstCondition = twoCondition[0];
				String secondCondition = twoCondition[1];
				String[] dumbFirstArr = firstCondition.split(",");
				String[] dumbSecondArr = secondCondition.split(",");
				boolean isOneOk = false;
				boolean isTwoOk = false;
				String[] dumbOneFirstArr = dumbFirstArr[0].split(":");
				String[] dumbOneSecondArr = dumbSecondArr[0].split(":");
				if (dumbOneFirstArr.length == 2 && dumbOneSecondArr.length == 2) {
					// 第一个条件
					String textIndex = dumbOneFirstArr[0];// 引用的index
					String textItemOrder = dumbOneFirstArr[1];// 引用的位置
					if (!Util.isEmpty(textIndex)
							&& !Util.isEmpty(textItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbFirstArr[1] = dumbFirstArr[1] + ":" + type;
							}
							// 哑题改动
							if (2 < dumbFirstArr.length) {
								if (Util.isPass(answerText, dumbFirstArr[1],
										dumbFirstArr[2])) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbFirstArr[1], "")) {
									isOneOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}

					// 第二个条件
					String textTwoIndex = dumbOneSecondArr[0];// 引用的index
					String textTwoItemOrder = dumbOneSecondArr[1];// 引用的位置
					if (!Util.isEmpty(textTwoIndex)
							&& !Util.isEmpty(textTwoItemOrder)) {
						Answer textAnswer = ma.dbService.getAnswer(
								feed.getUuid(), textTwoIndex);
						// 存取值的list
						ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
						if (null != textAnswer) {
							amTextList = textAnswer.getAnswerMapArr();
						}
						// 假如存取值的List不为空
						if (!Util.isEmpty(amTextList)) {
							AnswerMap answerMap = new AnswerMap();
							for (AnswerMap am : amTextList) {
								if (am.getRow() == Integer
										.parseInt(textTwoItemOrder.trim())) {
									answerMap = am;
									break;
								}
							}
							String answerText = answerMap.getAnswerText();
							// 哑题改动
							Question question = ma.dbService.getQuestion(
									feed.getSurveyId(), textIndex);
							int type = -1;
							if (null != question) {
								ArrayList<QuestionItem> colItemArr = question
										.getColItemArr();
								if (!Util.isEmpty(colItemArr)) {
									QuestionItem questionItem = colItemArr
											.get(Integer
													.parseInt(textItemOrder));
									if (null != questionItem) {
										type = questionItem.getType();
									}
								}
							}
							if (-1 != type) {
								dumbSecondArr[1] = dumbSecondArr[1] + ":"
										+ type;
							}
							// 哑题改动
							if (2 < dumbSecondArr.length) {
								if (Util.isPass(answerText, dumbSecondArr[1],
										dumbSecondArr[2])) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							} else {
								if (Util.isPass(answerText, dumbSecondArr[1],
										"")) {
									isTwoOk = true;
								} else {
									// 不满足什么都不做
								}
							}

						} else {
							// 答案为空什么也不做
						}
					} else {
						// 服务器传的字符串为空什么也不做
					}
					// 两者 满足一个
					if (isOneOk && isTwoOk) {
						isSetHide = true;
					}
				} else if (dumbOneFirstArr.length == 1
						&& dumbOneSecondArr.length == 1) {
					// 受访者属性题目

					/**
					 * 引用受访者参数
					 */
					String parametersStr = feed.getParametersStr();
					ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
					if (!Util.isEmpty(parametersStr)) {
						parameterList.clear();
						ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
								.parseArray(parametersStr, Parameter.class);
						if (!Util.isEmpty(tParameters)) {
							parameterList.addAll(tParameters);
						}
					}
					String content = "";
					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbFirstArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}
					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbFirstArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbFirstArr[1], 9)) {
							if (Util.isMath(content, dumbFirstArr[1],
									dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
						// 日期
						else if (dumbFirstArr[1].indexOf("-") != -1
								&& dumbFirstArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbFirstArr[2],
										content, dumbFirstArr[1])) {
									isOneOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbFirstArr[1], dumbFirstArr[2])) {
								isOneOk = true;
							}
						}
					}

					if (null != parameterList && parameterList.size() > 0) {
						for (Parameter parameter : parameterList) {
							if (parameter.getSid().trim()
									.equals(dumbSecondArr[0].trim())) {
								content = parameter.getContent();
								break;
							}
						}
					} else {
						content = "";
					}

					// 假如查到content了，在判断是什么符号
					if (!Util.isEmpty(content)) {
						// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
						// 假如是数字
						if (Util.isFormat(dumbSecondArr[2], 9)
								&& Util.isFormat(content, 9)
								&& !Util.isFormat(dumbSecondArr[1], 9)) {
							if (Util.isMath(content, dumbSecondArr[1],
									dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
						// 日期
						else if (dumbSecondArr[1].indexOf("-") != -1
								&& dumbSecondArr[1].length() > 5) {
							try {
								if (Util.getDateCompare(dumbSecondArr[2],
										content, dumbSecondArr[1])) {
									isTwoOk = true;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						// 文本格式
						else {
							if (Util.isRespondentsMatching(content,
									dumbSecondArr[1], dumbSecondArr[2])) {
								isTwoOk = true;
							}
						}
					}

					// 两者 满足一个
					if (isOneOk && isTwoOk) {
						isSetHide = true;
					}

				}
			} else {
				String[] dumbArr = hideList.split(",");
				// 哑题有引用的index和value值。
				// 5,2:3:4 第一种题型
				// 单复选
				// 单选矩阵题型1,0_2:0_3
				if (dumbArr.length == 2) {
					String dumbIndex = dumbArr[0];// index
					String dumbValueStr = dumbArr[1];// value
					// 引用的那道哑题的答案
					Answer dumbAnswer = ma.dbService.getAnswer(feed.getUuid(),
							dumbIndex);
					// 存取值的list
					ArrayList<AnswerMap> dumbAmList = new ArrayList<AnswerMap>();
//					for (int i = 0; i < ans.size(); i++) {
//						if (ans.get(i).getqIndex().equals(dumbIndex)) {
//							dumbAmList = ans.get(i).getAnswerMapArr();
//						}
//					}
					
					if (null != dumbAnswer) {
						dumbAmList = dumbAnswer.getAnswerMapArr();
					}
					// 假如存取值的List不为空
					if (!Util.isEmpty(dumbAmList)) {
						String[] dumbValueArr = dumbValueStr.split(":");// 数组
						// 循环储存的值
						for (AnswerMap dumbAm : dumbAmList) {
							String dumbValue = dumbAm.getAnswerValue();// 原有的值
							System.out.println("上一道的值qwe:" + dumbValue);
							// 假如答案和引用的value值相同
							for (String str : dumbValueArr) {
								System.out.println("本道逻辑可跳的值:" + str);
							}
							// 单选矩阵题型1,0_2:0_3
							if (dumbValueStr.indexOf("_") != -1) {
								int dumbRow = dumbAm.getRow();
								int dumbCol = dumbAm.getCol();
								if (Util.isHave(dumbValueArr, dumbValue,
										dumbRow, dumbCol)) {
									isSetHide = true;
								} else {

								}

							} else {
								if (Util.isHave(dumbValueArr, dumbValue)) {
									isSetHide = true;
								} else {

								}
							}
						}
					} else {
						// 为空,什么都不做。
						System.out.println("上一道的值qwe:为空");
					}
				}
				// 二三种题型
				else if (dumbArr.length == 3) {
					// Company,2,北京 受访者属性题型 不包含为 1 包含为2
					// 1:0,=,北京 单行文本题型
					String dumbOneStr = dumbArr[0];
					String[] dumbOneArr = dumbOneStr.split(":");
					// 1为受访者属性题型
					if (dumbOneArr.length == 1) {
						/**
						 * 引用受访者参数
						 */
						String parametersStr = feed.getParametersStr();
						ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
						if (!Util.isEmpty(parametersStr)) {
							parameterList.clear();
							ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
									.parseArray(parametersStr, Parameter.class);
							if (!Util.isEmpty(tParameters)) {
								parameterList.addAll(tParameters);
							}
						}
						String content = "";
						if (null != parameterList && parameterList.size() > 0) {
							for (Parameter parameter : parameterList) {
								if (parameter.getSid().trim()
										.equals(dumbArr[0].trim())) {
									content = parameter.getContent();
									break;
								}
							}
						} else {
							content = "";
						}
						// System.out.println("0:"+content+"1:"+dumbArr[1]+"2:"+dumbArr[2]);
						// 假如查到content了，在判断是什么符号
						if (!Util.isEmpty(content)) {
							// 判断是什么类型的，文本，现有方法就行。数字符号方法，日期专用方法
							// 假如是数字
							if (Util.isFormat(dumbArr[2], 9)
									&& Util.isFormat(content, 9)
									&& !Util.isFormat(dumbArr[1], 9)) {
								if (Util.isMath(content, dumbArr[1], dumbArr[2])) {
									isSetHide = true;

								}
							}
							// 日期
							else if (dumbArr[1].indexOf("-") != -1
									&& dumbArr[1].length() > 5) {
								try {
									if (Util.getDateCompare(dumbArr[2],
											content, dumbArr[1])) {
										isSetHide = true;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 文本格式
							else {
								if (Util.isRespondentsMatching(content,
										dumbArr[1], dumbArr[2])) {
									isSetHide = true;
								}
							}
						}
					}
					// 2为单行文本题型
					else if (dumbOneArr.length == 2) {
						// 1:0,=,北京 单行文本题型 符号 >、>=
						// 、<、=、!=
						String textIndex = dumbOneArr[0];// 引用的index
						String textItemOrder = dumbOneArr[1];// 引用的位置
						if (!Util.isEmpty(textIndex)
								&& !Util.isEmpty(textItemOrder)) {
							Answer textAnswer = ma.dbService.getAnswer(
									feed.getUuid(), textIndex);
							// 存取值的list
							ArrayList<AnswerMap> amTextList = new ArrayList<AnswerMap>();
							if (null != textAnswer) {
								amTextList = textAnswer.getAnswerMapArr();
							}
							// 假如存取值的List不为空
							if (!Util.isEmpty(amTextList)) {
								AnswerMap answerMap = new AnswerMap();
								for (AnswerMap am : amTextList) {
									if (am.getRow() == Integer
											.parseInt(textItemOrder.trim())) {
										answerMap = am;
										break;
									}
								}
								String answerText = answerMap.getAnswerText();
								// 哑题改动
								Question question = ma.dbService.getQuestion(
										feed.getSurveyId(), textIndex);
								int type = -1;
								if (null != question) {
									ArrayList<QuestionItem> colItemArr = question
											.getColItemArr();
									if (!Util.isEmpty(colItemArr)) {
										QuestionItem questionItem = colItemArr
												.get(Integer
														.parseInt(textItemOrder));
										if (null != questionItem) {
											type = questionItem.getType();
										}
									}
								}
								if (-1 != type) {
									dumbArr[1] = dumbArr[1] + ":" + type;
								}
								// 哑题改动
								if (2 < dumbArr.length) {
									if (Util.isPass(answerText, dumbArr[1],
											dumbArr[2])) {
										isSetHide = true;
									} else {
										// 不满足什么都不做
									}
								} else {
									if (Util.isPass(answerText, dumbArr[1], "")) {
										isSetHide = true;
									} else {
										// 不满足什么都不做
									}
								}

							} else {
								// 答案为空什么也不做
							}
						} else {
							// 服务器传的字符串为空什么也不做
						}
					}
				}
			}
		}
		return isSetHide;
	}

	/**
	 * 隐藏选项
	 * 
	 * @param item
	 * @param rb
	 * @param tempAnswer
	 */

	private boolean setHideRb(QuestionItem item, Question q) {
		Boolean isSetHide = false;
		if (1 == item.hideType) {
			q.hideCount++;
			isSetHide = true;
			q.realRows--;
			if (!Util.isEmpty(item.hideList)) {
				String hideListAll = item.hideList.trim();
				System.out.println("hideList:" + hideListAll);
				// 5,2:3:4 第一种题型
				// Region,1,北京 受访者属性题型 不包含为 1 包含为2
				// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
				// 哑题的List串不为空
				if (!Util.isEmpty(hideListAll)) {
					if (hideListAll.indexOf("&&") != -1) {// 如果包含&&,为多题关联并且的关系
						isSetHide = false;// 默认满足条件显示
						String[] hideListAllChild = hideListAll.split("&&");
						for (String hideList : hideListAllChild) {
							if (!Util.isEmpty(hideList)) {
								if (checkTypeShow(hideList)) {// 有一个为true(隐藏)不满足显示条件则直接跳出。
									isSetHide = true;
									break;
								}
							}
						}

					} else {// 如果包含||,为多题关联或者的关系
						isSetHide = true;// 默认不满足条件（隐藏状态）
						String[] hideListAllChild = hideListAll.split("\\|\\|");// 正则需转义
						for (String hideList : hideListAllChild) {
							if (!Util.isEmpty(hideList)) {
								if (!checkTypeShow(hideList)) {// 有一个为false(显示)满足显示条件则直接跳出。
									isSetHide = false;
									break;
								}
							}
						}
					}
					if (!isSetHide) {
						q.hideCount--;
						q.realRows++;
					}
				}
			}
			// if (isSetHide) {
			// if (rb instanceof RadioButton) {
			// ((RadioButton) rb).setChecked(false);
			// item.isCheck = false;	6 fcf
			// ((RadioButton) rb).setTag(null);
			// }
			// if (rb instanceof CheckBox) {
			// ((CheckBox) rb).setChecked(false);
			// item.isCheck = false;
			// }
			// if (rb instanceof LinearLayout) {
			// for (int i = 0; i < ((LinearLayout) rb).getChildCount(); i++) {
			// if (1 == i && ((LinearLayout) rb).getChildAt(i) instanceof
			// RadioGroup) {
			// for (int j = 0; j < ((RadioGroup) ((LinearLayout)
			// rb).getChildAt(i)).getChildCount(); j++) {
			// ((RadioButton) ((RadioGroup) ((LinearLayout)
			// rb).getChildAt(i)).getChildAt(j))
			// .setChecked(false);
			// System.out.println("取消选中");
			// }
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof CheckBox) {
			// ((CheckBox) ((LinearLayout) rb).getChildAt(i)).setChecked(false);
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof EditText) {
			// ((EditText) ((LinearLayout) rb).getChildAt(i)).setText("");
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof RadioButton) {
			// ((RadioButton) ((LinearLayout)
			// rb).getChildAt(i)).setChecked(false);
			// ((RadioButton) ((LinearLayout) rb).getChildAt(i)).setTag(null);
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof LinearLayout) {
			// LinearLayout l1 = (LinearLayout) ((LinearLayout)
			// rb).getChildAt(i);
			// if ((l1.getChildAt(0)) instanceof EditText) {
			// EditText et1 = (EditText) (l1.getChildAt(0));
			// et1.setText("");
			// } else if ((l1.getChildAt(0)) instanceof Spinner) {
			// Spinner sp1 = (Spinner) (l1.getChildAt(0));
			// sp1.setSelection(0);
			// } else if ((l1.getChildAt(1)) instanceof LinearLayout) {
			// LinearLayout l2 = (LinearLayout) (l1.getChildAt(1));
			// if (l2.getChildAt(0) instanceof Spinner) {
			// Spinner sp2 = (Spinner) (l2.getChildAt(0));
			// sp2.setSelection(0);
			// } else if (l2.getChildAt(0) instanceof EditText) {
			// EditText et2 = (EditText) (l2.getChildAt(0));
			// et2.setText("");
			// }
			// }
			// }
			// }
			// }
			// }
		} else {
			if (!Util.isEmpty(item.hideList)) {
				String hideListAll = item.hideList.trim();
				System.out.println("hideList:" + hideListAll);
				// 5,2:3:4 第一种题型
				// Region,1,北京 受访者属性题型 不包含为 1 包含为2
				// 1:0,=,北京 单行文本题型 符号 >、>= 、<、=、!=
				// 哑题的List串不为空
				if (!Util.isEmpty(hideListAll)) {
					if (hideListAll.indexOf("&&") != -1) {// 如果包含&&,为多题关联并且的关系
						isSetHide = true;// 默认满足条件（隐藏状态）
						String[] hideListAllChild = hideListAll.split("&&");
						for (String hideList : hideListAllChild) {
							if (!Util.isEmpty(hideList)) {
								if (!checkTypeHide(hideList)) {// 有一个为false(显示)不满足隐藏条件则直接跳出。
									isSetHide = false;
									break;
								}

							}
						}

					} else {// 如果包含||,为多题关联或者的关系
						isSetHide = false;// 默认不满足条件（显示状态）
						String[] hideListAllChild = hideListAll.split("\\|\\|");
						for (String hideList : hideListAllChild) {
							if (!Util.isEmpty(hideList)) {
								if (checkTypeHide(hideList)) {// 有一个为true(隐藏)满足隐藏条件则直接跳出。
									isSetHide = true;
									break;
								}

							}
						}
					}
					if (isSetHide) {
						q.hideCount++;
						q.realRows--;
					}
				}
			}
			// if (isSetHide) {
			// if (rb instanceof RadioButton) {
			// ((RadioButton) rb).setChecked(false);
			// item.isCheck = false;
			// ((RadioButton) rb).setTag(null);
			// }
			// if (rb instanceof CheckBox) {
			// ((CheckBox) rb).setChecked(false);
			// item.isCheck = false;
			// }
			// if (rb instanceof LinearLayout) {
			// for (int i = 0; i < ((LinearLayout) rb).getChildCount(); i++) {
			// if (1 == i && ((LinearLayout) rb).getChildAt(i) instanceof
			// RadioGroup) {
			// for (int j = 0; j < ((RadioGroup) ((LinearLayout)
			// rb).getChildAt(i)).getChildCount(); j++) {
			// ((RadioButton) ((RadioGroup) ((LinearLayout)
			// rb).getChildAt(i)).getChildAt(j))
			// .setChecked(false);
			// }
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof CheckBox) {
			// ((CheckBox) ((LinearLayout) rb).getChildAt(i)).setChecked(false);
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof EditText) {
			// ((EditText) ((LinearLayout) rb).getChildAt(i)).setText("");
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof RadioButton) {
			// ((RadioButton) ((LinearLayout)
			// rb).getChildAt(i)).setChecked(false);
			// ((RadioButton) ((LinearLayout) rb).getChildAt(i)).setTag(null);
			// item.isCheck = false;
			// }
			// if (((LinearLayout) rb).getChildAt(i) instanceof LinearLayout) {
			// LinearLayout l1 = (LinearLayout) ((LinearLayout)
			// rb).getChildAt(i);
			// if ((l1.getChildAt(0)) instanceof EditText) {
			// EditText et1 = (EditText) (l1.getChildAt(0));
			// et1.setText("");
			// } else if ((l1.getChildAt(0)) instanceof Spinner) {
			// Spinner sp1 = (Spinner) (l1.getChildAt(0));
			// sp1.setSelection(0);
			// } else if ((l1.getChildAt(1)) instanceof LinearLayout) {
			// LinearLayout l2 = (LinearLayout) (l1.getChildAt(1));
			// if (l2.getChildAt(0) instanceof Spinner) {
			// Spinner sp2 = (Spinner) (l2.getChildAt(0));
			// sp2.setSelection(0);
			// } else if (l2.getChildAt(0) instanceof EditText) {
			// EditText et2 = (EditText) (l2.getChildAt(0));
			// et2.setText("");
			// }
			// }
			// }
			// }
			// }
			// }
		}
		// 隐藏选项
		if (1 == item.hide) {
			q.realRows--;
			isSetHide = true;
			q.hideCount++;
		}
		return isSetHide;
		// 条件隐藏选项
	}

	/**
	 * 验证逻辑是否通过
	 * 
	 * @param rlQuestion
	 */
	private boolean resctCheck(Question q) {
//		 Answer tempAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex +
//		 "");
		ArrayList<Restriction> rs = new ArrayList<Restriction>();
		rs.addAll(q.getResctItemArr());
		// System.out.println("当前question_index--->" + q.qIndex);
		/**
		 * 本道引用了上一道的选项答案, 如果上面的某道题符合条件 则显示这道题,否则显示下一道题
		 */
		if (!Util.isEmpty(rs)) {
			for (Restriction ri : rs) {
				if (null == ri) {
					rs.remove(ri);
				}
			}
		}
		if (!Util.isEmpty(rs)) {
			boolean isMatcher = Util.isMatcher(ma, q, feed.getUuid());

			Log.i("resctCheck::", "title::" + q.qid + "-isMatcher::"
					+ isMatcher);

			// System.out.println("isMatcher:" + isMatcher);
			// 假如不成功 不符合逻辑
			if (!isMatcher) {
//				 if (null != tempAnswer) {
//				 System.out.println("答案不为空；");
//				 /**
//				 * 置为无效的答案
//				 */
//				 ma.dbService.updateAnswerUnEnable(tempAnswer);
//				 }
				return false;
			} else {
				// if (null != tempAnswer) {
				// }
			}
			// ArrayList<Restriction> restrList
		}
		return true;
	}

	// 单选追加说明方法
	public class MyRadioButtonClick implements OnCheckedChangeListener {
		// private int id;
		private String item;

		public MyRadioButtonClick(String _item) {
			// id=_id;
			item = _item;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isPrompt) {
				return;
			}
			if (isChecked) {
				Intent intent = new Intent(NativeModeNoPageActivity.this,
						MyDialogActivity.class);
				intent.putExtra("item", item);
				NativeModeNoPageActivity.this.startActivity(intent);
				// Toasts.makeText(NativeModeNoPageActivity.this,
				// item,Toast.LENGTH_LONG).show();
			}
		}

	}

	// 逻辑次数跳转 //给hashmap logic
	private HashMap<Integer, TempLogic> myLogic = new HashMap<Integer, TempLogic>();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();// 保存序列号list
	private boolean isForwarding = false;// 是否正在跳转
	// 追加说明方法继续上一页下一页终止提示
	private boolean isPrompt = false;
	// 生成界面是否选项随机
	private int randomPage = 0;
	// 记录上一次的realIndex,监控用
	private int recodeTempIndex = -1;
	// 监控用
	private int realIndex = 0;
	// 目录记录上一个order集合
	private ArrayList<Integer> recodeOrderList = new ArrayList<Integer>();

	// 记录禁用逻辑条件的数组
	ArrayList<AshingQidBean> qidlist = new ArrayList<AshingQidBean>();
	ArrayList<String> ettagids = new ArrayList<String>();

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	HashMap<Integer, ArrayList<View>> vsRecord = new HashMap<Integer, ArrayList<View>>();// 当前选择录音的文本域控件
	ArrayList<QuestionItem> mTempRowsRecord = new ArrayList<QuestionItem>();// 当前选择录音的文本域控件

	public View createQuestionBodyView(int position, boolean isREFRESH) {// 动态生成题干的方法
		final ArrayList<View> vs = new ArrayList<View>();
		ArrayList<QuestionItem> mTempRows = new ArrayList<QuestionItem>();

		// View convertView = inflater.inflate(R.layout.nopage_list_item, null);
		LinearLayout convertView = new LinearLayout(
				NativeModeNoPageActivity.this);
		convertView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// convertView.setPadding(0, dip2px(NativeModeNoPageActivity.this, 10),
		// 0, 0);
		convertView.setOrientation(LinearLayout.VERTICAL);
		View fgx = new View(NativeModeNoPageActivity.this);
		LayoutParams fgxParams = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
		fgxParams
				.setMargins(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		fgx.setLayoutParams(fgxParams);
		fgx.setBackgroundColor(Color.parseColor("#e8e8e8"));
		convertView.addView(fgx, convertView.getChildCount());
		LinearLayout ltitle_tv = new LinearLayout(NativeModeNoPageActivity.this);
		ltitle_tv.setOrientation(LinearLayout.VERTICAL);
		ltitle_tv.setLayoutParams(FILL_WRAP);
		ltitle_tv
				.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		convertView.addView(ltitle_tv, convertView.getChildCount());
		TextView tvTitle = new TextView(NativeModeNoPageActivity.this);
		tvTitle.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ltitle_tv.addView(tvTitle, ltitle_tv.getChildCount());
		/**
		 * 显示单题录音
		 */
		ImageView ivRecord = new ImageView(NativeModeNoPageActivity.this);
		ivRecord.setBackgroundResource(R.drawable.record_on);
		ivRecord.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ivRecord.setVisibility(View.GONE);
		ltitle_tv.addView(ivRecord, ltitle_tv.getChildCount());
		/**
		 * 显示单题签名
		 */
		ImageView ivSign = new ImageView(NativeModeNoPageActivity.this);
		ivSign.setBackgroundResource(R.drawable.signs);
		ivSign.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ivSign.setVisibility(View.GONE);
		ltitle_tv.addView(ivSign, ltitle_tv.getChildCount());
		/**
		 * 上方追缴说明
		 */
		TextView tvCaption = new TextView(NativeModeNoPageActivity.this);
		tvCaption.setLayoutParams(FILL_WRAP);
		tvCaption
				.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		tvCaption.setVisibility(View.GONE);
		tvCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		convertView.addView(tvCaption, convertView.getChildCount());
		/**
		 * 上方追加说明中包含的图片显示布局
		 */
		LinearLayout llCaption = new LinearLayout(NativeModeNoPageActivity.this);
		llCaption
				.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		llCaption.setVisibility(View.GONE);
		llCaption.setOrientation(LinearLayout.HORIZONTAL);
		convertView.addView(llCaption, convertView.getChildCount());
		// 以上 大树拒访 访问状态
		TextView twoSiteNoticeTv = new TextView(NativeModeNoPageActivity.this);
		twoSiteNoticeTv.setVisibility(View.GONE);
		convertView.addView(twoSiteNoticeTv, convertView.getChildCount());
		/**
		 * 题干界面
		 */
		LinearLayout bodyView = new LinearLayout(NativeModeNoPageActivity.this);
		bodyView.setLayoutParams(FILL_WRAP);
		bodyView.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		bodyView.setOrientation(LinearLayout.HORIZONTAL);
		convertView.addView(bodyView, convertView.getChildCount());
		/**
		 * 下方追加说明
		 */
		TextView tvComment = new TextView(NativeModeNoPageActivity.this);
		tvComment.setLayoutParams(FILL_WRAP);
		tvComment.setVisibility(View.GONE);
		tvComment
				.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		tvComment.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		convertView.addView(tvComment, convertView.getChildCount());
		/**
		 * 下方追加说明中包含的图片显示布局
		 */
		LinearLayout llComment = new LinearLayout(NativeModeNoPageActivity.this);
		llComment.setVisibility(View.GONE);
		llComment
				.setPadding(0, 0, 0, dip2px(NativeModeNoPageActivity.this, 15));
		llComment.setOrientation(LinearLayout.HORIZONTAL);
		convertView.addView(llComment, convertView.getChildCount());

		// 追加说明方法继续上一页下一页终止提示
		isPrompt = true;
		mTempRows.clear();

		// if ((qs.size() - 1) == index) {
		// return;
		// }
		/**
		 * 假如题目集合中没有题目则停止执行一下代码
		 */
		if (Util.isEmpty(qs)) {
			return null;
		}

		/*** zz题组随机开始 ****/
		realIndex = position;
		if (!Util.isEmpty(arrList)) {
			String strIndex = arrList.get(position);
			realIndex = Integer.parseInt(strIndex);
		}
		/*** zz题组随机结束 ****/

		/**
		 * 获取当前题目
		 */
		final Question q = qs.get(position);
		// 监控初始位置
		/**
		 * 若问题对象为空, 停止执行一下代码
		 */
		if (null == q) {
			return null;
		}
		bodyView.removeAllViews(); // 清空选项
		tvTitle.setText("");
		tvCaption.setTextColor(Color.GRAY);// 统计局专有页面
		tvCaption.setText("");
		if (View.VISIBLE == llCaption.getVisibility()) {
			llCaption.removeAllViews();
			llCaption.setVisibility(View.GONE);
		}

		tvComment.setTextColor(Color.GRAY);// 统计局专有页面
		tvComment.setText("");

		if (View.VISIBLE == llComment.getVisibility()) {
			llComment.removeAllViews();
			llComment.setVisibility(View.GONE);
		}
		// initView();
		// if (!resctCheck(q) || Cnt.TYPE_PAGE == q.qType) {// 不符合逻辑或分页符
		// convertView.setVisibility(View.GONE);
		// return convertView;
		// }

		/**
		 * 单题拍照 大树动画 7 设置单题拍照
		 */
		if (q.qCamera == 1) {
			isHaveSingle = true;
		} else {
			isHaveSingle = false;
		}

		// if (1 == feed.getSurvey().isPhoto) {z1删除
		// boolean isHaveCamera = (q.qCamera == 1 ? true : false);
		// // 有单题拍照
		// if (isHaveCamera) {
		// miCamera.setEnabled(false);
		// } else {
		// miCamera.setEnabled(true);
		// }
		// }z1删除

		/**
		 * 本道题的答案
		 */
		final Answer tempAnswer;
//		if (!isREFRESH) {
			tempAnswer = ma.dbService.getAnswer(feed.getUuid(),
					q.qIndex + "");
//		}else {
//			tempAnswer = null;
//		}
		 
		// 查看是否有干预
		if (!q.getIntervention().getIisMap().isEmpty()) {
			// 手动干预是否能通过
			boolean is = Util.isIntervention(ma, q, feed.getUuid());
			if (is) {
				/**
				 * 成立则显示本道题目
				 */
			} else {
				/**
				 * 假如跳到下一道题或上一题, 则将本道题的答案置为空
				 */
				ma.dbService.updateAnswer2Null(tempAnswer);
				convertView = (LinearLayout) new View(this);
				convertView.setLayoutParams(new AbsListView.LayoutParams(0, 0));
				return convertView;
			}
			System.out.println("-------->_index==_q.qIndex");
		}

		/** 逻辑次数跳转开始 **/
		// 假如数据库中筛选终止logicList不为空
		if (logicList != null) {
			// 假如没有正在跳转
			if (!isForwarding) {
				// 获得有几个筛选终止的list
				ArrayList<Logic> logics = logicList.getLogics();
				// 假如筛选终止list不为空
				if (!Util.isEmpty(logics)) {
					// 循环筛选终止
					for (Logic logic : logics) {
						// System.out.println("累加几次后跳:"+logic.getCountJump()+"/t跳到哪个index中:"+logic.getJumpIndex());
						// 获得累加几次跳转的数
						int countJump = logic.getCountJump();
						// 获得要跳转的index
						int jumpIndex = logic.getJumpIndex();
						// 假如0等于次数或者0等于要跳转的index就下一个循环
						if (0 == countJump || 0 == jumpIndex) {
							continue;
						}
						// 通过要跳转的index获得要跳转的order
						int jumpOrder = ma.dbService.getQuestionOrder(
								feed.getSurveyId(), jumpIndex);
						// 假如现在的order大于等于要跳的order,就下一个循环，取消这此循环。
						if (q.qOrder >= jumpOrder) {
							continue;
						}
						// 获取该筛选终止选项集合
						ArrayList<LogicItem> logicItems = logic.getLogicItem();
						// 假如选项集合不为空
						if (!Util.isEmpty(logicItems)) {
							// 记录累加了几次
							int sumCountIndex = 0;
							// 这个集合中记录最大的顺序号
							int maxOrder = 0;
							// 记录最小的order
							int minOrder = 0;
							// 循环以便获得最小的order
							for (LogicItem li : logicItems) {
								// 获得index
								int liIndex = li.getLogicIndex();
								// 获得这个index的order
								int liOrder = ma.dbService.getQuestionOrder(
										feed.getSurveyId(), liIndex);
								// order==0跳过
								if (liOrder == 0) {
									continue;
								}
								// 最小的order假如没记录过
								if (minOrder == 0) {
									// 最小的order
									minOrder = liOrder;
								}
								// 假如最小的order大于当前的order
								if (minOrder > liOrder) {
									// 最小的order就赋给新的
									minOrder = liOrder;
								}
							}
							// 假如当前的order小于最小的order 终止判断，不自动翻页
							if (q.qOrder < minOrder) {
								break;
							}
							// 循环选项
							for (LogicItem logicItem : logicItems) {
								// System.out.println("哪个题需要累加:"+logicItem.getLogicIndex()+"/t累加题需要选什么值才能累加:"+logicItem.getLogicValue());
								// 获得该选项的index
								int logicIndex = logicItem.getLogicIndex();
								// 获得该选项需要选得值有哪些
								String logicValue = logicItem.getLogicValue();
								// 存取的数组为空
								String[] arrLogicValue = null;
								// 假如选项能选择的值不为空跳过次循环
								if (Util.isEmpty(logicValue)) {
									continue;
								} else {
									// 否则获得该选项的数组值
									arrLogicValue = logicValue.split(",");
								}
								// 通过这个选项index的值获得answer实体
								Answer myAnswer = ma.dbService.getAnswer(
										feed.getUuid(), logicIndex + "");
								// 实例化AnswerMap list集合
								ArrayList<AnswerMap> answerMapList = new ArrayList<AnswerMap>();
								// 假如答案不为空
								if (null != myAnswer) {
									// 获取答案存取的AnswerMap list集合
									answerMapList = myAnswer.getAnswerMapArr();
								} else {
									// 否则跳转
									continue;
								}
								// 获得该题的order号
								int qOrder = myAnswer.qOrder;
								// 假如答案存取的answerMapList list集合不为空
								if (!Util.isEmpty(answerMapList)) {
									// 循环AnswerMap实体
									for (AnswerMap am : answerMapList) {
										// 获得这个AnswerMap的value值
										String localAnswerValue = am
												.getAnswerValue();
										// 假如AnswerMap的value值不为空
										if (!Util.isEmpty(localAnswerValue)) {
											// 假如数据库存取的值在 应该跳转的数组中存在
											if (Util.isHave(arrLogicValue,
													localAnswerValue)) {
												// 找到了 累加一次
												sumCountIndex++;
												// 假如储存的最大值<现在比较成功的值
												if (maxOrder < qOrder) {
													maxOrder = qOrder;// 记录最大的order值
												}
												break;
											}
										} else {
											// 为空跳过本次循环
											continue;
										}
									}
								} else {
									// 为空跳过本次循环
									continue;
								}
							}
							// System.out.println("sumCountIndex:"+sumCountIndex);
							// 假如累加的值>=要跳的数目值
							if (sumCountIndex >= countJump) {
								// 通过要跳的key获取暂存的TempLogic实体，此实体用来控制跳转
								TempLogic tl = myLogic.get(jumpIndex);
								// 假如TempLogic实体为空
								if (null == tl) {
									// 实例化实体
									tl = new TempLogic();
								}
								// 假如是下一页
								// TempLogic实体设置可以往后跳转
								tl.setIsForwardComplete(true);
								// TempLogic实体设置不可以往前跳转
								tl.setIsReturnComplete(false);
								// 假如现在的order小于等于最大的order
								if (q.qOrder <= maxOrder) {
									// 设置最后一个跳转order为0
									tl.setLastOrder(0);
								} else {
									// 设置最后一个跳转order为最大的order
									tl.setLastOrder(maxOrder);
								}
								// System.out.println("maxOrder:"+maxOrder);
								// 设置最后一个跳转order为最大的order作为临时order
								tl.setTempLastOrder(maxOrder);
								// 存取hashmap值
								myLogic.put(jumpIndex, tl);
								break;
							} else {
								// 假如累加的值小于要跳转的数目
								TempLogic tl = myLogic.get(jumpIndex);
								// 获得实体
								if (null == tl) {
									tl = new TempLogic();
								}
								// 设置既不能往前也不能往后跳
								tl.setIsForwardComplete(false);
								tl.setIsReturnComplete(false);
								// 储存最后的值为0
								tl.setLastOrder(0);
								// 存取值
								myLogic.put(jumpIndex, tl);
							}
						}
					}
				}
			}
		}

		/**
		 * 获取引用其他问题的集合
		 */
		/** 监控用开始 */
		// 获取上一个index的值,监控用 建设一个异步线程
		// 有网传 大树注销监控 1
		// if (NetUtil.checkNet(this) && mapMonitor) {
		// new MonitorTask( MSG_NEXT,realIndex, q).execute();
		// }
		/** 监控用结束 */
		// Util.showView(NativeModeNoPageActivity.this, convertView,
		// R.anim.slide_left_in);
		// tvBar.setText(getResources().getString(R.string.survey_title, (index
		// + 1), qs.size()));
		// 大树 添加 标题
		// tvBar.setText(feed.getSurveyTitle() + "(" + qs.size() + ")");
		// 大树
		System.err.println("----------------------------------" + "qIndex="
				+ q.qIndex + "---------------------------------");
		// 单复选矩阵固定
		boolean isFixed = (1 == q.isFixed);// true固定的,false不固定的
//		ettagids = (ArrayList<String>) LimitlistUtil.removeDuplicate(ettagids);
		
		
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		
//		qidlist.clear();
//		qidlist = LimitlistUtil.getlimlists(qs);
//		System.out.println("++"+qidlist.size());
//		
//		for (int i = 0; i < qidlist.size(); i++) {
//			System.out.println("++ i = " + i);
//			if (qidlist.get(i).getQid().equals(q.qIndex + "")) {
//				System.out.println("++ qid = " + q.qid);
//				for (int j = 0; j < qidlist.get(i).getAshingBeans().size(); j++) {
//					System.out.println("++ j = " + j);
//					ettagids.add(qidlist.get(i).getAshingBeans().get(j).getEtId());
//				}
//			}
//			
//		}
		
//		for (int i = 0; i < ashingBeans.size(); i++) {
//			System.out.println(ashingBeans.toString());
//		}
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

		
		
		if (true) {
			// 单复选矩阵固定
			// rlQuestion_new.setVisibility(View.GONE);
			// System.out.println("隐藏rlQuestion_new");
			ltitle_tv.setVisibility(View.VISIBLE);
			if (Cnt.POS_LEFT.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.LEFT);
			} else if (Cnt.POS_CENTER.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qTitlePosition)) {
				tvTitle.setGravity(Gravity.RIGHT);
			} else {
				tvTitle.setGravity(Gravity.LEFT);
			}
		}

		if (Cnt.TYPE_HEADER == q.qType) {// 假如是标题题
			/**
			 * 设置标题的颜色
			 */
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, bigSurveySize);
			/**
			 * 显示标题文本
			 */
			if (!Util.isEmpty(q.qTitle)) {
				// 更改的样式
				String strTilte = q.qTitle;
				/**
				 * 标题逻辑引用
				 */
				CstmMatcher qutoMatcherList = Util.findMatcherItemList(
						strTilte, ma, feed.getUuid(), q.surveyId);
				boolean qutoHave = Util.isEmpty(qutoMatcherList.getMis());
				if (!qutoHave) {
					strTilte = qutoMatcherList.getResultStr();
				}
				/**
				 * 引用受访者参数
				 */
				String parametersStr = feed.getParametersStr();
				ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
				if (!Util.isEmpty(parametersStr)) {
					parameterList.clear();
					ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
							.parseArray(parametersStr, Parameter.class);
					if (!Util.isEmpty(tParameters)) {
						parameterList.addAll(tParameters);
					}
				}
				CstmMatcher parameterMatcherList = Util
						.findMatcherPropertyItemList(strTilte, parameterList);
				boolean parameterHave = Util.isEmpty(parameterMatcherList
						.getMis());
				if (!parameterHave) {
					strTilte = parameterMatcherList.getResultStr();
				}
				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeNoPageActivity.this
								.getFilesDir()
								+ File.separator
								+ "survey"
								+ File.separator
								+ feed.getSurveyId()
								+ File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							float tWidth = image.getWidth();
							float tHeight = image.getHeight();
							if (tWidth > maxCWidth) {
								tHeight = maxCWidth / tWidth * tHeight;
								tWidth = maxCWidth - 6;
							}
							drawable.setBounds(0, 0, (int) tWidth,
									(int) tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
				tvTitle.setText(fromHtml);
				// 更改的样式
			} else {
				ltitle_tv.setVisibility(View.GONE);
			}

			/**
			 * 增加单题拍照按钮 在最后面
			 */
			if (q.qCamera == 1 && !Util.isEmpty(feed.getUuid())) {
				// System.out.println("单题拍照");
				// System.out.println("有单题拍照");
				Drawable drawable = this.getResources().getDrawable(
						R.drawable.camera_btn_bg);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tvTitle.setCompoundDrawables(null, null, drawable, null);
				tvTitle.setOnClickListener(new CstOnClickListener(q, 0));
			} else {
				tvTitle.setCompoundDrawables(null, null, null, null);
				// System.out.println("无单题拍照");
			}
			/**
			 * 增加单题签名
			 */
			if (q.qSign == 1 && !Util.isEmpty(feed.getUuid())) {
				ivSign.setVisibility(View.VISIBLE);
				ivSign.setOnClickListener(new CstOnClickListener(q, 1));
			}
			/**
			 * 增加单题录音
			 */
			if (q.itemRecording == 1 && !Util.isEmpty(feed.getUuid())) {
				ivRecord.setVisibility(View.VISIBLE);
				ivRecord.setOnClickListener(new CstOnClickListener(q, 2));
			} else {
				ivRecord.setVisibility(View.GONE);
				ivRecord.setOnClickListener(null);
			}
			bodyView.setVisibility(View.GONE);
		} else {
			if (0 == q.qTitleDisable) {
				tvTitle.setClickable(false);
			}
			tvTitle.setTextColor(Color.BLACK);
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, middleSueveySize);

			// StringBuilder sbTitle = new StringBuilder("");
			String strTilte = "";
			if (Util.isEmpty(q.qTitle)) {
				q.qTitle = NativeModeNoPageActivity.this
						.getString(R.string.no_title);
			}
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid + ". " + q.qTitle;
			} else {
				if (!Util.isEmpty(q.qTitle)) {
					strTilte = getResources().getString(
							R.string.question_order, q.qOrder)
							+ ". " + q.qTitle;
				} else {
					strTilte = getResources().getString(
							R.string.question_order, q.qOrder);
				}

			}

			/**
			 * 标题逻辑引用
			 */
			CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte,
					ma, feed.getUuid(), q.surveyId);
			boolean qutoHave = Util.isEmpty(qutoMatcherList.getMis());
			if (!qutoHave) {
				strTilte = qutoMatcherList.getResultStr();
			}

			/**
			 * 引用受访者参数
			 */
			String parametersStr = feed.getParametersStr();
			ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
			if (!Util.isEmpty(parametersStr)) {
				ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
						.parseArray(parametersStr, Parameter.class);
				if (!Util.isEmpty(tParameters)) {
					parameterList.addAll(tParameters);
				}
			}
			// System.out.println("parameterList.size():"+parameterList.size());
			CstmMatcher parameterMatcherList = Util
					.findMatcherPropertyItemList(strTilte, parameterList);
			boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
			if (!parameterHave) {
				strTilte = parameterMatcherList.getResultStr();
			}

			String must = "<font color='red'>"
					+ "*"
					+ "</font>";

			/**
			 * 必填
			 */
			if (1 == q.qRequired) {
				strTilte = must + strTilte;
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeModeNoPageActivity.this.getFilesDir()
							+ File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					// System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						float tWidth = image.getWidth();
						float tHeight = image.getHeight();
						if (tWidth > maxCWidth) {
							tHeight = maxCWidth / tWidth * tHeight;
							tWidth = maxCWidth - 6;
						}
						drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(strTilte, imgGetter, null);
			tvTitle.setText(fromHtml);
			// 更改的样式
			if (1 == q.qRadomed) {
				String radomed = getResources().getString(
						R.string.question_random);
				SpannableString ss = new SpannableString(radomed);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0,
						radomed.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}
			// 提示
			if (0 < q.lowerBound || 0 < q.upperBound) {
				String bound = "";
				if (0 == q.upperBound) {
					bound = getResources().getString(
							R.string.question_bound_min, q.lowerBound);
					if (0 == q.lowerBound) {
						bound = "";
					}
				} else {
					bound = getResources().getString(R.string.question_bound,
							q.upperBound, q.lowerBound);
					if (0 == q.lowerBound) {
						bound = getResources().getString(
								R.string.question_bound_max, q.upperBound);
					}
				}
				SpannableString ss = new SpannableString(bound);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0,
						bound.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}

			/**
			 * 增加单题拍照按钮 在最后面
			 */
			if (q.qCamera == 1 && !Util.isEmpty(feed.getUuid())) {
				System.out.println("单题拍照");
				// System.out.println("有单题拍照");
				Drawable drawable = this.getResources().getDrawable(
						R.drawable.camera_btn_bg);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tvTitle.setCompoundDrawables(null, null, drawable, null);
				tvTitle.setOnClickListener(new CstOnClickListener(q, 0));
			} else {
				tvTitle.setCompoundDrawables(null, null, null, null);
				// System.out.println("无单题拍照");
			}
			/**
			 * 增加单题签名
			 */
			if (q.qSign == 1 && !Util.isEmpty(feed.getUuid())) {
				ivSign.setVisibility(View.VISIBLE);
				ivSign.setOnClickListener(new CstOnClickListener(q, 1));
			}
			/**
			 * 增加单题录音
			 */
			if (q.itemRecording == 1 && !Util.isEmpty(feed.getUuid())) {
				ivRecord.setVisibility(View.VISIBLE);
				ivRecord.setOnClickListener(new CstOnClickListener(q, 2));
			} else {
				ivRecord.setVisibility(View.GONE);
				ivRecord.setOnClickListener(null);
			}
			// }
			bodyView.setVisibility(View.VISIBLE);
		}

		/**
		 * 不直接使用q.qCaption而使用中间变量qCaption, 因为直接使用q.qCaption会将其值改变,
		 * 在上一下、下一页多次操作后, 导致界面显示不正确
		 */
		String qCaption = q.qCaption;

		/**
		 * 假如题目有上方追加说明
		 */
		if (!Util.isEmpty(qCaption)) {
			/**
			 * 显示上方追加说明的控件
			 */
			tvCaption.setVisibility(View.VISIBLE);
			/**
			 * 设置上方追加说明的位置
			 */
			if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
				tvCaption.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
				tvCaption.setGravity(Gravity.RIGHT);
			} else if (Cnt.POS_LEFT.equals(q.qCommentPosition)) {
				tvCaption.setGravity(Gravity.LEFT);
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeModeNoPageActivity.this.getFilesDir()
							+ File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					// System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						float tWidth = image.getWidth();
						float tHeight = image.getHeight();
						if (tWidth > maxCWidth) {
							tHeight = maxCWidth / tWidth * tHeight;
							tWidth = maxCWidth - 6;
						}
						drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(qCaption, imgGetter, null);
			tvCaption.setText(fromHtml);
			// 更改的样式
		}
		// }

		/**
		 * 不直接使用q.qComment而使用中间变量qComment, 因为直接使用q.qComment会将其值改变,
		 * 在上一下、下一页多次操作后, 导致界面显示不正确
		 */
		String qComment = q.qComment;
		/**
		 * 假如题目有下方追加说明
		 */
		if (!Util.isEmpty(qComment)) {
			/**
			 * 显示上方追加说明的控件
			 */
			tvComment.setVisibility(View.VISIBLE);
			System.out.println("q.qCommentPosition:" + q.qCommentPosition);
			/**
			 * 设置上方追加说明的位置
			 */
			if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.RIGHT);
			} else if (Cnt.POS_LEFT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.LEFT);
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeModeNoPageActivity.this.getFilesDir()
							+ File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					// System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						float tWidth = image.getWidth();
						float tHeight = image.getHeight();
						if (tWidth > maxCWidth) {
							tHeight = maxCWidth / tWidth * tHeight;
							tWidth = maxCWidth - 6;
						}
						drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(q.qComment, imgGetter, null);
			tvComment.setText(fromHtml);
			// 更改的样式

		}
		// Answer an = tmpAnswerList.get(q.qIndex);
		// Answer an = ma.dbService.getAnswer(qAnswer.uuid, q.qIndex)
//		获取保存的答案
		ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
		if (null != tempAnswer) {
			amList = tempAnswer.getAnswerMapArr();
		}
		q.hideCount = 0;
		q.realRows = 0;
		// 大树 添加 双引用在里实现 双引用
		final ArrayList<String> twoQsiteOptions = new ArrayList<String>(); // 双引用
																			// 题目
		if (!Util.isEmpty(q.qSiteOption2)) {
			// 单复选矩阵固定
			// if (isFixed) {
			// twoSiteNoticeTv.setVisibility(View.GONE);
			// } else {
			// twoSiteNoticeTv.setVisibility(View.VISIBLE);
			// }
			if (q.qSiteOption2.indexOf(",") == -1) {
				twoQsiteOptions.add(q.qSiteOption2);
			} else {
				for (int i = 0; i < q.qSiteOption2.split(",").length; i++) {
					twoQsiteOptions.add(q.qSiteOption2.split(",")[i]);
				}
			}
		} else {
			twoSiteNoticeTv.setVisibility(View.GONE);
		}
		// 大树 以上
		switch (q.qType) {
		case Cnt.TYPE_HEADER:// 标题
			// tvTitle.setMaxWidth(Integer.MAX_VALUE);
			break;

		case Cnt.TYPE_RADIO_BUTTON:// 单选按钮
		{
			/**
			 * 题型的横向、纵向摆放 追加说明新布局 && 0 == q.isHaveItemCap
			 */
			bodyView.setOrientation(LinearLayout.VERTICAL);
			/**
			 * 行
			 */
			ArrayList<QuestionItem> radioRows = new ArrayList<QuestionItem>();
			radioRows.addAll(q.getRowItemArr());
			if (!Util.isEmpty(radioRows)) {
				boolean isDumbOk = false;// 哑题 这一项是否成功
				Answer an = null;
				boolean isInclusion = false;
				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					isInclusion = true;
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (null != an) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							if (an.answerType == Cnt.TYPE_RADIO_BUTTON
									&& !Util.isEmpty(am.getParentValue())) {
								System.out.println("am.getParentValue()):"
										+ am.getParentValue());
								// if (Cnt.TYPE_RADIO_BUTTON ==
								// an.answerType) {
								// have.add(am.getRow());
								// } else {
								// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								// }
								// 双引用but修改
								if (Util.isFormat(am.getParentValue(), 9)) {
									have.add(Integer.valueOf(am
											.getParentValue()));
								} else {
									have.add(Integer.parseInt(am
											.getAnswerName().split("_")[3]
											.trim()));
								}
							} else if (!Util.isEmpty(am.getAnswerValue())) {
								System.out.println("am.getAnswerValue()):"
										+ am.getAnswerValue());
								// if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
								// have.add(am.getRow());
								// } else {
								// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								// }
								// 双引用but修改
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am
											.getAnswerValue()));
								} else {
									have.add(Integer.parseInt(am
											.getAnswerName().split("_")[3]
											.trim()));
								}
							}
						}
						// 大树 一下 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 把所有要排斥的选项添加到集合hava中
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {
									Answer anQsite = ma.dbService.getAnswer(
											feed.getUuid(),
											twoQsiteOptions.get(i));
									if (anQsite != null
											&& anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite
												.getAnswerMapArr()) {
											if (an.answerType == Cnt.TYPE_RADIO_BUTTON
													&& !Util.isEmpty(am
															.getParentValue())) {
												// if (Cnt.TYPE_RADIO_BUTTON
												// ==
												// an.answerType) {
												// have.add(am.getRow());
												// } else {
												// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												// }
												// 双引用but修改
												if (Util.isFormat(
														am.getParentValue(), 9)) {
													have.add(Integer.valueOf(am
															.getParentValue()));
												} else {
													have.add(Integer
															.parseInt(am
																	.getAnswerName()
																	.split("_")[3]
																	.trim()));
												}

											} else if (!Util.isEmpty(am
													.getAnswerValue())) {
												/**
												 * 假如有值 自动查重 去除重复
												 */
												// if (Cnt.TYPE_RADIO_BUTTON ==
												// an.answerType) {
												// have.add(am.getRow());
												// } else {
												// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												// }
												// 双引用but修改
												if (Util.isFormat(
														am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am
															.getAnswerValue()));
												} else {
													have.add(Integer
															.parseInt(am
																	.getAnswerName()
																	.split("_")[3]
																	.trim()));
												}
											}
										}
									}
								}
							}
						}
						twoQsiteOptions.add(0, q.qSiteOption);
						// 大树 以上部分
						have = Util.getSiteOption(feed, q, ma, have, radioRows,
								q);
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							// have里面找到这个项
							if (-1 != have.indexOf(qi.itemValue)
									&& 1 != qi.itemShow) {
								/**
								 * 选择了这些选项
								 */
								radioRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(radioRows);
					} else {
						mTempRows.addAll(radioRows);
					}
				} else if ("0".equals(q.qInclusion)) {
					isInclusion = true;
					/**
					 * 引用
					 */
					an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
					if (an != null) {
						ArrayList<Integer> have = new ArrayList<Integer>();
						Map<Integer, String> amMap = new HashMap<Integer, String>();
						for (AnswerMap am : an.getAnswerMapArr()) {
							if (an.answerType == Cnt.TYPE_RADIO_BUTTON
									&& !Util.isEmpty(am.getParentValue())) {

								// if (Cnt.TYPE_RADIO_BUTTON ==
								// an.answerType) {
								// have.add(am.getRow());
								// } else {
								// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								// }
								// 双引用but修改
								if (Util.isFormat(am.getParentValue(), 9)) {
									have.add(Integer.valueOf(am
											.getParentValue()));
								} else {
									have.add(Integer.parseInt(am
											.getAnswerName().split("_")[3]
											.trim()));
									if ("2".equals(am.getAnswerName()
											.split("_")[4])) {
										amMap.put(Integer.parseInt(am
												.getAnswerName().split("_")[3]
												.trim()), am.getAnswerText());
									}
								}

							}
							/**
							 * 假如有值
							 */
							else if (!Util.isEmpty(am.getAnswerValue())) {
								// if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
								// have.add(am.getRow());
								// } else {
								// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
								// }
								// 双引用but修改
								if (Util.isFormat(am.getAnswerValue(), 9)) {
									have.add(Integer.valueOf(am
											.getAnswerValue()));
								} else {
									have.add(Integer.parseInt(am
											.getAnswerName().split("_")[3]
											.trim()));
									if ("2".equals(am.getAnswerName()
											.split("_")[4])) {
										amMap.put(Integer.parseInt(am
												.getAnswerName().split("_")[3]
												.trim()), am.getAnswerText());
									}
								}
							}
						}
						// 大树 添加 双引用在里实现 以下： 双引用
						if (!Util.isEmpty(q.qSiteOption2)) {
							// 大树 输出 自动查重 的功能
							if (twoQsiteOptions.size() > 0) {
								for (int i = 0; i < twoQsiteOptions.size(); i++) {

									Answer anQsite = ma.dbService.getAnswer(
											feed.getUuid(),
											twoQsiteOptions.get(i));
									if (anQsite != null
											&& anQsite.getAnswerMapArr().size() > 0) {
										for (AnswerMap am : anQsite
												.getAnswerMapArr()) {
											if (an.answerType == Cnt.TYPE_RADIO_BUTTON
													&& !Util.isEmpty(am
															.getParentValue())) {

												// if (Cnt.TYPE_RADIO_BUTTON
												// ==
												// an.answerType) {
												// have.add(am.getRow());
												// } else {
												// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												// }
												// 双引用but修改
												if (Util.isFormat(
														am.getParentValue(), 9)) {
													have.add(Integer.valueOf(am
															.getParentValue()));
												} else {
													have.add(Integer
															.parseInt(am
																	.getAnswerName()
																	.split("_")[3]
																	.trim()));
													if ("2".equals(am
															.getAnswerName()
															.split("_")[4])) {
														amMap.put(
																Integer.parseInt(am
																		.getAnswerName()
																		.split("_")[3]
																		.trim()),
																am.getAnswerText());
													}
												}

											}
											/**
											 * 假如有值 自动查重 去除重复
											 */
											else if (!Util.isEmpty(am
													.getAnswerValue())) {
												// if (Cnt.TYPE_RADIO_BUTTON ==
												// an.answerType) {
												// have.add(am.getRow());
												// } else {
												// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
												// }
												// 双引用but修改
												if (Util.isFormat(
														am.getAnswerValue(), 9)) {
													have.add(Integer.valueOf(am
															.getAnswerValue()));
												} else {
													have.add(Integer
															.parseInt(am
																	.getAnswerName()
																	.split("_")[3]
																	.trim()));
													if ("2".equals(am
															.getAnswerName()
															.split("_")[4])) {
														amMap.put(
																Integer.parseInt(am
																		.getAnswerName()
																		.split("_")[3]
																		.trim()),
																am.getAnswerText());
													}
												}
											}
										}
									}
								}
							}

						}
						twoQsiteOptions.add(0, q.qSiteOption);
						// 引用没有用
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)
									&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								radioRows.remove(qi);
							} else if (-1 != have.indexOf(qi.itemValue)) {
								// Log.i("@@@", amMap.toString());
								if (null != amMap.get(qi.itemValue)) {
									qi.isAnOther = 1;
									qi.isOther = 0;
									qi.itemText = amMap.get(qi.itemValue);
									int rowindex = radioRows.indexOf(qi);
									radioRows.remove(rowindex);
									radioRows.add(rowindex, qi);
								}
							}
						}
						have.clear();
						have = null;
						amMap.clear();
						amMap = null;
						mTempRows.addAll(radioRows);
					} else {
						radioRows.removeAll(radioRows);
						mTempRows.addAll(radioRows);
					}
				}

				/**
				 * 暂存单选题的所有选项
				 */
				ArrayList<QuestionItem> tempRows = new ArrayList<QuestionItem>();
				// 加radioRows
				tempRows.addAll(radioRows);

				/**
				 * 暂存单选题的其他项,因为其他项不需要随机,所以单独处理
				 */
				ArrayList<QuestionItem> otherItems = new ArrayList<QuestionItem>();

				Random rand = new Random();
				// 额外的
				ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
				/**
				 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
				 * 
				 * 获取 所有选项置顶的 选项 集合
				 */
				boolean isItemStick = ComUtil.isItemStick(tempRows)[0];
				int[] itemList = ComUtil.getItemStick(tempRows);// 置顶标示位置 数组
				int counter = 0; // 计数器
				ArrayList<QuestionItem> botItem = new ArrayList<QuestionItem>();
				/**
				 * 题外关联 之选项置顶 给其他项 itemvalue 赋值 并且删除掉 置底
				 */
				if (isItemStick) {
					for (int i = 0; i < tempRows.size(); i++) {
						QuestionItem item = tempRows.get(i);
						if (item.isOther == 1) {
							item.itemValue = i;
						}
					}
				}
				// 如果是横向布局的
				boolean isMultiline = false;// 判断是否为多列显示
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					isMultiline = true;
				}
				// 判断当前是每行第几列
				int lineNum = 0;
				int actualRowsNum = 1;// 实际选项列数
				if (isMultiline) {
					actualRowsNum = q.rowsNum;
					if (radioRows.size() < q.rowsNum) {
						actualRowsNum = radioRows.size();
					}
				}
				float rowsOneWidth = (float) maxCWidth / (float) actualRowsNum;

				System.out.println("rowsOneWidth" + rowsOneWidth
						+ "actualRowsNum" + actualRowsNum);
				/**
				 * 处理随机项
				 */
				for (int i = 0; i < radioRows.size(); i++) {
					QuestionItem item;

					// 题外关联 之 选项置顶 文本 获取

					/**
					 * 假如题目要求选项随机
					 */
					if (1 == q.qRadomed) {

						/**
						 * 随机产生数组的下标值
						 */
						int index = rand.nextInt(tempRows.size());
						/**
						 * 取得随机产生的选项对象item
						 * 
						 */
						item = tempRows.get(index);

						/**
						 * 题外关联 之选项 置顶 选项的 操作 进行交换 得到 padding==1 等于置顶
						 * 
						 * 题外关联 之选项置底 选项的操作 进行交换 得到 padding==2 等于 置底
						 */
						if (1 != item.isOther) {

							if (null != item.padding && counter == 0
									&& item.padding != 1) {
								if (isItemStick) {
									index = itemList[0];
									item = tempRows.get(index);
								}
							}
							counter++;
							// 删除掉 置底 的选项
							if (null != item.padding && item.padding == 2) {
								botItem.add(item);
								tempRows.remove(index);
								continue;
							}
						}

						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						tempRows.remove(index);
					} else {
						/**
						 * 假如题目的选项没有要求随机
						 */
						item = tempRows.get(i);
					}
					if (1 == item.isOther) {// 其他项
						// System.out.println("其他项");
						/**
						 * 将其他项暂存在otherItems集合中,此处不显示,留在最后显示
						 */
						otherItems.add(item);
					} else {
						/**
						 * 生成一个单选布局
						 */
						// 生成一项的整体布局
						LinearLayout rbA = new LinearLayout(
								NativeModeNoPageActivity.this);
						rbA.setOrientation(LinearLayout.VERTICAL);
						if (isMultiline) {// 如果是多列则进入判断
							if (actualRowsNum == lineNum) {
								lineNum = 0;
							}
							if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
								LinearLayout rbM = new LinearLayout(
										NativeModeNoPageActivity.this);
								rbM.setOrientation(LinearLayout.HORIZONTAL);
								bodyView.addView(rbM, bodyView.getChildCount());
								arrLayout.add(rbM);
							}
							lineNum++;
						}
						ArrayList<QuestionItem> childRows = item.getChildRows();
						if (0 < childRows.size()) {// 是否有子项
							TextView tvItemTitle = new TextView(
									NativeModeNoPageActivity.this);
							// 追加说明新布局原FILL_WRAP
							tvItemTitle
									.setLayoutParams(new LinearLayout.LayoutParams(
											(int) rowsOneWidth,
											LinearLayout.LayoutParams.WRAP_CONTENT));
							tvItemTitle.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							final float imgItemWidth = rowsOneWidth;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgItemWidth) {
											tHeight = imgItemWidth / tWidth
													* tHeight;
											tWidth = imgItemWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(
									item.getItemText(), imgGetter, null);
							tvItemTitle.setText(fromHtml);
							rbA.addView(tvItemTitle);
							for (int cr = 0; cr < childRows.size(); cr++) {
								QuestionItem childItem = childRows.get(cr);
								HashMap<String, Object> map = addRb(
										rowsOneWidth, amList, arrLayout,
										childItem, rbA, otherItems, isDumbOk,
										lineNum, vs, bodyView, tempAnswer,
										tempRows, q, position);
								isDumbOk = (Boolean) map.get("isDumbOk");
								lineNum = (Integer) map.get("lineNum");
							}
						} else {
							HashMap<String, Object> map = addRb(rowsOneWidth,
									amList, arrLayout, item, rbA, otherItems,
									isDumbOk, lineNum, vs, bodyView,
									tempAnswer, tempRows, q, position);
							isDumbOk = (Boolean) map.get("isDumbOk");
							lineNum = (Integer) map.get("lineNum");
						}
						/**
						 * 单选题目选项有图片
						 */
						if (0 < arrLayout.size()) {
							LinearLayout rbM = arrLayout
									.get(arrLayout.size() - 1);
							rbM.addView(rbA, rbM.getChildCount());
						} else {
							bodyView.addView(rbA, bodyView.getChildCount());
						}
					}
				}

				/**
				 * 题外关联 之 选项置顶 其他项的的处理 把置底的选项 扔到 其他项里头 是最好的选择
				 */
				if (0 < botItem.size()) {
					otherItems.addAll(botItem);
				}
				// Collections.sort()
				// 处理其他项, 因为其他项不需要随机
				for (int i = 0; i < otherItems.size(); i++) {
					// 获取其他项item
					QuestionItem item = otherItems.get(i);
					/**
					 * 生成一个单选布局
					 */
					// 生成一项的整体布局
					LinearLayout rbA = new LinearLayout(
							NativeModeNoPageActivity.this);
					rbA.setOrientation(LinearLayout.VERTICAL);
					if (isMultiline) {// 如果是多列则进入判断
						if (actualRowsNum == lineNum) {
							lineNum = 0;
						}
						if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
							LinearLayout rbM = new LinearLayout(
									NativeModeNoPageActivity.this);
							rbM.setOrientation(LinearLayout.HORIZONTAL);
							bodyView.addView(rbM, bodyView.getChildCount());
							arrLayout.add(rbM);
						}
						lineNum++;
					}
					if (1 != item.isOther) {
						ArrayList<QuestionItem> childRows = item.getChildRows();
						if (0 < childRows.size()) {// 是否有子项
							TextView tvItemTitle = new TextView(
									NativeModeNoPageActivity.this);
							// 追加说明新布局原FILL_WRAP
							tvItemTitle
									.setLayoutParams(new LinearLayout.LayoutParams(
											(int) rowsOneWidth,
											LinearLayout.LayoutParams.WRAP_CONTENT));
							tvItemTitle.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							final float imgItemWidth = rowsOneWidth;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgItemWidth) {
											tHeight = imgItemWidth / tWidth
													* tHeight;
											tWidth = imgItemWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(
									item.getItemText(), imgGetter, null);
							tvItemTitle.setText(fromHtml);
							rbA.addView(tvItemTitle);
							for (int cr = 0; cr < childRows.size(); cr++) {
								QuestionItem childItem = childRows.get(cr);
								HashMap<String, Object> map = addRb(
										rowsOneWidth, amList, arrLayout,
										childItem, rbA, otherItems, isDumbOk,
										lineNum, vs, bodyView, tempAnswer,
										tempRows, q, position);
								isDumbOk = (Boolean) map.get("isDumbOk");
								lineNum = (Integer) map.get("lineNum");
							}
						} else {
							HashMap<String, Object> map = addRb(rowsOneWidth,
									amList, arrLayout, item, rbA, otherItems,
									isDumbOk, lineNum, vs, bodyView,
									tempAnswer, tempRows, q, position);
							isDumbOk = (Boolean) map.get("isDumbOk");
							lineNum = (Integer) map.get("lineNum");
						}
					} else {
						/**
						 * 生成一个单选布局
						 */
						LinearLayout rbLL = new LinearLayout(
								NativeModeNoPageActivity.this);
						rbLL.setOrientation(LinearLayout.HORIZONTAL);
						rbA.addView(rbLL, rbA.getChildCount());
						if (!Util.isEmpty(item.popUp)) {
							/**
							 * 生成弹窗说明小图
							 */
							RadioButton imTv = new RadioButton(
									NativeModeNoPageActivity.this);
							imTv.setOnClickListener(new ImTvListenner(
									item.popUp));
							imTv.setButtonDrawable(R.drawable.tip);
							rbLL.addView(imTv, rbLL.getChildCount());
						}
						RadioButton rb = new RadioButton(
								NativeModeNoPageActivity.this);
						if (-1 != item.itemValue) {
							LinearLayout otherLayout = new LinearLayout(
									NativeModeNoPageActivity.this);
							otherLayout.setOrientation(LinearLayout.HORIZONTAL);
							otherLayout.setGravity(Gravity.CENTER_VERTICAL);
							if (1 < q.rowsNum
									&& Cnt.ORIENT_HORIZONTAL
											.equals(q.deployment)) {
								rbLL.setLayoutParams(new LinearLayout.LayoutParams(
										(int) rowsOneWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							} else {
								rbLL.setLayoutParams(new LinearLayout.LayoutParams(
										maxCWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							}
							rb.setFocusable(false);
							// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
							rb.setButtonDrawable(R.drawable.small_radiobutton);
							Drawable drawable = getResources().getDrawable(
									R.drawable.small_radiobutton_on);
							int leftwidth = drawable.getIntrinsicWidth();
							rb.setTextColor(Color.BLACK);
							rb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							// rb.setText(item.getItemText());
							// 更改的样式
							final float imgWidth = rowsOneWidth - leftwidth;
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgWidth) {
											tHeight = imgWidth / tWidth
													* tHeight;
											tWidth = imgWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(
									item.getItemText(), imgGetter, null);
							rb.setText(fromHtml);
							rb.setMaxWidth((int) rowsOneWidth * 2 / 3);
							// 更改的样式
							// if (null != item.padding && item.padding == 1 &&
							// 1 != item.isOther) {
							// String radomed =
							// getResources().getString(R.string.option_bottom);
							// SpannableString ss = new
							// SpannableString(radomed);
							// ss.setSpan(new ForegroundColorSpan(Color.RED), 0,
							// radomed.length(),
							// Spanned.SPAN_COMPOSING);
							// rb.append(ss);
							// }
							String idStr = "ohter_" + q.qIndex + "_" + i;
							rb.setId(idStr.hashCode());
							// System.out.println("radio_id="+idStr.hashCode());
							EditText et = new EditText(
									NativeModeNoPageActivity.this);
							et.setFocusableInTouchMode(false);
							et.setFocusable(false);
							et.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							et.setBackgroundResource(R.drawable.bg_edittext);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							// if (1 < actualRowsNum) {
							// params.setMargins(leftwidth, 0, 0, 0);
							// }
							et.setLayoutParams(params);
							et.setMinimumWidth(125);
							// et.setId(free.itemValue);
							et.setId((idStr + "_et").hashCode());
							if (!Util.isEmpty(amList)) {
								// System.out.println("amList != null");
								String name = Util.GetAnswerName(q, item, 0, 0,
										false, false);
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								// System.out.println("etName--->"+etName);
								for (AnswerMap am : amList) {
									// System.out.println("am="+am.getAnswerName());
									if (name.equals(am.getAnswerName().trim())
											&& String
													.valueOf(item.itemValue)
													.trim()
													.equals(am.getAnswerValue()
															.trim())) {
										// System.out.println("ddddddddd");
										rb.setChecked(true);
										item.isCheck = true;
										rb.setTag(item);
									}
									if (etName
											.equals(am.getAnswerName().trim())) {
										// System.out.println("etName=" + etName
										// +
										// ", etValue=" + am.getAnswerValue());
										et.setText(am.getAnswerValue());
										item.isCheck = true;
										et.setFocusableInTouchMode(true);
										et.setTag(item);
									}
								}
							}
							rbLL.addView(otherLayout, rbLL.getChildCount());
							/**
							 * 没有预选项
							 */
							otherLayout
									.addView(rb, otherLayout.getChildCount());
							vs.add(rb);
							/**
							 * 题外关联 之 选项置顶 去掉文本框 如果是置底
							 */
							if (1 == item.isOther) {
								otherLayout.addView(et,
										otherLayout.getChildCount());
							}
							// 将文本框加入集合中
							vs.add(et);
							rb.setOnCheckedChangeListener(new RadioListenner(
									idStr.hashCode(), 1, (idStr + "_et")
											.hashCode(), item, vs, q, position));
							et.addTextChangedListener(new EditChangedListener(
									q, vs, mTempRows, position));// 给EditText
							// 配置型隐藏选项
							if (setHideRb(item, q)) {
								rbA.setVisibility(View.GONE);
								if (rb.isChecked()) {
									rb.setChecked(false);
									rb.setTag(null);
									et.setTag(null);
									et.setText("");
									et.setFocusableInTouchMode(false);
									et.setFocusable(false);
									int success = getQuestionAnswer(
											false, q, vs, null, mTempRows,
											false);
								}
							}

						}
					}
					if (0 < arrLayout.size()) {
						LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
						rbM.addView(rbA, rbM.getChildCount());
					} else {
						bodyView.addView(rbA, bodyView.getChildCount());
					}
				}
				if (isInclusion) {
					int success = getQuestionAnswer( false, q, vs,
							null, mTempRows, false);
				}
				if (1 == q.qPreSelect) {
					// Log.i("@@@", "选中单选获取预选答案");
					int success = getQuestionAnswer( false, q, vs,
							null, mTempRows, false);
					// if (isREFRESH) {
					// if (Cnt.STATE_SUCCESS == success) {
					// Message msg = Message.obtain();
					// msg.what = REFRESH;
					// msg.arg1 = position;
					// handler.sendMessage(msg);
					// }
					// }
				}
				// 哑题赋完值，自动翻页。
				if (isDumbOk) {
					// int templ=-1;
					for (int l = 0; l < vs.size(); l++) {
						View tempV = vs.get(l);
						QuestionItem tag = (QuestionItem) tempV.getTag();
						if (tag != null) {
							// System.out.println("tag:"+tag.itemText);
							// System.out.println("tag.isDumbOk:"+tag.isDumbOk);
							if (!tag.isDumbOk) {
								tempV.setTag(null);
							}
						}
						// if(templ!=-1){
						// vs.remove(templ);
						// }
					}
					// Log.i("@@@", "选中单选获取答案");
					int success = getQuestionAnswer( false, q, vs,
							null, mTempRows, false);
					// if (isREFRESH) {
					// if (Cnt.STATE_SUCCESS == success) {
					// Message msg = Message.obtain();
					// msg.what = REFRESH;
					// msg.arg1 = position;
					// handler.sendMessage(msg);
					// }
					//
					// }
				}
				if (1 == q.qDumbFlag) {
					// 哑题没有对应选项清空
					convertView = (LinearLayout) new LinearLayout(this);
					convertView.setLayoutParams(new AbsListView.LayoutParams(0,
							0));
					return convertView;
				}
				// TODO
			}
		}
			break;
		case Cnt.TYPE_CHECK_BOX:// 复选框
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			// System.out.println("复选题");
			if (true) {
				/**
				 * 题型的横向、纵向摆放 追加说明新布局&& 0 == q.isHaveItemCap
				 */
				bodyView.setOrientation(LinearLayout.VERTICAL);
				ArrayList<QuestionItem> checkRows = new ArrayList<QuestionItem>();
				checkRows.addAll(q.getRowItemArr());
				Random rand = new Random();
				if (!Util.isEmpty(checkRows)) {
					boolean isDumbOk = false;// 哑题 这一项是否成功
					Answer an = null;
					boolean isInclusion = false;
					/**
					 * 排斥
					 */
					if ("1".equals(q.qInclusion)) {
						isInclusion = true;
						// isInclusion = true;
						// Question _q =
						// ma.dbService.getQuestion(feed.getSurveyId(),
						// q.qSiteOption);
						an = ma.dbService.getAnswer(feed.getUuid(),
								q.qSiteOption);
						if (null != an) {
							ArrayList<Integer> have = new ArrayList<Integer>();
							for (AnswerMap am : an.getAnswerMapArr()) {
								/**
								 * 假如有值
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									// if (Cnt.TYPE_RADIO_BUTTON ==
									// an.answerType) {
									// // 单选加法
									// have.add(am.getRow());
									// } else {
									// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
									// }
									// 双引用but修改
									if (Util.isFormat(am.getAnswerValue(), 9)) {
										have.add(Integer.valueOf(am
												.getAnswerValue()));
									} else {
										have.add(Integer.parseInt(am
												.getAnswerName().split("_")[3]
												.trim()));
									}
								}
							}
							// 大树 一下 双引用
							if (!Util.isEmpty(q.qSiteOption2)) {
								// 大树 输出 把所有要排斥的选项添加到集合hava中
								if (twoQsiteOptions.size() > 0) {
									for (int i = 0; i < twoQsiteOptions.size(); i++) {
										Answer anQsite = ma.dbService
												.getAnswer(feed.getUuid(),
														twoQsiteOptions.get(i));
										if (anQsite != null
												&& anQsite.getAnswerMapArr()
														.size() > 0) {
											for (AnswerMap am : anQsite
													.getAnswerMapArr()) {
												/**
												 * 假如有值 自动查重 去除重复
												 */
												if (!Util.isEmpty(am
														.getAnswerValue())) {
													// if (Cnt.TYPE_RADIO_BUTTON
													// ==
													// an.answerType) {
													// have.add(am.getRow());
													// } else {
													// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
													// }
													// 双引用but修改
													if (Util.isFormat(
															am.getAnswerValue(),
															9)) {
														have.add(Integer.valueOf(am
																.getAnswerValue()));
													} else {
														have.add(Integer
																.parseInt(am
																		.getAnswerName()
																		.split("_")[3]
																		.trim()));
													}
												}
											}
										}
									}
								}
							}
							twoQsiteOptions.add(0, q.qSiteOption);
							// 大树 以上部分
							have = Util.getSiteOption(feed, q, ma, have,
									checkRows, q);
							for (int z = 0; z < have.size(); z++) {
								System.out.println(z + ":" + have.get(z));
							}
							for (int i = 0; i < q.getRowItemArr().size(); i++) {
								QuestionItem qi = q.getRowItemArr().get(i);
								if (-1 != have.indexOf(qi.itemValue)
										&& 1 != qi.itemShow) {
									/**
									 * 选择了这些选项
									 */
									checkRows.remove(qi);
								}
							}
							have.clear();
							have = null;
							mTempRows.addAll(checkRows);
						} else {
							mTempRows.addAll(checkRows);
						}
					} else if ("0".equals(q.qInclusion)) {
						isInclusion = true;
						/**
						 * 引用
						 */
						// isInclusion = true;
						// Question _q =
						// ma.dbService.getQuestion(feed.getSurveyId(),
						// q.qSiteOption);
						an = ma.dbService.getAnswer(feed.getUuid(),
								q.qSiteOption);
						if (an != null) {
							ArrayList<Integer> have = new ArrayList<Integer>();
							Map<Integer, String> amMap = new HashMap<Integer, String>();
							for (AnswerMap am : an.getAnswerMapArr()) {
								/**
								 * 假如有值
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									// if (Cnt.TYPE_RADIO_BUTTON ==
									// an.answerType) {
									// have.add(am.getRow());
									// } else {
									// String answerValue = am.getAnswerValue();
									// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
									// }
									// 双引用but修改
									if (Util.isFormat(am.getAnswerValue(), 9)) {
										have.add(Integer.valueOf(am
												.getAnswerValue()));
									} else {
										have.add(Integer.parseInt(am
												.getAnswerName().split("_")[3]
												.trim()));
										if ("2".equals(am.getAnswerName()
												.split("_")[4])) {
											amMap.put(Integer
													.parseInt(am
															.getAnswerName()
															.split("_")[3]
															.trim()), am
													.getAnswerText());
										}
									}
								}
							}
							// 大树 添加 双引用在里实现 以下： 双引用
							if (!Util.isEmpty(q.qSiteOption2)) {
								// 大树 输出 自动查重 的功能
								if (twoQsiteOptions.size() > 0) {
									for (int i = 0; i < twoQsiteOptions.size(); i++) {

										Answer anQsite = ma.dbService
												.getAnswer(feed.getUuid(),
														twoQsiteOptions.get(i));
										if (anQsite != null
												&& anQsite.getAnswerMapArr()
														.size() > 0) {
											for (AnswerMap am : anQsite
													.getAnswerMapArr()) {
												/**
												 * 假如有值 自动查重 去除重复
												 */
												if (!Util.isEmpty(am
														.getAnswerValue())) {
													// if (Cnt.TYPE_RADIO_BUTTON
													// ==
													// an.answerType) {
													// have.add(am.getRow());
													// } else {
													// have.add(Integer.parseInt(am.getAnswerName().split("_")[3]));
													// }
													// 双引用but修改
													if (Util.isFormat(
															am.getAnswerValue(),
															9)) {
														have.add(Integer.valueOf(am
																.getAnswerValue()));
													} else {
														have.add(Integer
																.parseInt(am
																		.getAnswerName()
																		.split("_")[3]
																		.trim()));
														if ("2".equals(am
																.getAnswerName()
																.split("_")[4])) {
															amMap.put(
																	Integer.parseInt(am
																			.getAnswerName()
																			.split("_")[3]
																			.trim()),
																	am.getAnswerText());
														}
													}
												}
											}
										}
									}
								}

							}
							twoQsiteOptions.add(0, q.qSiteOption);
							// 大树 以上
							for (int i = 0; i < q.getRowItemArr().size(); i++) {
								QuestionItem qi = q.getRowItemArr().get(i);
								if (-1 == have.indexOf(qi.itemValue)
										&& 1 != qi.itemShow) {
									/**
									 * 没有选择的, 则移走
									 */
									checkRows.remove(qi);
								} else if (-1 != have.indexOf(qi.itemValue)) {
									// Log.i("@@@", amMap.toString());
									if (null != amMap.get(qi.itemValue)) {
										qi.isAnOther = 1;
										qi.isOther = 0;
										qi.itemText = amMap.get(qi.itemValue);
										int rowindex = checkRows.indexOf(qi);
										checkRows.remove(rowindex);
										checkRows.add(rowindex, qi);
									}
								}
							}
							have.clear();
							have = null;
							amMap.clear();
							amMap = null;
							mTempRows.addAll(checkRows);
						} else {
							checkRows.removeAll(checkRows);
							mTempRows.addAll(checkRows);
						}
					}
					ArrayList<QuestionItem> tempRows = new ArrayList<QuestionItem>();
					tempRows.addAll(checkRows);

					ArrayList<QuestionItem> otherRows = new ArrayList<QuestionItem>();

					ArrayList<LinearLayout> arrLayout = new ArrayList<LinearLayout>();
					/**
					 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
					 * 
					 * 获取 所有选项置顶的 选项 集合
					 */
					boolean isItemStick = ComUtil.isItemStick(tempRows)[0];
					int[] itemList = ComUtil.getItemStick(tempRows);// 置顶标示位置 数组
					int counter = 0; // 计数器
					ArrayList<QuestionItem> botItem = new ArrayList<QuestionItem>();
					boolean isMultiline = false;// 判断是否为多列显示
					if (1 < q.rowsNum
							&& Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
						isMultiline = true;
					}
					// 判断当前是每行第几列
					int lineNum = 0;
					int actualRowsNum = 1;// 实际选项列数
					if (isMultiline) {
						actualRowsNum = q.rowsNum;
						if (checkRows.size() < q.rowsNum) {
							actualRowsNum = checkRows.size();
						}
					}
					float rowsOneWidth = (float) maxCWidth
							/ (float) actualRowsNum;
					for (int i = 0; i < checkRows.size(); i++) {
						QuestionItem item;

						if (1 == q.qRadomed) {
							/**
							 * 随机产生数组的下标值
							 */
							int index = rand.nextInt(tempRows.size());
							/**
							 * 取得随机产生的选项对象item
							 */

							item = tempRows.get(index);

							/**
							 * 题外关联 之选项 置顶 选项的 操作 进行交换 得到 padding==1 等于置顶
							 * 
							 * 题外关联 之选项置底 选项的操作 进行交换 得到 padding==2 等于 置底
							 */

							if (1 != item.isOther) {

								if (null != item.padding && counter == 0
										&& item.padding != 1) {
									if (isItemStick) {
										index = itemList[0];
										item = tempRows.get(index);
									}
								}
								counter++;
								// 删除掉 置底 的选项
								if (null != item.padding && item.padding == 2) {
									botItem.add(item);
									tempRows.remove(index);
									continue;
								}
							}

							/**
							 * 在暂存数组中移除随机产生的选项对象item
							 */
							tempRows.remove(index);
						} else {
							item = checkRows.get(i);
						}

						if (1 == item.isOther) {
							otherRows.add(item);
						} else {

							// 生成一项的整体布局
							LinearLayout rbA = new LinearLayout(
									NativeModeNoPageActivity.this);
							rbA.setOrientation(LinearLayout.VERTICAL);
							if (isMultiline) {// 如果是多列则进入判断
								if (actualRowsNum == lineNum) {
									lineNum = 0;
								}
								if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
									LinearLayout rbM = new LinearLayout(
											NativeModeNoPageActivity.this);
									rbM.setOrientation(LinearLayout.HORIZONTAL);
									rbM.setGravity(Gravity.CENTER_VERTICAL);
									bodyView.addView(rbM,
											bodyView.getChildCount());
									arrLayout.add(rbM);
								}
								lineNum++;
							}
							item.isCheck = false;
							CheckBox cb = new CheckBox(
									NativeModeNoPageActivity.this);
							LinearLayout llcb = new LinearLayout(
									NativeModeNoPageActivity.this);
							llcb.setOrientation(LinearLayout.HORIZONTAL);
							llcb.setGravity(Gravity.CENTER_VERTICAL);
							TextView tvcb = new TextView(
									NativeModeNoPageActivity.this);
							tvcb.setBackgroundResource(R.drawable.small_textview_off_disable_focused);
							tvcb.setGravity(Gravity.CENTER);
							tvcb.setTextColor(Color.RED);
							tvcb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							llcb.addView(tvcb);
							// 复选追加说明方法
							TextView tvItemCap = new TextView(
									NativeModeNoPageActivity.this);
							if (!Util.isEmpty(item.caption)) {
								if (1 == item.caption_check) {
									cb.setOnCheckedChangeListener(new MyRadioButtonClick(
											item.caption));
								} else {
									// 追加说明新布局原FILL_WRAP
									tvItemCap
											.setLayoutParams(new LinearLayout.LayoutParams(
													LinearLayout.LayoutParams.MATCH_PARENT,
													LinearLayout.LayoutParams.WRAP_CONTENT));
									tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
									tvItemCap.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											lowSurveySize);
									// 更改的样式
									ImageGetter imgGetter = new Html.ImageGetter() {
										public Drawable getDrawable(
												String source) {
											Drawable drawable = null;
											String name = NativeModeNoPageActivity.this
													.getFilesDir()
													+ File.separator
													+ "survey"
													+ File.separator
													+ feed.getSurveyId()
													+ File.separator + source;
											// System.out.println("name:" +
											// name);
											drawable = Drawable
													.createFromPath(name);
											Bitmap image = BitmapFactory
													.decodeFile(name);
											if (image != null) {
												float tWidth = image.getWidth();
												float tHeight = image
														.getHeight();
												if (tWidth > maxCWidth) {
													tHeight = maxCWidth
															/ tWidth * tHeight;
													tWidth = maxCWidth - 6;
												}
												drawable.setBounds(0, 0,
														(int) tWidth,
														(int) tHeight);
												return drawable;
											} else {
												return null;
											}
										}
									};
									Spanned fromHtml = Html.fromHtml(
											item.caption, imgGetter, null);
									tvItemCap.setText(fromHtml);
									// 更改的样式
								}
							}
							// 复选追加说明方法
							// CheckBox cb = new CheckBox(this);
							String idStr = q.qIndex + "_" + i;
							cb.setId(idStr.hashCode());
							tvcb.setId((idStr + "_tv").hashCode());
							// 复选百分比 原复选，现在复选
							// cb.setLayoutParams(WRAP_WRAP);
							if (1 < q.rowsNum
									&& Cnt.ORIENT_HORIZONTAL
											.equals(q.deployment)) {
								cb.setLayoutParams(new LinearLayout.LayoutParams(
										(int) rowsOneWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							} else {
								cb.setLayoutParams(new LinearLayout.LayoutParams(
										maxCWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							}
							// 复选百分比结束
							cb.setButtonDrawable(R.drawable.small_checkbox);
							if (1 == q.qSortChecked) {
								cb.setButtonDrawable(android.R.color.transparent);
							}
							Drawable drawable = getResources().getDrawable(
									R.drawable.small_checkbox_on);
							int leftwidth = drawable.getIntrinsicWidth();
							cb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							cb.setTextColor(Color.BLACK);
							// cb.setText(item.getItemText());
							final float imgWidth = rowsOneWidth - leftwidth;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgWidth) {
											tHeight = imgWidth / tWidth
													* tHeight;
											tWidth = imgWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(
									item.getItemText(), imgGetter, null);
							cb.setText(fromHtml);
							if (null != item.padding && item.padding == 1) {
								String radomed = getResources().getString(
										R.string.option_top);
								SpannableString ss = new SpannableString(
										radomed);
								ss.setSpan(new ForegroundColorSpan(Color.RED),
										0, radomed.length(),
										Spanned.SPAN_COMPOSING);
								cb.append(ss);
							}
							// 更改的样式
							if (1 == q.qSortChecked) {// 复选排序
								tvcb.setOnClickListener(new TvCheckListener(
										tvcb, cb, null, item, vs, q, mTempRows,
										position));
								cb.setClickable(false);
							} else {
								cb.setOnClickListener(new CheckListener(cb,
										null, item, vs, q, mTempRows, position));
							}
							String name = Util.GetAnswerName(q, item, 0, 0,
									false, false);
							/**
							 * 判断是否是哑题,然后逻辑判断。 新哑题
							 */
							// TODO
							// 假如是哑题 开始判断
							if (1 == q.qDumbFlag) {
								item.isDumbOk = false;
								HashMap<String, Object> map = setDumbRb(item,
										cb, isDumbOk, MSG_NEXT, tempAnswer, 1);
								isDumbOk = (Boolean) map.get("isDumbOk");
								item = (QuestionItem) map.get("item");
							}
							/**
							 * 新哑题 判断哑题结束
							 */
							if (1 == q.qPreSelect) {
								if (1 != q.qSortChecked) {
									// 假如没有哑题，就预设，
									if (1 == item.deft) {
										if (Util.isEmpty(amList)) {
											cb.setChecked(true);
											item.isCheck = true;
											cb.setTag(item);
										} else {
											for (AnswerMap am : amList) {
												if (name.equals(am
														.getAnswerName().trim())
														&& String
																.valueOf(
																		item.itemValue)
																.trim()
																.equals(am
																		.getAnswerValue()
																		.trim())) {
													// System.out.println("ddddddddd");
													cb.setChecked(true);
													item.isCheck = true;
													cb.setTag(item);
													break;
												}
											}
										}
									} else {
										// System.out.println("不是预选项");
										if (!Util.isEmpty(amList)) {// 题目有答案
											// System.out.println("题目有答案");
											for (AnswerMap am : amList) {
												if (name.equals(am
														.getAnswerName().trim())
														&& String
																.valueOf(
																		item.itemValue)
																.trim()
																.equals(am
																		.getAnswerValue()
																		.trim())) {
													// System.out.println("ddddddddd");
													cb.setChecked(true);
													item.isCheck = true;
													cb.setTag(item);
													break;
												}
											}
										}
									}
								}
							} else {
								if (!Util.isEmpty(amList)) {
									// System.out.println("复选题以前的答案集合不为空");
									String chName = Util.GetAnswerName(q, item,
											0, 0, false, false);
									// System.out.println("chName--->"+chName+",
									String tvName = Util.GetAnswerName(q, item,
											0, 0, false, true);
									// value="+item.itemValue);
									for (AnswerMap am : amList) {
										// System.out.prisntln("name="+am.getAnswerName()+",
										// value="+am.getAnswerValue());
										if (chName.equals(am.getAnswerName()
												.trim())
												&& am.getAnswerValue()
														.trim()
														.equals(String.valueOf(
																item.itemValue)
																.trim())) {
											cb.setChecked(true);
											item.isCheck = true;
											// cb.setTag(item);
											// System.out.println("复选题以前的答案配对");
										}
										if (tvName.equals(am.getAnswerName()
												.trim())// 排序题序号在itemvalue上保存，text保存原有item的value值
										) {

											if (0 == am.getAnswerValue()
													.indexOf("sort")) {
												String value = am
														.getAnswerValue();
												tvcb.setText(value.substring(4));
												item.isCheck = true;
											}
											// System.out.println("复选题以前的答案配对");
										}
									}
								}
							}
							// 追加说明方法复选
							if (!Util.isEmpty(item.caption)) {
								if (1 == item.caption_check) {

								} else {

									if (0 < arrLayout.size()) {
										if (1 != lineNum) {
											lineNum = 1;
											LinearLayout rbM = new LinearLayout(
													NativeModeNoPageActivity.this);
											rbM.setOrientation(LinearLayout.HORIZONTAL);
											rbM.setGravity(Gravity.CENTER_VERTICAL);
											bodyView.addView(rbM,
													bodyView.getChildCount());
											arrLayout.add(rbM);
										}
										bodyView.addView(tvItemCap,
												bodyView.getChildCount() - 1);
									} else {
										bodyView.addView(tvItemCap,
												bodyView.getChildCount());
									}
								}
							}
							/**
							 * 单选题目选项有图片
							 */
							if (1 == q.qSortChecked) {
								llcb.addView(cb, llcb.getChildCount());
								rbA.addView(llcb, rbA.getChildCount());
								if (0 < arrLayout.size()) {
									LinearLayout rbM = arrLayout.get(arrLayout
											.size() - 1);
									rbM.addView(rbA, rbM.getChildCount());
								} else {
									bodyView.addView(rbA,
											bodyView.getChildCount());
								}
							} else {
								rbA.addView(cb, rbA.getChildCount());
								if (0 < arrLayout.size()) {
									LinearLayout rbM = arrLayout.get(arrLayout
											.size() - 1);
									rbM.addView(rbA, rbM.getChildCount());
								} else {
									bodyView.addView(rbA,
											bodyView.getChildCount());
								}
							}
							vs.add(cb);
							if (1 == q.qSortChecked) {
								vs.add(tvcb);
							}
							// 隐藏选项
							// 配置型隐藏选项
							if (0 == q.qSortChecked) {
								if (setHideRb(item, q)) {
									tvItemCap.setVisibility(View.GONE);
									rbA.setVisibility(View.GONE);
									if (cb.isChecked()) {
										cb.setChecked(false);
										item.isCheck = false;
										int success = getQuestionAnswer(
												 false, q, vs, null,
												mTempRows, false);
									}
								}
							} else {
								if (setHideRb(item, q)) {
									tvItemCap.setVisibility(View.GONE);
									rbA.setVisibility(View.GONE);
									if (cb.isChecked()) {
										cb.setChecked(false);
										tvcb.setText("");
										item.isCheck = false;
										int success = getQuestionAnswer(
												 false, q, vs, null,
												mTempRows, false);
									}
								}
							}
							tvcb.setTag(item);
							cb.setTag(item);
							// 条件隐藏选项
						}
					}

					/**
					 * 题外关联 之 选项置顶 其他项的的处理 把置底的选项 扔到 其他项里头 是最好的选择
					 */
					if (0 < botItem.size()) {
						otherRows.addAll(botItem);
					}
					for (int i = 0; i < otherRows.size(); i++) {
						LinearLayout rbA = new LinearLayout(
								NativeModeNoPageActivity.this);
						rbA.setOrientation(LinearLayout.VERTICAL);
						if (isMultiline) {// 如果是多列则进入判断
							if (actualRowsNum == lineNum) {
								lineNum = 0;
							}
							if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
								LinearLayout rbM = new LinearLayout(
										NativeModeNoPageActivity.this);
								rbM.setOrientation(LinearLayout.HORIZONTAL);
								rbM.setGravity(Gravity.CENTER_VERTICAL);
								bodyView.addView(rbM, bodyView.getChildCount());
								arrLayout.add(rbM);
							}
							lineNum++;
						}
						QuestionItem item = otherRows.get(i);
						CheckBox cb = new CheckBox(
								NativeModeNoPageActivity.this);
						LinearLayout llcb = new LinearLayout(
								NativeModeNoPageActivity.this);
						llcb.setOrientation(LinearLayout.HORIZONTAL);
						llcb.setGravity(Gravity.CENTER_VERTICAL);
						TextView tvcb = new TextView(
								NativeModeNoPageActivity.this);
						tvcb.setBackgroundResource(R.drawable.small_textview_off_disable_focused);
						tvcb.setGravity(Gravity.CENTER);
						tvcb.setTextColor(Color.RED);
						tvcb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						llcb.addView(tvcb);
						tvcb.setTag(item);
						cb.setTag(item);
						Drawable drawable = getResources().getDrawable(
								R.drawable.small_checkbox_on);
						int leftwidth = drawable.getIntrinsicWidth();
						if (-1 != item.itemValue) {// 并且不是<freeInput/>这种空标签
							LinearLayout otherLayout = new LinearLayout(
									NativeModeNoPageActivity.this);
							otherLayout.setOrientation(LinearLayout.HORIZONTAL);
							otherLayout.setGravity(Gravity.CENTER_VERTICAL);
							cb.setLayoutParams(WRAP_WRAP);
							if (1 < q.rowsNum
									&& Cnt.ORIENT_HORIZONTAL
											.equals(q.deployment)) {
								otherLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												(int) rowsOneWidth,
												LinearLayout.LayoutParams.WRAP_CONTENT));
							} else {
								otherLayout
										.setLayoutParams(new LinearLayout.LayoutParams(
												maxCWidth,
												LinearLayout.LayoutParams.WRAP_CONTENT));
							}
							rbA.addView(otherLayout, rbA.getChildCount());

							String idStr = "ohter_" + q.qIndex + "_" + i;
							cb.setId(idStr.hashCode());
							tvcb.setId((idStr + "_tv").hashCode());
							cb.setButtonDrawable(R.drawable.small_checkbox);
							if (1 == q.qSortChecked) {
								cb.setButtonDrawable(android.R.color.transparent);
							}
							cb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							cb.setTextColor(Color.BLACK);
							final float imgWidth = rowsOneWidth - leftwidth;
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgWidth) {
											tHeight = imgWidth / tWidth
													* tHeight;
											tWidth = imgWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(
									item.getItemText(), imgGetter, null);
							cb.setText(fromHtml);
							cb.setMaxWidth((int) rowsOneWidth * 2 / 3);
							if (1 == q.qSortChecked) {
								cb.setLayoutParams(new LinearLayout.LayoutParams(
										(int) rowsOneWidth - leftwidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
								llcb.addView(cb, llcb.getChildCount());
								otherLayout.addView(llcb,
										otherLayout.getChildCount());
							} else {
								otherLayout.addView(cb,
										otherLayout.getChildCount());
							}
							vs.add(cb);
							EditText et = new EditText(
									NativeModeNoPageActivity.this);
							et.setFocusableInTouchMode(false);
							et.setFocusable(false);
							et.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							et.setBackgroundResource(R.drawable.bg_edittext);
							et.setTag(item);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							// if(1<actualRowsNum){
							// params.setMargins(leftwidth, 0, 0, 0);
							// }
							et.setLayoutParams(params);
							et.setMinimumWidth(125);
							if (!Util.isEmpty(amList)) {
								// System.out.println("复选题以前的答案集合不为空");
								String chName = Util.GetAnswerName(q, item, 0,
										0, false, false);
								// System.out.println("chName--->"+chName+",
								// value="+item.itemValue);
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								String tvName = Util.GetAnswerName(q, item, 0,
										0, false, true);
								// System.out.println("etName:"+etName);
								for (AnswerMap am : amList) {
									// System.out.println("name="+am.getAnswerName()+",
									// value="+am.getAnswerValue());
									if (chName
											.equals(am.getAnswerName().trim())
											&& am.getAnswerValue()
													.trim()
													.equals(String
															.valueOf(item.itemValue))) {
										cb.setChecked(true);
										item.isCheck = true;
										cb.setTag(item);
										// System.out.println("复选题以前的答案配对");
									}
									// System.out.println("复选框其他项etName=" +
									// etName +
									// ", name=" + am.getAnswerName() + ",
									// value=" +
									// am.getAnswerValue());
									// System.out.println("am.getAnswerName():"+am.getAnswerName());
									if (etName
											.equals(am.getAnswerName().trim())) {
										item.isCheck = true;
										// System.out.println("复选am.getAnswerValue():"+am.getAnswerValue());
										et.setText(am.getAnswerValue());
										et.setFocusableInTouchMode(true);
										// et.setTag(item);
									}
									if (tvName
											.equals(am.getAnswerName().trim())) {
										if (0 == am.getAnswerValue().indexOf(
												"sort")) {
											String value = am.getAnswerValue();
											tvcb.setText(value.substring(4));
											item.isCheck = true;
										}
										// System.out.println("复选题以前的答案配对");
									}
								}
							}
							// et.setId(free.itemValue);
							et.setId((idStr + "_et").hashCode());
							// otherLayout.setOnClickListener(new
							// RadioListenner(idStr.hashCode(), 1,
							// (idStr+"_et").hashCode()));
							/**
							 * 假如当前的CheckBox被选中了则让文本框可以编辑
							 * 假如没有被选中则让文本框中的内容为空并且是不可以编辑的
							 */
							if (1 == q.qSortChecked) {// 复选排序
								tvcb.setOnClickListener(new TvCheckListener(
										tvcb, cb, et, item, vs, q, mTempRows,
										position));
								cb.setClickable(false);
							} else {
								cb.setOnClickListener(new CheckListener(cb, et,
										item, vs, q, mTempRows, position));
							}
							/**
							 * 题外关联 之 选项置顶 添加其他项 的操作 选项置底 隐藏 文本框
							 */
							if (1 == item.isOther) {
								otherLayout.addView(et,
										otherLayout.getChildCount());
							}
							if (0 < arrLayout.size()) {
								LinearLayout rbM = arrLayout.get(arrLayout
										.size() - 1);
								rbM.addView(rbA, rbM.getChildCount());
							} else {
								bodyView.addView(rbA, bodyView.getChildCount());
							}
							// 将文本框假如结合中
							vs.add(et);
							et.addTextChangedListener(new EditChangedListener(
									q, vs, mTempRows, position));// 给EditText
							if (1 == q.qSortChecked) {
								vs.add(tvcb);
							}
							// 隐藏选项
							// 配置型隐藏选项
							if (setHideRb(item, q)) {
								rbA.setVisibility(View.GONE);
								if (cb.isChecked()) {
									cb.setChecked(false);
									et.setText("");
									et.setFocusable(false);
									et.setFocusableInTouchMode(false);
									if (1 == q.qSortChecked) {// 复选排序
										tvcb.setText("");
									}
									item.isCheck = false;
									int success = getQuestionAnswer(
											false, q, vs, null, mTempRows,
											false);
								}
							}
							tvcb.setTag(item);
							cb.setTag(item);
							// 配置型隐藏选项
						}
					}
					if (isInclusion) {
						int success = getQuestionAnswer( false, q, vs,
								null, mTempRows, false);
					}
					if (1 == q.qPreSelect) {
						// Log.i("@@@", "选中复选获取预选答案");
						int success = getQuestionAnswer( false, q, vs,
								null, mTempRows, false);
						// if (isREFRESH) {
						// if (Cnt.STATE_SUCCESS == success) {
						// Message msg = Message.obtain();
						// msg.what = REFRESH;
						// msg.arg1 = position;
						// handler.sendMessage(msg);
						// }
						//
						// }
					}
					// 哑题赋完值，自动翻页。
					if (isDumbOk) {
						// int templ=-1;
						for (int l = 0; l < vs.size(); l++) {
							View tempV = vs.get(l);
							QuestionItem tag = (QuestionItem) tempV.getTag();
							if (tag != null) {
								// System.out.println("tag:"+tag.itemText);
								// System.out.println("tag.isDumbOk:"+tag.isDumbOk);
								if (!tag.isDumbOk) {
									tempV.setTag(null);
								}
							}
							// if(templ!=-1){
							// vs.remove(templ);
							// }
						}
						// Log.i("@@@", "选中复选获取答案");
						int success = getQuestionAnswer( false, q, vs,
								null, mTempRows, false);
						// if (isREFRESH) {
						// if (Cnt.STATE_SUCCESS == success) {
						// Message msg = Message.obtain();
						// msg.what = REFRESH;
						// msg.arg1 = position;
						// handler.sendMessage(msg);
						// }
						//
						// }
					}
					if (1 == q.qDumbFlag) {
						// 哑题没有对应选项清空
						convertView = (LinearLayout) new LinearLayout(this);
						convertView
								.setLayoutParams(new AbsListView.LayoutParams(
										0, 0));
						return convertView;
					}
				}
			}
			break;
		case Cnt.TYPE_MATRIX_RADIO_BUTTON:// 矩阵单选
			if (true) {
				// ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
				// if (null != tempAnswer) {
				// amList = tempAnswer.getAnswerMapArr();
				// }
				matrixRadioBt(isFixed, twoQsiteOptions, amList, bodyView, vs,
						q, twoSiteNoticeTv, mTempRows, position);
			}
			break;

		case Cnt.TYPE_MATRIX_CHECK_BOX:// 矩阵复选
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			if (true) {
				/**
				 * 题型的横向、纵向摆放。
				 */
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					// System.out.println("横向");
					bodyView.setOrientation(LinearLayout.HORIZONTAL);
				} else {
					// System.out.println("纵向");
					bodyView.setOrientation(LinearLayout.VERTICAL);
				}
				/**
				 * 获取行标题
				 */
				ArrayList<QuestionItem> cRows = new ArrayList<QuestionItem>();
				cRows.addAll(q.getRowItemArr());
				/**
				 * 获取列标题
				 */
				ArrayList<QuestionItem> cColmns = new ArrayList<QuestionItem>();
				cColmns.addAll(q.getColItemArr());

				boolean _isInclusion = false;

				/**
				 * 排斥
				 */
				if ("1".equals(q.qInclusion)) {
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(),
							q.qSiteOption);
					ArrayList<AnswerMap> aml = an.getAnswerMapArr();
					ArrayList<Integer> have = new ArrayList<Integer>();
					if (null != aml) {
						for (AnswerMap am : aml) {
							/**
							 * 假如有值
							 */
							if (!Util.isEmpty(am.getAnswerValue())) {
								if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
									have.add(am.getRow());
								} else {
									have.add(Integer.parseInt(am
											.getAnswerName().split("_")[3]));
								}
							}
						}
						aml.clear();
						aml = null;
					}
					for (int i = 0; i < q.getRowItemArr().size(); i++) {
						QuestionItem qi = q.getRowItemArr().get(i);
						if (-1 != have.indexOf(qi.itemValue)
								&& 1 != qi.itemShow) {
							/**
							 * 选择了这些选项
							 */
							cRows.remove(qi);
						}
					}
					have.clear();
					have = null;
					mTempRows.addAll(cRows);
				} else if ("0".equals(q.qInclusion)) {
					/**
					 * 引用
					 */
					_isInclusion = true;
					// Question _q =
					// ma.dbService.getQuestion(feed.getSurveyId(),
					// q.qSiteOption);
					Answer an = ma.dbService.getAnswer(feed.getUuid(),
							q.qSiteOption);
					if (an != null) {
						ArrayList<AnswerMap> aml = (null == an) ? null : an
								.getAnswerMapArr();
						ArrayList<Integer> have = new ArrayList<Integer>();
						if (null != aml) {
							for (AnswerMap am : aml) {
								/**
								 * 假如有值
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
										have.add(am.getRow());
									} else {
										have.add(Integer.parseInt(am
												.getAnswerName().split("_")[3]));
									}
								}
							}
							aml.clear();
							aml = null;
						}
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (-1 == have.indexOf(qi.itemValue)
									&& 1 != qi.itemShow) {
								/**
								 * 没有选择的, 则移走
								 */
								cRows.remove(qi);
							}
						}
						have.clear();
						have = null;
						mTempRows.addAll(cRows);
					} else {
						cRows.retainAll(cRows);
						mTempRows.addAll(cRows);
					}
				}

				/**
				 * 假如是随即题目 则将其行和列都随机一遍
				 */
				if (1 == q.qRadomed) {
					/**
					 * 行随机暂存行中的数据
					 */
					ArrayList<QuestionItem> tmpRows = new ArrayList<QuestionItem>();
					Random rad = new Random();
					int size = 0;
					if (_isInclusion) {
						size = cRows.size();
					} else {
						size = q.getRowItemArr().size();
					}
					for (int i = 0; i < size; i++) {
						/**
						 * 随机产生数组的下标值
						 */
						int index = rad.nextInt(cRows.size());
						/**
						 * 取得随机产生的选项对象item
						 */
						tmpRows.add(cRows.get(index));
						/**
						 * 在暂存数组中移除随机产生的选项对象item
						 */
						cRows.remove(index);
					}
					cRows.addAll(tmpRows);
					tmpRows.clear();
				}

				/**
				 * 假如题干的宽度大于或等于屏幕宽度的3/4
				 */
				boolean _isBeyond = (screenWidth * 2 / 3 - 20) <= cColmns
						.size() * 100;

				/**
				 * 遍历每一行
				 */
				for (int r = 0; r < cRows.size() + 1; r++) {
					QuestionItem rowItem = null;
					if (0 != r) {
						rowItem = cRows.get(r - 1);
					}

					/**
					 * 遍历每一列
					 */
					RadioGroup ll = new RadioGroup(
							NativeModeNoPageActivity.this);
					ll.setGravity(Gravity.CENTER_VERTICAL);
					for (int c = 0; c < cColmns.size() + 1; c++) {
						QuestionItem colItem = null;

						if (0 != c) {
							colItem = cColmns.get(c - 1);
						}

						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						TextView tvTb = new TextView(
								NativeModeNoPageActivity.this);
						tvTb.setLayoutParams(WRAP_WRAP);
						tvTb.setGravity(Gravity.FILL);
						tvTb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvTb.setWidth(100);
						tvTb.setPadding(5, 2, 2, 2);
						// tvTb.setHeight(100);
						// tvTb.setBackgroundColor(Color.LTGRAY);

						if (0 == r) {// 如过是第一行, 则打印出每一列的值
							// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
							ll.setBackgroundColor(Color.LTGRAY);
							if (0 == c) {// 打印表头
								tvTb.setText(" ");
								tvTb.setWidth(screenWidth / 3);
								tvTb.setPadding(2, 2, 2, 2);
							} else {// 打印每列的
								tvTb.setTextColor(Color.BLACK);
								tvTb.setPadding(2, 2, 2, 2);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									tvTb.setWidth((screenWidth * 2 / 3 - 20)
											/ cColmns.size());
								}
								String t = colItem.itemText;

								final float Width = (screenWidth * 2 / 3 - 20)
										/ cColmns.size();
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(t, imgGetter,
										null);
								tvTb.setText(fromHtml);
							}
							// ***********************************样式处理**************************//
							ll.addView(tvTb, ll.getChildCount());
						} else {
							if (r % 2 == 0)
								ll.setBackgroundColor(Color
										.parseColor("#10000050"));
							else
								ll.setBackgroundColor(Color.TRANSPARENT);
							if (0 == c) {
								tvTb.setTextColor(Color.BLACK);
								tvTb.setWidth(screenWidth / 3);
								tvTb.setPadding(2, 2, 2, 2);
								tvTb.setBackgroundColor(Color.TRANSPARENT);
								// tvTb.setText(cRows.get(r - 1).itemText);
								// tvTb.setText(rowItem.itemText);
								String t = rowItem.itemText;

								if (!Util.isEmpty(t)) {
									final float Width = (screenWidth * 2 / 3 - 20)
											/ cColmns.size();
									// 更改的样式
									ImageGetter imgGetter = new Html.ImageGetter() {
										public Drawable getDrawable(
												String source) {
											Drawable drawable = null;
											String name = NativeModeNoPageActivity.this
													.getFilesDir()
													+ File.separator
													+ "survey"
													+ File.separator
													+ feed.getSurveyId()
													+ File.separator + source;
											// System.out.println("name:" +
											// name);
											drawable = Drawable
													.createFromPath(name);
											Bitmap image = BitmapFactory
													.decodeFile(name);
											if (image != null) {
												float tWidth = image.getWidth();
												float tHeight = image
														.getHeight();
												if (tWidth > Width) {
													tHeight = Width / tWidth
															* tHeight;
													tWidth = Width;
												}
												drawable.setBounds(0, 0,
														(int) tWidth,
														(int) tHeight);
												return drawable;
											} else {
												return null;
											}
										}
									};
									Spanned fromHtml = Html.fromHtml(t,
											imgGetter, null);
									tvTb.setText(fromHtml);
								}
								// ***********************************样式处理**************************//
								ll.addView(tvTb, ll.getChildCount());
							} else {// 打印单选按钮
								// QuestionItem rowItem = cRows.get(r - 1);
								if (null == rowItem
										|| (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
									// 空其他项不打印CheckBox
									continue;
								}
								// QuestionItem colItem = cColmns.get(c - 1);
								CheckBox check = new CheckBox(
										NativeModeNoPageActivity.this);

								String name = Util.GetAnswerName(q, null,
										rowItem.itemValue, colItem.itemValue,
										false, false);

								int value = q.getColItemArr().size()
										* rowItem.itemValue + colItem.itemValue;
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setRow(rowItem.itemValue);
								am.setCol(colItem.itemValue);
								am.setAnswerValue(String.valueOf(value));
								check.setTag(am);
								Log.i("setAnswerValue", am.getAnswerValue());

								ll.addView(check, ll.getChildCount());
								if (!Util.isEmpty(amList)) {
									for (AnswerMap tam : amList) {
										if (name.endsWith(tam.getAnswerName())
												&& am.getAnswerValue().equals(
														tam.getAnswerValue())) {
											check.setChecked(true);
										}
									}
								}
								check.setPadding(2, 2, 2, 2);
								check.setGravity(Gravity.FILL);
								check.setBackgroundResource(R.drawable.small_checkbox_background);
								check.setButtonDrawable(R.drawable.small_checkbox);
								check.setWidth(100);
								if (_isBeyond) {
									/**
									 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
									 */
								} else {
									check.setWidth((screenWidth * 2 / 3 - 20)
											/ cColmns.size());
								}
								// check.setHeight(100);

								/**
								 * 复选矩阵排他项
								 */
								Log.i("CheckListenerMatrix", "rowItem="
										+ rowItem + "mTempRows=" + mTempRows
										+ "position=" + position);
								check.setOnClickListener(new CheckListenerMatrix(
										check, rowItem, ll, vs, q, mTempRows,
										position));
								vs.add(check);
								// 单选矩阵选项排斥
								if ("true".equals(colItem.exclude)
										&& !Util.isEmpty(colItem.excludeIn)) {
									String excludeIn = colItem.excludeIn;
									String[] excludeValue = excludeIn
											.split(",");
									if (excludeValue.length > 0) {
										for (int i = 0; i < excludeValue.length; i++) {
											if (Integer
													.parseInt(excludeValue[i]
															.trim()) == (rowItem
													.getItemValue())) {
												check.setChecked(false);
												check.setVisibility(View.INVISIBLE);
											}
										}
									}
								}
							}
						}

					}
					bodyView.addView(ll, bodyView.getChildCount());
				}
			}

			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下来列表
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			if (true) {
				/**
				 * 题型的横向、纵向摆放
				 */
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					// System.out.println("横向");
					bodyView.setOrientation(LinearLayout.HORIZONTAL);
				} else {
					// System.out.println("纵向");
					bodyView.setOrientation(LinearLayout.VERTICAL);
				}

				Spinner sp = new Spinner(NativeModeNoPageActivity.this);
				sp.setLayoutParams(FILL_WRAP);

				ArrayList<QuestionItem> columns = q.getColItemArr();
				HashMap<String, Integer> tvMap = new HashMap<String, Integer>();
				HashMap<Integer, Integer> ivMap = new HashMap<Integer, Integer>();
				if (!Util.isEmpty(columns)) {
					ArrayAdapter<String> aa = new ArrayAdapter<String>(
							NativeModeNoPageActivity.this,
							R.layout.simple_spinner_adapter);
					aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
					int deftPosition = 0;
					int firstPos = 0;
					if (1 == q.ignoreFirstItem) {
						aa.add(q.firstText);
						firstPos++;
					}
					for (int i = firstPos; i < columns.size() + firstPos; i++) {
						QuestionItem item = columns.get(i - firstPos);
						if (1 == item.deft) {
							deftPosition = i;
						}
						aa.add(item.itemText);
						tvMap.put(i + "_" + item.itemText, item.itemValue);
						ivMap.put(item.itemValue, i);
					}
					sp.setAdapter(aa);
					sp.setSelection(deftPosition);
					if (!Util.isEmpty(amList)) {
						String name = Util.GetAnswerName(q, null, 0, 0, false,
								false);
						for (AnswerMap am : amList) {
							if (name.equals(am.getAnswerName())) {
								int value = Integer.parseInt(am
										.getAnswerValue());
								int pos = ivMap.get(value);
								sp.setSelection(pos);
							}
						}
					}
				}
				bodyView.addView(sp, bodyView.getChildCount());
				vs.add(sp);
				// sp.setOnItemSelectedListener();
				sp.setOnItemSelectedListener(new SpSelectedListener(q, vs,
						tvMap, mTempRows, position));
				ivMap.clear();
				ivMap = null;
			}
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框
			if (true) {
				// ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
				// if (null != tempAnswer) {
				// amList = tempAnswer.getAnswerMapArr();
				// }
				freeTextBox(amList, bodyView, tvCaption, q, vs, mTempRows,
						position);
			}

			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);
			if (true) {
				/**
				 * 题型的横向、纵向摆放
				 */
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
					// System.out.println("横向");
					bodyView.setOrientation(LinearLayout.HORIZONTAL);
				} else {
					// System.out.println("纵向");
					bodyView.setOrientation(LinearLayout.VERTICAL);
				}
				ArrayList<QuestionItem> tbRows = q.getRowItemArr();
				int textAreaRows = q.textAreaRows;// 一共几行
				if (!Util.isEmpty(tbRows)) {
					for (int i = 0; i < tbRows.size(); i++) {
						QuestionItem item = tbRows.get(i);
						item.itemValue = i;
						EditText et = new EditText(
								NativeModeNoPageActivity.this);
						et.setBackgroundResource(R.drawable.bg_edittext);
						et.setTag(item);
						et.setLayoutParams(FILL_WRAP);
						et.setWidth((int) (screenWidth / 1.5));
						et.setHeight(textAreaRows * dis.getHeight() / 20);
						// 问卷字号动态设置
						et.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						et.setGravity(Gravity.TOP);
						// System.out.println("item.itemText=" + item.itemText);
						if (!Util.isEmpty(item.itemText)) {
							TextView tvCap = new TextView(
									NativeModeNoPageActivity.this);
							tvCap.setLayoutParams(FILL_WRAP);
							tvCap.setTextColor(Color.BLACK);
							tvCap.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvCap.setText(item.itemText);
							bodyView.addView(tvCap, bodyView.getChildCount());
						}
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						bodyView.addView(et, bodyView.getChildCount());
						vs.add(et);
						et.addTextChangedListener(new EditChangedListener(q,
								vs, mTempRows, position));// 给EditText
						// 添加监听
					}
				}
				// 赋给全局变量在点击单题录音时使用;
				vsRecord.put(q.qIndex, vs);
				// mTempRowsRecord = mTempRows;
			}

			break;

		case Cnt.TYPE_MEDIA:
			// 预留多为体题型
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}
			if (!Util.isEmpty(q.qMediaSrc)) {
				if (q.qMediaSrc.toLowerCase().endsWith(".png")//
						|| q.qMediaSrc.toLowerCase().endsWith(".jpg") //
						|| q.qMediaSrc.toLowerCase().endsWith(".bmp")//
						|| q.qMediaSrc.toLowerCase().endsWith(".gif")) {//
					ImageView iv = new ImageView(NativeModeNoPageActivity.this);
					iv.setLayoutParams(WRAP_WRAP);
					String filename = Util.getMediaPath(
							NativeModeNoPageActivity.this, q.surveyId,
							q.qMediaSrc);
					try {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						File file = new File(filename);
						// 数字越大读出的图片占用的heap越小 不然总是溢出
						long len = file.length();
						if (1048576 * 3 > len) { // 显示图片时的压缩 小于1024k, 即1M的
							opts.inSampleSize = 2;
						} else if (1048576 * 8 > len && len >= 1048576 * 3) {
							/**
							 * 大于1M的
							 */
							opts.inSampleSize = 6;
						} else {
							opts.inSampleSize = 10;
						}
						Bitmap oldBitmap = BitmapFactory.decodeStream(
								new FileInputStream(file), null, opts);

						float width = oldBitmap.getWidth();
						float height = oldBitmap.getHeight();

						Bitmap newBitmap = null;
						if (width > screenWidth || height > screenHeight) {
							float scaleRate = width > height ? screenWidth
									/ width : screenHeight / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							// 得到新的图片
							newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
									(int) width, (int) height, matrix, true);
						} else {
							float scaleRate = width > height ? screenWidth
									/ width : screenHeight / height;
							Matrix matrix = new Matrix();
							matrix.postScale(scaleRate, scaleRate);
							newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
									(int) width, (int) height, matrix, true);
						}
						// if (1048576 > len) { // 小于1024k
						// opts.inSampleSize = 3;
						// } else {
						// opts.inSampleSize = 6;
						// }

						bd = new BitmapDrawable(newBitmap);
						iv.setImageDrawable(bd);
						iv.setOnLongClickListener(new ImageLongClickListener(
								filename));
					} catch (Exception e) {
						e.printStackTrace();
					}
					bodyView.addView(iv, bodyView.getChildCount());
				} else if (q.qMediaSrc.toLowerCase().endsWith(".mp3")//
						|| q.qMediaSrc.toLowerCase().endsWith(".avi")//
						|| q.qMediaSrc.toLowerCase().endsWith(".wmv")//
						|| q.qMediaSrc.toLowerCase().endsWith(".mp4")//
						|| q.qMediaSrc.toLowerCase().endsWith(".flv")//
						|| q.qMediaSrc.toLowerCase().endsWith(".wma")//
						|| q.qMediaSrc.toLowerCase().endsWith(".swf")//
						|| q.qMediaSrc.toLowerCase().endsWith(".3gp")//
						|| q.qMediaSrc.toLowerCase().endsWith(".mov")) {
					ImageView iv = new ImageView(NativeModeNoPageActivity.this);
					iv.setPadding(60, 60, 60, 60);
					iv.setBackgroundResource(R.drawable.shape_bg);
					iv.setLayoutParams(WRAP_WRAP);
					iv.setImageResource(R.drawable.play);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(
									NativeModeNoPageActivity.this,
									VideoPlayerActivity.class);
							it.putExtra("path",
									Util.getMediaPath(q.surveyId, q.qMediaSrc));
							startActivity(it);
						}
					});
					bodyView.addView(iv, bodyView.getChildCount());
					// VideoView vv = new
					// VideoView(NativeModeNoPageActivity.this);
					// vv.setLayoutParams(new
					// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					// vv.setMediaController(new MediaController(this));
					// vv.setVideoPath(Util.getMediaPath(q.surveyId,
					// q.qMediaSrc));
					// vv.start();
					// vv.requestFocus();
					// bodyView.addView(vv, bodyView.getChildCount());

				}
			}
			break;
		}
		// 追加说明方法继续上一页下一页终止提示
		isPrompt = false;
		vss.add(vs);
		return convertView;
		

	}

	/**
	 * 设置 二级联动 城市 适配器
	 * 
	 * @param pos
	 */
	private void setCityAdapter(int pos, QuestionItem item) {

		areaListTemp = new ArrayList<String>();
		areaListTemp = ThreeLeverUtil.getCityPosList(area, city, pos);
		if (areaListTemp.size() == 0) {
			areaListTemp.add("");
		}

		cityAdapter = new ArrayAdapter<String>(NativeModeNoPageActivity.this,
				R.layout.simple_spinner_adapter, areaListTemp);
		cityAdapter
				.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
		citySpinner.setAdapter(cityAdapter);
		citySpinner.setTag(item);
	}

	/**
	 * 设置 三级 联动 的 关联 情况
	 */

	private void setCountryAdapter(int pos, QuestionItem item) {

		wayListTemp = new ArrayList<String>();
		wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, pos);
		if (wayListTemp.size() == 0) {
			wayListTemp.add("");
		}

		countryAdapter = new ArrayAdapter<String>(
				NativeModeNoPageActivity.this, R.layout.simple_spinner_adapter,
				wayListTemp);
		countryAdapter
				.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
		countrySpinner.setAdapter(countryAdapter);
		countrySpinner.setTag(item);
	}

	/**
	 * 维码扫描用,给指定的值赋值
	 */
	private EditText etSave;

	class ScanningListener implements OnClickListener {

		private EditText innerEt;

		public ScanningListener(EditText editText) {
			innerEt = editText;
		}

		@Override
		public void onClick(View v) {
			etSave = innerEt;
			Intent openCameraIntent = new Intent(NativeModeNoPageActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);

		}

	}

	/**
	 * 维码扫描用
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etSave.setText(scanResult);
		}
		// 数据字典
		else if (resultCode == 2) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			etData.setText(result);
			etData.setFocusable(true);
			etData.setFocusableInTouchMode(true);
			etData.requestFocus();
		}
		// 录像返回 重新启动录音
		else if (resultCode == 101) {
			/**
			 * 继续访问
			 */
			// System.out.println("继续访问FileName=" + feed.getName());
			feed.setStartTime(System.currentTimeMillis());
			/**
			 * recordView、cameraView 根据问卷的要求设置其显示或隐藏
			 */
			if (null != feed.getSurvey()) {
				if (1 == feed.getSurvey().globalRecord) {
					// 注释录音
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("feed", feed);
					params.put("content", NativeModeNoPageActivity.this);
					params.put("isClicked", isClicked);
					params.put("q", qs.get(0));
					openGlobalRecord(params);
					// MainService.newTask(new Task(TaskType.TS_RECORED,
					// params));
				}
			}
		}
		switch (requestCode) {
		case 2:
			if (resultCode == 3) {// 预览后返回修改
				if (pageNum == aryQs.size()) {
					pageNum--;
				}
				setTopClick(true);
				handler.sendEmptyMessage(MSG_REDIRECT_BACK);
			} else if (resultCode == 4) {// 预览后确认提交
				new XmlTask(MSG_WRITE, "1").execute();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 监控用的写异步线程
	 */
	private final class MonitorTask extends
			AsyncTask<Void, Integer, InputStream> {

		private int optType;// 是上一页还是下一页 next为2 opt为1
		private int realIndex;
		private Question q;

		public MonitorTask(int opt, int realIndex, Question q) {
			this.optType = opt;
			this.realIndex = realIndex;
			this.q = q;
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			int recodeQIndex = -1;
			int recodeTempRealIndex = -1;
			ArrayList<AnswerMap> recodeAmList = new ArrayList<AnswerMap>();
			if (recodeTempIndex != -1) {
				System.out.println("recodeTempIndex:" + recodeTempIndex);
				Question recodeQuestion = qs.get(recodeTempIndex);
				recodeQIndex = recodeQuestion.qIndex;
				Answer recodeAnswer = ma.dbService.getAnswer(feed.getUuid(),
						recodeQIndex + "");
				if (null != recodeAnswer) {
					recodeAmList = recodeAnswer.getAnswerMapArr();
				}
				// 更改的
				recodeTempRealIndex = ma.dbService.getQuestionIndex(
						feed.getSurveyId(), recodeTempIndex);
			}
			String monitorPath = writeMonitorXml(recodeTempRealIndex,
					recodeAmList, optType, q);
			// 上传 地址不为空就上传
			InputStream is = null;
			if (!Util.isEmpty(monitorPath)) {
				String name = monitorPath.substring(
						monitorPath.lastIndexOf(File.separator) + 1,
						monitorPath.length());
				String path = monitorPath.substring(0,
						monitorPath.lastIndexOf(File.separator));
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("surveyId", feed.getSurveyId());
				paramsMap.put(Cnt.USER_ID, URLEncoder.encode(ma.userId));
				paramsMap.put("uuid", feed.getUuid());
				paramsMap.put("index", recodeTempRealIndex + "");
				paramsMap.put(Cnt.USER_PWD, ma.userPwd);
				FileUpLoad fupLoad = new FileUpLoad();
				is = fupLoad.upLoad(Cnt.MONITOR_URL, path, name, paramsMap);
			}
			if (is == null) {
				// System.out.println("is空");
				return null;
			} else {
				// System.out.println("is不为空");
				return is;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(InputStream is) {
			String state = "0";
			recodeTempIndex = realIndex;
			if (is != null) {
				state = Util.resolvData(is);
			}
			if ("100".equals(state)) {
				// System.out.println("成功");
			}
			super.onPostExecute(is);
		}

	}

	/**
	 * 监控用的写xml
	 * 
	 * @param recodeQIndex
	 * @param recodeAmList
	 */
	private String writeMonitorXml(int recodeTempRealIndex,
			ArrayList<AnswerMap> recodeAmList, int optType, Question q) {
		String path = "";
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "monitor");
			serializer.attribute(null, "jumpIndex", q.qIndex + "");
			serializer.attribute(null, "index", recodeTempRealIndex + "");
			serializer.attribute(null, "uuid", feed.getUuid());
			if (MSG_NEXT == optType) {
				serializer.attribute(null, "opt", "2");
			} else if (3 == optType) {
				serializer.attribute(null, "opt", "3");
			} else if (4 == optType) {
				serializer.attribute(null, "opt", "4");
			} else {
				serializer.attribute(null, "opt", "1");
			}
			for (AnswerMap recodeAnswerMap : recodeAmList) {
				serializer.startTag(null, "answer");
				String recodeAnswerValue = recodeAnswerMap.getAnswerValue();
				String recodeAnswerName = recodeAnswerMap.getAnswerName();
				if (Util.isEmpty(recodeAnswerValue)) {
					recodeAnswerValue = "";
				}
				serializer.startTag(null, "name");
				serializer.text(recodeAnswerName);
				serializer.endTag(null, "name");
				serializer.startTag(null, "value");
				serializer.text(recodeAnswerValue);
				serializer.endTag(null, "value");
				serializer.endTag(null, "answer");
			}
			serializer.endTag(null, "monitor");
			serializer.endDocument();
			writer.flush();
			writer.close();
			path = Util.getMonitorPath(ma, feed.getSurveyId(), feed.getUuid(),
					q.qIndex + "", optType);
			File file = new File(path);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			OutputStream out = new FileOutputStream(path);
			OutputStreamWriter outw = new OutputStreamWriter(out);
			outw.write(writer.toString());
			outw.close();
			out.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	private ImageView ivRecord;
	boolean isRecording = false;// 单题录音是否开启

	class CstOnClickListener implements OnClickListener {

		private Question question;
		private int type;

		public CstOnClickListener(Question q, int type) {
			this.question = q;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			switch (type) {
			case 2:// 单题录音
				System.out.println("录音");
				if (isRecording) {
					int id2 = v.hashCode();
					int id1 = ivRecord.hashCode();
				}
				if (isRecording && ivRecord.hashCode() != v.hashCode()) {// 如果正在录音先关闭
					closeQRecord();
					ivRecord = null;
				}
				openQRecord(question.qIndex + "", (ImageView) v, question);
				break;
			// 单题签名
			// 单复选矩阵固定
			// case R.id.ivSign_new:
			// case R.id.ivSign:
			case 1:
				WritePadDialog writeTabletDialog = new WritePadDialog(
						NativeModeNoPageActivity.this, new DialogListener() {
							@Override
							public void refreshActivity(Object object) {
								// System.out.println("刷新界面");
								Bitmap mSignBitmap = (Bitmap) object;
								UploadFeed photo = new UploadFeed();
								photo.setUserId(feed.getUserId());
								photo.setSurveyId(feed.getSurveyId());
								photo.setUuid(feed.getUuid());
								if (ma.cfg.getBoolean("save_inner", false)) {
									/**
									 * 存放在内部
									 */
									photo.setPath(Util.getRecordInnerPath(
											NativeModeNoPageActivity.this,
											feed.getSurveyId()));
									photo.setIsSaveInner(0);
								} else {
									/**
									 * 存放在外部
									 */
									photo.setPath(Util.getRecordPath(feed
											.getSurveyId()));
									photo.setIsSaveInner(1);
								}
								// photo.setPath(Util.getRecordInnerPath(NativeModeNoPageActivity.this,
								// feed.getSurveyId()));
								// 增加pid 命名规则
								photo.setName(Util.getSignName(
										getQid(question), feed.getUserId(),
										feed.getSurveyId(), Cnt.FILE_TYPE_PNG,
										feed.getUuid(), question.qIndex + "",
										feed.getPid(),
										feed.getParametersContent(),
										(question.qOrder + 1) + ""));
								File signFile = Util.createFile(mSignBitmap,
										photo.getPath(), photo.getName());
								photo.setStartTime(System.currentTimeMillis());
								photo.setRegTime(System.currentTimeMillis());
								photo.setSize(signFile.length());
								// 缩略图
								if (signFile.length() > 0) {
									// 缩略图
									float width = mSignBitmap.getWidth();
									float height = mSignBitmap.getHeight();
									Bitmap newBitmap = null;
									if (width > 90 || height > 90) {
										float scaleRate = width > height ? 90 / width
												: 90 / height;
										Matrix matrix = new Matrix();
										matrix.postScale(scaleRate, scaleRate);
										// 得到新的图片
										newBitmap = Bitmap.createBitmap(
												mSignBitmap, 0, 0, (int) width,
												(int) height, matrix, true);
									} else {
										Matrix matrix = new Matrix();
										matrix.postScale(1, 1);
										newBitmap = Bitmap.createBitmap(
												mSignBitmap, 0, 0, (int) width,
												(int) height, matrix, true);
									}

									UploadFeed uf = (UploadFeed) photo.clone();
									// uf为空
									String name = uf.getName();
									int index = name.lastIndexOf(".");
									String name_q = name.substring(0, index);
									String type = name.substring(index + 1);
									name_q += "_thumbnail";
									uf.setName(name_q + "." + type);
									// String[] split1 = name.split("[.]");
									// String name_q = split1[0];
									// name_q += "_thumbnail";
									// uf.setName(name_q + "." + split1[1]);
									File newsignFile = Util.createFile(
											newBitmap, uf.getPath(),
											uf.getName());
									uf.setSize(newsignFile.length());

									ma.dbService.addPhoto(photo, true, "");
									ma.dbService.addPhoto(uf, true, "");
								}

								/*
								 * BitmapFactory.Options options = new
								 * BitmapFactory.Options(); options.inSampleSize
								 * = 15; options.inTempStorage = new byte[5 *
								 * 1024]; Bitmap zoombm =
								 * BitmapFactory.decodeFile(signPath, options);
								 */

							}
						});
				writeTabletDialog.show();
				break;
			// 单复选矩阵固定
			// case R.id.qtitle_tv_new:
			// case R.id.qtitle_tv:
			case 0:
				Intent intent = new Intent(NativeModeNoPageActivity.this,
						PhotoActivity.class);
				Bundle bundle = new Bundle();
				UploadFeed photo = new UploadFeed();
				photo.setUserId(feed.getUserId());
				photo.setSurveyId(feed.getSurveyId());
				photo.setUuid(feed.getUuid());
				if (ma.cfg.getBoolean("save_inner", false)) {
					/**
					 * 存放在内部
					 */
					photo.setPath(Util.getRecordInnerPath(
							NativeModeNoPageActivity.this, feed.getSurveyId()));
					photo.setIsSaveInner(0);
				} else {
					/**
					 * 存放在外部
					 */
					photo.setPath(Util.getRecordPath(feed.getSurveyId()));
					photo.setIsSaveInner(1);
				}
				// photo.setPath(Util.getRecordInnerPath(NativeModeNoPageActivity.this,
				// feed.getSurveyId()));
				// 增加pid 命名规则
				photo.setName(Util.getRecordName(getQid(question),
						feed.getUserId(), feed.getSurveyId(),
						Cnt.FILE_TYPE_PNG, feed.getUuid(),
						question.qIndex + "", feed.getPid(),
						feed.getParametersContent(), (question.qOrder + 1) + ""));
				photo.setStartTime(System.currentTimeMillis());
				bundle.putSerializable("photo", photo);
				bundle.putSerializable("question", question);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;

			default:
				break;
			}
		}

	}

	private String getQid(Question qu) {
		String qid;
		if (!Util.isEmpty(qu.qid)) {// 取题号
			qid = qu.qid;
		} else {
			qid = getResources().getString(R.string.question_order, qu.qOrder);

		}
		return qid;
	}

	/*
	 * private void initView() { tvTitle.setText("");
	 * tvCaption.setTextColor(Color.GRAY);// 统计局专有页面 tvCaption.setText(""); if
	 * (View.VISIBLE == llCaption.getVisibility()) { llCaption.removeAllViews();
	 * llCaption.setVisibility(View.GONE); }
	 * 
	 * tvComment.setTextColor(Color.GRAY);// 统计局专有页面 tvComment.setText("");
	 * 
	 * if (View.VISIBLE == llComment.getVisibility()) {
	 * llComment.removeAllViews(); llComment.setVisibility(View.GONE); } }
	 */

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshView();
	}

	/**
	 * 下拉题监听
	 * 
	 * @author Administrator
	 * 
	 */
	class SpSelectedListener implements OnItemSelectedListener {
		private Question q;
		private ArrayList<View> vs;
		private HashMap<String, Integer> tvMap;
		private int count = 0;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public SpSelectedListener(Question q, ArrayList<View> vs,
				HashMap<String, Integer> tvMap,
				ArrayList<QuestionItem> mTempRows, int position) {
			// TODO Auto-generated constructor stub
			this.q = q;
			this.vs = vs;
			this.tvMap = tvMap;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// Log.i("@@@", "选中下拉题获取答案");
			int success = getQuestionAnswer( false, q, vs, tvMap,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success && 1 == count) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = this.position;
				handler.sendMessage(msg);
			}
			count = 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 为单选添加单选按钮
	 * 
	 * @param amList
	 * @param arrLayout
	 * @param item
	 * @param rbLL
	 * @param otherItems
	 * @param isDumbOk
	 * @param operType
	 * @param lineNum
	 * @param vs
	 * @param bodyView
	 * @param tempAnswer
	 * @param mTempRows
	 * @param q
	 * @param i
	 * @return
	 */
	private HashMap<String, Object> addRb(final float rowsOneWidth,
			ArrayList<AnswerMap> amList, ArrayList<LinearLayout> arrLayout,
			QuestionItem item, LinearLayout rbA,
			ArrayList<QuestionItem> otherItems, boolean isDumbOk, int lineNum,
			ArrayList<View> vs, LinearLayout bodyView, Answer tempAnswer,
			ArrayList<QuestionItem> mTempRows, Question q, int position) {
		HashMap<String, Object> mapAddRb = new HashMap<String, Object>();
		// 生成一项选择框布局
		LinearLayout rbLL = new LinearLayout(NativeModeNoPageActivity.this);
		rbLL.setOrientation(LinearLayout.HORIZONTAL);
		// 将选项框加入整体布局中
		rbA.addView(rbLL, rbA.getChildCount());
		if (!Util.isEmpty(item.popUp)) {
			/**
			 * 生成弹窗说明小图
			 */
			RadioButton imTv = new RadioButton(NativeModeNoPageActivity.this);
			imTv.setOnClickListener(new ImTvListenner(item.popUp));
			imTv.setButtonDrawable(R.drawable.tip);
			rbLL.addView(imTv, rbLL.getChildCount());
		}

		/**
		 * 生成一个单选按钮
		 */
		RadioButton rb = new RadioButton(NativeModeNoPageActivity.this);
		rb.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		/**
		 * 选项的追加说明 ,有追加说明的情况
		 */
		TextView tvItemCap = new TextView(NativeModeNoPageActivity.this);
		if (!Util.isEmpty(item.caption)) {
			// 单选追加说明方法
			if (1 == item.caption_check) {
				rb.setOnCheckedChangeListener(new MyRadioButtonClick(
						item.caption));
			} else {
				// 追加说明新布局原FILL_WRAP
				tvItemCap.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
				tvItemCap
						.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);

				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = NativeModeNoPageActivity.this
								.getFilesDir()
								+ File.separator
								+ "survey"
								+ File.separator
								+ feed.getSurveyId()
								+ File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							float tWidth = image.getWidth();
							float tHeight = image.getHeight();
							if (tWidth > rowsOneWidth) {
								tHeight = rowsOneWidth / tWidth * tHeight;
								tWidth = rowsOneWidth;
							}
							drawable.setBounds(0, 0, (int) tWidth,
									(int) tHeight);
							return drawable;
						} else {
							return null;
						}
					}
				};
				Spanned fromHtml = Html.fromHtml(item.caption, imgGetter, null);
				tvItemCap.setText(fromHtml);
				// 更改的样式
			}
		}

		// 单选百分比开始 原单选 现单选
		// rb.setLayoutParams(WRAP_WRAP);
		if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
			rbLL.setLayoutParams(new LinearLayout.LayoutParams(
					(int) rowsOneWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
			// System.out.println("rowsOneWidth"+rowsOneWidth);
		} else {
			rbLL.setLayoutParams(new LinearLayout.LayoutParams(maxCWidth,
					LinearLayout.LayoutParams.WRAP_CONTENT));
		}
		// 百分比结束
		String idStr = q.qIndex + "_" + item.itemValue;
		rb.setId(idStr.hashCode());
		// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
		rb.setButtonDrawable(R.drawable.small_radiobutton);
		Drawable drawable = getResources().getDrawable(
				R.drawable.small_radiobutton_on);
		int leftwidth = drawable.getIntrinsicWidth();
		rb.setTextColor(Color.BLACK);
		rb.setGravity(Gravity.CENTER_VERTICAL);
		rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		// 更改的样式
		final float imgWidth = rowsOneWidth - leftwidth;
		ImageGetter imgGetter = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				String name = NativeModeNoPageActivity.this.getFilesDir()
						+ File.separator + "survey" + File.separator
						+ feed.getSurveyId() + File.separator + source;
				// System.out.println("name:" + name);
				drawable = Drawable.createFromPath(name);
				Bitmap image = BitmapFactory.decodeFile(name);
				if (image != null) {
					float tWidth = image.getWidth();
					float tHeight = image.getHeight();
					if (tWidth > imgWidth) {
						tHeight = imgWidth / tWidth * tHeight;
						tWidth = imgWidth;
					}
					drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
					return drawable;
				} else {
					return null;
				}
			}
		};
		Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
		rb.setText(fromHtml);
		if (null != item.padding && item.padding == 1) {
			String radomed = getResources().getString(R.string.option_top);
			SpannableString ss = new SpannableString(radomed);
			ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(),
					Spanned.SPAN_COMPOSING);
			rb.append(ss);
		}
		// 更改的样式
		/**
		 * 判断是否是哑题,然后逻辑判断。 新哑题
		 */
		// TODO
		// 假如是哑题 开始判断
		if (1 == q.qDumbFlag) {
			item.isDumbOk = false;
			HashMap<String, Object> map = setDumbRb(item, rb, isDumbOk,
					MSG_NEXT, tempAnswer, 0);
			isDumbOk = (Boolean) map.get("isDumbOk");
			item = (QuestionItem) map.get("item");
		}
		/**
		 * 新哑题 判断哑题结束
		 */
		String name = Util.GetAnswerName(q, item, 0, 0, false, false);
		if (1 == q.qPreSelect) {
			// 假如没有哑题，就预设，
			if (1 == item.deft) {
				if (Util.isEmpty(amList)) {
					rb.setChecked(true);
					item.isCheck = true;
					rb.setTag(item);
				} else {
					for (AnswerMap am : amList) {
						if (name.equals(am.getAnswerName().trim())
								&& String.valueOf(item.itemValue).trim()
										.equals(am.getAnswerValue().trim())) {
							// System.out.println("ddddddddd");
							rb.setChecked(true);
							item.isCheck = true;
							rb.setTag(item);
							break;
						}
					}
				}
			} else {
				// System.out.println("不是预选项");
				if (!Util.isEmpty(amList)) {// 题目有答案
					// System.out.println("题目有答案");
					for (AnswerMap am : amList) {
						if (name.equals(am.getAnswerName().trim())
								&& String.valueOf(item.itemValue).trim()
										.equals(am.getAnswerValue().trim())) {
							// System.out.println("ddddddddd");
							rb.setChecked(true);
							item.isCheck = true;
							rb.setTag(item);
							break;
						}
					}
				}
			}
		} else {
			// if (1 == q.qDumbFlag) {
			// } else {
			// System.out.println("不是预选项");
			if (!Util.isEmpty(amList)) {// 题目有答案
				// System.out.println("题目有答案");
				for (AnswerMap am : amList) {
					if (name.equals(am.getAnswerName().trim())
							&& String.valueOf(item.itemValue).trim()
									.equals(am.getAnswerValue().trim())) {
						// System.out.println("ddddddddd");
						rb.setChecked(true);
						item.isCheck = true;
						rb.setTag(item);
						break;
					}
				}
			}
			// }
		}
		// 单选自动下一页
		rb.setOnCheckedChangeListener(new RadioListenner(idStr.hashCode(), 0,
				0, item, 1, vs, q, mTempRows, position));
		// if (Util.isEmpty(q.qSiteOption)) {
		/**
		 * 假如选项说明不为空
		 */
		if (!Util.isEmpty(item.caption)) {
			if (item.isChild) {// item为子项
				// 追加说明方法
				if (1 == item.caption_check) {

				} else {
					rbA.addView(tvItemCap, rbA.getChildCount() - 1);
				}
			} else {// item为正常选项
				// 追加说明方法
				if (1 == item.caption_check) {

				} else {
					if (0 < arrLayout.size()) {
						if (1 != lineNum) {
							lineNum = 1;
							LinearLayout rbM = new LinearLayout(
									NativeModeNoPageActivity.this);
							rbM.setOrientation(LinearLayout.HORIZONTAL);
							bodyView.addView(rbM, bodyView.getChildCount());
							arrLayout.add(rbM);
						}
						bodyView.addView(tvItemCap,
								bodyView.getChildCount() - 1);
					} else {
						bodyView.addView(tvItemCap, bodyView.getChildCount());
					}

				}
			}
		}
		rbLL.addView(rb, rbLL.getChildCount());
		// 将没有其他项的单选按钮加入集合中
		vs.add(rb);
		// 配置型隐藏选项
		if (setHideRb(item, q)) {
			tvItemCap.setVisibility(View.GONE);
			rbA.setVisibility(View.GONE);
			if (rb.isChecked()) {// 有答案就清空，并保存
				rb.setChecked(false);
				rb.setTag(null);
				int success = getQuestionAnswer( false, q, vs, null,
						mTempRows, false);
			}
		}
		mapAddRb.put("lineNum", lineNum);
		mapAddRb.put("isDumbOk", isDumbOk);
		return mapAddRb;
	}

	private final class ImTvListenner implements OnClickListener {
		String strPopup = "";

		public ImTvListenner(String popUp) {
			// TODO Auto-generated constructor stub
			strPopup = popUp;
		}

		@Override
		public void onClick(View v) {
			System.out.println("弹窗字符串：" + strPopup);
			System.out.println("弹窗宽度：" + popUpWidth);
			vPopupLL.setVisibility(View.VISIBLE);
			vPopupTv.setVisibility(View.VISIBLE);
			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = NativeModeNoPageActivity.this.getFilesDir()
							+ File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					// System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						float tWidth = image.getWidth();
						float tHeight = image.getHeight();
						tHeight = popUpWidth / tWidth * tHeight;
						tWidth = popUpWidth;
						drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(strPopup, imgGetter, null);
			vPopupTv.setText(fromHtml);
		}
	}

	/**
	 * 单选监听
	 * 
	 * @author Administrator
	 * 
	 */
	private final class RadioListenner implements OnCheckedChangeListener {
		private int rId;// i=1表示是带有其他项的, i=0表示单纯的RadioButton
		private int o;
		private int eId;// 其他项的id号
		private Question q;
		private QuestionItem item;
		// 单选自动下一页
		private ArrayList<View> vs;
		private int flag;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public RadioListenner(int radioId, int isOther, int etId,
				QuestionItem i, int flag, ArrayList<View> vs, Question qustion,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.rId = radioId;
			this.o = isOther;
			this.eId = etId;
			this.item = i;
			this.flag = flag;
			this.vs = vs;
			this.q = qustion;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		public RadioListenner(int radioId, int isOther, int etId,
				QuestionItem i, ArrayList<View> vs, Question qustion,
				int position) {
			this.rId = radioId;
			this.o = isOther;
			this.eId = etId;
			this.q = qustion;
			this.item = i;
			flag = 0;
			this.vs = vs;
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// qAnswer.qIndex = q.qIndex;
				// qAnswer.qOrder = q.qOrder;
				// TODOqAnswer.userId
				// System.out.println("v_size=" + vs.size());
				// qAnswer.getAnswerMapArr().clear();
				for (View v : vs) {
					if (v instanceof RadioButton) {
						RadioButton rb = (RadioButton) v;
						if (rId != rb.getId()) {// 别人
							rb.setChecked(false);
							rb.setTag(null);
						} else {
							rb.setChecked(true);
							rb.setTag(item);
						}
					} else if (v instanceof EditText) {
						EditText et = (EditText) v;
						if (1 == o) {// 如果他自己是其他项
							if (eId == et.getId()) {// 自己的文本框
								et.setFocusable(true);
								et.setFocusableInTouchMode(true);
								et.setTag(item);
							} else {// 别人的文本框
								et.setTag(null);
								et.setText("");
								et.setFocusableInTouchMode(false);
								et.setFocusable(false);
							}
						} else {// 别人的其他项
							et.setTag(null);
							/**
							 * 清空里面的值
							 */
							et.setText("");
							/**
							 * 置为不可用
							 */
							et.setFocusable(false);
							et.setFocusableInTouchMode(false);
							// System.out.println("别人的文本框2");
						}
					}
				}
				// Log.i("@@@", "选中单选获取答案");
				int success = getQuestionAnswer( false, q, vs, null,
						mTempRows, false);
				if (Cnt.STATE_SUCCESS == success) {
					Message msg = Message.obtain();
					msg.what = REFRESH;
					msg.arg1 = position;
					handler.sendMessage(msg);
				}
			}
		}
	}

	/**
	 * 对话框图片控件点击监听器, 继承了对话框的点击时间,也就意味着有对话框点击事件类的所有功能
	 * 但在执行父类onClick方法之前,判断复选框的选中状态及重置为当前状态的反向状态
	 */
	private final class CheckImageClickListener extends CheckListener {

		public CheckImageClickListener(CheckBox check, EditText edit,
				QuestionItem item, ArrayList<View> vs, Question q,
				ArrayList<QuestionItem> mTempRows, int position) {
			super(check, edit, item, vs, q, mTempRows, position);
		}

		@Override
		public void onClick(View v) {
			/**
			 * 取当前状态的反向状态
			 */
			cb.setChecked(cb.isChecked() ? false : true);
			super.onClick(v);
		}

	}

	class CheckListener implements OnClickListener {

		CheckBox cb;
		private EditText et;
		private QuestionItem item;
		private ArrayList<View> vs;
		private Question q;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public CheckListener(CheckBox check, EditText edit, QuestionItem item,
				ArrayList<View> vs, Question q,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.cb = check;
			this.et = edit;
			this.item = item;
			this.vs = vs;
			this.q = q;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if ("true".equals(item.getExclude())) {// 有排斥的
				// System.out.println("排斥");
				for (View view : vs) {// for
					if (view instanceof CheckBox) {// check
						if (cb.isChecked()) {
							CheckBox checkBox = (CheckBox) view;
							QuestionItem qitem = (QuestionItem) checkBox
									.getTag();
							if (!Util.isEmpty(item.excludeIn)) {
								String excludeIn = item.excludeIn;
								System.out.println("excludeIn:" + excludeIn);
								if (!Util.isEmpty(excludeIn)) {
									String[] excludeValue = excludeIn
											.split(",");

									if (cb.getId() == checkBox.getId()) {// 是自己
										checkBox.setChecked(true);
										item.isCheck = true;
										checkBox.setTag(item);
									} else {
										for (int i = 0; i < excludeValue.length; i++) {
											if (Integer
													.parseInt(excludeValue[i]
															.trim()) == (qitem
													.getItemValue())) {
												checkBox.setChecked(false);
												qitem.isCheck = false;
												checkBox.setTag(qitem);
											}
										}
										if ("true".equals(qitem.exclude)
												&& Util.isEmpty(qitem.excludeIn)) {
											checkBox.setChecked(false);
											qitem.isCheck = false;
											checkBox.setTag(qitem);
										}
									}

								}
							} else {
								if (cb.getId() == checkBox.getId()) {// 是自己
									checkBox.setChecked(true);
									item.isCheck = true;
									checkBox.setTag(item);
								} else {
									checkBox.setChecked(false);
									qitem.isCheck = false;
									checkBox.setTag(qitem);
								}
							}
						}
					} else if (view instanceof EditText) {
						EditText vet = (EditText) view;
						QuestionItem qitem = (QuestionItem) vet.getTag();
						if (null != et && vet.getId() == et.getId()) {// 是自己的EditText
							if (cb.isChecked()) {
								/**
								 * 焦点有效
								 */
								vet.setFocusable(true);
								/**
								 * 触碰焦点有效
								 */
								vet.setFocusableInTouchMode(true);
								item.isCheck = true;
								vet.setTag(item);
							} else {
								vet.setText("");
								vet.setFocusable(false);
								vet.setFocusableInTouchMode(false);
								qitem.isCheck = false;
								vet.setTag(qitem);
							}
						} else {// 别人的EidtText
							vet.setText("");
							vet.setFocusable(false);
							vet.setFocusableInTouchMode(false);
							qitem.isCheck = false;
							vet.setTag(qitem);
						}
					}
				} // for
			} else {// 无排斥的
				if (null != et) {// 其他项
					if (cb.isChecked()) {
						/**
						 * CheckBox被选中了, 则CheckBox后面的EditText可是输入
						 */
						et.setFocusable(true);
						et.setFocusableInTouchMode(true);
						item.isCheck = true;
						cb.setTag(item);
						et.setTag(item);
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId()
										&& "true".equals(qi.getExclude())
										&& Util.isEmpty(qi.excludeIn)) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {
								QuestionItem qi = (QuestionItem) vv.getTag();
								if ("true".equals(qi.getExclude())) {
									((EditText) vv).setText("");
									((EditText) vv).setFocusable(false);
									((EditText) vv)
											.setFocusableInTouchMode(false);
								}
							}
						}
					} else {
						/**
						 * 假如CheckBox为非选中状态, 则其后面的EditText中的文本为空, 并且是不可以输入的
						 */
						et.setText("");
						et.setFocusable(false);
						et.setFocusableInTouchMode(false);
						item.isCheck = false;
						cb.setTag(item);
						et.setTag(item);
						// System.out.println("其他项取消选中");
					}
				} else {// 非其他项

					if (cb.isChecked()) {
						cb.setChecked(true);
						item.isCheck = true;
						cb.setTag(item);
						// System.out.println("非其他项选中");
						// for (QuestionItem qi:items) {
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId()
										&& "true".equals(qi.getExclude())
										&& Util.isEmpty(qi.excludeIn)) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {
								// 提示
								// ((EditText) vv).setText("");
								((EditText) vv).setFocusable(false);
								((EditText) vv).setFocusableInTouchMode(false);
							}
						}
					}
				}
			}
			// Log.i("@@@", "选中复选获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		}
	}

	class TvCheckListener implements OnClickListener {
		TextView tvcb;
		CheckBox cb;
		private EditText et;
		private QuestionItem item;
		private ArrayList<View> vs;
		private Question q;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public TvCheckListener(TextView tvcb, CheckBox check, EditText edit,
				QuestionItem item, ArrayList<View> vs, Question q,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.tvcb = tvcb;
			this.cb = check;
			this.et = edit;
			this.item = item;
			this.vs = vs;
			this.q = q;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (!Util.isEmpty(tvcb.getText())) {
				int cbText = Integer.parseInt(tvcb.getText().toString());
				tvcb.setText("");
				item.isCheck = false;
				tvcb.setTag(item);
				cb.setTag(item);
				cb.setChecked(false);

				// 大于 cbText的值都减去1
				for (View view : vs) {// for
					if (view instanceof TextView && !(view instanceof Button)
							&& !(view instanceof EditText)) {
						if (!Util.isEmpty(((TextView) view).getText())) {
							int i = Integer.parseInt(((TextView) view)
									.getText().toString());
							if (cbText < i) {
								int j = i - 1;
								((TextView) view).setText(j + "");
							}
						}
					}
				}
			} else {
				int max = 1;
				cb.setChecked(true);
				item.isCheck = true;
				tvcb.setTag(item);
				cb.setTag(item);
				// 如果不是空，则添加序号
				for (View view : vs) {// for
					if (view instanceof TextView && !(view instanceof Button)
							&& !(view instanceof EditText)) {
						if (!Util.isEmpty(((TextView) view).getText())) {
							max++;
						}
					}
				}
				tvcb.setText(max + "");
			}
			if ("true".equals(item.getExclude())) {// 有排斥的
				// System.out.println("排斥");
				for (View view : vs) {// for
					if (view instanceof CheckBox) {// check
						if (cb.isChecked()) {
							CheckBox checkBox = (CheckBox) view;
							QuestionItem qitem = (QuestionItem) checkBox
									.getTag();
							if (!Util.isEmpty(item.excludeIn)) {
								String excludeIn = item.excludeIn;
								System.out.println("excludeIn:" + excludeIn);
								if (!Util.isEmpty(excludeIn)) {
									String[] excludeValue = excludeIn
											.split(",");
									if (cb.getId() == checkBox.getId()) {// 是自己
										checkBox.setChecked(true);
										item.isCheck = true;
										checkBox.setTag(item);
									} else {
										for (int i = 0; i < excludeValue.length; i++) {
											if (Integer
													.parseInt(excludeValue[i]
															.trim()) == (qitem
													.getItemValue())) {
												checkBox.setChecked(false);
												qitem.isCheck = false;
												checkBox.setTag(qitem);
											}
										}
										if ("true".equals(qitem.exclude)
												&& Util.isEmpty(qitem.excludeIn)) {
											checkBox.setChecked(false);
											qitem.isCheck = false;
											checkBox.setTag(qitem);
										}
									}
								}
							} else {
								if (cb.getId() == checkBox.getId()) {// 是自己
									checkBox.setChecked(true);
									item.isCheck = true;
									checkBox.setTag(item);
								} else {
									checkBox.setChecked(false);
									qitem.isCheck = false;
									checkBox.setTag(qitem);
								}
							}
						}
					} else if (view instanceof EditText) {
						EditText vet = (EditText) view;
						QuestionItem qitem = (QuestionItem) vet.getTag();
						if (null != et && vet.getId() == et.getId()) {// 是自己的EditText
							if (cb.isChecked()) {
								/**
								 * 焦点有效
								 */
								vet.setFocusable(true);
								/**
								 * 触碰焦点有效
								 */
								vet.setFocusableInTouchMode(true);
								item.isCheck = true;
								vet.setTag(item);
							} else {
								vet.setText("");
								vet.setFocusable(false);
								vet.setFocusableInTouchMode(false);
								qitem.isCheck = false;
								vet.setTag(qitem);
							}
						} else {// 别人的EidtText
							vet.setText("");
							vet.setFocusable(false);
							vet.setFocusableInTouchMode(false);
							qitem.isCheck = false;
							vet.setTag(qitem);
						}
					}
				} // for
			} else {// 无排斥的
				if (null != et) {// 其他项
					if (cb.isChecked()) {
						/**
						 * CheckBox被选中了, 则CheckBox后面的EditText可是输入
						 */
						et.setFocusable(true);
						et.setFocusableInTouchMode(true);
						item.isCheck = true;
						cb.setTag(item);
						et.setTag(item);
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId()
										&& "true".equals(qi.getExclude())
										&& Util.isEmpty(qi.excludeIn)) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {
								QuestionItem qi = (QuestionItem) vv.getTag();
								if ("true".equals(qi.getExclude())) {
									((EditText) vv).setText("");
									((EditText) vv).setFocusable(false);
									((EditText) vv)
											.setFocusableInTouchMode(false);
								}
							}
						}
					} else {
						/**
						 * 假如CheckBox为非选中状态, 则其后面的EditText中的文本为空, 并且是不可以输入的
						 */
						et.setText("");
						et.setFocusable(false);
						et.setFocusableInTouchMode(false);
						item.isCheck = false;
						cb.setTag(item);
						et.setTag(item);
						// System.out.println("其他项取消选中");
					}
				} else {// 非其他项

					if (cb.isChecked()) {
						cb.setChecked(true);
						item.isCheck = true;
						cb.setTag(item);
						// System.out.println("非其他项选中");
						// for (QuestionItem qi:items) {
						for (View vv : vs) {
							if (vv instanceof CheckBox) {
								CheckBox cb1 = ((CheckBox) vv);
								QuestionItem qi = (QuestionItem) cb1.getTag();
								if (cb.getId() != vv.getId()
										&& "true".equals(qi.getExclude())
										&& Util.isEmpty(qi.excludeIn)) {
									cb1.setChecked(false);
									qi.isCheck = false;
									cb1.setTag(qi);
								}
							} else if (vv instanceof EditText) {
								// 提示
								// ((EditText) vv).setText("");
								// ((EditText) vv).setFocusable(false);
								// ((EditText)
								// vv).setFocusableInTouchMode(false);
							}
						}
					}
				}

			}
			// Log.i("@@@", "选中复选获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		}
	}

	/**
	 * 复选矩阵排他项点击事件
	 */
	class CheckListenerMatrix implements OnClickListener {
		private CheckBox cb;
		private RadioGroup ll;
		private QuestionItem item;
		private ArrayList<View> vs;
		private Question q;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public CheckListenerMatrix(CheckBox check, QuestionItem item,
				RadioGroup ll, ArrayList<View> vs, Question q,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.cb = check;
			this.item = item;
			this.ll = ll;
			this.vs = vs;
			this.q = q;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Log.i("CheckListenerMatrix", "CheckListenerMatrix");
			if (cb.isChecked()) {
				Log.i("CheckListenerMatrix", "true");
				String excludeIn = item.getExclude();
				if (!Util.isEmpty(excludeIn)) {
					String[] excludeValue = excludeIn.split(",");
					if (excludeValue.length > 0) {
						boolean isClickExclude = false; // 点击的是否为排他项
						for (int j = 0; j < excludeValue.length; j++) {
							int exclude = Integer.parseInt(excludeValue[j]
									.trim());
							if (ll.getChildAt(exclude + 1) instanceof CheckBox) {
								CheckBox checkBox = (CheckBox) ll
										.getChildAt(exclude + 1);// 设置的排斥项
								if (cb == checkBox) {// 点击的是排他项
									isClickExclude = true;
									for (int i = 0; i < ll.getChildCount(); i++) {
										if (ll.getChildAt(i) instanceof CheckBox) {
											// ////////////////////////
											// 同为排斥项目的相互之间不互斥
											boolean isExcluded = false;// 是排斥项
											for (int k = 0; k < excludeValue.length; k++) {
												int excludeA = Integer
														.parseInt(excludeValue[k]
																.trim());
												if (i == excludeA + 1) {
													isExcluded = true;
												}
											}
											if (!isExcluded) {
												((CheckBox) ll.getChildAt(i))
														.setChecked(false);
											}
										}
									}
								}
							}
						}
						if (!isClickExclude) {
							for (int j = 0; j < excludeValue.length; j++) {
								int exclude = Integer.parseInt(excludeValue[j]
										.trim());
								if (ll.getChildAt(exclude + 1) instanceof CheckBox) {
									CheckBox checkBox = (CheckBox) ll
											.getChildAt(exclude + 1);
									checkBox.setChecked(false);
								}
							}
						} // 设置的排斥项
					}
				}
				String itemSingle = item.getItemSingle();
				if (!Util.isEmpty(itemSingle) && "true".equals(itemSingle)) {
					for (int i = 0; i < ll.getChildCount(); i++) {
						if (ll.getChildAt(i) instanceof CheckBox) {
							((CheckBox) ll.getChildAt(i)).setChecked(false);
						}
					}
				}
				cb.setChecked(true);
				ll.setTag(item);
				// Log.i("CheckListenerMatrix", "setTag="+item.toString());
			} else {
				Log.i("CheckListenerMatrix", "false");
			}
			// Log.i("@@@", "选中复选矩阵获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {

				Log.i("CheckListenerMatrix",
						"mTempRows=" + mTempRows.toString());
				Log.i("CheckListenerMatrix", "REFRESH=" + REFRESH + "position="
						+ position);
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		}
	}

	class MatrixRadioBtListener implements OnClickListener {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<View> rowViews;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public MatrixRadioBtListener(Question q, ArrayList<View> vs,
				ArrayList<View> rowViews, ArrayList<QuestionItem> mTempRows,
				int position) {
			this.q = q;
			this.vs = vs;
			this.rowViews = rowViews;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			RadioButton rButton = (RadioButton) v;
			if (rButton.isChecked()) {
				if (0 != q.qStarCheck) {
					for (int j = 0; j < rowViews.size(); j++) {
						int drawable = R.drawable.small_radiobutton;
						switch (q.qStarCheck) {
						case 1:
							drawable = android.R.color.transparent;
							if (q.realRows % 2 == 0) {
								((RadioButton) rowViews.get(j))
										.setBackgroundResource(R.color.white);
							} else {
								((RadioButton) rowViews.get(j))
										.setBackgroundResource(R.color.Radio_back);
							}
							break;
						case 2:
							drawable = R.drawable.star_24_off;
							break;
						case 3:
							drawable = R.drawable.hand_24_off;
							break;
						case 4:
							drawable = R.drawable.heart_24_off;
							break;
						}
						((RadioButton) rowViews.get(j))
								.setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable),
										null, null, null);
					}
					for (int j = 0; j < rowViews.indexOf(v) + 1; j++) {
						int drawable = R.drawable.small_radiobutton;
						switch (q.qStarCheck) {
						case 1:
							((RadioButton) rowViews.get(j))
									.setBackgroundResource(R.color.transparent);
							drawable = android.R.color.transparent;
							break;
						case 2:
							drawable = R.drawable.star_24;
							break;
						case 3:
							drawable = R.drawable.hand_24;
							break;
						case 4:
							drawable = R.drawable.heart_24;
							break;
						}
						((RadioButton) rowViews.get(j))
								.setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable),
										null, null, null);
					}
				}
				// Log.i("@@@", "选中单选矩阵获取答案");
				// Log.i("@@@", "hideCount=" + q.hideCount);
				int success = getQuestionAnswer( false, q, vs, null,
						mTempRows, false);
				if (Cnt.STATE_SUCCESS == success) {
					Message msg = Message.obtain();
					msg.what = REFRESH;
					msg.arg1 = position;
					handler.sendMessage(msg);
				}
			}
		}
	}

	private void refreshView() {
		/**
		 * 获取屏幕的宽度
		 */
		screenWidth = dis.getWidth();
		/**
		 * 获取屏幕的高度
		 */
		screenHeight = dis.getHeight();

		/**
		 * 获取题干View的宽度
		 */
		// int qWidth = rlQuestion.getWidth();
		/**
		 * 获取题干View的高度
		 */
		// int qHeight = rlQuestion.getHeight();
		/**
		 * 标题、上方说明、底部追加说明的最大宽度只能是屏幕的
		 */
		// tvTitle.setMaxWidth(maxWidth);
		// tvCaption.setMaxWidth(maxWidth);
		// tvComment.setMaxWidth(maxWidth);
		int maxWidth = (int) (screenWidth * 0.9);
		ll.setPadding(10, //
				10, //
				10, //
				10);
	}

	/**
	 * 提交验证
	 * 
	 * @param operType
	 * @param isNoValidate
	 * @param q
	 * @param vs
	 * @param tvMap
	 * @return
	 */
	public int getValidate(boolean isNoValidate, Question q) {
		BaseLog.w("提交验证");
		if (null == q) {
			return Cnt.STATE_SUCCESS;
		}
		int currIndex = q.currIndex;
		BaseLog.w("本提-"+q.qid);
		BaseLog.w("本提第几项-"+currIndex);
		/**
		 * 当前问题的答案
		 */
		Answer qAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex + "");
		
		if (null == qAnswer) {
			BaseLog.w("qAnswer=null");
			if (1 == q.qRequired) {
				BaseLog.w("qRequired=1");
				/**
				 * 文本域题若无答案附加默认提示
				 */
				/** 单题录音验证开始 */
				if (Cnt.TYPE_FREE_TEXT_AREA == q.qType) {// 文本域
					int success = getQuestionAnswer( false, q,
							vsRecord.get(q.qIndex), null, mTempRowsRecord, true);
					if (1 == q.itemRecording) {// 如果是文本域题判断是否有录音，有录音即为答题成功
						// System.out.println("验证录音");
						boolean isSingleRecord = ma.dbService.isSingleRecord(
								feed.getSurveyId(), feed.getUserId(),
								feed.getUuid(), q.qIndex + "",
								ma.cfg.getString(Cnt.AUTHORID, ""));
						if (!isSingleRecord) {
							// System.out.println("没有录音");
							Message msg = Message.obtain();
							msg.what = FORMAT_NO_CAMERA;
							msg.obj = getResources().getString(
									R.string.no_record);
							currIndex = 0;
							sum = 0;
							handler.sendMessage(msg);
							return FORMAT_NO_CAMERA;
						} else {// 有录音，开始验证拍照和签名
							/** 单题拍照验证开始 */
							if (1 == q.qRequired && 1 == q.qCamera) {

								// System.out.println("验证拍照");
								boolean isSingleCamera = ma.dbService
										.isSingleCamera(feed.getSurveyId(),
												feed.getUserId(), feed
														.getUuid(), q.qIndex
														+ "", ma.cfg.getString(
														Cnt.AUTHORID, ""));
								if (!isSingleCamera) {
									// System.out.println("没有拍照");
									Message msg = Message.obtain();
									msg.what = FORMAT_NO_CAMERA;
									msg.obj = getResources().getString(
											R.string.no_picture);
									currIndex = 0;
									sum = 0;
									handler.sendMessage(msg);
									return FORMAT_NO_CAMERA;
								} else {
									// System.out.println("有拍照");
								}
							}
							/** 单题拍照验证结束 */
							/** 单题签名验证开始 */
							if (1 == q.qRequired && 1 == q.qSign) {
								// System.out.println("验证拍照");
								boolean isSingleSign = ma.dbService
										.isSingleSign(feed.getSurveyId(), feed
												.getUserId(), feed.getUuid(),
												q.qIndex + "", ma.cfg
														.getString(
																Cnt.AUTHORID,
																""));
								if (!isSingleSign) {
									// System.out.println("没有拍照");
									Message msg = Message.obtain();
									msg.what = FORMAT_NO_SIGN;
									msg.obj = getResources().getString(
											R.string.no_sign);
									currIndex = 0;
									sum = 0;
									handler.sendMessage(msg);
									return FORMAT_NO_SIGN;
								} else {
									// System.out.println("有拍照");
								}
							}
							return Cnt.STATE_SUCCESS;
						}
					}
				}
				return STATE_NOTHING;

			} else {
				
				BaseLog.w("qRequired !=1");
				ArrayList<QuestionItem> colItemArr = q.getColItemArr();
				for (QuestionItem qitem : colItemArr) {
					if (qitem.required) {
						String strTilte = "";
						if (!Util.isEmpty(q.qid)) {
							strTilte = q.qid;
						} else {
							strTilte = getResources().getString(
									R.string.question_order, q.qOrder);
						}
						Message msg = Message.obtain();
						msg.what = STATE_NOTHING;
						msg.obj = strTilte
								+ getResources().getString(
										R.string.msg_must_item);
						handler.sendMessage(msg);
						return ITEM_NO_REQUIRED;
					}
				}
				return Cnt.STATE_SUCCESS;
			}
		}else{
			BaseLog.w("qAnswer  ！！！！=null");
		}
		
		BaseLog.w("returnType="+qAnswer.returnType);
		
		
		String relevanceIndex = "0";// 关联的 上一题的 index
		int relevanceOptionIndex = 0;// 关联的上一题的 哪一个选项
		if (ComUtil.questionOutyingRelevance(q, qAnswer.getAnswerMapArr(), ma,feed)) {
			Log.i("zrl1", "走这里1" + "关联类型" + q.qParentAssociatedCheck);
			qAnswer.returnType = QUESTION_OUT_YING_RELEVANCE;
			String[] ss = q.qParentAssociatedCheck.split(",");
			relevanceIndex = ss[1];
			relevanceOptionIndex = Integer.valueOf(ss[2]) + 1;
		}
		String sumSign = "";
		if (qAnswer.returnType != STATE_NOTHING&& q.qParentAssociatedCheck != null) {
			if (qAnswer.getAnswerMapArr() != null
					&& qAnswer.getAnswerMapArr().size() > 0) {
				for (AnswerMap map : qAnswer.getAnswerMapArr()) {
					String value = map.getAnswerText().trim();
					if (map.isSUM && !value.equals("")) {
						if (Util.isFormat(value, 9)) {
							sumSign = String.valueOf((Integer.valueOf(map
									.getAnswerName().split("_")[3]) + 1));
						}
					}
				}
			}
		}
		/**
		 * 初始状态
		 */
		if (!isNoValidate) {
			BaseLog.w("初始状态"+isNoValidate);
			BaseLog.w("初始状态"+qAnswer.returnType);
			
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid;
			} else {
				strTilte = getResources().getString(R.string.question_order,
						q.qOrder);
			}
			if (STATE_NOTHING == qAnswer.returnType) {
				// 判断文本域题有单题录音的必答
				Log.i("getValidate++","returnType==" + STATE_NOTHING);
			} else if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
				Message msg = Message.obtain();
				msg.what = qAnswer.returnType;
				switch (qAnswer.returnType) {
				case Cnt.STATE_CLASS_STOP:
					msg.obj = getResources().getString(
							R.string.item_no_continue);
					break;
				case Cnt.STATE_FAIL:
					msg.obj = strTilte
							+ getResources()
									.getString(R.string.other_item_null);
					break;
				case STATE_SYB_MAX:
					if (q.isNew) {
						msg.obj = strTilte
								+ getResources().getString(R.string.max_value,
										currIndex, q.freeMaxNumber);
					} else {
						msg.obj = strTilte
								+ getResources()
										.getString(R.string.old_max_value,
												q.freeMaxNumber);
					}
					break;

				case STATE_SYB_MIN:
					if (q.isNew) {
						msg.obj = strTilte
								+ getResources().getString(R.string.min_value,
										currIndex, q.freeMinNumber);
					} else {
						msg.obj = strTilte
								+ getResources()
										.getString(R.string.old_min_value,
												q.freeMinNumber);
					}
					break;

				case FORMAT_NO_NUMBER:
					if (q.isNew) {
						msg.obj = strTilte
								+ getResources().getString(
										R.string.format_no_number,
										currIndex + "");
					} else {
						msg.obj = strTilte
								+ getResources().getString(
										R.string.old_format_no_number);
					}
					break;

				case STATE_BOUND_LOWER:
					msg.obj = strTilte
							+ getResources().getString(R.string.bound_min,
									q.lowerBound);
					break;
				case STATE_SORT_CHECK:
					msg.obj = strTilte
							+ getResources().getString(R.string.bound_must);
					break;
				case STATE_BOUND_UPPER:
					msg.obj = strTilte
							+ getResources().getString(R.string.bound_max,
									q.upperBound);
					break;

				case STATE_CONTINUOUS:
					msg.obj = strTilte
							+ getResources()
									.getString(R.string.question_coutinuous,
											q.qContinuous);
					break;

				case STATE_BOUND_MATRIX_LOWER:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.bound_matrix_min, q.lowerBound);
					break;

				case STATE_BOUND_MATRIX_UPPER:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.bound_matrix_max, q.upperBound);
					break;

				case STATE_ROW_LESS:
					msg.obj = strTilte
							+ getResources().getString(R.string.row_less);
					break;

				case STATE_SYB_REPEAT:
					msg.obj = strTilte
							+ getResources().getString(R.string.value_repeat);
					break;
				case STATE_SYB_DATE_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_equal_error, strTilte,
							currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_upper_equal_error, strTilte,
							currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_NO_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_no_equal_error, strTilte,
							currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER:
					msg.obj = getResources().getString(
							R.string.date_upper_error, strTilte,
							currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_low_equal_error, strTilte,
							currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW:
					msg.obj = getResources().getString(R.string.date_low_error,
							strTilte, currIndex + "", preTitle,
							(preIndex + 1) + "");
					break;
				case STATE_SYB_LEN_EQUAL:
					Log.i("STATE_SYB_LEN_EQUAL", "strTilte=" + strTilte
							+ "q.currIndex=" + q.currIndex + "strLen="
							+ q.getColItemArr().get(q.currIndex - 1).getLen());
					int index = 0;
					if (q.currIndex == 0) {

					} else if (q.currIndex > 0) {
						index = q.currIndex - 1;
					}

					String len = q.getColItemArr().get(index).getLen();
					msg.obj = getResources().getString(
							R.string.len_equal_error, strTilte,
							q.currIndex + "", len);
					break;
				case STATE_SYB_LEN_UPPER_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_upper_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_NO_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_no_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_UPPER:
					msg.obj = getResources().getString(
							R.string.len_upper_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_LOW_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_low_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_LOW:
					msg.obj = getResources().getString(R.string.len_low_error,
							strTilte, q.currIndex + "", strLen);
					break;
				case FORMAT_NO_FLOAT_NUMBER:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.format_no_float_number,
									currIndex + "");
					break;
				case STATE_SYB_SUM_VALUE:
					msg.obj = strTilte
							+ getResources().getString(R.string.question_sum,
									q.freeSymbol + q.freeSumNumber);
					break;
				case FORMAT_NO_NUMBER_AND_WORD:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.format_no_word_and_number,
									currIndex + "");
					break;
				case FORMAT_NO_EMAIL:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.format_no_email, currIndex + "");
					break;
				case ITEM_NO_REQUIRED:
					msg.obj = strTilte
							+ getResources().getString(
									R.string.item_no_required, currIndex + "");
					break;
				/**
				 * 题外关联之和 提示语 的展现 ！ 大树 外部关联 4
				 */
				case QUESTION_OUT_YING_RELEVANCE:
					msg.obj = strTilte
							+ getString(R.string.question_outying_error,
									relevanceIndex, relevanceOptionIndex + "");
					break;
				/**
				 * 题外关联 之和 内部关联 大树 内部关联 15
				 */
				case QUESTION_INTERIOR_RELEVANCE:
					msg.obj = strTilte
							+ getString(R.string.question_innerying_error,
									sumSign);
					break;
				/**
				 * IMS临时干预价格 小数点位数为 2
				 */
				case DECIMAL_PLACES_NO_MEET:
					msg.obj = strTilte
							+ getString(R.string.decimel_places_no_meet);
					break;
				/**
				 * 大树排序 选项值不能重复
				 */
				case QUESTION_ITEM_ORDER_REPEAT:
					msg.obj = strTilte + getString(R.string.sequence_repeat);
					break;
				case LUO_JI_WRONG: // IMS 皮炎湿疹
					msg.obj = strTilte + getString(R.string.luoji_wrong);
					break;
				}
				handler.sendMessage(msg);
				return qAnswer.returnType;
			}
			if (q.isNew) {
				if (1 == q.qRequired && syb == STATE_NOTHING
						&& q.qType == Cnt.TYPE_FREE_TEXT_BOX) {
					return qAnswer.returnType;
				}
				/**** 数字之和开始 **/
				// 看看数值题目的文本题是否是小于号
				if ("=".equals(q.freeSymbol)) {
					// 假如总值不小于最大值并且没有其他逻辑错误 提示值必须大于
					if (!(sum == Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				} else if ("<=".equals(q.freeSymbol)) {
					// 假如总值不小于等于最大值并且没有其他逻辑错误
					if (!(sum <= Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				} else if (">=".equals(q.freeSymbol)) {
					// 假如总值不大于等于最大值并且没有其他逻辑错误
					if (!(sum >= Double.parseDouble(q.freeSumNumber))) {
						syb = STATE_SYB_SUM_VALUE;
						qAnswer.returnType = STATE_SYB_SUM_VALUE;
					}
				}
				if (qAnswer.returnType == STATE_SYB_SUM_VALUE) {
					Message msg = Message.obtain();
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.question_sum,
							q.freeSymbol + q.freeSumNumber);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				}
			}
			/**** 数字之和结束 **/
			/** 单题录音验证开始 */
			if (1 == q.qRequired && 1 == q.itemRecording) {
				// System.out.println("验证录音");
				boolean isSingleRecord = ma.dbService.isSingleRecord(
						feed.getSurveyId(), feed.getUserId(), feed.getUuid(),
						q.qIndex + "", ma.cfg.getString(Cnt.AUTHORID, ""));
				if (!isSingleRecord) {
					// System.out.println("没有录音");
					Message msg = Message.obtain();
					qAnswer.returnType = FORMAT_NO_CAMERA;
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.no_record);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				} else {
					if (Cnt.TYPE_FREE_TEXT_AREA == q.qType) {// 文本域
						// 有录音但是答案为空
						int success = getQuestionAnswer( false, q,
								vsRecord.get(q.qIndex), null, mTempRowsRecord,
								true);

						qAnswer.returnType = Cnt.STATE_SUCCESS;
					}
					// System.out.println("有录音");
				}
			}
			/** 单题录音验证结束 */
			/** 单题拍照验证开始 */
			if (1 == q.qRequired && 1 == q.qCamera) {
				// System.out.println("验证拍照");
				boolean isSingleCamera = ma.dbService.isSingleCamera(
						feed.getSurveyId(), feed.getUserId(), feed.getUuid(),
						q.qIndex + "", ma.cfg.getString(Cnt.AUTHORID, ""));
				if (!isSingleCamera) {

					// System.out.println("验证拍照");
					String Photocheck[] = q.getPhotocheck().split(",");
					boolean isphotocheck = false;
					for (int h = 0; h < Photocheck.length; h++) {

						if (!Util.isEmpty(Photocheck[h])) {
							for (int i = 0; i < qAnswer.getAnswerMapArr()
									.size(); i++) {
								String answervalue = qAnswer.getAnswerMapArr()
										.get(i).getAnswerValue();

								if (answervalue.equals(Photocheck[h].trim())) {
									isphotocheck = true;
									continue;
								}
							}

						} else {

							if (!isSingleCamera) {
								// System.out.println("没有拍照");
								Message msg = Message.obtain();
								msg.what = FORMAT_NO_CAMERA;
								msg.obj = q.qid
										+ getResources().getString(
												R.string.no_picture);
								currIndex = 0;
								sum = 0;
								handler.sendMessage(msg);
								return FORMAT_NO_CAMERA;
							} else {
								// System.out.println("有拍照");
							}
							continue;

						}
					}

					System.out.println("answerMapArr=="
							+ qAnswer.getAnswerMapArr());
					for (int i = 0; i < Photocheck.length; i++) {
						System.out.println("Photocheck==" + Photocheck[i]);
					}

					if (isphotocheck) {

						if (!isSingleCamera) {
							// System.out.println("没有拍照");
							Message msg = Message.obtain();
							msg.what = FORMAT_NO_CAMERA;
							msg.obj = q.qid
									+ getResources().getString(
											R.string.no_picture);
							currIndex = 0;
							sum = 0;
							handler.sendMessage(msg);
							return FORMAT_NO_CAMERA;
						} else {
							// System.out.println("有拍照");
						}

					}

					// // System.out.println("没有拍照");
					// Message msg = Message.obtain();
					// qAnswer.returnType = FORMAT_NO_CAMERA;
					// msg.what = qAnswer.returnType;
					// msg.obj = getResources().getString(R.string.no_picture);
					// currIndex = 0;
					// sum = 0;
					// handler.sendMessage(msg);
					// return qAnswer.returnType;
				} else {
					// System.out.println("有拍照");
				}
			}
			/** 单题拍照验证结束 */
			/** 单题签名验证开始 */
			if (1 == q.qRequired && 1 == q.qSign) {
				// System.out.println("验证拍照");
				boolean isSingleSign = ma.dbService.isSingleSign(
						feed.getSurveyId(), feed.getUserId(), feed.getUuid(),
						q.qIndex + "", ma.cfg.getString(Cnt.AUTHORID, ""));
				if (!isSingleSign) {
					// System.out.println("没有拍照");
					Message msg = Message.obtain();
					qAnswer.returnType = FORMAT_NO_SIGN;
					msg.what = qAnswer.returnType;
					msg.obj = getResources().getString(R.string.no_sign);
					currIndex = 0;
					sum = 0;
					handler.sendMessage(msg);
					return qAnswer.returnType;
				} else {
					// System.out.println("有拍照");
				}
			}
		} else {
			Log.i("getValidate++","初始状态isnull");
			qAnswer.returnType = Cnt.STATE_SUCCESS;
		}
		
		
		
		
		
		
		
		
		
		return qAnswer.returnType;
	}

	private List<String> getNewData(String input_info, List<String> list) {
		List<String> newlist = new ArrayList<String>();
		input_info = input_info.toLowerCase();
		// 遍历list
		for (int i = 0; i < list.size(); i++) {
			String domain = list.get(i).toLowerCase().trim().toLowerCase();
			// 如果遍历到的名字包含所输入字符串
			if (domain.equals(input_info)) {
				newlist.add(domain);
			}
		}
		return newlist;
	}

	/**
	 * 哑题验证
	 * 
	 * @param operType
	 * @param mTempRows
	 * @param tempAnswer
	 * @param isRecord
	 *            //是否是文本题手动录音获取答案
	 * @return
	 */
	ArrayList<Answer> ansertlist = new ArrayList<Answer>();
	
	public int getQuestionAnswer( boolean isNoValidate,
			Question q, ArrayList<View> vs, HashMap<String, Integer> tvMap,
			ArrayList<QuestionItem> mTempRows, boolean isRecord) {
		
		Log.i("getQuestionAnswer", "进入getQuestionAnswer");
		
		if (null == q) {
			return Cnt.STATE_SUCCESS;
		}
		int hideCount = q.hideCount;
		/**
		 * 当前问题的答案
		 */
		Answer qAnswer = new Answer();
		qAnswer.uuid = feed.getUuid();
		Answer tempAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex
				+ "");
		ArrayList<AnswerMap> answerMapArr = new ArrayList<AnswerMap>();
		
		
//		Log.i("getQuestionAnswer", "tempAnswer"+tempAnswer.returnType);
		
		
		/**
		 * 初始状态
		 */
		qAnswer.returnType = STATE_NOTHING;
		q.currIndex = 0;
		switch (q.qType) {
		case Cnt.TYPE_HEADER:
			/**
			 * 标题直接算获取答案成功
			 */
			qAnswer.returnType = Cnt.STATE_SUCCESS;
			break;

		case Cnt.TYPE_RADIO_BUTTON:
			for (View view : vs) {
				QuestionItem item = (QuestionItem) view.getTag();
				if (null != item) {
					if (1 == item.isOther) {// 带其他项
						if (view instanceof RadioButton) {
							// RadioButton rb = (RadioButton)view;
							// if(rb.isChecked()){
							AnswerMap am = new AnswerMap();
							String radioName = Util.GetAnswerName(q, item, 0,
									0, false, false);
							am.setAnswerName(radioName);
							am.setAnswerValue(String.valueOf(item.itemValue));
							am.setRow(item.itemValue);
							// System.out.println("其他项单选按钮_name=" + radioName +
							// ", 其他项单选按钮_value=" +
							// String.valueOf(item.itemValue));
							am.setAnswerText(item.itemText);
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
							// }
						} else if (view instanceof EditText) {
							EditText et = (EditText) view;
							AnswerMap am = new AnswerMap();
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							am.setAnswerName(etName);
							String text = et.getText().toString();
							// System.out.println("其他项_文本框_name=" + etName +
							// ", 其他项_文本框_value=" + text);
							am.setAnswerValue(text);
							am.setRow(item.itemValue);
							am.setAnswerText(Util.isEmpty(item.itemText) ? text
									: (item.itemText + ":" + text));
							if (Util.isEmpty(text)) {
								// et.setBackgroundColor(Color.YELLOW);
								Util.viewShake(NativeModeNoPageActivity.this,
										et);
								qAnswer.returnType = Cnt.STATE_FAIL;
							} else {
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}
						}
					} else {// 不带其他项的
						if (view instanceof RadioButton) {
							// RadioButton rb = (RadioButton)view;
							// if(rb.isChecked()){
							AnswerMap am = new AnswerMap();
							// System.out.println("单选按钮_name=" +
							// Util.GetAnswerName(q, item, 0, 0, false));
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0,
									false, false));
							// System.out.println("单选按钮_value=" +
							// String.valueOf(item.itemValue));
							am.setAnswerValue(String.valueOf(item.itemValue));
							am.setRow(item.itemValue);
							am.setAnswerText(item.itemText);
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
							qAnswer.returnType = Cnt.STATE_SUCCESS;
							// }
						}
					}
				}
			}
			break;
		/**
		 * 复选题目
		 */
		case Cnt.TYPE_CHECK_BOX:
			int bound = 0;
			for (View view : vs) {
				QuestionItem item = (QuestionItem) view.getTag();
				// System.out.println("ddddddddddddddd");
				if (null != item) {
					// System.out.println("item");
					if (1 == item.isOther) {// 带其他项的
						System.out.println("item.isCheck:" + item.isCheck);
						if (view instanceof CheckBox) {
							CheckBox cb = (CheckBox) view;
							if (cb.isChecked()) {
								bound += 1;
								AnswerMap am = new AnswerMap();
								// System.out.println("其他项复选_name=" +
								// Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerName(Util.GetAnswerName(q, item, 0,
										0, false, false));
								// System.out.println("其他项复选_value=" +
								// String.valueOf(item.itemValue));
								am.setAnswerValue(String
										.valueOf(item.itemValue));
								am.setAnswerText(item.itemText);
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
							} else if (1 != q.qRequired) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(Util.GetAnswerName(q, item, 0,
										0, false, false));
								am.setAnswerValue("");
								answerMapArr.add(am);
							}
						} else if (view instanceof EditText && item.isCheck) {
							EditText et = (EditText) view;
							AnswerMap am = new AnswerMap();
							// System.out.println("复选其他项_文本框_name=" +
							// Util.GetAnswerName(q, item, 0, 0, true));
							System.out.println(Util.GetAnswerName(q, item, 0,
									0, true, false) + "编号");
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0,
									true, false));
							String text = et.getText().toString();
							am.setAnswerValue(text);
							am.setAnswerText(text);
							// System.out.println("复选其他项_文本框_value=" + text);
							if (Util.isEmpty(text)) {
								// et.setBackgroundColor(Color.YELLOW);
								Util.viewShake(NativeModeNoPageActivity.this,
										et);
								qAnswer.returnType = Cnt.STATE_FAIL;
							} else {
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								/**
								 * 防止选了两个其他项,但是只填了其中一个其他项,另一个没填
								 */
								if (Cnt.STATE_FAIL != qAnswer.returnType) {
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
							}
						} else if (view instanceof TextView
								&& !(view instanceof Button)
								&& !(view instanceof EditText) && item.isCheck) {
							TextView tv = (TextView) view;
							AnswerMap am = new AnswerMap();
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0,
									false, true));
							String text = tv.getText().toString();
							am.setAnswerValue("sort" + text);
							am.setAnswerText(text);
							answerMapArr.add(am);
						}
					} else {// 不带其他项
						if (view instanceof TextView
								&& !(view instanceof Button)
								&& !(view instanceof EditText) && item.isCheck) {
							TextView tv = (TextView) view;
							AnswerMap am = new AnswerMap();
							am.setAnswerName(Util.GetAnswerName(q, item, 0, 0,
									false, true));
							String text = tv.getText().toString();
							am.setAnswerValue("sort" + text);
							am.setAnswerText(text);
							answerMapArr.add(am);
						} else if (view instanceof CheckBox) {
							CheckBox cb = (CheckBox) view;
							if (cb.isChecked()) {
								if (1 == item.isAnOther) {
									AnswerMap am = new AnswerMap();
									// System.out.println("复选其他项_文本框_name=" +
									// Util.GetAnswerName(q, item, 0, 0, true));
									System.out.println(Util.GetAnswerName(q,
											item, 0, 0, true, false) + "编号");
									am.setAnswerName(Util.GetAnswerName(q,
											item, 0, 0, true, false));
									am.setAnswerValue(item.itemText);
									am.setAnswerText(item.itemText);
									answerMapArr.add(am);
								}
								bound += 1;
								AnswerMap am = new AnswerMap();
								// System.out.println("复选_name=" +
								// Util.GetAnswerName(q, item, 0, 0, false));
								am.setAnswerName(Util.GetAnswerName(q, item, 0,
										0, false, false));
								// System.out.println("复选_value=" +
								// String.valueOf(item.itemValue));
								am.setAnswerValue(String
										.valueOf(item.itemValue));
								am.setAnswerText(item.itemText);
								// qAnswer.getAnswerMapArr().add(am);
								answerMapArr.add(am);
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							} else if (1 != q.qRequired) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(Util.GetAnswerName(q, item, 0,
										0, false, false));
								am.setAnswerValue("");
								answerMapArr.add(am);
							}
						}
					}
				}
			}
			if (1 == q.qSortChecked && !(0 < q.lowerBound || 0 < q.upperBound)) {
				// 如果是排序题则所有项必答
				if ("1".equals(q.qInclusion) || "0".equals(q.qInclusion)) {
					// 隐藏选项
					if (0 != bound && bound < (mTempRows.size() - hideCount)) {
						qAnswer.returnType = STATE_SORT_CHECK;
					}
				} else if (0 != bound
						&& bound < (q.getRowItemArr().size() - hideCount)) {
					/**
					 * 下限
					 */
					qAnswer.returnType = STATE_SORT_CHECK;
				}
			}
			/**
			 * 逻辑判断
			 */
			if (0 != bound && (0 < q.lowerBound || 0 < q.upperBound)) {
				if (bound < q.lowerBound) {
					/**
					 * 下限
					 */
					qAnswer.returnType = STATE_BOUND_LOWER;
				} else if (bound > q.upperBound && q.upperBound != 0) {
					/**
					 * 上限
					 */
					qAnswer.returnType = STATE_BOUND_UPPER;

				}
			}
			break;

		case Cnt.TYPE_MATRIX_RADIO_BUTTON:
			// int tempRow = 0;
			HashMap<Integer, Integer> tempRow = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> radioMap = new HashMap<Integer, Integer>();// 回答的列数
			HashMap<Integer, Integer> rowMap = new HashMap<Integer, Integer>();
			for (View view : vs) {
				AnswerMap am = (AnswerMap) view.getTag();
				if (null != am) {
					if (view instanceof RadioButton) {
						RadioButton rb = (RadioButton) view;
						if (rb.isChecked()) {
							/**
							 * 统计回答了多少行
							 */
							rowMap.put(am.getRow(), am.getRow());
							// if (1 != q.qRadomed) {
							// Integer col = radioMap.get(am.getCol());
							// if (null == col) {
							// radioMap.put(am.getCol(), 1);
							// tempRow.put(am.getCol(), am.getRow());
							// } else {
							// if (1 == (am.getRow() -
							// tempRow.get(am.getCol()))) {
							// radioMap.put(am.getCol(), col + 1);
							// } else {
							// if (q.qContinuous <= radioMap.get(am.getCol())) {
							//
							// } else {
							// radioMap.put(am.getCol(), 1);
							// }
							// }
							// tempRow.put(am.getCol(), am.getRow());
							// }
							// }
							// System.out.println("矩阵单选的AnswerName=" +
							// am.getAnswerName());
							// System.out.println("矩阵单选的AnswerValue=" +
							// am.getAnswerValue());
							// qAnswer.getAnswerMapArr().add(am);
							answerMapArr.add(am);
						}
					}
				}
			}
			if (1 == q.qRequired) {
				if (0 == rowMap.size()) {
					/**
					 * 假如单选矩阵一个选项都没选
					 */
					qAnswer.returnType = STATE_NOTHING;
					break;
				} else {

					/**
					 * 排斥或引用
					 */
					if ("1".equals(q.qInclusion) || "0".equals(q.qInclusion)) {
						// 隐藏选项
						if (rowMap.size() < (mTempRows.size() - hideCount)) {
							qAnswer.returnType = STATE_ROW_LESS;
						}
					}
					// 隐藏选项
					else if (rowMap.size() < (q.getRowItemArr().size() - hideCount)) {
						qAnswer.returnType = STATE_ROW_LESS;
						break;
					}
				}
			} else {
				/**
				 * 非必填
				 */
				qAnswer.returnType = Cnt.STATE_SUCCESS;
			}
			rowMap.clear();
			rowMap = null;
			/**
			 * 说明这道题设置了同一列连续作答的限制
			 */
			break;
		case Cnt.TYPE_MATRIX_CHECK_BOX:
			/**
			 * 保存每一行选了多少个
			 */
			Log.i("answerMapArr", "1");
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (View view : vs) {
				Log.i("answerMapArr", "2");
				if (view instanceof CheckBox) {
					CheckBox cb = (CheckBox) view;
					if (cb.isChecked()) {

						AnswerMap am = (AnswerMap) cb.getTag();
						String s[] = am.getAnswerName().split("_");
						am.setAnswerValue(s[3]);
						Log.i("矩阵复选_name=", "am = " + am.toString());

						answerMapArr.add(am);
						/**
						 * 如果am.getRow()那一行没有选过,则将其值置为1;否则,在其原有的值上加1
						 */
						map.put(am.getRow(), null == map.get(am.getRow()) ? 1
								: (map.get(am.getRow()) + 1));
					} else if (1 != q.qRequired) {
						Log.i("answerMapArr", "b");
						AnswerMap am = (AnswerMap) cb.getTag();
						am.setAnswerValue("");
						answerMapArr.add(am);
						if (cb.isChecked()) {
							map.put(am.getRow(),
									null == map.get(am.getRow()) ? 1 : (map
											.get(am.getRow()) + 1));
						}
					}
				}
			}
			if (1 == q.qRequired) {
				if (0 == map.size()) {
					/**
					 * 假如单选矩阵一个选项都没选
					 */
					qAnswer.returnType = STATE_NOTHING;
					break;
				} else {
					if ("1".equals(q.qInclusion) || "0".equals(q.qInclusion)) {
						if (map.size() != mTempRows.size()) {
							qAnswer.returnType = STATE_ROW_LESS;
							break;
						}
					} else if (map.size() != q.getRowItemArr().size()) {
						qAnswer.returnType = STATE_ROW_LESS;
						break;
					}
				}
			} else {
				Log.i("answerMapArr", "5");
				qAnswer.returnType = Cnt.STATE_SUCCESS;
			}
			/**
			 * 说明这道题设置了上下限 或者的关系
			 */
			// 矩阵复选问题记录
			if (0 < q.lowerBound || 0 < q.upperBound) {
				Log.i("answerMapArr", "3");
				Iterator<Entry<Integer, Integer>> it = map.entrySet()
						.iterator();
				while (it.hasNext()) {
					Entry<Integer, Integer> entry = it.next();
					Integer v = entry.getValue();
					System.out.println("v:" + v);
					if ((null != v) && (v < q.lowerBound)) {
						qAnswer.returnType = STATE_BOUND_MATRIX_LOWER;
						break;
					} else if ((null != v)
							&& (v > q.upperBound && q.upperBound != 0)) {
						qAnswer.returnType = STATE_BOUND_MATRIX_UPPER;
						break;
					}
				}
			}
			/**
			 * 清空map中的数据
			 */
			map.clear();
			/**
			 * 断开内存地址映射
			 */
			map = null;
			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下来列表
			for (View view : vs) {
				if (view instanceof Spinner) {
					Spinner sp = (Spinner) view;
					String name = Util.GetAnswerName(q, null, 0, 0, false,
							false);
					int value = sp.getSelectedItemPosition();
					// QuestionItem qi;
					// 1为忽略第一项，选第一项也算没选
					if (1 == q.ignoreFirstItem) {
						if (0 == value) {
							qAnswer.returnType = STATE_NOTHING;
							break;
						} else {
							String key = value + "_"
									+ sp.getAdapter().getItem(value);
							int realValue = tvMap.get(key);
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
							am.setAnswerValue(String.valueOf(realValue));
							am.setAnswerText(sp.getSelectedItem().toString());
							answerMapArr.add(am);
						}
					} else {
						String key = value + "_"
								+ sp.getAdapter().getItem(value);
						int realValue = tvMap.get(key);
						AnswerMap am = new AnswerMap();
						am.setAnswerName(name);
						am.setAnswerValue(String.valueOf(realValue));
						am.setAnswerText(sp.getSelectedItem().toString());
						answerMapArr.add(am);
					}
				}
			}
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框
			if (q.isNew) {
				
				BaseLog.w("单行文本框");
				/** 数字类型的存储状态 **/
				syb = 0;
				/** 是否需要验证 **/
				boolean is = false;
				/** 临时记录item各个项 **/
				int tempIndex = 0;
				/** 是否判断有没有浮点数还是整数 **/
				int format = 10;
				/** 判断是不是数字题目并且最大值不等于空为true否则为false **/
				// boolean _syb = ("figure".equals(q.freeTextSort) &&
				// !Util.isEmpty(q.freeMaxNumber));// 1
				/** 不能重复时候把所有值放到集合中去判断 **/
				ArrayList<Double> repeat_value = new ArrayList<Double>();
				// 大树排序
				Log.i("zrl1", q.qSortChecked + "排序：");
				if (q.qSortChecked == 1) {
					orderRepeatList = new ArrayList<Integer>();
					orderRepeatMap = new HashMap<Integer, String>();
				}
				for (View view : vs) {
					tempIndex++;
					
					if (view instanceof EditText) {
						BaseLog.w("单行文本框 foreach 文本、数字、邮件、日期 vs = " + tempIndex);
						/**
						 * 文本、数字、邮件、日期
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						q.item = null;
						if (null != item) {
							EditText et = (EditText) view;
							String value = et.getText().toString();
							String name = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							BaseLog.w("EditText ritem.type" + item.type);
							switch (item.type) {
							
							case 0:// 普通类型
									// 维码扫描
							case 4:// 数据字典
							case 6:
								if (1 == q.qRequired) {
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util
												.isEmpty(item.caption) ? value
												: (item.caption + ":" + value));
										am.setRow(item.itemValue);
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {
										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 22
										 */
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
											syb = Cnt.STATE_FAIL;
											break;
										}
									}
								} else {
									// 看看是不是单项必填的
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											if (item.isHide || item.isSetHide) {
												qAnswer.returnType = Cnt.STATE_SUCCESS;
											} else {
												q.currIndex = tempIndex;
												syb = ITEM_NO_REQUIRED;
												qAnswer.returnType = ITEM_NO_REQUIRED;
												break;
											}
										}
									}
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}

								if (is) {
									if (0 == item.type) {
										// 无类型
										// 判断字符串长度是否符合
										strLen = item.len;
										String lensymbol = item.lensymbol;
										// 引用和逻辑不为空
										if (!Util.isEmpty(strLen)
												&& !Util.isEmpty(lensymbol)) {
											try {
												boolean lenCompare = Util
														.getLenNumberCompare(
																strLen, value,
																lensymbol);
												// System.out.println("比较结果:"
												// + dateCompare + " 现在的值"
												// + value + " 原有的值:" +
												// preValue);
												if (lenCompare) {
													/**** TODO **/
													// 验证成功
												} else {
													q.currIndex = tempIndex;
													// System.out.println("验证不成功的:"
													// + dateCompare);
													if ("=".equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_EQUAL;
													} else if ("!="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_NO_EQUAL;
													} else if (">"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER;
													} else if (">="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER_EQUAL;
													} else if ("<"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW;
													} else if ("<="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW_EQUAL;
													}
													break;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}

										}
									}

								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
										//UIEditText 保存答案
										
									}
								}
								if (4 == item.type && !Util.isEmpty(value)) {// 数据字典判断
									System.out.println("数据字典判断");
									if ("1".equals(item.classtype)) {// 匹配后继续
										System.out.println("item.classtype==1");
										Data data = ma.dbService
												.queryDataById(item.classid);
										List<String> list = new ArrayList<String>();
										List<String> newlist = new ArrayList<String>();
										String newArr = null;
										if (!Util.isEmpty(data.getDatas())
												&& !Util.isEmpty(data
														.getLocalDatas())) {
											newArr = data.getDatas()
													+ data.getLocalDatas();
										} else if (!Util.isEmpty(data
												.getDatas())
												&& Util.isEmpty(data
														.getLocalDatas())) {
											newArr = data.getDatas();
										} else if (Util
												.isEmpty(data.getDatas())
												&& Util.isEmpty(data
														.getLocalDatas())) {
											newArr = "";
										}
										if (!Util.isEmpty(newArr)) {
											String[] split = newArr.split(",");
											for (int i = 0; i < split.length; i++) {
												list.add(split[i]);
											}
										}
										newlist = getNewData(value, list);
										if (newlist != null
												&& newlist.size() > 0) {
											System.out.println("数据字典找到了");
										} else {
											System.out.println("数据字典没找到");
											q.currIndex = tempIndex;
											syb = Cnt.STATE_CLASS_STOP;
											qAnswer.returnType = Cnt.STATE_CLASS_STOP;

										}

									} else if ("2".equals(item.classtype)) {// 匹配后终止，提示
										System.out.println("item.classtype==2");
										Data data = ma.dbService
												.queryDataById(item.classid);
										List<String> list = new ArrayList<String>();
										List<String> newlist = new ArrayList<String>();
										String newArr = null;
										if (!Util.isEmpty(data.getDatas())
												&& !Util.isEmpty(data
														.getLocalDatas())) {
											newArr = data.getDatas()
													+ data.getLocalDatas();
										} else if (!Util.isEmpty(data
												.getDatas())
												&& Util.isEmpty(data
														.getLocalDatas())) {
											newArr = data.getDatas();
										} else if (Util
												.isEmpty(data.getDatas())
												&& Util.isEmpty(data
														.getLocalDatas())) {
											newArr = "";
										}
										if (!Util.isEmpty(newArr)) {
											String[] split = newArr.split(",");
											for (int i = 0; i < split.length; i++) {
												list.add(split[i]);
											}
										}
										newlist = getNewData(value, list);
										if (newlist != null
												&& newlist.size() > 0) {
											System.out.println("数据字典找到了");
											q.currIndex = tempIndex;
											syb = Cnt.STATE_CLASS_STOP;
											qAnswer.returnType = Cnt.STATE_CLASS_STOP;
										} else {
											System.out.println("数据字典没找到");

										}

									}

								}
								break;
							case 1:// 日期格式 2013-08-08 16:12:49
								if (1 == q.qRequired) {
									// System.out.println("value:" + value);
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util
												.isEmpty(item.caption) ? value
												: (item.caption + ":" + value));
										am.setRow(item.itemValue);
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {
										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 23
										 */
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
											syb = Cnt.STATE_FAIL;
											break;
										}
									}
								} else {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											if (item.isHide || item.isSetHide) {
												qAnswer.returnType = Cnt.STATE_SUCCESS;
											} else {
												q.currIndex = tempIndex;
												syb = ITEM_NO_REQUIRED;
												qAnswer.returnType = ITEM_NO_REQUIRED;
												break;
											}
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
								if (is) {
									/**
									 * 一个答案都没填
									 */
									// 获取引用的题号和顺序
									String titlefrom = item.titlefrom;
									String symbol = item.symbol;
									// 引用和逻辑不为空
									if (!Util.isEmpty(titlefrom)
											&& !Util.isEmpty(symbol)) {
										String qIndex = titlefrom.split(",")[0];
										Answer answer = ma.dbService.getAnswer(
												feed.getUuid(), qIndex);
										Question preQues = ma.dbService
												.getQuestion(q.surveyId, qIndex);
										// 查到了
										if (answer != null && preQues != null) {
											ArrayList<AnswerMap> answerMapArr2 = answer
													.getAnswerMapArr();
											preIndex = Integer
													.parseInt(titlefrom
															.split(",")[1]);
											if (!Util.isEmpty(preQues.qid)) {
												preTitle = preQues.qid;
											} else {
												preTitle = getResources()
														.getString(
																R.string.question_order,
																preQues.qOrder);
											}
											AnswerMap answerMap = answerMapArr2
													.get(preIndex);
											String dateValue1 = answerMap
													.getAnswerValue();
											try {
												boolean dateCompare = Util
														.getDateCompare(
																dateValue1,
																value, symbol);
												// System.out.println("比较结果:" +
												// dateCompare);
												if (dateCompare) {
													/**** TODO **/
													// 验证成功
												} else {
													q.currIndex = tempIndex;
													// System.out.println("验证不成功的:"
													// + dateCompare);
													if ("=".equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_EQUAL;
													} else if ("!="
															.equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
													} else if (">"
															.equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_UPPER;
													} else if (">="
															.equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
													} else if ("<"
															.equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_LOW;
													} else if ("<="
															.equals(symbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
													}
													break;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										}
										// 没查到
										else {
											/**** TODO **/
											// 服务器配置xml错误
										}
									}
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
											syb = STATE_NOTHING;
											qAnswer.returnType = STATE_NOTHING;
											//UIEditText 保存答案
											
										}
								}
								break;

							case 2:// 数字格式
								BaseLog.w("数字格式---------------");
								if (1 == q.qRequired) {
									BaseLog.w("必答题---------------");
									if (!Util.isEmpty(value)) {
										BaseLog.w("value不为空--------------");
										AnswerMap am = new AnswerMap();

										/**
										 * 题外关联 之 外部关联 设置 数字标示 大树 外部关联 2
										 */
										am.isFigure = true;

										/**
										 * 题外关联 之 内部关联 设置 SUM标示 大树 内部关联 12
										 */

										if (item.symbol != null
												&& item.symbol
														.equals(this
																.getString(R.string.sum))) {
											am.isSUM = true;
										}

										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util
												.isEmpty(item.caption) ? value
												: (item.caption + ":" + value));
										am.setRow(item.itemValue);
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {

										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 24
										 */
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
											syb = Cnt.STATE_FAIL;
											break;
										}

									}
								} else {
									AnswerMap am = new AnswerMap();
									/**
									 * 题外关联 之 外部关联 设置 数字标示 大树 外部关联 3
									 */
									am.isFigure = true;

									/**
									 * 题外关联 之 内部关联 设置 SUM标示 大树 内部关联 13
									 */

									if (item.symbol != null
											&& item.symbol.equals(this
													.getString(R.string.sum))) {
										am.isSUM = true;
									}
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											if (item.isHide || item.isSetHide) {
												qAnswer.returnType = Cnt.STATE_SUCCESS;
											} else {
												q.currIndex = tempIndex;
												syb = ITEM_NO_REQUIRED;
												qAnswer.returnType = ITEM_NO_REQUIRED;
												break;
											}
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}

								
								BaseLog.w("必答验证结束---rtp = " + qAnswer.returnType + "\nis == "+is);
								
								if (is) {
									q.special = item.specialNumber;
									if (item.isFloat) {
										format = 9;// 浮点 整数
									} else {
										format = 10;// 整数
									}
									
									if (Util.isFormat(value, format)) {
										// 判断字符串长度是否符合
										strLen = item.len;
										String lensymbol = item.lensymbol;
										
										
										BaseLog.w("strLen = " + strLen);
										BaseLog.w("lensymbol = " + lensymbol);

										
										
										// 引用和逻辑不为空
										if (!Util.isEmpty(strLen)
												&& !Util.isEmpty(lensymbol)) {
											try {
												boolean lenCompare = Util
														.getLenNumberCompare(
																strLen, value,
																lensymbol);
												// System.out.println("比较结果:"
												// + dateCompare + " 现在的值"
												// + value + " 原有的值:" +
												// preValue);
												if (lenCompare) {
													/**** TODO **/
													// 验证成功
												} else {
													q.currIndex = tempIndex;
													// System.out.println("验证不成功的:"
													// + dateCompare);
													if ("=".equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_EQUAL;
													} else if ("!="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_NO_EQUAL;
													} else if (">"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER;
													} else if (">="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER_EQUAL;
													} else if ("<"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW;
													} else if ("<="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW_EQUAL;
													}
													break;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}

										}

										
										BaseLog.w("需要验证。是否是数字并且是否为数字");
										// 需要验证。是否是数字并且是否为数字
										/**
										 * IMS临时干预价格 4月份执行final 判断 浮点数位数 是否2位
										 */
										Log.i("zrl1", "走这里");
										boolean isIms = false; // 判断是不是
																// IMS的项目以及干预题目
										if (Cnt.appVersion == 3
												&& "132".equals(feed
														.getSurveyId())) {
											BaseLog.w("appVersion == 3");
											
											if (q.qIndex == 299
													|| q.qIndex == 302
													|| q.qIndex == 306
													|| q.qIndex == 309
													|| q.qIndex == 312
													|| q.qIndex == 315
													|| q.qIndex == 318
													|| q.qIndex == 321
													|| q.qIndex == 324
													|| q.qIndex == 327
													|| q.qIndex == 350
													|| q.qIndex == 330
													|| q.qIndex == 333
													|| q.qIndex == 336
													|| q.qIndex == 341) {
												isIms = true;
												if (value.contains(".")) {
													String[] ss = value
															.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										} else if (Cnt.appVersion == 3
												&& "134".equals(feed
														.getSurveyId())) {
											if (q.qIndex == 299
													|| q.qIndex == 302
													|| q.qIndex == 420
													|| q.qIndex == 306
													|| q.qIndex == 309
													|| q.qIndex == 312
													|| q.qIndex == 315
													|| q.qIndex == 318
													|| q.qIndex == 321
													|| q.qIndex == 324
													|| q.qIndex == 429
													|| q.qIndex == 438
													|| q.qIndex == 327
													|| q.qIndex == 350
													|| q.qIndex == 330
													|| q.qIndex == 333
													|| q.qIndex == 336
													|| q.qIndex == 341) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value
															.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										} else if (Cnt.appVersion == 3
												&& "138".equals(feed
														.getSurveyId())) {
											if (q.qIndex == 299
													|| q.qIndex == 302
													|| q.qIndex == 420
													|| q.qIndex == 306
													|| q.qIndex == 309
													|| q.qIndex == 312
													|| q.qIndex == 315
													|| q.qIndex == 318
													|| q.qIndex == 321
													|| q.qIndex == 324
													|| q.qIndex == 429
													|| q.qIndex == 438
													|| q.qIndex == 327
													|| q.qIndex == 350
													|| q.qIndex == 330
													|| q.qIndex == 333
													|| q.qIndex == 336
													|| q.qIndex == 341) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value
															.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										}
										// 138 6月份 final 临时干预
										else if (Cnt.appVersion == 3
												&& "145".equals(feed
														.getSurveyId())) {
											if (q.qIndex == 299
													|| q.qIndex == 302
													|| q.qIndex == 420
													|| q.qIndex == 306
													|| q.qIndex == 309
													|| q.qIndex == 312
													|| q.qIndex == 315
													|| q.qIndex == 318
													|| q.qIndex == 321
													|| q.qIndex == 324
													|| q.qIndex == 429
													|| q.qIndex == 438
													|| q.qIndex == 327
													|| q.qIndex == 350
													|| q.qIndex == 330
													|| q.qIndex == 333
													|| q.qIndex == 336
													|| q.qIndex == 341
													|| q.qIndex == 447
													|| q.qIndex == 456) {

												isIms = true;
												if (value.contains(".")) {
													String[] ss = value
															.split("\\.");
													if (ss[1].length() != 2) {
														Log.i("zrl1", "不合条件");
														qAnswer.returnType = DECIMAL_PLACES_NO_MEET;
													}
												}
											}
										}
										
										BaseLog.w("145 7月份 final 临时干预");
										// 145 7月份 final 临时干预
										double v = Double.parseDouble(value);
										if (!Util.isEmpty(item.minNumber)) {
											BaseLog.w("item.minNumber = "+item.minNumber);
											BaseLog.w("V = "+v);
											// 假如v< 最小的值 不合法 跳出本次循环
											if (v < Double
													.parseDouble(item.minNumber)) {
												// IMS临时干预价格 如果是0 过 如果 不是两位 小数
												// 也过
												// Log.i("@@@", 333 + "");
												// Log.i("@@@", isIms + "");
												if (!Util
														.isEmpty(item.specialNumber)) {
													// 特殊值处理
													String itemsp = item.specialNumber;
													String[] speciallist = itemsp
															.split(",");
													boolean issp = false;
													for (String special : speciallist) {
														double spe = Double
																.parseDouble(special);
														if (v == spe) {
															issp = true;
															break;
														}
													}
													if (issp) {
														qAnswer.returnType = Cnt.STATE_SUCCESS;
													} else {
														if ("true"
																.equals(item.range)) {
															if (!item.isRange) {
																q.currIndex = tempIndex;
																syb = STATE_SYB_MIN_RANGE;
																q.freeMinNumber = item.minNumber;
																qAnswer.returnType = STATE_SYB_MIN_RANGE;
																q.item = item;
															}
															this.question = q;
														} else {
															q.currIndex = tempIndex;
															syb = STATE_SYB_MIN;
															q.freeMinNumber = item.minNumber;
															qAnswer.returnType = STATE_SYB_MIN;
														}
													}
												} else {
													if ("true"
															.equals(item.range)) {
														if (!item.isRange) {
															q.currIndex = tempIndex;
															syb = STATE_SYB_MIN_RANGE;
															q.freeMinNumber = item.minNumber;
															qAnswer.returnType = STATE_SYB_MIN_RANGE;
															q.item = item;
														}
														this.question = q;
													} else {
														q.currIndex = tempIndex;
														syb = STATE_SYB_MIN;
														q.freeMinNumber = item.minNumber;
														qAnswer.returnType = STATE_SYB_MIN;
													}

												}
												break;
												// return Cnt.STATE_SYB;
												// 假如v> 最大的值 不合法 跳出本次循环
											}
										}

										BaseLog.w("item.maxNumber");
										if (!Util.isEmpty(item.maxNumber)) {
											BaseLog.w("item.maxNumber ="+item.maxNumber);
											BaseLog.w("V ="+v);
											if (v > (Util
													.isEmpty(item.maxNumber) ? 0
													: Double.parseDouble(item.maxNumber))) {
												BaseLog.w("V>xxxx = true");
												if (!Util
														.isEmpty(item.specialNumber)) {
													BaseLog.w("特殊值处理");
													// 特殊值处理
													String itemsp = item.specialNumber;
													String[] speciallist = itemsp
															.split(",");
													boolean issp = false;
													for (String special : speciallist) {
														double spe = Double
																.parseDouble(special);
														if (v == spe) {
															issp = true;
															break;
														}
													}
													if (issp) {
														qAnswer.returnType = Cnt.STATE_SUCCESS;
													} else {
														if ("true"
																.equals(item.range)) {
															if (!item.isRange) {
																q.currIndex = tempIndex;
																q.freeMaxNumber = item.maxNumber;
																syb = STATE_SYB_MAX_RANGE;
																qAnswer.returnType = STATE_SYB_MAX_RANGE;
																q.item = item;
															}
															this.question = q;
														} else {
															q.currIndex = tempIndex;
															q.freeMaxNumber = item.maxNumber;
															syb = STATE_SYB_MAX;
															qAnswer.returnType = STATE_SYB_MAX;
														}
													}
												} else {
													BaseLog.w("没有特殊值处理");
													if ("true"
															.equals(item.range)) {
														if (!item.isRange) {
															q.currIndex = tempIndex;
															q.freeMaxNumber = item.maxNumber;
															syb = STATE_SYB_MAX_RANGE;
															qAnswer.returnType = STATE_SYB_MAX_RANGE;
															q.item = item;
														}
														this.question = q;

													} else {
														q.currIndex = tempIndex;
														q.freeMaxNumber = item.maxNumber;
														syb = STATE_SYB_MAX;
														qAnswer.returnType = STATE_SYB_MAX;
													}
												}
												BaseLog.w("qAnswer.returnType = "+qAnswer.returnType);
												break;
											}
										}
										
										// 假如不能重复并且 重复集合中找到这个值 不合法 退出
										if (1 == q.freeNoRepeat
												&& -1 != repeat_value
														.indexOf(Double
																.parseDouble(value))) {
											syb = STATE_SYB_REPEAT;
											qAnswer.returnType = STATE_SYB_REPEAT;
											break;
										}
										repeat_value.add(Double
												.parseDouble(value));

										/** 比较和原先题目 **/
										/**
										 * 一个答案都没填
										 */
										// 获取引用的题号和顺序
										String titlefrom = item.titlefrom;
										String symbol = item.symbol;
										// 引用和逻辑不为空
										if (!Util.isEmpty(titlefrom)
												&& !Util.isEmpty(symbol)) {
											String qIndex = titlefrom
													.split(",")[0];
											Answer answer = ma.dbService
													.getAnswer(feed.getUuid(),
															qIndex);
											Question preQues = ma.dbService
													.getQuestion(q.surveyId,
															qIndex);
											// 查到了
											if (answer != null
													&& preQues != null) {
												ArrayList<AnswerMap> answerMapArr2 = answer
														.getAnswerMapArr();
												preIndex = Integer
														.parseInt(titlefrom
																.split(",")[1]);
												if (!Util.isEmpty(preQues.qid)) {
													preTitle = preQues.qid;
												} else {
													preTitle = getResources()
															.getString(
																	R.string.question_order,
																	preQues.qOrder);
												}
												String preValue = null;
												for (AnswerMap answerMap : answerMapArr2) {
													if (answerMap.getRow() == preIndex) {
														preValue = answerMap
																.getAnswerValue();
													}
												}
												try {
													boolean dateCompare = Util
															.getDoubleNumberCompare(
																	preValue,
																	value,
																	symbol);
													// System.out.println("比较结果:"
													// + dateCompare + " 现在的值"
													// + value + " 原有的值:" +
													// preValue);
													if (dateCompare) {
														/**** TODO **/
														// 验证成功
													} else {
														q.currIndex = tempIndex;
														// System.out.println("验证不成功的:"
														// + dateCompare);
														if ("=".equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_EQUAL;
														} else if ("!="
																.equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
														} else if (">"
																.equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_UPPER;
														} else if (">="
																.equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
														} else if ("<"
																.equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_LOW;
														} else if ("<="
																.equals(symbol)) {
															syb = STATE_SYB_DATE_EQUAL;
															qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
														}
														break;
													}
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
											// 没查到
											else {
												/**** TODO **/
												// 服务器配置xml错误
											}
										}
										sum += v;
									} else {

										q.currIndex = tempIndex;
										if (format == 9) {
											// 浮点

											syb = FORMAT_NO_NUMBER;
											qAnswer.returnType = FORMAT_NO_NUMBER;
										} else if (format == 10) {
											// 数字
											syb = FORMAT_NO_FLOAT_NUMBER;
											qAnswer.returnType = FORMAT_NO_FLOAT_NUMBER;
										}
									}
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
											syb = STATE_NOTHING;
											qAnswer.returnType = STATE_NOTHING;
											//UIEditText 保存答案

											BaseLog.w("一个答案都没填 = ------");
									}
								}
								BaseLog.w("break  qAnswer.returnType = " + qAnswer.returnType);
								break;

							case 3:// 英文/数字格式
							case 5:// 邮件
								
								BaseLog.w("英文/数字格式/邮件");
								
								if (1 == q.qRequired) {
									if (!Util.isEmpty(value)) {
										AnswerMap am = new AnswerMap();
										am.setAnswerName(name);
										am.setAnswerValue(value);
										am.setAnswerText(Util
												.isEmpty(item.caption) ? value
												: (item.caption + ":" + value));
										am.setRow(item.itemValue);
										answerMapArr.add(am);
										if (Cnt.STATE_FAIL != qAnswer.returnType) {
											/**
											 * 防止前面选项的题目没有填写
											 */
											qAnswer.returnType = Cnt.STATE_SUCCESS;
											is = true;
										}
									} else {

										/**
										 * 题外关联 之 显示关联 第一次加载的时候 限制干预 大树 显示关联 25
										 */
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											qAnswer.returnType = Cnt.STATE_FAIL;
											syb = Cnt.STATE_FAIL;
											break;
										}
									}
								} else {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (!Util.isEmpty(value)) {
										is = true;
									} else {
										// 假如是必选的
										if (item.required) {
											if (item.isHide || item.isSetHide) {
												qAnswer.returnType = Cnt.STATE_SUCCESS;
											} else {
												q.currIndex = tempIndex;
												syb = ITEM_NO_REQUIRED;
												qAnswer.returnType = ITEM_NO_REQUIRED;
												break;
											}
										}
									}

									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
								if (is) {
									if (3 == item.type) {
										BaseLog.w("英文/数字格式" + qAnswer.returnType);
										// 判断字符串长度是否符合
										strLen = item.len;
										String lensymbol = item.lensymbol;
										// 引用和逻辑不为空
										if (!Util.isEmpty(strLen)
												&& !Util.isEmpty(lensymbol)) {
											try {
												boolean lenCompare = Util
														.getLenNumberCompare(
																strLen, value,
																lensymbol);
												// System.out.println("比较结果:"
												// + dateCompare + " 现在的值"
												// + value + " 原有的值:" +
												// preValue);
												if (lenCompare) {
													/**** TODO **/
													// 验证成功
												} else {
													q.currIndex = tempIndex;
													// System.out.println("验证不成功的:"
													// + dateCompare);
													if ("=".equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_EQUAL;
													} else if ("!="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_NO_EQUAL;
													} else if (">"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER;
													} else if (">="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_UPPER_EQUAL;
													} else if ("<"
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW;
													} else if ("<="
															.equals(lensymbol)) {
														syb = STATE_SYB_DATE_EQUAL;
														qAnswer.returnType = STATE_SYB_LEN_LOW_EQUAL;
													}
													break;
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}

										}

										// 判断是不是数字\英文
										if (Util.isFormat(value, 11)
												&& (value.getBytes().length == value
														.length())) {
											// 是数字或者英文
										} else {
											// 不是
											q.currIndex = tempIndex;
											syb = FORMAT_NO_NUMBER_AND_WORD;
											qAnswer.returnType = FORMAT_NO_NUMBER_AND_WORD;
										}
									} else if (5 == item.type) {
										BaseLog.w("邮件" + qAnswer.returnType);
										// 判断是不是邮件格式
										if (Util.isFormat(value, 4)) {
											// 是正确的邮箱地址
										} else {
											// 不是
											q.currIndex = tempIndex;
											syb = FORMAT_NO_EMAIL;
											qAnswer.returnType = FORMAT_NO_EMAIL;
										}
									}
									
									BaseLog.w("3-5==" + qAnswer.returnType);
								} else {
									/**
									 * 一个答案都没填
									 */
									if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
											syb = STATE_NOTHING;
											qAnswer.returnType = STATE_NOTHING;
											//UIEditText 保存答案
											
									}
								}
								BaseLog.w("3-5==" + qAnswer.returnType);
								break;
							}
						}
//						BaseLog.w("EditText returnType 1 =" + qAnswer.returnType);
//						if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
//							/**
//							 * 一个答案都没填
//							 */
//							if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
//								syb = STATE_NOTHING;
//								qAnswer.returnType = STATE_NOTHING;
//								//UIEditText 保存答案
//							}
//						}
						
					} else if (view instanceof Spinner) {
						BaseLog.w("Spinner=" + qAnswer.returnType);
						/**
						 * 下拉,含1%%2%%3 三级联动关联 问题的解决 @ 存值问题的解决！
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						if (null != item) {
							Spinner sp = (Spinner) view;
							// System.out.println("类型:" + sp.toString());
							String name = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							String value = sp.getSelectedItem().toString();
							// 大树排序
							if (orderRepeatList != null) {
								orderRepeatList.add(sp
										.getSelectedItemPosition());
								orderRepeatMap.put(
										sp.getSelectedItemPosition(), value);
							}
							/**
							 * 三级联动 存值 设置
							 */
							if (1 == q.qRequired) {
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
									}
								} else {
									if (item.isHide || item.isSetHide) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
									} else {
										qAnswer.returnType = Cnt.STATE_FAIL;
										syb = STATE_NOTHING;
										qAnswer.returnType = STATE_NOTHING;
									}
								}
							} else {
								if (!Util.isEmpty(value)) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value);
									am.setAnswerText(Util.isEmpty(item.caption) ? value
											: (item.caption + ":" + value));
									am.setRow(item.itemValue);
									answerMapArr.add(am);
								} else {
									if (item.required) {
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											q.currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}
								}
								qAnswer.returnType = Cnt.STATE_SUCCESS;
							}

							Log.i("保存答案-修改sp答案的时候走这里了", "保存Spinner答案	qAnswer.returnType"
									+ qAnswer.returnType);
							
							
						}

					} else if (view instanceof SeekBar) {
						BaseLog.w("SeekBar=" + qAnswer.returnType);
						// item.type=2
						/**
						 * 拖动题
						 */
						QuestionItem item = (QuestionItem) view.getTag();
						int itemMinNumber = Integer
								.parseInt(Util.isEmpty(item.minNumber) ? "0"
										: item.minNumber);
						if (null != item) {
							SeekBar sb = (SeekBar) view;
							String name = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							int value = sb.getProgress() + itemMinNumber;
							if (1 == q.qRequired) {
								if (item.isChange) {
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue(value + "");
									am.setRow(item.itemValue);
									answerMapArr.add(am);
									if (Cnt.STATE_FAIL != qAnswer.returnType) {
										/**
										 * 防止前面选项的题目没有填写
										 */
										qAnswer.returnType = Cnt.STATE_SUCCESS;
										is = true;
									}
								} else {
									if (item.isHide || item.isSetHide) {
										qAnswer.returnType = Cnt.STATE_SUCCESS;
									} else {
										qAnswer.returnType = Cnt.STATE_FAIL;
										syb = Cnt.STATE_FAIL;
									}
								}
							} else {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value + "");
								am.setRow(item.itemValue);
								answerMapArr.add(am);
								qAnswer.returnType = Cnt.STATE_SUCCESS;
								if (item.isChange) {
									is = true;
								} else {
									if (item.required) {
										if (item.isHide || item.isSetHide) {
											qAnswer.returnType = Cnt.STATE_SUCCESS;
										} else {
											q.currIndex = tempIndex;
											syb = ITEM_NO_REQUIRED;
											qAnswer.returnType = ITEM_NO_REQUIRED;
											break;
										}
									}

								}
							}

							if (is) {
								// 需要验证。是否是数字并且是否为数字

								if (!Util.isEmpty(item.minNumber)) {
									// 假如v< 最小的值 不合法 跳出本次循环
									if (value < (Util.isEmpty(item.minNumber) ? 0
											: Integer.parseInt(item.minNumber))) {
										q.currIndex = tempIndex;
										syb = STATE_SYB_MIN;
										q.freeMinNumber = item.minNumber;
										qAnswer.returnType = STATE_SYB_MIN;
										break;
									}
									// return Cnt.STATE_SYB;

									// 假如v> 最大的值 不合法 跳出本次循环
								}
								if (!Util.isEmpty(item.maxNumber)) {
									if (value > (Util.isEmpty(item.maxNumber) ? 0
											: Integer.parseInt(item.maxNumber))) {
										q.currIndex = tempIndex;
										q.freeMaxNumber = item.maxNumber;
										syb = STATE_SYB_MAX;
										qAnswer.returnType = STATE_SYB_MAX;
										break;
									}
								}
								/** 比较和原先题目 **/
								/**
								 * 一个答案都没填
								 */
								// 获取引用的题号和顺序
								String titlefrom = item.titlefrom;
								String symbol = item.symbol;
								// 引用和逻辑不为空
								if (!Util.isEmpty(titlefrom)
										&& !Util.isEmpty(symbol)) {
									String qIndex = titlefrom.split(",")[0];
									Answer answer = ma.dbService.getAnswer(
											feed.getUuid(), qIndex);
									Question preQues = ma.dbService
											.getQuestion(q.surveyId, qIndex);
									// 查到了
									if (answer != null && preQues != null) {
										ArrayList<AnswerMap> answerMapArr2 = answer
												.getAnswerMapArr();
										preIndex = Integer.parseInt(titlefrom
												.split(",")[1]);
										if (!Util.isEmpty(preQues.qid)) {
											preTitle = preQues.qid;
										} else {
											preTitle = getResources()
													.getString(
															R.string.question_order,
															preQues.qOrder);
										}
										String preValue = null;
										for (AnswerMap answerMap : answerMapArr2) {
											if (answerMap.getRow() == preIndex) {
												preValue = answerMap
														.getAnswerValue();
											}
										}
										try {
											boolean dateCompare = Util
													.getDoubleNumberCompare(
															preValue, value
																	+ "",
															symbol);
											// System.out.println("比较结果:" +
											// dateCompare);
											if (dateCompare) {
												/**** TODO **/
												// 验证成功
											} else {
												q.currIndex = tempIndex;
												// System.out.println("验证不成功的:"
												// + dateCompare);
												if ("=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_EQUAL;
												} else if ("!=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_NO_EQUAL;
												} else if (">".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_UPPER;
												} else if (">=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_UPPER_EQUAL;
												} else if ("<".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_LOW;
												} else if ("<=".equals(symbol)) {
													syb = STATE_SYB_DATE_EQUAL;
													qAnswer.returnType = STATE_SYB_DATE_LOW_EQUAL;
												}
											}
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
									// 没查到
									else {
										/**** TODO **/
										// 服务器配置xml错误
									}
								}
							} else {
								/**
								 * 一个答案都没填
								 */

								
								if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
									syb = STATE_NOTHING;
									qAnswer.returnType = STATE_NOTHING;
									//UIEditText 保存答案
								}
							
							}
						}
					}


					BaseLog.w("UIEditText =" + qAnswer.returnType);		
					
					is = false;
					if (syb == STATE_NOTHING || syb == STATE_SYB_DATE_EQUAL
							|| syb == STATE_SYB_DATE_UPPER_EQUAL
							|| syb == STATE_SYB_DATE_NO_EQUAL
							|| syb == STATE_SYB_DATE_UPPER
							|| syb == STATE_SYB_DATE_LOW_EQUAL
							|| syb == STATE_SYB_DATE_LOW
							|| syb == STATE_SYB_MIN || syb == STATE_SYB_MAX
							|| syb == STATE_SYB_REPEAT
							|| syb == FORMAT_NO_FLOAT_NUMBER
							|| syb == FORMAT_NO_NUMBER
							|| syb == STATE_SYB_SUM_VALUE
							|| syb == FORMAT_NO_NUMBER_AND_WORD
							|| syb == FORMAT_NO_EMAIL
							|| syb == ITEM_NO_REQUIRED
							|| syb == STATE_SYB_MIN_RANGE
							|| syb == STATE_SYB_MAX_RANGE
							|| syb == Cnt.STATE_FAIL) {
						sum = 0;
						break;
					}

				}

			}
			BaseLog.w("临时干预  qAnswer.returnType = " + qAnswer.returnType);
			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			for (View view : vs) {
				if (view instanceof EditText) {
					QuestionItem item = (QuestionItem) view.getTag();
					if (null != item) {
						EditText et = (EditText) view;
						// et.set
						String value = et.getText().toString();
						String name = Util.GetAnswerName(q, item, 0, 0, true,
								false);
						// System.out.println("文本_name=" + name + ", value=" +
						// value);
						if (1 == q.qRequired) {
							if (!Util.isEmpty(value)) {
								AnswerMap am = new AnswerMap();
								am.setAnswerName(name);
								am.setAnswerValue(value);
								am.setAnswerText(Util.isEmpty(item.caption) ? value
										: (item.caption + ":" + value));
								answerMapArr.add(am);
								if (Cnt.STATE_FAIL != qAnswer.returnType) {
									/**
									 * 防止前面选项的题目没有填写
									 */
									qAnswer.returnType = Cnt.STATE_SUCCESS;
								}
							} else {
								if (1 == q.itemRecording && isRecord) {// 如果是文本域题型则修改答案为已完成
									AnswerMap am = new AnswerMap();
									am.setAnswerName(name);
									am.setAnswerValue("有录音");
									am.setAnswerText(Util.isEmpty(item.caption) ? "有录音"
											: (item.caption + ":" + "有录音"));
									answerMapArr.add(am);
									// qAnswer.returnType = Cnt.STATE_SUCCESS;
								} else {
									qAnswer.returnType = STATE_NOTHING;
								}
							}
						} else {
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
							am.setAnswerValue(value);
							am.setAnswerText(Util.isEmpty(item.caption) ? value
									: (item.caption + ":" + value));
							answerMapArr.add(am);
							qAnswer.returnType = Cnt.STATE_SUCCESS;
						}
					}
				}
			}
			break;
		}
		
		
		
		/**
		 * IMS 临时干预 皮炎湿疹 单选自动下一页 注释掉 单选自动下一页
		 */
		if (!Util.isEmpty(answerMapArr)) {
			Log.i("zrl1", "不为空");
		} else {
			Log.i("zrl1", "为空");
		}
		if (null != imsIntervetion) {
			if (imsIntervetion.isSkipNext(imsIntervetion, q, 89, answerMapArr)) {
				qAnswer.returnType = LUO_JI_WRONG;
				//
			}
		}
		/**
		 * 题外关联 干预 在这里 大树 外部关联 3
		 */
		String relevanceIndex = "0";// 关联的 上一题的 index
		int relevanceOptionIndex = 0;// 关联的上一题的 哪一个选项
		if (ComUtil.questionOutyingRelevance(q, answerMapArr, ma, feed)) {
			Log.i("zrl1", "走这里1" + "关联类型" + q.qParentAssociatedCheck);
			qAnswer.returnType = QUESTION_OUT_YING_RELEVANCE;
			String[] ss = q.qParentAssociatedCheck.split(",");
			relevanceIndex = ss[1];
			relevanceOptionIndex = Integer.valueOf(ss[2]) + 1;
		}
		/**
		 * 题外关联 之和 干预 以上
		 */

		/**
		 * 题外关联 之 内部关联 以下 求和标志选项 这里 需要修改一下 有一个 小 bug 大树 内部关联 14
		 */

		String sumSign = "";
		if (qAnswer.returnType != STATE_NOTHING
				&& q.qParentAssociatedCheck != null) {
			double valueSum = 0;
			double itemSUm = 0;
			BigDecimal big = new BigDecimal("0");
			boolean isInterRe = false;
			if (answerMapArr != null && answerMapArr.size() > 0) {
				for (AnswerMap map : answerMapArr) {
					String value = map.getAnswerText();
					if (map.isSUM && !value.equals("")) {
						isInterRe = true;
						if (Util.isFormat(value, 9)) {
							sumSign = String.valueOf((Integer.valueOf(map
									.getAnswerName().split("_")[3]) + 1));
							valueSum = Double.valueOf(value);
						}
					} else if (map.isFigure && !value.equals("")) {
						if (Util.isFormat(value, 9)) {
							itemSUm += Double.valueOf(value);
							big = big.add(new BigDecimal(value));

						}
					}
				}
			}
			if (isInterRe && valueSum != big.doubleValue()) {
				qAnswer.returnType = QUESTION_INTERIOR_RELEVANCE;
			}
		}
		/**
		 * 题外关联 之 内部关联 以上
		 */

		/**
		 * 大树排序 如果 重复选项不为空 那么给出提示
		 */
		if (!Util.isEmpty(orderRepeatList)) {
			Log.i("zrl1", "orderList:==" + orderRepeatList.size());
			if (orderRepeatList.size() > orderRepeatMap.size()) {
				qAnswer.returnType = QUESTION_ITEM_ORDER_REPEAT;
			}
			orderRepeatList.clear();
			orderRepeatList = null;
			orderRepeatMap.clear();
			orderRepeatMap = null;
		}
		if (!isNoValidate) {
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid;
			} else {
				strTilte = getResources().getString(R.string.question_order,
						q.qOrder);
			}
			if (STATE_NOTHING == qAnswer.returnType) {
			} else if (Cnt.STATE_SUCCESS != qAnswer.returnType) {
				Message msg = Message.obtain();
				msg.what = qAnswer.returnType;

				switch (qAnswer.returnType) {
				case Cnt.STATE_CLASS_STOP:
					msg.obj = getResources().getString(
							R.string.item_no_continue);
					break;
				case Cnt.STATE_FAIL:
					msg.obj = getResources()
							.getString(R.string.other_item_null);
					break;
				case STATE_SYB_MAX:
					if (q.isNew) {
						msg.obj = getResources().getString(R.string.max_value,
								q.currIndex, q.freeMaxNumber);
					} else {
						msg.obj = getResources().getString(
								R.string.old_max_value, q.freeMaxNumber);
					}
					break;
				case STATE_SYB_MIN_RANGE:
					if (q.isNew) {
						msg.obj = getResources().getString(
								R.string.min_value_range, q.currIndex,
								q.freeMinNumber);
					}
					break;
				case STATE_SYB_MAX_RANGE:
					if (q.isNew) {
						msg.obj = getResources().getString(
								R.string.max_value_range, q.currIndex,
								q.freeMaxNumber);
					}
					break;
				case STATE_SYB_MIN:
					if (q.isNew) {
						msg.obj = getResources().getString(R.string.min_value,
								q.currIndex, q.freeMinNumber);
					} else {
						msg.obj = getResources().getString(
								R.string.old_min_value, q.freeMinNumber);
					}
					break;

				case FORMAT_NO_NUMBER:
					if (q.isNew) {
						msg.obj = getResources().getString(
								R.string.format_no_number, q.currIndex + "");
					} else {
						msg.obj = getResources().getString(
								R.string.old_format_no_number);
					}
					break;

				case STATE_BOUND_LOWER:
					msg.obj = getResources().getString(R.string.bound_min,
							q.lowerBound);
					break;
				case STATE_SORT_CHECK:
					msg.obj = getResources().getString(R.string.bound_must);
					break;

				case STATE_BOUND_UPPER:
					msg.obj = getResources().getString(R.string.bound_max,
							q.upperBound);
					break;

				case STATE_CONTINUOUS:
					msg.obj = getResources().getString(
							R.string.question_coutinuous, q.qContinuous - 1);
					break;

				case STATE_BOUND_MATRIX_LOWER:
					msg.obj = getResources().getString(
							R.string.bound_matrix_min, q.lowerBound);
					break;

				case STATE_BOUND_MATRIX_UPPER:
					msg.obj = getResources().getString(
							R.string.bound_matrix_max, q.upperBound);
					break;

				case STATE_ROW_LESS:
					msg.obj = getResources().getString(R.string.row_less);
					break;

				case STATE_SYB_REPEAT:
					msg.obj = getResources().getString(R.string.value_repeat);
					break;
				case STATE_SYB_DATE_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_equal_error, strTilte,
							q.currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_upper_equal_error, strTilte,
							q.currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_NO_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_no_equal_error, strTilte,
							q.currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_UPPER:
					msg.obj = getResources().getString(
							R.string.date_upper_error, strTilte,
							q.currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW_EQUAL:
					msg.obj = getResources().getString(
							R.string.date_low_equal_error, strTilte,
							q.currIndex + "", preTitle, (preIndex + 1) + "");
					break;
				case STATE_SYB_DATE_LOW:
					msg.obj = getResources().getString(R.string.date_low_error,
							strTilte, q.currIndex + "", preTitle,
							(preIndex + 1) + "");
					break;
				case STATE_SYB_LEN_EQUAL:
					Log.i("STATE_SYB_LEN_EQUAL222", "strTilte=" + strTilte
							+ "q.currIndex=" + q.currIndex + "strLen=" + strLen);
					msg.obj = getResources().getString(
							R.string.len_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_UPPER_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_upper_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_NO_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_no_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_UPPER:
					msg.obj = getResources().getString(
							R.string.len_upper_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_LOW_EQUAL:
					msg.obj = getResources().getString(
							R.string.len_low_equal_error, strTilte,
							q.currIndex + "", strLen);
					break;
				case STATE_SYB_LEN_LOW:
					msg.obj = getResources().getString(R.string.len_low_error,
							strTilte, q.currIndex + "", strLen);
					break;
				case FORMAT_NO_FLOAT_NUMBER:
					msg.obj = getResources().getString(
							R.string.format_no_float_number, q.currIndex + "");
					break;
				case STATE_SYB_SUM_VALUE:
					msg.obj = getResources().getString(R.string.question_sum,
							q.freeSymbol + q.freeSumNumber);
					break;
				case FORMAT_NO_NUMBER_AND_WORD:
					msg.obj = getResources().getString(
							R.string.format_no_word_and_number,
							q.currIndex + "");
					break;
				case FORMAT_NO_EMAIL:
					msg.obj = getResources().getString(
							R.string.format_no_email, q.currIndex + "");
					break;
				case ITEM_NO_REQUIRED:
					msg.obj = getResources().getString(
							R.string.item_no_required, q.currIndex + "");
					break;
				/**
				 * 题外关联之和 提示语 的展现 ！ 大树 外部关联 4
				 */
				case QUESTION_OUT_YING_RELEVANCE:
					msg.obj = getString(R.string.question_outying_error,
							relevanceIndex, relevanceOptionIndex + "");
					break;
				/**
				 * 题外关联 之和 内部关联 大树 内部关联 15
				 */
				case QUESTION_INTERIOR_RELEVANCE:
					msg.obj = getString(R.string.question_innerying_error,
							sumSign);
					break;
				/**
				 * IMS临时干预价格 小数点位数为 2
				 */
				case DECIMAL_PLACES_NO_MEET:
					msg.obj = getString(R.string.decimel_places_no_meet);
					break;
				/**
				 * 大树排序 选项值不能重复
				 */
				case QUESTION_ITEM_ORDER_REPEAT:
					msg.obj = getString(R.string.sequence_repeat);
					break;
				case LUO_JI_WRONG: // IMS 皮炎湿疹
					msg.obj = getString(R.string.luoji_wrong);
					break;
				}
				// handler.sendMessage(msg);
				// Log.i("@@@", "1qAnswer.returnType=" + qAnswer.returnType);
				qAnswer.setAnswerMapArr(answerMapArr);
				qAnswer.surveyId = feed.getSurveyId();
				qAnswer.userId = feed.getUserId();
				qAnswer.enable = 1;
				qAnswer.answerType = q.qType;
				qAnswer.qIndex = q.qIndex;
				qAnswer.qOrder = q.qOrder;
				// int i = 0;
				if (null != tempAnswer) {
					ma.dbService.updateAnswer(qAnswer);
					System.out.println("更新答案1");
				} else {
					ma.dbService.addAnswer(qAnswer);
					System.out.println("插入答案1");
				}
				return qAnswer.returnType;
			}
			if (q.isNew) {
				if (1 == q.qRequired && syb == STATE_NOTHING
						&& q.qType == Cnt.TYPE_FREE_TEXT_BOX) {
					qAnswer.setAnswerMapArr(answerMapArr);
					qAnswer.surveyId = feed.getSurveyId();
					qAnswer.userId = feed.getUserId();
					qAnswer.enable = 1;
					qAnswer.answerType = q.qType;
					qAnswer.qIndex = q.qIndex;
					qAnswer.qOrder = q.qOrder;
					// int i = 0;
					if (null != tempAnswer) {
						ma.dbService.updateAnswer(qAnswer);
						BaseLog.w("qAnswer = " + qAnswer.toString());
						System.out.println("更新答案3");
					} else {
						ma.dbService.addAnswer(qAnswer);
						System.out.println("插入答案3");
					}
					ansertlist.add(qAnswer);
					return qAnswer.returnType;
				} else {
					/**** 数字之和开始 **/
					// 看看数值题目的文本题是否是小于号
					if ("=".equals(q.freeSymbol)) {
						// 假如总值不小于最大值并且没有其他逻辑错误 提示值必须大于
						if (!(sum == Double.parseDouble(q.freeSumNumber))) {
							syb = STATE_SYB_SUM_VALUE;
							qAnswer.returnType = STATE_SYB_SUM_VALUE;
						}
					} else if ("<=".equals(q.freeSymbol)) {
						// 假如总值不小于等于最大值并且没有其他逻辑错误
						if (!(sum <= Double.parseDouble(q.freeSumNumber))) {
							syb = STATE_SYB_SUM_VALUE;
							qAnswer.returnType = STATE_SYB_SUM_VALUE;
						}
					} else if (">=".equals(q.freeSymbol)) {
						// 假如总值不大于等于最大值并且没有其他逻辑错误
						if (!(sum >= Double.parseDouble(q.freeSumNumber))) {
							syb = STATE_SYB_SUM_VALUE;
							qAnswer.returnType = STATE_SYB_SUM_VALUE;
						}
					}
					if (qAnswer.returnType == STATE_SYB_SUM_VALUE) {
						Message msg = Message.obtain();
						msg.what = qAnswer.returnType;
						msg.obj = getResources().getString(
								R.string.question_sum,
								q.freeSymbol + q.freeSumNumber);
						q.currIndex = 0;
						sum = 0;
						// handler.sendMessage(msg);
						qAnswer.setAnswerMapArr(answerMapArr);
						qAnswer.surveyId = feed.getSurveyId();
						qAnswer.userId = feed.getUserId();
						qAnswer.enable = 1;
						qAnswer.answerType = q.qType;
						qAnswer.qIndex = q.qIndex;
						qAnswer.qOrder = q.qOrder;
						// int i = 0;
						if (null != tempAnswer) {
							ma.dbService.updateAnswer(qAnswer);
							
							
							
							System.out.println("更新答案3");
						} else {
							ma.dbService.addAnswer(qAnswer);
							System.out.println("插入答案3");
						}
						ansertlist.add(qAnswer);
						return qAnswer.returnType;
					}
				}
			}
			
		} else {
			qAnswer.returnType = Cnt.STATE_SUCCESS;
		}
		if ((!Util.isEmpty(answerMapArr) || 1 != q.qRequired)
				&& Cnt.STATE_FAIL != qAnswer.returnType) {
			qAnswer.returnType = Cnt.STATE_SUCCESS;
		}
		qAnswer.setAnswerMapArr(answerMapArr);
		// for (int i = 0; i < answerMapArr.size(); i++) {
		// Log.i("setAnswerValue2", answerMapArr.get(i).toString());
		// }

		qAnswer.surveyId = feed.getSurveyId();
		qAnswer.userId = feed.getUserId();
		qAnswer.enable = 1;
		qAnswer.answerType = q.qType;
		qAnswer.qIndex = q.qIndex;
		qAnswer.qOrder = q.qOrder;
		// int i = 0;
		if (null != tempAnswer) {
			ma.dbService.updateAnswer(qAnswer);
			Log.i("setAnswerValue2", qAnswer.toString());
			System.out.println("更新答案4");
		} else {
			ma.dbService.addAnswer(qAnswer);
			System.out.println("插入答案4");
		}
		return qAnswer.returnType;
	}

	// 数据字典触摸
	private EditText etData;

	private final class DataTouchListener implements OnTouchListener {

		private String classId;
		private EditText et;

		public DataTouchListener(String classId, EditText et) {
			this.classId = classId;
			this.et = et;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				if (!Util.isEmpty(classId)) {
					etData = this.et;
					Intent intent = new Intent(NativeModeNoPageActivity.this,
							EditTextListView.class);
					intent.putExtra("classId", classId);
					startActivityForResult(intent, 1);
				}
			}
			return false;
		}

	}

	class CountryItemSelectedListener implements OnItemSelectedListener {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public CountryItemSelectedListener(Question q, ArrayList<View> vs,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.q = q;
			this.vs = vs;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// Log.i("@@@", "选中三级联获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = this.position;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	class CityItemSelectedListener implements OnItemSelectedListener {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public CityItemSelectedListener(Question q, ArrayList<View> vs,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.q = q;
			this.vs = vs;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (vs.size() >= 3) {
				QuestionItem item = (QuestionItem) vs.get(2).getTag();
				setCountryAdapter(position, item);
			}
			// Log.i("@@@", "选中三级联获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = this.position;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	class ProvinceItemSelectedListener implements OnItemSelectedListener {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public ProvinceItemSelectedListener(Question q, ArrayList<View> vs,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.q = q;
			this.vs = vs;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (vs.size() >= 2) {
				QuestionItem item = (QuestionItem) vs.get(1).getTag();
				setCityAdapter(position, item);
			}
			// Log.i("@@@", "选中三级联获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = this.position;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	class MyOnItemSelectedListener implements OnItemSelectedListener {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public MyOnItemSelectedListener(Question q, ArrayList<View> vs,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.q = q;
			this.vs = vs;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// Log.i("@@@", "选中文本下拉获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = this.position;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * edittext 文本变化监听器.
	 * 
	 * @author Administrator
	 * 
	 */
	class EditChangedListener implements TextWatcher {
		private Question q;
		private ArrayList<View> vs;
		private ArrayList<QuestionItem> mTempRows;
		private int position;

		public EditChangedListener(Question q, ArrayList<View> vs,
				ArrayList<QuestionItem> mTempRows, int position) {
			this.q = q;
			this.vs = vs;
			this.mTempRows = mTempRows;
			this.position = position;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.i("@@@", "选中文本框获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		}
	}

	private final class OutDayTouchListener implements OnTouchListener {

		private Context c;
		private EditText et;

		private int type;

		public OutDayTouchListener(Context c, EditText et, int dateType) {
			this.c = c;
			this.et = et;
			this.type = dateType;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				// 设置出院日期

				// showDateTimePicker(1); 日期样式修改
				Util.showDateTimePicker(c, et, type, screenWidth, screenHeight);
				// 日期控件样式更改
				// Util.changeDateStyle(c, et, type);
			}
			return false;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isFinishProgress) {
			if (KeyEvent.KEYCODE_BACK == keyCode) {
				if (sdImages.isOpened()) {
					return true;
				}
				if (View.VISIBLE == vPopupLL.getVisibility()) {
					vPopupLL.setVisibility(View.GONE);
					vPopupTv.setText("");
					vPopupTv.setVisibility(View.GONE);
					return true;
				}
				if (View.VISIBLE != vResult.getVisibility()) {
					if (isRecording) {// 如果是正在录音则等待0.1秒
						closeQRecord();
						new Handler().postDelayed(new Runnable() {
							public void run() {
								// execute the task
								isShow = false;
								handler.sendEmptyMessage(MSG_SAVE);
							}
						}, 100);
					} else {// 否则直接下一页
						isShow = false;
						handler.sendEmptyMessage(MSG_SAVE);
					}
				}
				return true;
			}
		} else {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	int inner = 0;
	private boolean openFalse = false;

	// private final class RecordTask extends AsyncTask<Void, Integer, Boolean>
	// {
	//
	// public boolean click;
	// private String num;
	//
	// public RecordTask(boolean isClick, String number) {
	// this.click = isClick;
	// this.num = number;
	// }
	//
	// // 大树动画 添加构造方法 用于切换图片 8
	// private ImageView recordIv;
	//
	// public RecordTask(boolean click, String num, ImageView recordIv) {
	// super();
	// this.click = click;
	// this.num = num;
	// this.recordIv = recordIv;
	// }
	//
	// // 大树动画 添加构造
	// @Override
	// protected Boolean doInBackground(Void... params) {
	// if (!this.click) {
	// /**
	// * 录音
	// */
	// String path = "";
	// inner = 0;
	// // 注释部分
	// // if (ma.cfg.getBoolean("save_inner", false)) {
	// // path = Util.getRecordInnerPath(NativeModeNoPageActivity.this,
	// // feed.getSurveyId());
	// // } else {
	// // path = Util.getRecordPath(feed.getSurveyId());
	// // inner = 1;
	// // }
	// /**
	// * 录音地址变更 如果存在 SDCARD 并且 大小有100M 的空间
	// */
	// if (Util.readSDCard()[1] >= 0.1) {
	// // 存到本地 SDcard 中
	// Log.i(TAG, "剩余空间大小：G" + Util.readSDCard()[1]);
	// path = Util.getRecordPath(feed.getSurveyId());
	// inner = 1;
	// } else {
	// // 存到 系统内置卡 里头
	// path = Util.getRecordInnerPath(NativeModeNoPageActivity.this,
	// feed.getSurveyId());
	// }
	//
	// // 增加pid 命名规则
	// recordFile = new File(path, // path
	// Util.getRecordName(feed.getUserId(), feed.getSurveyId(),
	// Cnt.FILE_TYPE_MP3, feed.getUuid(),
	// null, feed.getPid(), feed.getParametersContent(), (1) + ""));
	// if (!recordFile.getParentFile().exists()) {
	// recordFile.getParentFile().mkdirs();
	// }
	//
	// try {
	// mRecorder = new MediaRecorder();
	// mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
	// mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	// mRecorder.setOutputFile(recordFile.getAbsolutePath());
	// mRecorder.prepare();
	// mRecorder.start();
	// // ma.dbService.addRecord(//
	// // feed.getUserId(), //
	// // feed.getSurveyId(), //
	// // feed.getUuid(), //
	// // recordFile.getParent(), //
	// // recordFile.getName(), //
	// // System.currentTimeMillis(), //
	// // Cnt.FILE_TYPE_MP3, //
	// // num, //
	// // inner, feed.getFeedId());
	// } catch (Exception e) {
	// e.printStackTrace();
	// /**
	// * 大树录音 捕获异常 处理
	// */
	// openFalse = true;
	// System.out.println("录音开启异常");
	// }
	// isClicked = true;
	// } else {
	// /**
	// * 停止录音
	// */
	// try {
	// if (null != recordFile && null != mRecorder) {
	// mRecorder.stop();
	// mRecorder.release();
	// mRecorder = null;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// /**
	// * 大树录音 捕获异常 处理
	// */
	// System.out.println("录音关闭异常");
	// }
	// isClicked = false;
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// /**
	// * 开始录音,网数据库中插入数据
	// */
	// // if (!this.click) {
	// // // if (miRecode != null) {
	// // // miRecode.setIcon(R.drawable.audio_busy);
	// // // }
	// // // 大树动画 录音图片切换 9
	// // if (recordIv != null) {
	// // recordIv.setImageResource(R.drawable.audio_busy_2);
	// // }
	// // Toasts.makeText(NativeModeNoPageActivity.this, R.string.record_open,
	// Toast.LENGTH_SHORT).show();
	// // }
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// super.onPostExecute(result);
	// if (this.click) {
	// /**
	// * 关闭录音
	// */
	// // if (miRecode != null) {
	// // miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
	// // }
	// // 大树动画 录音图片切换 10
	// if (recordIv != null) {
	// recordIv.setImageResource(R.drawable.ic_btn_speak_now_2);
	// }
	// Toasts.makeText(NativeModeNoPageActivity.this, R.string.record_close,
	// Toast.LENGTH_SHORT).show();
	// ma.dbService.updateRecord(recordFile.getName(),
	// System.currentTimeMillis(), recordFile.length());
	// } else {
	// if (null != mRecorder) {
	// mRecorder.stop();
	// mRecorder.release();
	// mRecorder = null;
	// }
	// if (!openFalse && null != recordFile && 0 < recordFile.length()) {
	// if (!this.click) {
	// // 录音已经点击 为 真
	// try {
	// if (!recordFile.getParentFile().exists()) {
	// recordFile.getParentFile().mkdirs();
	// }
	// mRecorder = new MediaRecorder();
	// mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
	// mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	// mRecorder.setOutputFile(recordFile.getAbsolutePath());
	// mRecorder.prepare();
	// mRecorder.start();
	// ma.dbService.addRecord(//
	// feed.getUserId(), //
	// feed.getSurveyId(), //
	// feed.getUuid(), //
	// recordFile.getParent(), //
	// recordFile.getName(), //
	// System.currentTimeMillis(), //
	// Cnt.FILE_TYPE_MP3, //
	// num, //
	// inner, feed.getFeedId());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// // if (miRecode != null) {
	// // miRecode.setIcon(R.drawable.audio_busy);
	// // }
	// // 大树动画 录音图片切换 9
	// if (recordIv != null) {
	// recordIv.setImageResource(R.drawable.audio_busy_2);
	// }
	// }
	// Toasts.makeText(NativeModeNoPageActivity.this, R.string.record_open,
	// Toast.LENGTH_SHORT).show();
	// } else {
	// if (null != recordFile) {
	// recordFile.delete();
	// }
	// isClicked = false;
	// try {
	// if (null != mRecorder) {
	// mRecorder.stop();
	// mRecorder.release();
	// mRecorder = null;
	// }
	// } catch (Exception e) {
	// System.out.println("录音关闭异常");
	// // TODO: handle exception
	// }
	// Toasts.makeText(NativeModeNoPageActivity.this,
	// R.string.record_open_false, Toast.LENGTH_LONG)
	// .show();
	// if (View.VISIBLE != vResult.getVisibility()) {
	// if (1 == feed.getIsCompleted()) {
	// handler.sendEmptyMessage(MSG_WRITE);
	// } else {
	// isShow = false;
	// handler.sendEmptyMessage(MSG_SAVE);
	// }
	// }
	// }
	//
	// }
	// }
	//
	// };
	private final class QRecordTask extends AsyncTask<Void, Integer, Boolean> {

		public boolean click;
		private String num;
		private Question q;

		public QRecordTask(boolean isClick, String number, Question q) {
			this.click = isClick;
			this.num = number;
			this.q = q;
		}

		// 大树动画 添加构造方法 用于切换图片 8
		private ImageView recordIv;

		public QRecordTask(boolean click, String num, ImageView recordIv,
				Question q) {
			super();
			this.click = click;
			this.num = num;
			this.recordIv = recordIv;
			this.q = q;
		}

		// 大树动画 添加构造
		@Override
		protected Boolean doInBackground(Void... params) {
			if (!this.click) {
				/**
				 * 录音
				 */
				if (CheckAudioPermission
						.isHasPermission(NativeModeNoPageActivity.this)) {
					String path = "";
					int inner = 0;
					// 注释部分
					// if (ma.cfg.getBoolean("save_inner", false)) {
					// path = Util.getRecordInnerPath(NativeModeActivity.this,
					// feed.getSurveyId());
					// } else {
					// path = Util.getRecordPath(feed.getSurveyId());
					// inner = 1;
					// }
					/**
					 * 录音地址变更 如果存在 SDCARD 并且 大小有100M 的空间
					 */
					if (Util.readSDCard()[1] >= 0.1) {
						// 存到本地 SDcard 中
						Log.i(TAG, "剩余空间大小：G" + Util.readSDCard()[1]);
						path = Util.getRecordPath(feed.getSurveyId());
						inner = 1;
					} else {
						// 存到 系统内置卡 里头
						path = Util.getRecordInnerPath(
								NativeModeNoPageActivity.this,
								feed.getSurveyId());
					}

					// 增加pid 命名规则
					recordFile = new File(path, // path
							Util.getRecordName(getQid(q), feed.getUserId(),
									feed.getSurveyId(), Cnt.FILE_TYPE_MP3,
									feed.getUuid(), num, feed.getPid(),
									feed.getParametersContent(), (q.qOrder + 1)
											+ ""));
					if (!recordFile.getParentFile().exists()) {
						recordFile.getParentFile().mkdirs();
					}

					if (mRecorder == null) {
						mRecorder = new MediaRecorder();
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						mRecorder.setOutputFile(recordFile.getAbsolutePath());
						try {
							mRecorder.prepare();
							mRecorder.start();
						} catch (Exception e) {
							e.printStackTrace();
							/**
							 * 大树录音 捕获异常 处理
							 */
							openFalse = true;
							System.out.println("录音开启异常");
							// ma.uncaughtException(new
							// Thread(Thread.currentThread().getName()), e);
						}
					} else {
						stop();
						mRecorder = new MediaRecorder();
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						mRecorder.setOutputFile(recordFile.getAbsolutePath());
						try {
							mRecorder.prepare();
							mRecorder.start();
						} catch (Exception e) {
							e.printStackTrace();
							/**
							 * 大树录音 捕获异常 处理
							 */
							openFalse = true;
							System.out.println("录音开启异常");
							// ma.uncaughtException(new
							// Thread(Thread.currentThread().getName()), e);
						}
					}
				} else {
					openFalse = true;
					System.out.println("录音开启异常");
				}
				isRecording = true;
			} else {
				// 停止录音
				stop();
				isRecording = false;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (this.click) {
				/**
				 * 关闭录音
				 */
				// if (miRecode != null) {
				// miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
				// }
				// 大树动画 录音图片切换 10
				if (recordIv != null) {
					recordIv.setBackgroundResource(R.drawable.record_on);
				}
				Toasts.makeText(NativeModeNoPageActivity.this,
						R.string.record_close, Toast.LENGTH_SHORT).show();
				ma.dbService.updateRecord(recordFile.getName(),
						System.currentTimeMillis(), recordFile.length());
			} else {
				if (!openFalse && null != recordFile && 0 < recordFile.length()) {
					ma.dbService.addRecord(//
							feed.getUserId(), //
							feed.getSurveyId(), //
							feed.getUuid(), //
							recordFile.getParent(), //
							recordFile.getName(), //
							System.currentTimeMillis(), //
							Cnt.FILE_TYPE_MP3, //
							num, //
							inner, feed.getFeedId());
					if (!this.click) {
						// if (miRecode != null) {
						// miRecode.setIcon(R.drawable.audio_busy);
						// }
						// 大树动画 录音图片切换 9
						ivRecord = recordIv;
						if (recordIv != null) {
							recordIv.setBackgroundResource(R.drawable.record_off);
						}
					}
					/**
					 * 文本域题若无答案附加默认提示
					 */
					if (Cnt.TYPE_FREE_TEXT_AREA == q.qType) {// 文本域
						int success = getQuestionAnswer( false, q,
								vsRecord.get(q.qIndex), null, mTempRowsRecord,
								true);
					}
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open, Toast.LENGTH_SHORT).show();
				} else {
					stop();
					isRecording = false;
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open_false, Toast.LENGTH_SHORT)
							.show();
					if (View.VISIBLE != vResult.getVisibility()) {
						if (1 == feed.getIsCompleted()) {
							handler.sendEmptyMessage(MSG_WRITE);
						} else {
							isShow = false;
							handler.sendEmptyMessage(MSG_SAVE);
						}
					}
				}

			}
		}

	}

	private final class RecordTask extends AsyncTask<Void, Integer, Boolean> {

		public boolean click;
		private String num;

		public RecordTask(boolean isClick, String number) {
			this.click = isClick;
			this.num = number;
		}

		// 大树动画 添加构造方法 用于切换图片 8
		private ImageView recordIv;

		public RecordTask(boolean click, String num, ImageView recordIv) {
			super();
			this.click = click;
			this.num = num;
			this.recordIv = recordIv;
		}

		// 大树动画 添加构造
		@Override
		protected Boolean doInBackground(Void... params) {
			if (!this.click) {
				/**
				 * 录音
				 */
				String path = "";
				int inner = 0;
				// 注释部分
				// if (ma.cfg.getBoolean("save_inner", false)) {
				// path = Util.getRecordInnerPath(NativeModeNoPageActivity.this,
				// feed.getSurveyId());
				// } else {
				// path = Util.getRecordPath(feed.getSurveyId());
				// inner = 1;
				// }
				/**
				 * 录音地址变更 如果存在 SDCARD 并且 大小有100M 的空间
				 */
				if (Util.readSDCard()[1] >= 0.1) {
					// 存到本地 SDcard 中
					Log.i(TAG, "剩余空间大小：G" + Util.readSDCard()[1]);
					path = Util.getRecordPath(feed.getSurveyId());
					inner = 1;
				} else {
					// 存到 系统内置卡 里头
					path = Util.getRecordInnerPath(
							NativeModeNoPageActivity.this, feed.getSurveyId());
				}

				// 增加pid 命名规则
				recordFile = new File(path, // path
						Util.getRecordName(feed.getUserId(),
								feed.getSurveyId(), Cnt.FILE_TYPE_MP3,
								feed.getUuid(), null, feed.getPid(),
								feed.getParametersContent(), (1) + ""));
				if (!recordFile.getParentFile().exists()) {
					recordFile.getParentFile().mkdirs();
				}

				if (mRecorder == null) {
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					mRecorder
							.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					mRecorder.setOutputFile(recordFile.getAbsolutePath());
					try {
						mRecorder.prepare();
						mRecorder.start();
					} catch (Exception e) {
						e.printStackTrace();
						/**
						 * 大树录音 捕获异常 处理
						 */
						openFalse = true;
						System.out.println("录音开启异常");
						// ma.uncaughtException(new
						// Thread(Thread.currentThread().getName()), e);
					}
				} else {
					stop();
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					mRecorder
							.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					mRecorder.setOutputFile(recordFile.getAbsolutePath());
					try {
						mRecorder.prepare();
						mRecorder.start();
					} catch (Exception e) {
						e.printStackTrace();
						/**
						 * 大树录音 捕获异常 处理
						 */
						openFalse = true;
						System.out.println("录音开启异常");
						// ma.uncaughtException(new
						// Thread(Thread.currentThread().getName()), e);
					}

				}
				isClicked = true;
			} else {
				// 停止录音
				stop();
				isClicked = false;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/**
			 * 开始录音,网数据库中插入数据
			 */
			// if (!this.click) {
			// // if (miRecode != null) {
			// // miRecode.setIcon(R.drawable.audio_busy);
			// // }
			// // 大树动画 录音图片切换 9
			// if (recordIv != null) {
			// recordIv.setImageResource(R.drawable.audio_busy_2);
			// }
			// Toasts.makeText(NativeModeNoPageActivity.this,
			// R.string.record_open, Toast.LENGTH_SHORT).show();
			// }
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (this.click) {
				/**
				 * 关闭录音
				 */
				// if (miRecode != null) {
				// miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
				// }
				// 大树动画 录音图片切换 10
				if (recordIv != null) {
					recordIv.setImageResource(R.drawable.ic_btn_speak_now_2);
				}
				Toasts.makeText(NativeModeNoPageActivity.this,
						R.string.record_close, Toast.LENGTH_SHORT).show();
				ma.dbService.updateRecord(recordFile.getName(),
						System.currentTimeMillis(), recordFile.length());
			} else {
				if (!openFalse && null != recordFile && 0 < recordFile.length()) {
					ma.dbService.addRecord(//
							feed.getUserId(), //
							feed.getSurveyId(), //
							feed.getUuid(), //
							recordFile.getParent(), //
							recordFile.getName(), //
							System.currentTimeMillis(), //
							Cnt.FILE_TYPE_MP3, //
							num, //
							inner, feed.getFeedId());
					if (!this.click) {
						// if (miRecode != null) {
						// miRecode.setIcon(R.drawable.audio_busy);
						// }
						// 大树动画 录音图片切换 9
						if (recordIv != null) {
							recordIv.setImageResource(R.drawable.audio_busy_2);
						}
					}
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open, Toast.LENGTH_SHORT).show();
				} else {
					stop();
					isClicked = false;
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open_false, Toast.LENGTH_LONG)
							.show();
					if (View.VISIBLE != vResult.getVisibility()) {
						if (1 == feed.getIsCompleted()) {
							handler.sendEmptyMessage(MSG_WRITE);
						} else {
							isShow = false;
							handler.sendEmptyMessage(MSG_SAVE);
						}
					}
				}

			}
		}

	};

	// 结束录音
	public void stop() {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
			} catch (Exception e) {
				// TODO 如果当前java状态和jni里面的状态不一致，
				// e.printStackTrace();
				mRecorder = null;
				mRecorder = new MediaRecorder();
			}
			mRecorder.release();
			mRecorder = null;
		}
	}

	/**
	 * 最总写入
	 * 
	 * @param str
	 * @return
	 */
	private Integer writeXML() {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setVernum(getVersion());
		versionInfo.setSurupdate(feed.getSurvey().getLastGeneratedTime());
		versionInfo.setDeviceBrand(SystemUtil.getDeviceBrand());
		versionInfo.setSystemModel(SystemUtil.getSystemModel());
		versionInfo.setSystemVersion(SystemUtil.getSystemVersion());
		versionInfo.setVerifyFlag(verifyFlag);
		// str = "";
		int result = 0;
		/**
		 * 0空串 1写入失败 2js有问题 3成功
		 */
		feed.setRegTime(System.currentTimeMillis());
		if (Util.isEmpty(feed.getFeedId())) {
			feed.setFeedId("0");
		}
		String first = Util.getTime(feed.getCreateTime(), 5);
		String start = Util.getTime(feed.getStartTime(), 5);
		String end = Util.getTime(feed.getRegTime(), 5);
		if (!Util.isEmpty(feed.getManyTimes())) {
			if (!feed.getManyTimes().contains(start + "," + end)) {
				feed.setManyTimes(feed.getManyTimes() + start + "," + end + ";");
				feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed
						.getStartTime())));
			}
		} else {
			feed.setManyTimes(start + "," + end + ";");
			feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed
					.getStartTime())));
		}
		
		
//		Gps gpsbean = ma.dbService.queryPos(feed.getName());
//		feed.setLat(gpsbean.getLatitude()+"");
//		feed.setLng(gpsbean.getLongitude()+"");
//		feed.setLotsCoord(gpsbean.getLotscoord());
//		feed.setVisitAddress(gpsbean.getAdd());
		
		
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("startdate", first);
		hMap.put("RegDate", end);
		hMap.put("UserAgent", feed.getUuid());
		hMap.put("VisitorID", feed.getUserId());
		hMap.put("FeedID", feed.getFeedId());
		hMap.put("VisitTimes", feed.getSpent() + "");
		hMap.put("lat", feed.getLat());
		hMap.put("lng", feed.getLng());
		hMap.put("VisitAddress", feed.getVisitAddress());
		// 大树拒访 设置状态在这里体现 访问状态
		if (returnTypeId != -2) {
			// 2代表拒绝访问 大树拒访
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", returnTypeId + "");
		} else {
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", Cnt.VISIT_STATE_COMPLETED + "");
		}
		returnTypeId = -2;
		// 以上部分拒访
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));
		hMap.put("RandomOption", groupSequence);

		if (-1 == feed.getSurvey().eligible) {
			// Log.i("_test", "-1 == feed.getSurvey().eligible");
			hMap.put("CheckFormName", "");
		} else {
			Question q = ma.dbService.getQuestion(feed.getSurveyId(),
					String.valueOf(feed.getSurvey().eligible));
			if (null == q) {
				// Log.i("_test", "null==q");
				hMap.put("CheckFormName", "");
			} else {
				if (Cnt.TYPE_RADIO_BUTTON == q.qType) {
					// Log.i("_test", "Cnt.TYPE_RADIO_BUTTON == q.qType");
					hMap.put("CheckFormName",
							Util.GetAnswerName(q, null, 0, 0, false, false));
				} else {
					if (Util.isEmpty(q.getRowItemArr())) {
						// Log.i("_test", "Util.isEmpty(q.getRowItemArr())");
						hMap.put("CheckFormName", "");
					} else {
						// Log.i("_test", "!Util.isEmpty(q.getRowItemArr())");
						StringBuilder sb = new StringBuilder("");
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (i == (q.getRowItemArr().size() - 1)) {
								sb.append(Util.GetAnswerName(q, qi, 0, 0,
										false, false));
							} else {
								sb.append(
										Util.GetAnswerName(q, qi, 0, 0, false,
												false)).append(",");
							}
						}
						hMap.put("CheckFormName", sb.toString());
					}
				}
			}
		}

		/**
		 * 内部名单信息
		 */
		if (1 == feed.getSurvey().openStatus) {
			HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
			Iterator<Entry<String, Parameter>> it = hm.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Parameter> entry = it.next();
				hMap.put(entry.getKey(), entry.getValue().getContent());
			}
		}

		/**
		 * 每一次的坐标点
		 */
		if (!Util.isEmpty(feed.getLotsCoord())) {
			feed.setLotsCoord(feed.getLotsCoord() + feed.getLat() + ","
					+ feed.getLng() + ";");
		} else {
			feed.setLotsCoord(feed.getLat() + "," + feed.getLng() + ";");
		}

		/**
		 * 每一次的访问位置
		 */
		if (!Util.isEmpty(feed.getManyPlaces())) {
			feed.setManyPlaces(feed.getManyPlaces() + ","
					+ feed.getVisitAddress());
		} else {
			feed.setManyPlaces(feed.getVisitAddress());
		}
		// todo
		ArrayList<UploadFeed> tempfs = ma.dbService.getEmptyRecordList();
		for (int ii = 0; ii < tempfs.size(); ii++) {
			UploadFeed tempUf = tempfs.get(ii);
			String name = tempUf.getName();
			String path = tempUf.getPath();
			File tempFile = new File(path, name);
			long realLenth = tempFile.length();
			if (realLenth > 0) {
				// 更新已经有的录音文件
				ma.dbService.updateRecord(name, System.currentTimeMillis(),
						realLenth);
			} else {
				// 放弃该录音文件
				ma.dbService.updateRecordGiveUp(name);
			}
		}
		// 获取所有音视频文件
		ArrayList<UploadFeed> fs = ma.dbService.getRecordList(feed.getUuid(),
				feed.getSurveyId());
		// feed.setName(name);
		ArrayList<Answer> aswList = ma.dbService.getUserAllAnswer(feed
				.getUuid());
		/** 青岛逻辑次数跳转验证 */
		boolean isLogicList = false;
		if (logicList != null) {
			isLogicList = true;
		}
		hMap.put("logicJumpsFlag", isLogicList + "");
		// 命名规则存附件地址
		boolean b = fm.write2Xml(feed.getPath(), feed.getName(), hMap, aswList,
				fs, feed.getManyTimes(), feed.getLotsCoord(),
				feed.getManyPlaces(), true, feed.getSurveyId(), feed.getPid(),
				versionInfo,isbos,uploadtype);
		if (b) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
			result = 2;
		} else {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 1;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		if (Util.isEmpty(aswList)) {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 2;
		}
		String strDirectory = "";
		for (int directory : dtOrderList) {
			strDirectory += directory + ",";
		}
		// 设置目录
		feed.setDirectory(strDirectory);
		// 大树拒访 设置完成状态 如果拒访 那么 完成 状态 访问状态
		if (feed.getReturnTypeId() != -2) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
		}
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	/**
	 * 中间保存
	 * 
	 * @param str
	 * @return
	 */
	private Integer saveXML() {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setVernum(getVersion());
		versionInfo.setSurupdate(feed.getSurvey().getLastGeneratedTime());
		versionInfo.setDeviceBrand(SystemUtil.getDeviceBrand());
		versionInfo.setSystemModel(SystemUtil.getSystemModel());
		versionInfo.setSystemVersion(SystemUtil.getSystemVersion());
		versionInfo.setVerifyFlag(verifyFlag);
		// str = "";
		int result = 0;
		feed.setRegTime(System.currentTimeMillis());
		if (Util.isEmpty(feed.getFeedId())) {
			feed.setFeedId("0");
		}
		String first = Util.getTime(feed.getCreateTime(), 5);
		String start = Util.getTime(feed.getStartTime(), 5);
		String end = Util.getTime(feed.getRegTime(), 5);
		if (!Util.isEmpty(feed.getManyTimes())) {
			if (!feed.getManyTimes().contains(start + "," + end)) {
				feed.setManyTimes(feed.getManyTimes() + start + "," + end + ";");
				feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed
						.getStartTime())));
			}
		} else {
			feed.setManyTimes(start + "," + end + ";");
			feed.setSpent((feed.getSpent() + (feed.getRegTime() - feed
					.getStartTime())));
		}
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("startdate", first);
		hMap.put("RegDate", end);
		hMap.put("UserAgent", feed.getUuid());
		hMap.put("VisitorID", feed.getUserId());
		hMap.put("FeedID", feed.getFeedId());
		hMap.put("VisitTimes", feed.getSpent() + "");
		hMap.put("lat", feed.getLat());
		hMap.put("lng", feed.getLng());
		hMap.put("VisitAddress", feed.getVisitAddress());
		// 大树拒访
		if (returnTypeId != -2) {
			// 2代表拒绝访问 大树拒访
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", returnTypeId + "");
		} else {
			feed.setReturnTypeId(returnTypeId);
			hMap.put("VisitSate", Cnt.VISIT_STATE_INTERRUPT + "");
		}
		returnTypeId = -2;
		// 大树拒访
		// hMap.put("VisitSate", Cnt.VISIT_STATE_INTERRUPT + "");
		hMap.put("VersionCode", ma.versionCode);
		hMap.put("MacAddress", ma.macAddress);
		hMap.put("MapType", feed.getSurvey().mapType);
		hMap.put("VisitMode", String.valueOf(feed.getVisitMode()));
		hMap.put("RandomOption", groupSequence);

		if (-1 == feed.getSurvey().eligible) {
			// Log.i("_test", "-1 == feed.getSurvey().eligible");
			hMap.put("CheckFormName", "");
		} else {
			Question q = ma.dbService.getQuestion(feed.getSurveyId(),
					String.valueOf(feed.getSurvey().eligible));
			if (null == q) {
				// Log.i("_test", "null==q");
				hMap.put("CheckFormName", "");
			} else {
				if (Cnt.TYPE_RADIO_BUTTON == q.qType) {
					// Log.i("_test", "Cnt.TYPE_RADIO_BUTTON == q.qType");
					hMap.put("CheckFormName",
							Util.GetAnswerName(q, null, 0, 0, false, false));
				} else {
					if (Util.isEmpty(q.getRowItemArr())) {
						// Log.i("_test", "Util.isEmpty(q.getRowItemArr())");
						hMap.put("CheckFormName", "");
					} else {
						// Log.i("_test", "!Util.isEmpty(q.getRowItemArr())");
						StringBuilder sb = new StringBuilder("");
						for (int i = 0; i < q.getRowItemArr().size(); i++) {
							QuestionItem qi = q.getRowItemArr().get(i);
							if (i == (q.getRowItemArr().size() - 1)) {
								sb.append(Util.GetAnswerName(q, qi, 0, 0,
										false, false));
							} else {
								sb.append(
										Util.GetAnswerName(q, qi, 0, 0, false,
												false)).append(",");
							}
						}
						hMap.put("CheckFormName", sb.toString());
					}
				}
			}
		}

		/**
		 * 内部名单信息
		 */
		/**
		 * 内部名单信息
		 */
		if (1 == feed.getSurvey().openStatus) {
			HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
			Iterator<Entry<String, Parameter>> it = hm.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Parameter> entry = it.next();
				hMap.put(entry.getKey(), entry.getValue().getContent());
			}
		}

		/**
		 * 每一次的坐标点
		 */
		if (!Util.isEmpty(feed.getLotsCoord())) {
			feed.setLotsCoord(feed.getLotsCoord() + feed.getLat() + ","
					+ feed.getLng() + ";");
		} else {
			feed.setLotsCoord(feed.getLat() + "," + feed.getLng() + ";");
		}

		/**
		 * 每一次的访问位置
		 */
		if (!Util.isEmpty(feed.getManyPlaces())) {
			feed.setManyPlaces(feed.getManyPlaces() + ","
					+ feed.getVisitAddress());
		} else {
			feed.setManyPlaces(feed.getVisitAddress());
		}
		ArrayList<UploadFeed> tempfs = ma.dbService.getEmptyRecordList();
		for (int ii = 0; ii < tempfs.size(); ii++) {
			UploadFeed tempUf = tempfs.get(ii);
			String name = tempUf.getName();
			String path = tempUf.getPath();
			File tempFile = new File(path, name);
			long realLenth = tempFile.length();
			if (realLenth > 0) {
				// 更新已经有的录音文件
				ma.dbService.updateRecord(name, System.currentTimeMillis(),
						realLenth);
			} else {
				// 放弃该录音文件
				ma.dbService.updateRecordGiveUp(name);
			}
		}

		ArrayList<UploadFeed> fs = ma.dbService.getRecordList(feed.getUuid(),
				feed.getSurveyId());
		// feed.setName(name);
		ArrayList<Answer> aswList = ma.dbService.getUserAllAnswer(feed
				.getUuid());
		// 命名规则存附件地址
		boolean b = fm.write2Xml(feed.getPath(), feed.getName(), hMap, aswList,
				fs, feed.getManyTimes(), feed.getLotsCoord(),
				feed.getManyPlaces(), true, feed.getSurveyId(), feed.getPid(),
				versionInfo,isbos,uploadtype);
		if (b) {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 2;
		} else {
			feed.setIsCompleted(Cnt.VISIT_STATE_NOACCESS);
			result = 1;
		}
		File xml = new File(feed.getPath(), feed.getName());
		feed.setSize(xml.length());
		if (Util.isEmpty(aswList)) {
			feed.setIsCompleted(Cnt.VISIT_STATE_INTERRUPT);
			result = 2;
		}
		String strDirectory = "";
		for (int directory : dtOrderList) {
			strDirectory += directory + ",";
		}
		// 设置目录
		// feed.setDirectory(strDirectory);
		// 大树拒访 设置 拒访 如果拒访 ，那么完成 状态
		if (feed.getReturnTypeId() != -2) {
			feed.setIsCompleted(Cnt.VISIT_STATE_COMPLETED);
		}
		ma.dbService.updateUploadFeed(feed);
		return result;
	}

	/**
	 * 关闭录音
	 */
	public void closeRecored() {
		try {
			if (null != recordFile && null != mRecorder) {
				stop();
				// mRecorder.stop();
				// mRecorder.release();
				// mRecorder = null;
				isClicked = false;
				// 没更新上
				ma.dbService.updateRecord(recordFile.getName(),
						System.currentTimeMillis(), recordFile.length());
			}
			/**
			 * 关闭录音 走这里 在这里体现 全局录音
			 */
			if (null != MainService.recordFile && null != MainService.mRecorder) {
				MainService.stop();
				// MainService.mRecorder.stop();
				// MainService.mRecorder.release();
				// MainService.mRecorder = null;
				isClicked = false;
				ma.dbService.updateRecord(MainService.recordFile.getName(),
						System.currentTimeMillis(),
						MainService.recordFile.length());
			}
			// 关闭 录音
			// 设置组内序列号
			if (!Util.isEmpty(groupSequence)) {
				System.out.println("手动题组序列号:" + groupSequence);
				feed.setGroupSequence(groupSequence);
			}
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 大树录音 捕获异常 处理
			 */

		}
		isClicked = false;
	}

	private final class XmlTask extends AsyncTask<Void, Integer, Integer> {
		/**
		 * 是中间保存还是最终保存
		 */
		private int action;
		private String submitCode = "";

		public XmlTask(int saveAction) {
			this.action = saveAction;
		}

		/**
		 * 监控用，重新的构造方法
		 * 
		 * @param saveAction
		 */
		public XmlTask(int saveAction, String submitCode) {
			this.action = saveAction;
			this.submitCode = submitCode;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// show();
			if (isClicked) {
				/**
				 * 关闭录音
				 */
				if (miRecode != null) {
					miRecode.setIcon(android.R.drawable.ic_btn_speak_now);
				}
				Toasts.makeText(NativeModeNoPageActivity.this,
						R.string.record_close, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				if (null != recordFile && null != mRecorder) {
					stop();
					// mRecorder.stop();
					// mRecorder.release();
					// mRecorder = null;
					isClicked = false;
					// 没更新上
					ma.dbService.updateRecord(recordFile.getName(),
							System.currentTimeMillis(), recordFile.length());
				}
				/**
				 * 关闭录音 走这里 在这里体现 全局录音
				 */
				if (null != MainService.recordFile
						&& null != MainService.mRecorder) {
					MainService.stop();
					// MainService.mRecorder.stop();
					// MainService.mRecorder.release();
					// MainService.mRecorder = null;
					isClicked = false;
					ma.dbService.updateRecord(MainService.recordFile.getName(),
							System.currentTimeMillis(),
							MainService.recordFile.length());
				}
				// 关闭 录音
				// 设置组内序列号
				if (!Util.isEmpty(groupSequence)) {
					System.out.println("手动题组序列号:" + groupSequence);
					feed.setGroupSequence(groupSequence);
				}

				// 监控用 有网的情况。告知服务器退出 写xml文件 大树注销监控 2
				// if (NetUtil.checkNet(NativeModeNoPageActivity.this) &&
				// mapMonitor) {
				// if ("1".equals(submitCode)) {
				// // 提交退出
				// new MonitorTask(4, index, q).execute();
				// } else {
				// // 中断退出
				// new MonitorTask(3, realIndex, q).execute();
				// }
				// }
				// 监控用结束
			} catch (Exception e) {

				e.printStackTrace();
				/**
				 * 大树录音 捕获异常 处理
				 */
				// ma.uncaughtException(new
				// Thread(Thread.currentThread().getName()), e);
			}
			isClicked = false;
			if (MSG_WRITE == action) {
				return writeXML();
			} else {
				return saveXML();
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (0 == feed.getSurvey().appAutomaticUpload
					&& 1 == feed.getIsCompleted()
					&& !Util.isEmpty(ma.cfg.getString(Cnt.USER_PWD, ""))) {
				show();
				new NetTask(result, action).execute();
			} else {
				showResultView();
				TextView tvVisitTime = (TextView) vResult
						.findViewById(R.id.visit_time_tv);
				tvVisitTime.setText(getResources().getString(
						R.string.visit_time,
						Util.getTime(feed.getStartTime(), 5)));
				TextView tvVisitor = (TextView) vResult
						.findViewById(R.id.visitor_tv);
				tvVisitor.setText(getResources().getString(R.string.visitor,
						feed.getUserId()));
				TextView tvAddr = (TextView) vResult
						.findViewById(R.id.visit_addr_tv);
				tvAddr.setText(Util.isEmpty(feed.getVisitAddress())//
				? //
				getResources().getString(R.string.null_addr)//
						: //
						getResources().getString(R.string.visit_addr,
								feed.getVisitAddress()));
				TextView tvVisitState = (TextView) vResult
						.findViewById(R.id.visitor_state_tv);
				TextView tvUploadState = (TextView) vResult
						.findViewById(R.id.upload_state_tv);
				TextView tvUploadAlert = (TextView) vResult
						.findViewById(R.id.upload_alert_tv);
				tvUploadAlert.setTextColor(Color.RED);
				tvUploadAlert.setVisibility(View.GONE);
				String str = NativeModeNoPageActivity.this.getResources()
						.getString(R.string.upload_false);
				SpannableString ss = new SpannableString(str);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 6, str.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (9 == feed.getIsUploaded()) {
					str = NativeModeNoPageActivity.this.getResources()
							.getString(R.string.upload_true);
					ss = new SpannableString(str);
					ss.setSpan(new ForegroundColorSpan(Color.BLUE), 6,
							str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else if (1 == feed.getIsCompleted()) {
					tvUploadAlert.setVisibility(View.VISIBLE);
				}
				tvUploadState.setText(ss);
				adpter_text_size = (int) (UIUtils
						.getDimenPixelSize(R.dimen.adpter_text_size) * TextSizeManager
						.getInstance().getRealScale());
				Button btnLeft = (Button) vResult.findViewById(R.id.left_btn);
				Button btnRight = (Button) vResult.findViewById(R.id.right_btn);

				switch (result) {
				case 0:
				case 1:
					// tvVisitState.setTextColor(Color.RED);
					String str1 = NativeModeNoPageActivity.this.getResources()
							.getString(R.string.write_failure);
					SpannableString ss1 = new SpannableString(str1);
					ss1.setSpan(new ForegroundColorSpan(Color.RED), 6,
							str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tvVisitState.setText(ss1);
					// 大树拒访 拒绝访问
					if (feed.getReturnTypeId() != -2) {
						tvVisitState.setText(NativeModeNoPageActivity.this
								.getResources().getString(R.string.no_fang_mao,
										returnName));
					} else {
						tvVisitState.setText(ss1);
					}
					// 大树拒访
					btnLeft.setTextColor(Color.YELLOW);
					btnLeft.setText(NativeModeNoPageActivity.this
							.getResources().getString(R.string.giveup));
					btnRight.setTextColor(Color.GREEN);
					btnRight.setText(NativeModeNoPageActivity.this
							.getResources().getString(R.string.try_again));
					btnLeft.setOnClickListener(new ResultClickListener(1,
							action));
					btnRight.setOnClickListener(new ResultClickListener(1,
							action));
					btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							adpter_text_size);
					btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							adpter_text_size);
					// System.out.println("写入失败");
					break;

				case 2:
					// System.out.println("成功!");
					// finish();
					// tvVisitState.setTextColor(Color.GREEN);

					String str3 = NativeModeNoPageActivity.this.getResources()
							.getString(R.string.visited_interrupt);
					/**
					 * 假如是最终保存,则将左边的那个按钮置为“新建”
					 */
					if (MSG_WRITE == action) {
						str3 = NativeModeNoPageActivity.this.getResources()
								.getString(R.string.visit_successfully);
						if (1 == feed.getSurvey().openStatus) {
							btnLeft.setVisibility(View.GONE);
						} else {
							btnLeft.setText(NativeModeNoPageActivity.this
									.getResources().getString(
											R.string.new_panel));
							// 新建限制
							if (1 == feed.getSurvey().oneVisit) {
								btnLeft.setVisibility(View.GONE);
							}
						}
					} else {
						/**
						 * 假如是中间退出,则将左边的那个按钮置为“继续”
						 */
						btnLeft.setText(NativeModeNoPageActivity.this
								.getResources().getString(R.string._continue));
					}

					SpannableString ss3 = new SpannableString(str3);
					ss3.setSpan(new ForegroundColorSpan(Color.BLUE), 6,
							str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tvVisitState.setText(ss3);
					// 大树拒访 12
					if (feed.getReturnTypeId() != -2) {
						tvVisitState.setText(NativeModeNoPageActivity.this
								.getResources().getString(R.string.no_fang_mao,
										returnName));
					} else {
						tvVisitState.setText(ss3);
					}
					// 大树拒访
					btnLeft.setTextColor(Color.WHITE);
					btnRight.setTextColor(Color.BLACK);
					btnRight.setText(NativeModeNoPageActivity.this
							.getResources().getString(R.string.terminal));
					btnLeft.setOnClickListener(new ResultClickListener(2,
							action));
					btnRight.setOnClickListener(new ResultClickListener(2,
							action));
					btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							adpter_text_size);
					btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							adpter_text_size);
					break;
				}

			}
		}
	}

	private boolean newRefresh = false;// 新建刷新
	private boolean noSkipNext = false;// 页面不再继续跳转
	private int operType;// 0为下一页，1为上一页
	private final Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			int one = 0;
			// Thread thread = null;
			switch (msg.what) {
			case UPLOAD_AGIN:
				new UpLoadFileTask(feed, msg.arg1, MSG_WRITE).execute(
						ma.userId,
						MD5.Md5Pwd(ma.cfg.getString(Cnt.USER_PWD, "")),
						feed.getSurveyId(), feed.getPath(), feed.getName(),
						Cnt.UPLOAD_URL);
				break;
			case ADD_ALL:
				// ThreadPoolUtils.execute(new Runnable() {
				// public void run() {
				qs = aryQs.get(pageNum);
				ll.removeAllViews();
				es.execute(new Runnable() {
					public void run() {
						try {
							if (!Util.isEmpty(qs)) {
								if (null == Looper.myLooper()) {
									Looper.prepare();
								}
								for (int i = 0; i < qs.size(); i++) {
									Question q = qs.get(i);
									if (resctCheck(q)
											&& Cnt.TYPE_PAGE != q.qType
											&& noSkipNext == false) {// 符合逻辑且不是分页符
										noSkipNext = true;// 不再继续跳转
										break;
									}
								}
								if (noSkipNext) {
									ArrayList<View> viewList = new ArrayList<View>();
									for (int i = 0; i < qs.size(); i++) {
										Question q = qs.get(i);
										View view = createQuestionBodyView(i,
												false);
										if (!resctCheck(q)
												|| Cnt.TYPE_PAGE == q.qType) {// 不符合逻辑或分页符
											Answer tempAn = ma.dbService
													.getAnswer(feed.getUuid(),
															q.qIndex + "");
											if (null != tempAn) {
												System.out.println("答案不为空；");
												/**
												 * 置为无效的答案
												 */
												ma.dbService
														.updateAnswerUnEnable(tempAn);
											}
											view.setVisibility(View.GONE);
										}
										viewList.add(view);
									}
									Message viewMsg = Message.obtain();
									viewMsg.what = ADD_ALL_BODY;
									viewMsg.obj = viewList;
									handler.sendMessage(viewMsg);
									noSkipNext = false;
								} else {
									if (0 == operType) {
										handler.sendEmptyMessage(MSG_NEXT);
									} else {
										handler.sendEmptyMessage(MSG_PRE);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				// }
				// });
				break;
			case HIDE_BODY:
				ll.getChildAt(msg.arg1).setVisibility(View.GONE);
				Answer tempAnswer = ma.dbService.getAnswer(feed.getUuid(),
						msg.arg2 + "");
//				if (null != tempAnswer) {
//					System.out.println("答案不为空；");
					/**
					 * 置为无效的答案
					 */
//					ma.dbService.updateAnswerUnEnable(tempAnswer);
					
//				}
				break;
				
			case SHOW_BODY:
				ll.getChildAt(msg.arg1).setVisibility(View.VISIBLE);
				break;
			case ADD_ALL_BODY:
				proBar.setProgress(pageNum + 1);
				ArrayList<View> viewList = (ArrayList<View>) msg.obj;
				for (int i = 0; i < viewList.size(); i++) {
					View view = viewList.get(i);
					ll.addView(view, ll.getChildCount());
				}

				// 最后一页的时候下一页变成提交

				if ((pageNum == aryQs.size() - 1)) {
					nq_btn.setText(R.string.commit_survey);
					uploadtype = "100";

				} else if ((pageNum < aryQs.size() - 1)) {
					nq_btn.setText(R.string.next_page);
				}

				setTopClick(true);
				globleProgress.setVisibility(View.GONE);
				break;
			case ADD_BODY:
				ll.removeViewAt(msg.arg1);
				ll.addView((View) msg.obj, msg.arg1);
				break;
			case REFRESH:
				/**
				 * 检查逻辑，刷新题目
				 */
				final int position = msg.arg1;
				es.execute(new Runnable() {
					public void run() {
						try {
							if (!Util.isEmpty(qs)) {
								if (null == Looper.myLooper()) {
									Looper.prepare();
								}
								boolean ADDBODY = false;
								for (int i = 0; i < qs.size(); i++) {
									
									Log.i("刷新的题目：：",qs.get(i).qIndex+qs.get(i).qid);
									if (i > position) {
										Question q = qs.get(i);
										boolean isresctcheck = resctCheck(q);

										Log.i("isresctcheck::","isresctcheck==" + isresctcheck);
										Log.i("isresctcheck::","isresctcheck:qid" + q.qid);
										// t出现f不出现

										if (!isresctcheck
												|| Cnt.TYPE_PAGE == q.qType) {// 不符合逻辑
											// if (View.GONE !=
											// view.getVisibility()) {
											// ll.getChildAt(i).setVisibility(View.GONE);
											
											
											Message viewMsg = Message.obtain();
											viewMsg.what = HIDE_BODY;
											viewMsg.arg1 = i;
											viewMsg.arg2 = q.qIndex;
											handler.sendMessage(viewMsg);
											checknextquestion(i);
											// }
										} else {
//											if (!ADDBODY && Cnt.TYPE_HEADER != q.qType) {
											if (Cnt.TYPE_HEADER != q.qType) {
												// ll.removeViewAt(i);
												// ll.addView(createQuestionBodyView(i,true),
												// i);
													View v = createQuestionBodyView(i, true);
													Message viewMsg = Message.obtain();
													viewMsg.what = ADD_BODY;
													viewMsg.arg1 = i;
													viewMsg.obj = v;
													handler.sendMessage(viewMsg);
													if (1 == q.qRequired && 1 != q.qPreSelect
															&& Cnt.TYPE_FREE_TEXT_BOX != q.qType
															&& Cnt.TYPE_FREE_TEXT_AREA != q.qType) {
														ADDBODY = true;
													}
//												Message showviewMsg = Message.obtain();
//												showviewMsg.what = SHOW_BODY;
//												showviewMsg.arg1 = i;
//												showviewMsg.arg2 = q.qIndex;
//												handler.sendMessage(showviewMsg);
												
											}
										}
										// }
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					
				});
				break;
			case MSG_PRE:
				/**
				 * 上一页
				 */
				backPage();
				break;
			case MSG_NEXT:
				/**
				 * 下一页
				 */
				nextPage(true);
				break;

			case MSG_RECORD:
				/**
				 * 单题录录音
				 */
				openRecord(String.valueOf(msg.obj), null);
				// new RecordTask(isClicked, String.valueOf(msg.obj)).execute();
				break;
			// 目录自动翻下一页
			case NEXT_READ_TITLE:

				break;
			// 目录自动翻上一页
			case BACK_READ_TITLE:

				break;
			case MSG_SAVE:
			case MSG_WRITE:
				Log.i(TAG, "反正是返回了-----"+ (1 == feed.getSurvey().appPreview && MSG_WRITE == msg.what));
				// 先判断有无预览确认页面
				if (1 == feed.getSurvey().appPreview && MSG_WRITE == msg.what) {
					Log.i(TAG, "反正是返回了-----true");
					Intent it = new Intent(NativeModeNoPageActivity.this,
							ReviewCommitActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("feed", feed);
					it.putExtras(bundle);
					startActivityForResult(it, 2);
					// 大树 关闭界面
					overridePendingTransition(R.anim.right1, R.anim.left1);
				} else {
					Log.i(TAG, "反正是返回了-----false");
					Log.i(TAG, "反正是返回了-----survey++++"+survey.getAppAutomaticUpload());
					Log.i(TAG, "反正是返回了-----uploadtype+++"+uploadtype);
					if(survey.getAppAutomaticUpload() == 0){
						if (uploadtype.equals("98")){
							uploadhttp(uploadtype);
						}
					}
					
					
					// 若无预览确认页面则直接写入xml
					// 监控用得到最后确认提交码
					String submitCode = (String) msg.obj;
					if (Util.isEmpty(submitCode)) {
						new XmlTask(msg.what).execute();
					} else {
						new XmlTask(msg.what, submitCode).execute();
					}
				}
				break;
			// 新建受访者
			case MSG_NEW_FEED:
				// 是否是题组改动
				boolean isQuestionGroup = false;
				// System.out.println("新建");
				Survey survey = feed.getSurvey();
				final String spwd = survey.getPassword();
				// 输入密码，假如正确新建，否则退出。
				if (!"".equals(survey.getPassword())) {
					// 弹出窗口，正确就能新建。
					final EditText et = new EditText(
							NativeModeNoPageActivity.this);
					new AlertDialog.Builder(NativeModeNoPageActivity.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle(
									NativeModeNoPageActivity.this
											.getString(R.string.input_pwd))
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(et)
							.setPositiveButton(
									NativeModeNoPageActivity.this
											.getString(R.string.ok),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 改动
											globleProgress
													.setVisibility(View.VISIBLE);
											boolean isQuestion = false;
											String pwd = et.getText()
													.toString();
											if ("".equals(pwd.trim())) {
												Toasts.makeText(
														NativeModeNoPageActivity.this,
														NativeModeNoPageActivity.this
																.getString(R.string.null_reverse_input),
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
											if (pwd.equals(spwd)) {
												// 目录新建重新赋值开始
												if (!isQgroupOrLogic) {
													recodeOrderList.clear();
													recodeOrderList.add(0);
													dtOrderList.clear();
													dtOrderList.add(0);
												}
												// 目录新建重新赋值结束
												// 有题组随机时候才重新初始化
												if (groupSize > 0) {
													// 改动
													isQuestion = true;
													initGroupHand();
												}
												// initView();
												pageNum = 0;
												qs = aryQs.get(pageNum);
												index = 0;
												// 监控用(西城BUG)
												recodeTempIndex = -1;
												showVisitView();
												newFeed(System
														.currentTimeMillis());
												// if (1 ==
												// feed.getSurvey().isPhoto)
												// {z1删
												// cameraView(View.VISIBLE);
												// } else {
												// cameraView(View.GONE);
												// if (isHaveSingle) {
												//
												// } else {
												// sdImages.setVisibility(View.INVISIBLE);
												// Log.i("kjy",
												// "sdImages.setVisibility(View.GONE);");
												// }
												// }z1删
												// tvTitle.setCompoundDrawables(null,
												// null, null, null);
												// System.out.println("qs.size()="
												// +
												// qs.size());
												// 改动
												ll.removeAllViews();
												handler.sendEmptyMessage(ADD_ALL);
												// ThreadPoolUtils.execute(new
												// Runnable() {
												// public void run() {
												// if (!Util.isEmpty(qs)) {
												// for (int i = 0; i <
												// qs.size(); i++) {
												// Message viewMsg =
												// Message.obtain();
												// viewMsg.what = ADD_ALL_BODY;
												// viewMsg.arg1 = i;
												// viewMsg.obj =
												// createQuestionBodyView(i);
												// handler.sendMessage(viewMsg);
												// }
												// }
												// }
												// });
												if (1 == feed.getSurvey().globalRecord) {
													// System.out.println("隐藏录音按钮");
													// recordView(View.GONE);
													HashMap<String, Object> params = new HashMap<String, Object>();
													params.put("feed", feed);
													params.put(
															"content",
															NativeModeNoPageActivity.this);
													params.put("isClicked",
															isClicked);
													params.put("q", qs.get(0));
													openGlobalRecord(params);
													// MainService.newTask(new
													// Task(TaskType.TS_RECORED,
													// params));
												}
												dialog.dismiss();
											} else {
												Toasts.makeText(
														NativeModeNoPageActivity.this,
														NativeModeNoPageActivity.this
																.getString(R.string.pwd_no),
														Toast.LENGTH_SHORT)
														.show();
												dialog.dismiss();
											}
										}
									})
							.setNegativeButton(
									NativeModeNoPageActivity.this
											.getString(R.string.cancle),
									null).show();
				} else {
					globleProgress.setVisibility(View.VISIBLE);
					// 目录新建重新赋值开始
					if (!isQgroupOrLogic) {
						recodeOrderList.clear();
						recodeOrderList.add(0);
						dtOrderList.clear();
						dtOrderList.add(0);
					}
					// 目录新建重新赋值结束
					// 有题组随机时候才重新初始化
					if (groupSize > 0) {
						// 改动
						isQuestionGroup = true;
						initGroupHand();
					}
					// initView();
					pageNum = 0;
					qs = aryQs.get(pageNum);
					index = 0;
					// 监控用(西城BUG)
					recodeTempIndex = -1;
					showVisitView();
					newFeed(System.currentTimeMillis());
					// if (1 == feed.getSurvey().isPhoto) {z1删除
					// cameraView(View.VISIBLE);
					// } else {
					// cameraView(View.GONE);
					// if (isHaveSingle) {
					//
					// } else {
					// sdImages.setVisibility(View.INVISIBLE);
					// Log.i("kjy", "sdImages.setVisibility(View.GONE);");
					// }
					// }z1删除
					// tvTitle.setCompoundDrawables(null, null, null, null);
					// System.out.println("qs.size()=" + qs.size());
					// 更改
					ll.removeAllViews();
					handler.sendEmptyMessage(ADD_ALL);
					// ThreadPoolUtils.execute(new Runnable() {
					// public void run() {
					// if (!Util.isEmpty(qs)) {
					// for (int i = 0; i < qs.size(); i++) {
					// Message viewMsg = Message.obtain();
					// viewMsg.what = ADD_ALL_BODY;
					// viewMsg.arg1 = i;
					// viewMsg.obj = createQuestionBodyView(i);
					// handler.sendMessage(viewMsg);
					// }
					// }
					// }
					// });
					if (1 == feed.getSurvey().globalRecord) {
						// System.out.println("隐藏录音按钮");
						// recordView(View.GONE);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("feed", feed);
						params.put("content", NativeModeNoPageActivity.this);
						params.put("isClicked", isClicked);
						params.put("q", qs.get(0));
						openGlobalRecord(params);
						// MainService.newTask(new Task(TaskType.TS_RECORED,
						// params));
					}

				}
				break;

			case MSG_REDIRECT:
				// bodyView.removeAllViews();
				// 单复选矩阵固定
				// bodyView_new.removeAllViews();
				// vs.clear();
				showVisitView();
				setTopClick(true);
				// if (1 == feed.getSurvey().isPhoto) {z1删除
				// cameraView(View.VISIBLE);
				// } else {
				// cameraView(View.GONE);
				// if (isHaveSingle) {
				//
				// } else {
				// sdImages.setVisibility(View.INVISIBLE);
				// Log.i("kjy", "sdImages.setVisibility(View.GONE);");
				// }
				// }z1删除
				if (1 == feed.getSurvey().globalRecord) {
					// System.out.println("隐藏录音按钮");
					// recordView(View.GONE);
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("feed", feed);
					params.put("content", NativeModeNoPageActivity.this);
					params.put("isClicked", isClicked);
					params.put("q", qs.get(0));
					openGlobalRecord(params);
					// MainService.newTask(new Task(TaskType.TS_RECORED,
					// params));
				}
				break;
			case MSG_REDIRECT_BACK:
				showVisitView();
				setTopClick(true);
				break;
			case STATE_CHECK_COUNT:// 单列选择数超过制定数量
			case STATE_CONTINUOUS_NEW:// 连续选择数超过制定数量
			case Cnt.STATE_CLASS_STOP:
			case Cnt.STATE_FAIL:
			case STATE_NOTHING:
			case STATE_BOUND_LOWER:
			case STATE_BOUND_UPPER:
			case STATE_BOUND_MATRIX_UPPER:
			case STATE_BOUND_MATRIX_LOWER:
			case STATE_CONTINUOUS:
			case STATE_ROW_LESS:
			case STATE_SORT_CHECK:
			case STATE_SYB_MIN:
			case STATE_SYB_MAX:
			case STATE_SYB_MIN_RANGE:
			case STATE_SYB_MAX_RANGE:
			case FORMAT_NO_NUMBER:
			case STATE_SYB_REPEAT:
			case STATE_SYB_DATE_EQUAL:
			case STATE_SYB_DATE_UPPER_EQUAL:
			case STATE_SYB_DATE_NO_EQUAL:
			case STATE_SYB_DATE_UPPER:
			case STATE_SYB_DATE_LOW_EQUAL:
			case STATE_SYB_DATE_LOW:
			case STATE_SYB_LEN_EQUAL:// 提示 某项的文字长度应该等于多少
			case STATE_SYB_LEN_UPPER_EQUAL:
			case STATE_SYB_LEN_NO_EQUAL:
			case STATE_SYB_LEN_UPPER:
			case STATE_SYB_LEN_LOW_EQUAL:
			case STATE_SYB_LEN_LOW:
			case FORMAT_NO_FLOAT_NUMBER:
			case STATE_SYB_SUM_VALUE:
			case FORMAT_NO_NUMBER_AND_WORD:
			case FORMAT_NO_EMAIL:
			case FORMAT_NO_CAMERA:// 单题签名
			case ITEM_NO_REQUIRED:
			case FORMAT_NO_SIGN:
			case QUESTION_OUT_YING_RELEVANCE:// 题外关联 之和 字段在这里 大树 外部关联 5
			case QUESTION_INTERIOR_RELEVANCE:// 题外关联 之和 字段 内部关联 大树 内部关联 16
			case DECIMAL_PLACES_NO_MEET: // IMS临时干预价格 小数点位数 为 2
			case QUESTION_ITEM_ORDER_REPEAT: // 大树排序 选项值不能重复
			case LUO_JI_WRONG: // IMS 逻辑错误
				final String noMsg = String.valueOf(msg.obj);

				// if (isShow && !Util.isEmpty(noMsg)) {
				// mDialog.show();
				// ((TextView)
				// mDialog.findViewById(R.id.msg_tv)).setText(noMsg);
				// mDialog.findViewById(R.id.ok_btn).setOnClickListener(new
				// OnDialogMissListener());
				// }
				// 干预提示框 样式修改
				if (isShow && !Util.isEmpty(noMsg)) {
					boolean isHaveRight = false;
					if (msg.what == STATE_SYB_MIN_RANGE
							|| msg.what == STATE_SYB_MAX_RANGE) {
						isHaveRight = true;
					}

					DialogBulder builder = new DialogBulder(
							NativeModeNoPageActivity.this, screenWidth,
							screenHeight);
					builder.setTitle(NativeModeNoPageActivity.this
							.getString(R.string.intevention_notice));
					builder.setMessage(noMsg,
							UIUtils.pixel2Dimen(lowSurveySize));

					builder.setButtons(NativeModeNoPageActivity.this
							.getString(R.string.ok),
							NativeModeNoPageActivity.this
									.getString(R.string.cancel),
							new DialogBulder.OnDialogButtonClickListener() {
								public void onDialogButtonClick(
										Context context, DialogBulder builder,
										Dialog dialog, int dialogId, int which) {
									if (null != question
											&& null != question.item
											&& "true"
													.equals(question.item.range)) {
										if (which == BUTTON_LEFT) {
											// System.out.println("BUTTON_LEFT");
											question.item.isRange = true;
											nextPage(false);
										}
										if (which == BUTTON_RIGHT) {
											// System.out.println("BUTTON_RIGHT");
											// Toasts.makeText(NativeModeNoPageActivity.this,
											// noMsg,
											// Toast.LENGTH_SHORT).show();//
											// 吐司提示
										}
									} else {
										if (which == BUTTON_LEFT) {
											// Toasts.makeText(NativeModeNoPageActivity.this,
											// noMsg,
											// Toast.LENGTH_SHORT).show();//
											// 吐司提示
											// System.out.println("BUTTON_LEFT");
										}
									}
								}
							}, isHaveRight);
					Dialog dialog = builder.create();
					dialog.show();
				}
				break;

			case MSG_FINISH:
				String _msg = String.valueOf(msg.obj);
				if (isShow && !Util.isEmpty(_msg)) {
					mDialog.show();
					((TextView) mDialog.findViewById(R.id.msg_tv))
							.setText(_msg);
					mDialog.findViewById(R.id.ok_btn).setOnClickListener(
							new OnDialogMissListener(MSG_FINISH));
				}
				break;
			}
		}

	};

	/** 新建时候重新初始化题组随机 */
	private void initGroupHand() {
		diffList.clear();
		qDiffList.clear();
		orderMap.clear();
		dialogMap.clear();
		groupSize = 0;
		tempOrderMap.clear();
		choiceNum = -1;
		allGroupOrder.clear();
		handGroupNum = 0;
		handList.clear();
		preDialogMap.clear();
		handGroupSum = 0;
		new OrderTask().execute();
	}

	private void initGroupHands() {
		diffList.clear();
		qDiffList.clear();
		orderMap.clear();
		dialogMap.clear();
		groupSize = 0;
		tempOrderMap.clear();
		choiceNum = -1;
		allGroupOrder.clear();
		handGroupNum = 0;
		handList.clear();
		preDialogMap.clear();
		handGroupSum = 0;
	}

	/**
	 * 题组时点击跳转
	 * 
	 * @param fOrder
	 */
	private void clearGroupHand(int fOrder) {
		Collections.sort(handList);
		// 包含这个index.说明返回了一层，这种触发重新分配手动循环位置事件
		if (handList.contains(index)) {
			// 先得到原先位置的实体
			choiceNum -= 1;
			handGroupNum -= 1;
			// 通过预选项得到原先这个位置的 暂存组
			TempGroup tempGroup = preDialogMap.get(index + 1);
			// 如果已经有的事件小于总手动随机数目,并且现在choiceNum没值
			if (dialogMap.size() < handGroupSum && -2 == choiceNum) {
				// choiceNum等于 改组集合顺序的个数-2。就为上一组题组随机触发事件的最后位置。
				choiceNum = orderMap.get(tempGroup.getBigOrder()).size() - 2;
				// System.out.println("choiceNum:"+choiceNum);
			} else {
				// 首先得到储存 手动集合位置
				int tempIndexOf = handList.indexOf(index);
				// 移除掉上一组触发事件的 map
				dialogMap.remove(handList.get(tempIndexOf + 1) + 1);
				// 移除上一次点下一题组的位置
				handList.remove(tempIndexOf + 1);
			}
			// 移除这一组保存的数据
			preDialogMap.remove(index + 1);
			// 给这一组赋值
			dialogMap.put(index + 1, tempGroup);
			// System.out.println("dialogMap:"+dialogMap.size());
			// System.out.println(dialogMap.get(index+1).toString());
			System.out.println("外部choiceNum:" + choiceNum);
			// System.out.println("handList:"+handList);
			// System.out.println("preDialogMap:"+preDialogMap.size());

			/**
			 * 重新分配位置 0@3|0@4
			 */
		}
	}

	private final class ResultClickListener implements OnClickListener {

		private int s;
		private int a;

		public ResultClickListener(int state, int action) {
			this.s = state;
			this.a = action;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/**
			 * 左边的按钮,新建访问or放弃
			 */
			case R.id.left_btn:
				switch (this.s) {
				case 0:// 空串
				case 1:// 写入失败
					ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
					finish();
					break;

				case 2:
					// 成功
					if (MSG_WRITE == a) {
						/**
						 * 此处显示新建
						 */
						if (1 == verifyFlag) {
							AlertDialog.Builder locBuilder = new AlertDialog.Builder(
									NativeModeNoPageActivity.this,
									AlertDialog.THEME_HOLO_LIGHT);
							locBuilder
									.setTitle("提示")
									.setIcon(android.R.drawable.ic_dialog_info)
									.setMessage("当前状态为测试模式，确认继续？")
									.setPositiveButton(
											NativeModeNoPageActivity.this
													.getResources().getString(
															R.string.ok),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													handler.sendEmptyMessage(MSG_NEW_FEED);
													dialog.dismiss();
													return;
												}

											})
									.setNegativeButton(
											NativeModeNoPageActivity.this
													.getResources().getString(
															R.string.cancel),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
							AlertDialog create = locBuilder.create();
							create.show();
							TextView msgTv = ((TextView) create
									.findViewById(android.R.id.message));
							msgTv.setMinLines(2);
							msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									dialogBtnSize);
							msgTv.setGravity(Gravity.CENTER_VERTICAL);
							create.getButton(create.BUTTON_NEGATIVE)
									.setTextSize(TypedValue.COMPLEX_UNIT_PX,
											dialogBtnSize);
							Button okBtn = create
									.getButton(create.BUTTON_POSITIVE);
							okBtn.setBackgroundColor(0xFF6751B6);
							okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									dialogBtnSize);
							okBtn.setTextColor(Color.WHITE);
						} else {
							handler.sendEmptyMessage(MSG_NEW_FEED);
						}
					} else {
						/**
						 * 此处显示继续
						 */
						handler.sendEmptyMessage(MSG_REDIRECT);
					}
					break;
				}
				break;

			/**
			 * 右边的按钮,重试or终止
			 */
			case R.id.right_btn:
				switch (this.s) {
				case 0:// 空串
				case 1:// 写入失败
					handler.sendEmptyMessage(MSG_REDIRECT);
					break;

				case 2:
					// 成功
					Survey survey = feed.getSurvey();
					final String spwd = survey.getPassword();
					// 输入密码，假如正确新建，否则退出。
					initGroupHands();
					finish();
					break;
				}
				break;
			}
		}
	};

	// 逻辑处理，判断是否联网，是否有附件，是否登录
	public class NetTask extends AsyncTask<Void, Void, Boolean> {
		private Integer result;
		private int action;

		public NetTask(Integer result, int action) {
			this.result = result;
			this.action = action;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean checknet = false;
			try {
				checknet = NetService.checknet("http://" + Cnt.RECORD_PHOTO_URL
						+ "/", null, "GET");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return checknet;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				UPLOAD_COUNT = 0;
				try {
					if (!Util.isEmpty(ma.cfg.getString(Cnt.USER_PWD, ""))) {
						new UpLoadFileTask(feed, this.result, action).execute(
								ma.userId,
								MD5.Md5Pwd(ma.cfg.getString(Cnt.USER_PWD, "")),
								feed.getSurveyId(), feed.getPath(),
								feed.getName(), Cnt.UPLOAD_URL);
					} else {
						dismiss();
						showResultPage(this.result, action);
					}
				} catch (Exception e) {
					dismiss();
					showResultPage(this.result, action);
					e.printStackTrace();
				}
			} else {
				dismiss();
				showResultPage(this.result, action);
			}
			super.onPostExecute(result);
		}

	}

	// 自动上传
	public class UpLoadFileTask extends
			AsyncTask<String, String, HashMap<String, String>> {
		private UploadFeed feed;
		private Integer result;
		private int action;

		public UpLoadFileTask(UploadFeed f, Integer result, int action) {
			feed = f;
			this.result = result;
			this.action = action;
		}

		protected HashMap<String, String> doInBackground(String... params) {// userId
																			// userPsd
																			// surveyId
																			// path
																			// filename
																			// URL
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("surveyId", params[2]);
			paramsMap.put(Cnt.USER_ID, URLEncoder.encode(params[0]));
			paramsMap.put(Cnt.USER_PWD, params[1]);
			paramsMap.put("ModelFlag", "1");
			FileUpLoad fupLoad = new FileUpLoad();
			InputStream is = fupLoad.upLoadBase64(params[5], params[3],
					params[4], paramsMap);
			if (is == null) {
				Log.e("kjy", "UpLoadErrorX:" + feed.getName());
				return null;
			} else {
				return resolvData(is, feed);
			}
		}

		private HashMap<String, String> resolvData(InputStream is,
				UploadFeed feed) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			HashMap<String, String> rMap = new HashMap<String, String>();
			String state = "0";
			String fid = null;
			String rtp = null;
			String PID = null;
			// System.out.println("resolvData");
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element element = document.getDocumentElement();
				state = element.getElementsByTagName("S").item(0)
						.getFirstChild().getNodeValue();
				rMap.put("S", state);
				// Log.e("DapDesk", "State:" + state);
				System.out.println("解析之后的状态--->" + state);
				if ("100".equals(state)) {
					fid = element.getElementsByTagName("FID").item(0)
							.getFirstChild().getNodeValue();
					rtp = element.getElementsByTagName("RTP").item(0)
							.getFirstChild().getNodeValue();
					PID = element.getElementsByTagName("PID").item(0)
							.getFirstChild().getNodeValue();
					// logic.updateFeedStatus(String.valueOf(feed.id), proId);
					// System.out.println("resolvData--->feed.uuid--->"+feed.getUuid());
					/**
					 * XML报完之后将uploaded=1--->uploaded=1,说明XML报过音频和图片没有报
					 */
					// ma.dbService.updateFeedStatusIpsos(feed.getUuid(), 9);
					// xml上传完成 等候上传照片录音
					Log.e("DapDesk", "FID:" + fid + " RTP:" + rtp);
					Log.e("DapDesk", "PID:" + PID);
				} else {
					is.close();
					return rMap;
				}
			} catch (Exception e) {
				Log.e("DapDesk", "Message:" + e.getMessage());
			}
			if (fid != null) {
				rMap.put("FID", fid);
			}
			if (rtp != null) {
				rMap.put("RTP", rtp);
			}
			if (PID != null) {
				rMap.put("PID", PID);
			}
			return rMap;
		}

		protected void onPostExecute(HashMap<String, String> rMap) {
			if (rMap == null) {
				if (3 > UPLOAD_COUNT) {
					Toasts.makeText(NativeModeNoPageActivity.this,
							"问卷上报失败，正在尝试重新上传", Toast.LENGTH_SHORT).show();
					UPLOAD_COUNT++;
					Message msg = Message.obtain();
					msg.what = UPLOAD_AGIN;
					msg.arg1 = this.result;
					handler.sendMessageDelayed(msg, 2000);
					return;
				}
				upLoadError(feed.getId(), 0, feed);
				dismiss();
				// 上传程序完成
				showResultPage(result, action);
				return;
			}
			String stStr = rMap.get("S");
			int state = 0;
			if (stStr != null)
				state = Integer.parseInt(stStr);
			System.out.println("XML上传后服务器返回的状态码--->" + state);
			if (state == 100) {
				String fid = rMap.get("FID");
				String rtp = rMap.get("RTP");
				String PID = rMap.get("PID");
				if (Util.isEmpty(fid) || "0".equals(fid)) {// 如果返回的feedid为空或者为0则视为上传失败
					upLoadError(feed.getId(), 1, feed);
				} else {
					if (ma.dbService.checkUploadfeed(feed.getUuid(),
							feed.getSurveyId())
							|| ma.dbService.upDateFeedId(feed, fid)) {// 修改附件feedid
						upLoadSuccess(feed, fid, rtp, PID);// 更新xml的数据库数据
						// Log.i("@@@", "feed.getFeedId()=" + feed.getFeedId());
					} else {
						upLoadError(feed.getId(), 2, feed);
					}
				}
			} else if (state == 200) {
				Toasts.makeText(NativeModeNoPageActivity.this,
						"此项目已经关闭，无法完成上传", Toast.LENGTH_SHORT).show();
				ma.dbService.removeSurvey(feed.getSurveyId());
			} else {
				if (3 > UPLOAD_COUNT) {
					Toasts.makeText(NativeModeNoPageActivity.this,
							"问卷上报失败，正在尝试重新上传", Toast.LENGTH_SHORT).show();
					UPLOAD_COUNT++;
					Message msg = Message.obtain();
					msg.what = UPLOAD_AGIN;
					msg.arg1 = this.result;
					handler.sendMessageDelayed(msg, 2000);
					return;
				}
				upLoadError(feed.getId(), state, feed);
			}
			dismiss();
			// 上传程序完成
			showResultPage(result, action);

//			System.out.println("------开始下载名单------");
//			// 连续性项目 自动下载下一个项目的名单
//			String authorId = ma.cfg.getString("authorId", "");
//			if (Util.isEmpty(ma.userId)) {
//				ma.userId = ma.cfg.getString("UserId", "");
//			}
//			Survey s = ma.dbService.getSurvey(feed.getSurveyId());
//			// 下载下个项目 的名单
//			if (!Util.isEmpty(s.getSCNextId())) {
//				System.out.println(s.getSCNextId() + "");
////				(String _authorId, String _userId, Survey _survey,Context mActivity,MyApp ma) {
//				new InnerTask(authorId, ma.userId,ma.dbService.getSurvey(s.getSCNextId()),NativeModeNoPageActivity.this,ma).execute();
//			}
//			System.out.println("------下载名单结束------");

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		public void upLoadError(long id, int state, UploadFeed feed) {
			if (0 == state) {
			} else if (1 == state) {// 卷号获取失败
			} else if (2 == state) {// 卷号更新失败
			} else if (95 == state) {
				ma.dbService.giveUpFeed(feed.getUuid(), feed.getSurveyId());
			}
			// boolean b = ma.dbService.feedAnswerIsHave(feed.getUuid());
			// File file = new File(feed.getPath(), feed.getName());
			/**
			 * 假如是空问卷
			 */
			// if ( !file.exists()) {
			// ma.dbService.updateUploadFeedNoAccessState(feed.getId());
			// }
		}

		private void upLoadSuccess(UploadFeed feed, String fid, String rtp,
				String PID) {
			if (null != feed) {
				feed.setFeedId(fid);
				feed.setReturnType(rtp);
				feed.setPid(PID);
				/**
				 * 上传状态设置为9,
				 */
				feed.setIsUploaded(9);
				ma.dbService.updateUploadFeedStatus(feed);
			}
		}
	}

	private final class SeekBarChangeListener implements
			OnSeekBarChangeListener {
		private TextView tv;
		private String w;
		private ArrayList<View> vs;
		private Question q;
		private ArrayList<QuestionItem> mTempRows;
		private int max;
		private int min;
		private QuestionItem item;
		private int position;

		public SeekBarChangeListener(TextView tvProgress, String word,
				ArrayList<View> vs, Question q,
				ArrayList<QuestionItem> mTempRows, int max, int min,
				QuestionItem item, int position) {
			this.tv = tvProgress;
			this.w = word;
			this.vs = vs;
			this.q = q;
			this.mTempRows = mTempRows;
			this.max = max;
			this.min = min;
			this.item = item;
			this.position = position;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (!item.isChange) {
				item.isChange = true;
			}
			int sbProgress = progress + min;
			if (Util.isEmpty(this.w)) {
				/**
				 * 假如单行文本题，在这里是滚动条体型, 假如它右边没有文字,则直接在其上方显示当前拖动进度
				 */
				this.tv.setText("(" + sbProgress + "/" + max + ")");
			} else {
				/**
				 * 假如右边有文字，则其文字后边加上一个括号，然后里面再标出其当前的进度
				 */
				this.tv.setText(this.w + "(" + sbProgress + "/" + max + ")");
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Log.i("@@@", "选中拖拉条获取答案");
			int success = getQuestionAnswer( false, q, vs, null,
					mTempRows, false);
			if (Cnt.STATE_SUCCESS == success) {
				Message msg = Message.obtain();
				msg.what = REFRESH;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		}

	};

	protected class MyOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_SEARCH == keyCode
					|| KeyEvent.KEYCODE_BACK == keyCode) {
				return true;
			}
			return false;
		}
	}

	private final class OnDialogMissListener implements OnClickListener {
		private int msg;

		public OnDialogMissListener() {

		}

		public OnDialogMissListener(int _msg) {
			msg = _msg;
		}

		@Override
		public void onClick(View v) {
			mDialog.dismiss();
			if (MSG_FINISH == msg) {
				finish();
			}
		}

	}

	private final class CustomViewFactor implements ViewFactory {

		@Override
		public View makeView() {
			ImageView i = new ImageView(NativeModeNoPageActivity.this);
			// i.setBackgroundColor(0xFF000000);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ImageSwitcher.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return i;
		}

	}

	private View preView;
	private BitmapDrawable bd = null;

	private final class CustomItemSelectListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			UploadFeed image = (UploadFeed) parent.getItemAtPosition(position);
			if (null != image) {
				if (null != preView) {
					/**
					 * 假如上一次点过图片的花 则将其背景置为白色,即表示不是当前显示的图片
					 */
					preView.setBackgroundColor(Color.WHITE);
				}
				/**
				 * 将现当前显示的图片置为上一次显示的图片
				 */
				preView = view;

				/**
				 * 显示当前图片
				 */
				// mSwitcher.setImageURI(Uri.parse(image.getPath() +
				// File.separator + image.getName()));

				if (null != bd) {
					bd.getBitmap().recycle();
					bd = null;
					System.gc();
				}
				mSwitcher.reset();
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					File file = new File(image.getPath() + File.separator
							+ image.getName());
					// 数字越大读出的图片占用的heap越小 不然总是溢出
					long len = file.length();
					if (1048576 * 3 > len) { // 显示图片时的压缩 小于1024k, 即1M的
						opts.inSampleSize = 2;
					} else if (1048576 * 8 > len && len >= 1048576 * 3) {
						/**
						 * 大于1M的
						 */
						opts.inSampleSize = 6;
					} else {
						opts.inSampleSize = 10;
					}
					Bitmap oldBitmap = BitmapFactory.decodeStream(
							new FileInputStream(file), null, opts);
					// if (1048576 > len) { // 小于1024k
					// opts.inSampleSize = 3;
					// } else {
					// opts.inSampleSize = 6;
					// }
					// resizeBmp = BitmapFactory.decodeFile(file.getPath(),
					// opts);
					// Bitmap resizeBmp = ;

					bd = new BitmapDrawable(oldBitmap);
					mSwitcher.setImageDrawable(bd);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}

				/**
				 * 显示当前显示的是哪一个位置的图片,例如(1/6)一共有6张图片当前显示是第1张
				 */
				tvImageCount.setText("(" + (1 + position) + "/"
						+ parent.getCount() + ")");
				/**
				 * 将当前显示的图片背景色置为深灰,即和其他的图片背景色不一样(其他的为白色的)
				 */
				view.setBackgroundColor(Color.BLUE);

			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private void matrixRadioBt(Boolean isFixed,
			ArrayList<String> twoQsiteOptions, ArrayList<AnswerMap> amList,
			LinearLayout bodyView, ArrayList<View> vs, final Question q,
			TextView twoSiteNoticeTv, ArrayList<QuestionItem> mTempRows,
			int position) {
		final HashMap<Integer, Integer> checkMatrixRadioMap = new HashMap<Integer, Integer>();
		final ArrayList<int[]> checkList = new ArrayList<int[]>();
		q.hideCount = 0;
		q.realRows = 0;
		/**
		 * 标题最大宽度
		 */

		/**
		 * 题型的横向、纵向摆放
		 */
		bodyView.setOrientation(LinearLayout.VERTICAL);
		// }
		/**
		 * 获取行标题, 局部变量移除无碍, 不可直接对q.getRowItemArr()做remove或clear操作,
		 * 所以申请rRows空间暂存q.getRowItemArr()中的值, 其他的做法与此类似
		 */
		ArrayList<QuestionItem> rRows = new ArrayList<QuestionItem>();
		rRows.addAll(q.getRowItemArr());

		/**
		 * 获取列标题
		 */
		ArrayList<QuestionItem> rColmns = new ArrayList<QuestionItem>();
		rColmns.addAll(q.getColItemArr());
		final ArrayList<QuestionItem> tempRColmns = rColmns;
		boolean isInclusion = false;

		/**
		 * 排斥
		 */
		if ("1".equals(q.qInclusion)) {
			isInclusion = true;
			// Question _q =
			// ma.dbService.getQuestion(feed.getSurveyId(),
			// q.qSiteOption);
			Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
			ArrayList<AnswerMap> aml = an.getAnswerMapArr();
			ArrayList<Integer> have = new ArrayList<Integer>();
			for (AnswerMap am : aml) {
				if (!Util.isEmpty(am.getAnswerValue())) {
					/**
					 * 假如有值
					 */
					if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
						have.add(am.getRow());
					} else {
						have.add(Integer
								.parseInt(am.getAnswerName().split("_")[3]));
					}
				}
			}
			aml.clear();
			aml = null;
			// 大树 一下 双引用 单选矩阵
			if (!Util.isEmpty(q.qSiteOption2)) {
				// 大树 输出 把所有要排斥的选项添加到集合hava中
				if (twoQsiteOptions.size() > 0) {
					for (int i = 0; i < twoQsiteOptions.size(); i++) {
						Answer anQsite = ma.dbService.getAnswer(feed.getUuid(),
								twoQsiteOptions.get(i));
						if (anQsite != null
								&& anQsite.getAnswerMapArr().size() > 0) {
							for (AnswerMap am : anQsite.getAnswerMapArr()) {
								/**
								 * 假如有值 自动查重 去除重复
								 */
								if (!Util.isEmpty(am.getAnswerValue())) {
									if (Cnt.TYPE_RADIO_BUTTON == anQsite.answerType) {
										have.add(am.getRow());
									} else {
										have.add(Integer.parseInt(am
												.getAnswerName().split("_")[3]));
									}
								}
							}
						}
					}
				}
			}
			twoQsiteOptions.add(0, q.qSiteOption);
			// 大树 以上部分 单选矩阵
			for (int i = 0; i < q.getRowItemArr().size(); i++) {
				QuestionItem qi = q.getRowItemArr().get(i);
				if (-1 != have.indexOf(qi.itemValue) && 1 != qi.itemShow) {
					/**
					 * 选择了这些选项
					 */
					rRows.remove(qi);
				}
			}
			have.clear();
			have = null;
			mTempRows.addAll(rRows);
		} else if ("0".equals(q.qInclusion)) {
			/**
			 * 引用
			 */
			isInclusion = true;
			// Question _q =
			// ma.dbService.getQuestion(feed.getSurveyId(),
			// q.qSiteOption);
			Answer an = ma.dbService.getAnswer(feed.getUuid(), q.qSiteOption);
			if (an != null) {
				ArrayList<AnswerMap> aml = an.getAnswerMapArr();
				ArrayList<Integer> have = new ArrayList<Integer>();
				for (AnswerMap am : aml) {
					/**
					 * 假如有值
					 */
					if (!Util.isEmpty(am.getAnswerValue())) {
						if (Cnt.TYPE_RADIO_BUTTON == an.answerType) {
							have.add(am.getRow());
						} else {
							have.add(Integer.parseInt(am.getAnswerName().split(
									"_")[3]));
						}
					}
				}
				aml.clear();
				aml = null;

				// 大树 添加 双引用在里实现 以下： 双引用 单选矩阵
				if (!Util.isEmpty(q.qSiteOption2)) {
					// 大树 输出 自动查重 的功能
					if (twoQsiteOptions.size() > 0) {
						for (int i = 0; i < twoQsiteOptions.size(); i++) {

							Answer anQsite = ma.dbService.getAnswer(
									feed.getUuid(), twoQsiteOptions.get(i));
							if (anQsite != null
									&& anQsite.getAnswerMapArr().size() > 0) {
								for (AnswerMap am : anQsite.getAnswerMapArr()) {
									/**
									 * 假如有值 自动查重 去除重复
									 */
									if (!Util.isEmpty(am.getAnswerValue())) {
										if (Cnt.TYPE_RADIO_BUTTON == anQsite.answerType) {
											have.add(am.getRow());
										} else {
											have.add(Integer
													.parseInt(am
															.getAnswerName()
															.split("_")[3]));
										}
									}
								}
							}
						}
					}

				}
				twoQsiteOptions.add(0, q.qSiteOption);
				// 单选矩阵 大树 双引用 的显示
				for (int i = 0; i < q.getRowItemArr().size(); i++) {
					QuestionItem qi = q.getRowItemArr().get(i);
					if (-1 == have.indexOf(qi.itemValue) && 1 != qi.itemShow) {

						/**
						 * 没有选择的, 则移走
						 */
						rRows.remove(qi);
					}
				}
				have.clear();
				have = null;
				mTempRows.addAll(rRows);
			} else {
				rRows.retainAll(rRows);
				mTempRows.addAll(rRows);
			}
		}

		// 大树 添加 垂直结构的 实现 提示语 水平结构 提示语 都在这里 引用排斥 双引用 单选矩阵 的处理 修改一下
		if (!Util.isEmpty(q.qSiteOption2) || !Util.isEmpty(q.qSiteOption)) {
			twoSiteNoticeTv.setTextColor(Color.RED);
			twoSiteNoticeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					lowSurveySize);
			StringBuilder sb1 = new StringBuilder();
			if (twoQsiteOptions.size() > 0) {
				for (int i = 0; i < twoQsiteOptions.size(); i++) {
					Log.i("zrl1", twoQsiteOptions.get(i) + "双引用题目编号：");
					Question twoq = ma.dbService.getQuestion(
							feed.getSurveyId(), twoQsiteOptions.get(i));
					if (null != twoq) {
						if (!Util.isEmpty(twoq.qOrder + "")) {
							if (!Util.isEmpty(twoq.qid)) {
								sb1.append(twoq.qid + ",");
							} else {
								sb1.append("Q" + twoq.qOrder + ",");
							}
						} else
							Log.i("zrl1", "qid为空");
					}
				}
			}
			if (q.qInclusion.equals("0") && -1 != sb1.toString().indexOf(",")) {
				twoSiteNoticeTv.setText(NativeModeNoPageActivity.this
						.getString(R.string.each_answer_associated)
						+ sb1.substring(0, sb1.toString().lastIndexOf(","))
								.toString()
						+ NativeModeNoPageActivity.this
								.getString(R.string.answer_value_reference));
			} else if (q.qInclusion.equals("1")
					&& -1 != sb1.toString().indexOf(",")) {
				twoSiteNoticeTv.setText(NativeModeNoPageActivity.this
						.getString(R.string.each_answer_associated)
						+ sb1.substring(0, sb1.toString().lastIndexOf(","))
								.toString()
						+ NativeModeNoPageActivity.this
								.getString(R.string.answer_value_rejection));
			}
		}

		// 大树 以上部分
		/**
		 * 题外关联 之 选项置顶 设置 标示 置顶 选项 置底 isItemBottom
		 * 
		 * 获取 所有选项置顶的 选项 集合 还有别忘啦 提示语的 显示 出来
		 */
		boolean isItemStick = ComUtil.isItemStick(rRows)[0];
		int[] itemList = ComUtil.getItemStick(rRows);// 置顶标示位置 数组
		int counter = 0; // 计数器
		int botValue = 0;// 置底 临时 位置 标示 底部
		int itemCount = rRows.size();// 所有的 选项 数量

		/**
		 * 假如是随机题目 则将其行和列都随机一遍
		 */
		if (1 == q.qRadomed) {
			/**
			 * 行随机暂存行中的数据
			 */
			ArrayList<QuestionItem> _tmpRows = new ArrayList<QuestionItem>();
			Random rd = new Random();
			int size = 0;
			if (isInclusion) {
				size = rRows.size();
			} else {
				size = q.getRowItemArr().size();
			}
			for (int i = 0; i < size; i++) {
				/**
				 * 随机产生数组的下标值
				 */
				int index = rd.nextInt(rRows.size());

				/**
				 * 题外关联 之 选项置顶 单选矩阵 的实现 选项置底 等操作的实现 过程
				 */
				QuestionItem item = rRows.get(index);
				if (null != item.padding && counter == 0 && item.padding != 1) {
					if (isItemStick) {
						index = itemList[0];
						item = rRows.get(index);
					}
				} else if (null != item.padding && counter != itemCount - 1
						&& item.padding == 2) {
					botValue = index;
					while (true) {
						index = rd.nextInt(rRows.size());
						if (index != botValue) {
							item = rRows.get(index);
							break;
						}
					}
				}
				counter++;

				_tmpRows.add(item);
				/**
				 * 取得随机产生的选项对象item
				 */

				// _tmpRows.add(rRows.get(index)); 注释 语句 这条
				/**
				 * 在暂存数组中移除随机产生的选项对象item
				 */
				rRows.remove(index);
			}

			rRows.addAll(_tmpRows);
			_tmpRows.clear();

			/**
			 * 列随机暂存列中的数据
			 */
			// ArrayList<QuestionItem> tmpCols = new
			// ArrayList<QuestionItem>();
			// for (int i = 0; i < q.getColItemArr().size(); i++) {
			// /**
			// * 随机产生数组的下标值
			// */
			// int index = rd.nextInt(rColmns.size());
			// /**
			// * 取得随机产生的选项对象item
			// */
			// tmpCols.add(rColmns.get(index));
			// /**
			// * 在暂存数组中移除随机产生的选项对象item
			// */
			// rColmns.remove(index);
			// }
			// rColmns.addAll(tmpCols);
			// tmpCols.clear();
		}

		/**
		 * 假如题干的宽度大于或等于屏幕宽度的3/4
		 */
		boolean isBeyond = ((screenWidth * 2 / 3 - 20)) <= rColmns.size() * 100;

		// 处理非随机矩阵
		/**
		 * 遍历每一行
		 */
		// 新加的
		int rightNum = (q.isRight == 1) ? 1 : 0;
		for (int r = 0; r < rRows.size() + 1; r++) {
			final ArrayList<View> rowViews = new ArrayList<View>();
			QuestionItem rowItem = null;
			if (0 != r) {
				// System.out.println("row=" + rRows.get(r -
				// 1).itemText);
				rowItem = rRows.get(r - 1);
				/**
				 * 题外关联 之 选项置顶 单选矩阵 把 itemValue 给注释掉 即可
				 */
				// rowItem.itemValue = r - 1;
			}

			/**
			 * 遍历每一列
			 */
			final RadioGroup rg = new RadioGroup(NativeModeNoPageActivity.this);// 每一列的
			rg.setVerticalGravity(Gravity.CENTER_VERTICAL);
			rg.setBackgroundColor(Color.TRANSPARENT);
			if (q.qStarCheck == 1) {
				rg.setBackgroundResource(R.drawable.piont_lv);
			}
			RadioGroup.LayoutParams rgPar = new RadioGroup.LayoutParams(
					(screenWidth * 2 / 3 - 20), LayoutParams.WRAP_CONTENT);
			rgPar.setMargins(0, 0, 0, 0);
			rg.setLayoutParams(rgPar);
			rg.setPadding(0, 0, 0, 0);
			rg.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout ll = new LinearLayout(NativeModeNoPageActivity.this);
			ll.setVerticalGravity(Gravity.CENTER_VERTICAL);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setLayoutParams(FILL_WRAP);
			// 新加的
			for (int c = 0; c < rColmns.size() + 1 + rightNum; c++) {
				QuestionItem colItem = null;
				// 新加的
				if (0 != c) {
					if (rightNum == 1 && rColmns.size() + rightNum == c) {

					} else {
						colItem = rColmns.get(c - 1);
					}
				}

				TextView tvTb = new TextView(NativeModeNoPageActivity.this);
				tvTb.setLayoutParams(WRAP_WRAP);
				tvTb.setGravity(Gravity.FILL);
				tvTb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
				tvTb.setWidth(100);
				tvTb.setPadding(2, 2, 2, 2);
				if (0 == r) {// 如过是第一行, 则打印出每一列的值
					// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
					ll.setBackgroundColor(Color.LTGRAY);// Color.LTGRAY
					if (0 == c) {// 打印表头
						tvTb.setText("");
						// 矩阵右侧
						if (q.isRight == 1) {
							tvTb.setWidth(screenWidth / 6);
						} else {
							tvTb.setWidth(screenWidth / 3);
						}

					}
					// 矩阵右侧 新加的
					else if (rColmns.size() + rightNum == c && q.isRight == 1) {
						tvTb.setText("");
						tvTb.setWidth(screenWidth / 6);
					} else {// c !=0 打印每列的,即列标题
							// System.out.println("c_colmns_item_value=" +
							// rColmns.get(c - 1).itemValue + ", colmns=" +
							// (c -
							// 1));
						tvTb.setTextColor(Color.BLACK);
						if (isBeyond) {
							/**
							 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
							 */
							// 矩阵百分比
							tvTb.setWidth((screenWidth * 2 / 3 - 20)
									/ rColmns.size());
						} else {
							tvTb.setWidth((screenWidth * 2 / 3 - 20)
									/ rColmns.size());
						}
						System.out.println("c:" + c);
						System.out.println("colItem.itemText:"
								+ colItem.itemText);
						String t = colItem.itemText;

						// ***********************************样式处理**************************//
						// CstmMatcher cm = Util.findFontMatcherList(t);
						// if (!Util.isEmpty(cm.getMis())) {
						// t = cm.getResultStr();
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : cm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end &&
						// mi.end <= t.length())
						// ss.setSpan(new ForegroundColorSpan(mi.color),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end &&
						// mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start,
						// mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// }
						// tvTb.setText(ss);
						// //
						// } else {
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// t = bCm.getResultStr();
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end &&
						// mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start,
						// mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// tvTb.setText(ss);
						// } else {
						// tvTb.setText(t);
						// }
						//
						// }
						final float Width = (screenWidth * 2 / 3 - 20)
								/ rColmns.size();
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeNoPageActivity.this
										.getFilesDir()
										+ File.separator
										+ "survey"
										+ File.separator
										+ feed.getSurveyId()
										+ File.separator
										+ source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									float tWidth = image.getWidth();
									float tHeight = image.getHeight();
									if (tWidth > Width) {
										tHeight = Width / tWidth * tHeight;
										tWidth = Width;
									}
									drawable.setBounds(0, 0, (int) tWidth,
											(int) tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(t, imgGetter, null);
						tvTb.setText(fromHtml);
						if (q.qStarCheck != 0 && c != 1 && c != rColmns.size()) {
							tvTb.setText("");
						}
					}
					// ***********************************样式处理**************************//
					// CstmMatcher cm =
					// Util.findImageMatherList(tvTb.getText().toString());
					// if (0 < cm.getMis().size()) {
					// LinearLayout _im = new
					// LinearLayout(NativeModeNoPageActivity.this);
					// _im.setOrientation(LinearLayout.VERTICAL);
					// _im.setGravity(Gravity.FILL);
					// _im.setLayoutParams(
					// new LayoutParams(isBeyond ? 100 : (screenWidth * 2 / 3 -
					// 20) / rColmns.size(),
					// LayoutParams.WRAP_CONTENT));
					// tvTb.setText(cm.getResultStr());
					// _im.addView(tvTb, _im.getChildCount());
					// for (int i = 0; i < cm.getMis().size(); i++) {
					// MatcherItem mi = cm.getMis().get(i);
					// ImageView iv = new
					// ImageView(NativeModeNoPageActivity.this);
					// iv.setPadding(2, 2, 2, 2);
					// String path =
					// Util.getImagePath(NativeModeNoPageActivity.this,
					// feed.getSurveyId(), mi.name);
					// // 图片百分比开始
					// // iv.setLayoutParams(WRAP_WRAP);
					// Bitmap image = BitmapFactory.decodeFile(path);
					// int tWidth = image.getWidth();
					// int tHeight = image.getHeight();
					// iv.setLayoutParams(new LinearLayout.LayoutParams(tWidth,
					// tHeight));
					// // 图片百分比结束
					// iv.setImageURI(Uri.parse(path));
					// _im.addView(iv, _im.getChildCount());
					// iv.setOnLongClickListener(new
					// ImageLongClickListener(path));
					// }
					// ll.addView(_im, ll.getChildCount());
					// } else {
					// ll.addView(tvTb, ll.getChildCount());
					// }
					ll.addView(tvTb, ll.getChildCount());
				} else {
					if (q.realRows % 2 == 0) {
						ll.setBackgroundColor(Color.parseColor("#F0F0F0"));
					} else {
						ll.setBackgroundColor(Color.TRANSPARENT);
					}
					if (0 == c) {
						/**
						 * 打印行标题
						 */
						tvTb.setBackgroundResource(R.drawable.small_text_background);
						tvTb.setTextColor(Color.BLACK);
						// 矩阵右侧
						if (1 == q.isRight) {
							tvTb.setWidth(screenWidth / 6);
						} else {
							tvTb.setWidth(screenWidth / 3);
						}

						// tvTb.setBackgroundColor(Color.TRANSPARENT);
						String t = rowItem.itemText;

						/**
						 * 题外关联 之 选项置顶 单选矩阵 的提示语的 显示 出来
						 */
						if (null != rowItem.padding) {
							if (rowItem.padding == 1) {
								t = rowItem.itemText + "<font color=red>  "
										+ this.getString(R.string.option_top)
										+ "</font>";
							} else if (rowItem.padding == 2) {
								t = rowItem.itemText
										+ "<font color=red>  "
										+ this.getString(R.string.option_bottom)
										+ "</font>";
							}
						}
						// ***********************************样式处理**************************//

						// if (!Util.isEmpty(t)) {
						// CstmMatcher cm = Util.findFontMatcherList(t);
						// if (!Util.isEmpty(cm.getMis())) {
						// t = cm.getResultStr();
						// if (!Util.isEmpty(t)) {
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : cm.getMis()) {
						// ss.setSpan(new ForegroundColorSpan(mi.color),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end
						// && mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// }
						// tvTb.setText(ss);
						// } else {
						// tvTb.setText(rowItem.itemText);
						// }
						// //
						// } else {
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// t = bCm.getResultStr();
						// if (!Util.isEmpty(t)) {
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end
						// && mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// tvTb.setText(ss);
						// } else {
						// tvTb.setText(rowItem.itemText);
						// }
						// } else {
						// tvTb.setText(t);
						// }
						//
						// }
						//
						// }
						// 样式修改 以上注释
						// if (!Util.isEmpty(t)) {
						// // t=t.replace("color=Khaki", "color=#FDF8BF");
						// tvTb.setText(Html.fromHtml(t));
						// }
						// // ******** 图片 加载 ******************
						// CstmMatcher cm =
						// Util.findImageMatherList(tvTb.getText().toString());
						// if (0 < cm.getMis().size()) {
						// LinearLayout _im = new
						// LinearLayout(NativeModeNoPageActivity.this);
						// _im.setOrientation(LinearLayout.VERTICAL);
						// _im.setGravity(Gravity.FILL);
						// _im.setLayoutParams(new LayoutParams(screenWidth / 3,
						// LayoutParams.WRAP_CONTENT));
						// // _im.setLayoutParams(new
						// // LayoutParams(isBeyond
						// // ? 100 : (screenWidth * 2 / 3 - 20) /
						// // rColmns.size(),
						// // LayoutParams.WRAP_CONTENT));
						// tvTb.setText(cm.getResultStr());
						// _im.addView(tvTb, _im.getChildCount());
						// for (int i = 0; i < cm.getMis().size(); i++) {
						// MatcherItem mi = cm.getMis().get(i);
						// ImageView iv = new
						// ImageView(NativeModeNoPageActivity.this);
						// iv.setPadding(2, 2, 2, 2);
						// String path =
						// Util.getImagePath(NativeModeNoPageActivity.this,
						// feed.getSurveyId(),
						// mi.name);
						// // 图片百分比开始
						// // iv.setLayoutParams(WRAP_WRAP);
						// Bitmap image = BitmapFactory.decodeFile(path);
						// int tWidth = image.getWidth();
						// int tHeight = image.getHeight();
						// iv.setLayoutParams(new
						// LinearLayout.LayoutParams(tWidth, tHeight));
						// // 图片百分比结束
						// iv.setImageURI(Uri.parse(path));
						// _im.addView(iv, _im.getChildCount());
						// iv.setOnLongClickListener(new
						// ImageLongClickListener(path));
						// }
						// ll.addView(_im, ll.getChildCount());
						// } else {
						// }
						if (!Util.isEmpty(t)) {
							final float Width = (screenWidth * 2 / 3 - 20)
									/ rColmns.size();
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" +
									// name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > Width) {
											tHeight = Width / tWidth * tHeight;
											tWidth = Width;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html
									.fromHtml(t, imgGetter, null);
							tvTb.setText(fromHtml);
						}
						ll.addView(tvTb, ll.getChildCount());

						// ***********************************样式处理**************************//
						// if (1 == q.qStarCheck) {
						// TextView tvLine = new
						// TextView(NativeModeNoPageActivity.this);
						// tvLine.setBackgroundColor(Color.LTGRAY);
						// tvLine.setLayoutParams(new LayoutParams(3,
						// LayoutParams.MATCH_PARENT));
						// ll.addView(tvLine);
						// }
					}
					// 矩阵右侧 新加的
					else if (rColmns.size() + rightNum == c && q.isRight == 1) {
						/**
						 * 打印行标题
						 */
						tvTb.setBackgroundResource(R.drawable.small_text_background);
						tvTb.setTextColor(Color.BLACK);
						// 矩阵右侧
						tvTb.setWidth(screenWidth / 6);
						tvTb.setGravity(Gravity.FILL);
						// tvTb.setBackgroundColor(Color.TRANSPARENT);
						String t = rowItem.itemTextRight;
						// ***********************************样式处理**************************//

						// if (!Util.isEmpty(t)) {
						// CstmMatcher cm = Util.findFontMatcherList(t);
						// if (!Util.isEmpty(cm.getMis())) {
						// t = cm.getResultStr();
						// if (!Util.isEmpty(t)) {
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : cm.getMis()) {
						// ss.setSpan(new ForegroundColorSpan(mi.color),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end
						// && mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// }
						// tvTb.setText(ss);
						// } else {
						// tvTb.setText(rowItem.itemText);
						// }
						// //
						// } else {
						// CstmMatcher bCm = Util.findBoldMatcherList(t);
						// if (!Util.isEmpty(bCm.getMis())) {
						// t = bCm.getResultStr();
						// if (!Util.isEmpty(t)) {
						// SpannableString ss = new SpannableString(t);
						// for (MatcherItem mi : bCm.getMis()) {
						// if (null != mi && -1 != mi.start && -1 != mi.end
						// && mi.end <= t.length()) {
						// ss.setSpan(new StyleSpan(Typeface.BOLD),
						// mi.start, mi.end,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// ss.setSpan(new RelativeSizeSpan(1.3f), mi.start,
						// mi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// }
						// }
						// tvTb.setText(ss);
						// } else {
						// tvTb.setText(rowItem.itemText);
						// }
						// } else {
						// tvTb.setText(t);
						// }
						//
						// }
						//
						// }
						// 样式修改 以上注释
						if (!Util.isEmpty(t)) {
							// t=t.replace("color=Khaki", "color=#FDF8BF");
							tvTb.setText(Html.fromHtml(t));
						}
						// ******** 图片 加载 ******************
						// CstmMatcher cm =
						// Util.findImageMatherList(tvTb.getText().toString());
						// if (0 < cm.getMis().size()) {
						// LinearLayout _im = new
						// LinearLayout(NativeModeNoPageActivity.this);
						// _im.setOrientation(LinearLayout.VERTICAL);
						// _im.setGravity(Gravity.FILL);
						// _im.setLayoutParams(new LayoutParams(screenWidth / 3,
						// LayoutParams.WRAP_CONTENT));
						// // _im.setLayoutParams(new
						// // LayoutParams(isBeyond
						// // ? 100 : (screenWidth * 2 / 3 - 20) /
						// // rColmns.size(),
						// // LayoutParams.WRAP_CONTENT));
						// tvTb.setText(cm.getResultStr());
						// _im.addView(tvTb, _im.getChildCount());
						// for (int i = 0; i < cm.getMis().size(); i++) {
						// MatcherItem mi = cm.getMis().get(i);
						// ImageView iv = new
						// ImageView(NativeModeNoPageActivity.this);
						// iv.setPadding(2, 2, 2, 2);
						// String path =
						// Util.getImagePath(NativeModeNoPageActivity.this,
						// feed.getSurveyId(),
						// mi.name);
						// // 图片百分比开始
						// // iv.setLayoutParams(WRAP_WRAP);
						// Bitmap image = BitmapFactory.decodeFile(path);
						// int tWidth = image.getWidth();
						// int tHeight = image.getHeight();
						// iv.setLayoutParams(new
						// LinearLayout.LayoutParams(tWidth, tHeight));
						// // 图片百分比结束
						// iv.setImageURI(Uri.parse(path));
						// _im.addView(iv, _im.getChildCount());
						// iv.setOnLongClickListener(new
						// ImageLongClickListener(path));
						// }
						// ll.addView(_im, ll.getChildCount());
						// } else {
						// ll.addView(tvTb, ll.getChildCount());
						// }
						if (!Util.isEmpty(t)) {
							final float Width = (screenWidth * 2 / 3 - 20)
									/ rColmns.size();
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > Width) {
											tHeight = Width / tWidth * tHeight;
											tWidth = Width;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html
									.fromHtml(t, imgGetter, null);
							tvTb.setText(fromHtml);
						}
						ll.addView(tvTb, ll.getChildCount());
					} else {// 打印单选按钮
						if (1 == c) {
							ll.addView(rg);
						}
						// QuestionItem item = rRows.get(r - 1);
						if (null == rowItem
								|| (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
							// 空其他项,即只有一个<freeInput/>标签
							continue;
						}
						// System.out.println("r_row_item_value=" +
						// rowItem.itemValue + ", r=" + (r - 1));
						RadioButton radio = new RadioButton(
								NativeModeNoPageActivity.this);
						radio.setLayoutParams(WRAP_WRAP);
						radio.setGravity(Gravity.FILL | Gravity.CENTER_VERTICAL);
						// radio.setGravity(Gravity.CENTER);

						AnswerMap am = new AnswerMap();
						String name = Util.GetAnswerName(q, rowItem,
								rowItem.itemValue, 0, false, false);
						am.setAnswerName(name);
						/**
						 * 相对的
						 */
						// am.setRow(realRows-1);
						am.setRow(rowItem.itemValue);
						/**
						 * 绝对的
						 */
						am.setCol(colItem.itemValue);
						// radio.setOnClickListener(new On(rowItem,
						// colItem));
						// System.out.println("单选矩阵随机--->Key(" +
						// rowItem.itemText + "," + colItem.itemText +
						// ")====>Value(" + rowItem.itemValue + ", " +
						// colItem.itemValue + ")");
						am.setAnswerValue(String.valueOf(colItem.itemValue));
						radio.setTag(am);
						// radio.setTag(key, tag)
						rg.addView(radio, rg.getChildCount());
						if (!Util.isEmpty(amList)) {
							for (AnswerMap tam : amList) {
								if (name.equals(tam.getAnswerName())
										&& am.getAnswerValue().equals(
												tam.getAnswerValue())) {
									// System.out.println("匹配--->name="+name+",
									// value="+am.getAnswerValue());
									radio.setChecked(true);
								}
							}
						}
						radio.setPadding(2, 2, 2, 2);
						radio.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						radio.setTextColor(Color.BLACK);
						// radio.setButtonDrawable(R.drawable.small_radiobutton_temp);
						radio.setButtonDrawable(R.drawable.small_radiobutton);
						radio.setBackgroundResource(R.drawable.small_radiobutton_background);
						if (q.qStarCheck != 0) {
							int drawable = R.drawable.small_radiobutton;
							radio.setCompoundDrawablesWithIntrinsicBounds(
									getResources().getDrawable(drawable), null,
									null, null);
							radio.setButtonDrawable(android.R.color.transparent);
							radio.setBackgroundResource(R.drawable.small_text_background);
							if (q.realRows % 2 == 0) {
								radio.setBackgroundColor(Color.WHITE);
							} else {
								radio.setBackgroundColor(Color
										.parseColor("#F0F0F0"));
							}
							switch (q.qStarCheck) {
							case 1:
								drawable = android.R.color.transparent;
								TextView tvLine = new TextView(
										NativeModeNoPageActivity.this);
								if (q.realRows % 2 == 0) {
									tvLine.setBackgroundColor(Color
											.parseColor("#F0F0F0"));
									radio.setBackgroundColor(Color.WHITE);
								} else {
									tvLine.setBackgroundColor(Color.WHITE);
									radio.setBackgroundColor(Color
											.parseColor("#F0F0F0"));
								}
								tvLine.setLayoutParams(new LayoutParams(3,
										LayoutParams.MATCH_PARENT));
								rg.addView(tvLine);
								break;
							case 2:
								drawable = R.drawable.star_24_off;
								break;
							case 3:
								drawable = R.drawable.hand_24_off;
								break;
							case 4:
								drawable = R.drawable.heart_24_off;
								break;
							}
							radio.setCompoundDrawablesWithIntrinsicBounds(
									getResources().getDrawable(drawable), null,
									null, null);
						}
						rowViews.add(radio);
						radio.setWidth(100);
						if (isBeyond) {
							/**
							 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
							 */
							// 矩阵百分比
							radio.setLayoutParams(new LinearLayout.LayoutParams(
									(screenWidth * 2 / 3 - 20) / rColmns.size(),
									LinearLayout.LayoutParams.WRAP_CONTENT));
							radio.setWidth((screenWidth * 2 / 3 - 20)
									/ rColmns.size());
						} else {
							// System.out.println("设置了Radio的宽度");
							radio.setWidth((screenWidth * 2 / 3 - 20)
									/ rColmns.size());
						}
						// radio.setHeight(100);
						vs.add(radio);
						radio.setOnClickListener(new MatrixRadioBtListener(q,
								vs, rowViews, mTempRows, position));
						// radio.setOnClickListener(new OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// Toasts.makeText(ma, "您选择了:"+t,
						// Toast.LENGTH_SHORT).show();
						// }
						// });
						// 单选矩阵选项排斥
						if ("true".equals(colItem.exclude)
								&& !Util.isEmpty(colItem.excludeIn)) {
							String excludeIn = colItem.excludeIn;
							String[] excludeValue = excludeIn.split(",");
							if (excludeValue.length > 0) {
								for (int i = 0; i < excludeValue.length; i++) {
									if (Integer
											.parseInt(excludeValue[i].trim()) == (rowItem
											.getItemValue())) {
										radio.setChecked(false);
										radio.setVisibility(View.INVISIBLE);
									}
								}
							}
						}
					}

				}
			}

			if (0 != q.qStarCheck) {
				for (int i = 0; i < rowViews.size(); i++) {
					RadioButton rb = (RadioButton) rowViews.get(i);
					if (rb.isChecked()) {
						for (int j = 0; j < rowViews.size(); j++) {
							int drawable = R.drawable.small_radiobutton;
							switch (q.qStarCheck) {
							case 1:
								drawable = android.R.color.transparent;
								TextView tvLine = new TextView(
										NativeModeNoPageActivity.this);
								if (q.realRows % 2 == 0) {
									tvLine.setBackgroundColor(Color
											.parseColor("#F0F0F0"));
									((RadioButton) rowViews.get(j))
											.setBackgroundResource(R.color.white);
								} else {
									tvLine.setBackgroundColor(Color.WHITE);
									((RadioButton) rowViews.get(j))
											.setBackgroundResource(R.color.Radio_back);
								}
								tvLine.setLayoutParams(new LayoutParams(3,
										LayoutParams.MATCH_PARENT));
								rg.addView(tvLine);
								break;
							case 2:
								drawable = R.drawable.star_24_off;
								break;
							case 3:
								drawable = R.drawable.hand_24_off;
								break;
							case 4:
								drawable = R.drawable.heart_24_off;
								break;
							}
							((RadioButton) rowViews.get(j))
									.setCompoundDrawablesWithIntrinsicBounds(
											getResources()
													.getDrawable(drawable),
											null, null, null);
						}
						for (int j = 0; j < i + 1; j++) {
							int drawable = R.drawable.small_radiobutton;
							switch (q.qStarCheck) {
							case 1:
								((RadioButton) rowViews.get(j))
										.setBackgroundResource(R.color.transparent);
								drawable = android.R.color.transparent;
								break;
							case 2:
								drawable = R.drawable.star_24;
								break;
							case 3:
								drawable = R.drawable.hand_24;
								break;
							case 4:
								drawable = R.drawable.heart_24;
								break;
							}
							((RadioButton) rowViews.get(j))
									.setCompoundDrawablesWithIntrinsicBounds(
											getResources()
													.getDrawable(drawable),
											null, null, null);
						}
					}
				}
			}
			for (int i = 0; i < rowViews.size(); i++) {
				RadioButton rb = (RadioButton) rowViews.get(i);
				if (rb.isChecked()) {
					AnswerMap aMap = (AnswerMap) rb.getTag();
					int row = aMap.getRow();
					int col = aMap.getCol();
					int comparison = tempRColmns.get(col).comparison;
					String itemText = tempRColmns.get(col).itemText;
					System.out.println("comparison=" + comparison);
					if (0 < comparison) {
						Integer count = checkMatrixRadioMap.get(col);
						System.out.println("count=" + count);
						if (null == count) {
							checkMatrixRadioMap.put(col, 1);
						} else {
							checkMatrixRadioMap.put(col, count + 1);
							System.out.println("checkMatrixRadioMap.get(col)="
									+ checkMatrixRadioMap.get(col));
						}
					}
				}
				rb.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							RadioButton rb = (RadioButton) v;
							AnswerMap aMap = (AnswerMap) rb.getTag();
							int row = aMap.getRow();
							int col = aMap.getCol();
							QuestionItem qi = null;
							for (QuestionItem qItem : tempRColmns) {
								if (qItem.getItemValue() == col) {
									qi = qItem;
								}
							}
							if (null != qi) {
								int comparison = qi.comparison;
								String itemText = qi.itemText;
								System.out.println("comparison=" + comparison);
								if (0 < comparison) {
									if (!rb.isChecked()) {
										Integer count = checkMatrixRadioMap
												.get(col);
										System.out.println("count=" + count);
										if (null == count) {
											// checkMatrixRadioMap.put(col, 1);
										} else {
											if (comparison <= count) {
												rg.check(0);
												Message msg = Message.obtain();
												msg.what = STATE_CHECK_COUNT;
												msg.obj = getResources()
														.getString(
																R.string.check_sum_bound,
																itemText + "",
																comparison);
												handler.sendMessage(msg);
												return true;
											}
										}
									}
								}
							}
						}

						return false;
					}
				});
				rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						System.out.println("onCheckedChanged");
						AnswerMap aMap = (AnswerMap) buttonView.getTag();
						int row = aMap.getRow();
						int col = aMap.getCol();
						QuestionItem qi = null;
						for (QuestionItem qItem : tempRColmns) {
							if (qItem.getItemValue() == col) {
								qi = qItem;
							}
						}
						if (null != qi) {
							int comparison = qi.comparison;
							String itemText = qi.itemText;
							System.out.println("comparison=" + comparison);
							if (0 < comparison) {
								if (isChecked) {
									Integer count = checkMatrixRadioMap
											.get(col);
									System.out.println("count=" + count);
									if (null == count) {
										checkMatrixRadioMap.put(col, 1);
									} else {
										if (comparison <= count) {
											//
										} else {
											checkMatrixRadioMap.put(col,
													count + 1);
											System.out
													.println("checkMatrixRadioMap.get(col)="
															+ checkMatrixRadioMap
																	.get(col));
										}
										// isCheckedCount=true;
									}
								} else {
									Integer count = checkMatrixRadioMap
											.get(col);
									if (null != count) {
										checkMatrixRadioMap.put(col, count - 1);
										System.out
												.println("checkMatrixRadioMap.get(col)="
														+ checkMatrixRadioMap
																.get(col));
									}
								}
							}
						}
						if (0 < q.qContinuous) {
							if (isChecked) {// 如果此选项被选中
								// 先判断与最近的一个是不是同一行
								// 1如果是则替换当前的记录
								// 2如果不是则判断是否连续超过了限制
								// 1超过限制则提示
								// 2未超过则继续记录当前选择
								if (!Util.isEmpty(checkList)) {
									int[] checkCount = checkList.get(0);// 数组第一项为row第二项为col第三项为连续选择数量
									// 先判断与最近的一个是不是同一行
									if (row == checkCount[0]) {// 1如果是则替换当前的记录
										if (1 < checkList.size()) {
											int[] checkCount1 = checkList
													.get(1);
											if (col == checkCount1[1]) {// 如果与第二条记录是同一列则递增
												checkCount[1] = col;
												checkCount[2] = checkCount1[2] + 1;// 重新赋值
											} else {// 如果不是则修改当前
												checkCount[1] = col;
												checkCount[2] = 0;
											}
										} else {// 如果只有一个则修改当前
											checkCount[1] = col;
											checkCount[2] = 0;
										}

									} else {// 2如果不是则新增记录
										// 判断是不是同一列
										if (col == checkCount[1]) {// 如果是同一列则递增
											int[] check = { row, col,
													checkCount[2] + 1 };
											checkList.add(0, check);
										} else {// 如果不是则新增
											int[] check = { row, col, 0 };
											checkList.add(0, check);
										}
									}
									// 判断是否连续超过了限制
									if (q.qContinuous - 1 <= checkList.get(0)[2]) {// 1超过限制则提示
										String tip = Util
												.isEmpty(q.qContinuousText) ? getResources()
												.getString(
														R.string.question_coutinuous_new,
														q.qContinuous)
												: q.qContinuousText;
										Message msg = Message.obtain();
										msg.what = STATE_CONTINUOUS_NEW;
										msg.obj = tip;
										handler.sendMessage(msg);
									} else {// 2未超过则继续记录当前选择

									}
								} else {
									int[] check = { row, col, 0 };
									checkList.add(0, check);
								}

							}
						}
					}
				});
			}
			// Log.i("@@@", "realRows=" +q.realRows);
			q.realRows++;
			if (null != rowItem) {
				if (setHideRb(rowItem, q)) {
					ll.setVisibility(View.GONE);
					boolean isChecked = false;
					for (int i = 0; i < rowViews.size(); i++) {
						RadioButton rb = (RadioButton) rowViews.get(i);
						if (rb.isChecked()) {
							rb.setChecked(false);
							isChecked = true;
						}
					}
					if (isChecked) {
						int success = getQuestionAnswer( false, q, vs,
								null, mTempRows, false);
					}
				}
				// 隐藏选项
				if (1 == rowItem.hide) {
					ll.setVisibility(View.GONE);
				}
			}
			bodyView.addView(ll, bodyView.getChildCount());
		}
		// Log.i("@@@", "hideCount=" + q.hideCount);
		// int success = getQuestionAnswer(MSG_NEXT, false, q, vs, null,
		// mTempRows);
		// }
	}

	public void showResultPage(Integer result, int action) {
		showResultView();
		TextView tvVisitTime = (TextView) vResult
				.findViewById(R.id.visit_time_tv);
		tvVisitTime.setText(getResources().getString(R.string.visit_time,
				Util.getTime(feed.getStartTime(), 5)));
		TextView tvVisitor = (TextView) vResult.findViewById(R.id.visitor_tv);
		tvVisitor.setText(getResources().getString(R.string.visitor,
				feed.getUserId()));
		TextView tvAddr = (TextView) vResult.findViewById(R.id.visit_addr_tv);
		tvAddr.setText(Util.isEmpty(feed.getVisitAddress())//
		? //
		getResources().getString(R.string.null_addr)//
				: //
				getResources().getString(R.string.visit_addr,
						feed.getVisitAddress()));
		TextView tvVisitState = (TextView) vResult
				.findViewById(R.id.visitor_state_tv);
		TextView tvUploadState = (TextView) vResult
				.findViewById(R.id.upload_state_tv);
		TextView tvUploadAlert = (TextView) vResult
				.findViewById(R.id.upload_alert_tv);
		tvUploadAlert.setTextColor(Color.RED);
		tvUploadAlert.setVisibility(View.GONE);
		String str = NativeModeNoPageActivity.this.getResources().getString(
				R.string.upload_false);
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(Color.RED), 6, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (9 == feed.getIsUploaded()) {
			str = NativeModeNoPageActivity.this.getResources().getString(
					R.string.upload_true);
			ss = new SpannableString(str);
			ss.setSpan(new ForegroundColorSpan(Color.BLUE), 6, str.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else if (1 == feed.getIsCompleted()) {
			tvUploadAlert.setVisibility(View.VISIBLE);
		}
		tvUploadState.setText(ss);
		adpter_text_size = (int) (UIUtils
				.getDimenPixelSize(R.dimen.adpter_text_size) * TextSizeManager
				.getInstance().getRealScale());
		Button btnLeft = (Button) vResult.findViewById(R.id.left_btn);
		Button btnRight = (Button) vResult.findViewById(R.id.right_btn);

		switch (result) {
		case 0:
		case 1:
			// tvVisitState.setTextColor(Color.RED);
			String str1 = NativeModeNoPageActivity.this.getResources()
					.getString(R.string.write_failure);
			SpannableString ss1 = new SpannableString(str1);
			ss1.setSpan(new ForegroundColorSpan(Color.RED), 6, str1.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvVisitState.setText(ss1);
			// 大树拒访 拒绝访问
			if (feed.getReturnTypeId() != -2) {
				tvVisitState.setText(NativeModeNoPageActivity.this
						.getResources().getString(R.string.no_fang_mao,
								returnName));
			} else {
				tvVisitState.setText(ss1);
			}
			// 大树拒访
			btnLeft.setTextColor(Color.YELLOW);
			btnLeft.setText(NativeModeNoPageActivity.this.getResources()
					.getString(R.string.giveup));
			btnRight.setTextColor(Color.GREEN);
			btnRight.setText(NativeModeNoPageActivity.this.getResources()
					.getString(R.string.try_again));
			btnLeft.setOnClickListener(new ResultClickListener(1, action));
			btnRight.setOnClickListener(new ResultClickListener(1, action));
			btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, adpter_text_size);
			btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, adpter_text_size);
			// System.out.println("写入失败");
			break;

		case 2:
			// System.out.println("成功!");
			// finish();
			// tvVisitState.setTextColor(Color.GREEN);

			String str3 = NativeModeNoPageActivity.this.getResources()
					.getString(R.string.visited_interrupt);
			/**
			 * 假如是最终保存,则将左边的那个按钮置为“新建”
			 */
			if (MSG_WRITE == action) {
				str3 = NativeModeNoPageActivity.this.getResources().getString(
						R.string.visit_successfully);
				if (1 == feed.getSurvey().openStatus) {
					btnLeft.setVisibility(View.GONE);
				} else {
					btnLeft.setText(NativeModeNoPageActivity.this
							.getResources().getString(R.string.new_panel));
					// 新建限制
					if (1 == feed.getSurvey().oneVisit) {
						btnLeft.setVisibility(View.GONE);
					}
				}
			} else {
				/**
				 * 假如是中间退出,则将左边的那个按钮置为“继续”
				 */
				btnLeft.setText(NativeModeNoPageActivity.this.getResources()
						.getString(R.string._continue));
			}

			SpannableString ss3 = new SpannableString(str3);
			ss3.setSpan(new ForegroundColorSpan(Color.BLUE), 6, str3.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvVisitState.setText(ss3);
			// 大树拒访 12
			if (feed.getReturnTypeId() != -2) {
				tvVisitState.setText(NativeModeNoPageActivity.this
						.getResources().getString(R.string.no_fang_mao,
								returnName));
			} else {
				tvVisitState.setText(ss3);
			}
			// 大树拒访
			btnLeft.setTextColor(Color.WHITE);
			btnRight.setTextColor(Color.BLACK);
			btnRight.setText(NativeModeNoPageActivity.this.getResources()
					.getString(R.string.terminal));
			btnLeft.setOnClickListener(new ResultClickListener(2, action));
			btnRight.setOnClickListener(new ResultClickListener(2, action));
			btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, adpter_text_size);
			btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, adpter_text_size);
			break;
		}
		// TODO Auto-generated method stub
		btnLeft.setVisibility(View.INVISIBLE);
	}

	/**
	 * 单行文本框
	 * 
	 * @param amList
	 * @param bodyView
	 * @param tvCaption
	 * @param q
	 * @param vs
	 * @param mTempRows
	 */
	private void freeTextBox(ArrayList<AnswerMap> amList,
			LinearLayout bodyView, TextView tvCaption, final Question q,
			final ArrayList<View> vs, ArrayList<QuestionItem> mTempRows,
			final int position) {
		q.hideCount = 0;
		q.realRows = 0;
		q.isNew = true;
		/**
		 * 题外关联 之 内部关联 字段 判断 大树 内部关联 2
		 */
		// boolean isInner = false;
		ArrayList<QuestionItem> tbColumns = q.getColItemArr();
		for (int i = 0; i < tbColumns.size(); i++) {
			QuestionItem questionItem = tbColumns.get(i);
			if (questionItem.type == -1) {
				q.isNew = false;
				break;
			}
		}

		if (q.isNew) {
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// 单行文本框
			/**
			 * 题外关联 之 显示关联 的处理 在这里做一下处理 ： 判断 --获取答案---比对---显示 大树 显示关联 1
			 */
			tbColumns = ComUtil.getOutLyingRelevanceDisplayItems(q, feed, ma,
					tbColumns);
			// 表格布局
			// 总布局宽度
			ArrayList<LinearLayout> colsLLy = new ArrayList<LinearLayout>();
			LinearLayout tl = new LinearLayout(NativeModeNoPageActivity.this);
			tl.setLayoutParams(new LinearLayout.LayoutParams(maxCWidth,
					LayoutParams.WRAP_CONTENT));
			tl.setGravity(Gravity.CENTER_VERTICAL);
			bodyView.addView(tl, bodyView.getChildCount());
			tl.setOrientation(LinearLayout.VERTICAL);
			int tbRows = tbColumns.size() / q.freeTextColumn;
			if (0 != tbColumns.size() % q.freeTextColumn) {
				tbRows += 1;
			}
			for (int i = 0; i < tbRows; i++) {
				LinearLayout tbr = new LinearLayout(
						NativeModeNoPageActivity.this);// 每行的TableRow对象
				tbr.setOrientation(LinearLayout.HORIZONTAL);
				tbr.setGravity(Gravity.CENTER_VERTICAL);
				if (0 != tbColumns.size() % q.freeTextColumn && i == tbRows - 1) {
					int overColumn = tbColumns.size() % q.freeTextColumn;
					for (int col = 0; col < overColumn; col++) {
						LinearLayout itemll = new LinearLayout(
								NativeModeNoPageActivity.this);// 每一列的整体布局
						LinearLayout lLeft = new LinearLayout(
								NativeModeNoPageActivity.this);
						LinearLayout lRight = new LinearLayout(
								NativeModeNoPageActivity.this);
						itemll.setOrientation(LinearLayout.HORIZONTAL);
						itemll.setGravity(Gravity.CENTER_VERTICAL);
						itemll.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.MATCH_PARENT));
						itemll.setBackgroundColor(getResources().getColor(
								R.color.gray1));
						lLeft.setOrientation(LinearLayout.HORIZONTAL);
						lLeft.setGravity(Gravity.CENTER_VERTICAL);
						lRight.setOrientation(LinearLayout.HORIZONTAL);
						lRight.setGravity(Gravity.CENTER_VERTICAL);
						colsLLy.add(itemll);
						itemll.addView(lLeft, itemll.getChildCount());
						itemll.addView(lRight, itemll.getChildCount());
						tbr.addView(itemll, tbr.getChildCount());
					}
				} else {
					for (int col = 0; col < q.freeTextColumn; col++) {
						LinearLayout itemll = new LinearLayout(
								NativeModeNoPageActivity.this);// 每一列的整体布局
						LinearLayout lLeft = new LinearLayout(
								NativeModeNoPageActivity.this);
						LinearLayout lRight = new LinearLayout(
								NativeModeNoPageActivity.this);
						itemll.setOrientation(LinearLayout.HORIZONTAL);
						itemll.setGravity(Gravity.CENTER_VERTICAL);
						itemll.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.MATCH_PARENT));
						itemll.setBackgroundColor(getResources().getColor(
								R.color.gray1));
						lLeft.setOrientation(LinearLayout.HORIZONTAL);
						lLeft.setGravity(Gravity.CENTER_VERTICAL);
						lRight.setOrientation(LinearLayout.HORIZONTAL);
						lRight.setGravity(Gravity.CENTER_VERTICAL);
						colsLLy.add(itemll);
						itemll.addView(lLeft, itemll.getChildCount());
						itemll.addView(lRight, itemll.getChildCount());
						tbr.addView(itemll, tbr.getChildCount());
					}
				}
				tl.addView(tbr, tl.getChildCount());
			}
			double fx = 1;
			/**
			 * 循环取左右侧说明框的宽度
			 */
			ArrayList<Float> leftWidAr = new ArrayList<Float>();
			ArrayList<Float> leftRequiredWidAr = new ArrayList<Float>();
			ArrayList<Float> editWidAr = new ArrayList<Float>();
			ArrayList<Float> rightWidAr = new ArrayList<Float>();
			for (int i = 0; i < tbColumns.size(); i++) {
				QuestionItem item = tbColumns.get(i);
				item.itemValue = i;
				float lenRight = 0;
				if (!Util.isEmpty(item.rightsideWord)) {
					TextView tvRight = new TextView(
							NativeModeNoPageActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvRight.setVisibility(View.GONE);
					}
					tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							lowSurveySize);
					tvRight.setTextColor(Color.BLACK);
					tvRight.setText(item.rightsideWord);
					tvRight.setPadding(0, 0, 0, 0);
					TextPaint paintRight = tvRight.getPaint();
					lenRight = (float) paintRight
							.measureText(item.rightsideWord);
					float maxRight = (float) ((maxCWidth / q.freeTextColumn) / 5);
					// float minRight = (float) paintRight.measureText("一");
					if (lenRight > maxRight) {
						lenRight = maxRight;
					}
					// if (lenRight < minRight) {
					// lenRight = minRight;
					// }
				}
				rightWidAr.add(lenRight);
				float leftText = 0;
				if (!Util.isEmpty(item.leftsideWord) && 1 != q.qLinkage) {
					TextView tvLeft = new TextView(
							NativeModeNoPageActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvLeft.setVisibility(View.GONE);
					}
					tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							lowSurveySize);
					tvLeft.setTextColor(Color.BLACK);
					tvLeft.setText(item.leftsideWord);
					tvLeft.setPadding(0, 0, 0, 0);
					TextPaint paintLeft = tvLeft.getPaint();
					String textLeft = item.leftsideWord;
					if (-1 != textLeft.indexOf("%%")) {
						textLeft = textLeft
								.substring(0, textLeft.indexOf("%%"));
					}
					if (-1 != textLeft.indexOf("@@")) {
						textLeft = textLeft
								.substring(0, textLeft.indexOf("@@"));
					}
					leftText = (float) paintLeft.measureText(textLeft);
					float maxLeft = (float) ((maxCWidth / q.freeTextColumn) / 2);
					// float minLeft = (float) paintLeft.measureText("一");
					if (leftText > maxLeft) {
						leftText = maxLeft;
					}
					// if (leftText < minLeft) {
					// leftText = minLeft;
					// }
				}
				leftWidAr.add(leftText);
				float tvRequiredWid = 0;
				if (item.required) {
					TextView tvRequired = new TextView(
							NativeModeNoPageActivity.this);
					tvRequired.setText(getResources().getString(
							R.string.notice_required));
					tvRequired.setTextColor(Color.RED);
					tvRequiredWid = (float) tvRequired.getPaint().measureText(
							getResources().getString(R.string.notice_required));
				}
				leftRequiredWidAr.add(tvRequiredWid);
				// 初始化每一项的edittext
				if (item.itemSize != 0) {
					float editWidth = (float) (Util.getEditWidth(item.itemSize,
							maxCWidth * 2 / 3));
					if (item.dragChecked && 2 == item.type) {
						editWidth = (float) ((maxCWidth / q.freeTextColumn) * 2 / 3);
					}
					editWidAr.add(editWidth);
				} else {
					float editWidth = (float) (Util.getEditWidth(30, maxCWidth));
					editWidAr.add(editWidth);
				}
			}
			// 每一列edit的最大值
			ArrayList<Float> editWidCo = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				editWidCo.add((float) 0);
			}
			for (int i = 0; i < editWidAr.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (editWidCo.get(j) < editWidAr.get(i)) {
							editWidCo.set(j, editWidAr.get(i));
						}
					}
				}
			}
			// 每一列左边文本的最大宽度
			ArrayList<Float> leftWidCo = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftWidCo.add((float) 0);
			}
			for (int i = 0; i < leftWidAr.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftWidCo.get(j) < leftWidAr.get(i)) {
							leftWidCo.set(j, leftWidAr.get(i));
						}
					}
				}
			}
			// 每一列左边必答标记最大宽度
			ArrayList<Float> leftRequiredWidCo = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftRequiredWidCo.add((float) 0);
			}
			for (int i = 0; i < leftWidAr.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftRequiredWidCo.get(j) < leftRequiredWidAr.get(i)) {
							leftRequiredWidCo.set(j, leftRequiredWidAr.get(i));
						}
					}
				}
			}
			// 每一列右边文字的最大宽度
			ArrayList<Float> rightWidCo = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightWidCo.add((float) 0);
			}
			for (int i = 0; i < rightWidAr.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (rightWidCo.get(j) < rightWidAr.get(i)) {
							rightWidCo.set(j, rightWidAr.get(i));
						}
					}
				}
			}
			// 每一列左侧最大总宽度
			ArrayList<Float> leftAllWi = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftAllWi.add(leftWidCo.get(j));
			}
			// 每一列右侧最大总宽度
			ArrayList<Float> rightAllWi = new ArrayList<Float>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightAllWi.add(rightWidCo.get(j) + editWidCo.get(j));
			}
			float sumWidth = 0;
			for (int h = 1; h <= q.freeTextColumn; h++) {
				float rightWid = 0;

				rightWid = rightAllWi.get(h - 1);

				sumWidth += leftAllWi.get(h - 1) + rightWid;
			}
			// 追加说明%%处理
			// 改的地方
			if (!Util.isEmpty(q.qCaption)) {
				// tvRequired.setTextSize(15);
				if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
					// 得到每一个项
					// 判断出生成几个小%%%%追加说明
					String[] tvCount = q.qCaption.split("%%");
					float lencap = 0;
					for (int col = 0; col < tvCount.length; col++) {
						if ((col / 2 > q.freeTextColumn)) {
							TextView tvcap = new TextView(this);
							TextPaint paintcap = tvcap.getPaint();
							String textcap = tvCount[col];
							lencap += (float) paintcap.measureText(textcap);
						}
					}
					sumWidth = sumWidth + lencap;
				}
			}
			float lenRequired = 0;
			for (int h = 1; h <= q.freeTextColumn; h++) {
				lenRequired += leftRequiredWidCo.get(h - 1) + 12;
			}

			fx = ((float) maxCWidth - 2 - lenRequired) / (float) sumWidth;
			// Log.i("@@@", "fx=" + fx);
			ArrayList<Integer> leftWidAry = new ArrayList<Integer>();
			ArrayList<Integer> leftRequiredWidAry = new ArrayList<Integer>();
			ArrayList<Integer> editWidAry = new ArrayList<Integer>();
			ArrayList<Integer> rightWidAry = new ArrayList<Integer>();
			for (int i = 0; i < tbColumns.size(); i++) {
				QuestionItem item = tbColumns.get(i);
				item.itemValue = i;
				float lenRight = 0;
				if (!Util.isEmpty(item.rightsideWord)) {
					TextView tvRight = new TextView(
							NativeModeNoPageActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvRight.setVisibility(View.GONE);
					}
					tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							lowSurveySize);
					tvRight.setTextColor(Color.BLACK);
					tvRight.setText(item.rightsideWord);
					tvRight.setPadding(0, 0, 0, 0);
					TextPaint paintRight = tvRight.getPaint();
					lenRight = (float) paintRight
							.measureText(item.rightsideWord);
					float maxRight = (float) ((maxCWidth / q.freeTextColumn) / 5);
					// float minRight = (float) paintRight.measureText("一");
					if (lenRight > maxRight) {
						lenRight = maxRight;
					}
					// if (lenRight < minRight) {
					// lenRight = minRight;
					// }
					lenRight *= fx;
				}
				rightWidAry.add((int) lenRight);
				float leftText = 0;
				if (!Util.isEmpty(item.leftsideWord) && 1 != q.qLinkage) {
					TextView tvLeft = new TextView(
							NativeModeNoPageActivity.this);

					/**
					 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
					 */
					if (item.isHide) {
						tvLeft.setVisibility(View.GONE);
					}
					tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							lowSurveySize);
					tvLeft.setTextColor(Color.BLACK);
					tvLeft.setText(item.leftsideWord);
					tvLeft.setPadding(0, 0, 0, 0);
					TextPaint paintLeft = tvLeft.getPaint();
					String textLeft = item.leftsideWord;
					if (-1 != textLeft.indexOf("%%")) {
						textLeft = textLeft.substring(0, textLeft.indexOf("%%"));
					}
					if (-1 != textLeft.indexOf("@@")) {
						textLeft = textLeft.substring(0, textLeft.indexOf("@@"));
					}
					leftText = (float) paintLeft.measureText(textLeft);
					float maxLeft = (float) ((maxCWidth / q.freeTextColumn) / 2);
					// float minLeft = (float) paintLeft.measureText("一");
					if (leftText > maxLeft) {
						leftText = maxLeft;
					}
					// if (leftText < minLeft) {
					// leftText = minLeft;
					// }
					leftText *= fx;
				}
				leftWidAry.add((int) leftText);
				float tvRequiredWid = 0;
				if (item.required) {
					TextView tvRequired = new TextView(
							NativeModeNoPageActivity.this);
					tvRequired.setText(getResources().getString(
							R.string.notice_required));
					tvRequired.setTextColor(Color.RED);
					tvRequiredWid = (float) tvRequired.getPaint().measureText(
							getResources().getString(R.string.notice_required));
				}
				leftRequiredWidAry.add((int) tvRequiredWid);

				// 初始化每一项的edittext

				if (item.itemSize != 0) {
					float editWidth = (float) (Util.getEditWidth(item.itemSize,
							maxCWidth * 2 / 3) * fx);
					if (item.dragChecked && 2 == item.type) {
						editWidth = (float) ((maxCWidth / q.freeTextColumn) * 2 / 3 * fx);
					}
					editWidAry.add((int) editWidth);
				} else {
					float editWidth = (float) (Util.getEditWidth(30, maxCWidth) * fx);
					editWidAry.add((int) editWidth);
				}

			}
			// 每一列edit的最大值
			ArrayList<Integer> editWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				editWidCol.add(0);
			}
			for (int i = 0; i < editWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (editWidCol.get(j) < editWidAry.get(i)) {
							editWidCol.set(j, editWidAry.get(i));
						}
					}
				}
			}
			// 每一列左边文本的最大宽度
			ArrayList<Integer> leftWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftWidCol.add(0);
			}
			for (int i = 0; i < leftWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftWidCol.get(j) < leftWidAry.get(i)) {
							leftWidCol.set(j, leftWidAry.get(i));
						}
					}
				}
			}
			// 每一列左边必答标记最大宽度
			ArrayList<Integer> leftRequiredWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftRequiredWidCol.add(0);
			}
			for (int i = 0; i < leftWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (leftRequiredWidCol.get(j) < leftRequiredWidAry
								.get(i)) {
							leftRequiredWidCol
									.set(j, leftRequiredWidAry.get(i));
						}
					}
				}
			}
			// 每一列右边文本的最大宽度
			ArrayList<Integer> rightWidCol = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightWidCol.add(0);
			}
			for (int i = 0; i < rightWidAry.size(); i++) {
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						if (rightWidCol.get(j) < rightWidAry.get(i)) {
							rightWidCol.set(j, rightWidAry.get(i));
						}
					}
				}
			}
			// 每一列左侧最大总宽度
			ArrayList<Integer> leftAllWid = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				leftAllWid.add(leftWidCol.get(j));
			}
			// 每一列右侧最大总宽度
			ArrayList<Integer> rightAllWid = new ArrayList<Integer>();
			for (int j = 0; j < q.freeTextColumn; j++) {
				rightAllWid.add(rightWidCol.get(j) + editWidCol.get(j));
			}
			// 追加说明%%处理
			// 改的地方
			if (!Util.isEmpty(q.qCaption)) {
				// tvRequired.setTextSize(15);
				if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
					// 得到每一个项
					// 判断出生成几个小%%%%追加说明
					String[] tvCount = q.qCaption.split("%%");
					LinearLayout tbr = new LinearLayout(
							NativeModeNoPageActivity.this);
					tbr.setOrientation(LinearLayout.HORIZONTAL);
					tbr.setGravity(Gravity.CENTER_HORIZONTAL);
					ArrayList<LinearLayout> CapLary = new ArrayList<LinearLayout>();// 每行的TableRow对象
					System.out.println("tvCount.length=" + tvCount.length);
					for (int col = 0; col < tvCount.length; col++) {
						LinearLayout capItem = new LinearLayout(
								NativeModeNoPageActivity.this);
						capItem.setOrientation(LinearLayout.VERTICAL);
						capItem.setGravity(Gravity.CENTER_HORIZONTAL);
						if (0 == col % 2) {// 左侧表头
							if (col / 2 < q.freeTextColumn) {// col/2表示第几个item
								LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
										leftAllWid.get(col / 2)
												+ 10
												+ leftRequiredWidCol
														.get(col / 2),
										LayoutParams.WRAP_CONTENT);
								capItemPar.setMargins(1, 1, 1, 1);
								capItem.setLayoutParams(capItemPar);
							}
						} else {
							if (col / 2 < q.freeTextColumn) {
								LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
										rightAllWid.get(col / 2) - 2,
										LayoutParams.WRAP_CONTENT);
								capItemPar.setMargins(1, 1, 1, 1);
								capItem.setLayoutParams(capItemPar);
							}
						}
						CapLary.add(capItem);
						tbr.addView(capItem, tbr.getChildCount());
					}
					tl.addView(tbr, 0);
					for (int tv = 0; tv < tvCount.length; tv++) {
						LinearLayout itemLary = CapLary.get(tv);
						TextView tvSmallCaption = new TextView(this);
						String strTvCaption = tvCount[tv];
						tvSmallCaption.setTextColor(Color.BLACK);
						tvSmallCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						final float Width = itemLary.getWidth();
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeNoPageActivity.this
										.getFilesDir()
										+ File.separator
										+ "survey"
										+ File.separator
										+ feed.getSurveyId()
										+ File.separator
										+ source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									float tWidth = image.getWidth();
									float tHeight = image.getHeight();
									if (tWidth > Width) {
										tHeight = Width / tWidth * tHeight;
										tWidth = Width;
									}
									drawable.setBounds(0, 0, (int) tWidth,
											(int) tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(strTvCaption,
								imgGetter, null);
						tvSmallCaption.setText(fromHtml);
						TextPaint paint = tvSmallCaption.getPaint();
						paint.setFakeBoldText(true);
						tvSmallCaption.setLayoutParams(WRAP_WRAP);
						itemLary.addView(tvSmallCaption);
					}
				}
			}

			// 结束 事件
			if (!Util.isEmpty(q.qCaption)) {
				if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
					tvCaption.setText("");
				}
			}
			for (int i = 0; i < tbColumns.size(); i++) {

				// 得到每一个项
				final QuestionItem item = tbColumns.get(i);
				item.itemValue = i;
				LinearLayout itemll = colsLLy.get(i);
				LinearLayout lLeft = (LinearLayout) itemll.getChildAt(0);
				// 设置每项的左侧布局
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						LinearLayout.LayoutParams lLeftPar;
						if (1 < q.freeTextColumn) {
							lLeftPar = new LinearLayout.LayoutParams(
									leftAllWid.get(j) + 10
											+ leftRequiredWidCol.get(j),
									LayoutParams.MATCH_PARENT);
							lLeftPar.setMargins(1, 1, 1, 1);
						} else {
							lLeftPar = new LinearLayout.LayoutParams(
									leftAllWid.get(j) + 10
											+ leftRequiredWidCol.get(j),
									LayoutParams.MATCH_PARENT);
						}

						lLeft.setLayoutParams(lLeftPar);
					}
				}
				lLeft.setPadding(5, 5, 5, 5);
				lLeft.setBackgroundColor(getResources().getColor(R.color.white));
				lLeft.removeAllViews();
				LinearLayout lRight = (LinearLayout) itemll.getChildAt(1);
				// 设置每项的右侧布局
				if (item.dragChecked) {
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							LinearLayout.LayoutParams lRightPar = new LinearLayout.LayoutParams(
									maxCWidth
											/ q.freeTextColumn
											- (leftAllWid.get(j) + 10 + leftRequiredWidCol
													.get(j)),
									LayoutParams.MATCH_PARENT);
							lRight.setLayoutParams(lRightPar);
						}
					}
				} else {
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							LinearLayout.LayoutParams lRightPar;
							if (1 < q.freeTextColumn) {
								lRightPar = new LinearLayout.LayoutParams(
										rightAllWid.get(j) - 2,
										LayoutParams.MATCH_PARENT);
								lRightPar.setMargins(1, 1, 1, 1);
							} else {
								lRightPar = new LinearLayout.LayoutParams(
										rightAllWid.get(j),
										LayoutParams.MATCH_PARENT);
							}
							lRight.setLayoutParams(lRightPar);
						}
					}
				}
				lRight.setBackgroundColor(getResources()
						.getColor(R.color.white));
				lRight.removeAllViews();
				lRight.setPadding(5, 5, 5, 5);
				// LayoutParams lp = new LayoutParams(100,
				// LayoutParams.WRAP_CONTENT);
				/**
				 * 题外关联 之 内部关联 判断 字段 并进行 界面 设定 宽度 大树 内部关联 6
				 */
				// itemLL.setBackgroundColor(Color.BLUE);
				if (item.required) {
					TextView tvRequired = new TextView(
							NativeModeNoPageActivity.this);

					/**
					 * 题外关联 之 显示 隐藏 选项 显示 必填 隐藏起来 大树 显示关联 2
					 */

					if (item.isHide) {
						tvRequired.setVisibility(View.GONE);
					}

					// myParams.addRule(RelativeLayout.CENTER_VERTICAL,
					// itemLL.getId());
					// myParams.setMargins(0, 0, 6, 0);
					tvRequired.setLayoutParams(WRAP_WRAP);
					tvRequired.setText(getResources().getString(
							R.string.notice_required));
					tvRequired.setTextColor(Color.RED);
					int tvRequiredWid = (int) tvRequired.getPaint()
							.measureText(
									getResources().getString(
											R.string.notice_required));
					// tvRequired.setTextSize(15);
					lLeft.addView(tvRequired, lLeft.getChildCount());
				}
				String ssRight = item.rightsideWord;
				// 初始化每一项的edittext
				UIEditText et = new UIEditText(NativeModeNoPageActivity.this);
				if (Util.isEmpty(item.newLine) || !item.newLine.equals("after")) {
					et.setSingleLine();
				}
				et.setBackgroundResource(R.drawable.bg_edittext);
				/**
				 * 题外关联 显示 的 设计 在这 显示几个选项
				 */
				if (item.isHide) {
					et.setVisibility(View.GONE);
				}
				for (int j = 0; j < q.freeTextColumn; j++) {
					if (j == i % q.freeTextColumn) {
						et.setLayoutParams(new LayoutParams(
								editWidCol.get(j) - 12,
								LayoutParams.WRAP_CONTENT));
					}
				}
				// et.setMinimumWidth(150);
				et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
				et.setTag(item);
				Log.i(TAG, "item = " + item.toString());

				
				//有禁用
//				if (!Util.isEmpty(qidlist)) {
//					for (int t = 0; t < qidlist.size(); t++) {
//						if (qidlist.get(t).Qid.equals(q.qIndex+"")) {
//							ArrayList<AshingBean> ashingBeans = qidlist.get(t).getAshingBeans();
//							
//							
//							Log.i(TAG, "ashingBeans.size = " + ashingBeans.size());
//							for (int ii = 0; ii < ashingBeans.size(); ii++) {
//								ashingBean = ashingBeans.get(ii);
//
//								if (ashingBean.getContetnId().equals(
//										item.getItemValue() + "")) {
//									et.addTextChangedListener(new TextWatcher() {
//										@Override
//										public void beforeTextChanged(CharSequence s,
//												int start, int count, int after) {
//											// 这个方法被调用，说明在s字符串中，从start位置开始的count个字符即将被长度为after的新文本所取代。在这个方法里面改变s，会报错。
//											Log.e(TAG, "beforeTextChanged: ");
//										}
//
//										@Override
//										public void onTextChanged(CharSequence s,
//												int start, int before, int count) {
//											// 这个方法被调用，说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错。
//											Log.e(TAG, "onTextChanged: ");
//										}
//
//										@Override
//										public void afterTextChanged(Editable editable) {
//											setetlinster(editable.toString().trim(),
//													item.getItemValue() + "", position,q.qIndex+"");
//										}
//									});
//
//								}
//							}
//							
//							//----------
//						}
//					}
//				}
				
				
				
				

				/**
				 * 2.其次问是什么类型的题目
				 */

				switch (item.type) {// switch
				/**
				 * 3.第三要询问左右两边是否都有字符串, 一共有3中情况:左右有、只左有、只右有、左右无
				 */
				case 0:
					// 维码扫描
				case 6:
					System.out.println("q.qLinkage:" + q.qLinkage);
					// 三级联动判断
					if (1 == q.qLinkage) {
						/**
						 * 需要加一个字段 来 做 判断 是否是三级联动 三级联动 的话 必须type 是none 现在测试
						 */
						if (!Util.isEmpty(item.leftsideWord)) {
							// Log.i("zrl1", item.leftsideWord);
							if (i == 0) {
								cityPos = 0;// 清0
								s1 = item.leftsideWord;
								provinceSpinner = new Spinner(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 3
								 */
								if (item.isHide) {
									provinceSpinner.setVisibility(View.GONE);
								}

								city = ThreeLeverUtil.getFirstList(s1);
								provinceAdapter = new ArrayAdapter<String>(
										this, R.layout.simple_spinner_adapter,
										city);// list是一个
								provinceAdapter
										.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								provinceSpinner.setAdapter(provinceAdapter);
								/**
								 * 获取 默认的答案
								 */
								if (!Util.isEmpty(amList)) {
									AnswerMap am1 = amList.get(0);
									cityPos = provinceAdapter.getPosition(am1
											.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == cityPos) {
										cityPos = 0;
									}
									provinceSpinner.setSelection(cityPos, true);
								} else {
									provinceSpinner.setSelection(0, true);
								}
								provinceSpinner
										.setOnItemSelectedListener(new ProvinceItemSelectedListener(
												q, vs, mTempRows, position));
								provinceSpinner.setTag(item);
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								String iCap = Util
										.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;

								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(iCap,
										imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(provinceSpinner,
										lRight.getChildCount());
								vs.add(provinceSpinner);
							} else if (i == 1) {
								areaPos = 0;// 清0
								s2 = item.leftsideWord;
								citySpinner = new Spinner(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 4
								 */
								if (item.isHide) {
									citySpinner.setVisibility(View.GONE);
								}

								area = ThreeLeverUtil.getSecondList(s2);
								System.out.println("cityPos:" + cityPos);
								areaListTemp = ThreeLeverUtil.getCityPosList(
										area, city, cityPos);
								if (areaListTemp.size() == 0) {
									areaListTemp.add("");
								}
								cityAdapter = new ArrayAdapter<String>(this,
										R.layout.simple_spinner_adapter,
										areaListTemp);// list是一个
								cityAdapter
										.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								citySpinner.setAdapter(cityAdapter);
								/**
								 * 获取原有答案
								 */
								if (!Util.isEmpty(amList)) {

									AnswerMap am1 = amList.get(1);
									areaPos = cityAdapter.getPosition(am1
											.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == areaPos) {
										areaPos = 0;
									}
									citySpinner.setSelection(areaPos, true);
								} else {
									citySpinner.setSelection(0, true);
								}
								citySpinner
										.setOnItemSelectedListener(new CityItemSelectedListener(
												q, vs, mTempRows, position));
								citySpinner.setTag(item);
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								String iCap = Util
										.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;

								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(iCap,
										imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(citySpinner,
										lRight.getChildCount());
								vs.add(citySpinner);

							} else if (i == 2) {
								s3 = item.leftsideWord;
								countrySpinner = new Spinner(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 5
								 */
								if (item.isHide) {
									countrySpinner.setVisibility(View.GONE);
								}
								way = ThreeLeverUtil.getThridList(s3);
								wayListTemp = ThreeLeverUtil.getAreaPosList(
										areaListTemp, way, areaPos);
								if (wayListTemp.size() == 0) {
									wayListTemp.add("");
								}

								countryAdapter = new ArrayAdapter<String>(this,
										R.layout.simple_spinner_adapter,
										wayListTemp);// list是一个
								countryAdapter
										.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								countrySpinner.setAdapter(countryAdapter);
								countrySpinner
										.setOnItemSelectedListener(new CountryItemSelectedListener(
												q, vs, mTempRows, position));
								/**
								 * 获取 原有 答案
								 */
								if (!Util.isEmpty(amList)) {

									AnswerMap am1 = amList.get(2);
									int pos = countryAdapter.getPosition(am1
											.getAnswerValue());
									// 三级联动更改 处理更新问卷的操作,原因是找不到值
									if (-1 == pos) {
										pos = 0;
									}
									countrySpinner.setSelection(pos, true);
								} else {
									countrySpinner.setSelection(0, true);
								}
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								String iCap = Util
										.getLeftCap(item.leftsideWord);
								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;

								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(iCap,
										imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								lLeft.addView(tvLeft, lLeft.getChildCount());
								lRight.addView(countrySpinner,
										lRight.getChildCount());
								countrySpinner.setTag(item);
								vs.add(countrySpinner);
							}
						}

					} else {
						// 非三级联动
						// 典型的文本
						// 左右有文本值
						// 假如左右不为空，给设置颜色
						ImageView iv = new ImageView(
								NativeModeNoPageActivity.this);

						/**
						 * 题外关联--- 显示关联 大树 显示关联 6
						 */
						if (item.isHide) {
							iv.setVisibility(View.GONE);
						}
						// 维码扫描 item.scanning
						if (6 == item.type) {
							iv = new ImageView(NativeModeNoPageActivity.this);
							LayoutParams myParams = WRAP_WRAP;
							String idStr = q.qIndex + "_" + i;
							iv.setId(idStr.hashCode());
							iv.setLayoutParams(myParams);
							iv.setBackgroundResource(R.drawable.icon_scanning);
							iv.setOnClickListener(new ScanningListener(et));
						}
						// 维码扫描

						if (!Util.isEmpty(item.leftsideWord)
								&& !Util.isEmpty(item.rightsideWord)) {
							// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
							ArrayList<String> leftList = Util
									.obtainList(item.leftsideWord);
							// 左边没有下拉
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(
										item.leftsideWord, imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								TextView tvRight = new TextView(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 8
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}

								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 0);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(new LayoutParams(
												rightWidCol.get(j),
												LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
								// 有答案取答案 赋值
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item,
											0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName()
												.trim())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
								vs.add(et);
							} else {
								/**
								 * 左边有下拉框
								 */
								String iCap = Util
										.getLeftCap(item.leftsideWord);
								int lenLeft = 0;
								if (!Util.isEmpty(iCap)) {
									/**
									 * @@::前面有文字
									 */
									TextView tvLeft = new TextView(
											NativeModeNoPageActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 9
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									LayoutParams tvLeftParam = WRAP_WRAP;
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {

											tvLeftParam = new LayoutParams(
													leftWidCol.get(j),
													LayoutParams.WRAP_CONTENT);
										}
									}
									tvLeft.setLayoutParams(tvLeftParam);
									final float Width = tvLeftParam.width;
									// 更改的样式
									ImageGetter imgGetter = new Html.ImageGetter() {
										public Drawable getDrawable(
												String source) {
											Drawable drawable = null;
											String name = NativeModeNoPageActivity.this
													.getFilesDir()
													+ File.separator
													+ "survey"
													+ File.separator
													+ feed.getSurveyId()
													+ File.separator + source;
											// System.out.println("name:" +
											// name);
											drawable = Drawable
													.createFromPath(name);
											Bitmap image = BitmapFactory
													.decodeFile(name);
											if (image != null) {
												float tWidth = image.getWidth();
												float tHeight = image
														.getHeight();
												if (tWidth > Width) {
													tHeight = Width / tWidth
															* tHeight;
													tWidth = Width;
												}
												drawable.setBounds(0, 0,
														(int) tWidth,
														(int) tHeight);
												return drawable;
											} else {
												return null;
											}
										}
									};
									Spanned fromHtml = Html.fromHtml(iCap,
											imgGetter, null);
									tvLeft.setText(fromHtml);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									lLeft.addView(tvLeft, lLeft.getChildCount());
								}

								Spinner spLeft = new Spinner(
										NativeModeNoPageActivity.this);
								spLeft.setOnItemSelectedListener(new SpSelectedListener(
										q, vs, null, mTempRows, position));
								/**
								 * 题外关联--- 显示关联 大树 显示关联 10
								 */
								if (item.isHide) {
									spLeft.setVisibility(View.GONE);
								}

								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(
										NativeModeNoPageActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);

								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am
													.getAnswerValue());
											if (-1 != pos) {
												// 选上位置

												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}

								TextView tvRight = new TextView(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 11
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}

								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight
										.measureText(item.rightsideWord);
								// 左边说明不为空，右边长度小于最大长度
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(new LayoutParams(
												rightWidCol.get(j),
												LayoutParams.WRAP_CONTENT));
									}
								}
								int spWidth = Util.getEditWidth(item.itemSize,
										maxCWidth);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										spLeft.setLayoutParams(new LayoutParams(
												editWidCol.get(j) - 12,
												LayoutParams.WRAP_CONTENT));
									}
								}
								lRight.addView(spLeft, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
								vs.add(spLeft);
							}
						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只有左边有文字。右边没文字
							ArrayList<String> leftList = Util
									.obtainList(item.leftsideWord);
							/**
							 * 获取左边的说明文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * 左边是说明文字 右边是下拉列表框
								 */
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 12
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(iCap,
										imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								lLeft.addView(tvLeft, lLeft.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								// 左边不是说明文字 。没有下拉列表框
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 13
								 */
								if (item.isHide) {
									tvLeft.setVisibility(View.GONE);
								}

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(
										item.leftsideWord, imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft
										.measureText(item.leftsideWord);
								lLeft.addView(tvLeft, lLeft.getChildCount());
							}
							/**
							 *** 假如左边不是下拉框 直接给文本框赋值。
							 */
							if (Util.isEmpty(leftList)) {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item,
											0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
								vs.add(et);
							} else {
								final Spinner spLeft = new Spinner(
										NativeModeNoPageActivity.this);
								spLeft.setOnItemSelectedListener(new SpSelectedListener(
										q, vs, null, mTempRows, position));
								/**
								 * 题外关联--- 显示关联 大树 显示关联 14
								 */
								if (item.isHide) {
									spLeft.setVisibility(View.GONE);
								}
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(
										NativeModeNoPageActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
	
								//有禁用
//								if (!Util.isEmpty(qidlist)) {
//									for (int t = 0; t < qidlist.size(); t++) {
//										if (qidlist.get(t).Qid.equals(q.qIndex+"")) {
//											final ArrayList<AshingBean> ashingBeans = qidlist.get(t).getAshingBeans();
//											
//											
//											spLeft.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//												@Override
//												public void onItemSelected(
//														AdapterView<?> arg0, View arg1,
//														int arg2, long arg3) {
//													// TODO Auto-generated method stub
//													for (int ii = 0; ii < ashingBeans
//															.size(); ii++) {
//														ashingBean = ashingBeans.get(ii);
//														qs = aryQs.get(pageNum);
//														if (ashingBean.getContetnId().equals(item.getItemValue()+ "")) {
//															Log.i("getItemValue",
//																	ashingBean
//																			.getContetnId()
//																			+ "");
//															Log.i("getItemValue == va",
//																	item.getItemValue()
//																			+ "");
//
//															setetlinster(spLeft
//																	.getSelectedItem()
//																	.toString(),
//																	item.getItemValue()
//																			+ "", position,q.qIndex+"");
//														}
//													}
//													
//													Log.i("spLeft", "spLeft 2"
//															+ spLeft.getSelectedItem()
//																	.toString());
//												}
//
//												@Override
//												public void onNothingSelected(
//														AdapterView<?> arg0) {
//													// TODO Auto-generated method stub
//													Log.i("spLeft", "spLeft 3");
//												}
//											});
//											
//											//----------
//										}
//									}
//								}
								
								
								
								
								
								
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am
													.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								int spWidth = Util.getEditWidth(item.itemSize,
										maxCWidth);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										spLeft.setLayoutParams(new LayoutParams(
												editWidCol.get(j) - 12,
												LayoutParams.WRAP_CONTENT));
									}
								}
								lRight.addView(spLeft, lRight.getChildCount());
								vs.add(spLeft);

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							// 只有右边有。左边没有
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(
									NativeModeNoPageActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 15
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();

							int lenRight = (int) paintRight
									.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(new LayoutParams(
											rightWidCol.get(j),
											LayoutParams.WRAP_CONTENT));
								}
							}
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(et);
						} else {
							// 左右无,只有文本题目
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						}
						// 维码
						if (6 == item.type) {
							lRight.addView(iv, lRight.getChildCount());
						}
					}
					break;

				case 1:// 日期格式
				case 2:// 数字格式
				case 3:// 英文/数字格式
				case 5:// 邮件格式
					LayoutParams sbParams = FILL_WRAP;
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							sbParams = new LayoutParams(editWidCol.get(j) - 12,
									LayoutParams.WRAP_CONTENT);
						}
					}
					if (!Util.isEmpty(item.leftsideWord)
							&& !Util.isEmpty(item.rightsideWord)) {
						// 左右有文字说明
						TextView tvLeft = new TextView(
								NativeModeNoPageActivity.this);

						/**
						 * 题外关联 之显示 隐藏 左边文字
						 */

						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);
						}

						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						LayoutParams tvLeftParam = WRAP_WRAP;
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvLeftParam = new LayoutParams(
										leftWidCol.get(j),
										LayoutParams.WRAP_CONTENT);
							}
						}
						tvLeft.setLayoutParams(tvLeftParam);
						final float Width = tvLeftParam.width;
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeNoPageActivity.this
										.getFilesDir()
										+ File.separator
										+ "survey"
										+ File.separator
										+ feed.getSurveyId()
										+ File.separator
										+ source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									float tWidth = image.getWidth();
									float tHeight = image.getHeight();
									if (tWidth > Width) {
										tHeight = Width / tWidth * tHeight;
										tWidth = Width;
									}
									drawable.setBounds(0, 0, (int) tWidth,
											(int) tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.leftsideWord,
								imgGetter, null);
						tvLeft.setText(fromHtml);
						tvLeft.setPadding(0, 0, 0, 8);
						TextPaint paintLeft = tvLeft.getPaint();
						int lenLeft = (int) paintLeft
								.measureText(item.leftsideWord);
						if (1 == item.type) {
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber)
									&& !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(
										R.string.edit_min_to_max,
										item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min,
										item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max,
										item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this
									.getString(R.string.please_input_email));
						}
						TextView tvRight = new TextView(
								NativeModeNoPageActivity.this);

						/**
						 * 题外关联 之 显示 隐藏 选项
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}

						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();
						int lenRight = (int) paintRight
								.measureText(item.rightsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(new LayoutParams(
										rightWidCol.get(j),
										LayoutParams.WRAP_CONTENT));
							}
						}
						lLeft.addView(tvLeft, lLeft.getChildCount());
						// 假如是滑动条,且是数字题目。显示滑动条。
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(
									NativeModeNoPageActivity.this);
							sb.setThumb(getResources().getDrawable(
									R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(
									R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// screenWidth / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							sb.setLayoutParams(FILL_WRAP);
							int itemMaxNumber = Integer.parseInt(Util
									.isEmpty(item.maxNumber) ? "100"
									: item.maxNumber);
							int itemMinNumber = Integer.parseInt(Util
									.isEmpty(item.minNumber) ? "0"
									: item.minNumber);
							sb.setMax(itemMaxNumber - itemMinNumber);
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am
													.getAnswerValue())
													- itemMinNumber);
											item.isChange = true;
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(
									NativeModeNoPageActivity.this);
							tvSeekTop.setLayoutParams(FILL_WRAP);
							tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
							tvSeekTop.setText("(" + sbProgress + "/"
									+ itemMaxNumber + ")");
							LinearLayout rightLL = new LinearLayout(
									NativeModeNoPageActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(sbParams);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
									tvSeekTop, null, vs, q, mTempRows,
									itemMaxNumber, itemMinNumber, item,
									position));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							// 不是情况直接显示文本题目

							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						}
						lRight.addView(tvRight, lRight.getChildCount());

						/**
						 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 7
						 */
						if (item.symbol != null
								&& item.symbol.equals(this
										.getString(R.string.sum))) {

							TextView tvSyb = new TextView(
									NativeModeNoPageActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							// tvSyb.setLayoutParams(new
							// LayoutParams(100,LayoutParams.WRAP_CONTENT));
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									surveySize);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！
							 */
							tvSyb.setText("(" + this.getString(R.string.sum)
									+ ")");
							TextPaint paintSyb = tvSyb.getPaint();
							int lenSyb = (int) paintSyb
									.measureText(item.leftsideWord);
							tvSyb.setLayoutParams(new LayoutParams(
									(int) (lenSyb * fx),
									LayoutParams.WRAP_CONTENT));
							lRight.addView(tvSyb);
						}

					} else if (!Util.isEmpty(item.leftsideWord)) {
						// 只左有
						TextView tvLeft = new TextView(
								NativeModeNoPageActivity.this);
						/**
						 * 题外关联 之显示 隐藏 选项 左边文字
						 */
						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);

						}
						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						LayoutParams tvLeftParam = WRAP_WRAP;
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvLeftParam = new LayoutParams(
										leftWidCol.get(j),
										LayoutParams.WRAP_CONTENT);
							}
						}
						tvLeft.setLayoutParams(tvLeftParam);
						final float Width = tvLeftParam.width;
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = NativeModeNoPageActivity.this
										.getFilesDir()
										+ File.separator
										+ "survey"
										+ File.separator
										+ feed.getSurveyId()
										+ File.separator
										+ source;
								// System.out.println("name:" + name);
								drawable = Drawable.createFromPath(name);
								Bitmap image = BitmapFactory.decodeFile(name);
								if (image != null) {
									float tWidth = image.getWidth();
									float tHeight = image.getHeight();
									if (tWidth > Width) {
										tHeight = Width / tWidth * tHeight;
										tWidth = Width;
									}
									drawable.setBounds(0, 0, (int) tWidth,
											(int) tHeight);
									return drawable;
								} else {
									return null;
								}
							}
						};
						Spanned fromHtml = Html.fromHtml(item.leftsideWord,
								imgGetter, null);
						tvLeft.setText(fromHtml);
						tvLeft.setPadding(0, 0, 0, 8);
						TextPaint paintLeft = tvLeft.getPaint();
						int lenLeft = (int) paintLeft
								.measureText(item.leftsideWord);
						if (1 == item.type) {
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber)
									&& !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(
										R.string.edit_min_to_max,
										item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min,
										item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max,
										item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this
									.getString(R.string.please_input_email));
						}

						// 假如是滑动条,且是数字题目。显示滑动条。
						lLeft.addView(tvLeft, lLeft.getChildCount());
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(
									NativeModeNoPageActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 16
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(
									R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(
									R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// screenWidth / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							sb.setLayoutParams(FILL_WRAP);
							// sb.set
							int itemMaxNumber = Integer.parseInt(Util
									.isEmpty(item.maxNumber) ? "100"
									: item.maxNumber);
							int itemMinNumber = Integer.parseInt(Util
									.isEmpty(item.minNumber) ? "0"
									: item.minNumber);
							sb.setMax(itemMaxNumber - itemMinNumber);
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am
													.getAnswerValue())
													- itemMinNumber);
											item.isChange = true;
										}
									}
								}
							}
							sb.setThumbOffset(0);

							TextView tvSeekTop = new TextView(
									NativeModeNoPageActivity.this);
							// tvSeekTop.setLayoutParams(FILL_WRAP);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 17
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}
							tvSeekTop.setLayoutParams(FILL_WRAP);
							tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
							tvSeekTop.setText("(" + sbProgress + "/"
									+ itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(
									NativeModeNoPageActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(sbParams);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
									tvSeekTop, null, vs, q, mTempRows,
									itemMaxNumber, itemMinNumber, item,
									position));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 8
							 */
							if (item.symbol != null
									&& item.symbol.equals(this
											.getString(R.string.sum))) {
								TextView tvSyb = new TextView(
										NativeModeNoPageActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								// tvSyb.setLayoutParams(new
								// LayoutParams(150,
								// LayoutParams.WRAP_CONTENT));
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										surveySize);
								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("("
										+ this.getString(R.string.sum) + ")");
								TextPaint paintSyb = tvSyb.getPaint();
								int lenSyb = (int) paintSyb
										.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams(
										(int) (lenSyb * fx),
										LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb, lRight.getChildCount());
							}

							vs.add(et);
						}
					} else if (!Util.isEmpty(item.rightsideWord)) {
						if (1 == item.type) {// 日期
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber)
									&& !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(
										R.string.edit_min_to_max,
										item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min,
										item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max,
										item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this
									.getString(R.string.please_input_email));
						}
						TextView tvRight = new TextView(
								NativeModeNoPageActivity.this);

						/**
						 * 题外关联 之显示 隐藏 选项 右边文字
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}
						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();
						int lenRight = (int) paintRight
								.measureText(item.rightsideWord);
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(new LayoutParams(
										rightWidCol.get(j),
										LayoutParams.WRAP_CONTENT));
							}
						}
						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(
									NativeModeNoPageActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 18
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(
									R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(
									R.layout.seekbar_style));
							sb.setTag(item);
							// sb.setLayoutParams(new LayoutParams(3 *
							// screenWidth / 4, //
							// LayoutParams.WRAP_CONTENT));
							// sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);

							sb.setLayoutParams(FILL_WRAP);
							// sb.set
							int itemMaxNumber = Integer.parseInt(Util
									.isEmpty(item.maxNumber) ? "100"
									: item.maxNumber);
							int itemMinNumber = Integer.parseInt(Util
									.isEmpty(item.minNumber) ? "0"
									: item.minNumber);
							sb.setMax(itemMaxNumber - itemMinNumber);
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am
													.getAnswerValue())
													- itemMinNumber);
											item.isChange = true;
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(
									NativeModeNoPageActivity.this);
							// tvSeekTop.setLayoutParams(FILL_WRAP);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 19
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}

							tvSeekTop.setLayoutParams(FILL_WRAP);
							tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
							tvSeekTop.setText("(" + sbProgress + "/"
									+ itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(
									NativeModeNoPageActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							// rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.setLayoutParams(sbParams);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
									tvSeekTop, null, vs, q, mTempRows,
									itemMaxNumber, itemMinNumber, item,
									position));
							lRight.addView(rightLL, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 9
							 */
							if (item.symbol != null
									&& item.symbol.equals(this
											.getString(R.string.sum))) {
								TextView tvSyb = new TextView(
										NativeModeNoPageActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("("
										+ this.getString(R.string.sum) + ")");
								TextPaint paintLeft = tvSyb.getPaint();
								int lenSyb = (int) paintLeft
										.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams(
										(int) (lenSyb * fx),
										LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb);
							}

							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 10
							 */
							if (item.symbol != null
									&& item.symbol.equals(this
											.getString(R.string.sum))) {
								TextView tvSyb = new TextView(
										NativeModeNoPageActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("("
										+ this.getString(R.string.sum) + ")");
								lRight.addView(tvSyb);
							}

							vs.add(et);
						}

						lRight.addView(tvRight, lRight.getChildCount());

					} else {
						// 左右无
						if (1 == item.type) {// 日期
							// et.setMinWidth(240);
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
						} else if (2 == item.type) {// 数字
							if (item.isFloat) {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							} else {
								et.setInputType(InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED);
							}
							if (!Util.isEmpty(item.minNumber)
									&& !Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(
										R.string.edit_min_to_max,
										item.minNumber, item.maxNumber));
							} else if (!Util.isEmpty(item.minNumber)) {
								et.setHint(this.getString(R.string.edit_min,
										item.minNumber));
							} else if (!Util.isEmpty(item.maxNumber)) {
								et.setHint(this.getString(R.string.edit_max,
										item.maxNumber));
							}
						} else if (3 == item.type) {// 英文/数字
							et.setInputType(InputType.TYPE_CLASS_TEXT);
						} else if (5 == item.type) {// 邮件
							et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							et.setHint(this
									.getString(R.string.please_input_email));
						}

						if (item.dragChecked && 2 == item.type) {
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
							SeekBar sb = new SeekBar(
									NativeModeNoPageActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 20
							 */
							if (item.isHide) {
								sb.setVisibility(View.GONE);
							}

							sb.setThumb(getResources().getDrawable(
									R.drawable.one_key_scan_bg_ani_bg));
							sb.setProgressDrawable(getResources().getDrawable(
									R.layout.seekbar_style));
							sb.setTag(item);

							sb.setLayoutParams(FILL_WRAP);
							sb.setPadding(5, 0, 5, 15);
							// sb.setMinimumWidth(400);
							// sb.set
							int itemMaxNumber = Integer.parseInt(Util
									.isEmpty(item.maxNumber) ? "100"
									: item.maxNumber);
							int itemMinNumber = Integer.parseInt(Util
									.isEmpty(item.minNumber) ? "0"
									: item.minNumber);
							sb.setMax(itemMaxNumber - itemMinNumber);
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										if (!Util.isEmpty(am.getAnswerValue())) {
											sb.setProgress(Integer.parseInt(am
													.getAnswerValue())
													- itemMinNumber);
											item.isChange = true;
										}
									}
								}
							}
							sb.setThumbOffset(0);
							TextView tvSeekTop = new TextView(
									NativeModeNoPageActivity.this);
							/**
							 * 题外关联--- 显示关联 大树 显示关联 21
							 */
							if (item.isHide) {
								tvSeekTop.setVisibility(View.GONE);
							}

							tvSeekTop.setLayoutParams(FILL_WRAP);
							tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvSeekTop.setGravity(Gravity.CENTER);
							tvSeekTop.setTextColor(Color.BLUE);

							/**
							 * 只有滚动条
							 */
							int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
							tvSeekTop.setText("(" + sbProgress + "/"
									+ itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(
									NativeModeNoPageActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(sbParams);
							rightLL.addView(tvSeekTop, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
									tvSeekTop, null, vs, q, mTempRows,
									itemMaxNumber, itemMinNumber, item,
									position));
							lRight.addView(rightLL, lRight.getChildCount());
							vs.add(sb);
							/**
							 * -----------------------------------拖动条样式---- ----
							 * ------------------
							 ***/
						} else {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}

							lRight.addView(et, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 11
							 */
							if (item.symbol != null
									&& item.symbol.equals(this
											.getString(R.string.sum))) {
								TextView tvSyb = new TextView(
										NativeModeNoPageActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("("
										+ this.getString(R.string.sum) + ")");
								TextPaint paintLeft = tvSyb.getPaint();
								int lenRight = (int) paintLeft
										.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams(
										(int) (lenRight * fx),
										LayoutParams.WRAP_CONTENT));

								lRight.addView(tvSyb);
							}

							// System.out.println("tempQWidth:"+tempQWidth+"--item"+itemLL.getLayoutParams().width);
							vs.add(et);
						}
					}
					break;

				case 4:// 字典格式
						// 假如左右不为空，给设置颜色

					if (!Util.isEmpty(item.leftsideWord)
							&& !Util.isEmpty(item.rightsideWord)) {
						// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
						ArrayList<String> leftList = Util
								.obtainList(item.leftsideWord);
						// 左边没有下拉
						if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							LayoutParams tvLeftParam = WRAP_WRAP;
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeftParam = new LayoutParams(
											leftWidCol.get(j),
											LayoutParams.WRAP_CONTENT);
								}
							}
							tvLeft.setLayoutParams(tvLeftParam);
							final float Width = tvLeftParam.width;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > Width) {
											tHeight = Width / tWidth * tHeight;
											tWidth = Width;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.leftsideWord,
									imgGetter, null);
							tvLeft.setText(fromHtml);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft
									.measureText(item.leftsideWord);
							TextView tvRight = new TextView(
									NativeModeNoPageActivity.this);
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight
									.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(new LayoutParams(
											rightWidCol.get(j),
											LayoutParams.WRAP_CONTENT));
								}
							}
							lLeft.addView(tvLeft, lLeft.getChildCount());
							// 有答案取答案 赋值
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							// 余下的长度
							lRight.addView(et, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(et);
						} else {
							/**
							 * 左边有下拉框
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * @@::前面有文字
								 */
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(iCap,
										imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								lLeft.addView(tvLeft, lLeft.getChildCount());
							}

							Spinner spLeft = new Spinner(
									NativeModeNoPageActivity.this);
							spLeft.setOnItemSelectedListener(new SpSelectedListener(
									q, vs, null, mTempRows, position));
							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(
									NativeModeNoPageActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);

							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am
												.getAnswerValue());
										if (-1 != pos) {
											// 选上位置

											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}

							TextView tvRight = new TextView(
									NativeModeNoPageActivity.this);

							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							// 左边说明不为空，右边长度小于最大长度
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(new LayoutParams(
											rightWidCol.get(j),
											LayoutParams.WRAP_CONTENT));
								}
							}
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									spLeft.setLayoutParams(new LayoutParams(
											editWidCol.get(j) - 12,
											LayoutParams.WRAP_CONTENT));
								}
							}
							lRight.addView(spLeft, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
							vs.add(spLeft);
						}
					} else if (!Util.isEmpty(item.leftsideWord)) {
						// 只有左边有文字。右边没文字
						ArrayList<String> leftList = Util
								.obtainList(item.leftsideWord);
						/**
						 * 获取左边的说明文字
						 */
						String iCap = Util.getLeftCap(item.leftsideWord);
						int lenLeft = 0;
						if (!Util.isEmpty(iCap)) {
							/**
							 * 左边是说明文字 右边是下拉列表框
							 */
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);

							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							LayoutParams tvLeftParam = WRAP_WRAP;
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {

									tvLeftParam = new LayoutParams(
											leftWidCol.get(j),
											LayoutParams.WRAP_CONTENT);
								}
							}
							tvLeft.setLayoutParams(tvLeftParam);
							final float Width = tvLeftParam.width;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > Width) {
											tHeight = Width / tWidth * tHeight;
											tWidth = Width;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(iCap, imgGetter,
									null);
							tvLeft.setText(fromHtml);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							lenLeft = (int) paintLeft.measureText(iCap);
							lLeft.addView(tvLeft, lLeft.getChildCount());
						} else if (Util.isEmpty(leftList)) {
							// 左边不是说明文字 。没有下拉列表框
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);

							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							LayoutParams tvLeftParam = WRAP_WRAP;
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {

									tvLeftParam = new LayoutParams(
											leftWidCol.get(j),
											LayoutParams.WRAP_CONTENT);
								}
							}
							tvLeft.setLayoutParams(tvLeftParam);
							final float Width = tvLeftParam.width;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = NativeModeNoPageActivity.this
											.getFilesDir()
											+ File.separator
											+ "survey"
											+ File.separator
											+ feed.getSurveyId()
											+ File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory
											.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > Width) {
											tHeight = Width / tWidth * tHeight;
											tWidth = Width;
										}
										drawable.setBounds(0, 0, (int) tWidth,
												(int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.leftsideWord,
									imgGetter, null);
							tvLeft.setText(fromHtml);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							lenLeft = (int) paintLeft
									.measureText(item.leftsideWord);
							lLeft.addView(tvLeft, lLeft.getChildCount());
						}
						/**
						 *** 假如左边不是下拉框 直接给文本框赋值。
						 */
						if (Util.isEmpty(leftList)) {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							vs.add(et);
						} else {
							// 是下拉框。给下拉框赋值
							/**
							 * 大树文本框修改
							 */
							if (Util.isEmpty(iCap)) {
								Log.i("zrl1", item.leftsideWord + "左边文字");
								// 1
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								LayoutParams tvLeftParam = WRAP_WRAP;
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {

										tvLeftParam = new LayoutParams(
												leftWidCol.get(j),
												LayoutParams.WRAP_CONTENT);
									}
								}
								tvLeft.setLayoutParams(tvLeftParam);
								final float Width = tvLeftParam.width;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = NativeModeNoPageActivity.this
												.getFilesDir()
												+ File.separator
												+ "survey"
												+ File.separator
												+ feed.getSurveyId()
												+ File.separator + source;
										// System.out.println("name:" + name);
										drawable = Drawable
												.createFromPath(name);
										Bitmap image = BitmapFactory
												.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > Width) {
												tHeight = Width / tWidth
														* tHeight;
												tWidth = Width;
											}
											drawable.setBounds(0, 0,
													(int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(
										item.leftsideWord, imgGetter, null);
								tvLeft.setText(fromHtml);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft
										.measureText(item.leftsideWord);
								lLeft.addView(tvLeft, lLeft.getChildCount());
								// 2
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item,
											0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
								vs.add(et);

							} else {
								// 大树文本框修改
								Spinner spLeft = new Spinner(
										NativeModeNoPageActivity.this);
								spLeft.setOnItemSelectedListener(new SpSelectedListener(
										q, vs, null, mTempRows, position));
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(
										NativeModeNoPageActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										if (j == i) {
											AnswerMap am = amList.get(i);
											int pos = aa.getPosition(am
													.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										spLeft.setLayoutParams(new LayoutParams(
												editWidCol.get(j) - 12,
												LayoutParams.WRAP_CONTENT));
									}
								}
								lRight.addView(spLeft, lRight.getChildCount());
								vs.add(spLeft);
							}

						}
					} else if (!Util.isEmpty(item.rightsideWord)) {
						// 只有右边有。左边没有
						/**
						 * 左边是文本框 右边是说明
						 */
						TextView tvRight = new TextView(
								NativeModeNoPageActivity.this);

						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 8);
						TextPaint paintRight = tvRight.getPaint();

						int lenRight = (int) paintRight
								.measureText(item.rightsideWord);
						if (lenRight > (q.freeTextColumn + 4) * fx) {
							lenRight = (int) ((q.freeTextColumn + 4) * fx);
						}
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								tvRight.setLayoutParams(new LayoutParams(
										rightWidCol.get(j),
										LayoutParams.WRAP_CONTENT));
							}
						}
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						lRight.addView(et, lRight.getChildCount());
						lRight.addView(tvRight, lRight.getChildCount());
						vs.add(et);
					} else {
						// 左右无,只有文本题目
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						lRight.addView(et, lRight.getChildCount());
						vs.add(et);
					}
					// 数据字典
					// 为输入添加TextWatcher监听文字的变化
					if ("1".equals(item.classtype)) {// 匹配后继续

					} else if ("2".equals(item.classtype)) {// 匹配后终止，提示

					} else {
						et.setOnTouchListener(new DataTouchListener(
								item.classid, et));
						et.setKeyListener(null);
					}
					break;
				}

				// switch
				// 每行多列
				// if (1 < q.freeTextColumn) {
				// LinearLayout ll = colsLL.get(i % q.freeTextColumn);
				// ll.addView(itemLL, ll.getChildCount());
				// } else {
				// // 直接加上 一行一列文本题目
				// bodyView.addView(itemLL, bodyView.getChildCount());
				// }
				// 隐藏选项
				
//				sethideitem( false, q, vs,null, mTempRows, false);
				
				boolean isSetHide = setHideRb(item, q);
				if (isSetHide) {
					itemll.setVisibility(View.GONE);
					if (!Util.isEmpty(et.getText())) {
						et.setText("");
						int success = getQuestionAnswer( false, q, vs,
								null, mTempRows, false);
					}
				}
				item.setIsSetHide(isSetHide);
				System.out.println("000000000000000000000" + isSetHide);
				// 隐藏选项
				et.addTextChangedListener(new EditChangedListener(q, vs,
						mTempRows, position));
			}
			// int success = getQuestionAnswer(MSG_NEXT, false, q, vs, null,
			// mTempRows);
		} else {
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else if (1 < q.rowsNum
					&& Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else if (1 == q.freeTextColumn) {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			} else {
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			/**
			 * 拖动条风格
			 */
			if (1 == q.qDragChecked && "figure".equals(q.freeTextSort)) {

				if (!Util.isEmpty(q.freeSymbol)) {
					TextView tvSyb = new TextView(NativeModeNoPageActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
					tvSyb.setText(getResources()
							.getString(
									R.string.question_syb,
									Util.isEmpty(q.freeMinNumber) ? "0"
											: q.freeMinNumber, //
									Util.isEmpty(q.freeMaxNumber) ? "0"
											: q.freeMaxNumber,
									q.freeSymbol
											+ q.freeSumNumber
											+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat)
													: "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else {
					/**
					 * 数字类型的最大值不为空
					 */
					TextView tvSyb = new TextView(NativeModeNoPageActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
					tvSyb.setText(getResources()
							.getString(
									R.string.question_max_min,
									Util.isEmpty(q.freeMinNumber) ? "0"
											: q.freeMinNumber,
									Util.isEmpty(q.freeMaxNumber) ? "0"
											: q.freeMaxNumber
													+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat)
															: "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				}
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					SeekBar sb = new SeekBar(NativeModeNoPageActivity.this);
					sb.setThumb(getResources().getDrawable(
							R.drawable.one_key_scan_bg_ani_bg));
					sb.setProgressDrawable(getResources().getDrawable(
							R.layout.seekbar_style));
					sb.setTag(item);
					sb.setLayoutParams(new LayoutParams(3 * screenWidth / 4, //
							LayoutParams.WRAP_CONTENT));
					sb.setPadding(5, 0, 5, 15);
					sb.setMinimumWidth(400);
					// sb.set
					int itemMaxNumber = Integer.parseInt(Util
							.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
					int itemMinNumber = Integer.parseInt(Util
							.isEmpty(item.minNumber) ? "0" : item.minNumber);
					sb.setMax(itemMaxNumber - itemMinNumber);
					if (!Util.isEmpty(amList)) {
						String etName = Util.GetAnswerName(q, item, 0, 0, true,
								false);
						for (AnswerMap am : amList) {
							if (etName.equals(am.getAnswerName())) {
								if (!Util.isEmpty(am.getAnswerValue())) {
									sb.setProgress(Integer.parseInt(am
											.getAnswerValue()) - itemMinNumber);
									item.isChange = true;
								}
							}
						}
					}
					int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
					// sb.setContentDescription("50");

					// sb.setO
					vs.add(sb);

					TextView tvRight = new TextView(
							NativeModeNoPageActivity.this);
					tvRight.setLayoutParams(FILL_WRAP);
					tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							lowSurveySize);
					tvRight.setGravity(Gravity.CENTER);
					tvRight.setTextColor(Color.BLUE);

					/**
					 * 
					 * 滚动条左右两边都有文字, 效果:左边的文字在滚动条的左边|右边文字和滚动条,其中文字在滚动条的上边
					 */
					if (!Util.isEmpty(item.leftsideWord)
							&& !Util.isEmpty(item.rightsideWord)) {
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */
						LinearLayout ll = new LinearLayout(
								NativeModeNoPageActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						ll.setPadding(5, 5, 20, 5);

						TextView tvLeft = new TextView(
								NativeModeNoPageActivity.this);
						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(item.leftsideWord);

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText(item.rightsideWord + "(" + sbProgress
								+ "/" + itemMaxNumber + ")");

						LinearLayout rightLL = new LinearLayout(
								NativeModeNoPageActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
								tvRight, item.rightsideWord, vs, q, mTempRows,
								itemMaxNumber, itemMinNumber, item, position));
						bodyView.addView(tvLeft, bodyView.getChildCount());
						bodyView.addView(rightLL, bodyView.getChildCount());
					} else if (!Util.isEmpty(item.leftsideWord)) {
						/**
						 * 左边有文字, 效果:左边文字,右边滚动条
						 */
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */
						LinearLayout ll = new LinearLayout(
								NativeModeNoPageActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(FILL_WRAP);
						ll.setPadding(5, 5, 20, 5);

						TextView tvLeft = new TextView(
								NativeModeNoPageActivity.this);
						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(item.leftsideWord);

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText("(" + sbProgress + "/" + itemMaxNumber
								+ ")");

						LinearLayout rightLL = new LinearLayout(
								NativeModeNoPageActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
								tvRight, null, vs, q, mTempRows, itemMaxNumber,
								itemMinNumber, item, position));
						bodyView.addView(tvLeft, bodyView.getChildCount());
						bodyView.addView(rightLL, bodyView.getChildCount());

					} else if (!Util.isEmpty(item.rightsideWord)) {
						/**
						 * 右边文字, 效果:上方文字,下边滚动条
						 */
						/**
						 * 次布局存放左边文字、滚动条、右边文字
						 */

						/**
						 * 显示当前的刻度
						 */
						tvRight.setText(item.rightsideWord + "(" + sbProgress
								+ "/" + itemMaxNumber + ")");
						LinearLayout rightLL = new LinearLayout(
								NativeModeNoPageActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
								tvRight, item.rightsideWord, vs, q, mTempRows,
								itemMaxNumber, itemMinNumber, item, position));
						bodyView.addView(rightLL, bodyView.getChildCount());
					} else {
						/**
						 * 只有滚动条
						 */
						tvRight.setText("(" + sbProgress + "/" + itemMaxNumber
								+ ")");

						LinearLayout rightLL = new LinearLayout(
								NativeModeNoPageActivity.this);
						rightLL.setOrientation(LinearLayout.VERTICAL);
						rightLL.setLayoutParams(WRAP_WRAP);
						rightLL.addView(tvRight, rightLL.getChildCount());
						rightLL.addView(sb, rightLL.getChildCount());
						sb.setOnSeekBarChangeListener(new SeekBarChangeListener(
								tvRight, null, vs, q, mTempRows, itemMaxNumber,
								itemMinNumber, item, position));
						bodyView.addView(rightLL, bodyView.getChildCount());
					}
				}
				// 一下代码不要执行
				return;
			}

			// 需要求EditText之中的条件是否符合大于、大于等于、小于、小于等于的条件
			boolean isSyb = (1 != q.qDragChecked
					&& "figure".equals(q.freeTextSort) && //
			!Util.isEmpty(q.freeSymbol));
			if (isSyb) {
				TextView tvSyb = new TextView(NativeModeNoPageActivity.this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
				tvSyb.setText(getResources()
						.getString(
								R.string.question_syb,
								Util.isEmpty(q.freeMinNumber) ? "0"
										: q.freeMinNumber, //
								Util.isEmpty(q.freeMaxNumber) ? "0"
										: q.freeMaxNumber,
								q.freeSymbol
										+ q.freeSumNumber
										+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat)
												: "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			} else if ("figure".equals(q.freeTextSort)
					&& !Util.isEmpty(q.freeMaxNumber)) {
				/**
				 * 数字类型的最大值不为空
				 */
				TextView tvSyb = new TextView(NativeModeNoPageActivity.this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
				tvSyb.setText(getResources()
						.getString(
								R.string.question_max_min,
								Util.isEmpty(q.freeMinNumber) ? "0"
										: q.freeMinNumber,
								q.freeMaxNumber
										+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat)
												: "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			}
			if (!Util.isEmpty(tbColumns)) {// 非滚动条
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					LinearLayout ll = new LinearLayout(
							NativeModeNoPageActivity.this);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(WRAP_WRAP);
					ll.setPadding(5, 5, 20, 5);
					EditText et = new EditText(NativeModeNoPageActivity.this);
					if (Util.isEmpty(item.newLine)
							|| !item.newLine.equals("after")) {
						et.setSingleLine();
					}
					et.setBackgroundResource(R.drawable.bg_edittext);
					// et.set
					if (1 != q.qDragChecked && "figure".equals(q.freeTextSort)) {
						et.setInputType(InputType.TYPE_CLASS_NUMBER);// setInputType(InputType.TYPE_CLASS_NUMBER);旧文本框
					}
					et.setLayoutParams(WRAP_WRAP);
					et.setMinimumWidth(150);
					et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					// et.setId(i);
					et.setTag(item);

					/**
					 * 假如文本框左右两边都有说明文字的话
					 */
					if (!Util.isEmpty(item.leftsideWord)
							&& !Util.isEmpty(item.rightsideWord)) {
						// System.out.println("两边都有");
						// 含有%%1%%2%%此类信息
						ArrayList<String> leftList = Util
								.obtainList(item.leftsideWord);
						// ArrayList<String> rightList =
						// Util.obtainList(item.rightsideWord);
						if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);
							TextView tvRight = new TextView(
									NativeModeNoPageActivity.this);
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							ll.addView(tvLeft, ll.getChildCount());

							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							ll.addView(tvRight, ll.getChildCount());
							vs.add(et);
						} else {
							/**
							 * 左边有文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							if (!Util.isEmpty(iCap)) {
								/**
								 * @@::前面有文字
								 */
								TextView tvLeft = new TextView(
										NativeModeNoPageActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
										lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								ll.addView(tvLeft, ll.getChildCount());
							}

							Spinner spLeft = new Spinner(
									NativeModeNoPageActivity.this);
							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(
									NativeModeNoPageActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);
							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am
												.getAnswerValue());
										if (-1 != pos) {
											// 选上位置

											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}
							ll.addView(spLeft, ll.getChildCount());

							TextView tvRight = new TextView(
									NativeModeNoPageActivity.this);
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							ll.addView(tvRight, ll.getChildCount());
							vs.add(spLeft);
						}
						bodyView.addView(ll, bodyView.getChildCount());

					} else if (!Util.isEmpty(item.leftsideWord)
							&& Util.isEmpty(item.rightsideWord)) {// 只有左边有文字
						// System.out.println("左边文字");
						ArrayList<String> leftList = Util
								.obtainList(item.leftsideWord);
						/**
						 * 获取左边的说明文字
						 */
						String iCap = Util.getLeftCap(item.leftsideWord);
						if (!Util.isEmpty(iCap)) {
							// System.out.println("iCap=" + iCap);
							/**
							 * 左边是说明文字 右边是下拉列表框
							 */
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(iCap);
							ll.addView(tvLeft, ll.getChildCount());
						} else if (Util.isEmpty(leftList)) {
							TextView tvLeft = new TextView(
									NativeModeNoPageActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);
							ll.addView(tvLeft, ll.getChildCount());
						}

						if (1 == item.dateCheck) {
							et.setMinWidth(240);
							// System.out.println("1 == item.dateCheck");
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							vs.add(et);
						} else if (Util.isEmpty(leftList)) {
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0,
										0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							vs.add(et);
						}
						if (!Util.isEmpty(leftList)) {
							// System.out.println("!Util.isEmpty(leftList)--->"
							// + leftList);
							Spinner spLeft = new Spinner(
									NativeModeNoPageActivity.this);
							spLeft.setTag(item);
							spLeft.setLayoutParams(WRAP_WRAP);
							// simple_spinner_item
							// R.layout.simple_spinner_dropdown_item
							ArrayAdapter<String> aa = new ArrayAdapter<String>(
									NativeModeNoPageActivity.this,
									R.layout.simple_spinner_adapter);
							aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
							for (String str : leftList) {
								aa.add(str);
							}
							spLeft.setAdapter(aa);

							if (!Util.isEmpty(amList)) {
								for (int j = 0; j < amList.size(); j++) {
									// 通过存的value得到位置
									if (j == i) {
										AnswerMap am = amList.get(i);
										int pos = aa.getPosition(am
												.getAnswerValue());
										if (-1 != pos) {
											// 选上位置
											spLeft.setSelection(pos);
											break;
										}
									}
								}
							}
							ll.addView(spLeft, ll.getChildCount());
							vs.add(spLeft);
						}
						bodyView.addView(ll, bodyView.getChildCount());
					} else if (Util.isEmpty(item.leftsideWord)
							&& !Util.isEmpty(item.rightsideWord)) {// 右边有文字
						// System.out.println("右边文字");
						/**
						 * 左边是文本框 右边是说明
						 */
						TextView tvRight = new TextView(
								NativeModeNoPageActivity.this);
						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(item.rightsideWord);
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						ll.addView(et, ll.getChildCount());
						ll.addView(tvRight, ll.getChildCount());
						bodyView.addView(ll, bodyView.getChildCount());
						vs.add(et);
					} else {// 两边都没有文字
						// System.out.println("两边都没文字");
						if (1 == item.dateCheck) {
							et.setMinWidth(240);
							// System.out.println("1 == item.dateCheck");
							Drawable d = getResources().getDrawable(
									R.drawable.day);
							et.setCompoundDrawablesWithIntrinsicBounds(null,
									null, //
									d, //
									null);
							et.setOnTouchListener(new OutDayTouchListener(
									NativeModeNoPageActivity.this, et,
									item.dateSelect));
						}
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0,
									true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									et.setText(am.getAnswerValue());
								}
							}
						}
						bodyView.addView(et, bodyView.getChildCount());
						vs.add(et);
					}
					if (setHideRb(item, q)) {
						ll.setVisibility(View.GONE);
						if (!Util.isEmpty(et.getText())) {
							et.setText("");
							int success = getQuestionAnswer( false, q,
									vs, null, mTempRows, false);
						}
					}
					et.addTextChangedListener(new EditChangedListener(q, vs,
							mTempRows, position));
				}
			} // 非滚动条
		}

		// 结束
	}

	public int getRandomQuestionIndex(Group group) {
		if (group.getIndexArr().isEmpty()) {
			return -1;
		}
		// group.getIndexArr();
		if (group.isRandom()) {
			/**
			 * 随机group子节点index节点
			 */
			Collections.shuffle(group.getIndexArr());
		}
		/**
		 * 获取随机后的group子节点的第一个节点
		 */
		Integer index = group.getIndexArr().get(0);
		/**
		 * 移除已获取的index节点
		 */
		group.getIndexArr().remove(index);
		/**
		 * 获取question_index=index节点在qs集合中的位置
		 */
		return null == iiMap.get(index) ? -1 : iiMap.get(index);
	}

	public int gotoQuestion(QGroup qGroup) {
		int _index = -1;
		if (qGroup.getGroups().isEmpty()) {
			return _index;
		}
		switch (qGroup.getGroupType()) {
		case QGroup.GROUP_TYPE_NONE:
		case QGroup.GROUP_TYPE_AUTO:
			if (QGroup.GROUP_TYPE_AUTO == qGroup.getGroupType()
					&& !qGroup.isAlreadyRandom()) {
				Collections.shuffle(qGroup.getGroups());
				/**
				 * 已经随机过
				 */
				qGroup.setAlreadyRandom(true);
			}
			Group group = qGroup.getGroups().get(0);
			/**
			 * 小题组遍历完成
			 */
			if (group.getIndexArr().isEmpty()) {
				if (2 <= qGroup.getGroups().size()) {
					/**
					 * 假如有3个group 分别为group_order=0, group_order=1, group_order=2
					 * 
					 * 假如当前group_order=0, 则nextGroup_order=1 假如当前group_order=1,
					 * 则nextGroup_order=2 假如当前group_order=2,
					 * 则nextGroup_order=3错误.
					 */
					Group nextGroup = qGroup.getGsMap().get(
							group.getOrder() + 1);
					if (null != nextGroup) {
						Group currGroup = qGroup.getGsMap().get(
								group.getOrder());
						/**
						 * 获取group与group中间间隔的index对应的order集合
						 */
						tempIndexArr.addAll(subIndexArr(
								currGroup.getIndexArr(),
								nextGroup.getIndexArr()));
						qGroup.getGroups().remove(group);
						_index = tempIndexArr.get(0);
						tempIndexArr.remove(0);
						/**
						 * 告诉createView方法需要显示这一道题
						 */
						isGroup = true;
						break;
					}
				}
				qGroup.getGroups().remove(group);
			}

			/**
			 * 所有的Group遍历完成
			 */
			if (qGroup.getGroups().isEmpty()) {
				isGroup = false;
				break;
			}

			Group _group = qGroup.getGroups().get(0);
			_index = getRandomQuestionIndex(_group);
			if (-1 == _index) {
				isGroup = false;
			} else {
				isGroup = true;
			}
			break;

		case QGroup.GROUP_TYPE_HAND:

			break;
		}
		return _index;
	}

	/**
	 * @param list1
	 *            index1的集合
	 * @param list2
	 *            index2的集合
	 * @return
	 */
	public ArrayList<Integer> subIndexArr(ArrayList<Integer> list1,
			ArrayList<Integer> list2) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		/**
		 * 第一个list取最大值
		 */
		int min = 0;
		int max = 0;

		/**
		 * index转换成order
		 */
		for (Integer i : list1) {
			// 取第一个list的最大值
			int temp = (null == iiMap.get(i) ? -1 : iiMap.get(i));
			min = temp > min ? temp : min;

		}

		/**
		 * index转换成order
		 */
		for (Integer i : list2) {
			// 取第二个list的最小值
			int temp = (null == iiMap.get(i) ? -1 : iiMap.get(i));
			max = temp < max ? temp : max;
		}

		/**
		 * 假如{min~max}={1,2,3,4,5,6}, 那么result={2,3,4,5}
		 */
		for (int i = (min - 1); i < max; i++) {
			result.add(i);
		}
		return result;
	}

	public int toNextIndex(ArrayList<Integer> list) {
		return Collections.max(list) + 1;
	}

	/**
	 * 设置上面是否能点击,isClickable是否是可以点击的.
	 */
	private void setTopClick(boolean isClickable) {
		nq_btn.setClickable(isClickable);
		bq_btn.setClickable(isClickable);
	}

	private final class ImageLongClickListener implements OnLongClickListener {
		private String path;

		public ImageLongClickListener(String _path) {
			this.path = _path;
		}

		@Override
		public boolean onLongClick(View v) {
			Intent it = new Intent(NativeModeNoPageActivity.this,
					ShowImageActivity.class);
			it.putExtra("image_path", path);
			NativeModeNoPageActivity.this.startActivity(it);
			return false;
		}

	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
		// 关闭服务
		if (MainService.isRun) {
			Intent i = new Intent(NativeModeNoPageActivity.this,
					MainService.class);
			NativeModeNoPageActivity.this.stopService(i);
		}
	}

	@Override
	public void finish() {
		super.finish();
		Publisher.getInstance().removeSubscriber(this);
		if (!(1 == survey.openGPS && 0 < survey.timeInterval)) {
			if (2 != survey.forceGPS && Cnt.LOC_SERVICE_START) {
				System.out.println("问卷界面销毁");
				ma.stopService(locIntent);
				Cnt.LOC_SERVICE_START = false;
			}
		}
	}

	/**
	 * 目录自动翻页的方法
	 * 
	 * @param forwardOrder
	 */
	private final int NEXT_READ_TITLE = 999;// 自动翻下一页常量
	private final int BACK_READ_TITLE = 998;// 自动翻上一页常量

	public void automaticPage(int forwardOrder) {
		// globleProgress.setVisibility(View.VISIBLE);
		show();
		if (realIndex != forwardOrder) {
			if (realIndex < forwardOrder) {
				Message msg = handler.obtainMessage();
				msg.what = NEXT_READ_TITLE;
				msg.arg1 = forwardOrder;
				handler.sendMessage(msg);
			} else if (realIndex > forwardOrder) {
				Message msg = handler.obtainMessage();
				msg.what = BACK_READ_TITLE;
				msg.arg1 = forwardOrder;
				handler.sendMessage(msg);
			}
		} else {
			// globleProgress.setVisibility(View.GONE);
			dismiss();
		}
	}

	private final int RECODE = 0x000001;
	private final int CAMERA = 0x000002;
	private final int VIDEO = 0x000003;
	private final int KNOWLEDGE = 0x000004;
	private final int PHOTO = 0x000005;

	private MenuItem miRecode = null;
	private MenuItem miCamera = null;
	private MenuItem miVideo = null;
	private MenuItem miKnowledge = null;
	private MenuItem miPhoto = null;

	// 菜单 大树动画 1 添加 菜单列表
	public HotalkMenuView menuListView = null;

	/**
	 * 系统菜单初始化 void 大树动画 2
	 */
	private void initSysMenu() {
		if (menuListView == null) {
			menuListView = new HotalkMenuView(this);
		}
		menuListView.listview.setOnItemClickListener(listClickListener);
		menuListView.clear();
		// 添加按位置添加 录音 拍照 知识库 打开图库 android.R.drawable.ic_menu_camera
		if (1 == feed.getSurvey().globalRecord) {
		} else if (1 == feed.getSurvey().isRecord) {
			if (isClicked) {
				menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, "关闭录音",
						R.drawable.audio_busy_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, "开启录音",
						R.drawable.ic_btn_speak_now_2);
			}
		}
		if (1 == feed.getSurvey().isPhoto) {
			menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, "拍照",
					R.drawable.ic_menu_camera_2);
			if (sdImages.getVisibility() == View.GONE) {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "打开图库",
						R.drawable.ic_menu_crop_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "关闭图库",
						R.drawable.ic_menu_crop_2);
			}
		} else if (isHaveSingle) {
			if (sdImages.getVisibility() == View.GONE) {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "打开图库",
						R.drawable.ic_menu_crop_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "关闭图库",
						R.drawable.ic_menu_crop_2);
			}
		}
		if (1 == feed.getSurvey().isVideo) {
			// R.drawable.presence_video_online_close
			menuListView.add(HotalkMenuView.MENU_DELETE_MULTI_MSG, "录像",
					R.drawable.presence_video_online_2);
		}
		menuListView.add(HotalkMenuView.MENU_MEMBER_MANAGER, "知识库",
				R.drawable.ic_menu_archive_2);
		menuListView.add(HotalkMenuView.MENU_SEND_MSG_FORMULAR, "退出访问",
				android.R.drawable.ic_menu_revert);
	}

	// 大树动画 3 添加
	protected void switchSysMenuShow() {
		// 初始化系统菜单
		initSysMenu();
		if (!menuListView.getIsShow()) {
			menuListView.show();
		} else {
			menuListView.close();
		}
	}

	// 大树动画 5 添加 菜单打开
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		switchSysMenuShow();
		return false;
	}

	/**
	 * 菜单点击事件处理 大树动画 6 跳转在这里 打开 录音 摄像 图片 等在这里
	 */
	OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			// 获取key唯一标识
			int key = Integer.parseInt(view.getTag().toString());
			ImageView selectIv = (ImageView) view
					.findViewById(R.id.menu_item_view_iv);
			// 跳转
			switch (key) {
			case HotalkMenuView.MENU_SEND_MSG_FORMULAR:
				System.out.println("退出访问");
				if (View.VISIBLE != vResult.getVisibility()) {
					if (isRecording) {// 如果是正在录音则等待0.1秒
						closeQRecord();
						new Handler().postDelayed(new Runnable() {
							public void run() {
								// execute the task
								if (1 == feed.getIsCompleted()) {
									handler.sendEmptyMessage(MSG_WRITE);
								} else {
									isShow = false;
									handler.sendEmptyMessage(MSG_SAVE);
								}
							}
						}, 100);
					} else {// 否则直接下一页
						if (1 == feed.getIsCompleted()) {
							handler.sendEmptyMessage(MSG_WRITE);
						} else {
							isShow = false;
							handler.sendEmptyMessage(MSG_SAVE);
						}
					}
				}
				break;
			case HotalkMenuView.MENU_VIEW_CONTACT:
				view.setBackgroundColor(Color.YELLOW);
				System.out.println("录音");
				openRecord(null, selectIv);
				// new RecordTask(isClicked, null, selectIv).execute();
				break;
			case HotalkMenuView.MENU_ADD_CONTACT:
			case HotalkMenuView.MENU_DELETE_MULTI_MSG:
				System.out.println("拍照+录像");
				view.setBackgroundColor(Color.YELLOW);
				Intent intent = new Intent(NativeModeNoPageActivity.this,
						PhotoActivity.class);
				Bundle bundle = new Bundle();
				UploadFeed photo = new UploadFeed();
				photo.setUserId(feed.getUserId());
				photo.setSurveyId(feed.getSurveyId());
				photo.setUuid(feed.getUuid());
				if (ma.cfg.getBoolean("save_inner", false)) {
					/**
					 * 存放在内部
					 */
					photo.setPath(Util.getRecordInnerPath(
							NativeModeNoPageActivity.this, feed.getSurveyId()));
					photo.setIsSaveInner(0);
				} else {
					/**
					 * 存放在外部
					 */
					photo.setPath(Util.getRecordPath(feed.getSurveyId()));
					photo.setIsSaveInner(1);
				}
				// 增加pid 命名规则
				photo.setName(Util.getRecordName(feed.getUserId(),
						feed.getSurveyId(), Cnt.FILE_TYPE_PNG, feed.getUuid(),
						null, feed.getPid(), feed.getParametersContent(),
						1 + ""));
				photo.setStartTime(System.currentTimeMillis());
				// 摄像传参判断是摄像还是拍照结束
				if (key == HotalkMenuView.MENU_ADD_CONTACT) {
					bundle.putString("opt", "0"); // 0代表拍照
				} else if (key == HotalkMenuView.MENU_DELETE_MULTI_MSG) {
					bundle.putString("opt", "1"); // 1代表摄像
					// 中断录音，开始录像。录像完毕后也要重新开启录音
					closeRecored();
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_close, Toast.LENGTH_SHORT).show();
				}
				// 摄像传参判断是摄像还是拍照结束
				bundle.putSerializable("photo", photo);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			case HotalkMenuView.MENU_MEMBER_MANAGER:
				System.out.println("知识库");
				view.setBackgroundColor(Color.YELLOW);
				Intent knowIntent = new Intent();
				knowIntent.setClass(NativeModeNoPageActivity.this,
						KnowleageActivity.class);
				startActivity(knowIntent);
				overridePendingTransition(R.anim.right, R.anim.left);
				break;

			case HotalkMenuView.MENU_ADD_TO_FAVORITES:
				System.out.println("打开图库");
				view.setBackgroundColor(Color.YELLOW);
				if (sdImages.getVisibility() == View.VISIBLE) {
					sdImages.setVisibility(View.GONE);
				} else {
					sdImages.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			// 关闭
			menuListView.close();
		}
	};

	/**
	 * 创建菜单,只在创建时调用一次. 菜单操作 大树动画 4 注释掉
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	// 大树拒访 在这里 访问状态
	@Override
	public void onItemClick(int index) {
		// TODO Auto-generated method stub
		ReturnType rt = rtList.get(index);
		returnTypeId = rt.getReturnId();
		returnName = rt.getReturnName();
		if (1 == feed.getIsCompleted()) {
			handler.sendEmptyMessage(MSG_WRITE);
		} else {
			isShow = false;
			handler.sendEmptyMessage(MSG_SAVE);
		}
		Toasts.makeText(this, "选中了" + rt.getReturnName(), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	// 录音 的各种状态 开启 关闭 点击真否
	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		case TaskType.TS_RECORED:
			if (null != param[1]) {
				int state = (Integer) param[1];
				if (state == 30) {
				} else if (state == 40) {
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_close, Toast.LENGTH_SHORT).show();
				} else if (state == 20) {
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open, Toast.LENGTH_SHORT).show();
					isClicked = true;
				} else if (state == 21) {
					Toasts.makeText(NativeModeNoPageActivity.this,
							R.string.record_open_false, Toast.LENGTH_LONG)
							.show();
					if (View.VISIBLE != vResult.getVisibility()) {
						if (1 == feed.getIsCompleted()) {
							handler.sendEmptyMessage(MSG_WRITE);
						} else {
							isShow = false;
							handler.sendEmptyMessage(MSG_SAVE);
						}
					}
				} else if (state == 10) {
					isClicked = false;
				}
			}
			break;
		}
	}

	@Override
	public void onPublish(int key, Object... data) {
		if (key == SubscriberKey.KEY_LOCATION_UPDATE) {
			if (data == null || data.length < 1) {
				return;
			}
			Object obj = data[0];
			if (obj instanceof BDLocation) {
				mBDLocation = (BDLocation) obj;
			}
		}
		if (null != mBDLocation) {
			Double lat = mBDLocation.getLatitude();
			Double lng = mBDLocation.getLongitude();
			if (null != lat) {
				String latStr = String.valueOf(lat);
				if (-1 < latStr.indexOf("E") || -1 < latStr.indexOf("e")) {
				} else {
					feed.setLat(latStr);
				}
			}
			if (null != lng) {
				String lngStr = String.valueOf(lng);
				if (-1 < lngStr.indexOf("E") || -1 < lngStr.indexOf("e")) {
				} else {
					feed.setLng(lngStr);
				}
			}
		}
		if (2 != survey.forceGPS && !Util.isEmpty(feed.getLat())
				&& Cnt.LOC_SERVICE_START) {
			feed.setVisitAddress(Util.isEmpty(mBDLocation.getAddrStr()) ? feed
					.getVisitAddress() : mBDLocation.getAddrStr());
			System.out.println("问卷发布界面");
			if (!(1 == survey.openGPS && 0 < survey.timeInterval)) {
				if (2 != survey.forceGPS && Cnt.LOC_SERVICE_START) {
					ma.stopService(locIntent);
					Cnt.LOC_SERVICE_START = false;
				}
			}
		}
	}

	/**
	 * 获得版本号
	 */
	private String getVersion() {
		String mVersion = "";
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(),
					PackageManager.GET_ACTIVITIES);
			mVersion = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			mVersion = "";
		}
		return mVersion;
	}

//	/**
//	 * 下载内部名单方法
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	private class InnerTask extends AsyncTask<Void, Integer, Boolean> {
//		private String authorId;
//		private String userId;
//		private Survey survey;
//
//		public InnerTask(String _authorId, String _userId, Survey _survey) {
//			this.authorId = _authorId;
//			this.userId = _userId;
//			this.survey = _survey;
//
//		}
//
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			// http://www.dapchina.cn/newsurvey/alisoft/DownloadUser.asp?AuthorID=1514&SurveyID=3076
//			try {
//				HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("AuthorID", this.authorId);
//				hm.put("SurveyID", survey.surveyId);
//				InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm,
//						"GET");
//				OpenStatus os = XmlUtil.ParserInnerPanelList(inStream);
//				// 遍历下载的内部名单panel号
//				ArrayList<String> spList = new ArrayList<String>();
//				for (int i = 0; i < os.getIps().size(); i++) {
//					InnerPanel ip = os.getIps().get(i);
//					// 引用受访者参数
//					ParameterInnerPanel pip = os.getParameterIps().get(i);
//					ArrayList<Parameter> parameters = pip.getParameters();
//					pip.setParameters(parameters);
//					// 引用受访者参数结束
//					// 命名规则开始
//					// 不为空判断
//					String content = "";
//					if (!Util.isEmpty(os.getParameterName())) {
//						for (Parameter parameter : parameters) {
//							if (parameter.getSid()
//									.equals(os.getParameterName())) {
//								content = parameter.getContent();
//								// 是中文的就置为空
//								if (Util.isContainChinese(content)) {
//									content = "";
//								}
//								break;
//							}
//						}
//					}
//					// 命名规则结束
//					if (!Util.isEmpty(ip.getFeedId())) {
//						spList.add(ip.getFeedId());
//						String str = XmlUtil.paserInnerPanel2Json(ip);
//						if (ma.dbService.isFeedExist(survey.surveyId,
//								ip.getFeedId())) {
//							// 假如服务器上有id 本地上也有id的更新。引用受访者参数
//							// System.out.println("更新");
//							ma.dbService.updateInnerUploadFeed(survey.surveyId,
//									ip.getPanelID(), str, ip.getFeedId(),
//									pip.getParametersStr());
//						} else {
//							// 假如服务器上有id 本地上没有id的增加。
//							String uuid = UUID.randomUUID().toString();
//							String path = Util.getXmlPath(
//									NativeModeNoPageActivity.this,
//									survey.surveyId);
//
//							// 增加pid 命名规则
//							String AUTHORID = ma.cfg
//									.getString(Cnt.AUTHORID, "");
//							String name = Util.getXmlName(AUTHORID, userId,
//									survey.surveyId, uuid, ip.getPanelID(),
//									content);
//							ma.dbService.addInnerUploadFeed(ip.getFeedId(),
//									userId, survey.surveyId, uuid,
//									System.currentTimeMillis(), path, name,
//									survey.visitMode, str, ip.getPanelID(),
//									pip.getParametersStr());
//						}
//					}
//				}
//				ArrayList<String> dbList = ma.dbService.getListBySurveyId(
//						survey.surveyId, ma.userId);
//				/**
//				 * 判断服务器上没有 本地上有这个panelid ,剩下的dblist就是剩下的服务器没有的。该删除的
//				 * 把查出来的panelId删除(没数据的删除，有数据的不删除?但传不传,不传)
//				 */
//				for (int i = dbList.size() - 1; i >= 0; i--) {
//					String temp = dbList.get(i).split(":::")[0];
//					for (int j = 0; j < spList.size(); j++) {
//						if (temp.equals(spList.get(j))) {
//							dbList.remove(i);
//							break;
//						}
//					}
//				}
//				// 命名规则更改
//				ma.dbService.updateSurveyOpenStatus(survey.surveyId,
//						os.getParameter1(), os.getParameter2(),
//						os.getParameter3(), os.getParameter4(),
//						os.getParameterName());
//				return true;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return false;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			super.onPostExecute(result);
//			if (result) {
//				ma.dbService.updateSurveyInnerDone(survey.surveyId);
//				Toasts.makeText(NativeModeNoPageActivity.this,
//						R.string.survey_add_complete, Toast.LENGTH_SHORT)
//						.show();
//			} else {
//				Toasts.makeText(NativeModeNoPageActivity.this,
//						R.string.inner_failure, Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
//		}
//	}

	/**
	 * 输入框的文本改变事件监听
	 * 
	 * @param et
	 * @param item
	 */
	AshingBean ashingBean;

	public void setetlinster(String editable, String contentid, int index,String qIndex) {
		
		
		if (!Util.isEmpty(qidlist)) {
			for (int t = 0; t < qidlist.size(); t++) {
				if (qidlist.get(t).Qid.equals(qIndex)) {
					final ArrayList<AshingBean> ashingBeans = qidlist.get(t).getAshingBeans();
					
					
					Log.i(TAG, "setetlinster");
					// 这个方法被调用，那么说明s字符串的某个地方已经被改变。
					Log.e(TAG, "afterTextChanged: 修改完字符串");
					for (int j = 0; j < ashingBeans.size(); j++) {
						if (contentid.equals(ashingBeans.get(j).getContetnId())) {
							Log.d(TAG, "afterTextChanged: contentid=" + contentid);
							Log.d(TAG,
									"afterTextChanged: getContetnId="
											+ ashingBeans.get(j).getContetnId());
							Log.d(TAG,
									"afterTextChanged: ashingBeans="
											+ ashingBeans.toString());
							ashingBeans.get(j).setContetntext(editable.toString().trim());
							boolean isashing = LimitlistUtil.isAsh(ashingBeans.get(j)
									.getContetntext(), ashingBeans.get(j).getOperator(),
									ashingBeans.get(j).getParameter());
							ashingBeans.get(j).setIsAshing(isashing);
							Log.d(TAG, "afterTextChanged: isashing=" + isashing);

						}
					}

					for (int i = 0; i < ettagids.size(); i++) {
						ArrayList<AshingBean> ashs = new ArrayList<AshingBean>();
						for (int j = 0; j < ashingBeans.size(); j++) {
							if (ettagids.get(i).equals(ashingBeans.get(j).getEtId())) {
								ashs.add(ashingBeans.get(j));
							}
						}
						for (int j = 0; j < ashs.size(); j++) {
							System.out.println("ashs=====" + ashs.toString());
						}

						for (int j = 0; j < ashs.size(); j++) {
							String ettagid = ashs.get(j).getEtId();

							boolean s = false;

							if (ashs.size() == 0) {

							}
							if (ashs.size() == 1) {
								s = ashs.get(j).getIsAshing();
							}
							if (ashs.size() > 1) {
								for (int k = 0; k < ashs.size() - 1; k++) {
									s = LimitlistUtil.getTemp(ashs.get(k), ashs.get(k + 1));
								}

							}
							System.out.println("s=" + s);
							if (s) {
								for (int m = 0; m < vss.size(); m++) {
									if (m == index) {
										ArrayList<View> vs = vss.get(m);
										for (int k = 0; k < vs.size(); k++) {
											QuestionItem item = (QuestionItem) vs.get(k)
													.getTag();
											if (vs.get(k) instanceof UIEditText) {
												if (("" + item.getItemValue())
														.equals(ettagid)) {
													UIEditText et = (UIEditText) vs.get(k);
													et.setFocusable(false);
													et.setFocusableInTouchMode(false);
													et.getText().clear();
													et.setBackgroundResource(R.drawable.bg_edittext_recursion);
													Log.e(TAG, "onClick: UIEditText不可输入");
												}
											}
											if (vs.get(k) instanceof Spinner) {
												if (("" + item.getItemValue())
														.equals(ettagid)) {
													Spinner sp = (Spinner) vs.get(k);
													sp.setEnabled(false);
													sp.setSelection(0, true);
													Log.e(TAG, "onClick: Spinner不可输入");
												}
											}
										}
									}
								}

							} else {
								for (int m = 0; m < vss.size(); m++) {
									if (m == index) {
										ArrayList<View> vs = vss.get(m);
										for (int k = 0; k < vs.size(); k++) {

											QuestionItem item = (QuestionItem) vs.get(k)
													.getTag();
											if (vs.get(k) instanceof UIEditText) {
												if (("" + item.getItemValue())
														.equals(ettagid)) {
													UIEditText et = (UIEditText) vs.get(k);
													et.setFocusableInTouchMode(true);
													et.setFocusable(true);
													et.setBackgroundResource(R.drawable.bg_edittext);

													Log.e(TAG, "onClick: UIEditText可以输入");
												}
											}
											if (vs.get(k) instanceof Spinner) {
												if (("" + item.getItemValue())
														.equals(ettagid)) {
													Spinner sp = (Spinner) vs.get(k);
													sp.setEnabled(true);
													Log.e(TAG, "onClick: Spinner可以输入");
												}
											}

										}
									}
								}
							}
						}

					}

					
				}
			}
		}
	}
	
	
	/**
	 * 有定位点了调用改变图标为黄色
	 */
	private void changelocimg(boolean isgps){
		if (isgps) {
			locationshow.setImageResource(R.drawable.locationshow);
		}else{
			locationshow.setImageResource(R.drawable.locationgone);
		}
	}
	/**
	 * 定位图标显示与隐藏
	 */
	private void showlocationicon(Survey s) {
		// TODO Auto-generated method stub
		if (s.getForceGPS() == 2) {
			locationshow.setVisibility(View.GONE);
		}else {
			locationshow.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 修复一页多题显示隐藏逻辑BUG
	 * @param i
	 */
	private void checknextquestion(int i) {
		// TODO Auto-generated method stub
		ma.dbService.deleteQindexAnswer(feed.getSurveyId(),feed.getUuid(),qs.get(i).qIndex+"");

	}
	
	
	//查重功能 上传数据判断查重（其他不调用）
	 private void uploadhttp(String type) {
		 
		 //查重固定98	
		 uploadtype = type;
			//保存数据
			saveXML();
			Log.i(TAG, "开始查重！！");
//	        String url = "http://www.survey-expert.cn/test/alisoft/OfflineUpload.aspx";
	        HashMap<String,String> paramsMap = new HashMap<String, String>();
	        paramsMap.put("userId",ma.userId);
	        paramsMap.put("userPsd", ma.cfg.getString(Cnt.USER_PWD, ""));
	        paramsMap.put("surveyId",feed.getSurveyId());
	        paramsMap.put("ModelFlag","1");
	        
//	        Log.i(TAG, "username:"+ma.userId);
//	        Log.i(TAG, "passworld:"+ma.cfg.getString(Cnt.USER_PWD, ""));
//	        Log.i(TAG, "surveyid:"+feed.getSurveyId());
	        
	        String filepath =feed.getPath()+File.separator+feed.getName();
	       

	        try {
				byte[] buf = FileUpLoad.get64Bytes(feed.getPath(), feed.getName());
				
				SDCardUtils.byte2File(buf, SDCardUtils.getSDCardPath()+File.separator, feed.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        //临时文件，用完了删除
	        final File file = new File(SDCardUtils.getSDCardPath()+File.separator, feed.getName());
	        
	        Xutils.getInstance().upLoadFile(Cnt.UPLOAD_URL, paramsMap, file, new Xutils.XCallBack() {
	            @Override
	            public void onResponse(String result) {

	                Log.i(TAG, "onResponse: result:"+result);

	                Repeat repeat = XmlService.DomXml(result);
	                
	                //修改feedid
	                feed.setFeedId(repeat.getFID());
	                ma.dbService.updateUploadFeedId(feed);
	                //上传状态改回来
	                uploadtype = "100";
	                //删除临时文件
	                SDCardUtils.deleteFile(file);
	                
	                
	                String state = repeat.getS();
	                //没有重复的
	                if (state.equals("100")) {
		                loadnextpage();
	                }else
	                //有重复的
	                if (state.equals("95")) {
	                	String text = "";
	                	for (int i = 0,length = repeat.getMSG().getData().size(); i < length; i++) {
	                		DataBean data = repeat.getMSG().getData().get(i);
	                		String qusetionid = qs.get(Integer.parseInt(data.getIndex())).qid;
	                		text += qusetionid + "题、" + data.getCause()+"\n";
						}
	                	DialogUtil.newdialog(NativeModeNoPageActivity.this,text);
	                	setTopClick(true);
					}else{
						//其他情况不明 继续往后走
						loadnextpage();
					}
	                
	                
	               
	            }


	        });
	    }
	 
	 /**
	  *查重后下一页操作
	  */
		private void loadnextpage() {
			
			String nqbtnstr = nq_btn.getText().toString().trim();
			String commit = getResources().getString(R.string.commit_survey);
			
			//先判断gps
			int forceGPS = survey.getForceGPS();
//			if (forceGPS == 1) {
//				System.out.println("forceGPS:"+forceGPS);
//				boolean isgps = CheckGps.getInstance().checkGPSDB(ma,feed.getName());
//				//修改定位图标
//				changelocimg(true);
//				
//				 //如果是最后一题这就是"提 交"
//					System.out.println("是最后一题这");
//					// 只有forceGPS == 1 的时候强制判断GPS定位信息，其他情况有没有无所谓
//					if (forceGPS != 2) {
//						System.out.println("有没有gps定位：isgps:"+isgps);
//						// 没有定位点就重新去定位，有定位就直接过了
//						if (!isgps) {
//							CheckGps.getInstance().startlocation(feed.getUuid(),ma);
//						}
//					}
//			}
			boolean isgps = CheckGps.getInstance().checkGPSDB(ma,
					feed.getName());
					if (forceGPS != 2) {
						System.out.println("有没有gps定位：isgps:" + isgps);
						BaseLog.w("有没有gps定位：isgps:" + isgps);
						// 没有定位点就重新去定位，有定位就直接过了
						if (!isgps) {
							CheckGps.getInstance().startlocation(feed.getUuid(), ma);
						}else {
							changelocimg(true);
						}
					}
			setTopClick(false);
			scroll_view.scrollTo(1, 1);
			pageNum++;
			if (pageNum < aryQs.size()) {
				globleProgress.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						es.execute(new Runnable() {
							public void run() {
								System.out
										.println("handler.sendEmptyMessage(ADD_ALL);");
								handler.sendEmptyMessage(ADD_ALL);
							}
						});
					}
				}, 100);
			} else {
				handler.sendEmptyMessage(MSG_WRITE);
				return;
			}
		}
	 
	 
}
