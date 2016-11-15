package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class RPCCall implements RPCKeyword {

    @Override
    public String getName() {
        return "call";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        List<Object> args = message.getArgs();
        if(args.size() < 2){
            return RPCMessage.makeFailResult("RPC Call need at least 2 arguments. 1)instance 2)method name");
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args);
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Call failed. "+RPCMessage.getTrace(e));
        }

        Object remoteInstance = reflectionArgs.getArgs().get(0);
        Object methodName = reflectionArgs.getArgs().get(1);
        if(!(methodName instanceof String)){
            return RPCMessage.makeFailResult("RPC Call failed. method name is not string");
        }

        Method method;
        try {
            method = remoteInstance.getClass().getMethod(((String) methodName), reflectionArgs.getArgTypesArray(2));
        } catch (NoSuchMethodException e) {
            return RPCMessage.makeFailResult("RPC Call failed. "+RPCMessage.getTrace(e));
        }

        Object result;
        try {
            result = method.invoke(remoteInstance, reflectionArgs.getArgsArray(2));
        } catch (IllegalAccessException | InvocationTargetException e) {

            return RPCMessage.makeFailResult("RPC Call failed." + RPCMessage.getTrace(e));
        }

        if(result!=null){
            context.getVars().put(result.hashCode(), result);
            return RPCMessage.makeSuccessResult(InstanceProxyHelper.toRemoteObject(result));
        }else{
            return RPCMessage.makeSuccessResult();
        }
    }
}
