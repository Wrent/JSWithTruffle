package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

/**
 * Node representing an If statement
 */
public class JSIfNode extends JSNode {
    @Child private JSNode testNode;
    @Child private JSNode thenNode;
    @Child private JSNode elseNode;

    private final ConditionProfile conditionProfile =
            ConditionProfile.createBinaryProfile();

    public JSIfNode(JSNode testNode, JSNode thenNode,
                    JSNode elseNode) {
        this.testNode = testNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (this.conditionProfile.profile(this.testResult(virtualFrame))) {
            //execute if branch
            return this.thenNode.execute(virtualFrame);
        } else {
            //execute else branch
            return this.elseNode.execute(virtualFrame);
        }
    }

    /**
     * Test condition
     * @param virtualFrame
     * @return
     */
    private boolean testResult(VirtualFrame virtualFrame) {
        try {
            return this.testNode.executeBooleanNode(virtualFrame).getValue();
        } catch (UnexpectedResultException e) {
            //todo
            return false;
        }
    }

    @Override
    public String toString() {
        return "(if " + testNode + thenNode +" else "+ elseNode + ")";
    }
}
