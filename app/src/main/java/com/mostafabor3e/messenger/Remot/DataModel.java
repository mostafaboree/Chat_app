package com.mostafabor3e.messenger.Remot;

public class DataModel {
    private String sendername;
    private String message;

    public DataModel() {
    }

    public DataModel(String sendername, String message) {
        this.sendername = sendername;
        this.message = message;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
