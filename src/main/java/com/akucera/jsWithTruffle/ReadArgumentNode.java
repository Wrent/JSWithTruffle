package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 4.2.17.
 */
public class ReadArgumentNode extends JSNode {
    public final int argumentIndex;

    public ReadArgumentNode(int argumentIndex) {
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (!this.isArgumentIndexInRange(virtualFrame, this.argumentIndex)) {
            throw new IllegalArgumentException("Not enough arguments passed");
        }
        return this.getArgument(virtualFrame, this.argumentIndex);
    }
}
