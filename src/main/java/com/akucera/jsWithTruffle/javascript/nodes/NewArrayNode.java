package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 5.2.17.
 */
public class NewArrayNode extends JSNode {

    public NewArrayNode() {
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return new ArrayNode();
    }

    @Override
    public String toString() {
        return "[]";
    }
}