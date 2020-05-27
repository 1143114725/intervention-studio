package com.investigate.newsupper.BaiDuBos;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.callback.BosProgressCallback;
import com.baidubce.services.bos.model.CreateBucketResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectRequest;
import com.baidubce.util.BLog;

import java.io.File;

/**
 * Created by EEH on 2018/6/25.
 */
public class Bos {
	private static final String TAG = "Bos";
	private static String filetype;

	// 是否上传成功了
	static boolean isupload = false;

	/**
	 * 
	 * @param AccessKeyID
	 *            AK
	 * @param SecretAccessKey
	 *            SK
	 * @param EndPoint
	 *            EndPoint
	 * @param BucketName
	 *            BuckName
	 * @param file
	 *            需要上传的文件
	 * @param Objectkey
	 *            文件名带后缀
	 * @param ContentType
	 *            文件格式
	 */
	public static boolean sendbaidubos(String AccessKeyID,
			String SecretAccessKey, String EndPoint, final String BucketName,
			final File file, final String Objectkey, int ContentType) {

		if (ContentType == 2) {
			filetype = "jpg";
		} else if (ContentType == 3) {
			filetype = "amr";
		}

		// 打开bossdk运行时log
		BLog.enableLog();
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(AccessKeyID,
				SecretAccessKey));
		config.setEndpoint(EndPoint); // Bucket所在区域
		final BosClient client = new BosClient(config);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					CreateBucketResponse response = client
							.createBucket(BucketName); // 新建一个Bucket并指定Bucket名称
					PutObjectRequest request = new PutObjectRequest(BucketName,
							Objectkey, file);
					ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.setContentType(filetype);
					request.setObjectMetadata(objectMetadata);
					request.setProgressCallback(new BosProgressCallback<PutObjectRequest>() {
						@Override
						public void onProgress(PutObjectRequest request,
								long currentSize, long totalSize) {
							Log.e(currentSize + "", totalSize + "");
							if (currentSize == totalSize) {
								isupload = true;
							}
						}
					});
					String eTag = client.putObject(request).getETag();
					Log.i(TAG, "run: eTag" + eTag);

				} catch (BceServiceException e) {
					isupload = false;
					System.out.println(TAG + "Error ErrorCode: "
							+ e.getErrorCode());
					System.out.println(TAG + "Error RequestId: "
							+ e.getRequestId());
					System.out.println(TAG + "Error StatusCode: "
							+ e.getStatusCode());
					System.out.println(TAG + "Error Message: " + e.getMessage());
					System.out.println(TAG + "Error ErrorType: "
							+ e.getErrorType());
				} catch (BceClientException e) {
					isupload = false;
					System.out.println(TAG + "Error Message: " + e.getMessage());
				}
			}
		}).start();

		return isupload;
	}
}