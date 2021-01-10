package ru.topjava.voting.util.exception;

public class ErrorInfo {
    private final String url;
    private final String detail;

    public ErrorInfo(CharSequence url, String detail) {
        this.url = url.toString();
        this.detail = detail;
    }
}