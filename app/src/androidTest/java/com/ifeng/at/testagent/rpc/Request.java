package com.ifeng.at.testagent.rpc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoye on 16/7/19.
 *
 */
public class Request {
    //TODO
    private int id;
    private int version;
    private String method;
    private String[] args;

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
}
