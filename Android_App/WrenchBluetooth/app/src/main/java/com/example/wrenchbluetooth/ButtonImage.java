package com.example.wrenchbluetooth;

public class ButtonImage {
    private String keyNumber;
    private Integer image;

    public ButtonImage(String keyNumber, Integer image) {
        this.keyNumber = keyNumber;
        this.image = image;
    }

    public String getKey() {
        return this.keyNumber;
    }

    public Integer getImage() {
        return this.image;
    }
}
