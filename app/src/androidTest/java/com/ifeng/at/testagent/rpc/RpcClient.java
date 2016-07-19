package com.ifeng.at.testagent.rpc;

/**
 * Created by zhaoye on 16/7/19.
 *
 */
public class RpcClient {
    private RequestHandler requestHandler;

    public RpcClient(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void startAndBlock(String host, int port){
        //TODO start json-rpc client and set request handler
    }
}
