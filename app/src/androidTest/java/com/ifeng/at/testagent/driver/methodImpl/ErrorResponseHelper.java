package com.ifeng.at.testagent.driver.methodImpl;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;

/**
 * Owner liuru
 */
public class ErrorResponseHelper {

    private ErrorResponseHelper(){
    }

    public static RPCMessage makeMethodNotRegisterErrorResponse(String methodName){
        return RPCMessage.makeFailResult(methodName+": method not registered.");
    }

    public static RPCMessage makeViewNotFoundErrorResponse(Class<? extends RPCMethod> rpcMethodClass){
        return RPCMessage.makeFailResult(rpcMethodClass.getSimpleName()+": View not found.");
    }

    public static RPCMessage makeArgsNumberErrorResponse(Class<? extends RPCMethod> rpcMethodClass, int needArgNumber, int givenArgNumber){
        String methodName = rpcMethodClass.getSimpleName();
        String errorMsg;

        if (needArgNumber == 0 || needArgNumber == 1){
            errorMsg = methodName + " takes exactly " + needArgNumber + " argument (" + givenArgNumber + " given)";
        } else {
            errorMsg = methodName + " takes exactly " + needArgNumber + " arguments (" + givenArgNumber + " given)";
        }

        return RPCMessage.makeFailResult(errorMsg);
    }
}
