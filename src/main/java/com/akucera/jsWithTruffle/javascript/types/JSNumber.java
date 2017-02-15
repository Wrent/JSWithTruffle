package com.akucera.jsWithTruffle.javascript.types;

/**
 * Integer type.
 */
public class JSNumber {
    public final Number value;

    public JSNumber(Number number) {
        this.value = number;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public int getInt() {
        return this.value.intValue();
    }
}
