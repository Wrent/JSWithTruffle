package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.operations.OpCode;
import com.akucera.jsWithTruffle.javascript.types.JSNumber;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Created by akucera on 28.1.17.
 */
public class JSBinaryNode extends JSNode {

    public final JSNode lhs;
    public final JSNode rhs;
    public final OpCode token;

    public JSBinaryNode(JSNode lhs, JSNode rhs, OpCode token) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.token = token;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        switch (token) {
            case PLUS:
                JSNumber left = (JSNumber) lhs.execute(virtualFrame);
                JSNumber right = (JSNumber) rhs.execute(virtualFrame);
                Number num = left.value.intValue() + right.value.intValue();

                JSNumber number = new JSNumber(num);
                return number;
        }
        //todo other ops

        return null;
    }

    @Override
    public String toString() {
        switch (token) {
            case PLUS:
                return "(" + lhs.toString() + "+" + rhs.toString() + ")";
        }
        //todo other ops
        return "";
    }
}
