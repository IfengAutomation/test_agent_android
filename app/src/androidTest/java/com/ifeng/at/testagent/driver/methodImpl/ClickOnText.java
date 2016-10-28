package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
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
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map varCache) {
        solo.clickOnText((String) request.getArgs().get(0));
        return RPCMessage.makeSuccessResult();
    }

}
