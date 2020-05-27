package com.investigate.newsupper.service;

import java.util.UUID;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.investigate.newsupper.bean.UserPosition;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.ComUtil;

/**
 * 内嵌程序里的定位功能
 * 
 * @author Administrator
 * 
 */
public class MyLocation extends Service implements BDLocationListener {
	private static final String TAG = MyLocation.class.getSimpleName();
	// 定位相关
	LocationClient mLocClient;
	private static MyApp ma;
	public static BDLocation location;
	private static String uuid;
	private static String points = "";
	private static String times = "";
	String issustain;

    
	@Override
	public void onCreate() {
		
		super.onCreate();
	}

    private static LocationService locationServices;
    private MyLocationListener myListener = new MyLocationListener();
	 /**
     * 初始化定位
     */
    private void initlocation() {
    	
        locationServices = ((MyApp)getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationServices.registerListener(myListener);
        //注册监听
        locationServices.setLocationOption(locationServices.getDefaultLocationClientOption());

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	uuid = newUUIDInstance(intent);
    	
		Log.i(TAG, "传入的uuid是: "+uuid);
		ma = (MyApp) getApplication();
		if (locationServices == null) {
			initlocation();
			Log.i(TAG, "locationServices=null");
		}
	        
	 
	        startloc();
        return START_STICKY;
    }

    private int recLen = 0;

    /**
     * 开始定位
     */
    private void startloc() {
        locationServices.start();
    }

    @Override
    public void onDestroy() {
        locationServices.stop();
        Log.i(TAG, "onDestroy: MyService");
        
        Intent intent = new Intent(ma, MyLocation.class);
		Bundle bundle = new Bundle();
		bundle.putString("uuid", uuid);
		intent.putExtras(bundle);// 定位间隔
		ma.startService(intent);
        
        startService(intent);
        super.onDestroy();
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
            
			long ldate = System.currentTimeMillis();
			String time = ComUtil.getTime(ldate, 0);
			times = (times == null) ? "" : times + time + ";";
			points = (points == null) ? "" : points
					+ location.getLatitude() + ","
					+ location.getLongitude() + ";";
			System.out.println("points=" + points);
			System.out.println("times=" + times);
			boolean isupdate = ma.dbService.updatePosition(uuid, points, times);
			if (isupdate) {
			Log.i(TAG, "updateWithNewLocation: 插入成功");	
			}else {
				Log.i(TAG, "updateWithNewLocation: 插入失败");
		 	}
			
            if ( location != null ) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latLongString = "纬度:" + lat + "经度:" + lng+"coorType"+coorType+"errorCode"+errorCode;
            } else {
                latLongString = "无法获取地理信息"+errorCode;
            }

            Log.i(TAG, "updateWithNewLocation: "+recLen+"您当前的位置是:" +
                    latLongString);

        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 *获取定位点信息
	 * @return
	 */
	public static BDLocation getLocation(){
		if (location !=null) {
			return location;	
		}else{
			return null;
		}
	}
	
	
	
	public String newUUIDInstance(Intent intent) {
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			String uuids = bundle.getString("uuid");
	        if (uuids != null) {
	        	return uuids;
	        } else {
	        	return uuid;
	        }
		}else {
			return null;
		}
		
		
    }
	public static void stoplocation(){
		 Log.i(TAG, "stoplocation");
		 locationServices.stop();
	}
}
