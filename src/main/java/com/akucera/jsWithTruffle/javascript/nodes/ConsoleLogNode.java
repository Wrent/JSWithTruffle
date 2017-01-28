package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.BuiltinNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 25.12.16.
 */
public class ConsoleLogNode extends BuiltinNode {
    private final Object value;

    public ConsoleLogNode(Object value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        System.out.println(value);
        return value;
    }

    @Override
    public String toString() {
        return "ConsoleLogNode{" +
                "value=" + value +
                '}';
    }
}
