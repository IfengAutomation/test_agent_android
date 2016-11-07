package com.ifeng.at.testagent.reflect;

import android.view.View;
import android.widget.TextView;

import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoye on 2016/11/3.
 *
 */

public class InstanceProxyHelper {
    private InstanceProxyHelper(){
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

    public static Args getArgsFromRPCMessage(RPCContext context, List<Object> args) throws ReflectionException {
        Args reflectionArgs = new Args();
        for(int i=0; i<args.size(); i++){
            Object arg = args.get(i);
            String type = ((String)arg).substring(0, 1);
            String value = ((String)arg).substring(2, ((String) arg).length());
            if(ArgumentType.CLASS.equals(type)){
                try {
                    Class argClazz = Class.forName(value);
                    reflectionArgs.getArgTypes().add(Class.class);
                    reflectionArgs.getArgs().add(argClazz);
                } catch (ClassNotFoundException e) {
                    throw new ReflectionException("Argument class not found", e);
                }
            }else if(ArgumentType.INT.equals(type)){
                reflectionArgs.getArgTypes().add(Integer.class);
                reflectionArgs.getArgs().add(Integer.parseInt(value));
            }else if(ArgumentType.STRING.equals(type)){
                reflectionArgs.getArgTypes().add(String.class);
                reflectionArgs.getArgs().add(value);
            }else if(ArgumentType.VAR.equals(type)){
                int hash = Integer.parseInt(value);
                if(!context.getVars().containsKey(hash)){
                    throw new ReflectionException("Argument remote object not found");
                }
                Object var = context.getVars().get(hash);
                reflectionArgs.getArgTypes().add(var.getClass());
                reflectionArgs.getArgs().add(var);
            }else{
                throw new ReflectionException("Unknown arguments type");
            }
        }
        return reflectionArgs;
    }


    public static class Args{
        private List<Class> argTypes = new ArrayList<>();
        private List<Object> args = new ArrayList<>();

        public List<Class> getArgTypes() {
            return argTypes;
        }

        public List<Object> getArgs() {
            return args;
        }

        public Class[] getArgTypesArray(){
            Class[] typesArray = new Class[argTypes.size()];
            return argTypes.toArray(typesArray);
        }

        public Object[] getArgsArray(){
            Object[] argsArray = new Object[args.size()];
            return args.toArray(argsArray);
        }
    }
}
