package com.ua.driver.exception;

public class CarNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Car not found";
    }

}