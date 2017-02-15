package com.akucera.jsWithTruffle.javascript.literals;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSString;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Representation of simple boolean node.
 */
public class LiteralStringNode extends JSNode{
    public final JSString string;

    public LiteralStringNode(JSString string) {
        this.string = string;
    }

    @Override
    public JSString executeStringNode(VirtualFrame virtualFrame) {
        return this.string;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        //System.out.println(this.getClass().getSimpleName().toString() + " executed, returning "+ this.string);
        return this.string;
    }

    @Override
    public String toString() {
        return this.string.toString();
    }
}
