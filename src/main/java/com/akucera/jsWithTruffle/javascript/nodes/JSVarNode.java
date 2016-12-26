package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

/**
 * Created by akucera on 25.12.16.
 */
@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class JSVarNode extends JSNode {
    protected abstract FrameSlot getSlot();
    protected abstract Node getValueNode();

    @Specialization
    protected Object write(VirtualFrame virtualFrame, Object value) {
        FrameSlot slot = this.getSlot();
        if (slot.getKind() != FrameSlotKind.Object) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            slot.setKind(FrameSlotKind.Object);
        }
        virtualFrame.setObject(slot, value);
        return value;
    }
}
