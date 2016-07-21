package com.ifeng.at.testagent;

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
        //TODO handle request
        driverManager.soloCall(request.getMethod(),request.getArgs());
        return null;
    }
}
