package com.github.hyota.asciiartboardreader.domain.model;

public class NetworkException extends RuntimeException {

    private int responseCode;

    public NetworkException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
