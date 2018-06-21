package com.ua.driver.exception;

public class WrongIdException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong id!";
    }
}
