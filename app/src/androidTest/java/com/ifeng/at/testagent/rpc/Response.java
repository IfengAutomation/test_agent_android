package com.ifeng.at.testagent.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * Owner lixintong.
 *
 */
public class Response {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAIL = 0;

    private int id;
    private int version = 1;
    private int result = 0;
    private String error = "";
    private Map<String, Object> entity = new HashMap<>();

    public Response() {
    }

    public Response(int id, int result, String error, Map<String, Object> entity) {
        this.id = id;
        this.result = result;
        this.error = error;
        this.entity = entity;
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
