package com.akucera.jsWithTruffle.exceptions;

/**
 * Created by akucera on 26.12.16.
 */
public class UnknownSyntaxException extends Throwable{

    public UnknownSyntaxException() {
        super();
    }

    public UnknownSyntaxException(String message) {
        super(message);
    }
}
