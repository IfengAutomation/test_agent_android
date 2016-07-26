package com.ifeng.at.testagent.driver.methodImpl;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.MethodExecute;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/25.
 */
public class StartMainActivity implements MethodExecute {

    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();
        response.setId(request.getId());
        response.setVersion(request.getVersion());

        if (request.getArgs().length != 1){
            response.setResult(0);
            response.setError("Wrong number of args,need 1.");
            return  response;
        }

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(instrumentation.getTargetContext(),request.getArgs()[0]); //TODO 参数更改为包名,不需启动类
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instrumentation.startActivitySync(intent);

        response.setResult(1);

        return response;
    }

    @Override
    public String getMethodName() {
        return "startMainActivity";
    }
}
