package com.dynali;

class MyIPResponse {
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

    int getCode() {
        return code;
    }

    void setMessage(String message) {
        this.message = message;
    }

    String getMessage() {
        return message;
    }

    void setData(Data data) {
        this.data = data;
    }

    Data getData() {
        return data;
    }
}
