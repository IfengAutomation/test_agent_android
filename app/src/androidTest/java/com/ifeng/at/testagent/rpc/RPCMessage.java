package com.ifeng.at.testagent.rpc;

import java.util.List;

/**
 *
 */
public class RPCMessage {
    public static final int RPCCall = 1;
    public static final int RPCResult = 2;

    private int msg_type;
    private int msg_id;
    private int version = 1;
    private String name;
    private List<Object> args;


}
