package ru.topjava.voting.util.exception;

public class DoubleViolationException extends RuntimeException{
    public DoubleViolationException(String message) {
        super(message);
    }
}