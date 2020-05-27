package com.investigate.newsupper.service.recordserver;

import java.io.File;
import java.io.IOException;

import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.FileUtil;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

/**
 * Created by EEH on 2018/7/16.
 */
public class MediaRecorderManager {
    private static MediaRecorderManager mInstance;
    //默认文件路径和文件名
    public  String filepath = "DapSurvey", filename = "record.amr";
    private boolean isRecord = false;
    private MediaRecorder mMediaRecorder;
    private MediaRecorderManager() {}


    /**
     * 唯一单例模式
     * @return
     */
    public synchronized static MediaRecorderManager getInstance() {
        if (mInstance == null)
            mInstance = new MediaRecorderManager();
        return mInstance;
    }

    //开启录音
    public String startRecordAndFile() {
    	stopRecordAndFile();
        //判断是否有外部存储设备sdcard
        if (FileUtil.isSdcardExit()) {
            if (isRecord) {
                return RecordUtils.E_STATE_RECODING;
            } else {
                if (mMediaRecorder == null)
                    createMediaRecord();
                try {
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    // 让录制状态为true
                    isRecord = true;
                    return RecordUtils.SUCCESS;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return RecordUtils.E_UNKOWN;
                }
            }

        } else {
            return RecordUtils.E_NOSDCARD;
        }
    }


    /**
     * MediaRecord 的一些设置
     */
    private void createMediaRecord() {
        /* ①Initial：实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();

        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置麦克风

        /* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

        /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        /* 设置输出文件的路径 */
        File file = new File(filepath + File.separator + filename);
        //创建下文件
        FileUtil.createNewFile(file);
        //录音转文件
        mMediaRecorder.setOutputFile(filepath + File.separator + filename);
    }

    /**
     * 关闭录音
     */
    public void stopRecordAndFile() {
        if (mMediaRecorder != null) {
            System.out.println("stopRecord");
            isRecord = false;
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            
            
            
//            BaseToast.showShortToast(context,"录音已关闭！");
        }
    }


    /**
     * 获取完整文件路径(没用到，测试的时候用的)
     * @param filepath
     * @param filename
     * @return
     */
    private String getFilepath(String filepath, String filename) {
        String mAudioAMRPath = "";
        if (FileUtil.isSdcardExit()) {
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioAMRPath = fileBasePath + File.separator + filepath + File.separator + filename;
        }
        return mAudioAMRPath;
    }

    /**
     * 设置修改文件路径以及文件名
     * @param filepath
     * @param filename
     */
    public void setFilePuth(String filepath, String filename){
        this.filepath = filepath;
        this.filename = filename;
    }


}
