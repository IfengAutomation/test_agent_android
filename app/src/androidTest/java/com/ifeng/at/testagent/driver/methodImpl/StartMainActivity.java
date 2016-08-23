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
 * Owner liuru
 */
public class StartMainActivity extends RPCMethod {
    public StartMainActivity() {
        setArgsNumber(1);
    }

    @Override
    public Response handleRequest(Request request, Solo solo, Map varCache) {
        Response response = new Response();

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(request.getArgs()[0]);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);   // Clear out any previous instances
        context.startActivity(intent);

        response.setResult(Response.RESULT_SUCCESS);

        return response;
    }

}
