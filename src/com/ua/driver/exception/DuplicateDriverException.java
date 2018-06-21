package com.ua.driver.exception;

public class DuplicateDriverException extends Exception {

    @Override
    public String getMessage() {
        return "There is such driver already!";
    }
}
