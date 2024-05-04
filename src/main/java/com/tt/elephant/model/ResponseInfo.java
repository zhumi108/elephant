package com.tt.elephant.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseInfo {
    private String code;
    private String msg;
    private Object data;
    private boolean flag;

    public static ResponseInfo getInstance() {
        return new ResponseInfo();
    }
    public ResponseInfo(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.flag = true;
    }
    public ResponseInfo(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.flag = false;
    }
    public ResponseInfo() {
        this.flag = false;

    }

    public static ResponseInfo success(Object data) {
        ResponseInfo responseInfo = getInstance();
        responseInfo.flag = true;
        responseInfo.setData(data);
        return responseInfo;
    }
    public static ResponseInfo success(String msg){
        ResponseInfo responseInfo = getInstance();
        responseInfo.flag = true;
        responseInfo.setMsg(msg);
        return responseInfo;
    }
    public static ResponseInfo success(String msg, Object data) {
        ResponseInfo responseInfo = getInstance();
        responseInfo.flag = true;
        responseInfo.setMsg(msg);
        responseInfo.setData(data);
        return responseInfo;
    }
    public static ResponseInfo success() {
        ResponseInfo responseInfo = getInstance();
        responseInfo.flag = true;
        return responseInfo;
    }
    public static ResponseInfo fail(String msg) {
        ResponseInfo responseInfo = getInstance();
        responseInfo.setMsg(msg);
        return responseInfo;
    }
    public static ResponseInfo fail(String code, String msg) {
        ResponseInfo responseInfo = getInstance();
        responseInfo.setCode(code);
        responseInfo.setMsg(msg);
        return responseInfo;
    }

    public static ResponseInfo fail(String code, Exception e) {
        ResponseInfo responseInfo = getInstance();
        responseInfo.setCode(code);
        if (e != null) {
            responseInfo.setMsg(e.getLocalizedMessage());
        }
        return responseInfo;
    }

}
