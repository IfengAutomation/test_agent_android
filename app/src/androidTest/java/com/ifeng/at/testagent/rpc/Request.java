package com.ifeng.at.testagent.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Owner lixintong.
 *
 */
public class Request {
    private int id;
    private int version = 1;
    private String method;
    private String[] args;
    private String var;

    public Request() {

    }

    public Request(int id, String method, String var,String... args) {
        this.id = id;
        this.method = method;
        this.var =  var;
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
