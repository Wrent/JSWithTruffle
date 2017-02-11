package com.akucera.jsWithTruffle.javascript.types;

/**
 * Created by akucera on 25.12.16.
 */
public class JSBoolean {
    public final boolean value;

    public JSBoolean(Boolean bool) {
        this.value = bool;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }
}
