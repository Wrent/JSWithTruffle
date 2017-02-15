package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSArray;
import com.oracle.truffle.api.frame.VirtualFrame;


/**
 * Node, which creates new Array.
 */
public class ArrayNode extends JSNode {
    public final JSArray<Object> array;

    public ArrayNode() {
        this.array = new JSArray<>();
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.array;
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
