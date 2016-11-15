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

public class RPCCallStatic implements RPCKeyword {
    @Override
    public String getName() {
        return "call_static";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        List<Object> args = message.getArgs();
        if(args.size() < 2){
            return RPCMessage.makeFailResult("RPC Call Static need at least 2 arguments. 1)class name 2)method name");
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args);
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. Parse args error." + RPCMessage.getTrace(e));
        }

        Object clazz = reflectionArgs.getArgs().get(0);
        Object methodName = reflectionArgs.getArgs().get(1);

        if(!(clazz instanceof Class)){
            return RPCMessage.makeFailResult("RPC Call Static failed. Class arg was not a Class object");
        }
        if(!(methodName instanceof String)){
            return RPCMessage.makeFailResult("RPC Call Static failed. MethodName arg was not a String object");
        }

        Method method;
        try {
            method = ((Class) clazz).getMethod((String) methodName, reflectionArgs.getArgTypesArray(2));
        } catch (NoSuchMethodException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. "+RPCMessage.getTrace(e));
        }

        Object result;
        try {
            result = method.invoke(clazz, reflectionArgs.getArgsArray(2));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. "+RPCMessage.getTrace(e));
        }

        if(result!=null){
            context.getVars().put(result.hashCode(), result);
            return RPCMessage.makeSuccessResult(InstanceProxyHelper.toRemoteObject(result));
        }else{
            return RPCMessage.makeSuccessResult();
        }
    }
}
