package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.InstanceProxyHelper;
import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;
import com.ifeng.at.testagent.rpc.RPCMessage;

import java.util.List;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class RPCDelete implements RPCKeyword {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public RPCMessage execute(RPCContext context, RPCMessage message) {
        List<Object> args = message.getArgs();
        if(args.size()<1){
            return RPCMessage.makeFailResult("RPC delete need at least 1 argument");
        }

        InstanceProxyHelper.Args reflectionArgs;
        try {
            reflectionArgs = InstanceProxyHelper.getArgsFromRPCMessage(context, args);
        } catch (ReflectionException e) {
            return RPCMessage.makeFailResult("RPC delete failed. "+e.getMessage());
        }

        Object remoteObj = reflectionArgs.getArgs().get(0);
        context.getVars().remove(remoteObj.hashCode());
        return RPCMessage.makeSuccessResult();
    }
}
