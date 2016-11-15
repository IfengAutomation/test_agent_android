package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.lang.reflect.Field;

/**
 * Created by zhaoye on 2016/11/14.
 *
 */

public class RPCSet implements RPCKeyword {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        if(message.getArgs().size()<3){
            return RPCMessage.makeFailResult("RPC Set failed. Need 3 arguments");
        }

        InstanceProxyHelper.Args args;
        try {
             args = InstanceProxyHelper.getArgsFromRPCMessage(context, message.getArgs());
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC Set failed.", RPCMessage.getTrace(e));
        }

        Object target = args.getArgs().get(0);
        Object fieldName = args.getArgs().get(1);
        Object value = args.getArgs().get(2);

        if(!(fieldName instanceof String)){
            RPCMessage.makeFailResult("RPC Set failed. Field name need a String argument");
        }

        Field field;
        try {
            if(target instanceof Class){
                field = ((Class) target).getDeclaredField((String) fieldName);
                field.setAccessible(true);
                field.set(null, value);
            }else{
                field = target.getClass().getDeclaredField((String) fieldName);
                field.setAccessible(true);
                field.set(target, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return RPCMessage.makeFailResult("RPC Set failed.", RPCMessage.getTrace(e));
        }

        return RPCMessage.makeSuccessResult();
    }
}
