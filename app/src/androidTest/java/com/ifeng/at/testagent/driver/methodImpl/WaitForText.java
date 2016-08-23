package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner lr
 */
public class WaitForText extends RPCMethod {
    public WaitForText() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        boolean isAppear = solo.waitForText(request.getArgs()[0]);
        if (isAppear) {
            response.setResult(Response.RESULT_SUCCESS);
        }

        return response;
    }

}
