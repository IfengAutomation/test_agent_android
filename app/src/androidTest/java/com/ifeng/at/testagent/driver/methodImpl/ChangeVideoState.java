package com.ifeng.at.testagent.driver.methodImpl;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lixintong on 2016/11/8.
 */
public class ChangeVideoState extends RPCMethod {
    private String play_state = "play";
    private String pause_state = "pause";

    private static HashMap<String, String> videoTypeMap = new HashMap<String, String>() {
        {
            put("video_player", "com.ifeng.newvideo.videoplayer.activity.ActivityVideoPlayerDetail");
            put("topic_player", "com.ifeng.newvideo.videoplayer.activity.ActivityTopicPlayer");
            put("live", "com.ifeng.newvideo.ui.live.TVLiveActivity");
//            put("vr_live", "com.ifeng.newvideo.ui.live.VRLiveActivity");
            put("pic_player", "com.ifeng.newvideo.ui.UniversalChannelFragment");
            put("local_player", "com.ifeng.newvideo.videoplayer.activity.ActivityCacheVideoPlayer");
        }
    };
    private static HashMap<String, String> videoIdsMap = new HashMap<String, String>() {
        {
            put("video_player", "com.ifeng.newvideo:id/video_skin");
            put("topic_player", "com.ifeng.newvideo:id/topic_video_skin");
            put("live", "com.ifeng.newvideo:id/video_skin");
//            put("vr_live", "com.ifeng.newvideo.ui.live.VRLiveActivity");//todo 改
            put("pic_player", "com.ifeng.newvideo:id/video_skin");
            put("local_player", "com.ifeng.newvideo:id/video_skin");

        }
    };

    public ChangeVideoState() {
        setArgsNumber(2);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
        try {
            String player_name = (String) request.getArgs().get(0);
            String state = (String) request.getArgs().get(1);
            ViewGroup videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
            View view = videoSkin.getChildAt(0);
            solo.clickOnView(view);
            solo.clickOnView(view);
            boolean isPlay = isPlay(solo, videoTypeMap.get(player_name));
            String message = state + "失败";
            boolean result = ((pause_state.equals(state) && isPlay == false) || (play_state.equals(state) && isPlay == true));
            Assert.assertTrue(message, result);
            return RPCMessage.makeSuccessResult();

        } catch (Throwable e) {
            return ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }
    }

    private boolean isPlay(Solo solo, String videoPlayerClass) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
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
            List<android.support.v4.app.Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
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