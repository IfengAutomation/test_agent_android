package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

/**
 * Created by lixintong on 2016/11/7.
 */
public class CurrentActivity extends RPCMethod {
    public static HashMap<String, String> viewIdOrNameMap = new HashMap<String, String>() {
        {
            put("video_player", "com.ifeng.newvideo.videoplayer.activity.ActivityVideoPlayerDetail");
            put("topic_player", "com.ifeng.newvideo.videoplayer.activity.ActivityTopicPlayer");
            put("live", "com.ifeng.newvideo.ui.live.TVLiveActivity");
            put("vr_live", "com.ifeng.newvideo.ui.live.VRLiveActivity");
            put("pic_player", "com.ifeng.newvideo:id/pic_video_detail_root");//todo 获取frameLayout
            put("local_player", "com.ifeng.newvideo.videoplayer.activity.ActivityCacheVideoPlayer");
        }
    };

    public CurrentActivity() {
        setArgsNumber(1);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
        RPCMessage response;
        try {
            String contentDesc = (String) request.getArgs().get(0);
            if ("pic_player".equals(contentDesc)) {
                View view = solo.getView(viewIdOrNameMap.get(contentDesc));
                Assert.assertEquals("当前非指定播放页",view.getContentDescription().toString(), contentDesc);
            } else {
                String activityName = solo.getCurrentActivity().getClass().getName();
                Assert.assertEquals("当前非指定播放页",activityName, viewIdOrNameMap.get(contentDesc));
            }
            return RPCMessage.makeSuccessResult();
        }catch(AssertionError e){
            return ErrorResponseHelper.makeAssertionErrorResponse(getClass(),e.getMessage());
        } catch (Throwable e) {
            return ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }
    }

}
