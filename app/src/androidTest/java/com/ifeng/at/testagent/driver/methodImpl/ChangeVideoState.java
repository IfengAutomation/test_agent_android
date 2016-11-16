package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;
import android.view.ViewGroup;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lixintong on 2016/11/8.
 */
public class ChangeVideoState extends RPCMethod {
    private String play_state = "play";
    private String pause_state = "pause";

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
            boolean isPlay = PlayerCommon.isPlay(solo, player_name);
            String message = state + "失败";
            boolean result = ((pause_state.equals(state) && isPlay == false) || (play_state.equals(state) && isPlay == true));
            Assert.assertTrue(message, result);
            return RPCMessage.makeSuccessResult();
        }catch(Throwable e) {
            return ErrorResponseHelper.errorResponse(getClass(),e.getMessage());
        }
    }


}