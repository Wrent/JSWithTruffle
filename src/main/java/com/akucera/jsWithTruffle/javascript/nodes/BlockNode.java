package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

/**
 * Created by akucera on 28.1.17.
 */
public class BlockNode extends JSNode {
    public final List<JSNode> statements;

    public BlockNode(List<JSNode> statements) {
        this.statements = statements;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        System.out.println(this.getClass().getSimpleName().toString() + " executed, executing statements returning "+ null);
        for (JSNode s : statements) {
            s.execute(virtualFrame);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (JSNode s : statements) {
            sb.append(s.toString() + "; ");
        }
        sb.append("}");
        return sb.toString();
    }
}
