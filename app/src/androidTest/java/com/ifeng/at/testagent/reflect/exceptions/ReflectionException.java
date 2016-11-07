package com.ifeng.at.testagent.reflect.exceptions;

/**
 * Created by zhaoye on 2016/11/4.
 *
 */

public class ReflectionException extends Exception {

    public ReflectionException() {
        super();
    }

    public ReflectionException(String detailMessage) {
        super(detailMessage);
    }

    public ReflectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
