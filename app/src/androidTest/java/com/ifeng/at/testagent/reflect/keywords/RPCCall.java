package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
        Object remoteInstanceMap = args.get(0);
        Object methodName = args.get(1);
        if(!(remoteInstanceMap instanceof Map)){
            return RPCMessage.makeFailResult("RPC Call failed. Argument 1 is not a remote object");
        }
        if(!(methodName instanceof String)){
            return RPCMessage.makeFailResult("RPC Call failed. Argument 2 is not a method name");
        }

        if(!((Map) remoteInstanceMap).containsKey("hash")){
            return RPCMessage.makeFailResult("RPC Call failed. Remote instance not have att \'hash\'");
        }

        String hashStr = (String) ((Map) remoteInstanceMap).get("hash");
        int remoteInstanceHash = Integer.parseInt(hashStr);

        Object remoteInstance = context.getVars().get(remoteInstanceHash);
        if(remoteInstance == null){
            return RPCMessage.makeFailResult("RPC Call failed. Not found remote instance");
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args.subList(2, args.size()));
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Call failed. "+e.getMessage());
        }

        Method method;
        try {
            method = remoteInstance.getClass().getMethod(((String) methodName), reflectionArgs.getArgTypesArray());
        } catch (NoSuchMethodException e) {
            return RPCMessage.makeFailResult("RPC Call failed. "+e.getMessage());
        }

        Object result = null;
        try {
            result = method.invoke(remoteInstance, reflectionArgs.getArgsArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("RPC Call failed." + e.getMessage());
        }

        if(result!=null){
            context.getVars().put(result.hashCode(), result);
            return RPCMessage.makeSuccessResult(InstanceProxyHelper.getProxyFromInstance(result));
        }else{
            return RPCMessage.makeSuccessResult();
        }
    }
}
