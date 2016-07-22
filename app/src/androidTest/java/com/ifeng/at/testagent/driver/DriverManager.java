package com.ifeng.at.testagent.driver;

import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.methodImpl.ClickOnText;
import com.ifeng.at.testagent.driver.methodImpl.EnterText;
import com.ifeng.at.testagent.driver.methodImpl.GetView;
import com.ifeng.at.testagent.driver.methodImpl.WaitForText;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoye on 16/7/19.
 *
 */
public class DriverManager{
    private Solo solo;
    private Map<String, MethodExecute> methodMap = new HashMap<>();
    private Map<String, Object> varCache = new HashMap<>();

    public DriverManager(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation());
        methodMap = registerMethodMap();
    }

    public Response soloCall(Request request){
        Response response = null;
        MethodExecute method = methodMap.get(request.getMethod());   //获取MethodExecute对象

        //method为空处理
        if (method != null){
            response = method.execute(request, solo, varCache);    //执行method
        }else{
            response = new Response();
            response.setId(request.getId());
            response.setVersion(request.getVersion());
            response.setResult(0);
            response.setError("There are no method:" + request.getMethod());
        }

        return response;

    }

    public Map<String, MethodExecute> registerMethodMap(){
        ClickOnText clickOnText = new ClickOnText();
        methodMap.put(clickOnText.getMethodName(), clickOnText);

        EnterText enterText = new EnterText();
        methodMap.put(enterText.getMethodName(), enterText);

        GetView getView = new GetView();
        methodMap.put(getView.getMethodName(), getView);

        WaitForText waitForText = new WaitForText();
        methodMap.put(waitForText.getMethodName(),waitForText);

        return methodMap;
    }

}
