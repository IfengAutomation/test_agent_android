package com.ifeng.at.testagent.driver.methodImpl;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
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
public class FindViewById extends RPCMethod{

    public FindViewById(){
        setArgsNumber(2);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {

        RPCMessage response;
        View view;
        int code;
        String resource_id;
        String packageName;
        String className;
        String contentDescription;
        String text = "";

        int hash = Integer.parseInt((String) request.getArgs().get(0));
        View parentView = (View) varCache.get(hash);

        int id = getResId((String) request.getArgs().get(1));

        try{
            view = parentView.findViewById(id);
        }catch (Throwable t){
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
            return  response;
        }

        code = view.hashCode();
        resource_id = view.getResources().getResourceName(view.getId());
        packageName = view.getResources().getResourcePackageName(view.getId());
        className = view.getClass().getName();
        contentDescription = view.getContentDescription() + "";

        Map<String, Object> entity = new HashMap<>();

        //agent端 动态存入key：hashcode，value：Object（view）
        varCache.put(code, view);

        //返回entity，包括hashcode、view的各种属性
        entity.put("hash", code + ""); //存入hashCode

        //判断是否继承自TextView，true：获取text；false：设置text为空
        if (TextView.class.isInstance(view)) {
            text = ((TextView) view).getText().toString();
        }
        entity.put("text", text + "");
        entity.put("resource_id", resource_id);
        entity.put("class_name", className);
        entity.put("package_name", packageName);
        entity.put("content_desc", contentDescription);
        return RPCMessage.makeSuccessResult(entity);
    }

    /***
     * 获取resource id
     * @param idStr
     * @return
     */
    private int getResId(String idStr){
        Context targetContext = InstrumentationRegistry.getTargetContext();
        Resources resources = targetContext.getResources();
        int id = resources.getIdentifier(idStr, "id", targetContext.getPackageName());

        return id;
    }
}
