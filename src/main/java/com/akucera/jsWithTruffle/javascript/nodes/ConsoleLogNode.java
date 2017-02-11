package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.BuiltinNode;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.io.PrintStream;

/**
 * Created by akucera on 25.12.16.
 */
public class ConsoleLogNode extends BuiltinNode {
    private final JSNode value;
    private final PrintStream out;

    public ConsoleLogNode(JSNode value, PrintStream out) {
        this.value = value;
        this.out = out;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        //System.out.println(this.getClass().getSimpleName().toString() + " executed");
        out.println(value.execute(virtualFrame));
        return value;
    }

    @Override
    public String toString() {
        return "ConsoleLogNode{" +
                "value=" + value +
                '}';
    }
}
