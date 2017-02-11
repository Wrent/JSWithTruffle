package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSFunction;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import jdk.nashorn.internal.ir.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Stack;
import java.util.stream.StreamSupport;

/**
 * Created by akucera on 25.12.16.
 */
public class JSImpl extends JS {
    private List<JSNode> statements;
    private Stack<FrameDescriptor> frameDescriptors;

    @Override
    public void prepare(List<Statement> statements, InputStream in, PrintStream out) {
        try {
            this.statements = StatementsHandler.handle(statements, in, out);
            this.frameDescriptors = StatementsHandler.frameDescriptors;
        } catch (UnknownSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() throws IOException {
        VirtualFrame topFrame = createTopFrame(frameDescriptors.peek());
        execute(statements, topFrame);
    }

    private VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
                new Object[] {}, frameDescriptor);
        return virtualFrame;
    }


    private Object execute(List<JSNode> nodes, VirtualFrame topFrame) {
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        JSFunction function = JSFunction.create(new FrameSlot[] {},
                StreamSupport.stream(nodes.spliterator(), false)
                        .toArray(size -> new JSNode[size]),
                frameDescriptor);
        DirectCallNode directCallNode = Truffle.getRuntime()
                .createDirectCallNode(function.callTarget);
        return directCallNode.call(
                topFrame,
                new Object[] {topFrame.materialize()});
    }

}
