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

        String className = (String) args.get(0);

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return RPCMessage.makeFailResult("Create new instance failed. Class not found : "+className);
        }

        RPCMessage response;
        if(args.size() > 1){
            try {
                response = newInstance(context, clazz, args.subList(1, args.size()));
            } catch (NoSuchMethodException | ReflectionException e) {
                response = RPCMessage.makeFailResult("Create new instance failed. "+e.getMessage());
            }
        }else{
            try {
                response = newInstance(context, clazz);
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

    private RPCMessage newInstance(RPCContext context, Class clazz, List<Object> args) throws ReflectionException, NoSuchMethodException {
        InstanceProxyHelper.Args reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args);



        Constructor constructor = clazz.getConstructor(reflectionArgs.getArgTypesArray());
        Object newInstance;
        try {
            newInstance = constructor.newInstance(reflectionArgs.getArgsArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("Create new instance failed. "+e.getMessage());
        }

        context.getVars().put(newInstance.hashCode(), newInstance);
        Map<String, String> remoteObject = InstanceProxyHelper.getProxyFromInstance(newInstance);
        return RPCMessage.makeSuccessResult(remoteObject);
    }
}
