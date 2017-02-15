package com.akucera.jsWithTruffle.javascript.types;


import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Undefined type
 */
public class JSUndefined extends JSNode {
    public final Object value = null;

    public JSUndefined() {
    }

    @Override
    public JSUndefined executeUndefinedNode(VirtualFrame virtualFrame) {
        return this;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this;
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
