package com.geekbrains.shop.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class JulyMarketError {
    private int status;
    private String message;
    private Date timestamp;

    public JulyMarketError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
