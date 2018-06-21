package com.ua.driver.exception;

public class DriverNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Driver not found";
    }
}
