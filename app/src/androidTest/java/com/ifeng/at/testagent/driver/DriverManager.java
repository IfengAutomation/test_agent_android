package com.ifeng.at.testagent.driver;

import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.methodImpl.ClickOnView;
import com.ifeng.at.testagent.driver.methodImpl.ClickOnText;
import com.ifeng.at.testagent.driver.methodImpl.EnterText;
import com.ifeng.at.testagent.driver.methodImpl.FinishActivity;
import com.ifeng.at.testagent.driver.methodImpl.GetView;
import com.ifeng.at.testagent.driver.methodImpl.StartMainActivity;
import com.ifeng.at.testagent.driver.methodImpl.SwitchToTab;
import com.ifeng.at.testagent.driver.methodImpl.WaitForText;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.ifeng.at.testagent.driver.methodImpl.ErrorResponseHelper;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Owner liuru
 *
 */
public class DriverManager{
    private Solo solo;
    private Map<String, RPCMethod> methodMap = new HashMap<>();
    private Map varCache = new HashMap();

    public DriverManager(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation());
        registerMethodMap();
    }

    public Response soloCall(Request request){
        Response response;
        RPCMethod method = methodMap.get(request.getMethod());   //获取RPCMethod对象

        //method为空处理
        if (method != null){
            response = method.execute(request, solo, varCache);    //执行method
        }else{
            response = ErrorResponseHelper.makeMethodNotRegisterErrorResponse(request.getMethod());
        }

        return response;
    }

    private void registerMethodMap(){
        register(new ClickOnText());
        register(new EnterText());
        register(new GetView());
        register(new WaitForText());
        register(new StartMainActivity());
        register(new FinishActivity());
        register(new ClickOnView());
        register(new SwitchToTab());
    }

    private void register(RPCMethod method){
        methodMap.put(method.getClass().getSimpleName(), method);
    }

}
