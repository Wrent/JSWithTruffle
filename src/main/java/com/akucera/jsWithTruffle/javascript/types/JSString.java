package com.akucera.jsWithTruffle.javascript.types;

/**
 * String type.
 */
public class JSString {
    public final String value;

    public JSString(String string) {
        this.value = string;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
