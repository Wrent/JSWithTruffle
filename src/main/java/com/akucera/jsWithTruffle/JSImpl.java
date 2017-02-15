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
 * Our Javascript implementation main class.
 */
public class JSImpl extends JS {
    private List<JSNode> statements;
    private Stack<FrameDescriptor> frameDescriptors;

    /**
     * Creates JS statements to be executed later.
     * @param statements
     * @param in
     * @param out
     */
    @Override
    public void prepare(List<Statement> statements, InputStream in, PrintStream out) {
        try {
            this.statements = StatementsHandler.handle(statements, in, out);
            this.frameDescriptors = StatementsHandler.frameDescriptors;
        } catch (UnknownSyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes statements.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        VirtualFrame topFrame = createTopFrame(frameDescriptors.peek());
        execute(statements, topFrame);
    }

    /**
     * Creates top frame just for running the main body of JS.
     * @param frameDescriptor
     * @return
     */
    private VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
                new Object[] {}, frameDescriptor);
        return virtualFrame;
    }

    /**
     * Executes the JSNodes with given topFrame.
     * @param nodes
     * @param topFrame
     * @return
     */
    private Object execute(List<JSNode> nodes, VirtualFrame topFrame) {
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        //makes the body of JS the main function
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
