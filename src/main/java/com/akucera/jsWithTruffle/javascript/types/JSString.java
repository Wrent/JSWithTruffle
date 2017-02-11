package com.akucera.jsWithTruffle.javascript.types;

/**
 * Created by akucera on 25.12.16.
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
