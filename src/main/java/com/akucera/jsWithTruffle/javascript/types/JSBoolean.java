package com.akucera.jsWithTruffle.javascript.types;

/**
 * Boolean type.
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
