package com.dynali;

public class DynaliException extends Exception {
    public DynaliException(int code, String message) {
        System.out.println("[" + code + "] " + message);
    }
}
