package com.investigate.newsupper.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.baidu.location.BDLocation;

/**
 * 设计模式之
 * 发布者/订阅者(含单例)
 */
public final class Publisher {
	BDLocation mBDLocation;
	
	public static interface Subscriber {
		void onPublish(int key, Object... data);
	}

	private static Publisher sInstance;
	
	private ArrayList<WeakReference<Subscriber>> mSubscribers;
	
	private Publisher() {
	}
	
	public static Publisher getInstance() {
		if (sInstance == null) {
			synchronized (Publisher.class) {
				if (sInstance == null) {
					sInstance = new Publisher();
				}
			}
		}
		return sInstance;
	}
	
	public void addSubscriber(Subscriber subscriber) {
		if (subscriber == null) {
			return;
		}
		if (mSubscribers == null) {
			mSubscribers = new ArrayList<WeakReference<Subscriber>>();
		}
		boolean isContains = false;
		for (WeakReference<Subscriber> wf : mSubscribers) {
			if (wf == null) {
				continue;
			}
			Subscriber s = wf.get();
			if (subscriber.equals(s)) {
				isContains = true;
				break;
			}
		}
		if (!isContains) {
			mSubscribers.add(new WeakReference<Subscriber>(subscriber));
		}
	}
	
	public void removeSubscriber(Subscriber subscriber) {
		if (subscriber == null || mSubscribers == null) {
			return;
		}
		int removeIndex = -1;
		for (int i = 0; i < mSubscribers.size(); i++) {
			WeakReference<Subscriber> wf = mSubscribers.get(i);
			if (wf == null) {
				continue;
			}
			Subscriber s = wf.get();
			if (subscriber.equals(s)) {
				removeIndex = i;
				break;
			}
		}
		if (removeIndex > -1) {
			mSubscribers.remove(removeIndex);
		}
	}
	
	public void publishAll(int key, Object...data) {
		if (mSubscribers == null) {
			return;
		}
		for (WeakReference<Subscriber> wf : mSubscribers) {
			if (wf == null) {
				continue;
			}
			Subscriber s = wf.get();
			if (s != null) {
				s.onPublish(key, data);
			}
		}
		
	}
	public void setLocation(BDLocation location) {
		mBDLocation=location;
	}
	public static interface SubscriberKey {
		/** 位置发生变化 */
		int KEY_LOCATION_UPDATE = 1;
	}
}
