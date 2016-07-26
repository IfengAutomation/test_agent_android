package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.MethodExecute;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class WaitForText implements MethodExecute{
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        Map entity = new HashMap();

        response.setId(request.getId());
        response.setVersion(request.getVersion());

        //参数数量不正确处理
        if (request.getArgs().length != 1){
            response.setResult(0);
            response.setError("Wrong number of args,need 2.");
            return  response;
        }

        boolean flag = solo.waitForText(request.getArgs()[0]); //TODO 超时时间,变量命名
        entity.put("flag", flag); //TODO

        response.setEntity(entity);
        response.setResult(1);
        response.setError(""); //TODO 删除

        return response;
    }

    @Override
    public String getMethodName() {
        return "waitForText";
    }
}
