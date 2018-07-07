package com.example.romek95a.bluetooth;

/**
 * Created by romek95a on 07.07.2018.
 */

public class MessageWithType {
    public MessageWithType(String message, boolean inOut){
        this.message=message;
        this.inOut=inOut;
    }
    String message;
    boolean inOut;
}
