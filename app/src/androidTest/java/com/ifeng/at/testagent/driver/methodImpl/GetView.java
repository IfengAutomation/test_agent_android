package com.ifeng.at.testagent.driver.methodImpl;

import android.view.View;
import android.widget.TextView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.ifeng.at.testagent.rpc.ResponseError;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public class GetView implements RPCMethod{

    @Override
    public Response execute(Request request, Solo solo, Map varCache) {

        Response response = new Response();
        ResponseError error = new ResponseError();
        Map<String, Object> entity = new HashMap<>();

        String errorMsg;
        if (request.getArgs().length != 1){    //传入args参数个数不正确
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            errorMsg = error.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
            View view = solo.getView(request.getArgs()[0]);
            if (view != null){
                int code = view.hashCode();
                //agent端 动态存入key：hashcode，value：Object（view）
                varCache.put(code, view);
                String packageName = view.getResources().getResourcePackageName(view.getId());

                //返回entity，包括hashcode、view的各种属性
                entity.put("code",code); //存入hashCode

                CharSequence text = "";
                //判断是否继承自TextView，true：获取text；false：设置text为空
                if (TextView.class.isInstance(view)){
                    text = ((TextView) view).getText();
                }
                entity.put("text", text + "");
                entity.put("resource-id", packageName + ":id/" + request.getArgs()[0]);
                entity.put("class", view.getClass().getName());
                entity.put("package", packageName);
                entity.put("content-desc", view.getContentDescription() + "");

                response.setResult(response.RESULT_SUCCESS);
                response.setEntity(entity);
            } else {   //找不到对应view
                response.setResult(response.RESULT_FAIL);
                errorMsg = error.errorViewNotFound;
                response.setError(errorMsg);
            }
        }

        return response;
    }

}
