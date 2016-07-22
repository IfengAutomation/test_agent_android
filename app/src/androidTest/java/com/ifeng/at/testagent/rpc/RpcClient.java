package com.ifeng.at.testagent.rpc;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by zhaoye on 16/7/19.
 */
public class RpcClient {
    private RequestHandler requestHandler;
    private Socket clientSocket;
    private int version = 1;
    public String TAG = "RpcClient";
    public Gson gson = new Gson();
    public Writer writer;
    public BufferedReader buf;

    public RpcClient(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void startAndBlock(String host, int port, String deviceId) {
        try {
            clientSocket = new Socket(host, port);        //绑定server
            buf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            writer = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
            boolean registerSuc = registerRequest(deviceId);//手机注册
            if (registerSuc) {
                listenToServer();//监听服务器发送请求
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 手机注册
     *
     * @param deviceId
     * @return
     */
    public boolean registerRequest(String deviceId) throws IOException {
        String[] args = {deviceId};
        Request registerRequest = new Request(1, version, "register", args);
        Response registerResponse = registerHandle(registerRequest);//手机注册
        if (registerResponse.getResult() == 1 && registerResponse.getId() == registerRequest.getId()) {
            Log.i(TAG, "注册成功");
            return true;
        } else {
            Log.i(TAG, "注册失败");//TODO 添加容错处理
            return false;
        }
    }

    /**
     * 注册请求
     *
     * @param registerRequest
     * @return
     */
    public Response registerHandle(Request registerRequest) throws IOException {
        String registerJsonStr = encode(gson.toJson(registerRequest)) + "\n";
        Log.i(TAG,registerJsonStr);
        writer.write(registerJsonStr);
        writer.flush();
        String registerResponseStr = buf.readLine();
        Response registerResponse = gson.fromJson(decode(registerResponseStr), Response.class);
        return registerResponse;
    }

    /**
     * 监听server 请求
     *
     * @throws IOException
     */
    public void listenToServer() throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        StringBuffer requestBuffer = new StringBuffer();
        requestBuffer.append(buf.readLine());
        while (true) {
                if(!"".equals(requestBuffer.toString()) && !"null".equals(requestBuffer.toString())) {
                    Log.i(TAG, requestBuffer.toString());
                    Request request = gson.fromJson(decode(requestBuffer.toString()), Request.class);
                    Response response = requestHandler.handle(request);
                    Log.i(TAG,decode(gson.toJson(response)));
                    String responseJson = encode(gson.toJson(response))+"\n";
                    writer.write(responseJson);
                    writer.flush();
            }
            requestBuffer.delete(0, requestBuffer.length());
            requestBuffer.append(buf.readLine());
        }
    }


    public String decode(String srcStr) {
        srcStr = srcStr.replace("%e", "%");
        srcStr = srcStr.replace("%n", "\n");
        return srcStr;
    }

    public String encode(String srcStr) {
        srcStr = srcStr.replace("%", "%e");
        srcStr = srcStr.replace("\n", "%n");
        return srcStr;
    }
}
