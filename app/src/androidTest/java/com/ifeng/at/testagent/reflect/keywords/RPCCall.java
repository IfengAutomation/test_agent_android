package com.ifeng.at.testagent.reflect.keywords;

import com.ifeng.at.testagent.reflect.RPCContext;
import com.ifeng.at.testagent.reflect.RPCKeyword;
import com.ifeng.at.testagent.rpc.RPCMessage;

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
        return null;
    }
}
