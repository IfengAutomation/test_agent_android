package com.ifeng.at.testagent.driver.methodImpl;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lixintong on 2016/11/15.
 */
public class PlayerCommon {

    public static boolean isPlay(Solo solo, String player_name) {
        try {
            return mIsPlay(solo, player_name);
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean mIsPlay(Solo solo, String videoPlayerClass) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        solo.sleep(3000);
        Class activityVideoPlayer = Class.forName(videoPlayerClass);
        Field[] declaredFields = activityVideoPlayer.getDeclaredFields();
        Field mPlayerHelperField = null;
        for (Field field : declaredFields) {
            if (field.getType().getName().equals("com.ifeng.newvideo.videoplayer.player.NormalVideoHelper")) {
                mPlayerHelperField = field;
                mPlayerHelperField.setAccessible(true);
                break;
            }
        }
        Object mPlayerHelper = null;
        if (videoPlayerClass.contains("UniversalChannelFragment")) {
            FragmentActivity fragmentActivity = (FragmentActivity) solo.getCurrentActivity();
            List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
            for (android.support.v4.app.Fragment fragment : fragments) {
                if (fragment.getClass().getName().equals("com.ifeng.newvideo.ui.FragmentHomePage")) {
                    List<android.support.v4.app.Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
                    for (int i = 0; i < childFragments.size(); i++) {
                        if (childFragments.get(i) != null && childFragments.get(i).getClass().getName().equals(("com.ifeng.newvideo.ui.UniversalChannelFragment"))) {
                            mPlayerHelper = mPlayerHelperField.get(childFragments.get(i));
                            break;
                        }
                    }
                    break;
                }
            }
        } else {
            Activity currentActivity = solo.getCurrentActivity();
            mPlayerHelper = mPlayerHelperField.get(currentActivity);
        }
        Field mPlayerField = mPlayerHelper.getClass().getDeclaredField("mPlayer");
        mPlayerField.setAccessible(true);
        Object mPlayer = mPlayerField.get(mPlayerHelper);
        Method isPlayingMethod = mPlayer.getClass().getDeclaredMethod("isPlaying");
        isPlayingMethod.setAccessible(true);
        boolean isPlay = (boolean) isPlayingMethod.invoke(mPlayer);
        return isPlay;
    }
}
