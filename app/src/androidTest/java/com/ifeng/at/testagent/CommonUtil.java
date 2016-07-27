package com.ifeng.at.testagent;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

/**
 * Created by fredric on 15-7-31.
 */
public class CommonUtil {

    /**
     * 凤凰视频启动页类名
     */
//    public static final String SPLASH_ACTIVITY_CLASS_NAME = "com.ifeng.newvideo.ui.ActivitySplash";
//    public static final String SPLASH_ACTIVITY_CLASS_NAME = "com.ifeng.at.testagent.LoginActivity";
    /**
     * 凤凰视频内容基页
     */
    public static final String MAIN_TAB_ACTIVITY_NAME;

    static {
        MAIN_TAB_ACTIVITY_NAME = "ActivityMainTab";
    }

    private static final int TIME_OUT = 5000;

    public static void startIfengVideo() {
        startIfengVideo(TIME_OUT);
    }

    /**
     * 启动凤凰视频
     *
     * @param timeout
     * @return 返回MainTabActivity实例
     */
    public static void startIfengVideo(long timeout) {

        // Launch a simple calculator app
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage("com.ifeng.newvideo");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Clear out any previous instances
        context.startActivity(intent);


       /* Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(instrumentation.getTargetContext(), SPLASH_ACTIVITY_CLASS_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Instrumentation.ActivityMonitor splashMonitor;
        splashMonitor = new Instrumentation.ActivityMonitor(SPLASH_ACTIVITY_CLASS_NAME, null, false);
        instrumentation.addMonitor(splashMonitor);
        instrumentation.startActivitySync(intent);
        Activity splash = splashMonitor.waitForActivityWithTimeout(timeout);
        instrumentation.removeMonitor(splashMonitor);
        return splash;*/
    }
}
