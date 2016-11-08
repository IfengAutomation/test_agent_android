package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class RPCNew implements RPCKeyword {
    @Override
    public String getName() {
        return "new";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        List<Object> args = message.getArgs();
        if(args.size() < 1){
            return RPCMessage.makeFailResult("RPC new need at least 1 arguments. e.g. rpcNew(class)");
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, message.getArgs());
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC new failed."+e.getMessage());
        }

        Object clazz = reflectionArgs.getArgs().get(0);
        if(!(clazz instanceof Class)){
            return RPCMessage.makeFailResult("RPC new failed. First arg was not a class");
        }

        RPCMessage response;
        if(args.size() > 1){
            try {
                response = newInstance(context, (Class) clazz, reflectionArgs);
            } catch (NoSuchMethodException | ReflectionException e) {
                response = RPCMessage.makeFailResult("Create new instance failed. "+e.getMessage());
            }
        }else{
            try {
                response = newInstance(context, (Class) clazz);
            }catch (InstantiationException | IllegalAccessException e) {
                response = RPCMessage.makeFailResult("Create nwe instance failed. " + e.getMessage());
            }
        }
        return response;
    }

    private RPCMessage newInstance(RPCContext context, Class clazz) throws IllegalAccessException, InstantiationException {
        Object newInstance = clazz.newInstance();
        context.getVars().put(newInstance.hashCode(), newInstance);
        return RPCMessage.makeSuccessResult(InstanceProxyHelper.getProxyFromInstance(newInstance));
    }

    private RPCMessage newInstance(RPCContext context, Class clazz, InstanceProxyHelper.Args args) throws ReflectionException, NoSuchMethodException {
        Constructor constructor = clazz.getConstructor(args.getArgTypesArray(1));
        Object newInstance;
        try {
            newInstance = constructor.newInstance(args.getArgsArray(1));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("Create new instance failed. "+e.getMessage());
        }

        context.getVars().put(newInstance.hashCode(), newInstance);
        Map<String, String> remoteObject = InstanceProxyHelper.getProxyFromInstance(newInstance);
        return RPCMessage.makeSuccessResult(remoteObject);
    }
}
