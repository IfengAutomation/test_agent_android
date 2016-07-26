package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class GetView implements RPCMethod{

    @Override
    public Response execute(Request request, Solo solo, Map varCache) {

        Response response = new Response();

        String errorMsg;
        if (request.getArgs().length != 1){    //传入args参数个数不正确
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            errorMsg = response.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            View view = solo.getView(request.getArgs()[0]);
            if (view != null){
                if (request.getVar() != null){
                    varCache.put(request.getVar(), view);
                }
                response.setResult(response.RESULT_SUCCESS);
            } else {   //找不到对应view
                response.setResult(response.RESULT_FAIL);
                errorMsg = response.errorViewNotFound;
                response.setError(errorMsg);
            }
        }

        return response;
    }

}
