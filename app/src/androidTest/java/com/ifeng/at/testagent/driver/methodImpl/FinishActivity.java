package com.ifeng.at.testagent.driver.methodImpl;

import android.util.Log;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class FinishActivity extends RPCMethod {

    private String TAG = getClass().getSimpleName();

    public FinishActivity() {
        setArgsNumber(0);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map varCache) {

        //关闭页面
        if (solo != null) {
            solo.finishOpenedActivities();
        }
        //清除数据
        if (varCache != null) {
            Log.d(TAG, "clear varCache");
            varCache.clear();
        }

        return RPCMessage.makeSuccessResult();
    }

}
