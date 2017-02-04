package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

/**
 * Created by akucera on 29.1.17.
 */
public class JSWhileNode extends JSNode {
    @Child private JSNode testNode;
    @Child private JSNode loopNode;

    private final ConditionProfile conditionProfile =
            ConditionProfile.createBinaryProfile();

    public JSWhileNode(JSNode testNode, JSNode loopNode) {
        this.testNode = testNode;
        this.loopNode = loopNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        System.out.println(this.getClass().getSimpleName().toString() + " executed, executing loop");
        while (this.conditionProfile.profile(this.testResult(virtualFrame))) {
            this.loopNode.execute(virtualFrame);
        }

        return null;
    }

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
        return "(while " + testNode + loopNode +")";
    }
}
