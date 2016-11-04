package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.ArgumentType;
import com.ifeng.at.testagent.reflect.InstanceProxyFactory;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
            } catch (NoSuchMethodException e) {
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
        return RPCMessage.makeSuccessResult(InstanceProxyFactory.getProxyFromInstance(newInstance));
    }

    private RPCMessage newInstance(RPCContext context, Class clazz, List<Object> args) throws NoSuchMethodException {
        Class[] argTypes = new Class[args.size()];
        Object[] realArgs = new Object[args.size()];
        for(int i=0; i<args.size(); i++){
            Object arg = args.get(i);
            String type = ((String)arg).substring(0, 1);
            String value = ((String)arg).substring(2, ((String) arg).length());
            if(ArgumentType.CLASS.equals(type)){
                try {
                    Class argClazz = Class.forName(value);
                    argTypes[i] = Class.class;
                    realArgs[i] = argClazz;
                } catch (ClassNotFoundException e) {
                    return RPCMessage.makeFailResult("Create new instance failed. Argument class not found");
                }
            }else if(ArgumentType.INT.equals(type)){
                argTypes[i] = Integer.class;
                realArgs[i] = Integer.parseInt(value);
            }else if(ArgumentType.STRING.equals(type)){
                argTypes[i] = String.class;
                realArgs[i] = value;
            }else if(ArgumentType.VAR.equals(type)){
                int hash = Integer.parseInt(value);
                if(!context.getVars().containsKey(hash)){
                    return RPCMessage.makeFailResult("Create new instance failed. Argument remote object not found");
                }
                Object var = context.getVars().get(hash);
                argTypes[i] = var.getClass();
                realArgs[i] = var;
            }else{
                return RPCMessage.makeFailResult("Create new instance failed. Unknown arguments type.");
            }
        }
        Constructor constructor = clazz.getConstructor(argTypes);
        Object newInstance;
        try {
            newInstance = constructor.newInstance(realArgs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return RPCMessage.makeFailResult("Create new instance failed. "+e.getMessage());
        }

        context.getVars().put(newInstance.hashCode(), newInstance);
        Map<String, String> remoteObject = new HashMap<>();
        remoteObject.put("hash", newInstance.hashCode()+"");
        return RPCMessage.makeSuccessResult(remoteObject);
    }
}
