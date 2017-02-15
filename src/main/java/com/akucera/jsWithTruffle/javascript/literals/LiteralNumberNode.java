package com.akucera.jsWithTruffle.javascript.literals;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSNumber;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Representation of simple integer node.
 */
public class LiteralNumberNode extends JSNode {
    public final JSNumber number;

    public LiteralNumberNode(JSNumber number) {
        this.number = number;
    }

    @Override
    public JSNumber executeNumberNode(VirtualFrame virtualFrame) {
        return this.number;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame)
    {
        //System.out.println(this.getClass().getSimpleName().toString() + " executed, returning "+ this.number);
        return this.number;
    }

    @Override
    public String toString() {
        return this.number.toString();
    }
}
