package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSArray;
import com.akucera.jsWithTruffle.javascript.types.JSNumber;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node, which assigns to array to given index.
 */
public class AssignToArrayNode extends JSNode {
    private JSNode symbol;
    private JSNode index;
    private JSNode rhs;

    public AssignToArrayNode(JSNode symbolNode, JSNode index, JSNode builtExpression) {
        this.symbol = symbolNode;
        this.index = index;
        this.rhs = builtExpression;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        JSArray<Object> jsArray = (JSArray<Object>) symbol.execute(virtualFrame);
        int i = ((JSNumber) index.execute(virtualFrame)).getInt();
        //adds to the array on index
        jsArray.add(i, rhs.execute(virtualFrame));
        return jsArray.get(i);
    }

    @Override
    public String toString() {
        return "AssignToArrayNode{" +
                "symbol=" + symbol +
                ", index=" + index +
                ", rhs=" + rhs +
                '}';
    }
}
