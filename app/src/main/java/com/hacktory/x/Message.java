package com.hacktory.x;

/**
 * Pojedyncza wiadomosc
 * Created by paweldylag on 07/11/15.
 */
public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
