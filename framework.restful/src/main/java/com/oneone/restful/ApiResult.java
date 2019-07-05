package com.oneone.restful;

/**
 * @author qingfei.chen
 * @since 2017-01-18 11:44:57
 */
public class ApiResult<T> {

    private String message;
    private int status = -1;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
