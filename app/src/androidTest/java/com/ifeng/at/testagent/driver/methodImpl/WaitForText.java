package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class WaitForText extends RPCMethod {
    public WaitForText() {
        setArgsNumber(1);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map varCache) {
        boolean isAppear = solo.waitForText((String) request.getArgs().get(0));
        if (isAppear) {
            return RPCMessage.makeSuccessResult();
        }else{
            return RPCMessage.makeFailResult("Text " + request.getArgs().get(0) + "not found!");
        }
    }

}
