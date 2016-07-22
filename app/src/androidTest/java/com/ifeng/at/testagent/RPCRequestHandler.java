package com.ifeng.at.testagent;

import com.google.gson.Gson;
import com.ifeng.at.testagent.driver.DriverManager;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.RequestHandler;
import com.ifeng.at.testagent.rpc.Response;

/**
 * Created by zhaoye on 16/7/21.
 *
 */
public class RPCRequestHandler implements RequestHandler {
    private DriverManager driverManager;

    public RPCRequestHandler() {
        driverManager = new DriverManager();
    }

    @Override
    public Response handle(Request request) {
        Gson gson = new Gson();
        System.out.println("request:"+gson.toJson(request));
        Response response = new Response();
        response.setId(request.getId());
        response.setVersion(request.getVersion());
        response.setError("");
        response.setResult(1);
        //TODO handle request
        driverManager.soloCall(request.getMethod(),request.getArgs());
        return response;
    }
}
