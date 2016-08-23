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
 * Owner liuru
 */
public class GetView extends RPCMethod {
    public GetView() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map<Integer, Object> varCache) {

        Response response;
        View view;
        int code;
        String packageName;
        String className;
        String contentDescription;
        CharSequence text = "";

        try{
            view = solo.getView(request.getArgs()[0]);
            code = view.hashCode();
            packageName = view.getResources().getResourcePackageName(view.getId());
            className = view.getClass().getName();
            contentDescription = view.getContentDescription() + "";
        } catch (Exception e){
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
            return  response;
        }

        Map<String, Object> entity = new HashMap<>();

        //agent端 动态存入key：hashcode，value：Object（view）
        varCache.put(code, view);

        //返回entity，包括hashcode、view的各种属性
        entity.put("hash", code); //存入hashCode

        //判断是否继承自TextView，true：获取text；false：设置text为空
        if (TextView.class.isInstance(view)) {
            text = ((TextView) view).getText();
        }
        entity.put("text", text + "");
        entity.put("resource-id", packageName + ":id/" + request.getArgs()[0]);
        entity.put("class", className);
        entity.put("package", packageName);
        entity.put("content-desc", contentDescription);

        response = new Response();
        response.setResult(Response.RESULT_SUCCESS);
        response.setEntity(entity);

        return response;
    }

}
