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
    private Socket clientSocket;
    private String TAG = "RpcClient ";
    private Gson gson = new Gson();
    private Writer writer;
    private BufferedReader reader;
    private Map<Integer, RequestHandler> handlers = new HashMap<>();

    public void addHandler(int ver, RequestHandler requestHandler){
        handlers.put(ver, requestHandler);
    }

    public void startAndBlock(String host, int port, String deviceId) throws IOException {
        Log.i(TAG, "connect to "+host+" id="+deviceId);
        clientSocket = new Socket(host, port);        //绑定server
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        writer = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
        registerToServer(deviceId);
        listenToServer();
        clientSocket.close();
    }

    private void registerToServer(String deviceId) throws IOException {
        RPCMessage registerMsg = new RPCMessage();
        registerMsg.setMsgId(1);
        registerMsg.setName("register");
        registerMsg.setMsgType(RPCMessage.RPCCall);
        registerMsg.getArgs().add(deviceId);
        writer.write(gson.toJson(registerMsg) + '\n');
        writer.flush();
        String serverResponse = reader.readLine();
        //TODO
    }


    /**
     * 监听server 请求
     *
     * @throws IOException
     */
    private void listenToServer() throws IOException {
        StringBuilder requestBuffer = new StringBuilder();
        requestBuffer.append(reader.readLine());
        while (true) {
            if (!"".equals(requestBuffer.toString()) && !"null".equals(requestBuffer.toString())) {//server 无数据请求时,requestBuffer为"null"
                Log.i(TAG, "keyword request :" + requestBuffer.toString());
                RPCMessage message = RPCMessage.fromJson(decode(requestBuffer.toString()));
                RPCMessage response;
                if(message.getMsgType() == RPCMessage.RPC_KILL_SIGNAL){
                    // receive kill signal
                    response = RPCMessage.makeKillSignal();
                    String responseJson = encode(gson.toJson(response)) + "\n";
                    writer.write(responseJson);
                    writer.flush();
                    break;
                }
                try {
                    response = handlers.get(message.getVersion()).handle(message);
                    response.setMsgType(RPCMessage.RPCResult);
                    response.setMsgId(message.getMsgId());
                } catch (Throwable e) {
                    response = makeErrorResponse(message, e);
                }
                Log.i(TAG, "keyword decode response :" + decode(gson.toJson(response)));
                String responseJson = encode(gson.toJson(response)) + "\n";
                writer.write(responseJson);
                writer.flush();
            }
            requestBuffer.delete(0, requestBuffer.length());
            requestBuffer.append(reader.readLine());
        }
    }

    private RPCMessage makeErrorResponse(RPCMessage request, Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        Map<String, Object> entity = new HashMap<>();
        entity.put("type", e.getClass().getName());
        entity.put("detail message", stringWriter.toString());
        return RPCMessage.makeFailResult("未知错误-请联系管理员", entity);
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
