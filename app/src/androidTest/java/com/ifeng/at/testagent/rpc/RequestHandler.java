package com.ifeng.at.testagent.rpc;

/**
 * Created by zhaoye on 16/7/19.
 *
 */
public interface RequestHandler {
    RPCMessage handle(RPCMessage request);
}
