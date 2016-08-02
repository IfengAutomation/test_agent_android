package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.ifeng.at.testagent.rpc.ResponseError;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class WaitForText implements RPCMethod{
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        Map entity = new HashMap();
        ResponseError error = new ResponseError();

        if (request.getArgs().length != 1){    //参数数量不正确处理
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            String errorMsg = error.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            boolean isAppear = solo.waitForText(request.getArgs()[0]);
            entity.put("isAppear", isAppear);

            response.setEntity(entity);
            response.setResult(response.RESULT_SUCCESS);
        }

        return response;
    }

}
