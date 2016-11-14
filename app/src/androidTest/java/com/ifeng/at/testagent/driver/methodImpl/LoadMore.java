package com.ifeng.at.testagent.driver.methodImpl;

import android.util.Log;
import android.widget.ListView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.Map;


/**
 * Owner liuru
 */
public class LoadMore extends RPCMethod {

    public LoadMore(){
        setArgsNumber(1);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {
        //获取listView，so: 参数：listView，count
        RPCMessage response;

        int hash = Integer.parseInt((String) request.getArgs().get(0));//获取hashcode

        ListView listView = (ListView) varCache.get(hash);

        int []  location=new int[2];
        listView.getLocationOnScreen(location);
        listView.getClipBounds();
        int  listX=location[0];  //listView在界面上的X位置
        int  listY=location[1];  //listView在界面上的Y位置

        float startX0=(float)listX+listView.getWidth()/2;
        float startY0=(float)listY+listView.getHeight()-1;   //-1是为了让拖拽的起点错开边界值
        float  endX0=(float)listX+listView.getWidth()/2;
        float  endY0=(float)listY;

        try{
            solo.drag(startX0,endX0,startY0,endY0,30);
        }catch (Throwable e){
            response = ErrorResponseHelper.makeViewNotFoundErrorResponse(getClass());
            return  response;
        }

        return RPCMessage.makeSuccessResult();
    }
}
