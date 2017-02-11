package com.akucera.jsWithTruffle.javascript.types;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 25.12.16.
 */
public class JSNull extends JSNode {
    public final Object value = null;

    public JSNull() {
    }

    @Override
    public JSNull executeNullNode(VirtualFrame virtualFrame) {
        return null;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
