package com.ifeng.at.testagent;

import android.app.Activity;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ifeng.at.testagent.rpc.RpcClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by zhaoye on 16/7/19.
 */
@RunWith(AndroidJUnit4.class)
public class Agent {
    public static final String ARG_HOST = "host";
    public static final String ARG_PORT = "port";
    public static final String ARG_DEVICE_ID = "id";
    private RpcClient rpcClient;
    private String host;
    private int port;
    private String id;

    @Before
    public void setUp() {
        Bundle arguments = InstrumentationRegistry.getArguments();
        host = arguments.getString(ARG_HOST);
        port = Integer.parseInt(arguments.getString(ARG_PORT));
        id = arguments.getString(ARG_DEVICE_ID);
    }

    @Test
    public void start() {
        //start RPC Client
        rpcClient = new RpcClient(new RPCRequestHandler());
        rpcClient.startAndBlock(host, port, id);
    }

    @Test
    public void testRecommendVideoForward() throws Exception {
        CommonUtil.startIfengVideo();
    }

}
