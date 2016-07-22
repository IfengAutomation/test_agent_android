package com.ifeng.at.testagent.driver;

import android.test.ActivityTestCase;

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
public class DriverManager extends ActivityTestCase{
    private Solo solo;
    private Map<String, MethodExecute> methodMap = new HashMap<>();
    private Map<String, Object> varCache = new HashMap<>();

    //TODO reflection solo
    public void call(Object instance, String method, String...args)  {

    }

    public Response soloCall(Request request){

        if (solo == null){
            solo = new Solo(getInstrumentation(),getActivity());
        }

        methodMap = registerMethodMap();

        MethodExecute method = methodMap.get(request.getMethod());   //获取MethodExecute对象
        Response response = method.execute(request, solo, varCache);    //执行method

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
