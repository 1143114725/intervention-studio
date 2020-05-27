package com.investigate.newsupper.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.investigate.newsupper.R;
import com.investigate.newsupper.util.Publisher;
import com.investigate.newsupper.util.Publisher.Subscriber;
import com.investigate.newsupper.util.Publisher.SubscriberKey;
import com.investigate.newsupper.util.UIUtils;

public class MapActivity extends Activity implements OnClickListener, Subscriber {

    private static final int ACCURACY_CIRCLE_FILLCOLOR = 0xAAFFFF88;
    private static final int ACCURACY_CIRCLE_STROKECOLOR = 0xAA00FF00;
	
	private TextView mShowLocationView;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		Publisher.getInstance().addSubscriber(this);
		findViewById(R.id.map_left_iv).setOnClickListener(this);
		mShowLocationView = (TextView) findViewById(R.id.local_tv);
		mMapView = (MapView) findViewById(R.id.map_mv);
		findViewById(R.id.chooiceDate).setOnClickListener(this);
		mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        BitmapDescriptor maker = BitmapDescriptorFactory.fromResource(R.drawable.mylocationmark);
        mBaiduMap.setMyLocationConfigeration(
        		new MyLocationConfiguration(
        				LocationMode.NORMAL, 
        				true, 
        				maker, 
        				ACCURACY_CIRCLE_FILLCOLOR, 
        				ACCURACY_CIRCLE_STROKECOLOR
        				)
        		);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.map_left_iv:
			finish();
			break;
			
		case R.id.chooiceDate:
			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
					String strMonth = (month + 1) + "";
					String strDay = dayOfMonth + "";
					if (strMonth.length() == 1) {
						strMonth = "0" + strMonth;
					}
					if (strDay.length() == 1) {
						strDay = "0" + strDay;
					}
					// 选择的日期
					String date = year + "-" + strMonth + "-" + strDay;
//					if (!Util.isEmpty(date)) {
//						Message msg = handler.obtainMessage();
//						msg.what = MAP;
//						msg.obj = date;
//						handler.sendMessage(msg);
//					}
				}
			};
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		Publisher.getInstance().removeSubscriber(this);
		super.onDestroy();
	}
	
	@Override
	public void onPublish(int key, Object... data) {
		if (key == SubscriberKey.KEY_LOCATION_UPDATE) {
			if (data != null && data.length >= 1) {
				Object obj = data[0];
				if (obj instanceof BDLocation) {
					final BDLocation location = (BDLocation) obj;
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							refreshMapUI(location);
						}
					});
				}
			}
		}
	}

	private boolean isFirstLoc = true;
	private void refreshMapUI(BDLocation location) {
		if (isFinishing()) {
			return;
		}
		String address = location.getAddrStr();
		if (TextUtils.isEmpty(address)) {
			address = UIUtils.getString(R.string.doing_local);
		}
		mShowLocationView.setText(
				UIUtils.getString(R.string.current_position,address)
				);

		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(18.0f);
			mBaiduMap.animateMapStatus(
					MapStatusUpdateFactory.newMapStatus(builder.build())
					);
		}
	}
	
}
