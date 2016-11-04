package com.ifeng.at.testagent.reflect;

import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class InstanceProxyFactory {
    private InstanceProxyFactory(){
    }

    public static Map<String, String> getProxyFromInstance(Object instance){
        Map<String, String> proxyObject = new HashMap<>();
        proxyObject.put("hash", ""+instance.hashCode());
        proxyObject.put("class", instance.getClass().getName());
        if(instance instanceof View){
            proxyObject.put("resource-id",
                    ((View) instance).getResources().getResourceName(((View) instance).getId()));
        }
        if(instance instanceof TextView){
            proxyObject.put("text", ((TextView) instance).getText().toString());
        }
        return proxyObject;
    }
}
