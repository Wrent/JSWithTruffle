package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 28.1.17.
 */
public class SymbolNode extends JSNode {
    public final FrameSlot slot;

    public SymbolNode(FrameSlot slot) {
        this.slot = slot;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        Frame frame = virtualFrame;
        Object value = frame.getValue(this.slot);
        while (value == null) {
            frame = this.getLexicalScope(frame);
            if (frame == null) {
                throw new RuntimeException("Unknown variable: " +
                        this.slot.getIdentifier());
            }
            value = frame.getValue(this.slot);
        }
        return value;
    }

    private Frame getLexicalScope(Frame frame) {
        return (Frame) frame.getArguments()[0];
    }

    @Override
    public String toString() {
        return "(symbol '"+slot.getIdentifier() +")";
    }
}
