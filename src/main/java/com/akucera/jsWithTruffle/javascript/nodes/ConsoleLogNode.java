package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.BuiltinNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 25.12.16.
 */
public class ConsoleLogNode extends BuiltinNode {
    public Object consoleLog(Object value) {
        System.out.println(value);
        return value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return null;
    }
}
