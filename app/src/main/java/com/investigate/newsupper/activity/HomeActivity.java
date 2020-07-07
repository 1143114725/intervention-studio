package com.investigate.newsupper.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.investigate.newsupper.R;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.slide.CenterFagment;
import com.investigate.newsupper.slide.LeftFragment;
import com.investigate.newsupper.slide.RightFragment;
import com.investigate.newsupper.slide.SlidingMenu;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.LogUtil;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.PermissionUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;

/**
 * @author EraJi
 */
public class HomeActivity extends FragmentActivity {


    private String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	CenterFagment centerFagment;
	FragmentTransaction t;
	public static boolean isHide = true;
	private MyApp ma;
    /**
     * 自动转换地址
     */
	private boolean isFree;
    /**
     *  true是免费 false是付费
     */
	private String freeIp, payIp;

    /**
     * 权限回调
     * @param requestCode 传入的值  固定1
     * @param permissions   权限名数组
     * @param grantResults  对应的权限是否通过 -1 拒绝  0 同意
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0, size = grantResults.length; i < size; i++) {
            if(grantResults[i] == -1){
                String permissionName = PermissionUtil.getPremissionName(permissions[i]);
                BaseToast.showShortToast(HomeActivity.this,"您已拒绝"+permissionName+"权限！");
                if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[i])){
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.slide_activity);
		if (null == ma) {
			ma = (MyApp) getApplication();
		}
		checkPremission();

		init();
	}

    /**
     * 检查权限
     */
    private void checkPremission() {
        PermissionUtil.with(this).isPermissions(permissions, new PermissionUtil.Operation() {
            @Override
            public void OnNext() {

            }
        });
    }


    @Override
	protected void onResume() {
		time = 0;
		isFree = ma.cfg.getBoolean("isFree");
		LogUtil.printfLog("isFree", isFree);
		LogUtil.printfLog("appUrl", Cnt.APP_URL);
		super.onResume();
	}
    /**
     * 次数
     */
    private int time = 0;
    /**
     * 时间
     */
    private long myTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按两次并且间隔小于5秒才能退出
			if (0 == time) {
				Toasts.makeText(HomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
				myTime = System.currentTimeMillis();
				time = 1;
				return false;
			} else if (1 == time) {
				if (System.currentTimeMillis() - myTime > 5000) {
					time = 1;
					myTime = System.currentTimeMillis();
					Toasts.makeText(HomeActivity.this, R.string.once_again, Toast.LENGTH_LONG).show();
					return false;
				} else {
					if (MainService.isRun) {
						Intent it = new Intent(HomeActivity.this, MainService.class);
						HomeActivity.this.stopService(it);
					}
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (MainService.isRun) {
			Intent it = new Intent(HomeActivity.this, MainService.class);
			HomeActivity.this.stopService(it);
		}
	}
	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));
		/**
		 * 碎片事件 把左边布局 替换成 左 碎片 把右边布局 替换成 右碎片 把中间布局 替换成 中间viewpage 即可
		 */
		t = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);
		centerFagment = new CenterFagment();
		t.replace(R.id.center_frame, centerFagment);
		t.commit();
		mSlidingMenu.setLeftFragment(leftFragment);
	}

	public void showLeft() {
		leftFragment.onChange(isHide);
		isHide = !isHide;
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	// 跳转 各个 页面
	public <T> void skipActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, cls);
		startActivityForResult(intent, 10);
		overridePendingTransition(R.anim.right, R.anim.left);
	}

	// 跳转 各个 页面
	public <T> void skipActivity(Class<T> cls, int request) {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, cls);
		startActivityForResult(intent, request);
		overridePendingTransition(R.anim.right, R.anim.left);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 10:
			
			break;
		case 20:
			//上传没登陆处理
			if(!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)){
				Intent intent=new Intent(HomeActivity.this,UploadActivity.class);
				startActivity(intent);
			}else{
				
			}
			break;
		case 30:
			//订阅没登陆处理
			if(!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)){
				ma.cfg.putBoolean("isFirst", false); //  设置第一次访问 否  
				Intent intent=new Intent(HomeActivity.this,SubscibeActivity.class);
				startActivity(intent);
			}else{
				
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
