package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSArray;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node, which creates new Array.
 */
public class NewArrayNode extends JSNode {

    public NewArrayNode() {
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return new JSArray<>();
    }

    @Override
    public String toString() {
        return "[]";
    }
}
