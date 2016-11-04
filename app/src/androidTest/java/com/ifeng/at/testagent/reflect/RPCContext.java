package com.ifeng.at.testagent.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class RPCContext {

    private Map<Integer, Object> vars = new HashMap<>();

    public Map<Integer, Object> getVars() {
        return vars;
    }


}
