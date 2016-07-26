package com.ifeng.at.testagent.driver.methodImpl;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/25.
 */
public class StartMainActivity implements RPCMethod {

    @Override
    public Response execute(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        if (request.getArgs().length != 1){  //传入参数个数不正确处理
            response.setResult(response.RESULT_FAIL);
            String RPCMethodName = getClass().getSimpleName();
            String errorMsg = response.errorArgsNumberWrong(RPCMethodName, 1, request.getArgs().length);
            response.setError(errorMsg);
        } else {
              Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setClassName(instrumentation.getTargetContext(),request.getArgs()[0]); //TODO 参数更改为包名,不需启动类
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            instrumentation.startActivitySync(intent);

            Context context = instrumentation.getContext();
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(request.getArgs()[0]);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);   // Clear out any previous instances
            context.startActivity(intent);

            response.setResult(response.RESULT_SUCCESS);
        }

        return response;
    }

}
