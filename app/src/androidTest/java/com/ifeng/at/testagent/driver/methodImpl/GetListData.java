package com.ifeng.at.testagent.driver.methodImpl;

import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ifeng.at.testagent.driver.RPCMethod;
import com.ifeng.at.testagent.rpc.RPCMessage;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * Owner liuru
 */
public class GetListData extends RPCMethod {

    public  GetListData(){
        setArgsNumber(1);
    }

    @Override
    public RPCMessage handleRequest(RPCMessage request, Solo solo, Map<Integer, Object> varCache) {

        int count;
        int code;
        String resource_id = "";
        String packageName = "";
        String className = "";
        String contentDescription = "";

        RPCMessage response;
        int hash = Integer.parseInt((String) request.getArgs().get(0));

        ListView listView = (ListView) varCache.get(hash);
        try{
            HeaderViewListAdapter listViewAdapter = (HeaderViewListAdapter) listView.getAdapter();
            ListAdapter mAdapter =  listViewAdapter.getWrappedAdapter();
            code = mAdapter.hashCode();
            count = mAdapter.getCount();
        }catch (Throwable e){
            response = RPCMessage.makeFailResult(e.getMessage());
            return response;
        }

        Map<String, Object> entity = new HashMap<>();
        entity.put("code", code + "");
        entity.put("text", count+"");
        entity.put("resource_id", resource_id);
        entity.put("class_name", className);
        entity.put("package_name", packageName);
        entity.put("content_desc", contentDescription);
        return RPCMessage.makeSuccessResult(entity);
    }
}
