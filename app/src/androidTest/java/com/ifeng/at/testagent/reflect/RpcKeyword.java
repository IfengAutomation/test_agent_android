package com.ifeng.at.testagent.reflect;

import com.ifeng.at.testagent.rpc.RPCMessage;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public interface RPCKeyword {
    String getName();
    RPCMessage execute(RPCContext context, RPCMessage message);
}
