package com.akucera.jsWithTruffle.javascript;

import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

/**
 * Basic abstract class representing a node in our language.
 */
@NodeInfo(language = "Javascript Language", description = "The abstract base node for all expressions")
public abstract class JSNode extends Node{

    /**
     * Basic execute function.
     * @param virtualFrame
     * @return
     */
    public abstract Object execute(VirtualFrame virtualFrame);

    //helper functions for truffle follow
    public JSNumber executeNumberNode(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSNumber(
                this.execute(virtualFrame));
    }

    public JSBoolean executeBooleanNode(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSBoolean(
                this.execute(virtualFrame));
    }

    public JSString executeStringNode(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSString(
                this.execute(virtualFrame));
    }

    public JSNull executeNullNode(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSNull(
                this.execute(virtualFrame));
    }

    public JSUndefined executeUndefinedNode(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSUndefined(
                this.execute(virtualFrame));
    }

    public JSFunction executeJSFunction(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return com.akucera.jsWithTruffle.javascript.JSTypesGen.JSTYPES.expectJSFunction(
                this.execute(virtualFrame));

    }

    protected boolean isArgumentIndexInRange(VirtualFrame virtualFrame,
                                             int index) {
        return (index + 1) < virtualFrame.getArguments().length;
    }

    protected Object getArgument(VirtualFrame virtualFrame, int index) {
        return virtualFrame.getArguments()[index + 1];
    }
}
