package com.ifeng.at.testagent.driver;

import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.methodImpl.ChangeVideoState;
import com.ifeng.at.testagent.driver.methodImpl.ClickOnText;
import com.ifeng.at.testagent.driver.methodImpl.ClickOnView;
import com.ifeng.at.testagent.driver.methodImpl.CurrentActivity;
import com.ifeng.at.testagent.driver.methodImpl.EnterText;
import com.ifeng.at.testagent.driver.methodImpl.ErrorResponseHelper;
import com.ifeng.at.testagent.driver.methodImpl.FindViewById;
import com.ifeng.at.testagent.driver.methodImpl.FinishActivity;
import com.ifeng.at.testagent.driver.methodImpl.GetListData;
import com.ifeng.at.testagent.driver.methodImpl.GetListItem;
import com.ifeng.at.testagent.driver.methodImpl.GetView;
import com.ifeng.at.testagent.driver.methodImpl.LoadMore;
import com.ifeng.at.testagent.driver.methodImpl.RefreshContent;
import com.ifeng.at.testagent.driver.methodImpl.StartMainActivity;
import com.ifeng.at.testagent.driver.methodImpl.SwitchToTab;
import com.ifeng.at.testagent.driver.methodImpl.WaitForText;
import com.ifeng.at.testagent.rpc.RPCMessage;
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
    private Map<Integer, Object> varCache = new HashMap<>();

    public DriverManager(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation());
        registerMethodMap();
    }

    public RPCMessage soloCall(RPCMessage request){
        RPCMessage response;
        RPCMethod method = methodMap.get(request.getName());   //获取RPCMethod对象

        //method为空处理
        if (method != null){
            response = method.execute(request, solo, varCache);    //执行method
        }else{
            response = ErrorResponseHelper.makeMethodNotRegisterErrorResponse(request.getName());
        }

        return response;
    }

    private void registerMethodMap(){
        register(new ClickOnText());
        register(new EnterText());
        register(new GetView());
        register(new WaitForText());
        register("LaunchApp", new StartMainActivity());
        register(new FinishActivity());
        register(new ClickOnView());
        register(new SwitchToTab());
        register(new CurrentActivity());
        register(new ChangeVideoState());
        register(new GetListItem());
        register(new LoadMore());
        register(new RefreshContent());
        register(new FindViewById());
        register(new GetListData());
    }

    private void register(RPCMethod method){
        methodMap.put(method.getClass().getSimpleName(), method);
    }

    private void register(String methodName, RPCMethod method){
        methodMap.put(methodName, method);
    }
}
