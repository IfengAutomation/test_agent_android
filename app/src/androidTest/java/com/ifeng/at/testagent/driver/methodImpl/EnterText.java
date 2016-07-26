package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.EditText;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class EnterText implements RPCMethod {
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        String errorMsg;
        if (request.getArgs().length != 2){     //传入参数args个数不正确
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            errorMsg = response.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            EditText editText = (EditText) varCache.get(request.getArgs()[0]);
            if (editText != null){
                solo.enterText(editText, request.getArgs()[1]);
                response.setResult(response.RESULT_SUCCESS);
            }else {
                response.setResult(response.RESULT_FAIL);
                errorMsg = response.errorViewNotFound;
                response.setError(errorMsg);
            }
        }

        return response;
    }

}
