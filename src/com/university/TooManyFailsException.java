package com.university;

public class TooManyFailsException extends Exception {
    public TooManyFailsException(String msg) {
        super(msg);
    }
}
