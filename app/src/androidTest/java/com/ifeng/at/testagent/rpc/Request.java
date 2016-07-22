package com.ifeng.at.testagent.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhaoye on 16/7/19.
 */
public class Request {
    //TODO
    private int id;
    private int version;
    private String method;
    private String[] args;
    private String var;

    public Request() {

    }

    public Request(int id, int version, String method, String[] args) {
        this.id = id;
        this.version = version;
        this.method = method;
        this.args = args;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
