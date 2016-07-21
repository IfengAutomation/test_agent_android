package com.ifeng.at.testagent.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoye on 16/7/19.
 *
 */
public class Response {
    //TODO
    private int id;
    private int version;
    private int result;
    private String error;
    private Map<String,Object> entity = new HashMap<>();

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getEntity() {
        return entity;
    }

    public void setEntity(Map<String, Object> entity) {
        this.entity = entity;
    }
}
