package com.akucera.jsWithTruffle.javascript.types;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Null type.
 */
public class JSNull extends JSNode {
    public final Object value = null;

    public JSNull() {
    }

    @Override
    public JSNull executeNullNode(VirtualFrame virtualFrame) {
        return this;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this;
    }

    @Override
    public String toString() {
        return "null";
    }
}
