package ru.topjava.voting.util.exception;

public class IllegalRequestDataException extends RuntimeException{
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}
