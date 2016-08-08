package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.ifeng.at.testagent.rpc.ResponseError;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/8/4.
 */
public class ClickOnView implements RPCMethod {
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        ResponseError error = new ResponseError();

        String errorMsg;
        if (request.getArgs().length != 1){
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            errorMsg = error.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            View view = (View) varCache.get("code");//获取hashcode
            if (view != null){
                solo.clickOnView(view);
                response.setResult(response.RESULT_SUCCESS);
            }else {
                response.setResult(response.RESULT_FAIL);
                errorMsg = error.errorViewNotFound;
                response.setError(errorMsg);
            }
        }

        varCache.clear();
        return response;
    }
}
