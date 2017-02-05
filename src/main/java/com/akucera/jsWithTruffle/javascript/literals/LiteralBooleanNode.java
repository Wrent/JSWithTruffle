package com.akucera.jsWithTruffle.javascript.literals;

import com.akucera.jsWithTruffle.javascript.types.JSBoolean;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 25.12.16.
 */
public class LiteralBooleanNode extends JSNode {
    public final JSBoolean bool;

    public LiteralBooleanNode(JSBoolean bool) {
        this.bool = bool;
    }


    @Override
    public JSBoolean executeBooleanNode(VirtualFrame virtualFrame) {
        return this.bool;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        //System.out.println(this.getClass().getSimpleName().toString() + " executed, returning "+ this.bool);
        return this.bool;
    }

    @Override
    public String toString() {
        return this.bool.toString();
    }
}
