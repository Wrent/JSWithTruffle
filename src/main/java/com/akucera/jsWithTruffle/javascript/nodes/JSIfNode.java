package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

/**
 * Created by akucera on 25.12.16.
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
            return this.thenNode.execute(virtualFrame);
        } else {
            return this.elseNode.execute(virtualFrame);
        }
    }

    private boolean testResult(VirtualFrame virtualFrame) {
        try {
            return this.testNode.executeBooleanNode(virtualFrame).getValue();
        } catch (UnexpectedResultException e) {
            //todo
            return false;
        }
    }
}
