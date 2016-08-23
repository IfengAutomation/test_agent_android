package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.TabWidget;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
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
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response;

        TabWidget tabWidget = (TabWidget) varCache.get("code");//获取hashcode
        if (tabWidget != null) {

            solo.clickOnView(tabWidget.getChildAt(Integer.parseInt(request.getArgs()[1])));
            response = new Response();
            response.setResult(Response.RESULT_SUCCESS);
        } else {
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        varCache.clear(); //使用完毕，清除暂存
        return response;
    }
}
