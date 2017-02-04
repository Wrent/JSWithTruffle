package com.akucera.jsWithTruffle.javascript.types;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.JSRootNode;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;

/**
 * Created by akucera on 30.1.17.
 */
public class JSFunction {
    //used purely as main function and nothing else? could implement functions as well though...
    public final RootCallTarget callTarget;
    private MaterializedFrame lexicalScope;

    public JSFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public MaterializedFrame getLexicalScope() {
        return this.lexicalScope;
    }

    public void setLexicalScope(MaterializedFrame lexicalScope) {
        this.lexicalScope = lexicalScope;
    }

    public static JSFunction create(FrameSlot[] arguments,
                                    JSNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        return new JSFunction(
                Truffle.getRuntime().createCallTarget(
                        JSRootNode.create(arguments, bodyNodes, frameDescriptor)));
    }
}
