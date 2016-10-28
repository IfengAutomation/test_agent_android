package com.ifeng.at.testagent;

import com.ifeng.at.testagent.driver.DriverManager;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.ifeng.at.testagent.rpc.RequestHandler;

/**
 * Owner liuru
 */
public class RPCRequestHandler implements RequestHandler{
    private DriverManager driverManager;

    public RPCRequestHandler() {
        driverManager = new DriverManager();
    }

    @Override
    public RPCMessage handle(RPCMessage request) {
        return driverManager.soloCall(request);
    }
}
