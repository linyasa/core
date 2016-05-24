package com.dotcms.rest;

/**
 * Created by freddyrodriguez on 24/5/16.
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
