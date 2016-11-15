package com.ifeng.at.testagent.reflect;


import java.lang.reflect.Field;

public class DataInterfaceHelper {
    private static String HTTP_VCIS_IFENG_COM = "http://vcis.ifeng.com";
    private static String HTTP_VCSP_IFENG_COM = "http://vcsp.ifeng.com";
    private static String HTTP_V_IFENG_COM = "http://v.ifeng.com";
    private static String HTTP_COMMENT_IFENG_COM = "http://comment.ifeng.com";

    public static void setHost(String host) throws ClassNotFoundException, IllegalAccessException {
        String className = "com.ifeng.video.dao.db.constants.DataInterface";
        Class dataInterface = Class.forName(className);
        Field[] fields = dataInterface.getDeclaredFields();
        for(Field field: fields){
            if(field.getType() == String.class){
                modifyHost(dataInterface, field, host);
            }
        }
    }

    private static void modifyHost(Object instance, Field field, String host) throws IllegalAccessException {
        field.setAccessible(true);
        String value = (String) field.get(instance);
        if(value.startsWith(HTTP_VCIS_IFENG_COM)){
            value = value.replaceFirst(HTTP_VCIS_IFENG_COM, "http://"+host);
            field.set(instance, value);
        }else if(value.startsWith(HTTP_VCSP_IFENG_COM)){
            value = value.replaceFirst(HTTP_VCSP_IFENG_COM, "http://"+host);
            field.set(instance, value);
        }else if(value.startsWith(HTTP_V_IFENG_COM)){
            value = value.replaceFirst(HTTP_V_IFENG_COM, "http://"+host);
            field.set(instance, value);
        }else if(value.startsWith(HTTP_COMMENT_IFENG_COM)){
            value = value.replaceFirst(HTTP_COMMENT_IFENG_COM, "http://"+host);
            field.set(instance, value);
        }
    }
}
