package com.ifeng.at.testagent.reflect;


import java.lang.reflect.Field;

public class DataInterfaceHelper {
    private static String HTTP_VCIS_IFENG_COM = "http://vcis.ifeng.com";
    private static String HTTP_VCSP_IFENG_COM = "http://vcsp.ifeng.com";
    private static String HTTP_V_IFENG_COM = "http://v.ifeng.com";
    private static String HTTP_COMMENT_IFENG_COM = "http://comment.ifeng.com";
    private static String HTTPS_ID_IFENG_COM = "https://id.ifeng.com";
    private static String VIDEO_AD_CONFIG_URL = "http://c0.ifengimg.com";
    private static String HTTP_EXP_3G_IFENG_COM = "http://exp.3g.ifeng.com";

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
            value = value.replaceFirst(HTTP_VCIS_IFENG_COM, "http://"+host+"/vcis.ifeng.com");
            field.set(instance, value);
        }else if(value.startsWith(HTTP_VCSP_IFENG_COM)){
            value = value.replaceFirst(HTTP_VCSP_IFENG_COM, "http://"+host+"/vcsp.ifeng.com");
            field.set(instance, value);
        }else if(value.startsWith(HTTP_V_IFENG_COM)){
            value = value.replaceFirst(HTTP_V_IFENG_COM, "http://"+host+"/v.ifeng.com");
            field.set(instance, value);
        }else if(value.startsWith(HTTP_COMMENT_IFENG_COM)){
            value = value.replaceFirst(HTTP_COMMENT_IFENG_COM, "http://"+host+"/comment.ifeng.com");
            field.set(instance, value);
        }else if(value.startsWith(HTTPS_ID_IFENG_COM)){
            value = value.replaceFirst(HTTPS_ID_IFENG_COM, "http://"+host+"/id.ifeng.com");
            field.set(instance, value);
        }else if(value.startsWith(VIDEO_AD_CONFIG_URL)){
            value = value.replaceFirst(VIDEO_AD_CONFIG_URL, "http://"+host+"/c0.ifengimg.com");
            field.set(instance, value);
        }else if(value.startsWith(HTTP_EXP_3G_IFENG_COM)){
            value = value.replaceFirst(HTTP_EXP_3G_IFENG_COM, "http://"+host+"/exp.3g.ifeng.com");
            field.set(instance, value);
        }
    }
}
