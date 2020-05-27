package com.investigate.newsupper.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.ImageAdapter;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.bean.CstmMatcher;
import com.investigate.newsupper.bean.MatcherItem;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.ComUtil;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.ThreeLeverUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.HotalkMenuView;

public class ReviewCommitActivity extends BaseActivity implements OnClickListener{
	
	private static final String TAG = ReviewCommitActivity.class.getSimpleName();

	MyApp ma;
	UploadFeed feed;
	LinearLayout ll;

	private Display dis;
	private LayoutInflater inflater;
	private ArrayList<Question> qs;
	private UITextView review_survey_title_tv;

	private final LayoutParams FILL_WRAP = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	private final LayoutParams WRAP_WRAP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private SlidingDrawer sdImages;
	// private ImageView ivDragIcon;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private ImageAdapter mImageAdapter;
	private TextView tvImageCount;
	private LinearLayout native_left_iv, native_opt;
	private Button leftBtn,rightBtn;
	
	/**
	 * 设备屏幕的宽
	 */
	private int screenWidth;
	/**
	 * 设备屏幕的高
	 */
	private int screenHeight;
	HashMap<Integer, Answer> anMap;
	private boolean isNew = true;
	// 屏幕的宽度
	private int maxCWidth;
	/**
	 * 判断是否是单题拍照
	 */
	private boolean isHaveSingle;
	// 问卷字号动态设置
	private int surveySize;// 最小的字体
	private int lowSurveySize;// 说明字体大小
	private int middleSueveySize;// 中等字体
	private int bigSurveySize;// 大字体
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

	int provincePos = 0;// 一级联动 点击的 位置

	int cityPos = 0;
	int areaPos = 0;
	int itemLL_Index = 0;

	private Spinner provinceSpinner = null;
	private Spinner citySpinner = null;
	private Spinner countrySpinner = null;
	private ArrayAdapter<String> provinceAdapter = null;
	private ArrayAdapter<String> countryAdapter = null;
	private ArrayAdapter<String> cityAdapter = null;

	// 三级联动 结束

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		feed = (UploadFeed) getIntent().getExtras().get("feed");
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(getApplicationContext());
		}
		if (1 == ma.cfg.getInt("ScreenOrientation", 0)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		surveySize = (int) (UIUtils.getDimenPixelSize(R.dimen.survey_text_size) * TextSizeManager.getInstance().getRealScale());
		lowSurveySize = surveySize;
		middleSueveySize = (int) (UIUtils.getDimenPixelSize(R.dimen.survey_middle_text_size) * TextSizeManager.getInstance().getRealScale());
		bigSurveySize = (int) (UIUtils.getDimenPixelSize(R.dimen.survey_big_text_size) * TextSizeManager.getInstance().getRealScale());
		/**
		 * 初始化问卷字号动态设置完毕
		 */
		setContentView(R.layout.activity_commit_review);
		review_survey_title_tv = (UITextView) findViewById(R.id.review_survey_title_tv);
		TextSizeManager.getInstance().addTextComponent(TAG, review_survey_title_tv);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		/**
		 * 获取显示
		 */
		dis = getWindowManager().getDefaultDisplay();
		maxCWidth = (int) (dis.getWidth() * 0.96);
		// ListView lvReview = (ListView) findViewById(R.id.review_list);
		//
		ll = (LinearLayout) findViewById(R.id.review_ll);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sdImages = (SlidingDrawer) findViewById(R.id.pic_sd);
		ReviewCommitActivity.this.setResult(3);
		if (null != feed) {
			show();
			new ShowTask().execute();
		} else {
			finish();
		}
		leftBtn = (Button) findViewById(R.id.left_btn);
		rightBtn = (Button) findViewById(R.id.right_btn);
		/**
		 * 预览音视频，录音按钮
		 * */
		native_opt = (LinearLayout) findViewById(R.id.native_opt);
		native_opt.setOnClickListener(this);
		native_left_iv = (LinearLayout) findViewById(R.id.native_left_iv);
		native_left_iv.setOnClickListener(this);
		
		/**
		 * 实例化抽屉控件
		 */
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(new CustomViewFactor());
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		g = (Gallery) findViewById(R.id.gallery);
		// g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(new CustomItemSelectListener());
		/**
		 * 实例化抽屉控件装载图片的GridView
		 */
		sdImages.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				/**
				 * 顶部的上一题、下一题界面隐藏掉
				 */
				/**
				 * 打开抽屉控件
				 */
				// ivDragIcon.setImageResource(R.drawable.arrow_down_float);
				ArrayList<UploadFeed> images = ma.dbService.getImages(feed.getUuid(), feed.getSurveyId());
				//去除缩略图显示
				Iterator<UploadFeed> iter = images.iterator();
			        while(iter.hasNext()){
			        	UploadFeed image = iter.next();
			        	if("thumbnail".equals(image.getName().substring(image.getName().lastIndexOf("_")+1, image.getName().lastIndexOf(".")))){
							iter.remove();
						}else {
						}
			        }
				
				if (null == tvImageCount) {
					tvImageCount = (TextView) findViewById(R.id.img_count_tv);
				}
				if (0 < images.size()) {
					if (null == mImageAdapter) {
						mImageAdapter = new ImageAdapter(ReviewCommitActivity.this, images);
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
			}
		});
	}

	private final class ShowTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			qs = ma.dbService.getQuestionList(feed.getSurveyId());
			if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted()//
					&& Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
				anMap = XmlUtil.getFeedAnswer(new File(feed.getPath(), feed.getName()));
			}
			int headNum = 0;
			for (int i = 0; i < qs.size(); i++) {
				Question question = qs.get(i);
				if (1 == question.qCamera) {
					isHaveSingle = true;
				}
				if (Cnt.TYPE_HEADER == question.qType) {
					headNum += 1;
				}
				if (headNum > 1) {
					if (Util.isEmpty(question.qid)) {
						question.qid = "Q" + (question.qOrder - headNum + 1);
					}
				}
			}
			return 0 != qs.size();
		}

		@Override
		protected void onPreExecute() {
			
			refreshView();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (1 != feed.getSurvey().isPhoto && !isHaveSingle) {
				sdImages.setVisibility(View.GONE);
			}
			for (int i = 0; i < qs.size(); i++) {
				handler.sendEmptyMessageDelayed(i, 6 * i);
			}
			super.onPostExecute(result);
		}

	}
	/**
	 * 设置上面是否能点击,isClickable是否是可以点击的.
	 */
	private void setTopClick(boolean isClickable) {
		leftBtn.setClickable(isClickable);
		rightBtn.setClickable(isClickable);
	}
	public void btnClick(View v) {
		setTopClick(false);
		switch (v.getId()) {
		case R.id.left_btn://返回修改
			this.setResult(3);
			finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.right_btn://确认提交
			this.setResult(4);
			finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		}
	}
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			createQuestionBodyView(qs.get(msg.what));
			if (msg.what == (qs.size() - 1)) {
				dismiss();
			}
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshView();
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
		// int maxWidth = (int) (screenWidth * 0.9);
	}

	public void createQuestionBodyView(Question q) {// 动态生成题干的方法
		// System.out.println("q.qTitle"+q.qTitle);
		Answer tempAnswer = null;
		/**
		 * 完成且上传过
		 */
		// if (Cnt.VISIT_STATE_COMPLETED == feed.getIsCompleted()//
		// && Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
		// tempAnswer = anMap.get(q.qIndex);
		// } else {
		// }
		tempAnswer = ma.dbService.getAnswer(feed.getUuid(), q.qIndex + "");

		ArrayList<AnswerMap> amList = new ArrayList<AnswerMap>();
		if (Cnt.TYPE_MEDIA == q.qType) {

		} else if (null != tempAnswer) {
			amList = tempAnswer.getAnswerMapArr();
		} else {
			return;
		}
		RelativeLayout questionView = (RelativeLayout) inflater.inflate(R.layout.review_list_item, null);

		TextView tvTitle = (TextView) questionView.findViewById(R.id.review_title_tv);
		tvTitle.setMaxWidth((int) (dis.getWidth() * 0.9));
		TextView tvCaption = (TextView) questionView.findViewById(R.id.review_caption_tv);
		tvCaption.setMaxWidth((int) (dis.getWidth() * 0.9));
		tvCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		LinearLayout llCaption = (LinearLayout) questionView.findViewById(R.id.review_caption_ll);
		TextView tvComment = (TextView) questionView.findViewById(R.id.review_comment_tv);
		tvComment.setMaxWidth((int) (dis.getWidth() * 0.9));
		tvComment.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		LinearLayout bodyView = (LinearLayout) questionView.findViewById(R.id.review_body_ll);
		LinearLayout llComment = (LinearLayout) questionView.findViewById(R.id.review_comment_ll);

		// if (null != tempAnswer) {
		// System.out.println("答案UUID--->" + tempAnswer.uuid);
		// } else {
		// System.out.println("Question--->" + q.qIndex);
		// }

		/**
		 * 获取引用其他问题的集合
		 */
		// ArrayList<Restriction> rs = q.getResctItemArr();

		// System.out.println("当前question_index--->" + q.qIndex);

		/**
		 * 设置标题的位置
		 */
		if (Cnt.POS_LEFT.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.LEFT);
		} else if (Cnt.POS_CENTER.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		} else if (Cnt.POS_RIGHT.equals(q.qTitlePosition)) {
			tvTitle.setGravity(Gravity.RIGHT);
		} else {
			tvTitle.setGravity(Gravity.LEFT);
		}

		if (Cnt.TYPE_HEADER == q.qType) {// 假如是标题
			/**
			 * 设置标题的颜色
			 */
			tvTitle.setTextColor(Color.BLUE);

			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, bigSurveySize);
			/**
			 * 显示标题文本
			 */
			if (!Util.isEmpty(q.qTitle)) {
				tvTitle.setText(q.qTitle);
			}
			bodyView.setVisibility(View.GONE);
		} else {
			// 更改的样式
			if (0 == q.qTitleDisable) {
				tvTitle.setClickable(false);
			}
			tvTitle.setTextColor(Color.BLACK);
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, middleSueveySize);

			// StringBuilder sbTitle = new StringBuilder("");
			String strTilte = "";
			if (!Util.isEmpty(q.qid)) {
				strTilte = q.qid + ". " + q.qTitle;
			} else {
				if (!Util.isEmpty(q.qTitle)) {
					strTilte = ReviewCommitActivity.this.getResources().getString(R.string.question_order, q.qOrder)
							+ q.qTitle;
				} else {
					strTilte = ReviewCommitActivity.this.getResources().getString(R.string.question_order, q.qOrder);
				}

			}

			/**
			 * 标题逻辑引用
			 */
			CstmMatcher qutoMatcherList = Util.findMatcherItemList(strTilte, ma, feed.getUuid(), q.surveyId);
			boolean qutoHave = Util.isEmpty(qutoMatcherList.getResultStr());
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
				ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(parametersStr,
						Parameter.class);
				if (!Util.isEmpty(tParameters)) {
					parameterList.addAll(tParameters);
				}
			}
			CstmMatcher parameterMatcherList = Util.findMatcherPropertyItemList(strTilte, parameterList);
			boolean parameterHave = Util.isEmpty(parameterMatcherList.getMis());
			if (!parameterHave) {
				strTilte = parameterMatcherList.getResultStr();
			}
			/**
			 * 引用受访者参数结束
			 */

			/**
			 * 必填
			 */
			if (1 == q.qRequired) {
				strTilte = strTilte + "<font color='red'>" + getResources().getString(R.string.question_must)
						+ "</font>";
			}
			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth, tHeight);
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
				String radomed = getResources().getString(R.string.question_random);
				SpannableString ss = new SpannableString(radomed);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}

			if (0 < q.lowerBound || 0 < q.upperBound) {
				String bound = getResources().getString(R.string.question_bound, q.upperBound, q.lowerBound);
				SpannableString ss = new SpannableString(bound);
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0, bound.length(), Spanned.SPAN_COMPOSING);
				tvTitle.append(ss);
			}

		}

		// tvTitle.setTextSize(30);

		/**
		 * 假如题目有上方追加说明
		 */
		if (!Util.isEmpty(q.qCaption)) {
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
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth, tHeight);
						return drawable;
					} else {
						return null;
					}
				}
			};
			Spanned fromHtml = Html.fromHtml(q.qCaption, imgGetter, null);
			tvCaption.setText(fromHtml);
			// 更改的样式
		}

		/**
		 * 假如题目有下方追加说明
		 */
		if (!Util.isEmpty(q.qComment)) {
			/**
			 * 设置上方追加说明的位置
			 */
			if (Cnt.POS_CENTER.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.CENTER_HORIZONTAL);
			} else if (Cnt.POS_RIGHT.equals(q.qCommentPosition)) {
				tvComment.setGravity(Gravity.RIGHT);
			}

			// 更改的样式
			ImageGetter imgGetter = new Html.ImageGetter() {
				public Drawable getDrawable(String source) {
					Drawable drawable = null;
					String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey" + File.separator
							+ feed.getSurveyId() + File.separator + source;
					System.out.println("name:" + name);
					drawable = Drawable.createFromPath(name);
					Bitmap image = BitmapFactory.decodeFile(name);
					if (image != null) {
						int tWidth = image.getWidth();
						int tHeight = image.getHeight();
						drawable.setBounds(0, 0, tWidth, tHeight);
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
		// 大树 添加 双引用在里实现 双引用
		switch (q.qType) {
		case Cnt.TYPE_HEADER:// 标题
			// tvTitle.setMaxWidth(Integer.MAX_VALUE);
			break;

		case Cnt.TYPE_RADIO_BUTTON:// 单选按钮
			/**
			 * 行
			 */
			bodyView.setOrientation(LinearLayout.VERTICAL);
			ArrayList<QuestionItem> radioRows = q.getRowItemArr();
			if (!Util.isEmpty(radioRows)) {
				// boolean isInclusion = false;
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
				/**
				 * 处理随机项
				 */
				for (int i = 0; i < radioRows.size(); i++) {
					QuestionItem item;

					// 题外关联 之 选项置顶 文本 获取

					/**
					 * 假如题目要求选项随机
					 */
					item = tempRows.get(i);
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
						LinearLayout rbA = new LinearLayout(ReviewCommitActivity.this);
						rbA.setOrientation(LinearLayout.VERTICAL);
						if (isMultiline) {// 如果是多列则进入判断
							if (actualRowsNum == lineNum) {
								lineNum = 0;
							}
							if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
								LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
								rbM.setOrientation(LinearLayout.HORIZONTAL);
								bodyView.addView(rbM, bodyView.getChildCount());
								arrLayout.add(rbM);
							}
							lineNum++;
						}
						ArrayList<QuestionItem> childRows = item.getChildRows();
						if (0 < childRows.size()) {// 是否有子项
							TextView tvItemTitle = new TextView(ReviewCommitActivity.this);
							// 追加说明新布局原FILL_WRAP
							tvItemTitle.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							tvItemTitle.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							final float imgItemWidth = rowsOneWidth;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
											+ File.separator + feed.getSurveyId() + File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgItemWidth) {
											tHeight = imgItemWidth / tWidth * tHeight;
											tWidth = imgItemWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
							tvItemTitle.setText(fromHtml);
							rbA.addView(tvItemTitle);
							for (int cr = 0; cr < childRows.size(); cr++) {
								QuestionItem childItem = childRows.get(cr);
								lineNum = addRb(rowsOneWidth, amList, arrLayout, childItem, rbA, otherItems, bodyView,
										q, lineNum);

							}
						} else {
							lineNum = addRb(rowsOneWidth, amList, arrLayout, item, rbA, otherItems, bodyView, q,
									lineNum);
						}
						/**
						 * 单选题目选项有图片
						 */
						if (0 < arrLayout.size()) {
							LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
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
					LinearLayout rbA = new LinearLayout(ReviewCommitActivity.this);
					rbA.setOrientation(LinearLayout.VERTICAL);
					if (isMultiline) {// 如果是多列则进入判断
						if (actualRowsNum == lineNum) {
							lineNum = 0;
						}
						if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
							LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
							rbM.setOrientation(LinearLayout.HORIZONTAL);
							bodyView.addView(rbM, bodyView.getChildCount());
							arrLayout.add(rbM);
						}
						lineNum++;
					}
					if (1 != item.isOther) {
						ArrayList<QuestionItem> childRows = item.getChildRows();
						if (0 < childRows.size()) {// 是否有子项
							TextView tvItemTitle = new TextView(ReviewCommitActivity.this);
							// 追加说明新布局原FILL_WRAP
							tvItemTitle.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							tvItemTitle.setTextColor(Color.GRAY);// 统计局专有页面
							tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							final float imgItemWidth = rowsOneWidth;
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
											+ File.separator + feed.getSurveyId() + File.separator + source;
									// System.out.println("name:" + name);
									drawable = Drawable.createFromPath(name);
									Bitmap image = BitmapFactory.decodeFile(name);
									if (image != null) {
										float tWidth = image.getWidth();
										float tHeight = image.getHeight();
										if (tWidth > imgItemWidth) {
											tHeight = imgItemWidth / tWidth * tHeight;
											tWidth = imgItemWidth;
										}
										drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
										return drawable;
									} else {
										return null;
									}
								}
							};
							Spanned fromHtml = Html.fromHtml(item.getItemText(), imgGetter, null);
							tvItemTitle.setText(fromHtml);
							rbA.addView(tvItemTitle);
							for (int cr = 0; cr < childRows.size(); cr++) {
								QuestionItem childItem = childRows.get(cr);
								lineNum = addRb(rowsOneWidth, amList, arrLayout, childItem, rbA, otherItems, bodyView,
										q, lineNum);

							}
						} else {
							lineNum = addRb(rowsOneWidth, amList, arrLayout, item, rbA, otherItems, bodyView, q,
									lineNum);
						}
					} else {
						/**
						 * 生成一个单选布局
						 */
						LinearLayout rbLL = new LinearLayout(ReviewCommitActivity.this);
						rbLL.setOrientation(LinearLayout.HORIZONTAL);
						rbA.addView(rbLL, rbA.getChildCount());
						if (!Util.isEmpty(item.popUp)) {
							/**
							 * 生成弹窗说明小图
							 */
							RadioButton imTv = new RadioButton(ReviewCommitActivity.this);
							imTv.setButtonDrawable(R.drawable.tip);
							rbLL.addView(imTv, rbLL.getChildCount());
						}
						RadioButton rb = new RadioButton(ReviewCommitActivity.this);
						rb.setClickable(false);
						if (-1 != item.itemValue) {
							LinearLayout otherLayout = new LinearLayout(ReviewCommitActivity.this);
							otherLayout.setOrientation(LinearLayout.HORIZONTAL);
							otherLayout.setGravity(Gravity.CENTER_VERTICAL);
							if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
								rbLL.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							} else {
								rbLL.setLayoutParams(new LinearLayout.LayoutParams(maxCWidth,
										LinearLayout.LayoutParams.WRAP_CONTENT));
							}
							rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							rb.setFocusable(false);
							// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
							rb.setButtonDrawable(R.drawable.small_radiobutton);
							Drawable drawable = getResources().getDrawable(R.drawable.small_radiobutton_on);
							int leftwidth = drawable.getIntrinsicWidth();
							rb.setTextColor(Color.BLACK);
							rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							// rb.setText(item.getItemText());
							// 更改的样式
							final float imgWidth = rowsOneWidth - leftwidth;
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
											+ File.separator + feed.getSurveyId() + File.separator + source;
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
							rb.setMaxWidth((int) rowsOneWidth * 2 / 3);
							if (null != item.padding && item.padding == 1 && 1 != item.isOther) {
								String radomed = getResources().getString(R.string.option_bottom);
								SpannableString ss = new SpannableString(radomed);
								ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(),
										Spanned.SPAN_COMPOSING);
								rb.append(ss);
							}
							// 更改的样式

							String idStr = "ohter_" + q.qIndex + "_" + i;
							rb.setId(idStr.hashCode());
							// System.out.println("radio_id="+idStr.hashCode());

							EditText et = new EditText(ReviewCommitActivity.this);
							et.setFocusableInTouchMode(false);
							et.setFocusable(false);
							et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							et.setBackgroundResource(R.drawable.bg_edittext);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//							if (1 < actualRowsNum) {
//								params.setMargins(leftwidth, 0, 0, 0);
//							}
							et.setLayoutParams(params);
							et.setMinimumWidth(125);
							// et.setId(free.itemValue);
							et.setId((idStr + "_et").hashCode());
							if (!Util.isEmpty(amList)) {
								// System.out.println("amList != null");
								String name = Util.GetAnswerName(q, item, 0, 0, false, false);
								String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
								// System.out.println("etName--->"+etName);
								for (AnswerMap am : amList) {
									// System.out.println("am="+am.getAnswerName());
									if (name.equals(am.getAnswerName().trim()) && String.valueOf(item.itemValue).trim()
											.equals(am.getAnswerValue().trim())) {
										// System.out.println("ddddddddd");
										rb.setChecked(true);
										item.isCheck = true;
										rb.setTag(item);
									}
									if (etName.equals(am.getAnswerName().trim())) {
										// System.out.println("etName=" + etName
										// +
										// ", etValue=" + am.getAnswerValue());
										et.setText(am.getAnswerValue());
										item.isCheck = true;
										et.setTag(item);
									}
								}
							}
							rbLL.addView(otherLayout, rbLL.getChildCount());
							/**
							 * 没有预选项
							 */
							otherLayout.addView(rb, otherLayout.getChildCount());
							/**
							 * 题外关联 之 选项置顶 去掉文本框 如果是置底
							 */
							if (1 == item.isOther) {
								otherLayout.addView(et, otherLayout.getChildCount());
							}
							// 将文本框加入集合中

						}
					}
					if (0 < arrLayout.size()) {
						LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
						rbM.addView(rbA, rbM.getChildCount());
					} else {
						bodyView.addView(rbA, bodyView.getChildCount());
					}
				}
			}
			break;

		case Cnt.TYPE_CHECK_BOX:// 复选框
			/**
			 * 标题最大宽度
			 */
			/**
			 * 题型的横向、纵向摆放 追加说明新布局&& 0 == q.isHaveItemCap
			 */
			bodyView.setOrientation(LinearLayout.VERTICAL);

			ArrayList<QuestionItem> checkRows = q.getRowItemArr();
			if (!Util.isEmpty(checkRows)) {
				/**
				 * 排斥
				 */
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
				if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
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
				float rowsOneWidth = (float) maxCWidth / (float) actualRowsNum;
				for (int i = 0; i < checkRows.size(); i++) {
					QuestionItem item;
					item = checkRows.get(i);

					if (1 == item.isOther) {
						otherRows.add(item);
					} else {

						// 生成一项的整体布局
						LinearLayout rbA = new LinearLayout(ReviewCommitActivity.this);
						rbA.setOrientation(LinearLayout.VERTICAL);
						if (isMultiline) {// 如果是多列则进入判断
							if (actualRowsNum == lineNum) {
								lineNum = 0;
							}
							if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
								LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
								rbM.setOrientation(LinearLayout.HORIZONTAL);
								rbM.setGravity(Gravity.CENTER_VERTICAL);
								bodyView.addView(rbM, bodyView.getChildCount());
								arrLayout.add(rbM);
							}
							lineNum++;
						}

						CheckBox cb = new CheckBox(ReviewCommitActivity.this);
						cb.setClickable(false);
						LinearLayout llcb = new LinearLayout(ReviewCommitActivity.this);
						llcb.setOrientation(LinearLayout.HORIZONTAL);
						llcb.setGravity(Gravity.CENTER_VERTICAL);
						TextView tvcb = new TextView(ReviewCommitActivity.this);
						tvcb.setBackgroundResource(R.drawable.small_textview_off_disable_focused);
						tvcb.setGravity(Gravity.CENTER);
						tvcb.setTextColor(Color.RED);
						tvcb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						llcb.addView(tvcb);
						// 复选追加说明方法
						TextView tvItemCap = new TextView(ReviewCommitActivity.this);
						if (!Util.isEmpty(item.caption)) {
							if (1 == item.caption_check) {
							} else {
								// 追加说明新布局原FILL_WRAP
								tvItemCap.setLayoutParams(
										new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.WRAP_CONTENT));
								tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
								tvItemCap.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = ReviewCommitActivity.this.getFilesDir() + File.separator
												+ "survey" + File.separator + feed.getSurveyId() + File.separator
												+ source;
										// System.out.println("name:" + name);
										drawable = Drawable.createFromPath(name);
										Bitmap image = BitmapFactory.decodeFile(name);
										if (image != null) {
											float tWidth = image.getWidth();
											float tHeight = image.getHeight();
											if (tWidth > maxCWidth) {
												tHeight = maxCWidth / tWidth * tHeight;
												tWidth = maxCWidth;
											}
											drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
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
						// 复选追加说明方法
						// CheckBox cb = new CheckBox(this);
						String idStr = q.qIndex + "_" + i;
						cb.setId(idStr.hashCode());
						tvcb.setId((idStr + "_tv").hashCode());
						// 复选百分比 原复选，现在复选
						// cb.setLayoutParams(WRAP_WRAP);
						if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
							cb.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth,
									LinearLayout.LayoutParams.WRAP_CONTENT));
						} else {
							cb.setLayoutParams(
									new LinearLayout.LayoutParams(maxCWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
						}
						// 复选百分比结束
						cb.setButtonDrawable(R.drawable.small_checkbox);
						if (1 == q.qSortChecked) {
							cb.setButtonDrawable(android.R.color.transparent);
						}
						Drawable drawable = getResources().getDrawable(R.drawable.small_checkbox_on);
						int leftwidth = drawable.getIntrinsicWidth();
						cb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						cb.setTextColor(Color.BLACK);
						// cb.setText(item.getItemText());
						final float imgWidth = rowsOneWidth - leftwidth;
						// 更改的样式
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
										+ File.separator + feed.getSurveyId() + File.separator + source;
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
						cb.setText(fromHtml);
						if (null != item.padding && item.padding == 1) {
							String radomed = getResources().getString(R.string.option_top);
							SpannableString ss = new SpannableString(radomed);
							ss.setSpan(new ForegroundColorSpan(Color.RED), 0, radomed.length(), Spanned.SPAN_COMPOSING);
							cb.append(ss);
						}
						// 更改的样式
						if (1 == q.qSortChecked) {// 复选排序
							cb.setClickable(false);
						} else {
						}
						String name = Util.GetAnswerName(q, item, 0, 0, false, false);
						/**
						 * 判断是否是哑题,然后逻辑判断。 新哑题
						 */
						// TODO
						/**
						 * 新哑题 判断哑题结束
						 */

						if (!Util.isEmpty(amList)) {
							// System.out.println("复选题以前的答案集合不为空");
							String chName = Util.GetAnswerName(q, item, 0, 0, false, false);
							// System.out.println("chName--->"+chName+",
							String tvName = Util.GetAnswerName(q, item, 0, 0, false, true);
							// value="+item.itemValue);
							for (AnswerMap am : amList) {
								// System.out.prisntln("name="+am.getAnswerName()+",
								// value="+am.getAnswerValue());
								if (chName.equals(am.getAnswerName().trim())
										&& am.getAnswerValue().trim().equals(String.valueOf(item.itemValue).trim())) {
									cb.setChecked(true);
									item.isCheck = true;
									// cb.setTag(item);
									// System.out.println("复选题以前的答案配对");
								}
								if (tvName.equals(am.getAnswerName().trim())// 排序题序号在itemvalue上保存，text保存原有item的value值
								) {

									if (0 == am.getAnswerValue().indexOf("sort")) {
										String value = am.getAnswerValue();
										tvcb.setText(value.substring(4));

										item.isCheck = true;
									}
									// System.out.println("复选题以前的答案配对");
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
										LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
										rbM.setOrientation(LinearLayout.HORIZONTAL);
										rbM.setGravity(Gravity.CENTER_VERTICAL);
										bodyView.addView(rbM, bodyView.getChildCount());
										arrLayout.add(rbM);
									}
									bodyView.addView(tvItemCap, bodyView.getChildCount() - 1);
								} else {
									bodyView.addView(tvItemCap, bodyView.getChildCount());
								}
							}
						}
						/**
						 * 单选题目选项有图片
						 */
						if (1 == q.qSortChecked) {
							cb.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth - leftwidth,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							llcb.addView(cb, llcb.getChildCount());
							rbA.addView(llcb, rbA.getChildCount());
							if (0 < arrLayout.size()) {
								LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
								rbM.addView(rbA, rbM.getChildCount());
							} else {
								bodyView.addView(rbA, bodyView.getChildCount());
							}
						} else {
							rbA.addView(cb, rbA.getChildCount());
							if (0 < arrLayout.size()) {
								LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
								rbM.addView(rbA, rbM.getChildCount());
							} else {
								bodyView.addView(rbA, bodyView.getChildCount());
							}
						}
						// 隐藏选项
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
					LinearLayout rbA = new LinearLayout(ReviewCommitActivity.this);
					rbA.setOrientation(LinearLayout.VERTICAL);
					if (isMultiline) {// 如果是多列则进入判断
						if (actualRowsNum == lineNum) {
							lineNum = 0;
						}
						if (0 == lineNum) {// 如果是每行第一列则新创建一个横向布局
							LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
							rbM.setOrientation(LinearLayout.HORIZONTAL);
							rbM.setGravity(Gravity.CENTER_VERTICAL);
							bodyView.addView(rbM, bodyView.getChildCount());
							arrLayout.add(rbM);
						}
						lineNum++;
					}
					QuestionItem item = otherRows.get(i);
					CheckBox cb = new CheckBox(ReviewCommitActivity.this);
					cb.setClickable(false);
					LinearLayout llcb = new LinearLayout(ReviewCommitActivity.this);
					llcb.setOrientation(LinearLayout.HORIZONTAL);
					llcb.setGravity(Gravity.CENTER_VERTICAL);
					TextView tvcb = new TextView(ReviewCommitActivity.this);
					tvcb.setBackgroundResource(R.drawable.small_textview_off_disable_focused);
					tvcb.setGravity(Gravity.CENTER);
					tvcb.setTextColor(Color.RED);
					tvcb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					llcb.addView(tvcb);
					tvcb.setTag(item);
					cb.setTag(item);
					Drawable drawable = getResources().getDrawable(R.drawable.small_checkbox_on);
					int leftwidth = drawable.getIntrinsicWidth();
					if (-1 != item.itemValue) {// 并且不是<freeInput/>这种空标签
						LinearLayout otherLayout = new LinearLayout(ReviewCommitActivity.this);
						otherLayout.setOrientation(LinearLayout.HORIZONTAL);
						otherLayout.setGravity(Gravity.CENTER_VERTICAL);
						cb.setLayoutParams(WRAP_WRAP);
						if (1 < q.rowsNum && Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
							otherLayout.setLayoutParams(new LinearLayout.LayoutParams((int) rowsOneWidth,
									LinearLayout.LayoutParams.WRAP_CONTENT));
						} else {
							otherLayout.setLayoutParams(
									new LinearLayout.LayoutParams(maxCWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
						}
						rbA.addView(otherLayout, rbA.getChildCount());

						String idStr = "ohter_" + q.qIndex + "_" + i;
						cb.setId(idStr.hashCode());
						tvcb.setId((idStr + "_tv").hashCode());
						cb.setButtonDrawable(R.drawable.small_checkbox);
						if (1 == q.qSortChecked) {
							cb.setButtonDrawable(android.R.color.transparent);
						}
						cb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						cb.setTextColor(Color.BLACK);
						final float imgWidth = rowsOneWidth - leftwidth;
						ImageGetter imgGetter = new Html.ImageGetter() {
							public Drawable getDrawable(String source) {
								Drawable drawable = null;
								String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
										+ File.separator + feed.getSurveyId() + File.separator + source;
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
						cb.setText(fromHtml);
						cb.setMaxWidth((int) rowsOneWidth * 2 / 3);
						if (1 == q.qSortChecked) {
							llcb.addView(cb, llcb.getChildCount());
							otherLayout.addView(llcb, otherLayout.getChildCount());
						} else {
							otherLayout.addView(cb, otherLayout.getChildCount());
						}
						EditText et = new EditText(ReviewCommitActivity.this);
						et.setFocusableInTouchMode(false);
						et.setFocusable(false);
						et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						et.setBackgroundResource(R.drawable.bg_edittext);
						et.setTag(item);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
//						if (1 < actualRowsNum) {
//							params.setMargins(leftwidth, 0, 0, 0);
//						}
						et.setLayoutParams(params);
						et.setMinimumWidth(125);
						if (!Util.isEmpty(amList)) {
							// System.out.println("复选题以前的答案集合不为空");
							String chName = Util.GetAnswerName(q, item, 0, 0, false, false);
							// System.out.println("chName--->"+chName+",
							// value="+item.itemValue);
							String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
							String tvName = Util.GetAnswerName(q, item, 0, 0, false, true);
							// System.out.println("etName:"+etName);
							for (AnswerMap am : amList) {
								// System.out.println("name="+am.getAnswerName()+",
								// value="+am.getAnswerValue());
								if (chName.equals(am.getAnswerName().trim())
										&& am.getAnswerValue().trim().equals(String.valueOf(item.itemValue))) {
									cb.setChecked(true);
									item.isCheck = true;
									cb.setTag(item);
									// System.out.println("复选题以前的答案配对");
								}
								// System.out.println("复选框其他项etName=" + etName +
								// ", name=" + am.getAnswerName() + ", value=" +
								// am.getAnswerValue());
								// System.out.println("am.getAnswerName():"+am.getAnswerName());
								if (etName.equals(am.getAnswerName().trim())) {
									item.isCheck = true;
									// System.out.println("复选am.getAnswerValue():"+am.getAnswerValue());
									et.setText(am.getAnswerValue());
									// et.setTag(item);
								}
								if (tvName.equals(am.getAnswerName().trim())) {
									if (0 == am.getAnswerValue().indexOf("sort")) {
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
						/**
						 * 题外关联 之 选项置顶 添加其他项 的操作 选项置底 隐藏 文本框
						 */
						if (1 == item.isOther) {
							otherLayout.addView(et, otherLayout.getChildCount());
						}
						if (0 < arrLayout.size()) {
							LinearLayout rbM = arrLayout.get(arrLayout.size() - 1);
							rbM.addView(rbA, rbM.getChildCount());
						} else {
							bodyView.addView(rbA, bodyView.getChildCount());
						}
						tvcb.setTag(item);
						cb.setTag(item);
						// 配置型隐藏选项
					}
				}
			}
			break;

		case Cnt.TYPE_MATRIX_RADIO_BUTTON:// 矩阵单选
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			// if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
			// System.out.println("横向");
			// bodyView.setOrientation(LinearLayout.HORIZONTAL);
			// } else {
			// System.out.println("纵向");
			bodyView.setOrientation(LinearLayout.VERTICAL);
			// }

			/**
			 * 获取行标题
			 */
			ArrayList<QuestionItem> rRows = q.getRowItemArr();
			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> rColmns = q.getColItemArr();

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean isBeyond = ((maxCWidth * 2 / 3 - 10)) <= rColmns.size() * 100;

			// 处理非随机矩阵
			/**
			 * 遍历每一行
			 */
			// 新加的
			int rightNum = (q.isRight == 1) ? 1 : 0;

			// 处理非随机矩阵
			/**
			 * 遍历每一行
			 */
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
				RadioGroup rg = new RadioGroup(ReviewCommitActivity.this);// 每一列的
				rg.setVerticalGravity(Gravity.CENTER_VERTICAL);
				rg.setBackgroundColor(Color.TRANSPARENT);
				if (q.qStarCheck == 1) {
					rg.setBackgroundResource(R.drawable.piont_lv);
				}
				RadioGroup.LayoutParams rgPar = new RadioGroup.LayoutParams((maxCWidth * 2 / 3 - 10),
						LayoutParams.WRAP_CONTENT);
				rgPar.setMargins(0, 0, 0, 0);
				rg.setLayoutParams(rgPar);
				rg.setPadding(0, 0, 0, 0);
				rg.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout ll = new LinearLayout(ReviewCommitActivity.this);
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

					TextView tvTb = new TextView(ReviewCommitActivity.this);
					tvTb.setLayoutParams(WRAP_WRAP);
					tvTb.setGravity(Gravity.FILL);
					tvTb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					tvTb.setWidth(100);
					tvTb.setPadding(5, 2, 2, 2);
					if (0 == r) {// 如过是第一行, 则打印出每一列的值
						// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
						ll.setBackgroundColor(Color.LTGRAY);// Color.LTGRAY
						if (0 == c) {// 打印表头
							tvTb.setText("");
							tvTb.setPadding(2, 2, 2, 2);
							// 矩阵右侧
							if (q.isRight == 1) {
								tvTb.setWidth(maxCWidth / 6);
							} else {
								tvTb.setWidth(maxCWidth / 3);
							}

						}
						// 矩阵右侧 新加的
						else if (rColmns.size() + rightNum == c && q.isRight == 1) {
							tvTb.setText("");
							tvTb.setPadding(2, 2, 2, 2);
							tvTb.setWidth(maxCWidth / 6);
						} else {// c !=0 打印每列的,即列标题
								// System.out.println("c_colmns_item_value=" +
								// rColmns.get(c - 1).itemValue + ", colmns=" +
								// (c -
								// 1));
							tvTb.setPadding(5, 2, 2, 2);
							tvTb.setTextColor(Color.BLACK);
							tvTb.setGravity(Gravity.FILL);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								tvTb.setWidth((maxCWidth * 2 / 3 - 10) / rColmns.size());
							} else {
								tvTb.setWidth((maxCWidth * 2 / 3 - 10) / rColmns.size());
							}
							System.out.println("c:" + c);
							System.out.println("colItem.itemText:" + colItem.itemText);
							String t = colItem.itemText;

							// ***********************************样式处理**************************//
							final float Width = (maxCWidth * 2 / 3 - 10) / rColmns.size();
							// 更改的样式
							ImageGetter imgGetter = new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
											+ File.separator + feed.getSurveyId() + File.separator + source;
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
										drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
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
						ll.addView(tvTb, ll.getChildCount());
					} else {
						if (r % 2 == 0) {
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
							tvTb.setPadding(2, 2, 2, 2);
							// 矩阵右侧
							float imgMaxWidth = maxCWidth / 3;
							tvTb.setWidth(maxCWidth / 3);
							if (1 == q.isRight) {
								tvTb.setWidth(maxCWidth / 6);
								imgMaxWidth = maxCWidth / 6;
							}

							// tvTb.setBackgroundColor(Color.TRANSPARENT);
							String t = rowItem.itemText;

							/**
							 * 题外关联 之 选项置顶 单选矩阵 的提示语的 显示 出来
							 */
							if (null != rowItem.padding) {
								if (rowItem.padding == 1) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_top)
											+ "</font>";
								} else if (rowItem.padding == 2) {
									t = rowItem.itemText + "<font color=red>  " + this.getString(R.string.option_bottom)
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
							// ***********************************样式处理**************************//
							if (!Util.isEmpty(t)) {
								final float Width = (imgMaxWidth - 5);
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = ReviewCommitActivity.this.getFilesDir() + File.separator
												+ "survey" + File.separator + feed.getSurveyId() + File.separator
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
											drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(t, imgGetter, null);
								tvTb.setText(fromHtml);
							}
							ll.addView(tvTb, ll.getChildCount());
							// ***********************************样式处理**************************//
							// if (1 == q.qStarCheck) {
							// TextView tvLine = new
							// TextView(ReviewCommitActivity.this);
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
							tvTb.setPadding(2, 2, 2, 2);
							tvTb.setBackgroundResource(R.drawable.small_text_background);
							tvTb.setTextColor(Color.BLACK);
							// 矩阵右侧
							float imgMaxWidth = maxCWidth / 6;
							tvTb.setWidth(maxCWidth / 6);

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
							// ***********************************样式处理**************************//
							if (!Util.isEmpty(t)) {
								final float Width = imgMaxWidth - 5;
								// 更改的样式
								ImageGetter imgGetter = new Html.ImageGetter() {
									public Drawable getDrawable(String source) {
										Drawable drawable = null;
										String name = ReviewCommitActivity.this.getFilesDir() + File.separator
												+ "survey" + File.separator + feed.getSurveyId() + File.separator
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
											drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
											return drawable;
										} else {
											return null;
										}
									}
								};
								Spanned fromHtml = Html.fromHtml(t, imgGetter, null);
								tvTb.setText(fromHtml);
							}
							ll.addView(tvTb, ll.getChildCount());
						} else {// 打印单选按钮
							if (1 == c) {
								ll.addView(rg);
							}
							// QuestionItem item = rRows.get(r - 1);
							if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
								// 空其他项,即只有一个<freeInput/>标签
								continue;
							}
							// System.out.println("r_row_item_value=" +
							// rowItem.itemValue + ", r=" + (r - 1));
							RadioButton radio = new RadioButton(ReviewCommitActivity.this);
							radio.setClickable(false);
							radio.setLayoutParams(WRAP_WRAP);
							radio.setGravity(Gravity.FILL | Gravity.CENTER_VERTICAL);
							// radio.setGravity(Gravity.CENTER);

							AnswerMap am = new AnswerMap();
							String name = Util.GetAnswerName(q, rowItem, rowItem.itemValue, 0, false, false);
							am.setAnswerName(name);
							/**
							 * 相对的
							 */
							am.setRow(rowItem.itemValue);
							/**
							 * 绝对的
							 */
							am.setCol(colItem.itemValue);
							am.setAnswerValue(String.valueOf(colItem.itemValue));
							radio.setTag(am);
							// radio.setTag(key, tag)
							rg.addView(radio, rg.getChildCount());
							if (!Util.isEmpty(amList)) {
								for (AnswerMap tam : amList) {
									if (name.equals(tam.getAnswerName())
											&& am.getAnswerValue().equals(tam.getAnswerValue())) {
										// System.out.println("匹配--->name="+name+",
										// value="+am.getAnswerValue());
										radio.setChecked(true);
									}
								}
							}

							radio.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							radio.setTextColor(Color.BLACK);
							// radio.setButtonDrawable(R.drawable.small_radiobutton_temp);
							radio.setButtonDrawable(R.drawable.small_radiobutton);
							radio.setBackgroundResource(R.drawable.small_radiobutton_background);
							if (q.qStarCheck != 0) {
								int drawable = R.drawable.small_radiobutton;
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable),
										null, null, null);
								radio.setButtonDrawable(android.R.color.transparent);
								radio.setBackgroundResource(R.drawable.small_text_background);
								if (r % 2 == 0) {
									radio.setBackgroundColor(Color.WHITE);
								} else {
									radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
								}
								switch (q.qStarCheck) {
								case 1:
									drawable = android.R.color.transparent;
									TextView tvLine = new TextView(ReviewCommitActivity.this);
									if (r % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										radio.setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										radio.setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
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
								radio.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable),
										null, null, null);
								rowViews.add(radio);
							}
							radio.setWidth(100);
							if (isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
								// 矩阵百分比
								radio.setLayoutParams(
										new LinearLayout.LayoutParams((maxCWidth * 2 / 3 - 10) / rColmns.size(),
												LinearLayout.LayoutParams.WRAP_CONTENT));
								radio.setWidth((maxCWidth * 2 / 3 - 10) / rColmns.size());
							} else {
								// System.out.println("设置了Radio的宽度");
								radio.setWidth((maxCWidth * 2 / 3 - 10) / rColmns.size());
							}
							if ("true".equals(colItem.exclude) && !Util.isEmpty(colItem.excludeIn)) {
								String excludeIn = colItem.excludeIn;
								String[] excludeValue = excludeIn.split(",");
								if (excludeValue.length > 0) {
									for (int i = 0; i < excludeValue.length; i++) {
										if (Integer.parseInt(excludeValue[i].trim()) == (rowItem.getItemValue())) {
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
									TextView tvLine = new TextView(ReviewCommitActivity.this);
									if (r % 2 == 0) {
										tvLine.setBackgroundColor(Color.parseColor("#F0F0F0"));
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.WHITE);
									} else {
										tvLine.setBackgroundColor(Color.WHITE);
										((RadioButton) rowViews.get(j)).setBackgroundColor(Color.parseColor("#F0F0F0"));
									}
									tvLine.setLayoutParams(new LayoutParams(3, LayoutParams.MATCH_PARENT));
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
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
							for (int j = 0; j < i + 1; j++) {
								int drawable = R.drawable.small_radiobutton;
								switch (q.qStarCheck) {
								case 1:
									((RadioButton) rowViews.get(j)).setBackgroundResource(R.color.transparent);
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
								((RadioButton) rowViews.get(j)).setCompoundDrawablesWithIntrinsicBounds(
										getResources().getDrawable(drawable), null, null, null);
							}
						}
					}
				}
				bodyView.addView(ll, bodyView.getChildCount());
			}
			break;

		case Cnt.TYPE_MATRIX_CHECK_BOX:// 矩阵复选
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}
			/**
			 * 获取行标题
			 */
			ArrayList<QuestionItem> cRows = q.getRowItemArr();
			/**
			 * 获取列标题
			 */
			ArrayList<QuestionItem> cColmns = q.getColItemArr();

			/**
			 * 假如题干的宽度大于或等于屏幕宽度的3/4
			 */
			boolean _isBeyond = ((screenWidth * 2 / 3 - 20)) <= cColmns.size() * 100;

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
				RadioGroup ll = new RadioGroup(this);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				for (int c = 0; c < cColmns.size() + 1; c++) {
					QuestionItem colItem = null;

					if (0 != c) {
						colItem = cColmns.get(c - 1);
					}

					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(FILL_WRAP);
					TextView tvTb = new TextView(this);
					tvTb.setLayoutParams(WRAP_WRAP);
					tvTb.setGravity(Gravity.FILL);
					tvTb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					tvTb.setWidth(100);
					tvTb.setPadding(2, 2, 2, 2);
					// tvTb.setHeight(100);
					// tvTb.setBackgroundColor(Color.LTGRAY);

					if (0 == r) {// 如过是第一行, 则打印出每一列的值
						// tvTb.setBackgroundResource(R.drawable.tb_item_bg);
						ll.setBackgroundColor(Color.LTGRAY);
						if (0 == c) {// 打印表头
							tvTb.setText(" ");
							tvTb.setWidth(dis.getWidth() / 3);
						} else {// 打印每列的
							tvTb.setTextColor(Color.BLACK);
							if (_isBeyond) {
								/**
								 * 所有单选按钮的宽度之和超出屏幕宽度的3/4
								 */
							} else {
								tvTb.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
							}
							String t = colItem.itemText;

							// ***********************************样式处理**************************//
							CstmMatcher cm = Util.findFontMatcherList(t);
							if (!Util.isEmpty(cm.getMis())) {
								t = cm.getResultStr();
								SpannableString ss = new SpannableString(t);
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length())
										ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
								}
								tvTb.setText(ss);
								//
							} else {
								CstmMatcher bCm = Util.findBoldMatcherList(t);
								if (!Util.isEmpty(bCm.getMis())) {
									t = bCm.getResultStr();
									SpannableString ss = new SpannableString(t);
									for (MatcherItem mi : bCm.getMis()) {
										if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= t.length()) {
											ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
											ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
									}
									tvTb.setText(ss);
								} else {
									tvTb.setText(t);
								}

							}
						}
						// ***********************************样式处理**************************//
						ll.addView(tvTb, ll.getChildCount());
					} else {
						if (r % 2 == 0)
							ll.setBackgroundColor(Color.parseColor("#10000050"));
						else
							ll.setBackgroundColor(Color.TRANSPARENT);
						if (0 == c) {
							tvTb.setTextColor(Color.BLACK);
							tvTb.setWidth(dis.getWidth() / 3);
							tvTb.setBackgroundColor(Color.TRANSPARENT);
							// tvTb.setText(cRows.get(r - 1).itemText);
							// tvTb.setText(rowItem.itemText);
							String t = rowItem.itemText;
							// ***********************************样式处理**************************//

							if (!Util.isEmpty(t)) {
								CstmMatcher cm = Util.findFontMatcherList(t);
								if (!Util.isEmpty(cm.getMis())) {
									t = cm.getResultStr();
									if (Util.isEmpty(t)) {
										SpannableString ss = new SpannableString(t);
										for (MatcherItem mi : cm.getMis()) {
											ss.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
													Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
										}
										CstmMatcher bCm = Util.findBoldMatcherList(t);
										if (!Util.isEmpty(bCm.getMis())) {
											for (MatcherItem mi : bCm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end
														&& mi.end <= t.length()) {
													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
															Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end,
															Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												}
											}
										}
										tvTb.setText(ss);
									} else {
										tvTb.setText(rowItem.itemText);
									}
									//
								} else {
									CstmMatcher bCm = Util.findBoldMatcherList(t);
									if (!Util.isEmpty(bCm.getMis())) {
										t = bCm.getResultStr();
										if (!Util.isEmpty(t)) {
											SpannableString ss = new SpannableString(t);
											for (MatcherItem mi : bCm.getMis()) {
												if (null != mi && -1 != mi.start && -1 != mi.end
														&& mi.end <= t.length()) {
													ss.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
															Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
													ss.setSpan(new RelativeSizeSpan(1.3f), mi.start, mi.end,
															Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
												}
											}
											tvTb.setText(ss);
										} else {
											tvTb.setText(rowItem.itemText);
										}
									} else {
										tvTb.setText(t);
									}

								}
							}
							// ***********************************样式处理**************************//
							ll.addView(tvTb, ll.getChildCount());
						} else {// 打印单选按钮
							// QuestionItem rowItem = cRows.get(r - 1);
							if (null == rowItem || (1 == rowItem.isOther && -1 == rowItem.itemValue)) {
								// 空其他项不打印CheckBox
								continue;
							}

							// QuestionItem colItem = cColmns.get(c - 1);

							CheckBox check = new CheckBox(this);
							check.setClickable(false);
							// System.out.println("(r - 1)=" + (r - 1) +
							// ", (c - 1)=" + (c - 1));
							System.out.println("rowItem.itemValue=" + rowItem.itemValue + ", colItem.itemValue="
									+ colItem.itemValue);
							String name = Util.GetAnswerName(q, null, rowItem.itemValue, colItem.itemValue, false,
									false);
							int value = q.getColItemArr().size() * rowItem.itemValue + colItem.itemValue;
							AnswerMap am = new AnswerMap();
							am.setAnswerName(name);
							am.setAnswerValue(String.valueOf(value));
							check.setTag(am);
							ll.addView(check, ll.getChildCount());
							if (!Util.isEmpty(amList)) {
								for (AnswerMap tam : amList) {
									if (name.endsWith(tam.getAnswerName())
											&& am.getAnswerValue().equals(tam.getAnswerValue())) {
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
								check.setWidth((screenWidth * 2 / 3 - 20) / cColmns.size());
							}
							// check.setHeight(100);
						}
					}

				}
				bodyView.addView(ll, bodyView.getChildCount());
			}
			break;

		case Cnt.TYPE_DROP_DOWN_LIST:// 下来列表
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}

			Spinner sp = new Spinner(ReviewCommitActivity.this);
			sp.setClickable(false);
			// LayoutParams dropLp = new LayoutParams(-1, -2);
			// dropLp.addRule(RelativeLayout.BELOW, q.qTitle.hashCode());
			sp.setLayoutParams(FILL_WRAP);
			ArrayList<QuestionItem> columns = q.getColItemArr();
			HashMap<String, Integer> tvMap = new HashMap<String, Integer>();
			HashMap<Integer, Integer> ivMap = new HashMap<Integer, Integer>();
			if (!Util.isEmpty(columns)) {
				ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
						R.layout.simple_spinner_adapter);
				aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
				for (int i = 0; i < columns.size(); i++) {
					QuestionItem item = columns.get(i);
					aa.add(item.itemText);
					tvMap.put(i + "_" + item.itemText, item.itemValue);
					ivMap.put(item.itemValue, i);
				}
				sp.setAdapter(aa);
				// Answer an = tmpAnswerList.get(q.qIndex);
				// ArrayList<AnswerMap> amList = an.getAnswerMapArr();
				if (!Util.isEmpty(amList)) {
					String name = Util.GetAnswerName(q, null, 0, 0, false, false);
					for (AnswerMap am : amList) {
						if (name.equals(am.getAnswerName())) {
							int value = Integer.parseInt(am.getAnswerValue());
							int pos = ivMap.get(value);
							sp.setSelection(pos);
						}
					}
				}
			}
			bodyView.addView(sp, bodyView.getChildCount());
			tvMap.clear();
			tvMap = null;
			ivMap.clear();
			ivMap = null;
			break;

		case Cnt.TYPE_FREE_TEXT_BOX:// 单行文本框
			isNew = true;
			boolean isInner = false;
			ArrayList<QuestionItem> tbColumns = q.getColItemArr();
			for (int i = 0; i < tbColumns.size(); i++) {
				QuestionItem questionItem = tbColumns.get(i);
				if (questionItem.type == -1) {
					isNew = false;
					break;
				}
			}

			if (isNew) {
				bodyView.setOrientation(LinearLayout.VERTICAL);
				// 每个item值
				ArrayList<LinearLayout> colsLL = new ArrayList<LinearLayout>();
				// 单行文本框
				if (1 == q.freeTextColumn) {
					if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber) && 1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat,
								q.freeSymbol + q.freeSumNumber));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else if (1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_no_repeat));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(
								getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else if (!Util.isEmpty(q.qParentAssociatedCheck)) {
						String value = q.qParentAssociatedCheck;
						String[] ss = value.split(",");
						if (ss[0].equals("2")) {

							TextView tvSyb = new TextView(ReviewCommitActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
							/**
							 * 在这里 加一个 题外关联 字段的 判断 ： 然后 给用户提示 给出提示语句 ！
							 */
							String relevanceAnswer = ComUtil.getRelevanceAnswer(q, ma, feed);
							tvSyb.setText(getResources().getString(R.string.question_outying_right, relevanceAnswer));
							bodyView.addView(tvSyb, bodyView.getChildCount());
						}
						if (ss[0].equals("1")) {
							TextView tvSyb = new TextView(ReviewCommitActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！ 大树 显示关联 26
							 */
							String parentQid = ma.dbService.getQuestion(feed.getSurveyId(), ss[1]).qid;
							tvSyb.setText(getResources().getString(R.string.question_outying_display_right,
									parentQid + "", (Integer.valueOf(ss[2]) + 1)) + "");
							bodyView.addView(tvSyb, bodyView.getChildCount());
						}
					} else {
						/**
						 * 题外关联 之 内部关联 提示语 大树 内部关联 3
						 */
						for (QuestionItem item : q.getColItemArr()) {
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
								TextView tvSyb = new TextView(ReviewCommitActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText(this.getString(R.string.inner_refrense));
								bodyView.addView(tvSyb, bodyView.getChildCount());
								break;
							}
						}
					}
				}
				int tempQWidth = (dis.getWidth()) / q.freeTextColumn;
				// 每行多个文本题 ，弄两个垂直布局
				if (1 < q.freeTextColumn) {

					LinearLayout lvTitle = new LinearLayout(ReviewCommitActivity.this);
					lvTitle.setOrientation(LinearLayout.HORIZONTAL);
					lvTitle.setLayoutParams(FILL_WRAP);
					if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber) && 1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_num_sum_no_repeat,
								q.freeSymbol + q.freeSumNumber));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					} else if (1 == q.freeNoRepeat) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_no_repeat));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					} else if (!Util.isEmpty(q.freeSymbol) && !Util.isEmpty(q.freeSumNumber)) {
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(
								getResources().getString(R.string.question_num_sum, q.freeSymbol + q.freeSumNumber));
						lvTitle.addView(tvSyb, lvTitle.getChildCount());
					} else if (!Util.isEmpty(q.qParentAssociatedCheck)) {
						String value = q.qParentAssociatedCheck;
						String[] ss = value.split(",");
						if (ss[0].equals("2")) {

							TextView tvSyb = new TextView(ReviewCommitActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
							/**
							 * 在这里 加一个 题外关联 字段的 判断 ： 然后 给用户提示 给出提示语句 ！
							 */
							String relevanceAnswer = ComUtil.getRelevanceAnswer(q, ma, feed);
							tvSyb.setText(getResources().getString(R.string.question_outying_right, relevanceAnswer));
							bodyView.addView(tvSyb, bodyView.getChildCount());
						}
						if (ss[0].equals("1")) {
							TextView tvSyb = new TextView(ReviewCommitActivity.this);
							tvSyb.setLayoutParams(WRAP_WRAP);
							tvSyb.setTextColor(Color.RED);
							tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

							/**
							 * 在这里加一个提示 题外关联 显示的 提示 语 ！ 大树 显示关联 27
							 */
							String parentQid = ma.dbService.getQuestion(feed.getSurveyId(), ss[1]).qid;
							tvSyb.setText(getResources().getString(R.string.question_outying_display_right,
									parentQid + "", (Integer.valueOf(ss[2]) + 1)) + "");
							bodyView.addView(tvSyb, bodyView.getChildCount());
						}
					} else {
						/**
						 * 题外关联 之 内部关联 提示语 大树 内部关联 4
						 */
						for (QuestionItem item : q.getColItemArr()) {
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {

								isInner = true;
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
								TextView tvSyb = new TextView(ReviewCommitActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText(this.getString(R.string.inner_refrense));
								bodyView.addView(tvSyb, bodyView.getChildCount());
								break;
							}
						}
					}
					bodyView.addView(lvTitle, bodyView.getChildCount());
					// 设置外层布局
					LinearLayout lvBodyView = new LinearLayout(ReviewCommitActivity.this);
					lvBodyView.setOrientation(LinearLayout.HORIZONTAL);
					lvBodyView.setLayoutParams(new LayoutParams(dis.getWidth(), LayoutParams.WRAP_CONTENT));
					// lvBodyView.setBackgroundColor(Color.RED);
					// 遍历每行的列数。
					for (int i = 0; i < q.freeTextColumn; i++) {
						LinearLayout ll = new LinearLayout(ReviewCommitActivity.this);
						ll.setOrientation(LinearLayout.VERTICAL);
						ll.setPadding(0, 0, 0, 0);
						// 都给1/3
						// ll.setLayoutParams(new LayoutParams(WRAP_WRAP));

						/**
						 * 题外关联 之内部关联 之 判断 字段 宽度进行 设置 对于 成 列的状态 大树 内部关联 5
						 */
						if (isInner) {
							ll.setLayoutParams(new LayoutParams(tempQWidth + tempQWidth / (q.freeTextColumn + 1),
									LayoutParams.WRAP_CONTENT));
						} else {
							QuestionItem itemNull = tbColumns.get(0);
							String leftsideWord = itemNull.getLeftsideWord();
							if (!Util.isEmpty(leftsideWord)) {
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
								if (i == 0) {
									ll.setLayoutParams(new LayoutParams(tempQWidth * 2, LayoutParams.WRAP_CONTENT));
								} else {
									ll.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
								}
							} else {
								ll.setLayoutParams(new LayoutParams(tempQWidth, LayoutParams.WRAP_CONTENT));
							}
						}

						// ll.setBackgroundColor(Color.BLUE);

						colsLL.add(ll);
						// 给横屏的bodyView加上
						lvBodyView.addView(ll, lvBodyView.getChildCount());
					}
					bodyView.addView(lvBodyView, bodyView.getChildCount());
				}
				/**
				 * 题外关联 之 显示关联 的处理 在这里做一下处理 ： 判断 --获取答案---比对---显示 大树 显示关联 1
				 */
				tbColumns = ComUtil.getOutLyingRelevanceDisplayItems(q, feed, ma, tbColumns);

				// 表格布局
				// 总布局宽度
				ArrayList<LinearLayout> colsLLy = new ArrayList<LinearLayout>();
				LinearLayout tl = new LinearLayout(ReviewCommitActivity.this);
				tl.setLayoutParams(WRAP_WRAP);
				bodyView.addView(tl, bodyView.getChildCount());
				tl.setOrientation(LinearLayout.VERTICAL);
				int tbRows = tbColumns.size() / q.freeTextColumn;
				if (0 != tbColumns.size() % q.freeTextColumn) {
					tbRows += 1;
				}
				for (int i = 0; i < tbRows; i++) {
					LinearLayout tbr = new LinearLayout(ReviewCommitActivity.this);// 每行的TableRow对象
					tbr.setOrientation(LinearLayout.HORIZONTAL);
					tbr.setGravity(Gravity.CENTER_VERTICAL);
					for (int col = 0; col < q.freeTextColumn; col++) {
						ArrayList<LinearLayout> itemLary = new ArrayList<LinearLayout>();// 每一个选项的整体
						LinearLayout itemll = new LinearLayout(ReviewCommitActivity.this);// 每一列的整体布局
						LinearLayout lLeft = new LinearLayout(ReviewCommitActivity.this);
						LinearLayout lRight = new LinearLayout(ReviewCommitActivity.this);
						itemll.setOrientation(LinearLayout.HORIZONTAL);
						itemll.setGravity(Gravity.CENTER_VERTICAL);
						itemll.setLayoutParams(
								new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
						itemll.setBackgroundColor(getResources().getColor(R.color.gray_bold));
						lLeft.setOrientation(LinearLayout.HORIZONTAL);
						lLeft.setGravity(Gravity.CENTER_VERTICAL);
						lLeft.setPadding(1, 1, 1, 1);
						lRight.setOrientation(LinearLayout.HORIZONTAL);
						lRight.setGravity(Gravity.CENTER_VERTICAL);
						lRight.setPadding(1, 1, 1, 1);
						colsLLy.add(itemll);
						itemll.addView(lLeft, itemll.getChildCount());
						itemll.addView(lRight, itemll.getChildCount());
						tbr.addView(itemll, tbr.getChildCount());
					}
					tl.addView(tbr, tl.getChildCount());
				}
				tempQWidth -= 20;
				// 1.2一般的方法
				// 遍历每一个项的值。
				// for_1
				ArrayList<Integer> maxSumWidArr = new ArrayList<Integer>();
				double fx = 1;
				/**
				 * 循环取左右侧说明框的宽度
				 */
				ArrayList<Integer> leftWidAr = new ArrayList<Integer>();
				ArrayList<Integer> leftRequiredWidAr = new ArrayList<Integer>();
				ArrayList<Integer> editWidAr = new ArrayList<Integer>();
				ArrayList<Integer> rightWidAr = new ArrayList<Integer>();
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					SpannableString ssLeft = null;
					if (!Util.isEmpty(item.leftsideWord)) {
						String strTilte = item.leftsideWord;

						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {

							ssLeft = new SpannableString(strTilte);
							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					SpannableString ssRight = null;
					if (!Util.isEmpty(item.rightsideWord)) {
						String strTilte = item.rightsideWord;
						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {
							ssRight = new SpannableString(strTilte);

							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					int lenRight = 0;
					if (!Util.isEmpty(item.rightsideWord)) {
						TextView tvRight = new TextView(ReviewCommitActivity.this);

						/**
						 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}
						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 0);
						TextPaint paintRight = tvRight.getPaint();
						lenRight = (int) paintRight.measureText(item.rightsideWord);
						int maxRight = (int) (screenWidth / (q.freeTextColumn + 4) * fx);
						int minRight = (int) paintRight.measureText("俩字");
						if (lenRight > maxRight) {
							lenRight = maxRight;
						} else if (lenRight < minRight) {
							lenRight = minRight;
						}
					}
					rightWidAr.add(lenRight);
					int leftText = 0;
					if (!Util.isEmpty(item.leftsideWord) && 1 != q.qLinkage) {
						TextView tvLeft = new TextView(ReviewCommitActivity.this);

						/**
						 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
						 */
						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);
						}
						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(ssLeft);
						tvLeft.setPadding(0, 0, 0, 0);
						TextPaint paintLeft = tvLeft.getPaint();
						String textLeft = item.leftsideWord;
						if (-1 != textLeft.indexOf("%%")) {
							textLeft = textLeft.substring(0, textLeft.indexOf("%%"));
						}
						if (-1 != textLeft.indexOf("@@")) {
							textLeft = textLeft.substring(0, textLeft.indexOf("@@"));
						}
						leftText = (int) paintLeft.measureText(textLeft);
						int maxLeft = (int) (screenWidth / (q.freeTextColumn + 2) * fx);
						int minLeft = (int) paintLeft.measureText("俩字");
						if (leftText > maxLeft) {
							leftText = maxLeft;
						} else if (leftText < minLeft) {
							leftText = minLeft;
						}
					}
					leftWidAr.add(leftText);
					int tvRequiredWid = 0;
					if (item.required) {
						TextView tvRequired = new TextView(ReviewCommitActivity.this);
						tvRequired.setText(getResources().getString(R.string.notice_required));
						tvRequired.setTextColor(Color.RED);
						tvRequiredWid = (int) tvRequired.getPaint()
								.measureText(getResources().getString(R.string.notice_required));
					}
					leftRequiredWidAr.add(tvRequiredWid);

					// 初始化每一项的edittext

					if (item.itemSize != 0) {
						int editWidth = (int) (Util.getEditWidth(item.itemSize, maxCWidth) * fx);
						if (item.dragChecked && 2 == item.type) {
							editWidth = (int) ((screenWidth / q.freeTextColumn) * 2 / 3 * fx);
						}
						editWidAr.add(editWidth);
					} else {
						int editWidth = Util.getEditWidth(30, maxCWidth);
						editWidAr.add(editWidth);
					}

				}
				// 每一列edit的最大值
				ArrayList<Integer> editWidCo = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					editWidCo.add(0);
				}
				for (int i = 0; i < editWidAr.size(); i++) {
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							if (editWidCo.get(j) < editWidAr.get(i) - 30) {
								editWidCo.set(j, editWidAr.get(i) - 30);
							}
						}
					}
				}
				// 每一列左边文本的最大宽度
				ArrayList<Integer> leftWidCo = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					leftWidCo.add(0);
				}
				for (int i = 0; i < leftWidAr.size(); i++) {
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							if (leftWidCo.get(j) < leftWidAr.get(i) - 30) {
								leftWidCo.set(j, leftWidAr.get(i) - 30);
							}
						}
					}
				}
				// 每一列左边必答标记最大宽度
				ArrayList<Integer> leftRequiredWidCo = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					leftRequiredWidCo.add(0);
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
				// 每一列右边的最大宽度
				ArrayList<Integer> rightWidCo = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					rightWidCo.add(0);
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
				ArrayList<Integer> leftAllWi = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					leftAllWi.add(leftWidCo.get(j) + 30 + leftRequiredWidCo.get(j));
				}
				// 每一列右侧最大总宽度
				ArrayList<Integer> rightAllWi = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					rightAllWi.add(rightWidCo.get(j) + 30 + editWidCo.get(j));
				}
				// 遍历第一行
				for (int c = 1; c <= (tbColumns.size() / (q.freeTextColumn)); c++) {
					int sumWidth = 0;
					for (int h = 1; h <= q.freeTextColumn; h++) {
						int i = h * c - 1;
						QuestionItem item = tbColumns.get(i);
						int rightWid = 0;
						rightWid = rightAllWi.get(h - 1);
						sumWidth += leftAllWi.get(h - 1) + rightWid;
					}
					maxSumWidArr.add(sumWidth);
				}
				int maxSumWid = 0;
				for (int i = 0; i < maxSumWidArr.size(); i++) {
					if (maxSumWid < maxSumWidArr.get(i)) {
						maxSumWid = maxSumWidArr.get(i);
					}
				}
				// 追加说明%%处理
				// 改的地方
				if (!Util.isEmpty(q.qCaption)) {
					// tvRequired.setTextSize(15);
					if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
						// 得到每一个项
						// 判断出生成几个小%%%%追加说明
						String[] tvCount = q.qCaption.split("%%");
						int lencap = 0;
						for (int col = 0; col < tvCount.length; col++) {
							if (!(col / 2 < q.freeTextColumn)) {
								TextView tvcap = new TextView(this);
								TextPaint paintcap = tvcap.getPaint();
								String textcap = tvCount[col];
								lencap += (int) paintcap.measureText(textcap);
							}
						}
						maxSumWid = maxSumWid + lencap;
					}
				}
				fx = (double) maxCWidth / (double) maxSumWid;
				Log.i("@@@", "fx=" + fx);
				ArrayList<Integer> leftWidAry = new ArrayList<Integer>();
				ArrayList<Integer> leftRequiredWidAry = new ArrayList<Integer>();
				ArrayList<Integer> editWidAry = new ArrayList<Integer>();
				ArrayList<Integer> rightWidAry = new ArrayList<Integer>();
				for (int i = 0; i < tbColumns.size(); i++) {
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					SpannableString ssLeft = null;
					if (!Util.isEmpty(item.leftsideWord)) {
						String strTilte = item.leftsideWord;

						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {

							ssLeft = new SpannableString(strTilte);
							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					SpannableString ssRight = null;
					if (!Util.isEmpty(item.rightsideWord)) {
						String strTilte = item.rightsideWord;
						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {
							ssRight = new SpannableString(strTilte);

							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					int lenRight = 0;
					if (!Util.isEmpty(item.rightsideWord)) {
						TextView tvRight = new TextView(ReviewCommitActivity.this);

						/**
						 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
						 */
						if (item.isHide) {
							tvRight.setVisibility(View.GONE);
						}
						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvRight.setTextColor(Color.BLACK);
						tvRight.setText(ssRight);
						tvRight.setPadding(0, 0, 0, 0);
						TextPaint paintRight = tvRight.getPaint();
						lenRight = (int) paintRight.measureText(item.rightsideWord);
						int maxRight = (int) (screenWidth / (q.freeTextColumn + 4));
						int minRight = (int) paintRight.measureText("俩字");
						if (lenRight > maxRight) {
							lenRight = maxRight;
						} else if (lenRight < minRight) {
							lenRight = minRight;
						}
						lenRight *= fx;
					}
					rightWidAry.add(lenRight);
					int leftText = 0;
					if (!Util.isEmpty(item.leftsideWord) && 1 != q.qLinkage) {
						TextView tvLeft = new TextView(ReviewCommitActivity.this);

						/**
						 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
						 */
						if (item.isHide) {
							tvLeft.setVisibility(View.GONE);
						}
						tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvLeft.setTextColor(Color.BLACK);
						tvLeft.setText(ssLeft);
						tvLeft.setPadding(0, 0, 0, 0);
						TextPaint paintLeft = tvLeft.getPaint();
						String textLeft = item.leftsideWord;
						if (-1 != textLeft.indexOf("%%")) {
							textLeft = textLeft.substring(0, textLeft.indexOf("%%"));
						}
						if (-1 != textLeft.indexOf("@@")) {
							textLeft = textLeft.substring(0, textLeft.indexOf("@@"));
						}
						leftText = (int) paintLeft.measureText(textLeft);
						int maxLeft = (int) (screenWidth / (q.freeTextColumn + 2));
						int minLeft = (int) paintLeft.measureText("俩字");
						if (leftText > maxLeft) {
							leftText = maxLeft;
						} else if (leftText < minLeft) {
							leftText = minLeft;
						}
						leftText *= fx;
					}
					leftWidAry.add(leftText);
					int tvRequiredWid = 0;
					if (item.required) {
						TextView tvRequired = new TextView(ReviewCommitActivity.this);
						tvRequired.setText(getResources().getString(R.string.notice_required));
						tvRequired.setTextColor(Color.RED);
						tvRequiredWid = (int) tvRequired.getPaint()
								.measureText(getResources().getString(R.string.notice_required));
					}
					leftRequiredWidAry.add(tvRequiredWid);

					// 初始化每一项的edittext

					if (item.itemSize != 0) {
						int editWidth = (int) (Util.getEditWidth(item.itemSize, maxCWidth) * fx);
						if (item.dragChecked && 2 == item.type) {
							editWidth = (int) ((screenWidth / q.freeTextColumn) * 2 / 3 * fx);
						}
						editWidAry.add(editWidth);
					} else {
						int editWidth = (int) (Util.getEditWidth(30, maxCWidth) * fx);
						editWidAry.add(editWidth);
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
							if (editWidCol.get(j) < editWidAry.get(i) - 30) {
								editWidCol.set(j, editWidAry.get(i) - 30);
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
							if (leftWidCol.get(j) < leftWidAry.get(i) - 30) {
								leftWidCol.set(j, leftWidAry.get(i) - 30);
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
							if (leftRequiredWidCol.get(j) < leftRequiredWidAry.get(i)) {
								leftRequiredWidCol.set(j, leftRequiredWidAry.get(i));
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
					leftAllWid.add(leftWidCol.get(j) + 30 + leftRequiredWidCol.get(j));
				}
				// 每一列右侧最大总宽度
				ArrayList<Integer> rightAllWid = new ArrayList<Integer>();
				for (int j = 0; j < q.freeTextColumn; j++) {
					rightAllWid.add(rightWidCol.get(j) + 30 + editWidCol.get(j));
				}
				// 追加说明%%处理
				// 改的地方
				if (!Util.isEmpty(q.qCaption)) {
					// tvRequired.setTextSize(15);
					if (q.qCaption.contains("%%") || q.qCaption.contains("%%%%")) {
						// 得到每一个项
						// 判断出生成几个小%%%%追加说明
						String[] tvCount = q.qCaption.split("%%");
						LinearLayout tbr = new LinearLayout(ReviewCommitActivity.this);
						tbr.setOrientation(LinearLayout.HORIZONTAL);
						tbr.setGravity(Gravity.CENTER_VERTICAL);
						ArrayList<LinearLayout> CapLary = new ArrayList<LinearLayout>();// 每行的TableRow对象
						for (int col = 0; col < tvCount.length; col++) {
							LinearLayout capItem = new LinearLayout(ReviewCommitActivity.this);
							capItem.setOrientation(LinearLayout.VERTICAL);
							capItem.setGravity(Gravity.CENTER_HORIZONTAL);
							if (0 == col % 2) {// 左侧表头
								if (col / 2 < q.freeTextColumn) {// col/2表示第几个item
									LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
											leftAllWid.get(col / 2) + 1, LayoutParams.WRAP_CONTENT);
									capItemPar.setMargins(1, 1, 1, 1);
									capItem.setLayoutParams(capItemPar);
								}
							} else {
								if (col / 2 < q.freeTextColumn) {
									LinearLayout.LayoutParams capItemPar = new LinearLayout.LayoutParams(
											rightAllWid.get(col / 2), LayoutParams.WRAP_CONTENT);
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
							tvSmallCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							TextPaint paint = tvSmallCaption.getPaint();
							paint.setFakeBoldText(true);
							tvSmallCaption.setLayoutParams(WRAP_WRAP);
							tvSmallCaption.setText(strTvCaption);
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
					if (1 < q.freeTextColumn) {
						QuestionItem itemNull = tbColumns.get(0);
						String leftsideWord = itemNull.getLeftsideWord();
						if (!Util.isEmpty(leftsideWord)) {
							if (0 == i % q.freeTextColumn) {
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1) * 2;
							} else {
								tempQWidth = (dis.getWidth()) / (q.freeTextColumn + 1);
							}
						}
					}
					// 得到每一个项
					QuestionItem item = tbColumns.get(i);
					item.itemValue = i;
					LinearLayout itemll = colsLLy.get(i);
					// 隐藏选项
					// 隐藏选项
					LinearLayout lLeft = (LinearLayout) itemll.getChildAt(0);
					// 设置每项的左侧布局
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							LinearLayout.LayoutParams lLeftPar = new LinearLayout.LayoutParams(leftAllWid.get(j),
									LayoutParams.MATCH_PARENT);
							if (1 < q.freeTextColumn) {
								lLeftPar.setMargins(1, 1, 1, 1);
							}
							lLeft.setLayoutParams(lLeftPar);
						}
					}
					lLeft.setBackgroundColor(getResources().getColor(R.color.white));
					lLeft.setPadding(15, 15, 15, 15);
					lLeft.removeAllViews();
					LinearLayout lRight = (LinearLayout) itemll.getChildAt(1);
					// 设置每项的右侧布局
					if (item.dragChecked) {
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								LinearLayout.LayoutParams lRightPar = new LinearLayout.LayoutParams(
										screenWidth / q.freeTextColumn - leftAllWid.get(j), LayoutParams.MATCH_PARENT);
								if (1 < q.freeTextColumn) {
									lRightPar.setMargins(1, 1, 1, 1);
								}
								lRight.setLayoutParams(lRightPar);
							}
						}
					} else {
						for (int j = 0; j < q.freeTextColumn; j++) {
							if (j == i % q.freeTextColumn) {
								LinearLayout.LayoutParams lRightPar = new LinearLayout.LayoutParams(rightAllWid.get(j),
										LayoutParams.MATCH_PARENT);
								if (1 < q.freeTextColumn) {
									lRightPar.setMargins(1, 1, 1, 1);
								}
								lRight.setLayoutParams(lRightPar);
							}
						}
					}
					lRight.setBackgroundColor(getResources().getColor(R.color.white));
					lRight.removeAllViews();
					lRight.setPadding(15, 15, 15, 15);
					// LayoutParams lp = new LayoutParams(100,
					// LayoutParams.WRAP_CONTENT);
					/**
					 * 题外关联 之 内部关联 判断 字段 并进行 界面 设定 宽度 大树 内部关联 6
					 */
					// itemLL.setBackgroundColor(Color.BLUE);
					// itemLL.setPadding(5, 5, 20, 5);
					if (item.required) {
						TextView tvRequired = new TextView(ReviewCommitActivity.this);

						/**
						 * 题外关联 之 显示 隐藏 选项 显示 必填 隐藏起来 大树 显示关联 2
						 */

						if (item.isHide) {
							tvRequired.setVisibility(View.GONE);
						}

						LayoutParams myParams = new LayoutParams(WRAP_WRAP);
						// myParams.addRule(RelativeLayout.CENTER_VERTICAL,
						// itemLL.getId());
						// myParams.setMargins(0, 0, 6, 0);
						tvRequired.setLayoutParams(myParams);
						tvRequired.setText(getResources().getString(R.string.notice_required));
						tvRequired.setTextColor(Color.RED);
						int tvRequiredWid = (int) tvRequired.getPaint()
								.measureText(getResources().getString(R.string.notice_required));
						tempQWidth -= tvRequiredWid;
						// tvRequired.setTextSize(15);
						lLeft.addView(tvRequired, lLeft.getChildCount());
					}
					// 初始化每一项的edittext
					EditText et = new EditText(ReviewCommitActivity.this);
					et.setBackgroundResource(R.drawable.bg_edittext);
					/**
					 * 题外关联 显示 的 设计 在这 显示几个选项
					 */

					if (item.isHide) {
						et.setVisibility(View.GONE);
					}
					for (int j = 0; j < q.freeTextColumn; j++) {
						if (j == i % q.freeTextColumn) {
							et.setLayoutParams(new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT));
						}
					}
					// et.setMinimumWidth(150);
					et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					et.setTag(item);
					// 左边样式
					SpannableString ssLeft = null;
					if (!Util.isEmpty(item.leftsideWord)) {
						String strTilte = item.leftsideWord;

						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {

							ssLeft = new SpannableString(strTilte);
							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssLeft.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssLeft.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
					// 右边样式
					SpannableString ssRight = null;
					if (!Util.isEmpty(item.rightsideWord)) {
						String strTilte = item.rightsideWord;
						/**
						 * 换行
						 */
						String newTitle = Util.replaceMatcherList(strTilte);
						if (!Util.isEmpty(newTitle)) {
							strTilte = newTitle;
						}

						/**
						 * 粗体
						 */
						CstmMatcher boldMatcherList = Util.findBoldMatcherList(strTilte);
						boolean boldHave = Util.isEmpty(boldMatcherList.getMis());
						if (!boldHave) {
							strTilte = boldMatcherList.getResultStr();
						}

						/**
						 * 加下划线
						 */
						CstmMatcher cmUnderLine = Util.findUnderlineMatcherList(strTilte);

						if (!Util.isEmpty(cmUnderLine.getMis())) {
							strTilte = cmUnderLine.getResultStr();
						}

						/**
						 * font标签
						 */
						CstmMatcher cm = Util.findFontMatcherList(strTilte);
						boolean noFont = Util.isEmpty(cm.getResultStr());
						if (!noFont) {
							strTilte = cm.getResultStr();
							// System.out.println("Font之后--->"+strTilte);
						}
						int len = strTilte.length();
						if (0 < len) {
							ssRight = new SpannableString(strTilte);

							if (!noFont) {
								for (MatcherItem mi : cm.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length()
											&& mi.end <= strTilte.length()) {
										ssRight.setSpan(new ForegroundColorSpan(mi.color), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									}
								}
							}

							/**
							 * 加粗引用
							 */
							if (!boldHave) {
								for (MatcherItem mi : boldMatcherList.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new StyleSpan(Typeface.BOLD), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									// ss.setSpan(new RelativeSizeSpan(1.3f),
									// mi.start,
									// mi.end,
									// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

							if (!Util.isEmpty(cmUnderLine.getMis())) {
								for (MatcherItem mi : cmUnderLine.getMis()) {
									if (null != mi && -1 != mi.start && -1 != mi.end && mi.end <= strTilte.length())
										ssRight.setSpan(new UnderlineSpan(), mi.start, mi.end,
												Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
							}

						}
					}
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
									provinceSpinner = new Spinner(ReviewCommitActivity.this);
									provinceSpinner.setFocusable(false);
									provinceSpinner.setClickable(false);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 3
									 */
									if (item.isHide) {
										provinceSpinner.setVisibility(View.GONE);
									}

									city = ThreeLeverUtil.getFirstList(s1);
									provinceAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											city);// list是一个
									provinceAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									provinceSpinner.setAdapter(provinceAdapter);
									/**
									 * 获取 默认的答案
									 */
									if (!Util.isEmpty(amList)) {
										AnswerMap am1 = amList.get(0);
										cityPos = provinceAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == cityPos) {
											cityPos = 0;
										}
										provinceSpinner.setSelection(cityPos, true);
									} else {
										provinceSpinner.setSelection(0, true);
									}
									provinceSpinner.setTag(item);
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(provinceSpinner, lRight.getChildCount());
								} else if (i == 1) {
									areaPos = 0;// 清0
									s2 = item.leftsideWord;
									citySpinner = new Spinner(ReviewCommitActivity.this);
									citySpinner.setFocusable(false);
									citySpinner.setClickable(false);
									/**
									 * 题外关联--- 显示关联 大树 显示关联 4
									 */
									if (item.isHide) {
										citySpinner.setVisibility(View.GONE);
									}

									area = ThreeLeverUtil.getSecondList(s2);
									System.out.println("cityPos:" + cityPos);
									areaListTemp = ThreeLeverUtil.getCityPosList(area, city, cityPos);
									if (areaListTemp.size() == 0) {
										areaListTemp.add("空");
									}
									cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											areaListTemp);// list是一个
									cityAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									citySpinner.setAdapter(cityAdapter);
									/**
									 * 获取原有答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(1);
										areaPos = cityAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == areaPos) {
											areaPos = 0;
										}
										citySpinner.setSelection(areaPos, true);
									} else {
										citySpinner.setSelection(0, true);
									}
									citySpinner.setTag(item);
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(citySpinner, lRight.getChildCount());

								} else if (i == 2) {
									s3 = item.leftsideWord;
									countrySpinner = new Spinner(ReviewCommitActivity.this);
									countrySpinner.setFocusable(false);
									countrySpinner.setClickable(false);
									countrySpinner.setOnTouchListener(null);
									/**
									 * 题外关联--- 显示关联 大树 显示关联 5
									 */
									if (item.isHide) {
										countrySpinner.setVisibility(View.GONE);
									}
									way = ThreeLeverUtil.getThridList(s3);
									wayListTemp = ThreeLeverUtil.getAreaPosList(areaListTemp, way, areaPos);
									if (wayListTemp.size() == 0) {
										wayListTemp.add("空");
									}

									countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_adapter,
											wayListTemp);// list是一个
									countryAdapter.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									countrySpinner.setAdapter(countryAdapter);
									/**
									 * 获取 原有 答案
									 */
									if (!Util.isEmpty(amList)) {

										AnswerMap am1 = amList.get(2);
										int pos = countryAdapter.getPosition(am1.getAnswerValue());
										// 三级联动更改 处理更新问卷的操作,原因是找不到值
										if (-1 == pos) {
											pos = 0;
										}
										countrySpinner.setSelection(pos, true);
									} else {
										countrySpinner.setSelection(0, true);
									}
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									String iCap = Util.getLeftCap(item.leftsideWord);
									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
									lRight.addView(countrySpinner, lRight.getChildCount());
									countrySpinner.setTag(item);
								}
							}

						} else {
							// 非三级联动
							// 典型的文本
							// 左右有文本值
							// 假如左右不为空，给设置颜色
							ImageView iv = new ImageView(ReviewCommitActivity.this);

							/**
							 * 题外关联--- 显示关联 大树 显示关联 6
							 */
							if (item.isHide) {
								iv.setVisibility(View.GONE);
							}
							// 维码扫描 item.scanning
							if (6 == item.type) {
								iv = new ImageView(ReviewCommitActivity.this);
								RelativeLayout.LayoutParams myParams = WRAP_WRAP;
								String idStr = q.qIndex + "_" + i;
								iv.setId(idStr.hashCode());
								iv.setLayoutParams(myParams);
								iv.setBackgroundResource(R.drawable.icon_scanning);
							}
							// 维码扫描

							if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
								// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
								ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
								// 左边没有下拉
								if (Util.isEmpty(leftList)) {
									TextView tvLeft = new TextView(ReviewCommitActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 7
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									TextView tvRight = new TextView(ReviewCommitActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 8
									 */
									if (item.isHide) {
										tvRight.setVisibility(View.GONE);
									}

									tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(ssRight);
									tvRight.setPadding(0, 0, 0, 8);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvRight.setLayoutParams(
													new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
									// 有答案取答案 赋值
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									lRight.addView(et, lRight.getChildCount());
									lRight.addView(tvRight, lRight.getChildCount());
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
										TextView tvLeft = new TextView(ReviewCommitActivity.this);

										/**
										 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 9
										 */
										if (item.isHide) {
											tvLeft.setVisibility(View.GONE);
										}

										tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
										tvLeft.setTextColor(Color.BLACK);
										tvLeft.setText(iCap);
										tvLeft.setPadding(0, 0, 0, 8);
										TextPaint paintLeft = tvLeft.getPaint();
										lenLeft = (int) paintLeft.measureText(iCap);
										for (int j = 0; j < q.freeTextColumn; j++) {
											if (j == i % q.freeTextColumn) {
												tvLeft.setLayoutParams(
														new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
											}
										}
										lLeft.addView(tvLeft, lLeft.getChildCount());
									}

									Spinner spLeft = new Spinner(ReviewCommitActivity.this);
									spLeft.setFocusable(false);
									spLeft.setClickable(false);
									spLeft.setOnTouchListener(null);
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
									ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);

									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											AnswerMap am = amList.get(j);
											if (item.itemValue == am.getRow()) {
												int pos = aa.getPosition(am.getAnswerValue());
												if (-1 != pos) {
													// 选上位置

													spLeft.setSelection(pos);
													break;
												}
											}
										}
									}

									TextView tvRight = new TextView(ReviewCommitActivity.this);

									/**
									 * 题外关联--- 显示关联 隐藏 左边文字 大树 显示关联 11
									 */
									if (item.isHide) {
										tvRight.setVisibility(View.GONE);
									}

									tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvRight.setTextColor(Color.BLACK);
									tvRight.setText(ssRight);
									tvRight.setPadding(0, 0, 0, 8);
									TextPaint paintRight = tvRight.getPaint();
									int lenRight = (int) paintRight.measureText(item.rightsideWord);
									// 左边说明不为空，右边长度小于最大长度
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvRight.setLayoutParams(
													new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											spLeft.setLayoutParams(
													new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lRight.addView(spLeft, lRight.getChildCount());
									lRight.addView(tvRight, lRight.getChildCount());
								}
							} else if (!Util.isEmpty(item.leftsideWord)) {
								// 只有左边有文字。右边没文字
								ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
								/**
								 * 获取左边的说明文字
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								int lenLeft = 0;
								if (!Util.isEmpty(iCap)) {
									/**
									 * 左边是说明文字 右边是下拉列表框
									 */
									TextView tvLeft = new TextView(ReviewCommitActivity.this);

									/**
									 * 题外关联--- 显示关联 大树 显示关联 12
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
								} else if (Util.isEmpty(leftList)) {
									// 左边不是说明文字 。没有下拉列表框
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									/**
									 * 题外关联--- 显示关联 大树 显示关联 13
									 */
									if (item.isHide) {
										tvLeft.setVisibility(View.GONE);
									}

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(ssLeft);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
								}
								/**
								 *** 假如左边不是下拉框 直接给文本框赋值。
								 */
								if (Util.isEmpty(leftList)) {
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									lRight.addView(et, lRight.getChildCount());
								} else {
									Spinner spLeft = new Spinner(ReviewCommitActivity.this);
									spLeft.setFocusable(false);
									spLeft.setClickable(false);
									spLeft.setOnTouchListener(null);
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
									ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);
									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											AnswerMap am = amList.get(j);
											if (item.itemValue == am.getRow()) {
												int pos = aa.getPosition(am.getAnswerValue());
												if (-1 != pos) {
													// 选上位置
													spLeft.setSelection(pos);
													break;
												}
											}
										}
									}
									int spWidth = Util.getEditWidth(item.itemSize, maxCWidth);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											spLeft.setLayoutParams(
													new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lRight.addView(spLeft, lRight.getChildCount());

								}
							} else if (!Util.isEmpty(item.rightsideWord)) {
								// 只有右边有。左边没有
								/**
								 * 左边是文本框 右边是说明
								 */
								TextView tvRight = new TextView(ReviewCommitActivity.this);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 15
								 */
								if (item.isHide) {
									tvRight.setVisibility(View.GONE);
								}
								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();

								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(
												new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
							} else {
								// 左右无,只有文本题目
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
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
								sbParams = new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT);
							}
						}
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 左右有文字说明
							TextView tvLeft = new TextView(ReviewCommitActivity.this);

							/**
							 * 题外关联 之显示 隐藏 左边文字
							 */

							if (item.isHide) {
								tvLeft.setVisibility(View.GONE);
							}

							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeft.setLayoutParams(
											new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}

							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}
							TextView tvRight = new TextView(ReviewCommitActivity.this);

							/**
							 * 题外关联 之 显示 隐藏 选项
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}

							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							lLeft.addView(tvLeft, lLeft.getChildCount());
							// 假如是滑动条,且是数字题目。显示滑动条。
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(ReviewCommitActivity.this);
								sb.setFocusable(false);
								sb.setClickable(false);
								sb.setOnTouchListener(null);
								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight)
										+ (tempQWidth / 3 - lenLeft);
								sb.setLayoutParams(FILL_WRAP);
								int itemMaxNumber = Integer
										.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
								int itemMinNumber = Integer
										.parseInt(Util.isEmpty(item.minNumber) ? "0" : item.minNumber);
								sb.setMax(itemMaxNumber - itemMinNumber);
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()) - itemMinNumber);
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(ReviewCommitActivity.this);
								tvSeekTop.setLayoutParams(FILL_WRAP);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
								tvSeekTop.setText("(" + sbProgress + "/" + itemMaxNumber + ")");
								LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(sbParams);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								lRight.addView(rightLL, lRight.getChildCount());
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								// 不是情况直接显示文本题目

								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 3 + (tempQWidth / 3 - lenRight)
										+ (tempQWidth / 3 - lenLeft);
								lRight.addView(et, lRight.getChildCount());
							}
							lRight.addView(tvRight, lRight.getChildCount());

							/**
							 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树 内部关联 7
							 */
							if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {

								TextView tvSyb = new TextView(ReviewCommitActivity.this);
								tvSyb.setLayoutParams(WRAP_WRAP);
								// tvSyb.setLayoutParams(new
								// LayoutParams(100,LayoutParams.WRAP_CONTENT));
								tvSyb.setTextColor(Color.RED);
								tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

								/**
								 * 在这里加一个提示 题外关联 显示的 提示 语 ！
								 */
								tvSyb.setText("(" + this.getString(R.string.sum) + ")");
								TextPaint paintSyb = tvSyb.getPaint();
								int lenSyb = (int) paintSyb.measureText(item.leftsideWord);
								tvSyb.setLayoutParams(new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
								lRight.addView(tvSyb);
							}

						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只左有
							TextView tvLeft = new TextView(ReviewCommitActivity.this);
							/**
							 * 题外关联 之显示 隐藏 选项 左边文字
							 */
							if (item.isHide) {
								tvLeft.setVisibility(View.GONE);

							}
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(ssLeft);
							tvLeft.setPadding(0, 0, 0, 8);
							TextPaint paintLeft = tvLeft.getPaint();
							int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvLeft.setLayoutParams(
											new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							if (1 == item.type) {
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}

							// 假如是滑动条,且是数字题目。显示滑动条。
							lLeft.addView(tvLeft, lLeft.getChildCount());
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(ReviewCommitActivity.this);
								sb.setFocusable(false);
								sb.setClickable(false);
								sb.setOnTouchListener(null);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 16
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								sb.setLayoutParams(FILL_WRAP);
								// sb.set
								int itemMaxNumber = Integer
										.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
								int itemMinNumber = Integer
										.parseInt(Util.isEmpty(item.minNumber) ? "0" : item.minNumber);
								sb.setMax(itemMaxNumber - itemMinNumber);
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()) - itemMinNumber);
											}
										}
									}
								}
								sb.setThumbOffset(0);

								TextView tvSeekTop = new TextView(ReviewCommitActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 17
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}
								tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
								tvSeekTop.setText("(" + sbProgress + "/" + itemMaxNumber + ")");

								LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(sbParams);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								lRight.addView(rightLL, lRight.getChildCount());
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenLeft);
								lRight.addView(et, lRight.getChildCount());

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 8
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(ReviewCommitActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									// tvSyb.setLayoutParams(new
									// LayoutParams(150,
									// LayoutParams.WRAP_CONTENT));
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintSyb = tvSyb.getPaint();
									int lenSyb = (int) paintSyb.measureText(item.leftsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									lRight.addView(tvSyb, lRight.getChildCount());
								}

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}
							TextView tvRight = new TextView(ReviewCommitActivity.this);

							/**
							 * 题外关联 之显示 隐藏 选项 右边文字
							 */
							if (item.isHide) {
								tvRight.setVisibility(View.GONE);
							}
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();
							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(ReviewCommitActivity.this);
								sb.setFocusable(false);
								sb.setClickable(false);
								sb.setOnTouchListener(null);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 18
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								// sb.setLayoutParams(new LayoutParams(3 *
								// dis.getWidth() / 4, //
								// LayoutParams.WRAP_CONTENT));
								// sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								int remainWidth = tempQWidth / 2 + (tempQWidth / 2 - lenRight);
								sb.setLayoutParams(FILL_WRAP);
								// sb.set
								int itemMaxNumber = Integer
										.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
								int itemMinNumber = Integer
										.parseInt(Util.isEmpty(item.minNumber) ? "0" : item.minNumber);
								sb.setMax(itemMaxNumber - itemMinNumber);
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()) - itemMinNumber);
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(ReviewCommitActivity.this);
								// tvSeekTop.setLayoutParams(FILL_WRAP);

								/**
								 * 题外关联--- 显示关联 大树 显示关联 19
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}

								tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
								tvSeekTop.setText("(" + sbProgress + "/" + itemMaxNumber + ")");
								LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								// rightLL.setLayoutParams(WRAP_WRAP);
								rightLL.setLayoutParams(sbParams);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								lRight.addView(rightLL, lRight.getChildCount());

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 9
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(ReviewCommitActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintLeft = tvSyb.getPaint();
									int lenSyb = (int) paintLeft.measureText(item.leftsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenSyb * fx), LayoutParams.WRAP_CONTENT));
									lRight.addView(tvSyb);
								}

								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 10
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(ReviewCommitActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									lRight.addView(tvSyb);
								}

							}

							lRight.addView(tvRight, lRight.getChildCount());

						} else {
							// 左右无
							if (1 == item.type) {// 日期
								// et.setMinWidth(240);
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							} else if (2 == item.type) {// 数字
								if (item.isFloat) {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								} else {
									et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
											| InputType.TYPE_NUMBER_FLAG_SIGNED);
								}
								if (!Util.isEmpty(item.minNumber) && !Util.isEmpty(item.maxNumber)) {
									et.setHint(
											this.getString(R.string.edit_min_to_max, item.minNumber, item.maxNumber));
								} else if (!Util.isEmpty(item.minNumber)) {
									et.setHint(this.getString(R.string.edit_min, item.minNumber));
								} else if (!Util.isEmpty(item.maxNumber)) {
									et.setHint(this.getString(R.string.edit_max, item.maxNumber));
								}
							} else if (3 == item.type) {// 英文/数字
								et.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if (5 == item.type) {// 邮件
								et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								et.setHint(this.getString(R.string.please_input_email));
							}

							if (item.dragChecked && 2 == item.type) {
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
								SeekBar sb = new SeekBar(ReviewCommitActivity.this);
								sb.setFocusable(false);
								sb.setClickable(false);
								sb.setOnTouchListener(null);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 20
								 */
								if (item.isHide) {
									sb.setVisibility(View.GONE);
								}

								sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
								sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
								sb.setTag(item);
								sb.setLayoutParams(FILL_WRAP);
								sb.setPadding(5, 0, 5, 15);
								// sb.setMinimumWidth(400);
								// sb.set
								int itemMaxNumber = Integer
										.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
								int itemMinNumber = Integer
										.parseInt(Util.isEmpty(item.minNumber) ? "0" : item.minNumber);
								sb.setMax(itemMaxNumber - itemMinNumber);
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											if (!Util.isEmpty(am.getAnswerValue())) {
												sb.setProgress(Integer.parseInt(am.getAnswerValue()) - itemMinNumber);
											}
										}
									}
								}
								sb.setThumbOffset(0);
								TextView tvSeekTop = new TextView(ReviewCommitActivity.this);
								/**
								 * 题外关联--- 显示关联 大树 显示关联 21
								 */
								if (item.isHide) {
									tvSeekTop.setVisibility(View.GONE);
								}

								tvSeekTop.setLayoutParams(FILL_WRAP);
								tvSeekTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvSeekTop.setGravity(Gravity.CENTER);
								tvSeekTop.setTextColor(Color.BLUE);

								/**
								 * 只有滚动条
								 */
								int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
								tvSeekTop.setText("(" + sbProgress + "/" + itemMaxNumber + ")");
								LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
								rightLL.setOrientation(LinearLayout.VERTICAL);
								rightLL.setLayoutParams(sbParams);
								rightLL.addView(tvSeekTop, rightLL.getChildCount());
								rightLL.addView(sb, rightLL.getChildCount());
								lRight.addView(rightLL, lRight.getChildCount());
								/**
								 * -----------------------------------拖动条样式----
								 * ---- ------------------
								 ***/
							} else {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}

								lRight.addView(et, lRight.getChildCount());

								/**
								 * 题外关联 之 内部关联 选项提示 （SUM） 标示 哪一个选项 是 求和目标 大树
								 * 内部关联 11
								 */
								if (item.symbol != null && item.symbol.equals(this.getString(R.string.sum))) {
									TextView tvSyb = new TextView(ReviewCommitActivity.this);
									tvSyb.setLayoutParams(WRAP_WRAP);
									tvSyb.setTextColor(Color.RED);
									tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);

									/**
									 * 在这里加一个提示 题外关联 显示的 提示 语 ！
									 */
									tvSyb.setText("(" + this.getString(R.string.sum) + ")");
									TextPaint paintLeft = tvSyb.getPaint();
									int lenRight = (int) paintLeft.measureText(item.leftsideWord);
									tvSyb.setLayoutParams(
											new LayoutParams((int) (lenRight * fx), LayoutParams.WRAP_CONTENT));

									lRight.addView(tvSyb);
								}

								// System.out.println("tempQWidth:"+tempQWidth+"--item"+itemLL.getLayoutParams().width);
							}
						}
						break;

					case 4:// 字典格式
							// 假如左右不为空，给设置颜色

						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// 含有%%1%%2%%此类信息 只有左边才可能有下拉题目
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							// 左边没有下拉
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(ReviewCommitActivity.this);

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								int lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								TextView tvRight = new TextView(ReviewCommitActivity.this);

								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								TextPaint paintRight = tvRight.getPaint();
								int lenRight = (int) paintRight.measureText(item.rightsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(
												new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
								// 有答案取答案 赋值
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								// 余下的长度
								lRight.addView(et, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
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
									TextView tvLeft = new TextView(ReviewCommitActivity.this);

									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(iCap);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
								}

								Spinner spLeft = new Spinner(ReviewCommitActivity.this);
								spLeft.setFocusable(false);
								spLeft.setClickable(false);
								spLeft.setOnTouchListener(null);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								// simple_spinner_item
								// R.layout.simple_spinner_dropdown_item
								ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);

								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										AnswerMap am = amList.get(j);
										if (item.itemValue == am.getRow()) {
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置

												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}

								TextView tvRight = new TextView(ReviewCommitActivity.this);

								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(ssRight);
								tvRight.setPadding(0, 0, 0, 8);
								// 左边说明不为空，右边长度小于最大长度
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvRight.setLayoutParams(
												new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										spLeft.setLayoutParams(
												new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lRight.addView(spLeft, lRight.getChildCount());
								lRight.addView(tvRight, lRight.getChildCount());
							}
						} else if (!Util.isEmpty(item.leftsideWord)) {
							// 只有左边有文字。右边没文字
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							/**
							 * 获取左边的说明文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							int lenLeft = 0;
							if (!Util.isEmpty(iCap)) {
								/**
								 * 左边是说明文字 右边是下拉列表框
								 */
								TextView tvLeft = new TextView(ReviewCommitActivity.this);

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(iCap);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								// 左边不是说明文字 。没有下拉列表框
								TextView tvLeft = new TextView(ReviewCommitActivity.this);

								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(ssLeft);
								tvLeft.setPadding(0, 0, 0, 8);
								TextPaint paintLeft = tvLeft.getPaint();
								lenLeft = (int) paintLeft.measureText(item.leftsideWord);
								for (int j = 0; j < q.freeTextColumn; j++) {
									if (j == i % q.freeTextColumn) {
										tvLeft.setLayoutParams(
												new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
									}
								}
								lLeft.addView(tvLeft, lLeft.getChildCount());
							}
							/**
							 *** 假如左边不是下拉框 直接给文本框赋值。
							 */
							if (Util.isEmpty(leftList)) {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								lRight.addView(et, lRight.getChildCount());
							} else {
								// 是下拉框。给下拉框赋值
								/**
								 * 大树文本框修改
								 */
								if (Util.isEmpty(iCap)) {
									Log.i("zrl1", item.leftsideWord + "左边文字");
									// 1
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(item.leftsideWord);
									tvLeft.setPadding(0, 0, 0, 8);
									TextPaint paintLeft = tvLeft.getPaint();
									lenLeft = (int) paintLeft.measureText(item.leftsideWord);
									for (int j = 0; j < q.freeTextColumn; j++) {
										if (j == i % q.freeTextColumn) {
											tvLeft.setLayoutParams(
													new LayoutParams(leftWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lLeft.addView(tvLeft, lLeft.getChildCount());
									// 2
									if (!Util.isEmpty(amList)) {
										String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
										for (AnswerMap am : amList) {
											if (etName.equals(am.getAnswerName())) {
												et.setText(am.getAnswerValue());
											}
										}
									}
									lRight.addView(et, lRight.getChildCount());

								} else {
									// 大树文本框修改
									Spinner spLeft = new Spinner(ReviewCommitActivity.this);
									spLeft.setTag(item);
									spLeft.setFocusable(false);
									spLeft.setClickable(false);
									spLeft.setOnTouchListener(null);
									spLeft.setLayoutParams(WRAP_WRAP);
									// simple_spinner_item
									// R.layout.simple_spinner_dropdown_item
									ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
											R.layout.simple_spinner_adapter);
									aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
									for (String str : leftList) {
										aa.add(str);
									}
									spLeft.setAdapter(aa);
									if (!Util.isEmpty(amList)) {
										for (int j = 0; j < amList.size(); j++) {
											// 通过存的value得到位置
											AnswerMap am = amList.get(j);
											if (item.itemValue == am.getRow()) {
												int pos = aa.getPosition(am.getAnswerValue());
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
											spLeft.setLayoutParams(
													new LayoutParams(editWidCol.get(j), LayoutParams.WRAP_CONTENT));
										}
									}
									lRight.addView(spLeft, lRight.getChildCount());
								}

							}
						} else if (!Util.isEmpty(item.rightsideWord)) {
							// 只有右边有。左边没有
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(ReviewCommitActivity.this);

							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(ssRight);
							tvRight.setPadding(0, 0, 0, 8);
							TextPaint paintRight = tvRight.getPaint();

							int lenRight = (int) paintRight.measureText(item.rightsideWord);
							if (lenRight > (q.freeTextColumn + 4) * fx) {
								lenRight = (int) ((q.freeTextColumn + 4) * fx);
							}
							for (int j = 0; j < q.freeTextColumn; j++) {
								if (j == i % q.freeTextColumn) {
									tvRight.setLayoutParams(
											new LayoutParams(rightWidCol.get(j), LayoutParams.WRAP_CONTENT));
								}
							}
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
							lRight.addView(tvRight, lRight.getChildCount());
						} else {
							// 左右无,只有文本题目
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							lRight.addView(et, lRight.getChildCount());
						}
						// 数据字典
						// 为输入添加TextWatcher监听文字的变化
						et.setKeyListener(null);
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
					et.setFocusable(false);
					et.setClickable(false);
					et.setOnTouchListener(null);
				}

			} else {
				/**
				 * 标题最大宽度
				 */
				// tvTitle.setMaxWidth(800);

				/**
				 * 题型的横向、纵向摆放
				 */
				if (Cnt.ORIENT_VERTICAL.equals(q.deployment)) {
					bodyView.setOrientation(LinearLayout.VERTICAL);
				} else if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
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
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_syb,
								Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber, //
								Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber
										+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					} else {
						/**
						 * 数字类型的最大值不为空
						 */
						TextView tvSyb = new TextView(ReviewCommitActivity.this);
						tvSyb.setLayoutParams(WRAP_WRAP);
						tvSyb.setTextColor(Color.RED);
						tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
						tvSyb.setText(getResources().getString(R.string.question_max_min,
								Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
								Util.isEmpty(q.freeMaxNumber) ? "0"
										: q.freeMaxNumber
												+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
						bodyView.addView(tvSyb, bodyView.getChildCount());
					}

					for (int i = 0; i < tbColumns.size(); i++) {
						QuestionItem item = tbColumns.get(i);
						item.itemValue = i;
						SeekBar sb = new SeekBar(ReviewCommitActivity.this);
						sb.setFocusable(false);
						sb.setClickable(false);
						sb.setThumb(getResources().getDrawable(R.drawable.one_key_scan_bg_ani_bg));
						sb.setProgressDrawable(getResources().getDrawable(R.layout.seekbar_style));
						sb.setTag(item);
						sb.setLayoutParams(new LayoutParams(3 * dis.getWidth() / 4, //
								LayoutParams.WRAP_CONTENT));
						sb.setPadding(5, 0, 5, 15);
						sb.setMinimumWidth(400);
						// sb.set
						int itemMaxNumber = Integer.parseInt(Util.isEmpty(item.maxNumber) ? "100" : item.maxNumber);
						int itemMinNumber = Integer.parseInt(Util.isEmpty(item.minNumber) ? "0" : item.minNumber);
						sb.setMax(itemMaxNumber - itemMinNumber);
						if (!Util.isEmpty(amList)) {
							String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
							for (AnswerMap am : amList) {
								if (etName.equals(am.getAnswerName())) {
									if (!Util.isEmpty(am.getAnswerValue())) {
										sb.setProgress(Integer.parseInt(am.getAnswerValue()) - itemMinNumber);
									}
								}
							}
						}
						// sb.setContentDescription("50");

						// sb.setO
						int sbProgress = sb.getProgress() + itemMinNumber;// 进度值
						TextView tvRight = new TextView(ReviewCommitActivity.this);
						tvRight.setLayoutParams(FILL_WRAP);
						tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvRight.setGravity(Gravity.CENTER);
						tvRight.setTextColor(Color.BLUE);

						/**
						 * 
						 * 滚动条左右两边都有文字, 效果:左边的文字在滚动条的左边|右边文字和滚动条,其中文字在滚动条的上边
						 */
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							/**
							 * 次布局存放左边文字、滚动条、右边文字
							 */
							LinearLayout ll = new LinearLayout(ReviewCommitActivity.this);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							ll.setLayoutParams(FILL_WRAP);
							ll.setPadding(5, 5, 20, 5);

							TextView tvLeft = new TextView(ReviewCommitActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);

							/**
							 * 显示当前的刻度
							 */

							tvRight.setText(item.rightsideWord + "(" + sbProgress + "/" + itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(tvLeft, bodyView.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						} else if (!Util.isEmpty(item.leftsideWord)) {
							/**
							 * 左边有文字, 效果:左边文字,右边滚动条
							 */
							/**
							 * 次布局存放左边文字、滚动条、右边文字
							 */
							LinearLayout ll = new LinearLayout(ReviewCommitActivity.this);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							ll.setLayoutParams(FILL_WRAP);
							ll.setPadding(5, 5, 20, 5);

							TextView tvLeft = new TextView(ReviewCommitActivity.this);
							tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvLeft.setTextColor(Color.BLACK);
							tvLeft.setText(item.leftsideWord);

							/**
							 * 显示当前的刻度
							 */
							tvRight.setText("(" + sbProgress + "/" + itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
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
							tvRight.setText(item.rightsideWord + "(" + sbProgress + "/" + itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						} else {
							/**
							 * 只有滚动条
							 */
							tvRight.setText("(" + sbProgress + "/" + itemMaxNumber + ")");

							LinearLayout rightLL = new LinearLayout(ReviewCommitActivity.this);
							rightLL.setOrientation(LinearLayout.VERTICAL);
							rightLL.setLayoutParams(WRAP_WRAP);
							rightLL.addView(tvRight, rightLL.getChildCount());
							rightLL.addView(sb, rightLL.getChildCount());
							bodyView.addView(rightLL, bodyView.getChildCount());
						}
					}
					// 一下代码不要执行
					break;
				}

				// 需要求EditText之中的条件是否符合大于、大于等于、小于、小于等于的条件
				boolean isSyb = (1 != q.qDragChecked && "figure".equals(q.freeTextSort) && //
						!Util.isEmpty(q.freeSymbol));
				if (isSyb) {
					TextView tvSyb = new TextView(ReviewCommitActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
					tvSyb.setText(getResources().getString(R.string.question_syb,
							Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber, //
							Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber
									+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				} else if ("figure".equals(q.freeTextSort) && !Util.isEmpty(q.freeMaxNumber)) {
					/**
					 * 数字类型的最大值不为空
					 */
					TextView tvSyb = new TextView(ReviewCommitActivity.this);
					tvSyb.setLayoutParams(WRAP_WRAP);
					tvSyb.setTextColor(Color.RED);
					tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
					tvSyb.setText(getResources().getString(R.string.question_max_min,
							Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
							q.freeMaxNumber + (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
					bodyView.addView(tvSyb, bodyView.getChildCount());
				}
				if (!Util.isEmpty(tbColumns)) {// 非滚动条
					for (int i = 0; i < tbColumns.size(); i++) {
						QuestionItem item = tbColumns.get(i);
						item.itemValue = i;
						LinearLayout ll = new LinearLayout(ReviewCommitActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setLayoutParams(WRAP_WRAP);
						ll.setPadding(5, 5, 20, 5);
						EditText et = new EditText(ReviewCommitActivity.this);
						et.setBackgroundResource(R.drawable.bg_edittext);
						et.setFocusable(false);
						et.setClickable(false);
						et.setOnTouchListener(null);
						// et.set
						if (1 != q.qDragChecked && "figure".equals(q.freeTextSort)) {
							et.setInputType(InputType.TYPE_CLASS_NUMBER);// setInputType(InputType.TYPE_CLASS_NUMBER);
						}
						et.setLayoutParams(WRAP_WRAP);
						et.setMinimumWidth(150);
						et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						// et.setId(i);
						et.setTag(item);

						/**
						 * 假如文本框左右两边都有说明文字的话
						 */
						if (!Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {
							// System.out.println("两边都有");
							// 含有%%1%%2%%此类信息
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							// ArrayList<String> rightList =
							// Util.obtainList(item.rightsideWord);
							if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(ReviewCommitActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(item.leftsideWord);
								TextView tvRight = new TextView(ReviewCommitActivity.this);
								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(item.rightsideWord);
								ll.addView(tvLeft, ll.getChildCount());

								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								ll.addView(et, ll.getChildCount());
								ll.addView(tvRight, ll.getChildCount());
							} else {
								/**
								 * 左边有文字
								 */
								String iCap = Util.getLeftCap(item.leftsideWord);
								if (!Util.isEmpty(iCap)) {
									/**
									 * @@::前面有文字
									 */
									TextView tvLeft = new TextView(ReviewCommitActivity.this);
									tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
									tvLeft.setTextColor(Color.BLACK);
									tvLeft.setText(iCap);
									ll.addView(tvLeft, ll.getChildCount());
								}
								Spinner spLeft = new Spinner(ReviewCommitActivity.this);
								spLeft.setClickable(false);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										AnswerMap am = amList.get(j);
										if (item.itemValue == am.getRow()) {
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								ll.addView(spLeft, ll.getChildCount());

								TextView tvRight = new TextView(ReviewCommitActivity.this);
								tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvRight.setTextColor(Color.BLACK);
								tvRight.setText(item.rightsideWord);
								ll.addView(tvRight, ll.getChildCount());
							}
							bodyView.addView(ll, bodyView.getChildCount());

						} else if (!Util.isEmpty(item.leftsideWord) && Util.isEmpty(item.rightsideWord)) {// 只有左边有文字
							// System.out.println("左边文字");
							ArrayList<String> leftList = Util.obtainList(item.leftsideWord);
							/**
							 * 获取左边的说明文字
							 */
							String iCap = Util.getLeftCap(item.leftsideWord);
							if (!Util.isEmpty(iCap)) {
								// System.out.println("iCap=" + iCap);
								/**
								 * 左边是说明文字 右边是下拉列表框
								 */
								TextView tvLeft = new TextView(ReviewCommitActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(iCap);
								ll.addView(tvLeft, ll.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								TextView tvLeft = new TextView(ReviewCommitActivity.this);
								tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
								tvLeft.setTextColor(Color.BLACK);
								tvLeft.setText(item.leftsideWord);
								ll.addView(tvLeft, ll.getChildCount());
							}

							if (1 == item.dateCheck) {
								et.setMinWidth(240);
								// System.out.println("1 == item.dateCheck");
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								ll.addView(et, ll.getChildCount());
							} else if (Util.isEmpty(leftList)) {
								if (!Util.isEmpty(amList)) {
									String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
									for (AnswerMap am : amList) {
										if (etName.equals(am.getAnswerName())) {
											et.setText(am.getAnswerValue());
										}
									}
								}
								ll.addView(et, ll.getChildCount());
							}
							if (!Util.isEmpty(leftList)) {
								// System.out.println("!Util.isEmpty(leftList)--->"
								// + leftList);
								Spinner spLeft = new Spinner(ReviewCommitActivity.this);
								spLeft.setClickable(false);
								spLeft.setTag(item);
								spLeft.setLayoutParams(WRAP_WRAP);
								ArrayAdapter<String> aa = new ArrayAdapter<String>(ReviewCommitActivity.this,
										R.layout.simple_spinner_adapter);
								aa.setDropDownViewResource(R.layout.simple_spinner_adapter_item);
								for (String str : leftList) {
									aa.add(str);
								}
								spLeft.setAdapter(aa);
								if (!Util.isEmpty(amList)) {
									for (int j = 0; j < amList.size(); j++) {
										// 通过存的value得到位置
										AnswerMap am = amList.get(j);
										if (item.itemValue == am.getRow()) {
											int pos = aa.getPosition(am.getAnswerValue());
											if (-1 != pos) {
												// 选上位置
												spLeft.setSelection(pos);
												break;
											}
										}
									}
								}
								ll.addView(spLeft, ll.getChildCount());
							}
							bodyView.addView(ll, bodyView.getChildCount());
						} else if (Util.isEmpty(item.leftsideWord) && !Util.isEmpty(item.rightsideWord)) {// 右边有文字
							// System.out.println("右边文字");
							/**
							 * 左边是文本框 右边是说明
							 */
							TextView tvRight = new TextView(ReviewCommitActivity.this);
							tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
							tvRight.setTextColor(Color.BLACK);
							tvRight.setText(item.rightsideWord);
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							ll.addView(et, ll.getChildCount());
							ll.addView(tvRight, ll.getChildCount());
							bodyView.addView(ll, bodyView.getChildCount());
						} else {// 两边都没有文字
							// System.out.println("两边都没文字");
							if (1 == item.dateCheck) {
								et.setMinWidth(240);
								// System.out.println("1 == item.dateCheck");
								Drawable d = getResources().getDrawable(R.drawable.day);
								et.setCompoundDrawablesWithIntrinsicBounds(null, null, //
										d, //
										null);
							}
							if (!Util.isEmpty(amList)) {
								String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
								for (AnswerMap am : amList) {
									if (etName.equals(am.getAnswerName())) {
										et.setText(am.getAnswerValue());
									}
								}
							}
							bodyView.addView(et, bodyView.getChildCount());
						}
					}
				} // 非滚动条
			}
			break;

		case Cnt.TYPE_FREE_TEXT_AREA:// 文本域
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
				// System.out.println("横向");
				bodyView.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				// System.out.println("纵向");
				bodyView.setOrientation(LinearLayout.VERTICAL);
			}
			if (1 != q.qDragChecked && //
					"figure".equals(q.freeTextSort) && //
					!Util.isEmpty(q.freeSymbol)) {
				TextView tvSyb = new TextView(this);
				tvSyb.setLayoutParams(WRAP_WRAP);
				tvSyb.setTextColor(Color.RED);
				tvSyb.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
				tvSyb.setText(getResources().getString(R.string.question_syb,
						Util.isEmpty(q.freeMinNumber) ? "0" : q.freeMinNumber,
						Util.isEmpty(q.freeMaxNumber) ? "0" : q.freeMaxNumber, q.freeSymbol + q.freeSumNumber
								+ (1 == q.freeNoRepeat ? getString(R.string.num_no_repeat) : "")));
				bodyView.addView(tvSyb, bodyView.getChildCount());
			}
			ArrayList<QuestionItem> tbRows = q.getRowItemArr();
			int textAreaRows = q.textAreaRows;// 一共几行
			if (!Util.isEmpty(tbRows)) {
				for (int i = 0; i < tbRows.size(); i++) {
					QuestionItem item = tbRows.get(i);
					item.itemValue = i;
					EditText et = new EditText(ReviewCommitActivity.this);
					et.setBackgroundResource(R.drawable.bg_edittext);
					et.setFocusable(false);
					et.setOnTouchListener(null);
					et.setClickable(false);
					et.setTag(item);
					et.setLayoutParams(FILL_WRAP);
					et.setWidth((int) (dis.getWidth() / 1.5));
					et.setHeight(textAreaRows * dis.getHeight() / 20);
					et.setGravity(Gravity.TOP);
					// 问卷字号动态设置
					et.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
					// System.out.println("item.itemText=" + item.itemText);
					if (!Util.isEmpty(item.itemText)) {
						TextView tvCap = new TextView(ReviewCommitActivity.this);
						tvCap.setLayoutParams(FILL_WRAP);
						tvCap.setTextColor(Color.BLACK);
						tvCap.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
						tvCap.setText(item.itemText);
						bodyView.addView(tvCap, bodyView.getChildCount());
					}
					if (!Util.isEmpty(amList)) {
						String etName = Util.GetAnswerName(q, item, 0, 0, true, false);
						for (AnswerMap am : amList) {
							if (etName.equals(am.getAnswerName())) {
								et.setText(am.getAnswerValue());
							}
						}
					}
					bodyView.addView(et, bodyView.getChildCount());
				}
			}
			break;

		case Cnt.TYPE_MEDIA:// 预留多媒体题型
			/**
			 * 标题最大宽度
			 */
			// tvTitle.setMaxWidth(800);

			/**
			 * 题型的横向、纵向摆放
			 */
			if (Cnt.ORIENT_HORIZONTAL.equals(q.deployment)) {
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
					ImageView iv = new ImageView(ReviewCommitActivity.this);
					iv.setLayoutParams(WRAP_WRAP);
					Uri uri = Uri.parse(Util.getMediaPath(ReviewCommitActivity.this, q.surveyId, q.qMediaSrc));
					iv.setImageURI(uri);
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
					ImageView iv = new ImageView(ReviewCommitActivity.this);
					iv.setPadding(60, 60, 60, 60);
					iv.setBackgroundResource(R.drawable.shape_bg);
					iv.setLayoutParams(WRAP_WRAP);
					iv.setImageResource(R.drawable.play);
					final Question _q = q;
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(ReviewCommitActivity.this, VideoPlayerActivity.class);
							it.putExtra("path", Util.getMediaPath(_q.surveyId, _q.qMediaSrc));
							startActivity(it);
						}
					});
					bodyView.addView(iv, bodyView.getChildCount());
				}

			}
			break;
		}
		ll.addView(questionView, ll.getChildCount());
	}
	/**
	 * 为单选添加单选按钮
	 * 
	 * @param amList
	 * @param arrLayout
	 * @param item
	 * @param rbLL
	 * @param otherItems
	 * @param lineNum
	 * @param isDumbOk
	 * @param operType
	 * @param i
	 * @return
	 */
	private int addRb(float rowsOneWidth, ArrayList<AnswerMap> amList, ArrayList<LinearLayout> arrLayout,
			QuestionItem item, LinearLayout rbA, ArrayList<QuestionItem> otherItems, LinearLayout bodyView, Question q,
			int lineNum) {
		// 生成一项选择框布局
		LinearLayout rbLL = new LinearLayout(ReviewCommitActivity.this);
		rbLL.setOrientation(LinearLayout.HORIZONTAL);
		// 将选项框加入整体布局中
		rbA.addView(rbLL, rbA.getChildCount());
		if (!Util.isEmpty(item.popUp)) {
			/**
			 * 生成弹窗说明小图
			 */
			RadioButton imTv = new RadioButton(ReviewCommitActivity.this);
			imTv.setButtonDrawable(R.drawable.tip);
			rbLL.addView(imTv, rbLL.getChildCount());
		}

		/**
		 * 生成一个单选按钮
		 */
		RadioButton rb = new RadioButton(ReviewCommitActivity.this);
		rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		rb.setClickable(false);
		/**
		 * 选项的追加说明 ,有追加说明的情况
		 */
		TextView tvItemCap = new TextView(ReviewCommitActivity.this);
		if (!Util.isEmpty(item.caption)) {
			// 单选追加说明方法
			if (1 == item.caption_check) {
			} else {
				// 追加说明新布局原FILL_WRAP
				tvItemCap.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				tvItemCap.setTextColor(Color.GRAY);// 统计局专有页面
				tvItemCap.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);

				// 更改的样式
				ImageGetter imgGetter = new Html.ImageGetter() {
					public Drawable getDrawable(String source) {
						Drawable drawable = null;
						String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey"
								+ File.separator + feed.getSurveyId() + File.separator + source;
						// System.out.println("name:" + name);
						drawable = Drawable.createFromPath(name);
						Bitmap image = BitmapFactory.decodeFile(name);
						if (image != null) {
							float tWidth = image.getWidth();
							float tHeight = image.getHeight();
							if (tWidth > maxCWidth) {
								tHeight = maxCWidth / tWidth * tHeight;
								tWidth = maxCWidth;
							}
							drawable.setBounds(0, 0, (int) tWidth, (int) tHeight);
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
			rbLL.setLayoutParams(
					new LinearLayout.LayoutParams((int) rowsOneWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		} else {
			rbLL.setLayoutParams(new LinearLayout.LayoutParams(maxCWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
		// 百分比结束
		String idStr = q.qIndex + "_" + item.itemValue;
		rb.setId(idStr.hashCode());
		// rb.setButtonDrawable(R.drawable.small_radiobutton_temp);
		rb.setButtonDrawable(R.drawable.small_radiobutton);
		Drawable drawable = getResources().getDrawable(R.drawable.small_radiobutton_on);
		int leftwidth = drawable.getIntrinsicWidth();
		rb.setTextColor(Color.BLACK);
		rb.setGravity(Gravity.CENTER_VERTICAL);
		rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, lowSurveySize);
		// 更改的样式
		final float imgWidth = rowsOneWidth - leftwidth;
		ImageGetter imgGetter = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				String name = ReviewCommitActivity.this.getFilesDir() + File.separator + "survey" + File.separator
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
		/**
		 * 新哑题 判断哑题结束
		 */
		String name = Util.GetAnswerName(q, item, 0, 0, false, false);
		// if (1 == q.qDumbFlag) {
		// } else {
		// System.out.println("不是预选项");
		if (!Util.isEmpty(amList)) {// 题目有答案
			// System.out.println("题目有答案");
			for (AnswerMap am : amList) {
				if (name.equals(am.getAnswerName().trim())
						&& String.valueOf(item.itemValue).trim().equals(am.getAnswerValue().trim())) {
					// System.out.println("ddddddddd");
					rb.setChecked(true);
					item.isCheck = true;
					rb.setTag(item);
					break;
				}
			}
		}
		// }
		// 单选自动下一页
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
							LinearLayout rbM = new LinearLayout(ReviewCommitActivity.this);
							rbM.setOrientation(LinearLayout.HORIZONTAL);
							bodyView.addView(rbM, bodyView.getChildCount());
							arrLayout.add(rbM);
						}
						bodyView.addView(tvItemCap, bodyView.getChildCount() - 1);
					} else {
						bodyView.addView(tvItemCap, bodyView.getChildCount());
					}

				}
			}
		}
		rbLL.addView(rb, rbLL.getChildCount());
		// 将没有其他项的单选按钮加入集合中
		// 配置型隐藏选项
		return lineNum;
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			this.setResult(3);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private final class CustomViewFactor implements ViewFactory {

		@Override
		public View makeView() {
			ImageView i = new ImageView(ReviewCommitActivity.this);
			// i.setBackgroundColor(0xFF000000);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return i;
		}

	}

	private View preView;
	private BitmapDrawable bd = null;

	private final class CustomItemSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			UploadFeed image = (UploadFeed) parent.getItemAtPosition(position);
			if (null != image) {
				if (null != preView) {
					/**
					 * 假如上一次点过图片的花 则将其北京置为白色,即表示不是当前显示的图片
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
						File file = new File(image.getPath() + File.separator + image.getName());
						// 数字越大读出的图片占用的heap越小 不然总是溢出
						long len = file.length();
						if (1048576 > len) { // 小于1024k
							opts.inSampleSize = 3;
						} else {
							opts.inSampleSize = 6;
						}
						// resizeBmp = BitmapFactory.decodeFile(file.getPath(),
						// opts);
						// Bitmap resizeBmp = ;
						
						bd = new BitmapDrawable(BitmapFactory.decodeStream(new FileInputStream(file), null, opts));
						mSwitcher.setImageDrawable(bd);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}

				/**
				 * 显示当前显示的是哪一个位置的图片,例如(1/6)一共有6张图片当前显示是第1张
				 */
				tvImageCount.setText("(" + (1 + position) + "/" + parent.getCount() + ")");
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

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}
	// 菜单
		public HotalkMenuView menuListView = null;

		/**
		 * 系统菜单初始化 void 大树动画 2 查看地图 访前说明 重置  
		 */
		private void initSysMenu() {
			if (menuListView == null) {
				menuListView = new HotalkMenuView(this);
			}
			menuListView.listview.setOnItemClickListener(listClickListener);
			menuListView.clear();
			long mp3coint=ma.dbService.getAllmp3Count(feed.getUuid(), feed.getUserId());
			long mp4coint=ma.dbService.getAllmp4Count(feed.getUuid(), feed.getUserId());
			// 添加按位置添加      ic_menu_mapmode_2   ic_menu_paste_holo_light_2   ic_menu_refresh_2   ic_menu_archive  
			menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, ReviewCommitActivity.this.getString(R.string.read_voice,mp3coint+""), R.drawable.test_zsj33);
			menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, ReviewCommitActivity.this.getString(R.string.read_vedio,mp4coint+""), R.drawable.test_zsj33);
			if (sdImages.getVisibility() == View.GONE) {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "打开图库", R.drawable.ic_menu_crop_2);
			} else {
				menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, "关闭图库", R.drawable.ic_menu_crop_2);
			}
		}

		// 大树动画
		protected void switchSysMenuShow() {
			// 初始化系统菜单
			initSysMenu();
			if (!menuListView.getIsShow()) {
				menuListView.show();
			} else {
				menuListView.close();
			}
		}

		/**
		 * 创建菜单,只在创建时调用一次. 大树动画
		 */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add("menu");// 必须创建一项
			// 注意返回值.
			return super.onCreateOptionsMenu(menu);
		}

		// 大树动画
		@Override
		public boolean onMenuOpened(int featureId, Menu menu) {
			switchSysMenuShow();
			return false;// 返回为true 则显示系统menu
		}

		/**
		 * 菜单点击事件处理 大树动画 1 跳转在这里
		 */
		OnItemClickListener listClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				// 获取key唯一标识
				int key = Integer.parseInt(view.getTag().toString());
				// 跳转
				switch (key) {
				
				case HotalkMenuView.MENU_VIEW_CONTACT:
//					System.out.println("查看地图");
					view.setBackgroundColor(Color.YELLOW);
					Intent it = new Intent(ReviewCommitActivity.this, RecordActivity.class);
					it.putExtra("feed", feed);
					it.putExtra("checkrecord", "3");
					startActivity(it);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
					break;
				case HotalkMenuView.MENU_ADD_CONTACT:
//					System.out.println("访前说明");
					view.setBackgroundColor(Color.YELLOW);
					Intent itmp4 = new Intent(ReviewCommitActivity.this, RecordActivity.class);
					itmp4.putExtra("feed", feed);
					itmp4.putExtra("checkrecord", "4");
					startActivity(itmp4);
					overridePendingTransition(R.anim.zzright, R.anim.zzleft);
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
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.native_left_iv:
			ReviewCommitActivity.this.setResult(3);
			ReviewCommitActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.native_opt:
			super.openOptionsMenu();
			break;
		default:
			break;
		}
	}
}
