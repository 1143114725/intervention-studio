package com.investigate.newsupper.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;

/**
 * 获取定位点
 * 
 * @author EraJi
 * 
 */
public class LocationUtil {

	static LocationClient mLocClient;

	public static void setLocation(BDLocation location) {
		LocationUtil.location = location;
	}

	static BDLocation location;

	/**
	 * 初始化定位
	 * 
	 * @param c
	 */
	public static void CreateLocation(Context c) {

		mLocClient = new LocationClient(c.getApplicationContext());
		mLocClient.setLocOption(getLocationClientOption());
		mLocClient.registerLocationListener(bdlocationlistener);
		mLocClient.start();

	}

	// 设置地图属性
	public static LocationClientOption getLocationClientOption() {
		LocationClientOption option = new LocationClientOption();
		//可选，设置定位模式，默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
		//可选，设置发起定位请求的间隔，int类型，单位ms
		//如果设置为0，则代表单次定位，即仅定位一次，默认为0
		//如果设置非0，需设置1000ms以上才有效
      //可选，设置是否使用gps，默认false
        option.setOpenGps(true);
      //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
      //可选，定位SDK内部是一个service，并放到了独立进程。
      //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(true);
      //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);
      //可选，7.2版本新增能力
      //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);
      //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);
     // 位置，一定要设置，否则后面得不到地址
        option.setIsNeedAddress(true);
		return option;
	}

	// 回掉函数
	static BDLocationListener bdlocationlistener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			setLocation(location);
		}
	};

	/**
	 * 获取定位点信息
	 * 
	 * @return
	 */
	public static BDLocation getLocation() {
		return location;
	}

	
	public static void stoplocation() {
		if (mLocClient !=null) {
			mLocClient.stop();
			mLocClient = null;
			location = null;
		}
		
	}
	static int dialogBtnSize = (int) (UIUtils
			.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager.getInstance().getRealScale());

	/**
	 * 录音
	 * 定位的判断逻辑
	 */
	public static void isforceGPS(BDLocation location, Survey survey,
			Context context, final ImPlement imp,MyApp ma) {
		
		isrecord(survey, context, ma);
		
		if (location.getLocType() == 161) {
			
			Log.e("isforceGPS if", "getLocType = "+location.getLocType());
			imp.implement();
		}else {
			Log.e("isforceGPS else", "getLocType = "+location.getLocType());
			// 判断定位是否强制
			if (1 == survey.forceGPS) {
				// 强制定位 没有定位信息 直接跳出
				AlertDialog.Builder locBuilder = new AlertDialog.Builder(
						context, AlertDialog.THEME_HOLO_LIGHT);
				locBuilder
				// .setTitle("提示")
				// .setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("尚未获取到定位信息，请稍后重试").setPositiveButton(
								context.getResources().getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				AlertDialog create = locBuilder.create();
				create.show();
				TextView msgTv = ((TextView) create
						.findViewById(android.R.id.message));
				msgTv.setMinLines(2);
				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				msgTv.setGravity(Gravity.CENTER_VERTICAL);
				create.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(
						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				Button okBtn = create.getButton(AlertDialog.BUTTON_POSITIVE);
				okBtn.setBackgroundColor(0xFF6751B6);
				okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				okBtn.setTextColor(Color.WHITE);
			} else {
				// 非强制定位 没有定位信息也可以继续新建问卷
				AlertDialog.Builder locBuilder = new AlertDialog.Builder(
						context, AlertDialog.THEME_HOLO_LIGHT);
				locBuilder
						// .setTitle("提示")
						// .setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("尚未获取到定位信息，是否继续？")
						.setPositiveButton(
								context.getResources().getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										imp.implement();
										dialog.dismiss();
										return;
									}
								})

						.setNegativeButton(
								context.getResources().getString(
										R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				AlertDialog create = locBuilder.create();
				create.show();
				TextView msgTv = ((TextView) create
						.findViewById(android.R.id.message));
				msgTv.setMinLines(2);
				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				msgTv.setGravity(Gravity.CENTER_VERTICAL);
				create.getButton(create.BUTTON_NEGATIVE).setTextSize(
						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				create.getButton(create.BUTTON_POSITIVE).setTextSize(
						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				Button okBtn = create.getButton(create.BUTTON_POSITIVE);
				okBtn.setBackgroundColor(0xFF6751B6);
				okBtn.setTextColor(Color.WHITE);
			}
		}
	}
	
	
	
	
	public static void isrecord(Survey survey,Context context,MyApp ma){
		if (survey.globalRecord == 1) {//是否有全局录音
			if (!CheckAudioPermission.isHasPermission(ma)) {//启用录音失败
				AlertDialog.Builder Recorddialog = new AlertDialog.Builder(
						context, AlertDialog.THEME_HOLO_LIGHT);
				Recorddialog
//						.setTitle("提示")
//						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("录音启用失败，请检查录音权限！")
						.setPositiveButton(
								context.getResources().getString(
										R.string.ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										// 打开系统设置界面
//										VisitActivity.this
//												.startActivity(new Intent(
//														Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));

										return;
									}

								});

				AlertDialog Recordcreate = Recorddialog.create();
				Recordcreate.show();
				TextView msgTv=((TextView) Recordcreate.findViewById(android.R.id.message));
				msgTv.setMinLines(2);
				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				msgTv.setGravity(Gravity.CENTER_VERTICAL);
				Recordcreate.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(
						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				Button okBtn = Recordcreate.getButton(AlertDialog.BUTTON_POSITIVE);
				okBtn.setBackgroundColor(0xFF6751B6);
				okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				okBtn.setTextColor(Color.WHITE);

			} 
//			else {
//				 //继续判断是否有定位
//				isforceGPS();
//			}

		} 
//		else {
//			 //继续判断是否有定位
//			isforceGPS();
//		}
	}
	
	
	
	
	
/**
 * 接口
 * @author EraJi
 *
 */
	public interface ImPlement {
		public abstract void implement();
	}
}
