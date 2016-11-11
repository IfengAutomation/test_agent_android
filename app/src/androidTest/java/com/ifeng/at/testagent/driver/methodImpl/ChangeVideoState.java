package com.ifeng.at.testagent.driver.methodImpl;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
import java.util.logging.Logger;


/**
 * Created by lixintong on 2016/11/8.
 */
public class ChangeVideoState extends RPCMethod {

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
//            put("pic_player", "");
            put("pic_player", "com.ifeng.newvideo:id/video_skin");
//            put("pic_player", "com.ifeng.newvideo:id/pic_video_detail_root");
            put("local_player", "com.ifeng.newvideo.videoplayer.activity.ActivityCacheVideoPlayer");

        }
    };
    private String stateImageId = "com.ifeng.newvideo:id/btn_play_pause";

    public ChangeVideoState() {
        setArgsNumber(2);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
        try {
            boolean isPlay = false;
            String player_name = (String) request.getArgs().get(0);
            String state = (String) request.getArgs().get(1);
//            ViewGroup videoSkin = null;
            ViewGroup videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
//            if ("video_player".equals(player_name)) {
//                videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
//            } else if ("topic_player".equals(player_name)) {
//                videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
//            } else if ("live".equals(player_name)) {
//                videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
//            } else if ("local_player".equals(player_name)) {
//                FrameLayout rootFrameLayout = (FrameLayout) solo.getCurrentActivity().getWindow().getDecorView().getRootView();
//                videoSkin = (ViewGroup) rootFrameLayout.getChildAt(0);
//            } else if ("pic_player".equals(player_name)) {
//                videoSkin = (ViewGroup) solo.getView(videoIdsMap.get(player_name));
////                FrameLayout rootFrameLayout = (FrameLayout) solo.getView(videoIdsMap.get(player_name));
////                videoSkin = (ViewGroup) rootFrameLayout.getChildAt(0);
//            }
//            solo.clickOnView(videoSkin);
//            solo.clickOnView(videoSkin);
            View view = videoSkin.getChildAt(0);
            solo.clickOnView(view);
            solo.clickOnView(view);
            isPlay = isPlay(solo, videoTypeMap.get(player_name));
            String message = state + "失败";
            boolean result = (("pause".equals(state) && isPlay == false) || ("play".equals(state) && isPlay == true));
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
                break;
            }
        }
//        Field mPlayerHelperField = activityVideoPlayer.getDeclaredField(videoHelper);
        Object mPlayerHelper = null;
        if (videoPlayerClass.contains("UniversalChannelFragment")){
            FragmentActivity fragmentActivity = (FragmentActivity) solo.getCurrentActivity();
            List<android.support.v4.app.Fragment> fragments =  fragmentActivity.getSupportFragmentManager().getFragments();
            for(android.support.v4.app.Fragment fragment:fragments){
                if(fragment.getClass().getName().equals("com.ifeng.newvideo.ui.FragmentHomePage")){
                    List<android.support.v4.app.Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
                    for(int i = 0 ;i<childFragments.size();i++){
                        if(childFragments.get(i)!=null && childFragments.get(i).getClass().getName().equals(("com.ifeng.newvideo.ui.UniversalChannelFragment"))){
                            mPlayerHelper = mPlayerHelperField.get(childFragments.get(i));
                            break;
                        }
                    }
                    break;
                }
            }

        }else{
            Activity currentActivity = solo.getCurrentActivity();
            mPlayerHelper = mPlayerHelperField.get(currentActivity);
        }
//         mPlayerHelper = mPlayerHelperField.get(currentActivity);
        Field mPlayerField = mPlayerHelper.getClass().getDeclaredField("mPlayer");
        mPlayerField.setAccessible(true);
        Object mPlayer = mPlayerField.get(mPlayerHelper);
        Method isPlayingMethod = mPlayer.getClass().getDeclaredMethod("isPlaying");
        isPlayingMethod.setAccessible(true);
        boolean isPlay = (boolean) isPlayingMethod.invoke(mPlayer);
        return isPlay;
    }


//    @Override
//    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
//        try {
//            String video_type = (String) request.getArgs().get(0);
//            String state = (String) request.getArgs().get(1);
//            if ("video_player".equals(video_type) ||  "topic_player".equals(video_type) || "live".equals(video_type)) {
//                RelativeLayout videoSkin = (RelativeLayout) solo.getView(videoIdsMap.get(video_type));
//                solo.clickOnView(videoSkin.getChildAt(0));
//                solo.clickOnView(videoSkin.getChildAt(0));
//
//                String videoPlayerClassName = videoTypeMap.get(video_type);
//                Class activityVideoPlayer = Class.forName(videoPlayerClassName);
//                Field mPlayerHelperField = activityVideoPlayer.getDeclaredField("mPlayerHelper");
//                mPlayerHelperField.setAccessible(true);
//                Activity currentActivity = solo.getCurrentActivity();
//                Object mPlayerHelper = mPlayerHelperField.get(currentActivity);
//                Field mPlayerField = mPlayerHelper.getClass().getDeclaredField("mPlayer");
//                mPlayerField.setAccessible(true);
//                Object mPlayer = mPlayerField.get(mPlayerHelper);
//                Method isPlayingMethod = mPlayer.getClass().getDeclaredMethod("isPlaying");
//                isPlayingMethod.setAccessible(true);
//                boolean isPlay = (boolean) isPlayingMethod.invoke(mPlayer);
//                String message = state + "失败";
//                boolean result = (("pause".equals(state) && isPlay == false) || ("play".equals(state) && isPlay == true));
//                Assert.assertTrue(message, result);
//                return RPCMessage.makeSuccessResult();
//            } else if ("local_player".equals(video_type) || "pic_player".equals(video_type)) {
//                String videoPlayerClassName = videoTypeMap.get(video_type);
//                Class activityVideoPlayer = Class.forName(videoPlayerClassName);
//                Field mVideoSkinField = activityVideoPlayer.getDeclaredField("mVideoSkin");
//                mVideoSkinField.setAccessible(true);
//                Activity currentActivity = solo.getCurrentActivity();
//                Object mPlayerHelper = mVideoSkinField.get(currentActivity);
//                Field mPlayerField = mPlayerHelper.getClass().getDeclaredField("mPlayer");
//                mPlayerField.setAccessible(true);
//                Object mPlayer = mPlayerField.get(mPlayerHelper);
//                Method isPlayingMethod = mPlayer.getClass().getDeclaredMethod("isPlaying");
//                isPlayingMethod.setAccessible(true);
//                boolean isPlay = (boolean) isPlayingMethod.invoke(mPlayer);
//                String message = state + "失败";
//                boolean result = (("pause".equals(state) && isPlay == false) || ("play".equals(state) && isPlay == true));
//                Assert.assertTrue(message, result);
//                return RPCMessage.makeSuccessResult();
//            } else {
//                return RPCMessage.makeSuccessResult();
//            }
//        } catch (Throwable e) {
//            return ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
//        }
//    }
//    public static IPlayer createPlayer(IfengSurfaceView surfaceView) {
//        IPlayer mediaPlayer;
//        if (ChoosePlayerUtils.useIJKPlayer(IfengApplication.getAppContext())) {
//            mediaPlayer = new WrapperIjkPlayer();
//        } else {
//            mediaPlayer = new WrapperIfengPlayer();
//        }
//        mediaPlayer.setDisplay(surfaceView);
//        return mediaPlayer;
//    }

//    @Override
//    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
//        String status = (String) request.getArgs().get(0);
//        View videoSkin = solo.getView("com.ifeng.newvideo:id/video_skin");
//        solo.clickOnView(videoSkin);
//        if ("pause".equals(status)) {
//            View btnPlayPause = solo.getView("com.ifeng.newvideo:id/btn_play_pause");
//            solo.clickOnView(btnPlayPause);
//            View controlSeekBar = solo.getView(" com.ifeng.newvideo:id/control_seekBar");
//            solo.sleep(3);
//            solo.clickOnView(videoSkin);
//            View nextControlSeekBar = solo.getView(" com.ifeng.newvideo:id/control_seekBar");
////            todo 比进度
//        } else {
//            View controlSeekBar = solo.getView(" com.ifeng.newvideo:id/control_seekBar");
//            View btnPlayPause = solo.getView("com.ifeng.newvideo:id/btn_play_pause");
//            solo.clickOnView(btnPlayPause);
//            solo.sleep(3);
//            View nextControlSeekBar = solo.getView(" com.ifeng.newvideo:id/control_seekBar");
//        }
//
//        return null;
//    }
}
