package com.investigate.newsupper.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class CustomArrayAdapter {

/**
 * 设置img 发送异步请求
 * @param imageView
 * @param path
 */
	public void showImages(ImageView imageView, String path){
		LoadImageTask imageTask = new LoadImageTask(imageView, path);
		imageTask.execute();
	}
	/**
	 * 异步操作
	 * @author Administrator
	 *
	 */
	public final class LoadImageTask extends AsyncTask<Void, Integer, Bitmap>{
		ImageView imageView = null;
		String imagePath = null;
		
		public LoadImageTask(ImageView _imageView, String _imagePath){
			this.imageView = _imageView;
			this.imagePath = _imagePath;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				return fitSizePic(new File(this.imagePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {

			if(null != result){
					imageView.setImageBitmap(result);
			}
			
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
	
	/**
	 * 图片压缩
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap fitSizePic(File file) throws FileNotFoundException {
		
		//File file = new File(path);
		// Bitmap bitMap = Util.fitSizePic(file);
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 数字越大读出的图片占用的heap越小 不然总是溢出
		long len = file.length();
		if (1048576 > len) { //小于1024k
			opts.inSampleSize = 8;
		} else{
			opts.inSampleSize = 10;
		}
		//resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		resizeBmp = BitmapFactory.decodeStream(new FileInputStream(file), null, opts);
		return resizeBmp;
	}
	
}
