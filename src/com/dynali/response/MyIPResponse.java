package com.dynali.response;

public class MyIPResponse extends Response {
    private String status;
    private int code;
    private String message;
    private Data data;

    void setStatus(String status) {
        this.status = status;
    }

    String getStatus() {
        return status;
    }

    void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
