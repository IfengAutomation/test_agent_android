package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class ClickOnText extends RPCMethod {

    public ClickOnText() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        solo.clickOnText(request.getArgs()[0]);
        Response response = new Response();
        response.setResult(Response.RESULT_SUCCESS);
        return response;
    }

}
