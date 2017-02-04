package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.operations.OpCode;
import com.akucera.jsWithTruffle.javascript.types.JSBoolean;
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
        JSNumber left, right, number;
        Number num;
        boolean bool;
        System.out.println(this.getClass().getSimpleName().toString() + " executed");
        switch (token) {
            case PLUS:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                num = left.value.intValue() + right.value.intValue();

                number = new JSNumber(num);
                System.out.println("returning "+number);
                return number;
            case GT:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                bool = left.value.intValue() > right.value.intValue();

                JSBoolean jsBoolean = new JSBoolean(bool);
                System.out.println("returning "+jsBoolean);

                return jsBoolean;
        }
        //todo other ops

        return null;
    }

    @Override
    public String toString() {
        switch (token) {
            case PLUS:
                return "(" + lhs.toString() + "+" + rhs.toString() + ")";
            case GT:
                return "(" + lhs.toString() + ">" + rhs.toString() + ")";
        }
        //todo other ops
        return "";
    }
}
