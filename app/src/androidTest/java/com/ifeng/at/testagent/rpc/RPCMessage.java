package com.ifeng.at.testagent.rpc;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RPCMessage {
    public static final int RPCCall = 1;
    public static final int RPCResult = 2;

    private static Gson gson;

    private int msg_type;
    private int msg_id;
    private int version = 1;
    private String name;
    private List<Object> args = new ArrayList<>();


    private static Gson getGson(){
        if(gson!=null){
            return gson;
        }
        synchronized (RPCMessage.class) {
            if (gson == null) {
                gson = new Gson();
            }
        }
        return gson;
    }

    public static RPCMessage fromJson(String json){
        return getGson().fromJson(json, RPCMessage.class);
    }

    public static RPCMessage makeSuccessResult(Object... messages){
        RPCMessage msg = new RPCMessage();
        msg.setName("OK");
        msg.setMsgType(RPCResult);
        for (Object entity : messages){
            msg.getArgs().add(entity);
        }
        return msg;
    }

    public static RPCMessage makeFailResult(Object... errorMessages){
        RPCMessage msg = new RPCMessage();
        msg.setName("Fail");
        msg.setMsgType(RPCResult);
        for (Object errMsg : errorMessages) {
            msg.getArgs().add(errMsg);
        }
        return msg;
    }

    public static String getTrace(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        String trace = sw.toString();
        pw.close();
        return trace;
    }

    public String toJson(){
        return getGson().toJson(this);
    }


    public int getMsgType() {
        return msg_type;
    }

    public void setMsgType(int msgType) {
        this.msg_type = msgType;
    }

    public int getMsgId() {
        return msg_id;
    }

    public void setMsgId(int msgId) {
        this.msg_id = msgId;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getArgs() {
        return args;
    }

}
