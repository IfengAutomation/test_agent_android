package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class ClickOnView extends RPCMethod {
    public ClickOnView() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response;
        int hash = Integer.parseInt(request.getArgs()[0]);//获取hashcode
        View view = (View) varCache.get(hash);
        if (view != null) {
            solo.clickOnView(view);
            response = new Response();
            response.setResult(Response.RESULT_SUCCESS);
        } else {
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        return response;
    }
}
