package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.BuiltinNode;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 25.12.16.
 */
public class ConsoleLogNode extends BuiltinNode {
    private final JSNode value;

    public ConsoleLogNode(JSNode value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        //System.out.println(this.getClass().getSimpleName().toString() + " executed");
        System.out.println(value.execute(virtualFrame));
        return value;
    }

    @Override
    public String toString() {
        return "ConsoleLogNode{" +
                "value=" + value +
                '}';
    }
}
