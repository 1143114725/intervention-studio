package com.investigate.newsupper.service.recordserver;

import java.io.File;

import com.investigate.newsupper.activity.NoViewAcitvity;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseToast;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

/**
 * 录音管理工具
 * Created by EEH on 2018/7/19.
 */
public class RecordUtils {
    //通知的id号
    public final static int NotificationID = 556854;
    //返回值
    public final static String SUCCESS = "成功！";
    public final static String E_NOSDCARD = "没有SD卡，无法存储录音数据！";
    public final static String E_STATE_RECODING = "正在录音中，请先停止录音！";
    public final static String E_UNKOWN = "无法识别的错误！";
    private static Intent serviceintent;
    MyApp ma;
    String filepath;
    String filename;

    /**
     * 唯一单例模式
     *
     * @return
     */
    private static RecordUtils mInstance;

    public synchronized static RecordUtils getInstance() {
        if (mInstance == null) {
            mInstance = new RecordUtils();
        }
        return mInstance;
    }

    /**
     * 开启录音操作
     * @param mActivity
     * @param filepath
     * @param filename
     */
    public void startCallRecord(Context mActivity,String filepath, String filename, MyApp ma){
        MediaRecorderManager.getInstance().setFilePuth(filepath,filename);
        this.ma = ma;
        this.filepath = filepath;
        this.filename = filename;
        String result = MediaRecorderManager.getInstance().startRecordAndFile();

        if (result.equals(SUCCESS)){
            BaseToast.showShortToast(mActivity,SUCCESS);
            startservice(mActivity);

        }else if (result.equals(E_NOSDCARD)){
            BaseToast.showShortToast(mActivity,E_NOSDCARD);
        }else if (result.equals(E_STATE_RECODING)){
            BaseToast.showShortToast(mActivity,E_STATE_RECODING);
        }else if (result.equals(E_UNKOWN)){
            BaseToast.showShortToast(mActivity,E_UNKOWN);
        }
        
        
        

    }


    /**
     * 开启更新通知的服务
     * @param mActivity
     */
    private void startservice(Context mActivity){
        serviceintent = new Intent(mActivity, CallListenerService.class);
//        serviceintent.putExtra("type", "start");
        mActivity.startService(serviceintent);
    }


    /**
     * 结束录音  把服务关掉，通知栏清了
     * @param mActivity
     */
    public void stopRecord(Context mActivity){
        MediaRecorderManager.getInstance().stopRecordAndFile();
        if (serviceintent == null) {
        	serviceintent = new Intent(mActivity, CallListenerService.class);
        	
		}
//        serviceintent.putExtra("type", "stop");
        mActivity.stopService(serviceintent);
        NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(RecordUtils.NotificationID);
        System.out.println("结束录音  把服务关掉，通知栏清了");
        
        
        if (filepath != null) {
        	System.out.println("file ::" + filepath + File.separator + filename);
            System.out.println("time ::" + filepath + System.currentTimeMillis());
            File file = new File(filepath + File.separator + filename);
            System.out.println("file.lenght ::" + filepath + file.length());
            if (ma !=null) {
            	ma.dbService.updateRecord(filename,
        				System.currentTimeMillis(), file.length());
			}
		}
        
        

    }
}
