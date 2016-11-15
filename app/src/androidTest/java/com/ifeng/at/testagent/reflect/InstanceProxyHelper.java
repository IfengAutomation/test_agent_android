package com.ifeng.at.testagent.reflect;

import android.view.View;
import android.widget.TextView;

import com.ifeng.at.testagent.reflect.exceptions.ReflectionException;

import java.util.ArrayList;
import java.util.Arrays;
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
        proxyObject.put("class_name", instance.getClass().getName());
        if(instance instanceof View){
            proxyObject.put("resource_id",
                    ((View) instance).getResources().getResourceName(((View) instance).getId()));
            proxyObject.put("package_name",((View) instance).getContext().getPackageName());
            CharSequence contentDesc = ((View) instance).getContentDescription();
            if(contentDesc == null){
                proxyObject.put("content_desc", "");
            }else{
                proxyObject.put("content_desc", contentDesc.toString());
            }
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
            String type = ((String)arg).substring(0, 2);
            String value = ((String)arg).substring(2);
            if(ArgumentType.CLASS.equals(type)){
                try {
                    Class argClazz = Class.forName(value);
                    reflectionArgs.getArgTypes().add(Class.class);
                    reflectionArgs.getArgs().add(argClazz);
                } catch (ClassNotFoundException e) {
                    throw new ReflectionException("Argument class not found", e);
                }
            }
            else if(ArgumentType.INT.equals(type)){
                reflectionArgs.getArgTypes().add(int.class);
                reflectionArgs.getArgs().add(Integer.parseInt(value));
            }
            else if(ArgumentType.STRING.equals(type)){
                reflectionArgs.getArgTypes().add(String.class);
                reflectionArgs.getArgs().add(value);
            }
            else if(ArgumentType.FLOAT.equals(type)){
                reflectionArgs.getArgTypes().add(float.class);
                reflectionArgs.getArgs().add(Float.parseFloat(value));
            }
            else if(ArgumentType.OBJECT.equals(type)){
                int splitIndex = value.indexOf(":");
                String hashStr = value.substring(0, splitIndex);
                String className = value.substring(splitIndex+1);
                Class clazz;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new ReflectionException("Argument class not found", e);
                }
                int hash = Integer.parseInt(hashStr);
                if(!context.getVars().containsKey(hash)){
                    throw new ReflectionException("Argument remote object not found");
                }
                Object var = context.getVars().get(hash);
                reflectionArgs.getArgTypes().add(clazz);
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

        public Class[] getArgTypesArray(int start){
            return Arrays.copyOfRange(getArgTypesArray(), start, argTypes.size());
        }

        public Class[] getArgTypesArray(int start, int end){
            return Arrays.copyOfRange(getArgTypesArray(), start, end);
        }

        public Object[] getArgsArray(){
            Object[] argsArray = new Object[args.size()];
            return args.toArray(argsArray);
        }

        public Object[] getArgsArray(int start){
            return Arrays.copyOfRange(getArgsArray(), start, args.size());
        }

        public Object[] getArgsArray(int start, int end){
            return Arrays.copyOfRange(getArgsArray(), start, end);
        }
    }
}
