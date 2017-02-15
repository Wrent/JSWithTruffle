package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.BuiltinNode;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.io.PrintStream;

/**
 * Node representing output to console.
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
        //writes to output stream
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
