package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSUndefined;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node which represents a symbol (pointer to a variable) which has to be executed.
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
                return new JSUndefined();
            }
            value = frame.getValue(this.slot);
        }
        //System.out.println(this.getClass().getSimpleName().toString() + " executed, returning "+value);
        return value;
    }

    private Frame getLexicalScope(Frame frame) {
        if (frame.getArguments().length > 0) {
            return (Frame) frame.getArguments()[0];
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(symbol '"+slot.getIdentifier() +")";
    }
}
