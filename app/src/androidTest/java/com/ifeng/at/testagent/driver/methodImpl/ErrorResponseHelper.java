package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.Response;

/**
 * Owner liuru
 */
public class ErrorResponseHelper {

    private ErrorResponseHelper(){
    }

    public static Response makeMethodNotRegisterErrorResponse(String methodName){
        Response response = new Response();
        response.setResult(Response.RESULT_FAIL);
        response.setError(methodName+": method not registered.");
        return response;
    }

    public static Response makeViewNotFoundErrorResponse(Class<? extends RPCMethod> rpcMethodClass){
        Response response = new Response();
        response.setResult(Response.RESULT_FAIL);
        response.setError(rpcMethodClass.getSimpleName()+": View not found.");
        return response;
    }

    public static Response makeArgsNumberErrorResponse(Class<? extends RPCMethod> rpcMethodClass, int needArgNumber, int givenArgNumber){
        String methodName = rpcMethodClass.getSimpleName();
        String errorMsg;

        if (needArgNumber == 0 || needArgNumber == 1){
            errorMsg = methodName + " takes exactly " + needArgNumber + " argument (" + givenArgNumber + " given)";
        } else {
            errorMsg = methodName + " takes exactly " + needArgNumber + " arguments (" + givenArgNumber + " given)";
        }

        Response response = new Response();
        response.setResult(Response.RESULT_FAIL);
        response.setError(errorMsg);
        return response;
    }
}
