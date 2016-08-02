package com.ifeng.at.testagent.rpc;

/**
 * Created by lr on 2016/8/2.
 */
public class ResponseError {

    /***
     * Error 可设置的值
     */
    public String errorViewNotFound = "View not found.";

    public String errorArgsNumberWrong(String methodName, int needNumber, int givenNumber){
        String errorMsg;

        if (needNumber == 0 || needNumber == 1){
            errorMsg = methodName + " takes exactly " + needNumber + " argument (" + givenNumber + " given)";
        } else {
            errorMsg = methodName + " takes exactly " + needNumber + " arguments (" + givenNumber + " given)";
        }
        return errorMsg;
    }

    public String errorMethodNotRegister(String methodName){
        return "Method:" + methodName + " has not been registered. ";
    }
}
