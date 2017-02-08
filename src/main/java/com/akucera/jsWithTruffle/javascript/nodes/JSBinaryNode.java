package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.operations.OpCode;
import com.akucera.jsWithTruffle.javascript.types.JSBoolean;
import com.akucera.jsWithTruffle.javascript.types.JSNumber;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.sun.org.apache.xpath.internal.operations.Bool;

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
        JSBoolean jsBoolean, leftBoolean, rightBoolean;;
        Number num;
        boolean bool;
        //System.out.println(this.getClass().getSimpleName().toString() + " executed");
        switch (token) {
            case PLUS:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                num = left.value.intValue() + right.value.intValue();

                number = new JSNumber(num);
                //System.out.println("returning "+number);
                return number;
            case GT:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                bool = left.value.intValue() > right.value.intValue();

                jsBoolean = new JSBoolean(bool);
                //System.out.println("returning "+jsBoolean);

                return jsBoolean;
            case MINUS:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                num = left.value.intValue() - right.value.intValue();

                number = new JSNumber(num);
                //System.out.println("returning "+number);
                return number;
            case TIMES:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                num = left.value.intValue() * right.value.intValue();

                number = new JSNumber(num);
                //System.out.println("returning "+number);
                return number;
            case DIV:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                num = left.value.intValue() / right.value.intValue();

                number = new JSNumber(num);
                //System.out.println("returning "+number);
                return number;
            case LT:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                bool = left.value.intValue() < right.value.intValue();

                jsBoolean = new JSBoolean(bool);
                //System.out.println("returning "+jsBoolean);

                return jsBoolean;
            case EQ:
                left = (JSNumber) lhs.execute(virtualFrame);
                right = (JSNumber) rhs.execute(virtualFrame);
                bool = left.value.intValue() == right.value.intValue();

                jsBoolean = new JSBoolean(bool);
                //System.out.println("returning "+jsBoolean);

                return jsBoolean;
            case AND:
                leftBoolean = (JSBoolean) lhs.execute(virtualFrame);
                rightBoolean = (JSBoolean) rhs.execute(virtualFrame);
                bool = leftBoolean.getValue() && rightBoolean.getValue();

                jsBoolean = new JSBoolean(bool);
                //System.out.println("returning "+jsBoolean);

                return jsBoolean;
            case OR:
                leftBoolean = (JSBoolean) lhs.execute(virtualFrame);
                rightBoolean = (JSBoolean) rhs.execute(virtualFrame);
                bool = leftBoolean.getValue() || rightBoolean.getValue();

                jsBoolean = new JSBoolean(bool);
                //System.out.println("returning "+jsBoolean);

                return jsBoolean;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (token) {
            case PLUS:
                return "(" + lhs.toString() + " + " + rhs.toString() + ")";
            case GT:
                return "(" + lhs.toString() + " > " + rhs.toString() + ")";
            case LT:
                return "(" + lhs.toString() + " < " + rhs.toString() + ")";
            case EQ:
                return "(" + lhs.toString() + " == " + rhs.toString() + ")";
            case MINUS:
                return "(" + lhs.toString() + " - " + rhs.toString() + ")";
            case TIMES:
                return "(" + lhs.toString() + " * " + rhs.toString() + ")";
            case DIV:
                return "(" + lhs.toString() + " / " + rhs.toString() + ")";
            case OR:
                return "(" + lhs.toString() + " || " + rhs.toString() + ")";
            case AND:
                return "(" + lhs.toString() + " && " + rhs.toString() + ")";

        }
        return "";
    }
}
