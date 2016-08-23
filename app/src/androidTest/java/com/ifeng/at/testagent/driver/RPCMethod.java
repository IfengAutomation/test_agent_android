package com.ifeng.at.testagent.driver;

import com.ifeng.at.testagent.driver.methodImpl.ErrorResponseHelper;
import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Owner liuru
 */
public abstract class RPCMethod{
    private int argsNumber = 0;

    protected void setArgsNumber(int argsNumber){
        this.argsNumber = argsNumber;
    }

    public Response checkArgs(Request request){
        if(request.getArgs().length < argsNumber){
            return ErrorResponseHelper.makeArgsNumberErrorResponse(getClass(),argsNumber,request.getArgs().length);
        }
        return null;
    }

    public Response execute(Request request, Solo solo, Map varCache){
        Response response = checkArgs(request);
        if(response == null){
            response = handleRequest(request, solo, varCache);
        }
        return response;
    }

    public abstract Response handleRequest(Request request, Solo solo, Map varCache);


}
