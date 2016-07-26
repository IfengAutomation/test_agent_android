package com.ifeng.at.testagent.driver;

import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.methodImpl.ClickOnText;
import com.ifeng.at.testagent.driver.methodImpl.EnterText;
import com.ifeng.at.testagent.driver.methodImpl.FinishActivity;
import com.ifeng.at.testagent.driver.methodImpl.GetView;
import com.ifeng.at.testagent.driver.methodImpl.StartMainActivity;
import com.ifeng.at.testagent.driver.methodImpl.WaitForText;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lr on 16/7/19.
 *
 */
public class DriverManager{
    private Solo solo;
    private Map<String, RPCMethod> methodMap = new HashMap<>();
    private Map<String, Object> varCache = new HashMap<>();

    public DriverManager(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation());
        methodMap = registerMethodMap();
    }

    public Response soloCall(Request request){
        Response response;
        RPCMethod method = methodMap.get(request.getMethod());   //获取RPCMethod对象

        //method为空处理
        if (method != null){
            response = method.execute(request, solo, varCache);    //执行method
        }else{
            response = new Response();
            response.setResult(response.RESULT_FAIL);
            String errorMsg = response.errorMethodNotRegister(request.getMethod());
            response.setError(errorMsg);
        }

        return response;

    }

    private Map<String, RPCMethod> registerMethodMap(){
        ClickOnText clickOnText = new ClickOnText();
        register(clickOnText);

        EnterText enterText = new EnterText();
        register(enterText);

        GetView getView = new GetView();
        register(getView);

        WaitForText waitForText = new WaitForText();
        register(waitForText);

        StartMainActivity startMainActivity = new StartMainActivity();
        register(startMainActivity);

        FinishActivity finishActivity = new FinishActivity();
        register(finishActivity);

        return methodMap;
    }

    private void register(RPCMethod method){
        methodMap.put(method.getClass().getSimpleName(), method);
    }

}
