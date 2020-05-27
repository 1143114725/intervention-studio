package com.investigate.newsupper.service;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.service.MyLocation.MyLocationListener;
import com.investigate.newsupper.util.CheckGps;
import com.investigate.newsupper.util.ComUtil;
import com.investigate.newsupper.util.Util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class NewLocationService extends Service{
	private static final String TAG = "NewLocationService";
	
    private static LocationService locationServices;
    private MyLocationListener myListener = new MyLocationListener();
    private String uuid;
    
    private static MyApp ma;
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
	}
	
//	进入的主方法gua'duan
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null ) {
			if (!Util.isEmpty(intent.getStringExtra("uuid"))) {
				uuid = intent.getStringExtra("uuid");
			}
			System.out.println("进入新定位服务 uuid:"+uuid);
			ma = (MyApp) getApplication();
			initlocation();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		CheckGps.getInstance().stopProgressDialog();
		stoplocation();
		super.onDestroy();
	}
	 /**
     * 初始化定位
     */
    private void initlocation() {
    	
        locationServices = ((MyApp)getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationServices.registerListener(myListener);
        //注册监听
        locationServices.setLocationOption(locationServices.getDefaultLocationClientOption());
        //开始定位
        startloc();

    }
    
	 /**
     * 开始定位
     */
    private void startloc() {
    	 Log.i(TAG, "startloc");
        locationServices.start();
    }
    /**
     * 结束定位
     */
    private void stoplocation(){
		 Log.i(TAG, "stoplocation");
		 locationServices.stop();
	}
	
	 public class MyLocationListener extends BDAbstractLocationListener {

			@SuppressWarnings("unused")
			@Override
	        public void onReceiveLocation(BDLocation location) {
	        	MyLocation.location = location;
	            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
	            //以下只列举部分获取经纬度相关（常用）的结果信息
	            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
	            String coorType = location.getCoorType();
	            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
	            int errorCode = location.getLocType();
	            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
	            String latLongString;

	            
//				这里修改数据库
	            if ( location != null ) {
	                double lat = location.getLatitude();
	                double lng = location.getLongitude();
	                String Addr = location.getAddrStr();
	                latLongString = "纬度:" + lat + "经度:" + lng+"coorType"+coorType+"errorCode"+errorCode;
	                //存数据库
	                Log.i(TAG, "filename:"+uuid);
	                Log.i(TAG, "lat:"+lat);
	                Log.i(TAG, "lng:"+lng);
	                Log.i(TAG, "Addr:"+Addr);
	                boolean isupdate = ma.dbService.updatePos(uuid, lat, lng, Addr);
	                
	                Log.i(TAG, "isupdate:"+isupdate);
	                
//	                if (isupdate) {
	                	 //关闭定位服务
	                	Log.i(TAG, "关闭定位服务");
		                NewLocationService.this.onDestroy();
//					}
	            } else {
	                latLongString = "无法获取地理信息"+errorCode;
	            }

	            Log.i(TAG, "updateWithNewLocation:您当前的位置是:" +
	                    latLongString);
	            

	        }
	    }

}
