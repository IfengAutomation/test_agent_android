package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.EditText;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.ifeng.at.testagent.rpc.ResponseError;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class EnterText implements RPCMethod {
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        ResponseError error = new ResponseError();

        String errorMsg;
        if (request.getArgs().length != 2){     //传入参数args个数不正确
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            response.setError(errorMsg);
        } else {
            EditText editText = (EditText) varCache.get(Integer.parseInt(request.getArgs()[0]));//获取hashcode
            if (editText != null){
                solo.enterText(editText, request.getArgs()[1]);
                response.setResult(response.RESULT_SUCCESS);
            }else {
                response.setResult(response.RESULT_FAIL);
                errorMsg = error.errorViewNotFound;
                response.setError(errorMsg);
            }
        }

        return response;
    }

}
