package com.investigate.newsupper.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public final class FileUtil {
	
	private static final String APP_STORAGE_FILE_PATH = "/DapSurvey";
	private static final String SDPATH = Environment.getExternalStorageDirectory() + APP_STORAGE_FILE_PATH;
	
	private FileUtil() {
	}
	
	public static final String getAppExternalStorageDirectory() {
		File f = new File(SDPATH);
		if (!f.exists()) {
			f.mkdir();
		}
		return SDPATH;
	}
	
	public static String getFileSimpleName(String filename) {
		if (TextUtils.isEmpty(filename)) {
			return "";
		}
		final int lastDotIndex = filename.indexOf(".");
		if (lastDotIndex >= 0) {
			return filename.substring(0, lastDotIndex);
		}
		return "";
		
	}
    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public static File createNewFile(File file) {

        try {

            if (file.exists()) {
                return file;
            }

            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            Log.e("FileUtil", "", e);
            return null;
        }
        return file;
    }
    /**
     * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    // 获取SD卡的根目录
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }
}
