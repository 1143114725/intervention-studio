package com.investigate.newsupper.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.investigate.newsupper.R;
import com.investigate.newsupper.view.CustomProgressDialog;

/**
 * Created by EraJieZhang
 * Fragment基类
 */
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected Activity mActivity;
    /**
     *  Fragment与Activity已经完成绑定，该方法有一个Activity类型的参数，代表绑定的Activity,防止内存泄露
     */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        this.mActivity = (Activity)context;
    }

    /**
     * 初始化Fragment。可通过参数savedInstanceState获取之前保存的值。
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }
    /**
     * 初始化Fragment的布局。加载布局和findViewById的操作通常在此函数内完成，但是不建议执行耗时的操
     * 作，比如读取数据库数据列表。
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    /**
     * 执行该方法时，与Fragment绑定的Activity的onCreate方法已经执行完成并返回，在该方法内可以进行与
     * Activity交互的UI操作，所以在该方法之前Activity的onCreate方法并未执行完成，如果提前进行交互操
     * 作，会引发空指针异常。
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }
    /**
     * onStart()：执行该方法时，Fragment由不可见变为可见状态。
     */
    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }
    /**
     * onResume()：执行该方法时，Fragment处于活动状态，用户可与之交互。
     */
    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }
    /**
     * onPause()：执行该方法时，Fragment处于暂停状态，但依然可见，用户不能与之交互。
     * onSaveInstanceState()：保存当前Fragment的状态。该方法会自动保存Fragment的状态，
     * 比如EditText键 入的文本，即使Fragment被回收又重新创建，一样能恢复EditText之前键入的文本。
     */
    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }
    /**
     * onStop()：执行该方法时，Fragment完全不可见。
     */
    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }
    /**
     * onDestroyView()：销毁与Fragment有关的视图，但未与Activity解除绑定，
     * 依然可以通过onCreateView方法重新创建视图。通常在ViewPager+Fragment的方式下会调用此方法。
     */
    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: ");
        super.onDestroyView();
    }
    /**
     * onDestroy()：销毁Fragment。通常按Back键退出或者Fragment被回收时调用此方法。
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
    /**
     * onDetach()：解除与Activity的绑定。在onDestroy方法之后调用。
     */
    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach: ");
        super.onDetach();
    }

    /**
     * 跳转到另一个Activity，携带数据
     * @param context
     *              上下文
     * @param cls
     *              目标类
     * @param bundle
     *              数据bundle
     */
    public static void goToActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 启动一个activity
     *
     * @param context
     *            上下文
     * @param cls
     *            目标类
     */
    public static void goToActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

	public volatile CustomProgressDialog progressDialog;
    public void show() {
		if (null == progressDialog) {
			progressDialog = CustomProgressDialog.createDialog(mActivity);
			progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
			progressDialog.findViewById(R.id.loadingImageView).setOnLongClickListener(new DismissListener());
			progressDialog.setOnKeyListener(new MyOnKeyListener());
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private final class DismissListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			progressDialog.dismiss();
			return true;
		}

	};
	
	protected class MyOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_SEARCH == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
				return true;
			}
			return false;
		}

	}
	public void dismiss() {
		if (null != progressDialog) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e) {

			}
		}

	}
}
