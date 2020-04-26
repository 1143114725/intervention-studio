package com.investigate.newsupper.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.service.LocationService;
import com.investigate.newsupper.view.CustomProgressDialog;

public class AcquireLocation {
	
	private static final String TAG = "AcquireLocation";

	Context context;
	  /**
     * 唯一单例模式
     * @return
     */
    private static AcquireLocation mInstance;

    public synchronized static AcquireLocation getInstance() {

        if (mInstance == null) {
            mInstance = new AcquireLocation();
        }
        return mInstance;
    }
    
	private LocationService locationServices;
	private MyLocationListener myListener = new MyLocationListener();
	 /**
    * 初始化定位
    */
	 public void initlocation(LocationService locationServices,Context context,BDAbstractLocationListener myListener) {
	this.context = context;
       this.locationServices = locationServices;
       //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
       locationServices.registerListener(myListener);
       //注册监听
       locationServices.setLocationOption(locationServices.getDefaultLocationClientOption());
       //开始定位
       startloc();
       startProgressDialog();
//       locationServices.start();

   }

	/**
    * 开始定位
    */
   public void startloc() {
   	 Log.i(TAG, "startloc");
       locationServices.start();
   }
   /**
    * 结束定位
    */
   public void stoplocation(){
		 Log.i(TAG, "stoplocation");
		 locationServices.stop();
		 stopProgressDialog();
	}
   public double lat;
   public double lng;
   public String Addr;
	 public class MyLocationListener extends BDAbstractLocationListener {

			@SuppressWarnings("unused")
			@Override
	        public void onReceiveLocation(BDLocation location) {
	            String coorType = location.getCoorType();
	            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
	            int errorCode = location.getLocType();
	            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
	            String latLongString;
	            if ( location != null ) {
	                 lat = location.getLatitude();
	                 lng = location.getLongitude();
	                 Addr = location.getAddrStr();
	                latLongString = "纬度:" + lat + "经度:" + lng+"coorType"+coorType+"errorCode"+errorCode;
	                //存数据库
	                Log.i(TAG, "lat:"+lat);
	                Log.i(TAG, "lng:"+lng);
	                Log.i(TAG, "Addr:"+Addr);
	                Log.i(TAG, "isupdate:"+latLongString);
	                //关闭定位服务
	                stoplocation();
	                Log.i(TAG, "关闭定位服务");
	            } else {
	            	stoplocation();
	                latLongString = "无法获取地理信息"+errorCode;
	            }

	            Log.i(TAG, "updateWithNewLocation:您当前的位置是:" +
	                    latLongString);
	            

	        }
	    }

	 /**
	  * 检查定位点
	  */
	 private boolean verifuLoc(String str){
		 if (Util.isEmpty(str)) {
			 return false;
		}else  if (-1 == str.indexOf("E")) {
			//没有E
			 return false;
		}else{
			//有E
			return true;
		}
	 }
	 
	

	Handler handlerloc = new Handler();  
    
    int locationtime = 0;
//    /**
//     * 循环定位
//     */
//    Runnable startlocationrun = new Runnable() {  
//        public void run() {  
//            
//       	 if (lat == 0.0) {
//       		 stoplocation();
//       		 startloc();
//       		 handlerloc.postDelayed(startlocationrun, 1000);  
//			}else if (lat != 0.0) {
//				stopProgressDialog();
//			}else {
//				if (locationtime == 30) {
//					stopProgressDialog();
//				}
//				locationtime++;
//				handlerloc.postDelayed(startlocationrun, 1000);  
//			}
//        }  
//    };  

	// 大树 重置 功能 5 滚动条的生成
	public volatile ProgressDialog mPd;

	// 定位进度条
		private CustomProgressDialog progressDialog = null;
		//启动定位等待动画
		public void startProgressDialog() {
			if (progressDialog == null) {
				progressDialog = CustomProgressDialog
						.createDialog(context);
				progressDialog.setMessage(context.getResources()
						.getString(R.string.start_get_loc));
				progressDialog.setCancelable(false);
				progressDialog.findViewById(R.id.loadingImageView)
						.setOnLongClickListener(new DismissListener());
			}
			progressDialog.show();
			
//			handlerloc.post(startlocationrun); 
		}
		//定位dialog的长按事件
		private final class DismissListener implements OnLongClickListener {

			@Override
			public boolean onLongClick(View v) {
//				if (null != task) {
//					task.cancel();
//					task = null;
//				}
				stopProgressDialog();
				return true;
			}

		};
		//关闭定位动画
		private void stopProgressDialog() {
			if (progressDialog != null) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
					

//				    //停止定位
//						stoplocation();
//					if (lat != 0.0) {
//						creatLocDialog(Addr, lat+"", lng+"");
//						newbtnClickable(true); 
//					}else {
//						//按钮可操作
//						creatLocDialog(Addr, "", "");
//						newbtnClickable(true);
//					}
					
				}
			}
//			newbtnClickable(true);
		}
		
		

}
