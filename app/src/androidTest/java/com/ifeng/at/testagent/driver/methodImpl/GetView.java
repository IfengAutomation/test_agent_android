package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;
import android.widget.TextView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
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
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {

        RPCMessage response;
        View view;
        int code;
        String packageName;
        String className;
        String contentDescription;
        String text = "";

        try{
            view = solo.getView((String)request.getArgs().get(0));
            code = view.hashCode();
            packageName = view.getResources().getResourcePackageName(view.getId());
            className = view.getClass().getName();
            contentDescription = view.getContentDescription() + "";
        } catch (Throwable t){
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
            text = ((TextView) view).getText().toString();
        }
        entity.put("text", text + "");
        entity.put("resource-id", packageName + ":id/" + request.getArgs().get(0));
        entity.put("class", className);
        entity.put("package", packageName);
        entity.put("content-desc", contentDescription);

        return RPCMessage.makeSuccessResult(entity);
    }

}
