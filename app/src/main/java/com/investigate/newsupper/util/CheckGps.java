package com.investigate.newsupper.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.investigate.newsupper.R;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.service.NewLocationService;
import com.investigate.newsupper.util.locationutils.Gps;
import com.investigate.newsupper.view.CustomProgressDialog;

/**
 * 检查GPS
 * 
 * @author EraJie
 * 
 */
public class CheckGps {
	// 流程进度条
	private CustomProgressDialog progressDialog = null;
	/**
	 * 唯一单例模式
	 * 
	 * @return
	 */
	private static CheckGps mInstance;

	public synchronized static CheckGps getInstance() {

		if (mInstance == null) {
			mInstance = new CheckGps();
		}
		return mInstance;
	}

	
    /**
     * 开启新定位的服务（仅有开启，关闭在服务里关闭）
     */
    public void startlocation(String feeduuid,MyApp ma){
    	System.out.println("开始定位 feeduuid"+feeduuid);
    	
    	Intent locationintent = new Intent(ma, NewLocationService.class);
		Bundle bundle = new Bundle();
		bundle.putString("uuid", feeduuid);
		locationintent.putExtras(bundle);
		ma.startService(locationintent);
		
    }
	
	
	
	/**
	 * 检查数据库是否有定位点
	 * @return true:有定位，false：没有定位
	 */
	public boolean checkGPSDB(MyApp ma,String filename) {
		boolean isGpsDB = false;
		//检查数据库是否有定位点
		Gps gpsbean = ma.dbService.queryPos(filename);
		if (!Util.isEmpty(gpsbean.getLatitude()+"") && !(gpsbean.getLatitude() == 0)) {
			isGpsDB = true;
		}
		return isGpsDB;
	}
	
	
	
	

	//启动等待动画
	public void startProgressDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(context);
			progressDialog.setMessage(context.getResources()
					.getString(R.string.toast_gps));
			progressDialog.setCancelable(false);
			progressDialog.findViewById(R.id.loadingImageView)
					.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							stopProgressDialog();
							return true;
						}

					});
		}
		progressDialog.show();
	}
	//关闭动画
	public void stopProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	}

}
