package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.Field;

/**
 * Created by zhaoye on 2016/11/15.
 *
 */

public class RPCGet implements RPCKeyword{
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        if(message.getArgs().size()<2){
            return RPCMessage.makeFailResult("RPC Get failed. Need 2 arguments");
        }

        InstanceProxyHelper.Args args;
        try {
            args = InstanceProxyHelper.getArgsFromRPCMessage(context, message.getArgs());
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Get failed.", RPCMessage.getTrace(e));
        }

        Object target = args.getArgs().get(0);
        Object fieldName = args.getArgs().get(1);

        if(!(fieldName instanceof String)){
            RPCMessage.makeFailResult("RPC Get failed. Field name need a String argument");
        }

        Field field;
        Object result;
        try {
            if(target instanceof Class){
                field = ((Class) target).getDeclaredField((String) fieldName);
                field.setAccessible(true);
                result = field.get(null);
            }else{
                field = target.getClass().getDeclaredField((String) fieldName);
                field.setAccessible(true);
                result = field.get(target);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return RPCMessage.makeFailResult("RPC Get failed.", RPCMessage.getTrace(e));
        }

        return RPCMessage.makeSuccessResult(InstanceProxyHelper.toRemoteObject(result));
    }
}
