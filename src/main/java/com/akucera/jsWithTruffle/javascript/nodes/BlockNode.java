package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

/**
 * Node representing block of statements (aka other nodes, used e.g. as body of a loop).
 */
public class BlockNode extends JSNode {
    public final List<JSNode> statements;

    public BlockNode(List<JSNode> statements) {
        this.statements = statements;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        //executes all statements
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
