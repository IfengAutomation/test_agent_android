package com.ifeng.at.testagent.driver;

import com.ifeng.at.testagent.driver.methodImpl.ErrorResponseHelper;
import com.ifeng.at.testagent.rpc.RPCMessage;
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

    public RPCMessage checkArgs(RPCMessage request){
        if(request.getArgs().size() < argsNumber){
            return ErrorResponseHelper.makeArgsNumberErrorResponse(getClass(),argsNumber,request.getArgs().size());
        }
        return null;
    }

    public RPCMessage execute(RPCMessage request, Solo solo, Map<Integer, Object> varCache){
        RPCMessage response = checkArgs(request);
        if(response == null){
            response = handleRequest(request, solo, varCache);
        }
        return response;
    }

    public abstract RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache);


}
