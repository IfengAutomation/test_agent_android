package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.EditText;

import com.ifeng.at.testagent.driver.MethodExecute;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class EnterText implements MethodExecute {
    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        response.setId(request.getId());
        response.setVersion(request.getVersion());

        if (request.getArgs().length != 2){
            response.setResult(0);
            response.setError("Wrong number of args,need 2.");
            response.setEntity(null);
            return response;
        }
        //TODO 根据key 获取Value
        EditText editText = (EditText) varCache.get(request.getArgs()[0]);

        solo.enterText(editText, request.getArgs()[1]);

        response.setEntity(null);
        response.setResult(1);
        response.setError("");

        return response;
    }

    @Override
    public String getMethodName() {
        return "enterText";
    }
}
