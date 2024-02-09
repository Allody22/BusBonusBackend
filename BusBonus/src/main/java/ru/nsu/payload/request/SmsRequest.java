package ru.nsu.payload.request;

import lombok.Data;

@Data
public class SmsRequest {
    private String user;
    private String pass;
    private String gzip;
    private String comment;
    private String clientAddr;
    private String httpAcceptLanguage;

    // Геттеры и сеттеры
}

