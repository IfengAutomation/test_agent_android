package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.TabWidget;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public class SwitchToTab extends RPCMethod {
    public SwitchToTab() {
        setArgsNumber(2);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map varCache) {
        RPCMessage response;
        int hash = Integer.parseInt((String) request.getArgs().get(0));//获取hashcode
        TabWidget tabWidget = (TabWidget) varCache.get(hash);
        if (tabWidget != null) {

            solo.clickOnView(tabWidget.getChildAt(Integer.parseInt((String) request.getArgs().get(1))));
            response = RPCMessage.makeSuccessResult();
        } else {
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        return response;
    }
}
