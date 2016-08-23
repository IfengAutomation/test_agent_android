package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;
import android.widget.TextView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Owner
 */
public class GetView extends RPCMethod {
    public GetView() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {

        Response response;
        Map<String, Object> entity = new HashMap<>();

        View view = solo.getView(request.getArgs()[0]);
        if (view != null) {
            int code = view.hashCode();
            //agent端 动态存入key：hashcode，value：Object（view）
            varCache.put("code", view);
            String packageName = view.getResources().getResourcePackageName(view.getId());

            //返回entity，包括hashcode、view的各种属性
            entity.put("code", code); //存入hashCode

            CharSequence text = "";
            //判断是否继承自TextView，true：获取text；false：设置text为空
            if (TextView.class.isInstance(view)) {
                text = ((TextView) view).getText();
            }
            entity.put("text", text + "");
            entity.put("resource-id", packageName + ":id/" + request.getArgs()[0]);
            entity.put("class", view.getClass().getName());
            entity.put("package", packageName);
            entity.put("content-desc", view.getContentDescription() + "");

            response = new Response();
            response.setResult(Response.RESULT_SUCCESS);
            response.setEntity(entity);
        } else {   //找不到对应view
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
        }

        return response;
    }

}
