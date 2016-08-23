package com.ifeng.at.testagent.rpc;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Owner lixintong
 */
public class RpcClient {
    private RequestHandler requestHandler;
    private Socket clientSocket;
    private String TAG = "RpcClient ";
    private Gson gson = new Gson();
    private Writer writer;
    private BufferedReader buf;


    public RpcClient(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    public void startAndBlock(String host, int port, String deviceId)  {
        try {
            clientSocket = new Socket(host, port);        //绑定server
            buf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            writer = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
            boolean registerSuccess = registerRequest(deviceId);//手机注册
            if (registerSuccess) {
                listenToServer();//监听服务器发送请求
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"socket 连接出错\n"+e.getMessage());
        }
    }

    /**
     * 手机注册
     *
     * @param deviceId
     * @return
     */
    private boolean registerRequest(String deviceId) throws IOException {
        Request registerRequest = new Request(1, "register", "", deviceId);
        Response registerResponse = registerHandle(registerRequest);//手机注册
        if (registerResponse.getResult() == Response.RESULT_SUCCESS && registerResponse.getId() == registerRequest.getId()) {
            Log.i(TAG, "注册成功");
            return true;
        } else {
            Log.i(TAG, "注册失败");
            return false;
        }
    }

    /**
     * 注册请求
     *
     * @param registerRequest
     * @return
     */
    private Response registerHandle(Request registerRequest) throws IOException {
        String registerJsonStr = encode(gson.toJson(registerRequest)) + "\n";
        Log.i(TAG, "register response message :"+registerJsonStr);
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
        StringBuffer requestBuffer = new StringBuffer();
        requestBuffer.append(buf.readLine());
        while (true) {
            if (!"".equals(requestBuffer.toString()) && !"null".equals(requestBuffer.toString())) {//server 无数据请求时,requestBuffer为"null"
                Log.i(TAG, "keyword request :"+requestBuffer.toString());
                Request request = gson.fromJson(decode(requestBuffer.toString()), Request.class);
                Response response = null;
                try {
                    response = requestHandler.handle(request);
                    response.setId(request.getId());
                } catch (Exception e) {
                    response = makeErrorResponse(request, e);
                }
                Log.i(TAG, "keyword decode response :"+decode(gson.toJson(response)));
                String responseJson = encode(gson.toJson(response)) + "\n";
                writer.write(responseJson);
                writer.flush();
            }
            requestBuffer.delete(0, requestBuffer.length());
            requestBuffer.append(buf.readLine());
        }
    }

    private Response makeErrorResponse(Request request, Throwable e) {
        Response response;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type",e.getClass().getName());
        entity.put("detail message",stringWriter.toString());
        response = new  Response(request.getId(), Response.RESULT_FAIL, "未知错误-请联系管理员", entity);
        return response;
    }


    private String decode(String srcStr) {
        srcStr = srcStr.replace("%e", "%");
        srcStr = srcStr.replace("%n", "\n");
        return srcStr;
    }

    private String encode(String srcStr) {
        srcStr = srcStr.replace("%", "%e");
        srcStr = srcStr.replace("\n", "%n");
        return srcStr;
    }
}
