package com.akucera.jsWithTruffle.javascript;

import java.util.Arrays;

import com.akucera.jsWithTruffle.ReadArgumentNode;
import com.akucera.jsWithTruffle.javascript.nodes.JSVarNodeGen;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class JSRootNode extends RootNode {
    @Children
    private final JSNode[] bodyNodes;

    public JSRootNode(JSNode[] bodyNodes,
                      FrameDescriptor frameDescriptor) {
        super(JSLanguage.class, null, frameDescriptor);
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        int secondToLast = this.bodyNodes.length - 1;
        CompilerAsserts.compilationConstant(secondToLast);
        for (int i = 0; i < secondToLast; i++) {
            this.bodyNodes[i].execute(virtualFrame);
        }
        return this.bodyNodes[secondToLast].execute(virtualFrame);
    }

    public static JSRootNode create(FrameSlot[] argumentNames,
                                    JSNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        JSNode[] allNodes = new JSNode[ argumentNames.length
                + bodyNodes.length];
        for (int i = 0; i < argumentNames.length; i++) {
            allNodes[i] = JSVarNodeGen.create(
                    new ReadArgumentNode(i), argumentNames[i]);
        }
        System.arraycopy(bodyNodes, 0, allNodes,
                argumentNames.length, bodyNodes.length);
        return new JSRootNode(allNodes, frameDescriptor);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.bodyNodes);
    }
}
