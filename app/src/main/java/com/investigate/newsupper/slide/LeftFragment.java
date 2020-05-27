package com.investigate.newsupper.slide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.HomeActivity;
import com.investigate.newsupper.activity.KnowleageActivity;
import com.investigate.newsupper.activity.LoginActivity;
import com.investigate.newsupper.activity.LogoutDialogActivity;
import com.investigate.newsupper.activity.SettingActivity;
import com.investigate.newsupper.activity.UploadActivity;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;

/**
 * 左边 布局
 * 
 * @author Administrator
 */
public class LeftFragment extends Fragment {

	private static final String TAG = CenterFagment.class.getSimpleName();
	LinearLayout left_login_all;//登录布局
	RelativeLayout left_home_page;// 左边 碎片 首页 相对布局 首页
	RelativeLayout left_upload; // 上传 布局
	RelativeLayout left_knowleage;// 知识库 布局
	RelativeLayout left_setting; // 设置 布局
	ImageView left_home_iv, left_upload_iv, left_knowleage_iv, left_settting_iv, left_login_iv;
	boolean isHome = true;
	boolean isUpload, isKnow, isSet;
	private String userName; // 大树 用户名
	private Config cfg;// 大树 配置变量
	private RelativeLayout left_login_realayout;// 大树 左侧 登陆布局
	private MyApp ma;
	// IMS兼容隐藏电话
	private UITextView mTitleView,left_upload_tv,left_knowleage_tv,left_setting_tv,username_tv,textView2,textView7,textView8, textView9;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		textView2 = (UITextView) view.findViewById(R.id.textView2);
		//德派软件自主研发的访问专家
		left_login_iv = (ImageView) view.findViewById(R.id.left_login_iv);
		left_home_page = (RelativeLayout) view.findViewById(R.id.left_home_page);
		left_home_iv = (ImageView) view.findViewById(R.id.left_home_iv);
		//left_home_tV = (TextView) view.findViewById(R.id.left_home_tV);
		mTitleView = (UITextView) view.findViewById(R.id.left_home_tV);
		// 首页
		left_upload = (RelativeLayout) view.findViewById(R.id.left_upload);
		left_upload_iv = (ImageView) view.findViewById(R.id.left_upload_iv);
		left_upload_tv = (UITextView) view.findViewById(R.id.left_upload_tv);
		// 上传
		left_knowleage = (RelativeLayout) view.findViewById(R.id.left_knowleage);
		left_knowleage_iv = (ImageView) view.findViewById(R.id.left_knowleage_iv);
		left_knowleage_tv = (UITextView) view.findViewById(R.id.left_knowleage_tv);
		// 知识库
		left_setting = (RelativeLayout) view.findViewById(R.id.left_setting);
		left_settting_iv = (ImageView) view.findViewById(R.id.left_setting_iv);
		left_setting_tv = (UITextView) view.findViewById(R.id.left_setting_tv);
		// 设置
		// 大树 用户名的设置 左侧布局
		username_tv = (UITextView) view.findViewById(R.id.user_name_tv);
		left_login_realayout = (RelativeLayout) view.findViewById(R.id.left_login_realayout);
		left_login_all=(LinearLayout) view.findViewById(R.id.left_login_all);
		ma = (MyApp) this.getActivity().getApplication();

		// IMS兼容
		textView7 = (UITextView) view.findViewById(R.id.textView7);
		textView8 = (UITextView) view.findViewById(R.id.textView8);
		textView9 = (UITextView) view.findViewById(R.id.textView9);
		
		TextSizeManager.getInstance()
			.addTextComponent(TAG, textView2)
			.addTextComponent(TAG, mTitleView)
			.addTextComponent(TAG, left_upload_tv)
			.addTextComponent(TAG, left_knowleage_tv)
			.addTextComponent(TAG, left_setting_tv)
			.addTextComponent(TAG, username_tv)
			.addTextComponent(TAG, textView7)
			.addTextComponent(TAG, textView8)
			.addTextComponent(TAG, textView9);
		
		if (3 == Cnt.appVersion) {
			textView7.setVisibility(View.GONE);
			textView8.setVisibility(View.GONE);
			textView9.setText("010-85674339");
		}
		//北京农业银行兼容
		else if(5 == Cnt.appVersion){
			textView7.setVisibility(View.GONE);
			textView8.setVisibility(View.GONE);
			textView9.setText("010-85106195");
		}
		return view;
	}

	@Override
	public void onResume() {
		// 大树 设置 用户名
		if (!Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))) {
			// System.out.println(cfg.getString(Cnt.USER_ID, ""));
			username_tv.setText(cfg.getString(Cnt.USER_ID, ""));
		} else {
			// System.out.println(this.getResources().getString(R.string.user_name));
			username_tv.setText(R.string.user_name);
		}
		// 大树 设置 以上 部分
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		TextSizeManager.getInstance().removeTextComponent(TAG);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		// 大树 修改 一下 登陆 界面 跳转 login 不用 图片跳转 ，用布局 做跳转 这样体验好
		cfg = new Config(getActivity().getApplicationContext());
		left_login_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Util.isEmpty(ma.userPwd)) {
					// 密码不为空弹注销
					Intent intent = new Intent(LeftFragment.this.getActivity(), LogoutDialogActivity.class);
					startActivity(intent);
				} else {
					// 为空弹登录
					((HomeActivity) getActivity()).skipActivity(LoginActivity.class);
				}
			}
		});

		left_home_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 如果 没有 选中 那么 把 图片 字体 变成 天蓝色
				if (!isHome) {
					left_home_iv.setImageResource(R.drawable.icon_star_click);
					int color = getResources().getColor(R.color.sky_blue);
					mTitleView.setTextColor(color);
					// 其他 布局 都变成 灰色 不显示
					left_upload_iv.setImageResource(R.drawable.icon_upload);
					int color1 = getResources().getColor(R.color.gray);
					left_upload_tv.setTextColor(color1);
					left_knowleage_iv.setImageResource(R.drawable.icon_knowledge);
					left_knowleage_tv.setTextColor(color1);
					left_settting_iv.setImageResource(R.drawable.icon_setting);
					left_setting_tv.setTextColor(color1);
				}
				isHome = true;
				isKnow = false;
				isSet = false;
				isUpload = false;
				((HomeActivity) getActivity()).showLeft();
			}
		});

		left_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 流程
				if (!isUpload) {
					left_upload_iv.setImageResource(R.drawable.icon_upload_click);
					int color = getResources().getColor(R.color.sky_blue);
					left_upload_tv.setTextColor(color);
					// 其他 布局 都 不显示
					left_home_iv.setImageResource(R.drawable.icon_star);
					int color1 = getResources().getColor(R.color.gray);
					mTitleView.setTextColor(color1);
					left_knowleage_iv.setImageResource(R.drawable.icon_knowledge);
					left_knowleage_tv.setTextColor(color1);
					left_settting_iv.setImageResource(R.drawable.icon_setting);
					left_setting_tv.setTextColor(color1);
				}
				isUpload = true;
				isHome = false;
				isSet = false;
				isKnow = false;
				if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)) {
					((HomeActivity) getActivity()).skipActivity(UploadActivity.class);
				} else if (!NetUtil.checkNet(ma)) {
					// 没网情况下
					Toasts.makeText(ma, R.string.exp_net, Toast.LENGTH_LONG).show();
				} else if (Util.isEmpty(ma.userPwd)) {
					// 用户名为空
					Toasts.makeText(ma, R.string.no_login, Toast.LENGTH_LONG).show();
					((HomeActivity) getActivity()).skipActivity(LoginActivity.class,20);
				}
			}
		});

		left_knowleage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 流程
				if (!isKnow) {
					left_knowleage_iv.setImageResource(R.drawable.icon_knowledge_click);
					int color = getResources().getColor(R.color.sky_blue);
					left_knowleage_tv.setTextColor(color);
					// 其他 布局 变暗
					left_home_iv.setImageResource(R.drawable.icon_star);
					int color1 = getResources().getColor(R.color.gray);
					mTitleView.setTextColor(color1);
					left_settting_iv.setImageResource(R.drawable.icon_setting);
					left_setting_tv.setTextColor(color1);
					left_upload_iv.setImageResource(R.drawable.icon_upload);
					left_upload_tv.setTextColor(color1);
				}
				isKnow = true;
				isUpload = false;
				isSet = false;
				isHome = false;
				((HomeActivity) getActivity()).skipActivity(KnowleageActivity.class);
			}
		});

		left_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isSet) {
					left_settting_iv.setImageResource(R.drawable.icon_setting_click);
					int color = getResources().getColor(R.color.sky_blue);
					left_setting_tv.setTextColor(color);
					// 其他布局 变暗
					left_home_iv.setImageResource(R.drawable.icon_star);
					int color1 = getResources().getColor(R.color.gray);
					mTitleView.setTextColor(color1);
					left_knowleage_iv.setImageResource(R.drawable.icon_knowledge);
					left_knowleage_tv.setTextColor(color1);
					left_upload_iv.setImageResource(R.drawable.icon_upload);
					left_upload_tv.setTextColor(color1);
				}
				isSet = true;
				isUpload = false;
				isKnow = false;
				isHome = false;
				((HomeActivity) getActivity()).skipActivity(SettingActivity.class);
			}
		});

		left_home_page.setClickable(false);
		left_setting.setClickable(false);
		left_knowleage.setClickable(false);
		left_upload.setClickable(false);
		left_login_all.setClickable(false);
		super.onActivityCreated(savedInstanceState);
	}

	public void onChange(boolean isClick) {
		left_home_page.setClickable(isClick);
		left_setting.setClickable(isClick);
		left_knowleage.setClickable(isClick);
		left_upload.setClickable(isClick);
		left_login_all.setClickable(isClick);
	}

}
