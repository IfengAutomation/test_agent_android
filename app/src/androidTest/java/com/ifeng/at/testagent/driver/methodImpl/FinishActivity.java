package com.ifeng.at.testagent.driver.methodImpl;

import android.util.Log;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/25.
 */
public class FinishActivity implements RPCMethod{

    private String TAG = getClass().getSimpleName();

    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        if (request.getArgs().length != 0){    //传入参数args个数不正确
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            String errorMsg = response.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            //关闭页面
            if (solo != null){
                solo.finishOpenedActivities();
            }
            //清除数据
            if (varCache != null){
                Log.d(TAG, "clear varCache");
                varCache.clear();
            }
            response.setResult(response.RESULT_SUCCESS);
        }
        return response;
    }

}
