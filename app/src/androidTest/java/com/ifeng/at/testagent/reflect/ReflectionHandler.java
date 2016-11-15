package com.ifeng.at.testagent.reflect;

import com.ifeng.at.testagent.reflect.keywords.RPCCall;
import com.ifeng.at.testagent.reflect.keywords.RPCCallStatic;
import com.ifeng.at.testagent.reflect.keywords.RPCDelete;
import com.ifeng.at.testagent.reflect.keywords.RPCGet;
import com.ifeng.at.testagent.reflect.keywords.RPCNew;
import com.ifeng.at.testagent.reflect.keywords.RPCSet;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.ifeng.at.testagent.rpc.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class ReflectionHandler implements RequestHandler{
    private Map<String, RPCKeyword> keywords = new HashMap<>();
    private RPCContext context = new RPCContext();

    public ReflectionHandler() {
        addKeyWord(new RPCCall());
        addKeyWord(new RPCCallStatic());
        addKeyWord(new RPCNew());
        addKeyWord(new RPCDelete());
        addKeyWord(new RPCSet());
        addKeyWord(new RPCGet());
    }

    private void addKeyWord(RPCKeyword keyword) {
        keywords.put(keyword.getName(), keyword);
    }

    @Override
    public RPCMessage handle(RPCMessage request) {
        String msgName = request.getName();
        RPCKeyword keyword = keywords.get(msgName);
        if(keyword == null){
            return RPCMessage.makeFailResult("Unsupported keyword : " + msgName);
        }
        try {
            return keyword.execute(context, request);
        }catch (Throwable t){
            return RPCMessage.makeFailResult("Unknown exception : " + t.getMessage());
        }
    }

}
