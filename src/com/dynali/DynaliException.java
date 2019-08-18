package com.dynali;

class DynaliException extends Exception {
    DynaliException(int code, String message) {
        System.out.println("[" + code + "] " + message);
    }
}
