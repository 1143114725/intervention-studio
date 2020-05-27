package com.investigate.newsupper.service;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.content.Context;


/**
 * 
 * @author baidu
 *
 */
public class LocationService {
	private LocationClient client = null;
	private LocationClientOption option,DIYoption;
	private Object  objLock = new Object();

	/***
	 * 
	 * @param locationContext
	 */
	public LocationService(Context locationContext){
//		synchronized (objLock) {
			if(client == null){
				client = new LocationClient(locationContext);
				client.setLocOption(getDefaultLocationClientOption());
			}
//		}
	}
	
	/***
	 * 
	 * @param listener
	 * @return
	 */
	
	public boolean registerListener(BDAbstractLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			client.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}
	
	public void unregisterListener(BDAbstractLocationListener listener){
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}
	
	/***
	 * 
	 * @param option
	 * @return isSuccessSetOption
	 */
	public boolean setLocationOption(LocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.stop();
			DIYoption = option;
			client.setLocOption(option);
		}
		return isSuccess;
	}
	
	public LocationClientOption getOption(){
		return DIYoption;
	}
	/***
	 * 
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(option == null){
//			mOption = new LocationClientOption();
//			mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
//			mOption.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//		    mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//		    mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
//		    mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死   
			//可选，设置定位模式，默认高精度
			option = new LocationClientOption();
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
		}
		return option;
	}
	
	public void start(){
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.start();
			}
		}
	}
	public void stop(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stop();
			}
		}
	}
	public void restart(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.restart();
			}
			if(client != null && !client.isStarted()){
				client.restart();
			}
		}
	}
//	public boolean requestHotSpotState(){
//		
//		return client.requestHotSpotState();
//		
//	}
	
}
