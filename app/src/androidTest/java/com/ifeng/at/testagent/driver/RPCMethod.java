package com.ifeng.at.testagent.driver;

import com.ifeng.at.testagent.rpc.Request;
import com.ifeng.at.testagent.rpc.Response;
import com.robotium.solo.Solo;

import java.util.Map;

/**
 * Created by lr on 2016/7/22.
 */
public interface RPCMethod{

    Response execute(Request request, Solo solo, Map varCache);

}
