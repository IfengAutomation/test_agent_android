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
        Object className = args.get(0);
        Object methodName = args.get(1);

        if(!(className instanceof String)){
            return RPCMessage.makeFailResult("RPC Call Static failed. ClassName arg was not a String object");
        }
        if(!(methodName instanceof String)){
            return RPCMessage.makeFailResult("RPC Call Static failed. MethodName arg was not a String object");
        }

        Class clazz;
        try {
            clazz = Class.forName((String) className);
        } catch (ClassNotFoundException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. "+e.getMessage());
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args.subList(2, args.size()));
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. Parse args error." + e.getMessage());
        }

        Method method;
        try {
            method = clazz.getMethod((String) methodName, reflectionArgs.getArgTypesArray());
        } catch (NoSuchMethodException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. "+e.getMessage());
        }

        Object result;
        try {
            result = method.invoke(clazz, reflectionArgs.getArgsArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("RPC Call Static failed. "+e.getMessage());
        }

        if(result!=null){
            context.getVars().put(result.hashCode(), result);
            return RPCMessage.makeSuccessResult(InstanceProxyHelper.getProxyFromInstance(result));
        }else{
            return RPCMessage.makeSuccessResult();
        }
    }
}
