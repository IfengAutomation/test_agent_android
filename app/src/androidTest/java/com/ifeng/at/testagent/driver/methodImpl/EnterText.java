package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.EditText;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class EnterText extends RPCMethod {
    public EnterText() {
        setArgsNumber(2);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response;
        int hash = Integer.parseInt(request.getArgs()[0]);//获取hashcode
        EditText editText = (EditText) varCache.get(hash);
        if (editText != null) {
            solo.clearEditText(editText);
            solo.enterText(editText, request.getArgs()[1]);
            response = new Response();
            response.setResult(Response.RESULT_SUCCESS);
        } else {
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        return response;
    }

}
