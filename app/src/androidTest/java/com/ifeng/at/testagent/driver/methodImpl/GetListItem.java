package com.ifeng.at.testagent.driver.methodImpl;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Owner liuru
 */
public class GetListItem extends RPCMethod {

    public GetListItem(){
        setArgsNumber(2);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
        // 参数：view hashcode, index
        RPCMessage response;
        View view;
        int code;
        String packageName = "";
        String className;
        String contentDescription;
        String text = "";
        String resourceId = ""; // Layout 没有id

        int hash = Integer.parseInt((String) request.getArgs().get(0));  //获取hashcode
        ListView listView = (ListView) varCache.get(hash);

        int index = Integer.parseInt((String) request.getArgs().get(1));  //获取在ListView中所处的index

        try{
            view = listView.getChildAt(index);
        }catch (Throwable e){
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
            return  response;
        }
        try {
            code = view.hashCode();
            // packageName = view.getResources().getResourcePackageName(view.getId());
            className = view.getClass().getName();
            contentDescription = view.getContentDescription() + "";
        }catch (Throwable e){
            Log.d("RpcClient", e.getMessage());
            response = RPCMessage.makeFailResult(e.getMessage());
            return response;
        }

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
        entity.put("resource-id", resourceId);
        entity.put("class", className);
        entity.put("package", packageName);
        entity.put("content-desc", contentDescription);

        return RPCMessage.makeSuccessResult(entity);

    }
}
