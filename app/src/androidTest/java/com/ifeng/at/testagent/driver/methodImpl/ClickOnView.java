package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
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
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map varCache) {
        RPCMessage response;
        int hash = Integer.parseInt((String) request.getArgs().get(0));//获取hashcode
        View view = (View) varCache.get(hash);
        if (view != null) {
            solo.clickOnView(view);
            response = RPCMessage.makeSuccessResult();
        } else {
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        return response;
    }
}
