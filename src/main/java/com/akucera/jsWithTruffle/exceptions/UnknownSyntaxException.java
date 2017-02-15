package com.akucera.jsWithTruffle.exceptions;

/**
 * Exception to be thrown if there is something in the Javascript code we cannot handle.
 */
public class UnknownSyntaxException extends Throwable{

    public UnknownSyntaxException() {
        super();
    }

    public UnknownSyntaxException(String message) {
        super(message);
    }
}
