package com.ifeng.at.testagent.driver.methodImpl;

import android.util.Log;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
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
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        //关闭页面
        if (solo != null) {
            solo.finishOpenedActivities();
        }
        //清除数据
        if (varCache != null) {
            Log.d(TAG, "clear varCache");
            varCache.clear();
        }
        response.setResult(Response.RESULT_SUCCESS);
        return response;
    }

}
