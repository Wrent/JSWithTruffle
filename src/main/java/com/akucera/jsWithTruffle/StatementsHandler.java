package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralBooleanNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralNumberNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralStringNode;
import com.akucera.jsWithTruffle.javascript.nodes.*;
import com.akucera.jsWithTruffle.javascript.nodes.JSVarNodeGen;
import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import jdk.nashorn.internal.ir.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by akucera on 26.12.16.
 */
public class StatementsHandler {
    public static final Stack<FrameDescriptor> frameDescriptors = new Stack<>();
    static {
        frameDescriptors.add(new FrameDescriptor());
    }

    public static List<JSNode> handle(List<Statement> statements) throws UnknownSyntaxException {
        ArrayList<JSNode> list = new ArrayList<>();

        for (Statement s : statements) {
            switch (s.getClass().toString()) {
                case "class jdk.nashorn.internal.ir.VarNode":
                    list.add(handleVarNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.IfNode":
                    list.add(handleIfNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.WhileNode":
                    list.add(handleWhileNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.ExpressionStatement":
                    list.add(handleExpressionStatement(s));
                    break;
                default:
                    System.out.println(s.getClass());
                    throw new UnknownSyntaxException();
            }
        }
        System.out.println(list);
        return list;
    }

    private static JSNode handleWhileNode(Statement s) {
        return null;
    }

    private static JSNode handleExpressionStatement(Statement s) {
        return null;
    }

    private static JSNode handleIfNode(Statement s) {
        IfNode ifNode = (IfNode) s;
        ifNode.getTest();
        return null;
    }

    private static JSNode handleVarNode(Statement s) throws UnknownSyntaxException {
        VarNode var = (VarNode) s;
        IdentNode assignmentDest = var.getAssignmentDest();
        Expression assignmentSource = var.getAssignmentSource();

        //musime sestavit expression vpravo
        JSNode builtExpression = handleExpression(assignmentSource);

        //podle toho jaky typ ma promenna vytvorime JSnodes
        switch (assignmentSource.getType().toString()) {
            case "int":
            case "double":
                JSVarNode numberVar = JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return numberVar;
            case "boolean":
                JSVarNode booleanVar = JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return booleanVar;
            case "object<type=String>":
                JSVarNode stringVar = JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return stringVar;
            case "object":
                JSVarNode nullVar = JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return nullVar;
            case "object<type=Undefined>":
                JSVarNode undefinedVar = JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return undefinedVar;
            default:
                throw new UnknownSyntaxException(assignmentSource.getType().toString());
        }
    }

    private static JSNode handleExpression(Expression exp) throws UnknownSyntaxException {
        switch (exp.getClass().toString()) {
            case "class jdk.nashorn.internal.ir.LiteralNode$NumberLiteralNode":
                LiteralNumberNode literalNumberNode = getLiteralNumberNode(exp);
                return literalNumberNode;
            case "class jdk.nashorn.internal.ir.LiteralNode$StringLiteralNode":
                LiteralStringNode literalStringNode = getLiteralStringNode(exp);
                return literalStringNode;
            case "class jdk.nashorn.internal.ir.LiteralNode$NullLiteralNode":
                JSNull jsNull = new JSNull();
                return jsNull;
            case "class jdk.nashorn.internal.ir.LiteralNode$BooleanLiteralNode":
                LiteralBooleanNode literalBooleanNode = getLiteralBooleanNode(exp);
                return literalBooleanNode;
            case "class jdk.nashorn.internal.ir.IdentNode":
                JSUndefined jsUndefined = new JSUndefined();
                return jsUndefined;
            case "class jdk.nashorn.internal.ir.BinaryNode":
                break;
            default:
                System.out.println(exp.getClass());
                throw new UnknownSyntaxException();
        }

        return null;
    }

    private static LiteralStringNode getLiteralStringNode(Expression assignmentSource) {
        return new LiteralStringNode(new JSString(assignmentSource.toString()));
    }

    private static LiteralBooleanNode getLiteralBooleanNode(Expression assignmentSource) {
        return new LiteralBooleanNode(new JSBoolean(Boolean.parseBoolean(assignmentSource.toString())));
    }

    private static LiteralNumberNode getLiteralNumberNode(final Expression assignmentSource) {
        return new LiteralNumberNode(new JSNumber(new Number() {
            @Override
            public int intValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public long longValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public float floatValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public double doubleValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public String toString() {
                return "" + this.intValue();
            }
        }));
    }
}
