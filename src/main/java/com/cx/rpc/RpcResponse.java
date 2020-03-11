package com.cx.rpc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 13:16
 * Description: No Description
 */
public class RpcResponse implements Serializable{
    private static final long serialVersionUID = 1L;
    private Throwable error;
    private Object result;

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
