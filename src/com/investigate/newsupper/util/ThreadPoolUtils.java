package com.investigate.newsupper.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPoolUtils {

	private static final int THREAD_COUNT = 5;
	
    public static final ExecutorService mExecutorService = Executors.newFixedThreadPool(THREAD_COUNT);

    private ThreadPoolUtils() {
    }


    public static void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }
}
